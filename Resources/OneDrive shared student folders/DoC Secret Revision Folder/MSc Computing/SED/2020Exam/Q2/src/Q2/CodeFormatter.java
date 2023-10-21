package Q2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeFormatter {

  private CodeProperties properties;

  public CodeFormatter(CodeProperties properties){

    this.properties = properties;
  }


  public String format(String source) {

    String trimmed = stripBlankLines(source);
    int indentLevel = 0;

    List<String> indentedCode = new ArrayList<>();

    for(String line : linesOf(trimmed)) {
      if (line.contains(properties.endOfBlock())) {
        indentLevel -= 1;
      }
      indentedCode.add(indentBy(indentLevel, properties.tabsOrSpaces(), line));
      for (String openBlock : properties.startOfBlock()) {
        if (line.contains(openBlock)) {
          System.out.println("line contains " + openBlock);
          indentLevel += 1;
        }
      }

    }

    return String.join("\n", indentedCode);
  }


  private String indentBy(int num, WhiteSpace whiteSpace, String line) {
    String indent = "";
    for(int i = 0; i < num; i++) {
      indent = indent + whiteSpace.literal;
    }
    return indent + line.trim();
  }

  private List<String> linesOf(String source) {
    return Arrays.asList(source.split("\n"));
  }

  private String stripBlankLines(String source) {
    return source.trim();
  }

}

// a) It uses the template design patten using inheritance.
// b) Alternatively the strategy pattern could be used. This uses a composition rather than inheritance approach.
// d) Generally composition is preferable to inheritance since it reduces the degree of coupling of the classes.
// The language-specific part could be used by other classes than the code-formatter as well, e.g. to analyse code in an IDE.
