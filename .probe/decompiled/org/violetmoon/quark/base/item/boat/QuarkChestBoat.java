package org.violetmoon.quark.base.item.boat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.handler.WoodSetHandler;

public class QuarkChestBoat extends ChestBoat implements IQuarkBoat {

    private static final EntityDataAccessor<String> DATA_QUARK_TYPE = SynchedEntityData.defineId(QuarkChestBoat.class, EntityDataSerializers.STRING);

    public QuarkChestBoat(EntityType<? extends Boat> entityType, Level world) {
        super(entityType, world);
    }

    public QuarkChestBoat(Level world, double x, double y, double z) {
        this(WoodSetHandler.quarkChestBoatEntityType, world);
        this.m_6034_(x, y, z);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_QUARK_TYPE, "blossom");
    }

    public String getQuarkBoatType() {
        return this.f_19804_.get(DATA_QUARK_TYPE);
    }

    public void setQuarkBoatType(String type) {
        this.f_19804_.set(DATA_QUARK_TYPE, type);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("QuarkType", this.getQuarkBoatType());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("QuarkType", 8)) {
            this.setQuarkBoatType(tag.getString("QuarkType"));
        }
    }

    @Override
    public ItemEntity spawnAtLocation(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath().contains("_planks") ? super.m_19998_(this.getQuarkBoatTypeObj().planks()) : super.m_19998_(itemLike);
    }

    @NotNull
    @Override
    public Item getDropItem() {
        return this.getQuarkBoatTypeObj().chestBoat();
    }

    @NotNull
    @Override
    public Boat.Type getVariant() {
        return Boat.Type.OAK;
    }

    @Override
    public void setVariant(@NotNull Boat.Type type) {
    }

    @Override
    public void setQuarkBoatTypeObj(WoodSetHandler.QuarkBoatType type) {
        this.setQuarkBoatType(type.name());
    }

    @Override
    public WoodSetHandler.QuarkBoatType getQuarkBoatTypeObj() {
        return WoodSetHandler.getQuarkBoatType(this.getQuarkBoatType());
    }
}