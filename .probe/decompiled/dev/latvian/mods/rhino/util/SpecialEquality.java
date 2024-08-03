package dev.latvian.mods.rhino.util;

public interface SpecialEquality {

    static boolean checkSpecialEquality(Object o, Object o1, boolean shallow) {
        if (o == o1) {
            return true;
        } else if (o instanceof SpecialEquality s) {
            return s.specialEquals(o1, shallow);
        } else if (o == null || o1 == null || !o.getClass().isEnum()) {
            return false;
        } else {
            return o1 instanceof Number ? ((Enum) o).ordinal() == ((Number) o1).intValue() : EnumTypeWrapper.getName(o.getClass(), (Enum<?>) o, true).equalsIgnoreCase(String.valueOf(o1));
        }
    }

    default boolean specialEquals(Object o, boolean shallow) {
        return this.equals(o);
    }
}