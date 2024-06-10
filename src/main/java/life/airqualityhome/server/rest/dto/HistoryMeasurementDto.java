package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryMeasurementDto {
    private Long baseId;
    private String baseName;
    private List<ChartDataDto> data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartDataDto {
        private String type;
        private String sensorName;
        private String name;
        private double minAlarm;
        private double maxAlarm;
        private double sensorMinValue;
        private double sensorMaxValue;
        private List<ChartDataPoint> series;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartDataPoint {
        private Double value;
        private LocalDateTime name;
    }
}
