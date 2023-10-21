package Q1;

import java.util.HashMap;
import java.util.Map;

public class StudentRecord {

  private final Student student;
  private final Map<ExamCode, Integer> examMarks;

  public StudentRecord(Student student) {
    this.student = student;
    examMarks = new HashMap<>();
  }

  public void enter(ExamCode examCode, int mark) {
    examMarks.put(examCode, mark);
  }

  public int calculateExamAverage() {

    int count = 0;
    int total = 0;

    for (Map.Entry<ExamCode, Integer> examMark : examMarks.entrySet()) {
      count += 1;
      total += examMark.getValue();
    }

    if (count == 0) {
      throw new RuntimeException("No marks in record for student: " + student.name());
    }

    return Math.round(total / count);
  }
}
