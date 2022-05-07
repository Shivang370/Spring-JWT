package org.example.service;
import org.example.model.ChangePasswordRequest;
import org.example.model.DAOUser;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    List<DAOUser> GetAllUsers();

    void logoutUser(HttpServletRequest request) throws Exception;

    ResponseEntity<DAOUser> getoneUser(Long id);

    ResponseEntity<DAOUser> UpdateUser(DAOUser daoUser);

    void deleteUser(Long id);

}
