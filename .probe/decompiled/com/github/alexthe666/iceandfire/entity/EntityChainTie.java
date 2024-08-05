package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EntityChainTie extends HangingEntity {

    public EntityChainTie(EntityType<? extends HangingEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityChainTie(EntityType<? extends HangingEntity> type, Level worldIn, BlockPos hangingPositionIn) {
        super(type, worldIn, hangingPositionIn);
        this.setPos((double) hangingPositionIn.m_123341_() + 0.5, (double) hangingPositionIn.m_123342_(), (double) hangingPositionIn.m_123343_() + 0.5);
    }

    public static EntityChainTie createTie(Level worldIn, BlockPos fence) {
        EntityChainTie entityChainTie = new EntityChainTie(IafEntityRegistry.CHAIN_TIE.get(), worldIn, fence);
        worldIn.m_7967_(entityChainTie);
        entityChainTie.playPlacementSound();
        return entityChainTie;
    }

    @Nullable
    public static EntityChainTie getKnotForPosition(Level worldIn, BlockPos pos) {
        int i = pos.m_123341_();
        int j = pos.m_123342_();
        int k = pos.m_123343_();
        for (EntityChainTie entityleashknot : worldIn.m_45976_(EntityChainTie.class, new AABB((double) i - 1.0, (double) j - 1.0, (double) k - 1.0, (double) i + 1.0, (double) j + 1.0, (double) k + 1.0))) {
            if (entityleashknot != null && entityleashknot.m_31748_() != null && entityleashknot.m_31748_().equals(pos)) {
                return entityleashknot;
            }
        }
        return null;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos((double) Mth.floor(x) + 0.5, (double) Mth.floor(y) + 0.5, (double) Mth.floor(z) + 0.5);
    }

    @Override
    protected void recalculateBoundingBox() {
        this.m_20343_((double) this.f_31698_.m_123341_() + 0.5, (double) this.f_31698_.m_123342_() + 0.5, (double) this.f_31698_.m_123343_() + 0.5);
        double xSize = 0.3;
        double ySize = 0.875;
        this.m_20011_(new AABB(this.m_20185_() - xSize, this.m_20186_() - 0.5, this.m_20189_() - xSize, this.m_20185_() + xSize, this.m_20186_() + ySize - 0.5, this.m_20189_() + xSize));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source.getEntity() != null && source.getEntity() instanceof Player ? super.hurt(source, amount) : false;
    }

    @Override
    public int getWidth() {
        return 9;
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        BlockPos blockpos = this.m_31748_();
        compound.putInt("TileX", blockpos.m_123341_());
        compound.putInt("TileY", blockpos.m_123342_());
        compound.putInt("TileZ", blockpos.m_123343_());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.f_31698_ = new BlockPos(compound.getInt("TileX"), compound.getInt("TileY"), compound.getInt("TileZ"));
    }

    @Override
    protected float getEyeHeight(@NotNull Pose poseIn, @NotNull EntityDimensions sizeIn) {
        return -0.0625F;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024.0;
    }

    @Override
    public void dropItem(@Nullable Entity brokenEntity) {
        this.m_5496_(SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason removalReason) {
        super.m_142687_(removalReason);
        double d0 = 30.0;
        for (LivingEntity livingEntity : this.m_9236_().m_45976_(LivingEntity.class, new AABB(this.m_20185_() - d0, this.m_20186_() - d0, this.m_20189_() - d0, this.m_20185_() + d0, this.m_20186_() + d0, this.m_20189_() + d0))) {
            EntityDataProvider.getCapability(livingEntity).ifPresent(data -> {
                if (data.chainData.isChainedTo(this)) {
                    data.chainData.removeChain(this);
                    ItemEntity entityitem = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_(), new ItemStack(IafItemRegistry.CHAIN.get()));
                    entityitem.setDefaultPickUpDelay();
                    this.m_9236_().m_7967_(entityitem);
                }
            });
        }
    }

    @NotNull
    @Override
    public InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.m_9236_().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            AtomicBoolean flag = new AtomicBoolean(false);
            double radius = 30.0;
            for (LivingEntity livingEntity : this.m_9236_().m_45976_(LivingEntity.class, new AABB(this.m_20185_() - radius, this.m_20186_() - radius, this.m_20189_() - radius, this.m_20185_() + radius, this.m_20186_() + radius, this.m_20189_() + radius))) {
                EntityDataProvider.getCapability(livingEntity).ifPresent(data -> {
                    if (data.chainData.isChainedTo(player)) {
                        data.chainData.removeChain(player);
                        data.chainData.attachChain(this);
                        flag.set(true);
                    }
                });
            }
            if (!flag.get()) {
                this.remove(Entity.RemovalReason.DISCARDED);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean survives() {
        return this.m_9236_().getBlockState(this.f_31698_).m_60734_() instanceof WallBlock;
    }

    @Override
    public void playPlacementSound() {
        this.m_5496_(SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }
}