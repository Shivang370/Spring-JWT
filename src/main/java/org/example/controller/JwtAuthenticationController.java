package org.example.controller;

import org.example.config.JwtTokenUtil;
import org.example.dto.Redisdata;
import org.example.model.JwtRequest;
import org.example.model.JwtResponse;
import org.example.model.UserDTO;
import org.example.service.DefaultUserService;
import org.example.service.JwtUserDetailsService;
import org.example.service.UserDetail;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;


    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private DefaultUserService defaultUserService;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        String newtoken=token.substring(0,8);

        System.out.println("token :"+token);
        System.out.println("newtoken :"+newtoken);

        Redisdata redisdata = new Redisdata(userDetails.getUsername(),token);

        defaultUserService.saveToRedis(redisdata);


        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    private void authenticate(String username, String password) throws Exception {
        System.out.println(username+" : "+password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}