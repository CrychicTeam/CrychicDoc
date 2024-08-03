package com.mna.entities.rituals;

import com.mna.ManaAndArtifice;
import com.mna.items.ItemInit;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Vector3f;

public class FlatLands extends Entity {

    private static final EntityDataAccessor<Vector3f> DATA_ID_TARGET = SynchedEntityData.defineId(FlatLands.class, EntityDataSerializers.VECTOR3);

    private BlockPos AABB1;

    private BlockPos AABB2;

    private BlockPos[] targets;

    private int curIndex;

    private int age;

    Player caster_reference;

    UUID casterID;

    public float nextPageTurningSpeed;

    public float pageTurningSpeed;

    public float nextPageAngle;

    public float pageAngle;

    public float flip;

    public float oFlip;

    public float flipT;

    public float flipA;

    public int ticks;

    public int ticksSinceTargetChange;

    public float yaw;

    public FlatLands(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        if (this.m_9236_().isClientSide()) {
            this.ticks++;
            this.updatePageTurning();
        } else if (this.curIndex >= this.targets.length) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            this.age++;
            if (this.getCaster() == null) {
                if (this.age > 20) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            } else {
                if (this.age % 10 == 0) {
                    BlockPos me = this.m_20183_();
                    BlockPos target = this.targets[this.curIndex++];
                    if (this.curIndex < this.targets.length) {
                        this.f_19804_.set(DATA_ID_TARGET, new Vector3f((float) this.targets[this.curIndex].m_123341_() + 0.5F, (float) this.targets[this.curIndex].m_123342_() + 0.5F, (float) this.targets[this.curIndex].m_123343_() + 0.5F));
                    }
                    FlatLandsProjectile proj = new FlatLandsProjectile(this.m_9236_());
                    proj.setPoints(Vec3.atCenterOf(me), target);
                    proj.setCaster(this.getCaster());
                    this.m_9236_().m_7967_(proj);
                }
            }
        }
    }

    public void setBounds(AABB bounds) {
        this.AABB1 = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
        this.AABB2 = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
        double xDim = bounds.getXsize();
        double yDim = bounds.getYsize();
        double zDim = bounds.getZsize();
        ArrayList<BlockPos> allPoints = new ArrayList();
        int x = 1;
        while ((double) x <= xDim - 1.0) {
            for (int y = (int) (yDim - 1.0); y > 0; y -= 3) {
                int z = 1;
                while ((double) z <= zDim - 1.0) {
                    allPoints.add(BlockPos.containing(bounds.minX + (double) x, bounds.minY + (double) y, bounds.minZ + (double) z));
                    z += 3;
                    if ((double) z >= zDim && (double) z < zDim + 2.0 && (zDim - 2.0) % 3.0 != 0.0) {
                        z = (int) (zDim - 1.0);
                    }
                }
            }
            x += 3;
            if ((double) x > xDim - 1.0 && (double) x < xDim + 2.0 && (xDim - 2.0) % 3.0 != 0.0) {
                x = (int) (xDim - 1.0);
            }
        }
        this.targets = new BlockPos[allPoints.size()];
        this.targets = (BlockPos[]) allPoints.toArray(this.targets);
        this.curIndex = 0;
    }

    private Vector3f target() {
        return this.f_19804_.get(DATA_ID_TARGET);
    }

    public void setCaster(Player caster) {
        this.caster_reference = caster;
    }

    private Player getCaster() {
        if (this.caster_reference == null && this.casterID != null) {
            this.caster_reference = this.m_9236_().m_46003_(this.casterID);
        }
        return this.caster_reference;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_ID_TARGET, new Vector3f(0.0F, 0.0F, 0.0F));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("min_x") && compound.contains("min_y") && compound.contains("min_z")) {
            this.AABB1 = new BlockPos(compound.getInt("min_x"), compound.getInt("min_y"), compound.getInt("min_z"));
        }
        if (compound.contains("max_x") && compound.contains("max_y") && compound.contains("max_z")) {
            this.AABB2 = new BlockPos(compound.getInt("max_x"), compound.getInt("max_y"), compound.getInt("max_z"));
        }
        if (this.AABB1 != null && this.AABB2 != null) {
            AABB bb = MathUtils.createInclusiveBB(this.AABB1, this.AABB2);
            this.setBounds(bb);
        }
        if (compound.contains("curIndex")) {
            this.curIndex = compound.getInt("curIndex");
        }
        if (compound.contains("age")) {
            this.age = compound.getInt("age");
        }
        if (compound.contains("caster_uuid")) {
            try {
                this.casterID = UUID.fromString(compound.getString("caster_uuid"));
            } catch (Exception var3) {
                ManaAndArtifice.LOGGER.error("Error loading caster UUID for flat lands projectile!");
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("min_x", this.AABB1.m_123341_());
        compound.putInt("min_y", this.AABB1.m_123342_());
        compound.putInt("min_z", this.AABB1.m_123343_());
        compound.putInt("max_x", this.AABB2.m_123341_());
        compound.putInt("max_y", this.AABB2.m_123342_());
        compound.putInt("max_z", this.AABB2.m_123343_());
        compound.putInt("curIndex", this.curIndex);
        compound.putInt("age", this.age);
        compound.putString("caster_uuid", this.caster_reference != null ? this.caster_reference.m_20148_().toString() : "");
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!this.m_9236_().isClientSide()) {
            ItemEntity item = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), new ItemStack(ItemInit.FLAT_LANDS_BOOK.get()));
            this.m_9236_().m_7967_(item);
        }
    }

    private void updatePageTurning() {
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        this.pageAngle = this.nextPageAngle;
        if (this.target() != null) {
            double d0 = (double) this.target().x - this.m_20185_();
            double d1 = (double) this.target().z() - this.m_20189_();
            this.yaw = (float) Mth.atan2(d1, d0);
            this.nextPageTurningSpeed += 0.1F;
            if (this.nextPageTurningSpeed < 0.5F || this.f_19796_.nextInt(40) == 0) {
                float f1 = this.flipT;
                do {
                    this.flipT = this.flipT + (float) (this.f_19796_.nextInt(4) - this.f_19796_.nextInt(4));
                } while (f1 == this.flipT);
            }
        } else {
            this.yaw += 0.02F;
            this.nextPageTurningSpeed -= 0.1F;
        }
        while (this.nextPageAngle >= (float) Math.PI) {
            this.nextPageAngle -= (float) (Math.PI * 2);
        }
        while (this.nextPageAngle < (float) -Math.PI) {
            this.nextPageAngle += (float) (Math.PI * 2);
        }
        while (this.yaw >= (float) Math.PI) {
            this.yaw -= (float) (Math.PI * 2);
        }
        while (this.yaw < (float) -Math.PI) {
            this.yaw += (float) (Math.PI * 2);
        }
        float f2 = this.yaw - this.nextPageAngle;
        while (f2 >= (float) Math.PI) {
            f2 -= (float) (Math.PI * 2);
        }
        while (f2 < (float) -Math.PI) {
            f2 += (float) (Math.PI * 2);
        }
        this.nextPageAngle += f2 * 0.4F;
        this.nextPageTurningSpeed = Mth.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        this.ticks++;
        this.ticksSinceTargetChange++;
        this.oFlip = this.flip;
        float f = (this.flipT - this.flip) * 0.4F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        this.flipA = this.flipA + (f - this.flipA) * 0.9F;
        this.flip = this.flip + this.flipA;
    }
}