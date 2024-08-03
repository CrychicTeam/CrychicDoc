package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CheckReturnValue
interface Reader {

    int READ_DONE = Integer.MAX_VALUE;

    int TAG_UNKNOWN = 0;

    boolean shouldDiscardUnknownFields();

    int getFieldNumber() throws IOException;

    int getTag();

    boolean skipField() throws IOException;

    double readDouble() throws IOException;

    float readFloat() throws IOException;

    long readUInt64() throws IOException;

    long readInt64() throws IOException;

    int readInt32() throws IOException;

    long readFixed64() throws IOException;

    int readFixed32() throws IOException;

    boolean readBool() throws IOException;

    String readString() throws IOException;

    String readStringRequireUtf8() throws IOException;

    <T> T readMessageBySchemaWithCheck(Schema<T> var1, ExtensionRegistryLite var2) throws IOException;

    <T> T readMessage(Class<T> var1, ExtensionRegistryLite var2) throws IOException;

    @Deprecated
    <T> T readGroup(Class<T> var1, ExtensionRegistryLite var2) throws IOException;

    @Deprecated
    <T> T readGroupBySchemaWithCheck(Schema<T> var1, ExtensionRegistryLite var2) throws IOException;

    <T> void mergeMessageField(T var1, Schema<T> var2, ExtensionRegistryLite var3) throws IOException;

    <T> void mergeGroupField(T var1, Schema<T> var2, ExtensionRegistryLite var3) throws IOException;

    ByteString readBytes() throws IOException;

    int readUInt32() throws IOException;

    int readEnum() throws IOException;

    int readSFixed32() throws IOException;

    long readSFixed64() throws IOException;

    int readSInt32() throws IOException;

    long readSInt64() throws IOException;

    void readDoubleList(List<Double> var1) throws IOException;

    void readFloatList(List<Float> var1) throws IOException;

    void readUInt64List(List<Long> var1) throws IOException;

    void readInt64List(List<Long> var1) throws IOException;

    void readInt32List(List<Integer> var1) throws IOException;

    void readFixed64List(List<Long> var1) throws IOException;

    void readFixed32List(List<Integer> var1) throws IOException;

    void readBoolList(List<Boolean> var1) throws IOException;

    void readStringList(List<String> var1) throws IOException;

    void readStringListRequireUtf8(List<String> var1) throws IOException;

    <T> void readMessageList(List<T> var1, Schema<T> var2, ExtensionRegistryLite var3) throws IOException;

    <T> void readMessageList(List<T> var1, Class<T> var2, ExtensionRegistryLite var3) throws IOException;

    @Deprecated
    <T> void readGroupList(List<T> var1, Class<T> var2, ExtensionRegistryLite var3) throws IOException;

    @Deprecated
    <T> void readGroupList(List<T> var1, Schema<T> var2, ExtensionRegistryLite var3) throws IOException;

    void readBytesList(List<ByteString> var1) throws IOException;

    void readUInt32List(List<Integer> var1) throws IOException;

    void readEnumList(List<Integer> var1) throws IOException;

    void readSFixed32List(List<Integer> var1) throws IOException;

    void readSFixed64List(List<Long> var1) throws IOException;

    void readSInt32List(List<Integer> var1) throws IOException;

    void readSInt64List(List<Long> var1) throws IOException;

    <K, V> void readMap(Map<K, V> var1, MapEntryLite.Metadata<K, V> var2, ExtensionRegistryLite var3) throws IOException;
}