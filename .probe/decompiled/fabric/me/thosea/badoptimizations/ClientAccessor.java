package fabric.me.thosea.badoptimizations;

import net.minecraft.class_310;

public interface ClientAccessor {

    void badoptimizations$updateFpsString();

    static boolean shouldUpdateFpsString(class_310 client) {
        return client.field_1690.field_1866 && (!client.field_1690.field_1842 || client.field_1755 != null);
    }
}