package dev.latvian.mods.rhino;

public interface Wrapper {

    static Object unwrapped(Object o) {
        return o instanceof Wrapper ? unwrapped(((Wrapper) o).unwrap()) : o;
    }

    Object unwrap();
}