package mezz.jei.common.platform;

import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;

public interface IPlatformRegistry<T> {

    Stream<T> getValues();

    Optional<T> getValue(ResourceLocation var1);

    int getId(T var1);

    Optional<T> getValue(int var1);

    boolean contains(T var1);

    Optional<ResourceLocation> getRegistryName(T var1);
}