package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class EntityStoneStatue extends LivingEntity implements IBlacklistedFromStatues {

    private static final EntityDataAccessor<String> TRAPPED_ENTITY_TYPE = SynchedEntityData.defineId(EntityStoneStatue.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<CompoundTag> TRAPPED_ENTITY_DATA = SynchedEntityData.defineId(EntityStoneStatue.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<Float> TRAPPED_ENTITY_WIDTH = SynchedEntityData.defineId(EntityStoneStatue.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> TRAPPED_ENTITY_HEIGHT = SynchedEntityData.defineId(EntityStoneStatue.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> TRAPPED_ENTITY_SCALE = SynchedEntityData.defineId(EntityStoneStatue.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> CRACK_AMOUNT = SynchedEntityData.defineId(EntityStoneStatue.class, EntityDataSerializers.INT);

    private EntityDimensions stoneStatueSize = EntityDimensions.fixed(0.5F, 0.5F);

    public EntityStoneStatue(EntityType<? extends LivingEntity> t, Level worldIn) {
        super(t, worldIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.0).add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    public static EntityStoneStatue buildStatueEntity(LivingEntity parent) {
        EntityStoneStatue statue = IafEntityRegistry.STONE_STATUE.get().create(parent.m_9236_());
        CompoundTag entityTag = new CompoundTag();
        try {
            if (!(parent instanceof Player)) {
                parent.m_20240_(entityTag);
            }
        } catch (Exception var4) {
            IceAndFire.LOGGER.debug("Encountered issue creating stone statue from {}", parent);
        }
        statue.setTrappedTag(entityTag);
        statue.setTrappedEntityTypeString(ForgeRegistries.ENTITY_TYPES.getKey(parent.m_6095_()).toString());
        statue.setTrappedEntityWidth(parent.m_20205_());
        statue.setTrappedHeight(parent.m_20206_());
        statue.setTrappedScale(parent.getScale());
        return statue;
    }

    @Override
    public void push(@NotNull Entity entityIn) {
    }

    @Override
    public void baseTick() {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(TRAPPED_ENTITY_TYPE, "minecraft:pig");
        this.f_19804_.define(TRAPPED_ENTITY_DATA, new CompoundTag());
        this.f_19804_.define(TRAPPED_ENTITY_WIDTH, 0.5F);
        this.f_19804_.define(TRAPPED_ENTITY_HEIGHT, 0.5F);
        this.f_19804_.define(TRAPPED_ENTITY_SCALE, 1.0F);
        this.f_19804_.define(CRACK_AMOUNT, 0);
    }

    public EntityType getTrappedEntityType() {
        String str = this.getTrappedEntityTypeString();
        return (EntityType) EntityType.byString(str).orElse(EntityType.PIG);
    }

    public String getTrappedEntityTypeString() {
        return this.f_19804_.get(TRAPPED_ENTITY_TYPE);
    }

    public void setTrappedEntityTypeString(String string) {
        this.f_19804_.set(TRAPPED_ENTITY_TYPE, string);
    }

    public CompoundTag getTrappedTag() {
        return this.f_19804_.get(TRAPPED_ENTITY_DATA);
    }

    public void setTrappedTag(CompoundTag tag) {
        this.f_19804_.set(TRAPPED_ENTITY_DATA, tag);
    }

    public float getTrappedWidth() {
        return this.f_19804_.get(TRAPPED_ENTITY_WIDTH);
    }

    public void setTrappedEntityWidth(float size) {
        this.f_19804_.set(TRAPPED_ENTITY_WIDTH, size);
    }

    public float getTrappedHeight() {
        return this.f_19804_.get(TRAPPED_ENTITY_HEIGHT);
    }

    public void setTrappedHeight(float size) {
        this.f_19804_.set(TRAPPED_ENTITY_HEIGHT, size);
    }

    public float getTrappedScale() {
        return this.f_19804_.get(TRAPPED_ENTITY_SCALE);
    }

    public void setTrappedScale(float size) {
        this.f_19804_.set(TRAPPED_ENTITY_SCALE, size);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CrackAmount", this.getCrackAmount());
        tag.putFloat("StatueWidth", this.getTrappedWidth());
        tag.putFloat("StatueHeight", this.getTrappedHeight());
        tag.putFloat("StatueScale", this.getTrappedScale());
        tag.putString("StatueEntityType", this.getTrappedEntityTypeString());
        tag.put("StatueEntityTag", this.getTrappedTag());
    }

    @Override
    public float getScale() {
        return this.getTrappedScale();
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setCrackAmount(tag.getByte("CrackAmount"));
        this.setTrappedEntityWidth(tag.getFloat("StatueWidth"));
        this.setTrappedHeight(tag.getFloat("StatueHeight"));
        this.setTrappedScale(tag.getFloat("StatueScale"));
        this.setTrappedEntityTypeString(tag.getString("StatueEntityType"));
        if (tag.contains("StatueEntityTag")) {
            this.setTrappedTag(tag.getCompound("StatueEntityTag"));
        }
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @NotNull
    @Override
    public EntityDimensions getDimensions(@NotNull Pose poseIn) {
        return this.stoneStatueSize;
    }

    @Override
    public void tick() {
        super.tick();
        this.m_146922_(this.f_20883_);
        this.f_20885_ = this.m_146908_();
        if ((double) Math.abs(this.m_20205_() - this.getTrappedWidth()) > 0.01 || (double) Math.abs(this.m_20206_() - this.getTrappedHeight()) > 0.01) {
            double prevX = this.m_20185_();
            double prevZ = this.m_20189_();
            this.stoneStatueSize = EntityDimensions.scalable(this.getTrappedWidth(), this.getTrappedHeight());
            this.m_6210_();
            this.m_6034_(prevX, this.m_20186_(), prevZ);
        }
    }

    @Override
    public void kill() {
        this.m_142687_(Entity.RemovalReason.KILLED);
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

    @NotNull
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public int getCrackAmount() {
        return this.f_19804_.get(CRACK_AMOUNT);
    }

    public void setCrackAmount(int crackAmount) {
        this.f_19804_.set(CRACK_AMOUNT, crackAmount);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }
}