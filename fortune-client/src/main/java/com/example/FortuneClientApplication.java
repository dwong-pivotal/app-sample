package com.example;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class FortuneClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FortuneClientApplication.class, args);
    }
}

@RestController
class UiController {

    @Autowired
    FortuneServiceFeign service;

    @RequestMapping("/random")
    public Fortune randomFortune() {
        return service.randomFortune();
    }
}

@Data
class Fortune {
    private Long id;
    private String text;
    public Fortune(Long id, String text){
        this.id=id;
        this.text=text;
    }
    public Fortune(){
    }
}

@Component
@FeignClient(value = "fortune-service", fallback = FortuneServcieFeignFallback.class)
interface FortuneServiceFeign{
    @RequestMapping(value="/random", method = RequestMethod.GET)
    Fortune randomFortune();
}

@Component
class FortuneServcieFeignFallback implements FortuneServiceFeign{

    @Autowired
    FortuneProperties fortuneProperties;

    public Fortune randomFortune(){
        return new Fortune(42L, fortuneProperties.getFallbackFortune());
    }
}

@Component
//@ConfigurationProperties(prefix = "fortune")
@RefreshScope @Data
class FortuneProperties {
    private String fallbackFortune = "Your future is unclear.";
}
