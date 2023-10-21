/**
 * Created by David on 28/03/2016.
 */
public class SmsAlert {
    private static final double THRESHOLD = 10.0;
    private final MoniteringSystem moniteringSystem;

    public SmsAlert(MoniteringSystem moniteringSystem) {
        this.moniteringSystem = moniteringSystem;
    }

    public void run() {
        for (Sensor sensor : moniteringSystem.allSensors()) {
            if (sensor.active() && sensor.level() > THRESHOLD) {
                String message = "Alert: sensor " + sensor.description()
                        + " is reading too high.";
                Sms sms = new Sms(message);
                sms.send("+447770123456");
            }
        }
    }

}

