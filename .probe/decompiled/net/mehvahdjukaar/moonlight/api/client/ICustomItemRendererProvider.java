package net.mehvahdjukaar.moonlight.api.client;

import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface ICustomItemRendererProvider extends ItemLike {

    @OnlyIn(Dist.CLIENT)
    Supplier<ItemStackRenderer> getRendererFactory();

    @Deprecated(forRemoval = true)
    @Internal
    default void registerFabricRenderer() {
    }
}