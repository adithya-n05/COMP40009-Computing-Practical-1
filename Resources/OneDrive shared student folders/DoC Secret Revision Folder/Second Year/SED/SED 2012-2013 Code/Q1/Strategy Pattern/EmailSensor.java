/**
 * Created by David on 28/03/2016.
 */
public class EmailSensor implements Sensor {

    @Override
    public boolean active() {
        return false;
    }

    @Override
    public double level() {
        return 0;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void alert() {
        String message = "Alert: sensor " + description()
                + " is reading too high.";
        Email email = new Email("tom@example.com", message);
        email.send();
    }
}
