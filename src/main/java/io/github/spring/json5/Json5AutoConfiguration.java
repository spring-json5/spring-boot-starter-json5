package io.github.spring.json5;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

import java.util.List;

@AutoConfiguration
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class Json5AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverter<Object> json5HttpMessageConverter() {
        return new Json5HttpMessageConverter();
    }

    @Bean
    public WebMvcConfigurer json5WebMvcConfigurer(HttpMessageConverter<Object> json5HttpMessageConverter) {
        return new WebMvcConfigurer() {
            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(json5HttpMessageConverter);
            }
        };
    }
}
