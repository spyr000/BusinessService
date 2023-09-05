package cs.vsu.businessservice.contentnegotiation.config;

import com.google.gson.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Type;
import java.util.List;

@Configuration
@EnableWebMvc
class ContentNegotiationConfig implements WebMvcConfigurer {
    @Value("${spring.mvc.contentnegotiation.default-type}")
    private String defaultContentNegotiationType;
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(true)
                .defaultContentType(MediaType.parseMediaType(defaultContentNegotiationType))
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);

    }



}
