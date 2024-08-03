package com.mna.rituals.effects;

import com.mna.api.items.IPhylacteryItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.sound.SFX;
import com.mna.entities.utility.PresentItem;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemCrystalPhylactery;
import com.mna.items.sorcery.ItemEntityCrystal;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RitualEffectShadowSoul extends RitualEffect {

    public RitualEffectShadowSoul(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        for (RitualBlockPos pos : context.getAllPositions()) {
            if (pos != null) {
                BlockPos cur = pos.getBlockPos();
                context.getLevel().addParticle(new MAParticleType(ParticleInit.SOUL.get()).setGravity(0.0F).setPhysics(true), (double) cur.m_123341_() + Math.random(), (double) cur.m_123342_() + 0.1, (double) cur.m_123343_() + Math.random(), 0.0, 0.05, 0.0);
            }
        }
        return true;
    }

    @Override
    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.SHADOW_COPY;
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        ItemStack phylacteryStack = ItemStack.EMPTY;
        ItemStack crystalStack = ItemStack.EMPTY;
        List<ItemStack> collected = context.getCollectedReagents();
        for (int i = 0; i < collected.size(); i++) {
            ItemStack stack = (ItemStack) collected.get(i);
            if (stack.getItem() instanceof IPhylacteryItem) {
                phylacteryStack = stack;
            } else if (stack.getItem() == ItemInit.ENTITY_ENTRAPMENT_CRYSTAL.get() && ItemEntityCrystal.getEntityType(stack) != null) {
                crystalStack = stack;
            }
        }
        if (!phylacteryStack.isEmpty() && !crystalStack.isEmpty()) {
            try {
                EntityType<? extends Mob> crystalType = (EntityType<? extends Mob>) ItemEntityCrystal.getEntityType(crystalStack);
                EntityType<? extends Mob> existing = ((IPhylacteryItem) phylacteryStack.getItem()).getContainedEntity(phylacteryStack);
                if (existing != null && existing != crystalType) {
                    this.ejectItems(context);
                    return false;
                }
                if (existing == null) {
                    ItemCrystalPhylactery.setEntityType(phylacteryStack, crystalType, context.getLevel());
                }
                ((IPhylacteryItem) phylacteryStack.getItem()).fill(phylacteryStack, crystalType, (float) ((IPhylacteryItem) phylacteryStack.getItem()).getMaximumFill() * 0.25F, context.getLevel());
                Vec3 center = Vec3.atCenterOf(context.getCenter());
                PresentItem epi = new PresentItem(context.getLevel(), center.x, center.y, center.z, phylacteryStack);
                context.getLevel().m_7967_(epi);
                Entity e = ItemEntityCrystal.restoreEntity(context.getLevel(), crystalStack);
                e.setPos(Vec3.atCenterOf(context.getCenter()));
                context.getLevel().m_7967_(e);
            } catch (Throwable var10) {
                this.ejectItems(context);
            }
            return false;
        } else {
            this.ejectItems(context);
            return false;
        }
    }

    private void ejectItems(IRitualContext context) {
        Vec3 center = Vec3.atCenterOf(context.getCenter());
        context.getCollectedReagents().forEach(is -> {
            ItemEntity ie = new ItemEntity(context.getLevel(), center.x, center.y, center.z, is, -0.2F + Math.random() * 0.4F, 0.2F, -0.2F + Math.random() * 0.4F);
            context.getLevel().m_7967_(ie);
        });
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 20;
    }
}