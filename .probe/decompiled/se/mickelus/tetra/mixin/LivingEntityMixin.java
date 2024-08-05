package se.mickelus.tetra.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.effect.FocusEffect;

@Mixin({ LivingEntity.class })
public class LivingEntityMixin {

    @Inject(at = { @At("HEAD") }, method = { "increaseAirSupply(I)I" }, cancellable = true)
    public void increaseAirSupply(int amount, CallbackInfoReturnable<Integer> ci) {
        if (this.getInstance().m_6047_() && FocusEffect.hasApplicableItem(this.getInstance())) {
            ci.setReturnValue(this.getInstance().m_20146_());
        }
    }

    private LivingEntity getInstance() {
        return (LivingEntity) this;
    }
}