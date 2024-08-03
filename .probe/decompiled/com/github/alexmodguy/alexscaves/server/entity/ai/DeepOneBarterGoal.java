package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.block.AbyssalAltarBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.AbyssalAltarBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.poi.ACPOIRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DeepOneBarterGoal extends Goal {

    private BlockPos altarPos = null;

    private DeepOneBaseEntity mob;

    private int executionCooldown = 10;

    private boolean groundTarget = false;

    public DeepOneBarterGoal(DeepOneBaseEntity mob) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.m_5448_();
        if ((target == null || !target.isAlive()) && this.mob.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            if (this.executionCooldown-- > 0) {
                return false;
            }
            this.executionCooldown = 150 + this.mob.m_217043_().nextInt(100);
            BlockPos pos = null;
            if (this.mob.getLastAltarPos() != null) {
                if (this.mob.m_9236_().getBlockEntity(this.mob.getLastAltarPos()) instanceof AbyssalAltarBlockEntity altar) {
                    this.executionCooldown = 10;
                    if (altar.getItem(0).is(ACTagRegistry.DEEP_ONE_BARTERS)) {
                        pos = this.mob.getLastAltarPos();
                    }
                } else {
                    this.mob.setLastAltarPos(null);
                }
            }
            if (pos == null) {
                List<BlockPos> list = getNearbyAltars(this.mob.m_20183_(), (ServerLevel) this.mob.m_9236_(), 64).sorted(Comparator.comparingDouble(this.mob.m_20183_()::m_123331_)).toList();
                if (!list.isEmpty()) {
                    pos = (BlockPos) list.get(0);
                }
            }
            if (pos != null) {
                this.altarPos = pos;
                this.mob.setLastAltarPos(this.altarPos);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.m_5448_();
        return this.altarPos != null && (hasPearls(this.mob.m_9236_(), this.altarPos, false) || this.mob.getAnimation() == this.mob.getTradingAnimation()) && (target == null || !target.isAlive());
    }

    private static Stream<BlockPos> getNearbyAltars(BlockPos blockpos, ServerLevel world, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        return pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.ABYSSAL_ALTAR.getKey()), blockpos2 -> hasPearls(world, blockpos2, true), blockpos, range, PoiManager.Occupancy.ANY);
    }

    private static boolean hasPearls(Level world, BlockPos pos, boolean timed) {
        if (world.getBlockEntity(pos) instanceof AbyssalAltarBlockEntity altar) {
            return timed && world.getGameTime() - altar.getLastInteractionTime() < 100L ? false : altar.getItem(0).is(ACTagRegistry.DEEP_ONE_BARTERS) && altar.m_58900_().m_60734_() instanceof AbyssalAltarBlock && !(Boolean) altar.m_58900_().m_61143_(AbyssalAltarBlock.ACTIVE);
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        Vec3 center = Vec3.atCenterOf(this.altarPos);
        double distance = Vec3.atBottomCenterOf(this.altarPos).subtract(this.mob.m_20182_()).horizontalDistance();
        if (distance < 8.0) {
            this.mob.m_21563_().setLookAt(center.x, center.y, center.z, 10.0F, (float) this.mob.m_8132_());
        }
        if (distance > 3.0) {
            this.mob.m_21573_().moveTo((double) ((float) this.altarPos.m_123341_() + 0.5F), (double) this.altarPos.m_123342_(), (double) ((float) this.altarPos.m_123343_() + 0.5F), 1.0);
        } else {
            this.mob.setLastAltarPos(this.altarPos);
            this.mob.setTradingLockedTime(50);
            this.mob.m_21573_().stop();
            if (this.mob.m_9236_().getBlockEntity(this.altarPos) instanceof AbyssalAltarBlockEntity altar && altar.getItem(0).is(ACTagRegistry.DEEP_ONE_BARTERS) && altar.queueItemDrop(altar.getItem(0))) {
                this.mob.m_9236_().broadcastEntityEvent(this.mob, (byte) 69);
                altar.onEntityInteract(this.mob, true);
                altar.setItem(0, ItemStack.EMPTY);
            }
        }
    }
}