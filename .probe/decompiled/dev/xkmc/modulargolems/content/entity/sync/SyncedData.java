package dev.xkmc.modulargolems.content.entity.sync;

import dev.xkmc.l2library.util.nbt.NBTObj;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.jetbrains.annotations.Nullable;

public class SyncedData {

    public static final SyncedData.Serializer<Integer> INT = new SyncedData.Serializer<>(EntityDataSerializers.INT, IntTag::m_128679_, tag -> tag instanceof NumericTag n ? n.getAsInt() : 0);

    public static final SyncedData.Serializer<BlockPos> BLOCK_POS = new SyncedData.Serializer<>(EntityDataSerializers.BLOCK_POS, pos -> {
        NBTObj ans = new NBTObj();
        ans.fromBlockPos(pos);
        return ans.tag;
    }, tag -> tag instanceof CompoundTag ct ? new NBTObj(ct).toBlockPos() : BlockPos.ZERO);

    public static final SyncedData.Serializer<Optional<UUID>> UUID = new SyncedData.Serializer<>(EntityDataSerializers.OPTIONAL_UUID, uuid -> (Tag) uuid.map(NbtUtils::m_129226_).orElse(null), tag -> Optional.ofNullable(tag).map(NbtUtils::m_129233_));

    private final SyncedData.Definer cls;

    private final List<SyncedData.Data<?>> list = new ArrayList();

    public SyncedData(SyncedData.Definer cls) {
        this.cls = cls;
    }

    public void register(SynchedEntityData data) {
        for (SyncedData.Data<?> entry : this.list) {
            entry.register(data);
        }
    }

    public <T> EntityDataAccessor<T> define(SyncedData.Serializer<T> ser, T init, @Nullable String name) {
        SyncedData.Data<T> data = new SyncedData.Data<>(ser, init, name);
        this.list.add(data);
        return data.data;
    }

    public void write(CompoundTag tag, SynchedEntityData entityData) {
        for (SyncedData.Data<?> entry : this.list) {
            entry.write(tag, entityData);
        }
    }

    public void read(CompoundTag tag, SynchedEntityData entityData) {
        for (SyncedData.Data<?> entry : this.list) {
            entry.read(tag, entityData);
        }
    }

    private class Data<T> {

        private final SyncedData.Serializer<T> ser;

        private final EntityDataAccessor<T> data;

        private final T init;

        private final String name;

        private Data(SyncedData.Serializer<T> ser, T init, @Nullable String name) {
            this.ser = ser;
            this.data = SyncedData.this.cls.define(ser.ser());
            this.init = init;
            this.name = name;
        }

        private void register(SynchedEntityData data) {
            data.define(this.data, this.init);
        }

        public void write(CompoundTag tag, SynchedEntityData entityData) {
            if (this.name != null) {
                Tag ans = this.ser.write(entityData.get(this.data));
                if (ans != null) {
                    tag.put(this.name, ans);
                }
            }
        }

        public void read(CompoundTag tag, SynchedEntityData entityData) {
            if (this.name != null) {
                entityData.set(this.data, this.ser.read(tag.get(this.name)));
            }
        }
    }

    public interface Definer {

        <T> EntityDataAccessor<T> define(EntityDataSerializer<T> var1);
    }

    public static record Serializer<T>(EntityDataSerializer<T> ser, Function<T, Tag> write, Function<Tag, T> read) {

        @Nullable
        public Tag write(T t) {
            return (Tag) this.write.apply(t);
        }

        public T read(@Nullable Tag tag) {
            return (T) this.read.apply(tag);
        }
    }
}