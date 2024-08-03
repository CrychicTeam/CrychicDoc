package snownee.lychee.mixin;

import net.minecraft.advancements.critereon.MinMaxBounds;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ MinMaxBounds.Ints.class })
public interface IntsAccess {

    @Invoker(value = "<init>", remap = false)
    static MinMaxBounds.Ints create(@Nullable Integer min, @Nullable Integer max) {
        throw new IllegalStateException();
    }
}