import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FormattedList {
    private final List<String> content = new ArrayList<>();
    public FormattedList(String... items) {
        content.addAll(Arrays.asList(items));
    }
    public void add(String item) {
        content.add(item);
    }
    public void print() {
        System.out.println(formatHeader());
        for (String item : content) {
            System.out.println(formatItem(item));
        }
        System.out.println(formatFooter());
    }
    protected abstract String formatHeader();
    protected abstract String formatItem(String item);
    protected abstract String formatFooter();
}

