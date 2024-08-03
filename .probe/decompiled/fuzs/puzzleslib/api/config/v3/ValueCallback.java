package fuzs.puzzleslib.api.config.v3;

import java.util.function.Consumer;
import net.minecraftforge.common.ForgeConfigSpec;

@FunctionalInterface
public interface ValueCallback {

    <S, V extends ForgeConfigSpec.ConfigValue<S>> V accept(V var1, Consumer<S> var2);
}