package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

interface ValueWriter {

    boolean canWrite(Object var1);

    void write(Object var1, WriterContext var2);

    boolean isPrimitiveType();
}