package dev.latvian.mods.rhino;

import java.util.function.Consumer;

public interface IdEnumerationIterator {

    boolean enumerationIteratorHasNext(Context var1, Consumer<Object> var2);

    boolean enumerationIteratorNext(Context var1, Consumer<Object> var2) throws JavaScriptException;
}