package Q1;

public class QuestionResult {

  private final int max;
  private final int mark;

  public QuestionResult(int mark, int max) {
    this.max = max;
    this.mark = mark;
  }

  public int mark() {
    return mark;
  }

  public int max() {
    return max;
  }
}
