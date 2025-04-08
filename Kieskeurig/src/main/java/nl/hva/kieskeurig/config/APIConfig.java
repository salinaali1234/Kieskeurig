package nl.hva.kieskeurig.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class APIConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
//@Value("${kieskeurig.frontend.url}")
//    private String frontendUrl;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        System.out.println("frontendUrl: " + frontendUrl);
//        System.out.println("added cors mappings");
//
//        registry.addMapping("/api/**")
//                .allowedOriginPatterns(
//                        frontendUrl
//                        )
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
//    }
//}
