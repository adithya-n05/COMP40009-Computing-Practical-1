package Q2;

public enum WhiteSpace {
  TABS("\t"),
  TWOSPACES("  "),
  FOURSPACES("    ");

  public String literal;

  WhiteSpace(String literal) {
    this.literal = literal;
  }
}
