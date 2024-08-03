package dev.xkmc.l2library.base.effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event.HasResult;
import org.jetbrains.annotations.NotNull;

@HasResult
public class ForceAddEffectEvent extends MobEffectEvent {

    public ForceAddEffectEvent(LivingEntity living, @NotNull MobEffectInstance effectInstance) {
        super(living, effectInstance);
    }

    @NotNull
    @Override
    public MobEffectInstance getEffectInstance() {
        return super.getEffectInstance();
    }
}