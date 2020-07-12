package exys666.yarts.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/statistics/{instrument}")
    public ResponseEntity<?> getStatistics(@PathVariable String instrument) {
        return ResponseEntity.ok(null);
    }
}
