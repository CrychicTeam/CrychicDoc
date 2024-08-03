package net.minecraft.world.entity.decoration;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LeashFenceKnotEntity extends HangingEntity {

    public static final double OFFSET_Y = 0.375;

    public LeashFenceKnotEntity(EntityType<? extends LeashFenceKnotEntity> entityTypeExtendsLeashFenceKnotEntity0, Level level1) {
        super(entityTypeExtendsLeashFenceKnotEntity0, level1);
    }

    public LeashFenceKnotEntity(Level level0, BlockPos blockPos1) {
        super(EntityType.LEASH_KNOT, level0, blockPos1);
        this.m_6034_((double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_());
    }

    @Override
    protected void recalculateBoundingBox() {
        this.m_20343_((double) this.f_31698_.m_123341_() + 0.5, (double) this.f_31698_.m_123342_() + 0.375, (double) this.f_31698_.m_123343_() + 0.5);
        double $$0 = (double) this.m_6095_().getWidth() / 2.0;
        double $$1 = (double) this.m_6095_().getHeight();
        this.m_20011_(new AABB(this.m_20185_() - $$0, this.m_20186_(), this.m_20189_() - $$0, this.m_20185_() + $$0, this.m_20186_() + $$1, this.m_20189_() + $$0));
    }

    @Override
    public void setDirection(Direction direction0) {
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
    protected float getEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.0625F;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        return double0 < 1024.0;
    }

    @Override
    public void dropItem(@Nullable Entity entity0) {
        this.m_5496_(SoundEvents.LEASH_KNOT_BREAK, 1.0F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        if (this.m_9236_().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            boolean $$2 = false;
            double $$3 = 7.0;
            List<Mob> $$4 = this.m_9236_().m_45976_(Mob.class, new AABB(this.m_20185_() - 7.0, this.m_20186_() - 7.0, this.m_20189_() - 7.0, this.m_20185_() + 7.0, this.m_20186_() + 7.0, this.m_20189_() + 7.0));
            for (Mob $$5 : $$4) {
                if ($$5.getLeashHolder() == player0) {
                    $$5.setLeashedTo(this, true);
                    $$2 = true;
                }
            }
            boolean $$6 = false;
            if (!$$2) {
                this.m_146870_();
                if (player0.getAbilities().instabuild) {
                    for (Mob $$7 : $$4) {
                        if ($$7.isLeashed() && $$7.getLeashHolder() == this) {
                            $$7.dropLeash(true, false);
                            $$6 = true;
                        }
                    }
                }
            }
            if ($$2 || $$6) {
                this.m_146852_(GameEvent.BLOCK_ATTACH, player0);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean survives() {
        return this.m_9236_().getBlockState(this.f_31698_).m_204336_(BlockTags.FENCES);
    }

    public static LeashFenceKnotEntity getOrCreateKnot(Level level0, BlockPos blockPos1) {
        int $$2 = blockPos1.m_123341_();
        int $$3 = blockPos1.m_123342_();
        int $$4 = blockPos1.m_123343_();
        for (LeashFenceKnotEntity $$6 : level0.m_45976_(LeashFenceKnotEntity.class, new AABB((double) $$2 - 1.0, (double) $$3 - 1.0, (double) $$4 - 1.0, (double) $$2 + 1.0, (double) $$3 + 1.0, (double) $$4 + 1.0))) {
            if ($$6.m_31748_().equals(blockPos1)) {
                return $$6;
            }
        }
        LeashFenceKnotEntity $$7 = new LeashFenceKnotEntity(level0, blockPos1);
        level0.m_7967_($$7);
        return $$7;
    }

    @Override
    public void playPlacementSound() {
        this.m_5496_(SoundEvents.LEASH_KNOT_PLACE, 1.0F, 1.0F);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, 0, this.m_31748_());
    }

    @Override
    public Vec3 getRopeHoldPosition(float float0) {
        return this.m_20318_(float0).add(0.0, 0.2, 0.0);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.LEAD);
    }
}