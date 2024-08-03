package dev.latvian.mods.rhino.util;

import dev.latvian.mods.rhino.Context;
import java.util.List;
import java.util.function.Supplier;

public interface DataObject {

    <T> T createDataObject(Supplier<T> var1, Context var2);

    <T> List<T> createDataObjectList(Supplier<T> var1, Context var2);

    boolean isDataObjectList();
}