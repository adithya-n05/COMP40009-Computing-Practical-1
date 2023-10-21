package Q1;

import java.util.Set;

public class ExamResult {

  private final Student student;
  private final ExamCode code;
  private final Set<QuestionResult> partResults;

  public ExamResult(Student student, ExamCode code, Set<QuestionResult> results) {
    this.partResults = results;
    this.student = student;
    this.code = code;
  }

  public void scaleAndSave(double scalingFactor) {
    MarkingSystem.getInstance().registerMark(this, scalingFactor);
  }

  public int calculateTotal(double scaleFactor) {
    double total = 0;
    double max = 0;
    for (QuestionResult q : partResults) {
      total += q.mark();
      max += q.max();
    }
    return (int) Math.round(scaleFactor * 100 * total / max);
  }

  public Student getStudent() {
    return student;
  }

  public ExamCode getCode() {
    return code;
  }
}
