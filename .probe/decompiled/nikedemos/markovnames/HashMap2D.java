package nikedemos.markovnames;

import java.util.HashMap;
import java.util.Map;

public class HashMap2D<T1, T2, T3> {

    public final Map<T1, Map<T2, T3>> mMap = new HashMap();

    public T3 put(T1 key1, T2 key2, T3 value) {
        Map<T2, T3> map;
        if (this.mMap.containsKey(key1)) {
            map = (Map<T2, T3>) this.mMap.get(key1);
        } else {
            map = new HashMap();
            this.mMap.put(key1, map);
        }
        return (T3) map.put(key2, value);
    }

    public T3 get(T1 key1, T2 key2) {
        return (T3) (this.mMap.containsKey(key1) ? ((Map) this.mMap.get(key1)).get(key2) : null);
    }

    public boolean containsKeys(T1 key1, T2 key2) {
        return this.mMap.containsKey(key1) && ((Map) this.mMap.get(key1)).containsKey(key2);
    }

    public void clear() {
        this.mMap.clear();
    }
}