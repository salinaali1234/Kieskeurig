package nl.hva.kieskeurig.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @GetMapping("/map")
    public ResponseEntity<String> getGeoJson() throws IOException {
        Resource resource = new ClassPathResource("provinces.geojson");
        String geoJson = new String(resource.getInputStream().readAllBytes());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(geoJson);
    }
}
