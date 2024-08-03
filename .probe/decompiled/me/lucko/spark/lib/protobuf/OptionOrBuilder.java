package me.lucko.spark.lib.protobuf;

public interface OptionOrBuilder extends MessageLiteOrBuilder {

    String getName();

    ByteString getNameBytes();

    boolean hasValue();

    Any getValue();
}