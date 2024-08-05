package me.lucko.spark.lib.protobuf;

import java.util.List;

public interface EnumValueOrBuilder extends MessageLiteOrBuilder {

    String getName();

    ByteString getNameBytes();

    int getNumber();

    List<Option> getOptionsList();

    Option getOptions(int var1);

    int getOptionsCount();
}