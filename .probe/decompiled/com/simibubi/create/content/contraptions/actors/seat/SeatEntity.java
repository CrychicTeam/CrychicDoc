package com.simibubi.create.content.contraptions.actors.seat;

import com.simibubi.create.AllEntityTypes;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class SeatEntity extends Entity implements IEntityAdditionalSpawnData {

    public SeatEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public SeatEntity(Level world, BlockPos pos) {
        this((EntityType<?>) AllEntityTypes.SEAT.get(), world);
        this.f_19794_ = true;
    }

    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        return builder.sized(0.25F, 0.35F);
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        AABB bb = this.m_20191_();
        Vec3 diff = new Vec3(x, y, z).subtract(bb.getCenter());
        this.m_20011_(bb.move(diff));
    }

    @Override
    protected void positionRider(Entity pEntity, Entity.MoveFunction pCallback) {
        if (this.m_20363_(pEntity)) {
            double d0 = this.m_20186_() + this.m_6048_() + pEntity.getMyRidingOffset();
            pCallback.accept(pEntity, this.m_20185_(), d0 + getCustomEntitySeatOffset(pEntity), this.m_20189_());
        }
    }

    public static double getCustomEntitySeatOffset(Entity entity) {
        if (entity instanceof Slime) {
            return 0.25;
        } else if (entity instanceof Parrot) {
            return 0.0625;
        } else if (entity instanceof Skeleton) {
            return 0.125;
        } else if (entity instanceof Creeper) {
            return 0.125;
        } else if (entity instanceof Cat) {
            return 0.125;
        } else if (entity instanceof Wolf) {
            return 0.0625;
        } else {
            return entity instanceof Frog ? 0.140625 : 0.0;
        }
    }

    @Override
    public void setDeltaMovement(Vec3 p_213317_1_) {
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide) {
            boolean blockPresent = this.m_9236_().getBlockState(this.m_20183_()).m_60734_() instanceof SeatBlock;
            if (!this.m_20160_() || !blockPresent) {
                this.m_146870_();
            }
        }
    }

    @Override
    protected boolean canRide(Entity entity) {
        return !(entity instanceof FakePlayer);
    }

    @Override
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
        if (entity instanceof TamableAnimal ta) {
            ta.setInSittingPose(false);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
        return super.getDismountLocationForPassenger(pLivingEntity).add(0.0, 0.5, 0.0);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
    }

    public static class Render extends EntityRenderer<SeatEntity> {

        public Render(EntityRendererProvider.Context context) {
            super(context);
        }

        public boolean shouldRender(SeatEntity p_225626_1_, Frustum p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
            return false;
        }

        public ResourceLocation getTextureLocation(SeatEntity p_110775_1_) {
            return null;
        }
    }
}