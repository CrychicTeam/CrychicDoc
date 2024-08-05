package me.lucko.spark.lib.protobuf;

import java.util.List;

public interface ListValueOrBuilder extends MessageLiteOrBuilder {

    List<Value> getValuesList();

    Value getValues(int var1);

    int getValuesCount();
}