package icyllis.arc3d.engine;

import java.util.Map;
import java.util.Map.Entry;

public class DriverBugWorkarounds {

    public static final int DEFAULT = 0;

    public static final int DISABLED = 1;

    public static final int ENABLED = 2;

    public byte dsa_element_buffer_broken = 0;

    public DriverBugWorkarounds() {
    }

    public DriverBugWorkarounds(Map<String, Boolean> states) {
        if (states != null && !states.isEmpty()) {
            for (Entry<String, Boolean> e : states.entrySet()) {
                String var4 = (String) e.getKey();
                switch(var4) {
                    case "dsa_element_buffer_broken":
                        this.dsa_element_buffer_broken = (byte) (this.dsa_element_buffer_broken | mask(e));
                }
            }
        }
    }

    private static byte mask(Entry<?, Boolean> e) {
        Boolean v = (Boolean) e.getValue();
        if (v == Boolean.TRUE) {
            return 2;
        } else {
            return (byte) (v == Boolean.FALSE ? 1 : 0);
        }
    }

    public static boolean isEnabled(byte state) {
        return (state & 2) != 0;
    }

    public static boolean isDisabled(byte state) {
        return (state & 1) != 0;
    }

    public void applyOverrides(DriverBugWorkarounds workarounds) {
        if (workarounds != null) {
            this.dsa_element_buffer_broken = (byte) (this.dsa_element_buffer_broken | workarounds.dsa_element_buffer_broken);
        }
    }
}