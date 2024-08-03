package journeymap.client.api.option;

public class EnumOption<E extends KeyedEnum> extends Option<E> {

    public EnumOption(OptionCategory category, String fieldName, String label, E defaultValue) {
        super(category, fieldName, label, defaultValue);
    }
}