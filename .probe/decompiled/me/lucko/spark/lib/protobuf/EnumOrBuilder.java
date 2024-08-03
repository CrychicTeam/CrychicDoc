package me.lucko.spark.lib.protobuf;

import java.util.List;

public interface EnumOrBuilder extends MessageLiteOrBuilder {

    String getName();

    ByteString getNameBytes();

    List<EnumValue> getEnumvalueList();

    EnumValue getEnumvalue(int var1);

    int getEnumvalueCount();

    List<Option> getOptionsList();

    Option getOptions(int var1);

    int getOptionsCount();

    boolean hasSourceContext();

    SourceContext getSourceContext();

    int getSyntaxValue();

    Syntax getSyntax();
}