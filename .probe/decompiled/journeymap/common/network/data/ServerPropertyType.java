package journeymap.common.network.data;

import java.util.HashMap;
import java.util.Map;

public enum ServerPropertyType {

    GLOBAL(1), DEFAULT(2), DIMENSION(3);

    private int id;

    private static final Map<Integer, ServerPropertyType> map = new HashMap();

    private ServerPropertyType(int id) {
        this.id = id;
    }

    public static ServerPropertyType getFromType(int id) {
        return (ServerPropertyType) map.get(id);
    }

    public int getId() {
        return this.id;
    }

    static {
        for (ServerPropertyType propertyType : values()) {
            map.put(propertyType.id, propertyType);
        }
    }
}