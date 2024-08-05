package com.rekindled.embers.augment;

import com.rekindled.embers.api.EmbersAPI;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.util.EmberInventoryUtil;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlastingCoreAugment extends AugmentBase {

    private HashSet<Entity> blastedEntities = new HashSet();

    public BlastingCoreAugment(ResourceLocation id) {
        super(id, 2.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private double getChanceBonus(double resonance) {
        return resonance > 1.0 ? 1.0 + (resonance - 1.0) * 0.5 : resonance;
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        if (event.getPlayer() != null && !event.getPlayer().m_21205_().isEmpty()) {
            ItemStack s = event.getPlayer().m_21205_();
            int blastingLevel = AugmentUtil.getAugmentLevel(s, this);
            if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(event.getPlayer()) >= this.cost) {
                event.getPlayer().m_9236_().explode(event.getPlayer(), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 0.5F, Level.ExplosionInteraction.BLOCK);
                double resonance = EmbersAPI.getEmberResonance(s);
                double chance = (double) blastingLevel / (double) (blastingLevel + 1) * this.getChanceBonus(resonance);
                for (BlockPos toExplode : this.getBlastCube(world, pos, event.getPlayer(), chance)) {
                    BlockState state = world.m_8055_(toExplode);
                    if (state.m_60800_(world, pos) >= 0.0F && event.getPlayer().hasCorrectToolForDrops(world.m_8055_(toExplode))) {
                        world.m_46953_(toExplode, false, event.getPlayer());
                        Block.dropResources(state, event.getPlayer().m_9236_(), pos, world.m_7702_(pos), event.getPlayer(), s);
                    }
                }
                EmberInventoryUtil.removeEmber(event.getPlayer(), this.cost);
            }
        }
    }

    public Iterable<BlockPos> getBlastAdjacent(LevelAccessor world, BlockPos pos, Player player, double chance) {
        ArrayList<BlockPos> posList = new ArrayList();
        for (Direction face : Direction.values()) {
            if (Misc.random.nextDouble() < chance) {
                posList.add(pos.relative(face));
            }
        }
        return posList;
    }

    public Iterable<BlockPos> getBlastCube(LevelAccessor world, BlockPos pos, Player player, double chance) {
        ArrayList<BlockPos> posList = new ArrayList();
        for (Direction facePrimary : Direction.values()) {
            if (Misc.random.nextDouble() < chance) {
                BlockPos posPrimary = pos.relative(facePrimary);
                posList.add(posPrimary);
                for (Direction faceSecondary : Direction.values()) {
                    if (faceSecondary.getAxis() != facePrimary.getAxis() && Misc.random.nextDouble() < chance - 0.5) {
                        BlockPos posSecondary = posPrimary.relative(faceSecondary);
                        posList.add(posSecondary);
                        for (Direction faceTertiary : Direction.values()) {
                            if (faceTertiary.getAxis() != facePrimary.getAxis() && faceTertiary.getAxis() != faceSecondary.getAxis() && Misc.random.nextDouble() < chance - 1.0) {
                                BlockPos posTertiary = posSecondary.relative(faceTertiary);
                                posList.add(posTertiary);
                            }
                        }
                    }
                }
            }
        }
        return posList;
    }

    @SubscribeEvent
    public void onHit(LivingHurtEvent event) {
        if (!this.blastedEntities.contains(event.getEntity()) && event.getSource().getEntity() != event.getEntity() && event.getSource().getDirectEntity() != event.getEntity()) {
            try {
                if (event.getSource().getEntity() instanceof Player damager) {
                    this.blastedEntities.add(damager);
                    ItemStack s = damager.m_21205_();
                    if (!s.isEmpty()) {
                        int blastingLevel = AugmentUtil.getAugmentLevel(s, this);
                        if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(damager) >= this.cost) {
                            double resonance = EmbersAPI.getEmberResonance(s);
                            float strength = (float) ((resonance + 1.0) * (Math.atan(0.6 * (double) blastingLevel) / Math.PI));
                            EmberInventoryUtil.removeEmber(damager, this.cost);
                            this.blastedEntities.add(event.getEntity());
                            List<LivingEntity> entities = damager.m_9236_().m_45976_(LivingEntity.class, new AABB(event.getEntity().m_20185_() - 4.0 * (double) strength, event.getEntity().m_20186_() - 4.0 * (double) strength, event.getEntity().m_20189_() - 4.0 * (double) strength, event.getEntity().m_20185_() + 4.0 * (double) strength, event.getEntity().m_20186_() + 4.0 * (double) strength, event.getEntity().m_20189_() + 4.0 * (double) strength));
                            Explosion explosion = damager.m_9236_().explode(damager, event.getEntity().m_20185_(), event.getEntity().m_20186_() + (double) event.getEntity().m_20206_() / 2.0, event.getEntity().m_20189_(), 0.5F, Level.ExplosionInteraction.NONE);
                            for (LivingEntity e : entities) {
                                if (!Objects.equals(e.m_20148_(), damager.m_20148_())) {
                                    this.blastedEntities.add(e);
                                    e.hurt(damager.m_9236_().damageSources().explosion(explosion), event.getAmount() * strength);
                                    e.hurtTime = 0;
                                }
                            }
                        }
                    }
                }
                if (event.getEntity() instanceof Player damagerx) {
                    int blastingLevel = AugmentUtil.getArmorAugmentLevel(damagerx, this);
                    if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(damagerx) >= this.cost) {
                        float strength = (float) (2.0 * (Math.atan(0.6 * (double) blastingLevel) / Math.PI));
                        EmberInventoryUtil.removeEmber(damagerx, this.cost);
                        List<LivingEntity> entities = damagerx.m_9236_().m_45976_(LivingEntity.class, new AABB(damagerx.m_20185_() - 4.0 * (double) strength, damagerx.m_20186_() - 4.0 * (double) strength, damagerx.m_20189_() - 4.0 * (double) strength, damagerx.m_20185_() + 4.0 * (double) strength, damagerx.m_20186_() + 4.0 * (double) strength, damagerx.m_20189_() + 4.0 * (double) strength));
                        Explosion explosion = damagerx.m_9236_().explode(damagerx, damagerx.m_20185_(), damagerx.m_20186_() + (double) damagerx.m_20206_() / 2.0, damagerx.m_20189_(), 0.5F, Level.ExplosionInteraction.NONE);
                        for (LivingEntity ex : entities) {
                            if (!Objects.equals(ex.m_20148_(), event.getEntity().m_20148_())) {
                                this.blastedEntities.add(ex);
                                ex.hurt(damagerx.m_9236_().damageSources().explosion(explosion), event.getAmount() * strength * 0.25F);
                                ex.knockback((double) (2.0F * strength), -ex.m_20185_() + damagerx.m_20185_(), -ex.m_20189_() + damagerx.m_20189_());
                                ex.hurtTime = 0;
                            }
                        }
                    }
                }
            } finally {
                this.blastedEntities.clear();
            }
        }
    }
}