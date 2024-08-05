package me.lucko.spark.lib.protobuf;

import java.util.List;

public interface FieldMaskOrBuilder extends MessageLiteOrBuilder {

    List<String> getPathsList();

    int getPathsCount();

    String getPaths(int var1);

    ByteString getPathsBytes(int var1);
}