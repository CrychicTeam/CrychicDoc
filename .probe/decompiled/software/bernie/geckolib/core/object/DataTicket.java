package software.bernie.geckolib.core.object;

import java.util.Map;
import java.util.Objects;

public class DataTicket<D> {

    private final String id;

    private final Class<? extends D> objectType;

    public DataTicket(String id, Class<? extends D> objectType) {
        this.id = id;
        this.objectType = objectType;
    }

    public String id() {
        return this.id;
    }

    public Class<? extends D> objectType() {
        return this.objectType;
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.id, this.objectType });
    }

    public <D> D getData(Map<? extends DataTicket<?>, ?> dataMap) {
        return (D) dataMap.get(this);
    }
}