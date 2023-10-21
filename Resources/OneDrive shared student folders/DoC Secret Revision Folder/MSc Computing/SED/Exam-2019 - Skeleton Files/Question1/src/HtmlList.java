public class HtmlList extends FormattedList {
    public HtmlList(String... items) {
        super(items);
    }

    @Override
    protected String formatHeader() {
        return "<ul>";
    }

    @Override
    protected String formatItem(String item) {
        return " <li>" + item + "</li>";
    }

    @Override
    protected String formatFooter() {
        return "</ul>";
    }
}
