package dev.architectury.mixin;

import dev.architectury.event.events.common.LightningEvent;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ LightningBolt.class })
public abstract class MixinLightningBolt extends Entity {

    public MixinLightningBolt(EntityType<?> type, Level level) {
        super(type, level);
        throw new IllegalStateException();
    }

    @Inject(method = { "tick" }, at = { @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", ordinal = 1, shift = Shift.BY, by = 1) }, locals = LocalCapture.CAPTURE_FAILHARD)
    public void handleLightning(CallbackInfo ci, List<Entity> list) {
        if (!this.m_213877_() && !this.m_9236_().isClientSide) {
            LightningEvent.STRIKE.invoker().onStrike((LightningBolt) this, this.m_9236_(), this.m_20182_(), list);
        }
    }
}