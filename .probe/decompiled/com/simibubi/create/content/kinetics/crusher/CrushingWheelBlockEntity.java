package com.simibubi.create.content.kinetics.crusher;

import com.simibubi.create.AllDamageTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CrushingWheelBlockEntity extends KineticBlockEntity {

    public CrushingWheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(20);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CRUSHING_WHEEL, AllAdvancements.CRUSHER_MAXED });
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        this.fixControllers();
    }

    public void fixControllers() {
        for (Direction d : Iterate.directions) {
            ((CrushingWheelBlock) this.m_58900_().m_60734_()).updateControllers(this.m_58900_(), this.m_58904_(), this.m_58899_(), d);
        }
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_).inflate(1.0);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        this.fixControllers();
    }

    @SubscribeEvent
    public static void crushingIsFortunate(LootingLevelEvent event) {
        DamageSource damageSource = event.getDamageSource();
        if (damageSource != null && damageSource.is(AllDamageTypes.CRUSH)) {
            event.setLootingLevel(2);
        }
    }

    @SubscribeEvent
    public static void handleCrushedMobDrops(LivingDropsEvent event) {
        DamageSource damageSource = event.getSource();
        if (damageSource != null && damageSource.is(AllDamageTypes.CRUSH)) {
            Vec3 outSpeed = Vec3.ZERO;
            for (ItemEntity outputItem : event.getDrops()) {
                outputItem.m_20256_(outSpeed);
            }
        }
    }
}