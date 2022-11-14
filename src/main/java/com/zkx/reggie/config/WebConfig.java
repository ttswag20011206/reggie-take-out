package com.zkx.reggie.config;

import com.zkx.reggie.commons.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    //设置静态支援映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
    //扩展mvc框架的消息转换器

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用JackSon将java对象转成json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器追加到我们mvc的容器当时
        converters.add(0,messageConverter);
    }
}
