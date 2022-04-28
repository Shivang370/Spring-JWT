package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication
public class Application extends WebMvcAutoConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class,args);
        logger.info("#### ------------------------ ####");
        logger.info("####     Spring JWT Started     ####");
        logger.info("#### ------------------------ ####");
    }

}
