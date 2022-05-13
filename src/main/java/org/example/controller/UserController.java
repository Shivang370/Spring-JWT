package org.example.controller;

import org.example.config.JwtTokenUtil;
import org.example.model.ChangePasswordRequest;
import org.example.model.DAOUser;
import org.example.model.EmailRequest;
import org.example.repository.userDao;
import org.example.response.ResetPasswordResponse;
import org.example.response.ResponseCode;
import org.example.service.EmailService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private userDao userDao;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping("/getUsers")
    public List<DAOUser> getDetails()
    {
        return userService.GetAllUsers();
    }

    @RequestMapping("/getoneUser/{id}")
    public ResponseEntity<DAOUser> getoneUser(@PathVariable Long id)
    {
        return userService.getoneUser(id);
    }

    @RequestMapping(value = "/signOff", method = RequestMethod.GET)
    public ResponseEntity<?> setLogout(final HttpServletRequest request,
                                       final HttpServletResponse response) {
        try {
            userService.logoutUser(request);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return ResponseEntity.ok("logout success");
    }

    @RequestMapping(value ="/changepassword",method = RequestMethod.POST)
    public @ResponseBody ResetPasswordResponse resetPassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {

        ResetPasswordResponse response = new ResetPasswordResponse();
        //Getting Token From Headers
        String token=request.getHeader("token");
        //Extracting Logged User's name
        String username=jwtTokenUtil.getUsernameFromToken(token);

        //Retrieving All information of User based on Name
        DAOUser CurrentUser=userDao.findByUsername(username);

        if(bcryptEncoder.matches(changePasswordRequest.getNewpassword(),CurrentUser.getPassword())){
            response.setStatus(ResponseCode.BAD_REQUEST);
            response.setDescription("Old password and new password can't be same");
        }

        else if(bcryptEncoder.matches(changePasswordRequest.getCurrentpassword(),CurrentUser.getPassword()))
        {
            String encryptedPassword = bcryptEncoder.encode(changePasswordRequest.getNewpassword());
            CurrentUser.setPassword(encryptedPassword);
            userDao.saveAndFlush(CurrentUser);

            response.setStatus(ResponseCode.OK);
            response.setDescription("Password reset successfully");
        }

        else {
            response.setStatus(ResponseCode.BAD_REQUEST);
            response.setDescription("Old password didn't match");
        }
        return response;
    }

    @RequestMapping(value = "/updateUser",method = RequestMethod.PUT)
    public ResponseEntity<DAOUser> UpdateUserInfo(@RequestBody DAOUser daoUser)
    {
        return  userService.UpdateUser(daoUser);
    }
    @RequestMapping(value = "/deleteUser/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/sendemail",method =RequestMethod.POST)
    public ResponseEntity<?> ResetPassword(@RequestBody EmailRequest emailRequest, HttpSession session)
    {
        boolean success=emailService.sendEmail(emailRequest.getSubject(),emailRequest.getMessage(),emailRequest.getTo());
        if(success)
        {
            session.setAttribute("message","Mail has been sent Successfully !!");
            return ResponseEntity.ok("Mail has been sent Successfully !!");
        }
        else
        {
            session.setAttribute("message","Mail not sent !!...Contact Administrator");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Mail not sent !!...Contact Administrator");
        }
    }
}
