package exys666.yarts.web;

import exys666.yarts.model.Tick;
import exys666.yarts.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicksController {

    private final StatisticsService statisticsService;

    public TicksController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostMapping("/ticks")
    public ResponseEntity<?> addTick(@Validated @RequestBody Tick tick) {
        var added = statisticsService.add(tick);
        return ResponseEntity.status(added ? HttpStatus.CREATED : HttpStatus.NO_CONTENT).build();
    }
}
