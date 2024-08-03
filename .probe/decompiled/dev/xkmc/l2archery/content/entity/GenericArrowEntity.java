package dev.xkmc.l2archery.content.entity;

import dev.xkmc.l2archery.content.feature.FeatureList;
import dev.xkmc.l2archery.content.feature.types.FlightControlFeature;
import dev.xkmc.l2archery.content.item.ArrowData;
import dev.xkmc.l2archery.content.item.BowData;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

@FieldsAreNonnullByDefault
public class GenericArrowEntity extends AbstractArrow implements IEntityAdditionalSpawnData {

    public static final String TAG = "l2archery:rawShoot";

    public GenericArrowEntity.ArrowEntityData data = GenericArrowEntity.ArrowEntityData.DEFAULT;

    public FeatureList features = new FeatureList();

    public GenericArrowEntity(EntityType<GenericArrowEntity> type, Level level) {
        super(type, level);
    }

    public GenericArrowEntity(Level level, LivingEntity user, GenericArrowEntity.ArrowEntityData data, FeatureList features) {
        super((EntityType<? extends AbstractArrow>) ArcheryRegister.ET_ARROW.get(), user, level);
        this.data = data;
        this.features = features;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.m_9236_().isClientSide()) {
            this.features.hit().forEach(e -> e.onHitEntity(this, result.getEntity(), result));
        }
        super.onHitEntity(result);
    }

    public void onHurtEntity(CreateSourceEvent ind) {
        if (!this.m_9236_().isClientSide()) {
            this.features.hit().forEach(e -> e.onHurtEntity(this, ind));
        }
    }

    public void onHurtModification(AttackCache cache) {
        if (!this.m_9236_().isClientSide()) {
            this.features.hit().forEach(e -> e.onHurtModifier(this, cache));
        }
    }

    @Override
    public void doPostHurtEffects(LivingEntity target) {
        if (!this.m_9236_().isClientSide()) {
            this.features.hit().forEach(e -> e.postHurtEntity(this, target));
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.data.arrow.stack();
    }

    @Override
    public void tick() {
        Vec3 velocity = this.m_20184_();
        super.tick();
        FlightControlFeature flight = this.features.flight();
        flight.tickMotion(this, velocity);
        if (flight.life > 0 && this.f_19797_ > flight.life) {
            this.m_146870_();
        }
    }

    @Override
    protected void tickDespawn() {
        this.f_36697_++;
        if (this.f_36697_ >= this.features.flight().ground_life) {
            this.m_146870_();
        }
    }

    @Override
    public void shoot(double vx, double vy, double vz, float v, float variation) {
        if (this.m_19880_().contains("l2archery:rawShoot")) {
            this.m_20137_("l2archery:rawShoot");
            if (this.m_19749_() instanceof Mob mob) {
                LivingEntity target = mob.getTarget();
                if (target != null && target.isAlive()) {
                    float speed = this.data.bow().getConfig().speed() / 3.0F * v;
                    float gravity = this.features.flight().gravity;
                    MobShootHelper.shootAimHelper(target, this, (double) speed, (double) gravity);
                    return;
                }
            }
        }
        super.shoot(vx, vy, vz, v, variation);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.features.hit().forEach(e -> e.onHitBlock(this, result));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        Tag data_tag = TagCodec.valueToTag(this.data);
        if (data_tag != null) {
            tag.put("l2archery", data_tag);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("l2archery")) {
            CompoundTag data_tag = tag.getCompound("l2archery");
            GenericArrowEntity.ArrowEntityData temp = (GenericArrowEntity.ArrowEntityData) TagCodec.valueFromTag(data_tag, GenericArrowEntity.ArrowEntityData.class);
            this.data = temp == null ? GenericArrowEntity.ArrowEntityData.DEFAULT : temp;
        }
        this.features = FeatureList.merge(this.data.bow.getFeatures(), this.data.arrow().getFeatures());
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        PacketCodec.to(buffer, this.data);
        Entity owner = this.m_19749_();
        buffer.writeInt(owner == null ? -1 : owner.getId());
        buffer.writeInt(this.m_19880_().size());
        for (String e : this.m_19880_()) {
            buffer.writeUtf(e);
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        GenericArrowEntity.ArrowEntityData temp = (GenericArrowEntity.ArrowEntityData) PacketCodec.from(additionalData, GenericArrowEntity.ArrowEntityData.class, null);
        this.data = temp == null ? GenericArrowEntity.ArrowEntityData.DEFAULT : temp;
        this.features = FeatureList.merge(this.data.bow.getFeatures(), this.data.arrow().getFeatures());
        this.features.shot().forEach(e -> e.onClientShoot(this));
        int id = additionalData.readInt();
        Entity owner = id == -1 ? null : this.m_9236_().getEntity(id);
        this.m_5602_(owner);
        int size = additionalData.readInt();
        for (int i = 0; i < size; i++) {
            this.m_20049_(additionalData.readUtf());
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static record ArrowEntityData(BowData bow, ArrowData arrow, boolean no_consume, float power) {

        public static final GenericArrowEntity.ArrowEntityData DEFAULT = new GenericArrowEntity.ArrowEntityData(BowData.of((GenericBowItem) ArcheryItems.STARTER_BOW.get()), ArrowData.of((Item) ArcheryItems.STARTER_ARROW.get()), false, 1.0F);
    }
}