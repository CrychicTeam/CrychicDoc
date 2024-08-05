package com.mna.mixins;

import com.mna.effects.EffectInit;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Creeper.class })
public class CreeperIgniteMixin {

    @Accessor("DATA_IS_IGNITED")
    public static EntityDataAccessor<Boolean> getIgnitedData() {
        throw new AssertionError();
    }

    @Accessor("DATA_SWELL_DIR")
    public static EntityDataAccessor<Integer> getSwellDir() {
        throw new AssertionError();
    }

    @ModifyVariable(method = { "setSwellDir(I)V" }, at = @At("HEAD"), ordinal = 0)
    private int mna$modifyParamSetSwellDir(int dir) {
        Creeper self = (Creeper) this;
        return self.m_21023_(EffectInit.SOAKED.get()) ? -1 : dir;
    }

    @Inject(at = { @At("HEAD") }, method = { "ignite" }, cancellable = true)
    public void mna$ignite(CallbackInfo cir) {
        Creeper self = (Creeper) this;
        if (self.m_21023_(EffectInit.SOAKED.get())) {
            self.m_20088_().set(getSwellDir(), -1);
            cir.cancel();
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "tick" })
    public void mna$tick(CallbackInfo cir) {
        Creeper self = (Creeper) this;
        if (self.isIgnited() && self.m_21023_(EffectInit.SOAKED.get())) {
            self.m_20088_().set(getIgnitedData(), false);
            self.m_20088_().set(getSwellDir(), -1);
        }
    }
}