package me.lucko.spark.lib.protobuf;

@CheckReturnValue
interface MessageInfo {

    ProtoSyntax getSyntax();

    boolean isMessageSetWireFormat();

    MessageLite getDefaultInstance();
}