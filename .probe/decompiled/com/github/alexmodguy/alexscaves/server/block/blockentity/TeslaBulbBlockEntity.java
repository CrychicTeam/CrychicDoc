package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TeslaBulbBlockEntity extends BlockEntity {

    public int age = 0;

    private Vec3 lightningPos = null;

    private int strikeTime = 0;

    private boolean exploding;

    private float explodeProgress;

    private float prevExplodeProgress;

    private LightningBolt dummyBolt;

    public TeslaBulbBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.TESLA_BULB.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, TeslaBulbBlockEntity entity) {
        entity.age++;
        entity.prevExplodeProgress = entity.explodeProgress;
        if (entity.exploding && entity.explodeProgress < 10.0F) {
            entity.explodeProgress += 0.5F;
        } else if (!entity.exploding && entity.explodeProgress > 0.0F) {
            entity.explodeProgress -= 0.5F;
        }
        if (entity.exploding) {
            if (entity.explodeProgress == 10.0F) {
                level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                if (!level.isClientSide) {
                    level.explode(null, (double) blockPos.m_123341_() + 0.5, (double) blockPos.m_123342_() + 0.5, (double) blockPos.m_123343_() + 0.5, 7.0F, false, Level.ExplosionInteraction.BLOCK);
                }
                return;
            }
            if (level.isClientSide) {
                Vec3 from = Vec3.atCenterOf(blockPos);
                for (int i = 0; i < 3 + level.random.nextInt(3); i++) {
                    Vec3 vec3 = entity.findStrikePos();
                    Vec3 to = vec3.subtract(from);
                    level.addParticle(ACParticleRegistry.TESLA_BULB_LIGHTNING.get(), from.x, from.y, from.z, to.x, to.y, to.z);
                }
            } else if (entity.explodeProgress % 1.0F == 0.0F) {
                if (entity.dummyBolt == null) {
                    entity.dummyBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                    entity.dummyBolt.setDamage(1.0F);
                    entity.dummyBolt.setVisualOnly(true);
                }
                for (LivingEntity entity1 : level.m_45976_(LivingEntity.class, new AABB(blockPos.offset(-5, -5, -5), blockPos.offset(5, 5, 5)))) {
                    entity1.m_8038_((ServerLevel) level, entity.dummyBolt);
                    entity1.m_7311_(0);
                }
            }
        }
        if (!level.isClientSide) {
            if (entity.strikeTime > 0) {
                entity.strikeTime--;
                entity.lightningPos = null;
            } else if (entity.strikeTime < 0) {
                entity.strikeTime++;
                if (entity.lightningPos != null && entity.strikeTime == -1) {
                    AABB aabb = new AABB(entity.lightningPos.subtract(1.0, 1.0, 1.0), entity.lightningPos.add(1.0, 1.0, 1.0));
                    if (entity.dummyBolt == null) {
                        entity.dummyBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        entity.dummyBolt.setDamage(1.0F);
                        entity.dummyBolt.setVisualOnly(true);
                    }
                    for (LivingEntity entity1 : level.m_45976_(LivingEntity.class, aabb)) {
                        entity1.m_8038_((ServerLevel) level, entity.dummyBolt);
                        entity1.m_7311_(0);
                    }
                }
            } else {
                entity.strikeTime = 15;
                if (level.getRandom().nextFloat() < 0.4F) {
                    Vec3 vec3 = entity.findStrikePos();
                    if (vec3 != null) {
                        entity.strikeTime = -5;
                        entity.lightningPos = vec3;
                        Vec3 from = Vec3.atCenterOf(blockPos);
                        Vec3 to = vec3.subtract(from);
                        ((ServerLevel) level).sendParticles(ACParticleRegistry.TESLA_BULB_LIGHTNING.get(), from.x, from.y, from.z, 0, to.x, to.y, to.z, 1.0);
                    }
                }
            }
        }
    }

    private Vec3 findStrikePos() {
        Vec3 center = Vec3.atCenterOf(this.m_58899_());
        return center.add((double) (5 - this.f_58857_.random.nextInt(10)), (double) (5 - this.f_58857_.random.nextInt(10)), (double) (5 - this.f_58857_.random.nextInt(10)));
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        BlockPos pos = this.m_58899_();
        return new AABB(pos.offset(-1, -1, -1), pos.offset(2, 2, 2));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.exploding = tag.getBoolean("Exploding");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("Exploding", this.exploding);
    }

    public float getExplodeProgress(float partialTicks) {
        return this.prevExplodeProgress + (this.explodeProgress - this.prevExplodeProgress) * partialTicks * 0.1F;
    }

    public void explode() {
        this.exploding = true;
    }
}