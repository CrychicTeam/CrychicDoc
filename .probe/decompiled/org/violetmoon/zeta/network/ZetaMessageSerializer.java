package org.violetmoon.zeta.network;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.tuple.Pair;

public final class ZetaMessageSerializer {

    private final HashMap<Class<?>, Pair<ZetaMessageSerializer.Reader, ZetaMessageSerializer.Writer>> handlers = new HashMap();

    private final HashMap<Class<?>, Field[]> fieldCache = new HashMap();

    public ZetaMessageSerializer() {
        this.mapFunctions(byte.class, FriendlyByteBuf::readByte, FriendlyByteBuf::writeByte);
        this.mapFunctions(short.class, FriendlyByteBuf::readShort, FriendlyByteBuf::writeShort);
        this.mapFunctions(int.class, FriendlyByteBuf::readInt, FriendlyByteBuf::writeInt);
        this.mapFunctions(long.class, FriendlyByteBuf::readLong, FriendlyByteBuf::writeLong);
        this.mapFunctions(float.class, FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat);
        this.mapFunctions(double.class, FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble);
        this.mapFunctions(boolean.class, FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean);
        this.mapFunctions(char.class, FriendlyByteBuf::readChar, FriendlyByteBuf::writeChar);
        this.mapFunctions(BlockPos.class, FriendlyByteBuf::m_130135_, FriendlyByteBuf::m_130064_);
        this.mapFunctions(Component.class, FriendlyByteBuf::m_130238_, FriendlyByteBuf::m_130083_);
        this.mapFunctions(UUID.class, FriendlyByteBuf::m_130259_, FriendlyByteBuf::m_130077_);
        this.mapFunctions(CompoundTag.class, FriendlyByteBuf::m_130260_, FriendlyByteBuf::m_130079_);
        this.mapFunctions(ItemStack.class, FriendlyByteBuf::m_130267_, ZetaMessageSerializer::writeItemStack);
        this.mapFunctions(String.class, ZetaMessageSerializer::readString, ZetaMessageSerializer::writeString);
        this.mapFunctions(ResourceLocation.class, FriendlyByteBuf::m_130281_, FriendlyByteBuf::m_130085_);
        this.mapFunctions(Date.class, FriendlyByteBuf::m_130282_, FriendlyByteBuf::m_130075_);
        this.mapFunctions(BlockHitResult.class, FriendlyByteBuf::m_130283_, FriendlyByteBuf::m_130062_);
    }

    public <T> T instantiateAndReadObject(Class<T> clazz, FriendlyByteBuf buf) {
        try {
            T msg = (T) clazz.getDeclaredConstructor().newInstance();
            this.readObject(msg, buf);
            return msg;
        } catch (ReflectiveOperationException var4) {
            throw new RuntimeException(var4);
        }
    }

    public void readObject(Object obj, FriendlyByteBuf buf) {
        try {
            Class<?> clazz = obj.getClass();
            Field[] clFields = this.getClassFields(clazz);
            for (Field f : clFields) {
                Class<?> type = f.getType();
                if (this.acceptField(f, type)) {
                    this.readField(obj, f, type, buf);
                }
            }
        } catch (Exception var10) {
            throw new RuntimeException("Error at reading message " + obj, var10);
        }
    }

    public void writeObject(Object obj, FriendlyByteBuf buf) {
        try {
            Class<?> clazz = obj.getClass();
            Field[] clFields = this.getClassFields(clazz);
            for (Field f : clFields) {
                Class<?> type = f.getType();
                if (this.acceptField(f, type)) {
                    this.writeField(obj, f, type, buf);
                }
            }
        } catch (Exception var10) {
            throw new RuntimeException("Error at writing message " + obj, var10);
        }
    }

    private Field[] getClassFields(Class<?> clazz) {
        if (this.fieldCache.containsKey(clazz)) {
            return (Field[]) this.fieldCache.get(clazz);
        } else {
            Field[] fields = clazz.getFields();
            Arrays.sort(fields, Comparator.comparing(Field::getName));
            this.fieldCache.put(clazz, fields);
            return fields;
        }
    }

