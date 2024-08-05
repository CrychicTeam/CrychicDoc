package dev.latvian.mods.rhino.classfile;

final class ConstantEntry {

    private final int type;

    private final int intval;

    private final String str1;

    private final String str2;

    private final int hashcode;

    private long longval;

    ConstantEntry(int type, int intval, String str1, String str2) {
        this.type = type;
        this.intval = intval;
        this.str1 = str1;
        this.str2 = str2;
        this.hashcode = type ^ intval + str1.hashCode() * str2.hashCode();
    }

    public int hashCode() {
        return this.hashcode;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ConstantEntry entry) {
            if (this.type != entry.type) {
                return false;
            } else {
                return switch(this.type) {
                    case 3, 4 ->
                        this.intval == entry.intval;
                    case 5, 6 ->
                        this.longval == entry.longval;
                    default ->
                        throw new RuntimeException("unsupported constant type");
                    case 12 ->
                        this.str1.equals(entry.str1) && this.str2.equals(entry.str2);
                    case 18 ->
                        this.intval == entry.intval && this.str1.equals(entry.str1) && this.str2.equals(entry.str2);
                };
            }
        } else {
            return false;
        }
    }
}