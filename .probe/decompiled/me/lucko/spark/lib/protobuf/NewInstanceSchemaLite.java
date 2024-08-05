package me.lucko.spark.lib.protobuf;

@CheckReturnValue
final class NewInstanceSchemaLite implements NewInstanceSchema {

    @Override
    public Object newInstance(Object defaultInstance) {
        return ((GeneratedMessageLite) defaultInstance).newMutableInstance();
    }
}