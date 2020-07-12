package exys666.yarts.web;

import exys666.yarts.model.Statistics;
import exys666.yarts.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public Statistics getStatistics() {
        return statisticsService.getGlobalStats();
    }

    @GetMapping("/statistics/{instrument}")
    public Statistics getStatistics(@PathVariable String instrument) {
        return statisticsService.getInstrumentStats(instrument);
    }
}
