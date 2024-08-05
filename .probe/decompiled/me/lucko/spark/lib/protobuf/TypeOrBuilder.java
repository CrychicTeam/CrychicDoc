package me.lucko.spark.lib.protobuf;

import java.util.List;

public interface TypeOrBuilder extends MessageLiteOrBuilder {

    String getName();

    ByteString getNameBytes();

    List<Field> getFieldsList();

    Field getFields(int var1);

    int getFieldsCount();

    List<String> getOneofsList();

    int getOneofsCount();

    String getOneofs(int var1);

    ByteString getOneofsBytes(int var1);

    List<Option> getOptionsList();

    Option getOptions(int var1);

    int getOptionsCount();

    boolean hasSourceContext();

    SourceContext getSourceContext();

    int getSyntaxValue();

    Syntax getSyntax();
}