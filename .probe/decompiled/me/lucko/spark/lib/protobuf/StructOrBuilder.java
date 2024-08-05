package me.lucko.spark.lib.protobuf;

import java.util.Map;

public interface StructOrBuilder extends MessageLiteOrBuilder {

    int getFieldsCount();

    boolean containsFields(String var1);

    @Deprecated
    Map<String, Value> getFields();

    Map<String, Value> getFieldsMap();

    Value getFieldsOrDefault(String var1, Value var2);

    Value getFieldsOrThrow(String var1);
}