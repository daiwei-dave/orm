package com.github.basic.config;

/**
 * Created by daiwei on 2018/1/23.
 */
public class Configuration {
     private Environment environment;

     public Configuration(Environment environment) {
          this.environment = environment;
     }

     public Environment getEnvironment() {
          return environment;
     }

     public void setEnvironment(Environment environment) {
          this.environment = environment;
     }
}
