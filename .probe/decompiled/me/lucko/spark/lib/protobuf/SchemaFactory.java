package me.lucko.spark.lib.protobuf;

@CheckReturnValue
interface SchemaFactory {

    <T> Schema<T> createSchema(Class<T> var1);
}