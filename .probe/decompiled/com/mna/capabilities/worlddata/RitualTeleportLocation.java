package com.mna.capabilities.worlddata;

import com.google.common.collect.ImmutableList;
import com.mna.api.capabilities.IRitualTeleportLocation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class RitualTeleportLocation implements IRitualTeleportLocation {

    private static final String KEY_POSITION_X = "position_x";

    private static final String KEY_POSITION_Y = "position_y";

    private static final String KEY_POSITION_Z = "position_z";

    private static final String KEY_WORLD_REG = "world_registry_key";

    private static final String KEY_WORLD_REGTYPE = "world_registry_key_regtype";

    private static final String KEY_DIRECTION = "direction";

    private static final String KEY_REAGENTS = "reagents";

    private static final String KEY_REAGENT_COUNT = "reagent_count";

    private static final String KEY_REAGENT_PREFIX = "reagent_";

    private final BlockPos pos;

    private final Direction direction;

    private final ImmutableList<ResourceLocation> reagents;

    private ResourceKey<Level> worldType;

    public RitualTeleportLocation(ResourceKey<Level> worldType, BlockPos position, List<ResourceLocation> reagents, Direction direction) {
        this.pos = position;
        this.reagents = ImmutableList.copyOf(reagents);
        this.direction = direction;
        this.worldType = worldType;
    }

    @Override
    public boolean matches(List<ResourceLocation> reagentList) {
        if (reagentList != null && reagentList.size() == this.reagents.size()) {
            for (int i = 0; i < reagentList.size(); i++) {
                ResourceLocation internal = (ResourceLocation) this.reagents.get(i);
                ResourceLocation external = (ResourceLocation) reagentList.get(i);
                if (external == null) {
                    return false;
                }
                if (!internal.toString().equals(external.toString())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean matches(BlockPos pos) {
        return pos == null ? false : this.pos.m_123341_() == pos.m_123341_() && this.pos.m_123342_() == pos.m_123342_() && this.pos.m_123343_() == pos.m_123343_();
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putFloat("direction", this.direction.toYRot());
        nbt.putInt("position_x", this.pos.m_123341_());
        nbt.putInt("position_y", this.pos.m_123342_());
        nbt.putInt("position_z", this.pos.m_123343_());
        nbt.putInt("reagent_count", this.reagents.size());
        nbt.putString("world_registry_key", this.worldType.location().toString());
        nbt.putString("world_registry_key_regtype", this.worldType.location().toString());
        CompoundTag reagentData = new CompoundTag();
        for (int i = 0; i < this.reagents.size(); i++) {
            reagentData.putString("reagent_" + i, ((ResourceLocation) this.reagents.get(i)).toString());
        }
        nbt.put("reagents", reagentData);
    }

    @Nullable
    public static RitualTeleportLocation fromNBT(CompoundTag nbt) {
        if (nbt.contains("world_registry_key") && nbt.contains("world_registry_key_regtype")) {
            ResourceLocation regType = new ResourceLocation(nbt.getString("world_registry_key_regtype"));
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, regType);
            Direction direction = nbt.contains("direction") ? Direction.fromYRot((double) nbt.getFloat("direction")) : Direction.NORTH;
            if (nbt.contains("position_x") && nbt.contains("position_y") && nbt.contains("position_z")) {
                BlockPos position = new BlockPos(nbt.getInt("position_x"), nbt.getInt("position_y"), nbt.getInt("position_z"));
                ArrayList reagents = new ArrayList();
                if (nbt.contains("reagent_count") && nbt.contains("reagents")) {
                    int count = nbt.getInt("reagent_count");
                    CompoundTag subNBT = nbt.getCompound("reagents");
                    for (int i = 0; i < count; i++) {
                        if (!subNBT.contains("reagent_" + i)) {
                            return null;
                        }
                        reagents.add(new ResourceLocation(subNBT.getString("reagent_" + i)));
                    }
                    return new RitualTeleportLocation(key, position, reagents, direction);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public ImmutableList<ResourceLocation> getReagents() {
        return this.reagents;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public ResourceKey<Level> getWorldType() {
        return this.worldType;
    }

    @Override
    public void tryCorrectWorldKey(ResourceKey<Level> world) {
        if (this.worldType.toString().equals(world.toString())) {
            this.worldType = world;
        }
    }
}