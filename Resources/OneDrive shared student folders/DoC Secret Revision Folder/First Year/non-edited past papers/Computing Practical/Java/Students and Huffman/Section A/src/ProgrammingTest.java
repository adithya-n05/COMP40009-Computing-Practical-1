import java.util.Arrays;
import java.util.HashSet;

public class ProgrammingTest {

  public static void main(String[] args) {
    Academic rr = new Academic("Ricardo Rodriguez");
    Academic ib = new Academic("Ismael Bento");
    Student gg4 = new Undergraduate("gg4", "gg4", "gg4", rr);
    Student pr3 = new Undergraduate("pr3", "pr3", "pr3", ib);
    Student te2 = new Postgraduate("te2", "te2", "te2", rr);
    Student yj34 = new Postgraduate("yj34", "yj34", "yj34", ib);
    Student jj8 = new Postgraduate("jj8", "jj8", "jj8", ib);
    Course course = new Course(
        "Course", new HashSet<>(Arrays.asList(gg4, pr3, te2, yj34, jj8))
    );
    new Notifier(course.getPostgraduates("Ismael Bento"))
        .doNotifyAll("You have been notified!");
  }

}