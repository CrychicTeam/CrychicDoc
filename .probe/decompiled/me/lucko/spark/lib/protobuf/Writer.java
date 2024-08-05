package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CheckReturnValue
interface Writer {

    Writer.FieldOrder fieldOrder();

    void writeSFixed32(int var1, int var2) throws IOException;

    void writeInt64(int var1, long var2) throws IOException;

    void writeSFixed64(int var1, long var2) throws IOException;

    void writeFloat(int var1, float var2) throws IOException;

    void writeDouble(int var1, double var2) throws IOException;

    void writeEnum(int var1, int var2) throws IOException;

    void writeUInt64(int var1, long var2) throws IOException;

    void writeInt32(int var1, int var2) throws IOException;

    void writeFixed64(int var1, long var2) throws IOException;

    void writeFixed32(int var1, int var2) throws IOException;

    void writeBool(int var1, boolean var2) throws IOException;

    void writeString(int var1, String var2) throws IOException;

    void writeBytes(int var1, ByteString var2) throws IOException;

    void writeUInt32(int var1, int var2) throws IOException;

    void writeSInt32(int var1, int var2) throws IOException;

    void writeSInt64(int var1, long var2) throws IOException;

    void writeMessage(int var1, Object var2) throws IOException;

    void writeMessage(int var1, Object var2, Schema var3) throws IOException;

    @Deprecated
    void writeGroup(int var1, Object var2) throws IOException;

    @Deprecated
    void writeGroup(int var1, Object var2, Schema var3) throws IOException;

    @Deprecated
    void writeStartGroup(int var1) throws IOException;

    @Deprecated
    void writeEndGroup(int var1) throws IOException;

    void writeInt32List(int var1, List<Integer> var2, boolean var3) throws IOException;

    void writeFixed32List(int var1, List<Integer> var2, boolean var3) throws IOException;

    void writeInt64List(int var1, List<Long> var2, boolean var3) throws IOException;

    void writeUInt64List(int var1, List<Long> var2, boolean var3) throws IOException;

    void writeFixed64List(int var1, List<Long> var2, boolean var3) throws IOException;

    void writeFloatList(int var1, List<Float> var2, boolean var3) throws IOException;

    void writeDoubleList(int var1, List<Double> var2, boolean var3) throws IOException;

    void writeEnumList(int var1, List<Integer> var2, boolean var3) throws IOException;

    void writeBoolList(int var1, List<Boolean> var2, boolean var3) throws IOException;

    void writeStringList(int var1, List<String> var2) throws IOException;

    void writeBytesList(int var1, List<ByteString> var2) throws IOException;

    void writeUInt32List(int var1, List<Integer> var2, boolean var3) throws IOException;

    void writeSFixed32List(int var1, List<Integer> var2, boolean var3) throws IOException;

    void writeSFixed64List(int var1, List<Long> var2, boolean var3) throws IOException;

    void writeSInt32List(int var1, List<Integer> var2, boolean var3) throws IOException;

    void writeSInt64List(int var1, List<Long> var2, boolean var3) throws IOException;

    void writeMessageList(int var1, List<?> var2) throws IOException;

    void writeMessageList(int var1, List<?> var2, Schema var3) throws IOException;

    @Deprecated
    void writeGroupList(int var1, List<?> var2) throws IOException;

    @Deprecated
    void writeGroupList(int var1, List<?> var2, Schema var3) throws IOException;

    void writeMessageSetItem(int var1, Object var2) throws IOException;

    <K, V> void writeMap(int var1, MapEntryLite.Metadata<K, V> var2, Map<K, V> var3) throws IOException;

    public static enum FieldOrder {

        ASCENDING, DESCENDING
    }
}