    private void writeField(Object obj, Field f, Class<?> clazz, FriendlyByteBuf buf) throws IllegalArgumentException, IllegalAccessException {
        Pair<ZetaMessageSerializer.Reader, ZetaMessageSerializer.Writer> handler = this.getHandler(clazz);
        ((ZetaMessageSerializer.Writer) handler.getRight()).write(buf, f, f.get(obj));
    }

    private void readField(Object obj, Field f, Class<?> clazz, FriendlyByteBuf buf) throws IllegalArgumentException, IllegalAccessException {
        Pair<ZetaMessageSerializer.Reader, ZetaMessageSerializer.Writer> handler = this.getHandler(clazz);
        f.set(obj, ((ZetaMessageSerializer.Reader) handler.getLeft()).read(buf, f));
    }

    private Pair<ZetaMessageSerializer.Reader, ZetaMessageSerializer.Writer> getHandler(Class<?> clazz) {
        Pair<ZetaMessageSerializer.Reader, ZetaMessageSerializer.Writer> pair = (Pair<ZetaMessageSerializer.Reader, ZetaMessageSerializer.Writer>) this.handlers.get(clazz);
        if (pair == null) {
            throw new RuntimeException("No R/W handler for  " + clazz);
        } else {
            return pair;
        }
    }

    private boolean acceptField(Field f, Class<?> type) {
        int mods = f.getModifiers();
        return !Modifier.isFinal(mods) && !Modifier.isStatic(mods) && !Modifier.isTransient(mods) ? this.handlers.containsKey(type) : false;
    }

    private <T> void mapFunctions(Class<T> type, Function<FriendlyByteBuf, T> readerLower, BiConsumer<FriendlyByteBuf, T> writerLower) {
        ZetaMessageSerializer.Reader<T> reader = (buf, field) -> (T) readerLower.apply(buf);
        ZetaMessageSerializer.Writer<T> writer = (buf, field, t) -> writerLower.accept(buf, t);
        this.mapHandlers(type, reader, writer);
    }

    private <T> void mapWriterFunction(Class<T> type, ZetaMessageSerializer.Reader<T> reader, BiConsumer<FriendlyByteBuf, T> writerLower) {
        ZetaMessageSerializer.Writer<T> writer = (buf, field, t) -> writerLower.accept(buf, t);
        this.mapHandlers(type, reader, writer);
    }

    private <T> void mapReaderFunction(Class<T> type, Function<FriendlyByteBuf, T> readerLower, ZetaMessageSerializer.Writer<T> writer) {
        ZetaMessageSerializer.Reader<T> reader = (buf, field) -> (T) readerLower.apply(buf);
        this.mapHandlers(type, reader, writer);
    }

    public <T> void mapHandlers(Class<T> type, ZetaMessageSerializer.Reader<T> reader, ZetaMessageSerializer.Writer<T> writer) {
        Class<T[]> arrayType = Array.newInstance(type, 0).getClass();
        ZetaMessageSerializer.Reader<T[]> arrayReader = (ZetaMessageSerializer.Reader<T[]>) ((buf, field) -> {
            int count = buf.readInt();
            T[] arr = (T[]) ((Object[]) Array.newInstance(type, count));
            for (int i = 0; i < count; i++) {
                arr[i] = reader.read(buf, field);
            }
            return arr;
        });
        ZetaMessageSerializer.Writer<T[]> arrayWriter = (buf, field, t) -> {
            int count = t.length;
            buf.writeInt(count);
            for (int i = 0; i < count; i++) {
                writer.write(buf, field, t[i]);
            }
        };
        this.handlers.put(type, Pair.of(reader, writer));
        this.handlers.put(arrayType, Pair.of(arrayReader, arrayWriter));
    }

    private static void writeItemStack(FriendlyByteBuf buf, ItemStack stack) {
        buf.writeItem(stack);
    }

    private static String readString(FriendlyByteBuf buf) {
        return buf.readUtf(32767);
    }

    private static void writeString(FriendlyByteBuf buf, String string) {
        buf.writeUtf(string);
    }

    public interface Reader<T> {

        T read(FriendlyByteBuf var1, Field var2);
    }

    public interface Writer<T> {

        void write(FriendlyByteBuf var1, Field var2, T var3);
    }
}