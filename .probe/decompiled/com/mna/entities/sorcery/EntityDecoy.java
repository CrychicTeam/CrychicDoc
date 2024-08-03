package com.mna.entities.sorcery;

import com.mna.entities.EntityInit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntityDecoy extends Mob {

    private static final EntityDataAccessor<Optional<UUID>> RENDER_AS_UUID = SynchedEntityData.defineId(EntityDecoy.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(EntityDecoy.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(EntityDecoy.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> MAGNITUDE = SynchedEntityData.defineId(EntityDecoy.class, EntityDataSerializers.FLOAT);

    private Player _cachedRenderAs;

    private HashSet<LivingEntity> _affectedEntities = new HashSet();

    public EntityDecoy(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityDecoy(Player caster, Level world, Vec3 position, float speed, float radius, float magnitude) {
        this(EntityInit.DECOY_ENTITY.get(), world);
        this.m_7678_(position.x, position.y, position.z, caster.m_5675_(0.0F), caster.m_5686_(0.0F));
        this.setPlayer(caster);
        this.f_19804_.set(SPEED, speed);
        this.f_19804_.set(RADIUS, radius);
        this.f_19804_.set(MAGNITUDE, magnitude);
        this._affectedEntities = new HashSet();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_21344_.isDone() && !this.m_9236_().isClientSide()) {
            Vec3 fwd = this.m_20182_().add(Vec3.directionFromRotation(this.m_20155_()).normalize().scale(30.0));
            this.f_21344_.moveTo(fwd.x, fwd.y, fwd.z, (double) this.f_19804_.get(SPEED).floatValue());
        }
        if (!this.m_9236_().isClientSide() && this.f_19797_ % 20 == 0) {
            this.m_9236_().getEntities(this, this.m_20191_().inflate((double) this.f_19804_.get(RADIUS).floatValue(), 2.0, (double) this.f_19804_.get(RADIUS).floatValue()), e -> e instanceof PathfinderMob && Math.random() <= (double) (this.f_19804_.get(MAGNITUDE) / 10.0F)).stream().map(e -> (PathfinderMob) e).forEach(e -> {
                if (!this._affectedEntities.contains(e)) {
                    e.m_6710_(this);
                    this._affectedEntities.add(e);
                }
            });
        }
        if (this.f_19797_ > 300) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public Player getPlayer() {
        if (this._cachedRenderAs == null) {
            Optional<UUID> param = this.f_19804_.get(RENDER_AS_UUID);
            if (!param.isPresent()) {
                return null;
            }
            UUID uuid = (UUID) param.get();
            this._cachedRenderAs = this.m_9236_().m_46003_(uuid);
        }
        return this._cachedRenderAs;
    }

    public void setPlayer(Player player) {
        this.f_19804_.set(RENDER_AS_UUID, Optional.of(player.m_20148_()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(RENDER_AS_UUID, Optional.empty());
        this.f_19804_.define(RADIUS, 3.0F);
        this.f_19804_.define(SPEED, 1.0F);
        this.f_19804_.define(MAGNITUDE, 5.0F);
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}