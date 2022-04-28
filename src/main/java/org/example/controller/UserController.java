package org.example.controller;

import org.example.model.DAOUser;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

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

}
