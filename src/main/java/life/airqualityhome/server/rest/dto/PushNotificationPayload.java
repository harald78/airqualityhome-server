package life.airqualityhome.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationPayload {
    String title;
    String body;
    String icon;
    String image;
    boolean silent;
    int[] vibrate;
    Map<String, Object> data;
    List<PushNotificationAction> actions;
}
