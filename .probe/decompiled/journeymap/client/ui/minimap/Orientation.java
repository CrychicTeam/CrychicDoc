package journeymap.client.ui.minimap;

import journeymap.client.Constants;
import journeymap.client.api.option.KeyedEnum;

public enum Orientation implements KeyedEnum {

    North("jm.minimap.orientation.north"), OldNorth("jm.minimap.orientation.oldnorth"), PlayerHeading("jm.minimap.orientation.playerheading");

    public final String key;

    private Orientation(String key) {
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