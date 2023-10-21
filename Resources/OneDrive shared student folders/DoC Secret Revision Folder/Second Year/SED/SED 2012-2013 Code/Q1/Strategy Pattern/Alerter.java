
public class Alerter {

    private static final double THRESHOLD = 10.0;
    private final MoniteringSystem moniteringSystem;

    public Alerter(MoniteringSystem moniteringSystem) {
        this.moniteringSystem = moniteringSystem;
    }

    public void run() {
        for (Sensor sensor : moniteringSystem.allSensors()) {
            if (sensor.active() && sensor.level() > THRESHOLD) {
                sensor.alert();
            }
        }
    }


}
