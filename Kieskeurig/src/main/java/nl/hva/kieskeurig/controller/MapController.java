package nl.hva.kieskeurig.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * REST controller handles geographic data (GeoJSON) for provinces.
 */
@RestController
@RequestMapping("/api/map")
public class MapController {

    @GetMapping("/map")
    public ResponseEntity<?> getGeoJson() {
        try {
            Resource resource = new ClassPathResource("provinces.geojson");
            String geoJson = new String(resource.getInputStream().readAllBytes());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(geoJson);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Unable to load GeoJSON: " + e.getMessage());
        }
    }
}
