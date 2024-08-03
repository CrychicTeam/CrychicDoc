package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.TephraEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class VolcanicCoreBlockEntity extends BlockEntity {

    private int battleTime = 0;

    private int bossSpawnCooldown = 0;

    private int tephraSpawnCooldown = 0;

    private final Predicate<ItemEntity> itemAttracted = item -> item.getItem().is(ACItemRegistry.OMINOUS_CATALYST.get());

    public VolcanicCoreBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ACBlockEntityRegistry.VOLCANIC_CORE.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, VolcanicCoreBlockEntity entity) {
        if (entity.bossSpawnCooldown > 0) {
            entity.bossSpawnCooldown--;
        } else {
            Vec3 vec3 = Vec3.atCenterOf(blockPos);
            AABB aabb = new AABB(vec3.subtract(20.0, 100.0, 20.0), vec3.add(20.0, 100.0, 20.0));
            double maxDist = 100.0;
            for (ItemEntity item : level.m_6443_(ItemEntity.class, aabb, entity.itemAttracted)) {
                double dist = (double) Mth.sqrt((float) item.m_20238_(vec3));
                if (dist < maxDist) {
                    Vec3 sub = vec3.subtract(item.m_20182_()).normalize().scale(0.2F);
                    Vec3 delta = item.m_20184_().scale(0.8F);
                    item.m_20256_(sub.add(delta));
                }
                if (dist < 0.66F && entity.spawnBoss()) {
                    item.getItem().shrink(1);
                    if (!level.isClientSide) {
                        for (Player player : level.m_6443_(Player.class, aabb, EntitySelector.NO_SPECTATORS)) {
                            ACAdvancementTriggerRegistry.SUMMON_LUXTRUCTOSAURUS.triggerForEntity(player);
                        }
                    }
                }
            }
        }
        if (AlexsCaves.PROXY.isPrimordialBossActive(level)) {
            if (entity.tephraSpawnCooldown-- <= 0) {
                entity.spawnTephra(true);
                entity.tephraSpawnCooldown = 120 + level.random.nextInt(120);
            }
        } else {
            entity.battleTime = 0;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.tephraSpawnCooldown = tag.getInt("TephraSpawnCooldown");
        this.bossSpawnCooldown = tag.getInt("BossSpawnCooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("TephraSpawnCooldown", this.tephraSpawnCooldown);
        tag.putInt("BossSpawnCooldown", this.bossSpawnCooldown);
    }

    public boolean spawnBoss() {
        if (this.bossSpawnCooldown > 0) {
            return false;
        } else {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            BlockPos volcanoTop = this.m_58899_();
            for (int i = -5; i <= -5; i++) {
                for (int j = -5; j <= -5; j++) {
                    BlockPos.MutableBlockPos localTop = this.getTopOfVolcano(mutableBlockPos.set(this.m_58899_().m_123341_() + i, this.m_58899_().m_123342_(), this.m_58899_().m_123343_() + j));
                    if (localTop.m_123342_() > volcanoTop.m_123342_()) {
                        volcanoTop = volcanoTop.atY(localTop.m_123342_());
                    }
                }
            }
            LuxtructosaurusEntity luxtructosaurus = ACEntityRegistry.LUXTRUCTOSAURUS.get().create(this.f_58857_);
            luxtructosaurus.m_146884_(Vec3.upFromBottomCenterOf(volcanoTop, 2.0));
            luxtructosaurus.m_6842_(true);
            luxtructosaurus.setAnimation(SauropodBaseEntity.ANIMATION_SUMMON);
            luxtructosaurus.enragedFor = 100;
            luxtructosaurus.setEnraged(true);
            this.f_58857_.m_7967_(luxtructosaurus);
            this.bossSpawnCooldown = 24000;
            return true;
        }
    }

    private BlockPos.MutableBlockPos getTopOfVolcano(BlockPos posIn) {
        BlockPos.MutableBlockPos volcanoTop = new BlockPos.MutableBlockPos();
        volcanoTop.set(posIn);
        while (this.f_58857_.getBlockState(volcanoTop).m_204336_(ACTagRegistry.VOLCANO_BLOCKS) && volcanoTop.m_123342_() < this.f_58857_.m_151558_()) {
            volcanoTop.move(0, 1, 0);
        }
        volcanoTop.move(0, -1, 0);
        return volcanoTop;
    }

    private void spawnTephra(boolean big) {
        BlockPos volcanoTop = this.getTopOfVolcano(this.m_58899_()).immutable();
        if (this.f_58857_.getBlockState(volcanoTop).m_60713_(ACBlockRegistry.VOLCANIC_CORE.get()) || this.f_58857_.getBlockState(volcanoTop).m_60713_(ACBlockRegistry.PRIMAL_MAGMA.get()) || this.f_58857_.getBlockState(volcanoTop).m_60713_(ACBlockRegistry.FISSURE_PRIMAL_MAGMA.get()) || this.f_58857_.getFluidState(volcanoTop).is(FluidTags.LAVA)) {
            Vec3 volcanoVec = Vec3.upFromBottomCenterOf(volcanoTop, 3.0);
            Player nearestPlayer = this.f_58857_.m_45924_(volcanoVec.x, volcanoVec.y, volcanoVec.z, 400.0, true);
            if (big) {
                TephraEntity bigTephra = ACEntityRegistry.TEPHRA.get().create(this.f_58857_);
                bigTephra.m_146884_(volcanoVec);
                bigTephra.setMaxScale(2.0F + this.f_58857_.random.nextFloat());
                Vec3 targetVec;
                if (nearestPlayer == null) {
                    targetVec = new Vec3((double) (this.f_58857_.random.nextFloat() - 0.5F), 0.0, (double) (this.f_58857_.random.nextFloat() - 0.5F)).normalize().scale((double) (this.f_58857_.random.nextInt(50) + 20));
                } else {
                    targetVec = nearestPlayer.m_20182_().subtract(volcanoVec);
                    bigTephra.setArcingTowards(nearestPlayer.m_20148_());
                }
                double d4 = Math.sqrt(targetVec.x * targetVec.x + targetVec.z * targetVec.z);
                double d5 = nearestPlayer == null ? (double) this.f_58857_.random.nextFloat() : 0.0;
                bigTephra.m_6686_(targetVec.x, targetVec.y + 0.5 + d4 * 0.75 + d5, targetVec.z, (float) (d4 * 0.1F + d5), 1.0F + this.f_58857_.random.nextFloat() * 0.5F);
                this.f_58857_.m_7967_(bigTephra);
            }
            for (int smalls = 0; smalls < 3 + this.f_58857_.random.nextInt(3); smalls++) {
                TephraEntity smallTephra = ACEntityRegistry.TEPHRA.get().create(this.f_58857_);
                smallTephra.m_146884_(volcanoVec);
                smallTephra.setMaxScale(0.6F + 0.6F * this.f_58857_.random.nextFloat());
                Vec3 targetVec = new Vec3((double) (this.f_58857_.random.nextFloat() - 0.5F), 0.0, (double) (this.f_58857_.random.nextFloat() - 0.5F)).normalize().scale((double) (this.f_58857_.random.nextInt(30) + 30));
                double d4 = Math.sqrt(targetVec.x * targetVec.x + targetVec.z * targetVec.z);
                smallTephra.m_6686_(targetVec.x, targetVec.y + 0.5 + d4 * 0.75 + (double) this.f_58857_.random.nextFloat(), targetVec.z, (float) (d4 * 0.1F + (double) this.f_58857_.random.nextFloat()), 1.0F);
                this.f_58857_.m_7967_(smallTephra);
            }
        }
    }
}