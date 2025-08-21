package com.TripRider.TripRider.util;

public class GeoUtil {
    // 제주도 대략 BBOX (간단 필터)
    public static boolean isInJeju(double lat, double lng) {
        return (lat >= 33.10 && lat <= 33.60) && (lng >= 126.10 && lng <= 126.99);
    }

    // 하버사인 거리(km)
    public static double haversineKm(double lat1,double lon1,double lat2,double lon2){
        double R=6371.0088;
        double dLat=Math.toRadians(lat2-lat1), dLon=Math.toRadians(lon2-lon1);
        double a=Math.sin(dLat/2)*Math.sin(dLat/2)+
                Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*
                        Math.sin(dLon/2)*Math.sin(dLon/2);
        return 2*R*Math.asin(Math.sqrt(a));
    }
}
