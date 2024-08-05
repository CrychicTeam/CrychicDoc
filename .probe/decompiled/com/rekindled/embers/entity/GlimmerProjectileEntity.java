package com.rekindled.embers.entity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.block.GlimmerBlock;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.particle.SparkParticleOptions;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.joml.Vector3f;

public class GlimmerProjectileEntity extends Projectile {

    public static final GlowParticleOptions EMBER = new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, new Vec3(0.0, 1.0E-6, 0.0), 3.0F, 120);

    public static final SparkParticleOptions GLIMMER = new SparkParticleOptions(new Vector3f(1.0F, 0.5019608F, 0.0627451F), 1.5F);

    public static final SmokeParticleOptions SMOKE = new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 6.0F);

    public static final EntityDataAccessor<Integer> lifetime = SynchedEntityData.defineId(EmberProjectileEntity.class, EntityDataSerializers.INT);

    public GlimmerProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_19794_ = true;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(lifetime, 160);
    }

    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        this.m_5602_(shooter);
        super.shoot((double) x, (double) y, (double) z, velocity, inaccuracy);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    public void tick() {
        super.tick();
        int lifetime = this.m_20088_().get(GlimmerProjectileEntity.lifetime);
        this.m_20088_().set(GlimmerProjectileEntity.lifetime, lifetime - 1);
        if (lifetime <= 0) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        Vec3 oldPosition = new Vec3(this.m_20185_(), this.m_20186_(), this.m_20189_());
        Vec3 newPosVector = oldPosition.add(this.m_20184_());
        BlockHitResult raytraceresult = this.m_9236_().m_45547_(new ClipContext(oldPosition, newPosVector.add(this.m_20184_().normalize().scale(1.5)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
            newPosVector = raytraceresult.m_82450_();
        }
        this.m_6478_(MoverType.SELF, newPosVector.subtract(oldPosition));
        this.m_20256_(this.m_20184_().add(0.0, -0.05F, 0.0));
        if (!this.m_9236_().isClientSide() && raytraceresult != null && raytraceresult.getType() == HitResult.Type.BLOCK && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.m_6532_(raytraceresult);
        }
        if (this.m_9236_().isClientSide()) {
            double deltaX = this.m_20185_() - oldPosition.x;
            double deltaY = this.m_20186_() - oldPosition.y;
            double deltaZ = this.m_20189_() - oldPosition.z;
            for (double i = 0.0; i < 9.0; i++) {
                double coeff = i / 9.0;
                this.m_9236_().addParticle(GLIMMER, oldPosition.x + deltaX * coeff, oldPosition.y + deltaY * coeff, oldPosition.z + deltaZ * coeff, (double) (1.1F * (Misc.random.nextFloat() - 0.5F)), (double) (1.1F * (Misc.random.nextFloat() - 0.5F)), (double) (1.1F * (Misc.random.nextFloat() - 0.5F)));
            }
        }
    }

    @Override
    public void onHitBlock(BlockHitResult raytraceresult) {
        super.onHitBlock(raytraceresult);
        Direction side = raytraceresult.getDirection();
        BlockPos hitPos = raytraceresult.getBlockPos().relative(side);
        BlockPlaceContext context = new BlockPlaceContext(this.m_9236_(), this.m_19749_() instanceof Player ? (Player) this.m_19749_() : null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, raytraceresult);
        if (this.m_9236_().getBlockState(hitPos).m_60629_(context)) {
            this.m_9236_().setBlock(hitPos, RegistryManager.GLIMMER.get().getStateForPlacement(context), 11);
            if (this.m_9236_() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(GlimmerBlock.GLIMMER, (double) hitPos.m_123341_() + 0.5, (double) hitPos.m_123342_() + 0.5, (double) hitPos.m_123343_() + 0.5, 1, 0.0, 0.0, 0.0, 0.0);
                serverLevel.sendParticles(EMBER, (double) hitPos.m_123341_() + 0.5, (double) hitPos.m_123342_() + 0.5, (double) hitPos.m_123343_() + 0.5, 1, 0.0, 0.001, 0.0, 0.0);
            }
        } else if (this.m_9236_() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(SMOKE, this.m_20185_(), this.m_20186_(), this.m_20189_(), 6, 0.0, 0.0, 0.0, 0.0);
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}