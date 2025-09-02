package com.TripRider.TripRider.controller.ride;

import com.TripRider.TripRider.domain.user.User;
import com.TripRider.TripRider.dto.ride.*;
import com.TripRider.TripRider.service.ride.RideService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;
    private final ObjectMapper objectMapper;

    @PostMapping("/start")
    public ResponseEntity<StartRideResponse> start(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(rideService.start(user));
    }

    @PostMapping("/{rideId}/points")
    public ResponseEntity<Void> append(@PathVariable Long rideId,
                                       @RequestBody RidePointRequest request,
                                       @AuthenticationPrincipal User user) {
        rideService.appendPoints(rideId, user, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{rideId}/finish", consumes = "multipart/form-data")
    public ResponseEntity<RideSummaryDto> finish(@PathVariable Long rideId,
                                                 @RequestPart(value = "body", required = false) String bodyStr, // ← String으로 받기
                                                 @RequestPart(value = "snapshot", required = false) MultipartFile snapshot,
                                                 @AuthenticationPrincipal User user) {
        FinishRideRequest body = new FinishRideRequest(); // title/memo 없을 수도 있으니 기본 객체
        try {
            if (bodyStr != null && !bodyStr.isBlank()) {
                body = objectMapper.readValue(bodyStr, FinishRideRequest.class); // ← 직접 JSON 파싱
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // 잘못된 JSON이면 400 반환
        }
        return ResponseEntity.ok(rideService.finish(rideId, user, body, snapshot));
    }

    @GetMapping
    public ResponseEntity<List<RideSummaryDto>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(rideService.list(user));
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideDetailDto> detail(@PathVariable Long rideId,
                                                @RequestParam(defaultValue="false") boolean withPolyline,
                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(rideService.detail(rideId, user, withPolyline));
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<StatsSummaryDto> summary(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(rideService.summary(user));
    }
}
