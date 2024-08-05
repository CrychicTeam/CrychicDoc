package net.minecraft.resources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.ListBuilder.Builder;
import com.mojang.serialization.RecordBuilder.MapBuilder;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public abstract class DelegatingOps<T> implements DynamicOps<T> {

    protected final DynamicOps<T> delegate;

    protected DelegatingOps(DynamicOps<T> dynamicOpsT0) {
        this.delegate = dynamicOpsT0;
    }

    public T empty() {
        return (T) this.delegate.empty();
    }

    public <U> U convertTo(DynamicOps<U> dynamicOpsU0, T t1) {
        return (U) this.delegate.convertTo(dynamicOpsU0, t1);
    }

    public DataResult<Number> getNumberValue(T t0) {
        return this.delegate.getNumberValue(t0);
    }

    public T createNumeric(Number number0) {
        return (T) this.delegate.createNumeric(number0);
    }

    public T createByte(byte byte0) {
        return (T) this.delegate.createByte(byte0);
    }

    public T createShort(short short0) {
        return (T) this.delegate.createShort(short0);
    }

    public T createInt(int int0) {
        return (T) this.delegate.createInt(int0);
    }

    public T createLong(long long0) {
        return (T) this.delegate.createLong(long0);
    }

    public T createFloat(float float0) {
        return (T) this.delegate.createFloat(float0);
    }

    public T createDouble(double double0) {
        return (T) this.delegate.createDouble(double0);
    }

    public DataResult<Boolean> getBooleanValue(T t0) {
        return this.delegate.getBooleanValue(t0);
    }

    public T createBoolean(boolean boolean0) {
        return (T) this.delegate.createBoolean(boolean0);
    }

    public DataResult<String> getStringValue(T t0) {
        return this.delegate.getStringValue(t0);
    }

    public T createString(String string0) {
        return (T) this.delegate.createString(string0);
    }

    public DataResult<T> mergeToList(T t0, T t1) {
        return this.delegate.mergeToList(t0, t1);
    }

    public DataResult<T> mergeToList(T t0, List<T> listT1) {
        return this.delegate.mergeToList(t0, listT1);
    }

    public DataResult<T> mergeToMap(T t0, T t1, T t2) {
        return this.delegate.mergeToMap(t0, t1, t2);
    }

    public DataResult<T> mergeToMap(T t0, MapLike<T> mapLikeT1) {
        return this.delegate.mergeToMap(t0, mapLikeT1);
    }

    public DataResult<Stream<Pair<T, T>>> getMapValues(T t0) {
        return this.delegate.getMapValues(t0);
    }

    public DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T t0) {
        return this.delegate.getMapEntries(t0);
    }

    public T createMap(Stream<Pair<T, T>> streamPairTT0) {
        return (T) this.delegate.createMap(streamPairTT0);
    }

    public DataResult<MapLike<T>> getMap(T t0) {
        return this.delegate.getMap(t0);
    }

    public DataResult<Stream<T>> getStream(T t0) {
        return this.delegate.getStream(t0);
    }

    public DataResult<Consumer<Consumer<T>>> getList(T t0) {
        return this.delegate.getList(t0);
    }

    public T createList(Stream<T> streamT0) {
        return (T) this.delegate.createList(streamT0);
    }

    public DataResult<ByteBuffer> getByteBuffer(T t0) {
        return this.delegate.getByteBuffer(t0);
    }

    public T createByteList(ByteBuffer byteBuffer0) {
        return (T) this.delegate.createByteList(byteBuffer0);
    }

    public DataResult<IntStream> getIntStream(T t0) {
        return this.delegate.getIntStream(t0);
    }

    public T createIntList(IntStream intStream0) {
        return (T) this.delegate.createIntList(intStream0);
    }

    public DataResult<LongStream> getLongStream(T t0) {
        return this.delegate.getLongStream(t0);
    }

    public T createLongList(LongStream longStream0) {
        return (T) this.delegate.createLongList(longStream0);
    }

    public T remove(T t0, String string1) {
        return (T) this.delegate.remove(t0, string1);
    }

    public boolean compressMaps() {
        return this.delegate.compressMaps();
    }

    public ListBuilder<T> listBuilder() {
        return new Builder(this);
    }

    public RecordBuilder<T> mapBuilder() {
        return new MapBuilder(this);
    }
}