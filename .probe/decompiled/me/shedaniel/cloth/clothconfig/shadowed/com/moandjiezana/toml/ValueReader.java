package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import java.util.concurrent.atomic.AtomicInteger;

interface ValueReader {

    boolean canRead(String var1);

    Object read(String var1, AtomicInteger var2, Context var3);
}