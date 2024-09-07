package reservationsystem.reservationcar;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handler for car images
        registry.addResourceHandler("/car/**")
                .addResourceLocations("file:" + uploadPath + "/car/");

        // Handler for warning images
        registry.addResourceHandler("/warning/**")
                .addResourceLocations("file:" + uploadPath + "/warning/");

        registry.addResourceHandler("/post/**")
                .addResourceLocations("file:" + uploadPath + "/post/");
    }

}