package com.mna.entities.utility;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class DisplayReagents extends Entity {

    public static final int DISPLAY_SWITCH_FREQUENCY = 20;

    private static final int MAX_AGE = 200;

    private static final EntityDataAccessor<CompoundTag> RESOURCE_LOCATIONS = SynchedEntityData.defineId(DisplayReagents.class, EntityDataSerializers.COMPOUND_TAG);

    private static final String NBT_ENTITY_AGE = "entity_age";

    private static final String NBT_LOCATIONS_COUNT = "locations_count";

    private static final String NBT_LOCATIONS_PREFIX = "location_";

    private int age = 0;

    public DisplayReagents(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        this.setAge(this.getAge() + 1);
        if (this.getAge() >= 200) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public void setResourceLocations(List<ResourceLocation> list) {
        CompoundTag data = new CompoundTag();
        this.writeNBT(data, list);
        this.f_19804_.set(RESOURCE_LOCATIONS, data);
    }

    public List<ResourceLocation> getResourceLocations() {
        CompoundTag data = this.f_19804_.get(RESOURCE_LOCATIONS);
        return this.readNBT(data);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(RESOURCE_LOCATIONS, new CompoundTag());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("entity_age")) {
            this.setAge(compound.getInt("entity_age"));
        }
        this.setResourceLocations(this.readNBT(compound));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("entity_age", this.getAge());
        List<ResourceLocation> locations = this.getResourceLocations();
        this.writeNBT(compound, locations);
    }

    private List<ResourceLocation> readNBT(CompoundTag compound) {
        List<ResourceLocation> locations = new ArrayList();
        if (!compound.contains("locations_count")) {
            return locations;
        } else {
            int count = compound.getInt("locations_count");
            for (int i = 0; i < count; i++) {
                if (compound.contains("location_" + i)) {
                    locations.add(new ResourceLocation(compound.getString("location_" + i)));
                }
            }
            return locations;
        }
    }

    private void writeNBT(CompoundTag compound, List<ResourceLocation> locations) {
        compound.putInt("locations_count", locations.size());
        for (int i = 0; i < locations.size(); i++) {
            compound.putString("location_" + i, ((ResourceLocation) locations.get(i)).toString());
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}