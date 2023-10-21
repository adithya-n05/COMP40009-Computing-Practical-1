package Q1;

import java.util.Objects;

public class Student {

  private final String name;

  public Student(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  @Override
  public String toString() {
    return "Student [ " + name + " ]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Student student = (Student) o;
    return name.equals(student.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
