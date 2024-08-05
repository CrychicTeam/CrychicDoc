package fabric.me.thosea.badoptimizations.interfaces;

import net.minecraft.class_1959;
import net.minecraft.class_4543;

@FunctionalInterface
public interface BiomeSkyColorGetter {

    int get(int var1, int var2, int var3);

    static BiomeSkyColorGetter of(class_4543 access) {
        return (x, y, z) -> ((class_1959) access.method_24854(x, y, z).comp_349()).method_8697();
    }
}