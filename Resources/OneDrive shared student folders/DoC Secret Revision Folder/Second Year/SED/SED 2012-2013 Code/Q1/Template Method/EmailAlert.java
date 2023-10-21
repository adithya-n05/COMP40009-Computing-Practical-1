/**
 * Created by David on 28/03/2016.
 */
public class EmailAlert extends Alerter {

    public EmailAlert(MoniteringSystem moniteringSystem) {
        super(moniteringSystem);
    }

    @Override
    public void alert(Sensor s) {
        String message = "Alert: sensor " + s.description()
                + " is reading too high.";
        Email email = new Email("tom@example.com", message);
        email.send();
    }


}
