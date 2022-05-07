package org.example.service;

import org.example.dto.Redisdata;
import org.example.model.ChangePasswordRequest;
import org.example.model.DAOUser;
import org.example.repository.userDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class DefaultUserService implements UserService{

    private static  final Logger logger= LoggerFactory.getLogger(DefaultUserService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private userDao userRepository;

    @Autowired
    private userDao userDao;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public DefaultUserService(){};

    public void setUserRepository(userDao userRepository)
    {
        this.userRepository=userRepository;
    }

    @Override
    public List<DAOUser> GetAllUsers() {
        return userRepository.findAll();
    }

    public Boolean saveToRedis(Redisdata redisdata) {

        redisdata.setTtl(5l);
        logger.info("<================ Storing user detail to redis : {}",
                LocalDateTime.now() + "  =================== >");
        redisService.saveUserInfo( redisdata.getToken(),
                (Object) redisdata,redisdata.getTtl());
        Object object = redisService.findUserInfo(redisdata.getToken());
        logger.info(object+"");
        logger.info("<================ User details stored on redis {}",
                LocalDateTime.now() + "  =================== >");
        return true;
    }

    @Transactional
    public void logoutUser(HttpServletRequest request) throws Exception {

        logger.info("User Service called. For Logout : " + request.getRequestURL());

        String token=request.getHeader("token");

        if (!StringUtils.isEmpty(token)) {
            redisService.deleteUserInfo(token);
        }

    }

    @Override
    public ResponseEntity<DAOUser> getoneUser(Long id) {
        Optional<DAOUser> user = userRepository.findById(id);
        if(user.isPresent())
            return new ResponseEntity<>(user.get(),HttpStatus.OK);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Id not found");
    }

    @Override
    public ResponseEntity<DAOUser> UpdateUser(DAOUser daoUser) {
        return new ResponseEntity<>(userRepository.saveAndFlush(daoUser),HttpStatus.OK);
    }

    @Override
    public void deleteUser(Long id) {
            userRepository.deleteById(id);
    }


}

