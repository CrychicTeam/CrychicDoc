package journeymap.client.api.option;

import java.util.Locale;

public abstract class Option<T> {

    private final OptionCategory category;

    private final String label;

    private final String fieldName;

    protected final T defaultValue;

    protected Config<T> config;

    private int sortOrder = 100;

    protected Option(OptionCategory category, String fieldName, String label, T defaultValue) {
        this.category = category;
        this.fieldName = fieldName;
        this.label = label;
        this.defaultValue = defaultValue;
        OptionsRegistry.register(category.getModId(), this);
    }

    public OptionCategory getCategory() {
        return this.category;
    }

    public String getFieldName() {
        return this.fieldName.toLowerCase(Locale.ROOT).replaceAll("\\s", "");
    }

    public String getLabel() {
        return this.label;
    }

    private void setConfig(Config config) {
        this.config = config;
    }

    public T get() {
        return this.config.get();
    }

    public void set(T value) {
        this.config.set(value);
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    public Option<T> setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }
}