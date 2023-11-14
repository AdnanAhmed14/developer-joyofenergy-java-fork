package uk.tw.energy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.MeterReadings;
import uk.tw.energy.exception.MeterReadingNotPresentException;
import uk.tw.energy.service.MeterReadingService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/readings")
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    public MeterReadingController(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    @PostMapping("/store")
    public ResponseEntity<String> storeReadings(@RequestBody MeterReadings meterReadings) {
        try {
            if (!meterReadingService.isMeterReadingsValid(meterReadings)) {
//                throw new InvalidMeterReadingException("Invalid meter readings");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid meter readings");
            }
            meterReadingService.storeReadings(meterReadings.smartMeterId(), meterReadings.electricityReadings());
            return ResponseEntity.ok("Readings stored successfully");
        } catch (Exception e) {
            log.error("Error storing meter readings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error storing meter readings");
        }
    }

    @GetMapping("/read/{smartMeterId}")
    public ResponseEntity<?> readReadings(@PathVariable String smartMeterId) {
        Optional<List<ElectricityReading>> readings = meterReadingService.getReadings(smartMeterId);
        if(readings.isPresent() && !readings.get().isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(readings.get());
        }else {
//            throw new MeterReadingNotPresentException("No meter reading present for Meter Id: "+smartMeterId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No meter reading present for Meter Id: "+smartMeterId);
        }
    }
}
