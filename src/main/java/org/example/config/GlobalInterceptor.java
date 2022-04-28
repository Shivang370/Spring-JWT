package org.example.config;

import org.example.constants.SystemConstants;
import org.example.dto.Redisdata;
import org.example.exception.UnauthorizedApiSessionException;
import org.example.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class GlobalInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);

    @Value("${redis.events.ttl}")
    private long eventsLifeTime;

    @Value("${excluded.urls}")
    private String urls;

    Set<String> excludedUrls = new HashSet<String>();

    @Autowired
    private RedisService redisService;

    public GlobalInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Global interceptor => preHandle called. For URL : " + request.getRequestURL()
                + " Date" + new Date());
        String url = request.getServletPath();
        request.setAttribute("START_TIME", LocalDateTime.now());
        logger.info("Requested URL is : " + url);
        logger.info("Requested method is : " + request.getMethod());
        logger.info("Requested Header Token is : " + request.getHeader("token"));
        if(!isExculdedUrl(url)) {
            authenticateToken(request.getHeader("token"), request);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    public Redisdata authenticateToken(String token,HttpServletRequest request)throws Exception {
        Redisdata redisdata = null;
        if (token != null && !token.isEmpty()) {
            logger.debug("getting data from redis for uuid {}", token);
            logger.info("Getting Key from Redis :", SystemConstants.USER_REDIS_KEY);
            Object object = redisService.findUserInfo(token);
            if (object != null) {
                redisdata = (Redisdata) object;
                long diff = new Date().getTime() - redisdata.getLastAccessTime().getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                if (diffMinutes > eventsLifeTime) {
                    redisService.deleteUserInfo(token);
                    throw new UnauthorizedApiSessionException("INVALIDREQ",
                            "Request not valid. Authorization failed.");
                }
            } else {
                throw new UnauthorizedApiSessionException("INVALIDREQ",
                        "Request not valid. Authorization failed.");
            }
            redisdata.setLastAccessTime(new Date());
            logger.debug("updating data in redis for uuid {}", token);

            redisService.updateUserInfo(token,redisdata,redisdata.getTtl());
            return redisdata;
        }
        return redisdata;
    }
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object object, ModelAndView model)
            throws Exception {
        logger.info("Handler execution is complete");
    }

    public Boolean isExculdedUrl(String url) {
        excludedUrls.clear();
        String excUrlStr = urls;
        if (excUrlStr != null && !excUrlStr.isEmpty()) {
            String[] excUrls = excUrlStr.split(",");
            if (excUrls != null && excUrls.length > 0)
                excludedUrls.addAll(Arrays.asList(excUrls));
        }

        if (excludedUrls.contains(url))
            return true;
        return false;
    }
}
