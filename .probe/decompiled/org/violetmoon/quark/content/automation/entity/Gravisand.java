package org.violetmoon.quark.content.automation.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.automation.module.GravisandModule;

public class Gravisand extends FallingBlockEntity {

    private static final EntityDataAccessor<Float> DIRECTION = SynchedEntityData.defineId(Gravisand.class, EntityDataSerializers.FLOAT);

    private static final String TAG_DIRECTION = "fallDirection";

    public Gravisand(EntityType<? extends Gravisand> type, Level world) {
        super(type, world);
        this.f_31946_ = GravisandModule.gravisand.defaultBlockState();
    }

    public Gravisand(Level world, double x, double y, double z, float direction) {
        this(GravisandModule.gravisandType, world);
        this.f_31946_ = GravisandModule.gravisand.defaultBlockState();
        this.f_19850_ = true;
        this.m_6034_(x, y + (double) ((1.0F - this.m_20206_()) / 2.0F), z);
        this.m_20256_(Vec3.ZERO);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
        this.m_31959_(new BlockPos(this.m_20183_()));
        this.f_19804_.set(DIRECTION, direction);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockpos1 = this.m_20183_();
        boolean aboveHasCollision = !this.m_9236_().getBlockState(blockpos1.above()).m_60812_(this.m_9236_(), blockpos1.above()).isEmpty();
        if (!this.m_9236_().isClientSide && this.getFallDirection() > 0.0F && !this.m_213877_() && aboveHasCollision) {
            Block block = this.f_31946_.m_60734_();
            BlockState blockstate = this.m_9236_().getBlockState(blockpos1);
            this.m_20256_(this.m_20184_().multiply(0.7, 0.5, 0.7));
            boolean flag2 = blockstate.m_60629_(new DirectionalPlaceContext(this.m_9236_(), blockpos1, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
            boolean flag3 = FallingBlock.isFree(this.m_9236_().getBlockState(blockpos1.above()));
            boolean flag4 = this.f_31946_.m_60710_(this.m_9236_(), blockpos1) && !flag3;
            if (flag2 && flag4) {
                if (this.m_9236_().setBlock(blockpos1, this.f_31946_, 3)) {
                    ((ServerLevel) this.m_9236_()).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockpos1, this.m_9236_().getBlockState(blockpos1)));
                    this.m_146870_();
                } else if (this.f_31943_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.m_146870_();
                    this.m_149650_(block, blockpos1);
                    this.m_19998_(block);
                }
            } else {
                this.m_146870_();
                if (this.f_31943_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.m_149650_(block, blockpos1);
                    this.m_19998_(block);
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DIRECTION, 0.0F);
    }

    @Override
    public void move(@NotNull MoverType type, @NotNull Vec3 vec) {
        if (type == MoverType.SELF) {
            super.m_6478_(type, vec.scale((double) (this.getFallDirection() * -1.0F)));
        } else {
            super.m_6478_(type, vec);
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource source) {
        return false;
    }

    private float getFallDirection() {
        return this.f_19804_.get(DIRECTION);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("fallDirection", this.getFallDirection());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.f_19804_.set(DIRECTION, compound.getFloat("fallDirection"));
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}