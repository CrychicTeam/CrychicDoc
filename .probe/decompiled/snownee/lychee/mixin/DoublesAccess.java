package snownee.lychee.mixin;

import net.minecraft.advancements.critereon.MinMaxBounds;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ MinMaxBounds.Doubles.class })
public interface DoublesAccess {

    @Invoker(value = "<init>", remap = false)
    static MinMaxBounds.Doubles create(@Nullable Double min, @Nullable Double max) {
        throw new IllegalStateException();
    }
}