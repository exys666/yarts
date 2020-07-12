package exys666.yarts.web;

import exys666.yarts.model.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicksController {

    @PostMapping("/ticks")
    public ResponseEntity<?> addTick(@Validated @RequestBody Tick tick) {
        return ResponseEntity.created(null).build();
    }
}
