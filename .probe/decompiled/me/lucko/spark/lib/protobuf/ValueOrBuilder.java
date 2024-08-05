package me.lucko.spark.lib.protobuf;

public interface ValueOrBuilder extends MessageLiteOrBuilder {

    boolean hasNullValue();

    int getNullValueValue();

    NullValue getNullValue();

    boolean hasNumberValue();

    double getNumberValue();

    boolean hasStringValue();

    String getStringValue();

    ByteString getStringValueBytes();

    boolean hasBoolValue();

    boolean getBoolValue();

    boolean hasStructValue();

    Struct getStructValue();

    boolean hasListValue();

    ListValue getListValue();

    Value.KindCase getKindCase();
}