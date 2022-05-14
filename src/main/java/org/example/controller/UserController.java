package org.example.controller;

import org.example.config.JwtTokenUtil;
import org.example.model.*;
import org.example.repository.otpDao;
import org.example.repository.userDao;
import org.example.response.ResponseCode;
import org.example.response.ResponseWO;
import org.example.service.EmailService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Random;

@RestController @CrossOrigin(origins = "*") public class UserController {

    @Autowired private UserService userService;

    @Autowired private EmailService emailService;

    @Autowired private userDao userDao;

    @Autowired private otpDao otpDao;

    @Autowired private PasswordEncoder bcryptEncoder;

    @Autowired private JwtTokenUtil jwtTokenUtil;

    @RequestMapping("/getUsers") public List<DAOUser> getDetails() {
        return userService.GetAllUsers();
    }

    @RequestMapping("/getoneUser/{id}")
    public ResponseEntity<DAOUser> getoneUser(@PathVariable Long id) {
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

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST) public @ResponseBody
    ResponseWO ChangePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
        HttpServletRequest request) {

        ResponseWO response = new ResponseWO();
        //Getting Token From Headers
        String token = request.getHeader("token");
        //Extracting Logged User's name
        String username = jwtTokenUtil.getUsernameFromToken(token);

        //Retrieving All information of User based on Name
        DAOUser CurrentUser = userDao.findByUsername(username);

        if (bcryptEncoder.matches(changePasswordRequest.getNewpassword(),
            CurrentUser.getPassword())) {
            response.setStatus(ResponseCode.BAD_REQUEST);
            response.setDescription("Old password and new password can't be same");
        } else if (bcryptEncoder.matches(changePasswordRequest.getCurrentpassword(),
            CurrentUser.getPassword())) {
            String encryptedPassword = bcryptEncoder.encode(changePasswordRequest.getNewpassword());
            CurrentUser.setPassword(encryptedPassword);
            userDao.saveAndFlush(CurrentUser);

            response.setStatus(ResponseCode.OK);
            response.setDescription("Password reset successfully");
        } else {
            response.setStatus(ResponseCode.BAD_REQUEST);
            response.setDescription("Old password didn't match");
        }
        return response;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
    public ResponseEntity<DAOUser> UpdateUserInfo(@RequestBody DAOUser daoUser) {
        return userService.UpdateUser(daoUser);
    }

    @RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/sendemail", method = RequestMethod.POST) public @ResponseBody
    ResponseWO ResetPassword(@RequestBody EmailRequest emailRequest) {
        ResponseWO response = new ResponseWO();
        String email = emailRequest.getTo();
        DAOUser user = userDao.findByEmail(email);
        if (user != null) {
            Random random = new Random();
            int otp = 100000 + random.nextInt(900000);
            OtpDetails  otpDetails = new OtpDetails();
            otpDetails.setOtp(otp);
            otpDetails.setEmail(email);
            otpDao.saveAndFlush(otpDetails);
            String message = emailRequest.getMessage() + otp;
            boolean success =
                emailService.sendEmail(emailRequest.getSubject(), message, emailRequest.getTo());
            if (success) {
                response.setStatus(ResponseCode.OK);
                response.setDescription("Mail Sent Successfully !!");
            } else {
                response.setStatus(ResponseCode.ERROR);
                response.setDescription("Bad Credentials !!");
            }
        } else {
            response.setStatus(ResponseCode.BAD_REQUEST);
            response.setDescription("Email does not exist !!");
        }
        return response;
    }

    @RequestMapping(value = "/verifyotp", method = RequestMethod.POST) public @ResponseBody
    ResponseWO ValidateOTP(@RequestBody OtpRequest otp, HttpServletRequest request) {
        ResponseWO response = new ResponseWO();
        String email = request.getHeader("email");
        OtpDetails user=otpDao.findByEmail(email);
        if (user.getOtp().equals(otp.getOtp())) {
            response.setStatus(ResponseCode.OK);
            response.setDescription("OTP Verified !!");
        } else {
            response.setStatus(ResponseCode.BAD_REQUEST);
            response.setDescription("OTP Verification Failed !!");
        }
        return response;
    }

    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST) public @ResponseBody
    ResponseWO ResetPassword(@RequestBody ChangePasswordRequest changePasswordRequest,
        HttpServletRequest request) {
        ResponseWO response=new ResponseWO();
        String email=request.getHeader("email");
        DAOUser user=userDao.findByEmail(email);
        if(user!=null && changePasswordRequest.getNewpassword()!=null)
        {
            String newpass=bcryptEncoder.encode(changePasswordRequest.getNewpassword());
            System.out.println(newpass);
            user.setPassword(newpass);
            userDao.saveAndFlush(user);
            response.setStatus(ResponseCode.OK);
            response.setDescription("Password reset Successfull ");
        }
        else
        {
            response.setStatus(ResponseCode.BAD_REQUEST);
            response.setDescription("Invalid Request ");
        }
        return response;
    }

}
