package fabric.me.thosea.badoptimizations.mixin.accessor;

import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ class_757.class })
public interface GameRendererAccessor {

    @Accessor("skyDarkness")
    float bo$getSkyDarkness();
}