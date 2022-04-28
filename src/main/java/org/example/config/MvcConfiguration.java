package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

  @Autowired
  GlobalInterceptor globalInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry)
  {
    registry.addInterceptor(globalInterceptor);
  }
}