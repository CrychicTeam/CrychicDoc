package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2hostility.content.item.traits.EffectBooster;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;

public enum ChargeType {

    BOOST(EffectBooster::boostCharge), ETERNAL(EffectBooster::boostInfinite);

    private final Consumer<LivingEntity> cons;

    private ChargeType(Consumer<LivingEntity> cons) {
        this.cons = cons;
    }

    public void onHit(LivingEntity le) {
        this.cons.accept(le);
    }
}