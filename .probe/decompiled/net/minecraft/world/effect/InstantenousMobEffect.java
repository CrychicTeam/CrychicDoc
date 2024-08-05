package net.minecraft.world.effect;

public class InstantenousMobEffect extends MobEffect {

    public InstantenousMobEffect(MobEffectCategory mobEffectCategory0, int int1) {
        super(mobEffectCategory0, int1);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public boolean isDurationEffectTick(int int0, int int1) {
        return int0 >= 1;
    }
}