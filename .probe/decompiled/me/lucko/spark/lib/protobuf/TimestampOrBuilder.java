package me.lucko.spark.lib.protobuf;

public interface TimestampOrBuilder extends MessageLiteOrBuilder {

    long getSeconds();

    int getNanos();
}