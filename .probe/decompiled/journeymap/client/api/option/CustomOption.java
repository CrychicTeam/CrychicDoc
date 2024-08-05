package journeymap.client.api.option;

public abstract class CustomOption<T> extends Option<T> {

    public CustomOption(OptionCategory category, String fieldName, String label, T defaultValue) {
        super(category, fieldName, label, defaultValue);
    }
}