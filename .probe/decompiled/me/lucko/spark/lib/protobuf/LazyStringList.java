package me.lucko.spark.lib.protobuf;

import java.util.Collection;
import java.util.List;

public interface LazyStringList extends ProtocolStringList {

    ByteString getByteString(int var1);

    Object getRaw(int var1);

    byte[] getByteArray(int var1);

    void add(ByteString var1);

    void add(byte[] var1);

    void set(int var1, ByteString var2);

    void set(int var1, byte[] var2);

    boolean addAllByteString(Collection<? extends ByteString> var1);

    boolean addAllByteArray(Collection<byte[]> var1);

    List<?> getUnderlyingElements();

    void mergeFrom(LazyStringList var1);

    List<byte[]> asByteArrayList();

    LazyStringList getUnmodifiableView();
}