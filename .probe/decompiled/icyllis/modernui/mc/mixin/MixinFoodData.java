package icyllis.modernui.mc.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Deprecated
@Mixin({ FoodData.class })
public class MixinFoodData {

    @Shadow
    private float saturationLevel;

    @Shadow
    private float exhaustionLevel;

    private float prevSaturationLevel;

    private float prevExhaustionLevel;

    private boolean needSync;

    @Inject(method = { "tick" }, at = { @At("TAIL") })
    private void postTick(Player player, CallbackInfo ci) {
        if (this.saturationLevel != this.prevSaturationLevel || this.exhaustionLevel != this.prevExhaustionLevel) {
            this.prevSaturationLevel = this.saturationLevel;
            this.prevExhaustionLevel = this.exhaustionLevel;
            this.needSync = true;
        }
        if (this.needSync && (player.m_9236_().getGameTime() & 7L) == 0L) {
            this.needSync = false;
        }
    }
}