package io.github.apace100.origins.mixin;

import io.github.edwinmindcraft.origins.common.power.NoSlowdownPower;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Player.class })
public abstract class NoCobwebSlowdownMixin extends LivingEntity implements Nameable, CommandSource {

    protected NoCobwebSlowdownMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(at = { @At("HEAD") }, method = { "makeStuckInBlock" }, cancellable = true)
    public void slowMovement(BlockState state, Vec3 multiplier, CallbackInfo info) {
        if (state.m_60734_() instanceof WebBlock && NoSlowdownPower.isActive(this, state)) {
            info.cancel();
        }
    }
}