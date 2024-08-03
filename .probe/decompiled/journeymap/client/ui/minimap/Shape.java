package journeymap.client.ui.minimap;

import journeymap.client.Constants;
import journeymap.client.api.option.KeyedEnum;

public enum Shape implements KeyedEnum {

    Square("jm.minimap.shape_square"), Rectangle("jm.minimap.shape_rectangle"), Circle("jm.minimap.shape_circle");

    public final String key;

    private Shape(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public String toString() {
        return Constants.getString(this.key);
    }
}