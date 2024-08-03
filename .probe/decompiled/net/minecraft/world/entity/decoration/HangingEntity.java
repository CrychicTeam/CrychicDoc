package net.minecraft.world.entity.decoration;

import com.mojang.logging.LogUtils;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

public abstract class HangingEntity extends Entity {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final Predicate<Entity> HANGING_ENTITY = p_31734_ -> p_31734_ instanceof HangingEntity;

    private int checkInterval;

    protected BlockPos pos;

    protected Direction direction = Direction.SOUTH;

    protected HangingEntity(EntityType<? extends HangingEntity> entityTypeExtendsHangingEntity0, Level level1) {
        super(entityTypeExtendsHangingEntity0, level1);
    }

    protected HangingEntity(EntityType<? extends HangingEntity> entityTypeExtendsHangingEntity0, Level level1, BlockPos blockPos2) {
        this(entityTypeExtendsHangingEntity0, level1);
        this.pos = blockPos2;
    }

    @Override
    protected void defineSynchedData() {
    }

    protected void setDirection(Direction direction0) {
        Validate.notNull(direction0);
        Validate.isTrue(direction0.getAxis().isHorizontal());
        this.direction = direction0;
        this.m_146922_((float) (this.direction.get2DDataValue() * 90));
        this.f_19859_ = this.m_146908_();
        this.recalculateBoundingBox();
    }

    protected void recalculateBoundingBox() {
        if (this.direction != null) {
            double $$0 = (double) this.pos.m_123341_() + 0.5;
            double $$1 = (double) this.pos.m_123342_() + 0.5;
            double $$2 = (double) this.pos.m_123343_() + 0.5;
            double $$3 = 0.46875;
            double $$4 = this.offs(this.getWidth());
            double $$5 = this.offs(this.getHeight());
            $$0 -= (double) this.direction.getStepX() * 0.46875;
            $$2 -= (double) this.direction.getStepZ() * 0.46875;
            $$1 += $$5;
            Direction $$6 = this.direction.getCounterClockWise();
            $$0 += $$4 * (double) $$6.getStepX();
            $$2 += $$4 * (double) $$6.getStepZ();
            this.m_20343_($$0, $$1, $$2);
            double $$7 = (double) this.getWidth();
            double $$8 = (double) this.getHeight();
            double $$9 = (double) this.getWidth();
            if (this.direction.getAxis() == Direction.Axis.Z) {
                $$9 = 1.0;
            } else {
                $$7 = 1.0;
            }
            $$7 /= 32.0;
            $$8 /= 32.0;
            $$9 /= 32.0;
            this.m_20011_(new AABB($$0 - $$7, $$1 - $$8, $$2 - $$9, $$0 + $$7, $$1 + $$8, $$2 + $$9));
        }
    }

    private double offs(int int0) {
        return int0 % 32 == 0 ? 0.5 : 0.0;
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide) {
            this.m_146871_();
            if (this.checkInterval++ == 100) {
                this.checkInterval = 0;
                if (!this.m_213877_() && !this.survives()) {
                    this.m_146870_();
                    this.dropItem(null);
                }
            }
        }
    }

    public boolean survives() {
        if (!this.m_9236_().m_45786_(this)) {
            return false;
        } else {
            int $$0 = Math.max(1, this.getWidth() / 16);
            int $$1 = Math.max(1, this.getHeight() / 16);
            BlockPos $$2 = this.pos.relative(this.direction.getOpposite());
            Direction $$3 = this.direction.getCounterClockWise();
            BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
            for (int $$5 = 0; $$5 < $$0; $$5++) {
                for (int $$6 = 0; $$6 < $$1; $$6++) {
                    int $$7 = ($$0 - 1) / -2;
                    int $$8 = ($$1 - 1) / -2;
                    $$4.set($$2).move($$3, $$5 + $$7).move(Direction.UP, $$6 + $$8);
                    BlockState $$9 = this.m_9236_().getBlockState($$4);
                    if (!$$9.m_280296_() && !DiodeBlock.isDiode($$9)) {
                        return false;
                    }
                }
            }
            return this.m_9236_().getEntities(this, this.m_20191_(), HANGING_ENTITY).isEmpty();
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean skipAttackInteraction(Entity entity0) {
        if (entity0 instanceof Player $$1) {
            return !this.m_9236_().mayInteract($$1, this.pos) ? true : this.hurt(this.m_269291_().playerAttack($$1), 0.0F);
        } else {
            return false;
        }
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else {
            if (!this.m_213877_() && !this.m_9236_().isClientSide) {
                this.m_6074_();
                this.m_5834_();
                this.dropItem(damageSource0.getEntity());
            }
            return true;
        }
    }

    @Override
    public void move(MoverType moverType0, Vec3 vec1) {
        if (!this.m_9236_().isClientSide && !this.m_213877_() && vec1.lengthSqr() > 0.0) {
            this.m_6074_();
            this.dropItem(null);
        }
    }

    @Override
    public void push(double double0, double double1, double double2) {
        if (!this.m_9236_().isClientSide && !this.m_213877_() && double0 * double0 + double1 * double1 + double2 * double2 > 0.0) {
            this.m_6074_();
            this.dropItem(null);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        BlockPos $$1 = this.getPos();
        compoundTag0.putInt("TileX", $$1.m_123341_());
        compoundTag0.putInt("TileY", $$1.m_123342_());
        compoundTag0.putInt("TileZ", $$1.m_123343_());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        BlockPos $$1 = new BlockPos(compoundTag0.getInt("TileX"), compoundTag0.getInt("TileY"), compoundTag0.getInt("TileZ"));
        if (!$$1.m_123314_(this.m_20183_(), 16.0)) {
            LOGGER.error("Hanging entity at invalid position: {}", $$1);
        } else {
            this.pos = $$1;
        }
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void dropItem(@Nullable Entity var1);

    public abstract void playPlacementSound();

    @Override
    public ItemEntity spawnAtLocation(ItemStack itemStack0, float float1) {
        ItemEntity $$2 = new ItemEntity(this.m_9236_(), this.m_20185_() + (double) ((float) this.direction.getStepX() * 0.15F), this.m_20186_() + (double) float1, this.m_20189_() + (double) ((float) this.direction.getStepZ() * 0.15F), itemStack0);
        $$2.setDefaultPickUpDelay();
        this.m_9236_().m_7967_($$2);
        return $$2;
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    @Override
    public void setPos(double double0, double double1, double double2) {
        this.pos = BlockPos.containing(double0, double1, double2);
        this.recalculateBoundingBox();
        this.f_19812_ = true;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public float rotate(Rotation rotation0) {
        if (this.direction.getAxis() != Direction.Axis.Y) {
            switch(rotation0) {
                case CLOCKWISE_180:
                    this.direction = this.direction.getOpposite();
                    break;
                case COUNTERCLOCKWISE_90:
                    this.direction = this.direction.getCounterClockWise();
                    break;
                case CLOCKWISE_90:
                    this.direction = this.direction.getClockWise();
            }
        }
        float $$1 = Mth.wrapDegrees(this.m_146908_());
        switch(rotation0) {
            case CLOCKWISE_180:
                return $$1 + 180.0F;
            case COUNTERCLOCKWISE_90:
                return $$1 + 90.0F;
            case CLOCKWISE_90:
                return $$1 + 270.0F;
            default:
                return $$1;
        }
    }

    @Override
    public float mirror(Mirror mirror0) {
        return this.rotate(mirror0.getRotation(this.direction));
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
    }

    @Override
    public void refreshDimensions() {
    }
}