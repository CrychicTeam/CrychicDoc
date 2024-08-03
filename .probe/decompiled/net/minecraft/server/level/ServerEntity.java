package net.minecraft.server.level;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.VecDeltaCodec;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class ServerEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int TOLERANCE_LEVEL_ROTATION = 1;

    private final ServerLevel level;

    private final Entity entity;

    private final int updateInterval;

    private final boolean trackDelta;

    private final Consumer<Packet<?>> broadcast;

    private final VecDeltaCodec positionCodec = new VecDeltaCodec();

    private int yRotp;

    private int xRotp;

    private int yHeadRotp;

    private Vec3 ap = Vec3.ZERO;

    private int tickCount;

    private int teleportDelay;

    private List<Entity> lastPassengers = Collections.emptyList();

    private boolean wasRiding;

    private boolean wasOnGround;

    @Nullable
    private List<SynchedEntityData.DataValue<?>> trackedDataValues;

    public ServerEntity(ServerLevel serverLevel0, Entity entity1, int int2, boolean boolean3, Consumer<Packet<?>> consumerPacket4) {
        this.level = serverLevel0;
        this.broadcast = consumerPacket4;
        this.entity = entity1;
        this.updateInterval = int2;
        this.trackDelta = boolean3;
        this.positionCodec.setBase(entity1.trackingPosition());
        this.yRotp = Mth.floor(entity1.getYRot() * 256.0F / 360.0F);
        this.xRotp = Mth.floor(entity1.getXRot() * 256.0F / 360.0F);
        this.yHeadRotp = Mth.floor(entity1.getYHeadRot() * 256.0F / 360.0F);
        this.wasOnGround = entity1.onGround();
        this.trackedDataValues = entity1.getEntityData().getNonDefaultValues();
    }

    public void sendChanges() {
        List<Entity> $$0 = this.entity.getPassengers();
        if (!$$0.equals(this.lastPassengers)) {
            this.broadcast.accept(new ClientboundSetPassengersPacket(this.entity));
            removedPassengers($$0, this.lastPassengers).forEach(p_289307_ -> {
                if (p_289307_ instanceof ServerPlayer $$1) {
                    $$1.connection.teleport($$1.m_20185_(), $$1.m_20186_(), $$1.m_20189_(), $$1.m_146908_(), $$1.m_146909_());
                }
            });
            this.lastPassengers = $$0;
        }
        if (this.entity instanceof ItemFrame $$1 && this.tickCount % 10 == 0) {
            ItemStack $$2 = $$1.getItem();
            if ($$2.getItem() instanceof MapItem) {
                Integer $$3 = MapItem.getMapId($$2);
                MapItemSavedData $$4 = MapItem.getSavedData($$3, this.level);
                if ($$4 != null) {
                    for (ServerPlayer $$5 : this.level.players()) {
                        $$4.tickCarriedBy($$5, $$2);
                        Packet<?> $$6 = $$4.getUpdatePacket($$3, $$5);
                        if ($$6 != null) {
                            $$5.connection.send($$6);
                        }
                    }
                }
            }
            this.sendDirtyEntityData();
        }
        if (this.tickCount % this.updateInterval == 0 || this.entity.hasImpulse || this.entity.getEntityData().isDirty()) {
            if (this.entity.isPassenger()) {
                int $$7 = Mth.floor(this.entity.getYRot() * 256.0F / 360.0F);
                int $$8 = Mth.floor(this.entity.getXRot() * 256.0F / 360.0F);
                boolean $$9 = Math.abs($$7 - this.yRotp) >= 1 || Math.abs($$8 - this.xRotp) >= 1;
                if ($$9) {
                    this.broadcast.accept(new ClientboundMoveEntityPacket.Rot(this.entity.getId(), (byte) $$7, (byte) $$8, this.entity.onGround()));
                    this.yRotp = $$7;
                    this.xRotp = $$8;
                }
                this.positionCodec.setBase(this.entity.trackingPosition());
                this.sendDirtyEntityData();
                this.wasRiding = true;
            } else {
                this.teleportDelay++;
                int $$10 = Mth.floor(this.entity.getYRot() * 256.0F / 360.0F);
                int $$11 = Mth.floor(this.entity.getXRot() * 256.0F / 360.0F);
                Vec3 $$12 = this.entity.trackingPosition();
                boolean $$13 = this.positionCodec.delta($$12).lengthSqr() >= 7.6293945E-6F;
                Packet<?> $$14 = null;
                boolean $$15 = $$13 || this.tickCount % 60 == 0;
                boolean $$16 = Math.abs($$10 - this.yRotp) >= 1 || Math.abs($$11 - this.xRotp) >= 1;
                boolean $$17 = false;
                boolean $$18 = false;
                if (this.tickCount > 0 || this.entity instanceof AbstractArrow) {
                    long $$19 = this.positionCodec.encodeX($$12);
                    long $$20 = this.positionCodec.encodeY($$12);
                    long $$21 = this.positionCodec.encodeZ($$12);
                    boolean $$22 = $$19 < -32768L || $$19 > 32767L || $$20 < -32768L || $$20 > 32767L || $$21 < -32768L || $$21 > 32767L;
                    if ($$22 || this.teleportDelay > 400 || this.wasRiding || this.wasOnGround != this.entity.onGround()) {
                        this.wasOnGround = this.entity.onGround();
                        this.teleportDelay = 0;
                        $$14 = new ClientboundTeleportEntityPacket(this.entity);
                        $$17 = true;
                        $$18 = true;
                    } else if ((!$$15 || !$$16) && !(this.entity instanceof AbstractArrow)) {
                        if ($$15) {
                            $$14 = new ClientboundMoveEntityPacket.Pos(this.entity.getId(), (short) ((int) $$19), (short) ((int) $$20), (short) ((int) $$21), this.entity.onGround());
                            $$17 = true;
                        } else if ($$16) {
                            $$14 = new ClientboundMoveEntityPacket.Rot(this.entity.getId(), (byte) $$10, (byte) $$11, this.entity.onGround());
                            $$18 = true;
                        }
                    } else {
                        $$14 = new ClientboundMoveEntityPacket.PosRot(this.entity.getId(), (short) ((int) $$19), (short) ((int) $$20), (short) ((int) $$21), (byte) $$10, (byte) $$11, this.entity.onGround());
                        $$17 = true;
                        $$18 = true;
                    }
                }
                if ((this.trackDelta || this.entity.hasImpulse || this.entity instanceof LivingEntity && ((LivingEntity) this.entity).isFallFlying()) && this.tickCount > 0) {
                    Vec3 $$23 = this.entity.getDeltaMovement();
                    double $$24 = $$23.distanceToSqr(this.ap);
                    if ($$24 > 1.0E-7 || $$24 > 0.0 && $$23.lengthSqr() == 0.0) {
                        this.ap = $$23;
                        this.broadcast.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.ap));
                    }
                }
                if ($$14 != null) {
                    this.broadcast.accept($$14);
                }
                this.sendDirtyEntityData();
                if ($$17) {
                    this.positionCodec.setBase($$12);
                }
                if ($$18) {
                    this.yRotp = $$10;
                    this.xRotp = $$11;
                }
                this.wasRiding = false;
            }
            int $$25 = Mth.floor(this.entity.getYHeadRot() * 256.0F / 360.0F);
            if (Math.abs($$25 - this.yHeadRotp) >= 1) {
                this.broadcast.accept(new ClientboundRotateHeadPacket(this.entity, (byte) $$25));
                this.yHeadRotp = $$25;
            }
            this.entity.hasImpulse = false;
        }
        this.tickCount++;
        if (this.entity.hurtMarked) {
            this.broadcastAndSend(new ClientboundSetEntityMotionPacket(this.entity));
            this.entity.hurtMarked = false;
        }
    }

    private static Stream<Entity> removedPassengers(List<Entity> listEntity0, List<Entity> listEntity1) {
        return listEntity1.stream().filter(p_275361_ -> !listEntity0.contains(p_275361_));
    }

    public void removePairing(ServerPlayer serverPlayer0) {
        this.entity.stopSeenByPlayer(serverPlayer0);
        serverPlayer0.connection.send(new ClientboundRemoveEntitiesPacket(this.entity.getId()));
    }

    public void addPairing(ServerPlayer serverPlayer0) {
        List<Packet<ClientGamePacketListener>> $$1 = new ArrayList();
        this.sendPairingData(serverPlayer0, $$1::add);
        serverPlayer0.connection.send(new ClientboundBundlePacket($$1));
        this.entity.startSeenByPlayer(serverPlayer0);
    }

    public void sendPairingData(ServerPlayer serverPlayer0, Consumer<Packet<ClientGamePacketListener>> consumerPacketClientGamePacketListener1) {
        if (this.entity.isRemoved()) {
            LOGGER.warn("Fetching packet for removed entity {}", this.entity);
        }
        Packet<ClientGamePacketListener> $$2 = this.entity.getAddEntityPacket();
        this.yHeadRotp = Mth.floor(this.entity.getYHeadRot() * 256.0F / 360.0F);
        consumerPacketClientGamePacketListener1.accept($$2);
        if (this.trackedDataValues != null) {
            consumerPacketClientGamePacketListener1.accept(new ClientboundSetEntityDataPacket(this.entity.getId(), this.trackedDataValues));
        }
        boolean $$3 = this.trackDelta;
        if (this.entity instanceof LivingEntity) {
            Collection<AttributeInstance> $$4 = ((LivingEntity) this.entity).getAttributes().getSyncableAttributes();
            if (!$$4.isEmpty()) {
                consumerPacketClientGamePacketListener1.accept(new ClientboundUpdateAttributesPacket(this.entity.getId(), $$4));
            }
            if (((LivingEntity) this.entity).isFallFlying()) {
                $$3 = true;
            }
        }
        this.ap = this.entity.getDeltaMovement();
        if ($$3 && !(this.entity instanceof LivingEntity)) {
            consumerPacketClientGamePacketListener1.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.ap));
        }
        if (this.entity instanceof LivingEntity) {
            List<Pair<EquipmentSlot, ItemStack>> $$5 = Lists.newArrayList();
            for (EquipmentSlot $$6 : EquipmentSlot.values()) {
                ItemStack $$7 = ((LivingEntity) this.entity).getItemBySlot($$6);
                if (!$$7.isEmpty()) {
                    $$5.add(Pair.of($$6, $$7.copy()));
                }
            }
            if (!$$5.isEmpty()) {
                consumerPacketClientGamePacketListener1.accept(new ClientboundSetEquipmentPacket(this.entity.getId(), $$5));
            }
        }
        if (!this.entity.getPassengers().isEmpty()) {
            consumerPacketClientGamePacketListener1.accept(new ClientboundSetPassengersPacket(this.entity));
        }
        if (this.entity.isPassenger()) {
            consumerPacketClientGamePacketListener1.accept(new ClientboundSetPassengersPacket(this.entity.getVehicle()));
        }
        if (this.entity instanceof Mob $$8 && $$8.isLeashed()) {
            consumerPacketClientGamePacketListener1.accept(new ClientboundSetEntityLinkPacket($$8, $$8.getLeashHolder()));
        }
    }

    private void sendDirtyEntityData() {
        SynchedEntityData $$0 = this.entity.getEntityData();
        List<SynchedEntityData.DataValue<?>> $$1 = $$0.packDirty();
        if ($$1 != null) {
            this.trackedDataValues = $$0.getNonDefaultValues();
            this.broadcastAndSend(new ClientboundSetEntityDataPacket(this.entity.getId(), $$1));
        }
        if (this.entity instanceof LivingEntity) {
            Set<AttributeInstance> $$2 = ((LivingEntity) this.entity).getAttributes().getDirtyAttributes();
            if (!$$2.isEmpty()) {
                this.broadcastAndSend(new ClientboundUpdateAttributesPacket(this.entity.getId(), $$2));
            }
            $$2.clear();
        }
    }

    private void broadcastAndSend(Packet<?> packet0) {
        this.broadcast.accept(packet0);
        if (this.entity instanceof ServerPlayer) {
            ((ServerPlayer) this.entity).connection.send(packet0);
        }
    }
}