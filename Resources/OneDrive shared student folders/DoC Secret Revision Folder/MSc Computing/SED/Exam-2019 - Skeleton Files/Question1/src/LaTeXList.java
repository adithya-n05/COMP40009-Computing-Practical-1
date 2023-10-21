public class LaTeXList extends FormattedList {
    public LaTeXList(String... items) {
        super(items);
    }

    @Override
    protected String formatHeader() {
        return "\\begin{itemize}";
    }

    @Override
    protected String formatItem(String item) {
        return " \\item " + item;
    }

    @Override
    protected String formatFooter() {
        return "\\end{itemize}";
    }
}
