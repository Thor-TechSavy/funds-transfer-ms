package com.quicktransfer.fundstransfer.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients({"com.quicktransfer.fundstransfer.client"})
public class FeignConfig {


//    @Bean
//    public Encoder multipartFormEncoder() {
//        return new SpringFormEncoder(new SpringEncoder(() -> new HttpMessageConverters(new RestTemplate().getMessageConverters())));
//    }

//    @Bean
//    public FeignErrorDecoder customErrorDecoder() {
//        return new FeignErrorDecoder();
//    }
}
