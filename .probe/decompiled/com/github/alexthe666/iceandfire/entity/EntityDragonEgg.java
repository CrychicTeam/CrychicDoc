package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.collect.ImmutableList;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EntityDragonEgg extends LivingEntity implements IBlacklistedFromStatues, IDeadMob {

    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityDragonEgg.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> DRAGON_TYPE = SynchedEntityData.defineId(EntityDragonEgg.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DRAGON_AGE = SynchedEntityData.defineId(EntityDragonEgg.class, EntityDataSerializers.INT);

    public EntityDragonEgg(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Color", (byte) this.getEggType().ordinal());
        tag.putInt("DragonAge", this.getDragonAge());
        try {
            if (this.getOwnerId() == null) {
                tag.putString("OwnerUUID", "");
            } else {
                tag.putString("OwnerUUID", this.getOwnerId().toString());
            }
        } catch (Exception var3) {
            IceAndFire.LOGGER.error("An error occurred while trying to read the NBT data of a dragon egg", var3);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setEggType(EnumDragonEgg.values()[tag.getInt("Color")]);
        this.setDragonAge(tag.getInt("DragonAge"));
        String s;
        if (tag.contains("OwnerUUID", 8)) {
            s = tag.getString("OwnerUUID");
        } else {
            String s1 = tag.getString("Owner");
            UUID converedUUID = OldUsersConverter.convertMobOwnerIfNecessary(this.m_20194_(), s1);
            s = converedUUID == null ? s1 : converedUUID.toString();
        }
        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(DRAGON_TYPE, 0);
        this.m_20088_().define(DRAGON_AGE, 0);
        this.m_20088_().define(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Nullable
    public UUID getOwnerId() {
        return (UUID) this.f_19804_.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.f_19804_.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public EnumDragonEgg getEggType() {
        return EnumDragonEgg.values()[this.m_20088_().get(DRAGON_TYPE)];
    }

    public void setEggType(EnumDragonEgg newtype) {
        this.m_20088_().set(DRAGON_TYPE, newtype.ordinal());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getEntity() != null && super.m_6673_(i);
    }

    public int getDragonAge() {
        return this.m_20088_().get(DRAGON_AGE);
    }

    public void setDragonAge(int i) {
        this.m_20088_().set(DRAGON_AGE, i);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide()) {
            this.m_20301_(200);
            this.updateEggCondition();
        }
    }

    public void updateEggCondition() {
        DragonType dragonType = this.getEggType().dragonType;
        if (dragonType == DragonType.FIRE) {
            if (this.m_9236_().getBlockState(this.m_20183_()).isBurning(this.m_9236_(), this.m_20183_())) {
                this.setDragonAge(this.getDragonAge() + 1);
            }
        } else if (dragonType == DragonType.ICE) {
            BlockState state = this.m_9236_().getBlockState(this.m_20183_());
            if (state.m_60713_(Blocks.WATER) && this.m_217043_().nextInt(500) == 0) {
                this.m_9236_().setBlockAndUpdate(this.m_20183_(), IafBlockRegistry.EGG_IN_ICE.get().defaultBlockState());
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_(), SoundEvents.GLASS_BREAK, this.m_5720_(), 2.5F, 1.0F, false);
                if (this.m_9236_().getBlockEntity(this.m_20183_()) instanceof TileEntityEggInIce eggInIce) {
                    eggInIce.type = this.getEggType();
                    eggInIce.ownerUUID = this.getOwnerId();
                }
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        } else if (dragonType == DragonType.LIGHTNING) {
            BlockPos.MutableBlockPos mutablePosition = new BlockPos.MutableBlockPos(this.m_20185_(), this.m_20186_(), this.m_20189_());
            boolean isRainingAt = this.m_9236_().isRainingAt(mutablePosition) || this.m_9236_().isRainingAt(mutablePosition.set(this.m_20185_(), this.m_20186_() + (double) this.m_20206_(), this.m_20189_()));
            if (this.m_9236_().m_45527_(this.m_20183_().above()) && isRainingAt) {
                this.setDragonAge(this.getDragonAge() + 1);
            }
        }
        if (this.getDragonAge() > IafConfig.dragonEggTime) {
            this.m_9236_().setBlockAndUpdate(this.m_20183_(), Blocks.AIR.defaultBlockState());
            EntityDragonBase dragon = dragonType.getEntity().create(this.m_9236_());
            if (this.m_8077_()) {
                dragon.m_6593_(this.m_7770_());
            }
            if (dragonType == DragonType.LIGHTNING) {
                dragon.setVariant(this.getEggType().ordinal() - 8);
            } else {
                dragon.setVariant(this.getEggType().ordinal());
            }
            dragon.setGender(this.m_217043_().nextBoolean());
            dragon.m_6034_((double) this.m_20183_().m_123341_() + 0.5, (double) (this.m_20183_().m_123342_() + 1), (double) this.m_20183_().m_123343_() + 0.5);
            dragon.setHunger(50);
            if (!this.m_9236_().isClientSide()) {
                this.m_9236_().m_7967_(dragon);
            }
            if (this.m_8077_()) {
                dragon.m_6593_(this.m_7770_());
            }
            dragon.m_7105_(true);
            dragon.m_21816_(this.getOwnerId());
            if (dragonType == DragonType.LIGHTNING) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.m_9236_());
                bolt.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                bolt.setVisualOnly(true);
                if (!this.m_9236_().isClientSide()) {
                    this.m_9236_().m_7967_(bolt);
                }
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_(), SoundEvents.LIGHTNING_BOLT_THUNDER, this.m_5720_(), 2.5F, 1.0F, false);
            } else if (dragonType == DragonType.FIRE) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_(), SoundEvents.FIRE_EXTINGUISH, this.m_5720_(), 2.5F, 1.0F, false);
            }
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_(), IafSoundRegistry.EGG_HATCH, this.m_5720_(), 2.5F, 1.0F, false);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return ImmutableList.of();
    }

    @NotNull
    @Override
    public ItemStack getItemBySlot(@NotNull EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slotIn, @NotNull ItemStack stack) {
    }

    @Override
    public boolean hurt(@NotNull DamageSource var1, float var2) {
        if (var1.is(DamageTypeTags.IS_FIRE) && this.getEggType().dragonType == DragonType.FIRE) {
            return false;
        } else {
            if (!this.m_9236_().isClientSide && !var1.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !this.m_213877_()) {
                this.m_20000_(this.getItem().getItem(), 1);
            }
            this.m_142687_(Entity.RemovalReason.KILLED);
            return true;
        }
    }

    private ItemStack getItem() {
        return switch(this.getEggType()) {
            case GREEN ->
                new ItemStack(IafItemRegistry.DRAGONEGG_GREEN.get());
            case BRONZE ->
                new ItemStack(IafItemRegistry.DRAGONEGG_BRONZE.get());
            case GRAY ->
                new ItemStack(IafItemRegistry.DRAGONEGG_GRAY.get());
            case BLUE ->
                new ItemStack(IafItemRegistry.DRAGONEGG_BLUE.get());
            case WHITE ->
                new ItemStack(IafItemRegistry.DRAGONEGG_WHITE.get());
            case SAPPHIRE ->
                new ItemStack(IafItemRegistry.DRAGONEGG_SAPPHIRE.get());
            case SILVER ->
                new ItemStack(IafItemRegistry.DRAGONEGG_SILVER.get());
            case ELECTRIC ->
                new ItemStack(IafItemRegistry.DRAGONEGG_ELECTRIC.get());
            case AMYTHEST ->
                new ItemStack(IafItemRegistry.DRAGONEGG_AMYTHEST.get());
            case COPPER ->
                new ItemStack(IafItemRegistry.DRAGONEGG_COPPER.get());
            case BLACK ->
                new ItemStack(IafItemRegistry.DRAGONEGG_BLACK.get());
            default ->
                new ItemStack(IafItemRegistry.DRAGONEGG_RED.get());
        };
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @NotNull
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(Player player) {
        this.setOwnerId(player.m_20148_());
    }

    @Override
    public boolean isMobDead() {
        return true;
    }
}