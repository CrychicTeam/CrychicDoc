package me.lucko.spark.lib.protobuf;

public interface AnyOrBuilder extends MessageLiteOrBuilder {

    String getTypeUrl();

    ByteString getTypeUrlBytes();

    ByteString getValue();
}