package Q1;

import java.util.HashMap;
import java.util.Map;

public class MarkingSystem {

  private static MarkingSystem instance = new MarkingSystem();

  public static MarkingSystem getInstance(){
    return instance;
  }

  private final Map<Student, StudentRecord> allStudentMarks;

  private MarkingSystem() {
    allStudentMarks = new HashMap<>();
  }

  public void registerMark(ExamResult examResult, double scaleFactor) {

    Student student = examResult.getStudent();

    StudentRecord studentRecord;

    if (!allStudentMarks.containsKey(student)) {
      studentRecord = new StudentRecord(student);
      allStudentMarks.put(student, studentRecord);
    } else {
      studentRecord = allStudentMarks.get(student);
    }

    studentRecord.enter(examResult.getCode(), examResult.calculateTotal(scaleFactor));
  }

  public int averageAcrossAllExams(Student student) {

    if (!allStudentMarks.containsKey(student)) {
      throw new RuntimeException("No marks for student: " + student);
    }
    return allStudentMarks.get(student).calculateExamAverage();
  }
}

// a) Singleton design pattern
// b) Lazy and eager initialisation. In this case eager initialisation is used, i.e. the instance is created as soon
// as the code is executed. In lazy initialisation getInstance() would check if an instance has been created.
// It would initialise instance if necessary.
// d) In many cases it unnecessarily restricts usability of the code. It can create codenpendencies since all uses of
// the class use the same instance.
