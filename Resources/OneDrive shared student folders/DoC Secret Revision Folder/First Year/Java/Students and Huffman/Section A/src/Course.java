import java.util.Set;
import java.util.stream.Collectors;

public class Course {

  private String name;
  private Set<Student> students;

  public Course(String name, Set<Student> students) {
    this.name = name;
    this.students = students;
  }

  public Set<Postgraduate> getPostgraduates(String supervisor) {
    return students.stream().filter(student -> student instanceof Postgraduate
        && ((Postgraduate) student).getSupervisor().getName().equals(supervisor)
    ).map(Postgraduate.class::cast).collect(Collectors.toSet());
  }

}