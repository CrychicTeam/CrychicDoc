package mezz.jei.gui.filter;

public interface IFilterTextSource {

    String getFilterText();

    boolean setFilterText(String var1);

    void addListener(IFilterTextSource.Listener var1);

    @FunctionalInterface
    public interface Listener {

        void onChange(String var1);
    }
}