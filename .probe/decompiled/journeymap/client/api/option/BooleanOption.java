package journeymap.client.api.option;

public class BooleanOption extends Option<Boolean> {

    private final Boolean isMaster;

    public BooleanOption(OptionCategory category, String fieldName, String label, Boolean defaultValue) {
        super(category, fieldName, label, defaultValue);
        this.isMaster = false;
    }

    public BooleanOption(OptionCategory category, String fieldName, String label, Boolean defaultValue, Boolean isMaster) {
        super(category, fieldName, label, defaultValue);
        this.isMaster = isMaster;
    }

    public Boolean isMaster() {
        return this.isMaster;
    }
}