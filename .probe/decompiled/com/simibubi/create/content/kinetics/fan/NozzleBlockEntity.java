package com.simibubi.create.content.kinetics.fan;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class NozzleBlockEntity extends SmartBlockEntity {

    private List<Entity> pushingEntities = new ArrayList();

    private float range;

    private boolean pushing;

    private BlockPos fanPos;

    public NozzleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(5);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (clientPacket) {
            compound.putFloat("Range", this.range);
            compound.putBoolean("Pushing", this.pushing);
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (clientPacket) {
            this.range = compound.getFloat("Range");
            this.pushing = compound.getBoolean("Pushing");
        }
    }

    @Override
    public void initialize() {
        this.fanPos = this.f_58858_.relative(((Direction) this.m_58900_().m_61143_(NozzleBlock.f_52588_)).getOpposite());
        super.initialize();
    }

    @Override
    public void tick() {
        super.tick();
        float range = this.calcRange();
        if (this.range != range) {
            this.setRange(range);
        }
        Vec3 center = VecHelper.getCenterOf(this.f_58858_);
        if (this.f_58857_.isClientSide && range != 0.0F && this.f_58857_.random.nextInt(Mth.clamp(AllConfigs.server().kinetics.fanPushDistance.get() - (int) range, 1, 10)) == 0) {
            Vec3 start = VecHelper.offsetRandomly(center, this.f_58857_.random, this.pushing ? 1.0F : range / 2.0F);
            Vec3 motion = center.subtract(start).normalize().scale((double) (Mth.clamp(range * (this.pushing ? 0.025F : 1.0F), 0.0F, 0.5F) * (float) (this.pushing ? -1 : 1)));
            this.f_58857_.addParticle(ParticleTypes.POOF, start.x, start.y, start.z, motion.x, motion.y, motion.z);
        }
        Iterator<Entity> iterator = this.pushingEntities.iterator();
        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            Vec3 diff = entity.position().subtract(center);
            if (entity instanceof Player || !this.f_58857_.isClientSide) {
                double distance = diff.length();
                if (distance > (double) range || entity.isShiftKeyDown() || AirCurrent.isPlayerCreativeFlying(entity)) {
                    iterator.remove();
                } else if (this.pushing || !(distance < 1.5)) {
                    float factor = entity instanceof ItemEntity ? 0.0078125F : 0.03125F;
                    Vec3 pushVec = diff.normalize().scale(((double) range - distance) * (double) (this.pushing ? 1 : -1));
                    entity.setDeltaMovement(entity.getDeltaMovement().add(pushVec.scale((double) factor)));
                    entity.fallDistance = 0.0F;
                    entity.hurtMarked = true;
                }
            }
        }
    }

    public void setRange(float range) {
        this.range = range;
        if (range == 0.0F) {
            this.pushingEntities.clear();
        }
        this.sendData();
    }

    private float calcRange() {
        if (!(this.f_58857_.getBlockEntity(this.fanPos) instanceof IAirCurrentSource source)) {
            return 0.0F;
        } else if (source.getAirCurrent() == null) {
            return 0.0F;
        } else if (source.getSpeed() == 0.0F) {
            return 0.0F;
        } else {
            this.pushing = source.getAirFlowDirection() == source.getAirflowOriginSide();
            return source.getMaxDistance();
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (this.range != 0.0F) {
            Vec3 center = VecHelper.getCenterOf(this.f_58858_);
            AABB bb = new AABB(center, center).inflate((double) (this.range / 2.0F));
            for (Entity entity : this.f_58857_.m_45976_(Entity.class, bb)) {
                Vec3 diff = entity.position().subtract(center);
                double distance = diff.length();
                if (!(distance > (double) this.range) && !entity.isShiftKeyDown() && !AirCurrent.isPlayerCreativeFlying(entity)) {
                    boolean canSee = this.canSee(entity);
                    if (!canSee) {
                        this.pushingEntities.remove(entity);
                    } else if (!this.pushingEntities.contains(entity)) {
                        this.pushingEntities.add(entity);
                    }
                }
            }
            Iterator<Entity> iterator = this.pushingEntities.iterator();
            while (iterator.hasNext()) {
                Entity entityx = (Entity) iterator.next();
                if (!entityx.isAlive()) {
                    iterator.remove();
                }
            }
            if (!this.pushing && this.pushingEntities.size() > 256 && !this.f_58857_.isClientSide) {
                this.f_58857_.explode(null, center.x, center.y, center.z, 2.0F, Level.ExplosionInteraction.NONE);
                iterator = this.pushingEntities.iterator();
                while (iterator.hasNext()) {
                    Entity entityx = (Entity) iterator.next();
                    entityx.discard();
                    iterator.remove();
                }
            }
        }
    }

    private boolean canSee(Entity entity) {
        ClipContext context = new ClipContext(entity.position(), VecHelper.getCenterOf(this.f_58858_), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
        return this.f_58858_.equals(this.f_58857_.m_45547_(context).getBlockPos());
    }
}