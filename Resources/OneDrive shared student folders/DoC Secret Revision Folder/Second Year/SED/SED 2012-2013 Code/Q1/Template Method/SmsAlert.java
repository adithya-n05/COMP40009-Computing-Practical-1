
public class SmsAlert extends Alerter {

    public SmsAlert(MoniteringSystem moniteringSystem) {
        super(moniteringSystem);
    }

    @Override
    public void alert(Sensor s) {
        String message = "Alert: sensor " + s.description()
                + " is reading too high.";
        Sms sms = new Sms(message);
        sms.send("+4407983452627");
    }

}

