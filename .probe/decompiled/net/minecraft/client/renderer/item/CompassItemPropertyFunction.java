package net.minecraft.client.renderer.item;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class CompassItemPropertyFunction implements ClampedItemPropertyFunction {

    public static final int DEFAULT_ROTATION = 0;

    private final CompassItemPropertyFunction.CompassWobble wobble = new CompassItemPropertyFunction.CompassWobble();

    private final CompassItemPropertyFunction.CompassWobble wobbleRandom = new CompassItemPropertyFunction.CompassWobble();

    public final CompassItemPropertyFunction.CompassTarget compassTarget;

    public CompassItemPropertyFunction(CompassItemPropertyFunction.CompassTarget compassItemPropertyFunctionCompassTarget0) {
        this.compassTarget = compassItemPropertyFunctionCompassTarget0;
    }

    @Override
    public float unclampedCall(ItemStack itemStack0, @Nullable ClientLevel clientLevel1, @Nullable LivingEntity livingEntity2, int int3) {
        Entity $$4 = (Entity) (livingEntity2 != null ? livingEntity2 : itemStack0.getEntityRepresentation());
        if ($$4 == null) {
            return 0.0F;
        } else {
            clientLevel1 = this.tryFetchLevelIfMissing($$4, clientLevel1);
            return clientLevel1 == null ? 0.0F : this.getCompassRotation(itemStack0, clientLevel1, int3, $$4);
        }
    }

    private float getCompassRotation(ItemStack itemStack0, ClientLevel clientLevel1, int int2, Entity entity3) {
        GlobalPos $$4 = this.compassTarget.getPos(clientLevel1, itemStack0, entity3);
        long $$5 = clientLevel1.m_46467_();
        return !this.isValidCompassTargetPos(entity3, $$4) ? this.getRandomlySpinningRotation(int2, $$5) : this.getRotationTowardsCompassTarget(entity3, $$5, $$4.pos());
    }

    private float getRandomlySpinningRotation(int int0, long long1) {
        if (this.wobbleRandom.shouldUpdate(long1)) {
            this.wobbleRandom.update(long1, Math.random());
        }
        double $$2 = this.wobbleRandom.rotation + (double) ((float) this.hash(int0) / 2.1474836E9F);
        return Mth.positiveModulo((float) $$2, 1.0F);
    }

    private float getRotationTowardsCompassTarget(Entity entity0, long long1, BlockPos blockPos2) {
        double $$3 = this.getAngleFromEntityToPos(entity0, blockPos2);
        double $$4 = this.getWrappedVisualRotationY(entity0);
        if (entity0 instanceof Player $$5 && $$5.isLocalPlayer()) {
            if (this.wobble.shouldUpdate(long1)) {
                this.wobble.update(long1, 0.5 - ($$4 - 0.25));
            }
            double $$6 = $$3 + this.wobble.rotation;
            return Mth.positiveModulo((float) $$6, 1.0F);
        }
        double $$7 = 0.5 - ($$4 - 0.25 - $$3);
        return Mth.positiveModulo((float) $$7, 1.0F);
    }

    @Nullable
    private ClientLevel tryFetchLevelIfMissing(Entity entity0, @Nullable ClientLevel clientLevel1) {
        return clientLevel1 == null && entity0.level() instanceof ClientLevel ? (ClientLevel) entity0.level() : clientLevel1;
    }

    private boolean isValidCompassTargetPos(Entity entity0, @Nullable GlobalPos globalPos1) {
        return globalPos1 != null && globalPos1.dimension() == entity0.level().dimension() && !(globalPos1.pos().m_203193_(entity0.position()) < 1.0E-5F);
    }

    private double getAngleFromEntityToPos(Entity entity0, BlockPos blockPos1) {
        Vec3 $$2 = Vec3.atCenterOf(blockPos1);
        return Math.atan2($$2.z() - entity0.getZ(), $$2.x() - entity0.getX()) / (float) (Math.PI * 2);
    }

    private double getWrappedVisualRotationY(Entity entity0) {
        return Mth.positiveModulo((double) (entity0.getVisualRotationYInDegrees() / 360.0F), 1.0);
    }

    private int hash(int int0) {
        return int0 * 1327217883;
    }

    public interface CompassTarget {

        @Nullable
        GlobalPos getPos(ClientLevel var1, ItemStack var2, Entity var3);
    }

    static class CompassWobble {

        double rotation;

        private double deltaRotation;

        private long lastUpdateTick;

        boolean shouldUpdate(long long0) {
            return this.lastUpdateTick != long0;
        }

        void update(long long0, double double1) {
            this.lastUpdateTick = long0;
            double $$2 = double1 - this.rotation;
            $$2 = Mth.positiveModulo($$2 + 0.5, 1.0) - 0.5;
            this.deltaRotation += $$2 * 0.1;
            this.deltaRotation *= 0.8;
            this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0);
        }
    }
}