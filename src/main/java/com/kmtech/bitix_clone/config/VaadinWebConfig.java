//package com.kmtech.bitix_clone.config;
//
//import com.vaadin.flow.spring.VaadinConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class VaadinWebConfig implements WebMvcConfigurer {
//
//    private final VaadinConfigurationProperties vaadinProperties;
//
//    public VaadinWebConfig(VaadinConfigurationProperties vaadinProperties) {
//        this.vaadinProperties = vaadinProperties;
//    }
//
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.addPathPrefix(vaadinProperties.getUrlMapping(), c -> false); // Отключаем Vaadin для всех маршрутов
//    }
//}
