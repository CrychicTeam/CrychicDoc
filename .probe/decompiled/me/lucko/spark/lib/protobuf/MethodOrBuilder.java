package me.lucko.spark.lib.protobuf;

import java.util.List;

public interface MethodOrBuilder extends MessageLiteOrBuilder {

    String getName();

    ByteString getNameBytes();

    String getRequestTypeUrl();

    ByteString getRequestTypeUrlBytes();

    boolean getRequestStreaming();

    String getResponseTypeUrl();

    ByteString getResponseTypeUrlBytes();

    boolean getResponseStreaming();

    List<Option> getOptionsList();

    Option getOptions(int var1);

    int getOptionsCount();

    int getSyntaxValue();

    Syntax getSyntax();
}