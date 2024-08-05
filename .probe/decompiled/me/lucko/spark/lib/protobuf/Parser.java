package me.lucko.spark.lib.protobuf;

import java.io.InputStream;
import java.nio.ByteBuffer;

public interface Parser<MessageType> {

    MessageType parseFrom(CodedInputStream var1) throws InvalidProtocolBufferException;

    MessageType parseFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(CodedInputStream var1) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parseFrom(ByteBuffer var1) throws InvalidProtocolBufferException;

    MessageType parseFrom(ByteBuffer var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parseFrom(ByteString var1) throws InvalidProtocolBufferException;

    MessageType parseFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(ByteString var1) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parseFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException;

    MessageType parseFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException;

    MessageType parseFrom(byte[] var1) throws InvalidProtocolBufferException;

    MessageType parseFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(byte[] var1) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parseFrom(InputStream var1) throws InvalidProtocolBufferException;

    MessageType parseFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(InputStream var1) throws InvalidProtocolBufferException;

    MessageType parsePartialFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parseDelimitedFrom(InputStream var1) throws InvalidProtocolBufferException;

    MessageType parseDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

    MessageType parsePartialDelimitedFrom(InputStream var1) throws InvalidProtocolBufferException;

    MessageType parsePartialDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;
}