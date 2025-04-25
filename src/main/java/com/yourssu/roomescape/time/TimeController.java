package com.yourssu.roomescape.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public List<Time> list() {
        return timeService.findAll();
    }

    @PostMapping("/times")
    public ResponseEntity<Time> create(@RequestBody Time time) {
        if (time.getTime_value() == null || time.getTime_value().isEmpty()) {
            throw new RuntimeException();
        }

        Time newTime = timeService.save(time);
        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(newTime);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        return ResponseEntity.ok(timeService.getAvailableTime(date, themeId));
    }
}
