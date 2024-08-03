package snownee.jade.api.config;

import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import snownee.jade.api.IToggleableProvider;

@NonExtendable
public interface IPluginConfig {

    Set<ResourceLocation> getKeys(String var1);

    Set<ResourceLocation> getKeys();

    boolean get(IToggleableProvider var1);

    boolean get(ResourceLocation var1);

    <T extends Enum<T>> T getEnum(ResourceLocation var1);

    int getInt(ResourceLocation var1);

    float getFloat(ResourceLocation var1);

    String getString(ResourceLocation var1);
}