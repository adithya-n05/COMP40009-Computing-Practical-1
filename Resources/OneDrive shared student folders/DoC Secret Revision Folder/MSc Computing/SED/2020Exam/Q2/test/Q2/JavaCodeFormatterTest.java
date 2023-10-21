package Q2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaCodeFormatterTest {

    CodeFormatter javaFormatter = new CodeFormatter(new JavaProperties());

    @Test
    public void doesNotIndentSingleLine(){
        String original = "Single line of code";
        String output = javaFormatter.format(original);
        assertEquals(original, output);
    }

    @Test
    public void indentsBlock() {
        String original = "{" + "\n" + "body" + "\n" + "}";
        String output = javaFormatter.format(original);
        assertEquals("{" + "\n" + "  body" + "\n" + "}", output);
    }

    @Test
    public void trimsOuterWhitespace() {
        String original = "        {" + "\n" + "body" + "\n" + "}     ";
        String output = javaFormatter.format(original);
        assertEquals("{" + "\n" + "  body" + "\n" + "}", output);
    }

    @Test
    public void correctsExcessiveIndent() {
        String original = "{" + "\n" + "       body" + "\n" + "}";
        String output = javaFormatter.format(original);
        assertEquals("{" + "\n" + "  body" + "\n" + "}", output);
    }

    @Test
    public void indentsNestedBlocks() {
        String original = "{" + "\n" + "1" + "\n" + "{" + "\n" + "2" + "\n" + "}" + "\n" + "3 " + "\n" + "}";
        String output = javaFormatter.format(original);
        assertEquals(String.join("\n", "{", "  1", "  {", "    2", "  }", "  3", "}"), output);
    }
}
