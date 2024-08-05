package snownee.kiwi;

import java.util.Map;

public class KiwiAnnotationData {

    String target;

    Map<String, Object> data;

    public KiwiAnnotationData(String target, Map<String, Object> data) {
        this.target = target;
        this.data = data;
    }

    public String target() {
        return this.target;
    }

    public Map<String, Object> data() {
        return this.data == null ? Map.of() : this.data;
    }
}