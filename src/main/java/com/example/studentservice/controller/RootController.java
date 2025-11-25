package  com.example.studentservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
public class RootController {
    
    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Students Service");
        response.put("status", "running");
        response.put("endpoints", List.of(
            "/api/students",
            "/api/students/{id}"
        ));
        return response;
    }
}