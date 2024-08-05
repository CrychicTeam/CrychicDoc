package me.lucko.spark.lib.protobuf;

import java.util.List;

public interface ApiOrBuilder extends MessageLiteOrBuilder {

    String getName();

    ByteString getNameBytes();

    List<Method> getMethodsList();

    Method getMethods(int var1);

    int getMethodsCount();

    List<Option> getOptionsList();

    Option getOptions(int var1);

    int getOptionsCount();

    String getVersion();

    ByteString getVersionBytes();

    boolean hasSourceContext();

    SourceContext getSourceContext();

    List<Mixin> getMixinsList();

    Mixin getMixins(int var1);

    int getMixinsCount();

    int getSyntaxValue();

    Syntax getSyntax();
}