package journeymap.common.properties;

import journeymap.client.Constants;
import journeymap.client.api.option.KeyedEnum;

public enum ServerOption implements KeyedEnum {

    ALL("jm.server.edit.option.all"), OPS("jm.server.edit.option.op"), NONE("jm.server.edit.option.none");

    private String key;

    private ServerOption(String key) {
        this.key = key;
    }

    public String displayName() {
        return Constants.getString(this.key);
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public boolean canOps() {
        return OPS.equals(this);
    }

    public boolean enabled() {
        return !NONE.equals(this);
    }

    public boolean hasOption(boolean isOp) {
        switch(this) {
            case ALL:
                return true;
            case OPS:
                return isOp;
            case NONE:
            default:
                return false;
        }
    }

    public ServerOption enabled(boolean isOp) {
        return this.hasOption(isOp) ? ALL : NONE;
    }
}