package forge.me.thosea.badoptimizations.mixin.accessor;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ LocalPlayer.class })
public interface PlayerAccessor {

    @Accessor("underwaterVisibilityTicks")
    int bo$underwaterVisibilityTicks();
}