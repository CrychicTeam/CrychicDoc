package fabric.me.thosea.badoptimizations.mixin.accessor;

import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ class_746.class })
public interface PlayerAccessor {

    @Accessor("underwaterVisibilityTicks")
    int bo$underwaterVisibilityTicks();
}