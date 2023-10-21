/**
 * Created by David on 28/03/2016.
 */
public class EmailAlert {

    private static final double THRESHOLD = 10.0;
    private final MoniteringSystem moniteringSystem;

    public EmailAlert(MoniteringSystem moniteringSystem) {
        this.moniteringSystem = moniteringSystem;
    }

    public void run() {
        for (Sensor sensor : moniteringSystem.allSensors()) {
            if (sensor.active() && sensor.level() > THRESHOLD) {
                String message = "Alert: sensor " + sensor.description()
                                    + " is reading too high.";
                Email email = new Email("too@example.com", message);
                email.send();
            }
        }
    }

}
