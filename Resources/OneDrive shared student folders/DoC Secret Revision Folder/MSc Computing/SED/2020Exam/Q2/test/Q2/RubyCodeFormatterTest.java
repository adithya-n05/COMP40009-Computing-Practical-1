package Q2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RubyCodeFormatterTest {

    CodeFormatter rubyFormatter = new CodeFormatter(new RubyProperties());

    @Test
    public void doesNotIndentSingleLine(){
        String original = "Single line of code";
        String output = rubyFormatter.format(original);
        assertEquals(original, output);
    }

    @Test
    public void indentsIfBlock() {
        String original = "if x > 1" + "\n" + "body" + "\n" + "end";
        String expected = "if x > 1" + "\n" + "\tbody" + "\n" + "end";
        assertEquals(expected, rubyFormatter.format(original));
    }

    @Test
    public void indentsDoBlock() {
        String original = "do" + "\n" + "body" + "\n" + "end";
        String expected = "do" + "\n" + "\tbody" + "\n" + "end";
        assertEquals(expected, rubyFormatter.format(original));
    }

    @Test
    public void trimsOuterWhitespace() {
        String original = "        1" + "\n" + "2" + "\n" + "3     ";
        String expected = "1" + "\n" + "2" + "\n" + "3";
        assertEquals(expected, rubyFormatter.format(original));
    }
}
