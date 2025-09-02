package com.TripRider.TripRider.service.ride;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Component
@RequiredArgsConstructor
public class LocalRouteImageStorage implements RouteImageStorage {

    @Value("${uploads.rides.path:${user.dir}/uploads/rides}")
    private String baseDir;

    @Override
    public String store(Long rideId, MultipartFile file) {
        try {
            // 절대경로로 보정
            Path base = Paths.get(baseDir);
            if (!base.isAbsolute()) {
                base = Paths.get(System.getProperty("user.dir")).resolve(baseDir);
            }

            // rides/{rideId}/snapshot.png
            Path dir = base.resolve(String.valueOf(rideId));
            Files.createDirectories(dir);                    // 부모 폴더까지 생성
            Path path = dir.resolve("snapshot.png");

            // 저장
            file.transferTo(path.toFile());

            // 브라우저에서 접근할 URL (정적 매핑을 /uploads/** 로 건다는 가정)
            // baseDir = .../uploads/rides -> URL은 /uploads/rides/{rideId}/snapshot.png
            return "/uploads/rides/" + rideId + "/snapshot.png";
        } catch (Exception e) {
            throw new RuntimeException("store failed", e);
        }
    }
}

