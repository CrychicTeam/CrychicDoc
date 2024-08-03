package com.simibubi.create.compat.computercraft.implementation.peripherals;

import com.simibubi.create.AllPackets;
import com.simibubi.create.compat.computercraft.implementation.CreateLuaTable;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.schedule.Schedule;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.station.TrainEditPacket;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.StringHelper;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class StationPeripheral extends SyncedPeripheral<StationBlockEntity> {

    public StationPeripheral(StationBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction(mainThread = true)
    public final void assemble() throws LuaException {
        if (!this.blockEntity.isAssembling()) {
            throw new LuaException("station must be in assembly mode");
        } else {
            this.blockEntity.assemble(null);
            if (this.blockEntity.getStation() == null || this.blockEntity.getStation().getPresentTrain() == null) {
                throw new LuaException("failed to assemble train");
            } else if (!this.blockEntity.exitAssemblyMode()) {
                throw new LuaException("failed to exit assembly mode");
            }
        }
    }

    @LuaFunction(mainThread = true)
    public final void disassemble() throws LuaException {
        if (this.blockEntity.isAssembling()) {
            throw new LuaException("station must not be in assembly mode");
        } else {
            this.getTrainOrThrow();
            if (!this.blockEntity.enterAssemblyMode(null)) {
                throw new LuaException("could not disassemble train");
            }
        }
    }

    @LuaFunction(mainThread = true)
    public final void setAssemblyMode(boolean assemblyMode) throws LuaException {
        if (assemblyMode) {
            if (!this.blockEntity.enterAssemblyMode(null)) {
                throw new LuaException("failed to enter assembly mode");
            }
        } else if (!this.blockEntity.exitAssemblyMode()) {
            throw new LuaException("failed to exit assembly mode");
        }
    }

    @LuaFunction
    public final boolean isInAssemblyMode() {
        return this.blockEntity.isAssembling();
    }

    @LuaFunction
    public final String getStationName() throws LuaException {
        GlobalStation station = this.blockEntity.getStation();
        if (station == null) {
            throw new LuaException("station is not connected to a track");
        } else {
            return station.name;
        }
    }

    @LuaFunction(mainThread = true)
    public final void setStationName(String name) throws LuaException {
        if (!this.blockEntity.updateName(name)) {
            throw new LuaException("could not set station name");
        }
    }

    @LuaFunction
    public final boolean isTrainPresent() throws LuaException {
        GlobalStation station = this.blockEntity.getStation();
        if (station == null) {
            throw new LuaException("station is not connected to a track");
        } else {
            return station.getPresentTrain() != null;
        }
    }

    @LuaFunction
    public final boolean isTrainImminent() throws LuaException {
        GlobalStation station = this.blockEntity.getStation();
        if (station == null) {
            throw new LuaException("station is not connected to a track");
        } else {
            return station.getImminentTrain() != null;
        }
    }

    @LuaFunction
    public final boolean isTrainEnroute() throws LuaException {
        GlobalStation station = this.blockEntity.getStation();
        if (station == null) {
            throw new LuaException("station is not connected to a track");
        } else {
            return station.getNearestTrain() != null;
        }
    }

    @LuaFunction
    public final String getTrainName() throws LuaException {
        Train train = this.getTrainOrThrow();
        return train.name.getString();
    }

    @LuaFunction(mainThread = true)
    public final void setTrainName(String name) throws LuaException {
        Train train = this.getTrainOrThrow();
        train.name = Components.literal(name);
        AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainEditPacket.TrainEditReturnPacket(train.id, name, train.icon.getId()));
    }

    @LuaFunction
    public final boolean hasSchedule() throws LuaException {
        Train train = this.getTrainOrThrow();
        return train.runtime.getSchedule() != null;
    }

    @LuaFunction
    public final CreateLuaTable getSchedule() throws LuaException {
        Train train = this.getTrainOrThrow();
        Schedule schedule = train.runtime.getSchedule();
        if (schedule == null) {
            throw new LuaException("train doesn't have a schedule");
        } else {
            return fromCompoundTag(schedule.write());
        }
    }

    @LuaFunction(mainThread = true)
    public final void setSchedule(IArguments arguments) throws LuaException {
        Train train = this.getTrainOrThrow();
        Schedule schedule = Schedule.fromTag(toCompoundTag(new CreateLuaTable(arguments.getTable(0))));
        boolean autoSchedule = train.runtime.getSchedule() == null || train.runtime.isAutoSchedule;
        train.runtime.setSchedule(schedule, autoSchedule);
    }

    @NotNull
    private Train getTrainOrThrow() throws LuaException {
        GlobalStation station = this.blockEntity.getStation();
        if (station == null) {
            throw new LuaException("station is not connected to a track");
        } else {
            Train train = station.getPresentTrain();
            if (train == null) {
                throw new LuaException("there is no train present");
            } else {
                return train;
            }
        }
    }

    @NotNull
    private static CreateLuaTable fromCompoundTag(CompoundTag tag) throws LuaException {
        return (CreateLuaTable) fromNBTTag(null, tag);
    }

    @NotNull
    private static Object fromNBTTag(@Nullable String key, Tag tag) throws LuaException {
        byte type = tag.getId();
        if (type == 1 && key != null && key.equals("Count")) {
            return ((NumericTag) tag).getAsByte();
        } else if (type == 1) {
            return ((NumericTag) tag).getAsByte() != 0;
        } else if (type == 2 || type == 3 || type == 4) {
            return ((NumericTag) tag).getAsLong();
        } else if (type == 5 || type == 6) {
            return ((NumericTag) tag).getAsDouble();
        } else if (type == 8) {
            return tag.getAsString();
        } else if (type == 9 || type == 7 || type == 11 || type == 12) {
            CreateLuaTable list = new CreateLuaTable();
            CollectionTag<?> listTag = (CollectionTag<?>) tag;
            for (int i = 0; i < listTag.size(); i++) {
                list.put(i + 1, fromNBTTag(null, (Tag) listTag.get(i)));
            }
            return list;
        } else if (type != 10) {
            throw new LuaException("unknown tag type " + tag.getType().getName());
        } else {
            CreateLuaTable table = new CreateLuaTable();
            CompoundTag compoundTag = (CompoundTag) tag;
            for (String compoundKey : compoundTag.getAllKeys()) {
                table.put(StringHelper.camelCaseToSnakeCase(compoundKey), fromNBTTag(compoundKey, compoundTag.get(compoundKey)));
            }
            return table;
        }
    }

    @NotNull
    private static CompoundTag toCompoundTag(CreateLuaTable table) throws LuaException {
        return (CompoundTag) toNBTTag(null, table.getMap());
    }

    @NotNull
    private static Tag toNBTTag(@Nullable String key, Object value) throws LuaException {
        if (value instanceof Boolean v) {
            return ByteTag.valueOf(v);
        } else if (!(value instanceof Byte) && (key == null || !key.equals("count"))) {
            if (value instanceof Number v) {
                return (Tag) ((double) v.intValue() == v.doubleValue() ? IntTag.valueOf(v.intValue()) : DoubleTag.valueOf(v.doubleValue()));
            } else if (value instanceof String v) {
                return StringTag.valueOf(v);
            } else {
                if (value instanceof Map<?, ?> v && v.containsKey(1.0)) {
                    ListTag list = new ListTag();
                    for (Object o : v.values()) {
                        list.add(toNBTTag(null, o));
                    }
                    return list;
                }
                if (!(value instanceof Map<?, ?> v)) {
                    throw new LuaException("unknown object type " + value.getClass().getName());
                } else {
                    CompoundTag compound = new CompoundTag();
                    for (Object objectKey : v.keySet()) {
                        if (!(objectKey instanceof String compoundKey)) {
                            throw new LuaException("table key is not of type string");
                        }
                        compound.put(compoundKey.equals("id") && v.containsKey("count") ? "id" : StringHelper.snakeCaseToCamelCase(compoundKey), toNBTTag(compoundKey, v.get(compoundKey)));
                    }
                    return compound;
                }
            }
        } else {
            return ByteTag.valueOf(((Number) value).byteValue());
        }
    }

    @NotNull
    public String getType() {
        return "Create_Station";
    }
}