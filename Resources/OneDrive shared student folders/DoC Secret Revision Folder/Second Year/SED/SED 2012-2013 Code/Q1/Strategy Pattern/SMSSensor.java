/**
 * Created by David on 28/03/2016.
 */
public class SMSSensor implements Sensor {
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
        Sms sms = new Sms(message);
        sms.send("+4407983452627");
    }
}
