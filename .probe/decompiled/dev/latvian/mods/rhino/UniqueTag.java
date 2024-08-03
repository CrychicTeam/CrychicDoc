package dev.latvian.mods.rhino;

public final class UniqueTag {

    private static final int ID_NOT_FOUND = 1;

    public static final UniqueTag NOT_FOUND = new UniqueTag(1);

    private static final int ID_NULL_VALUE = 2;

    public static final UniqueTag NULL_VALUE = new UniqueTag(2);

    private static final int ID_DOUBLE_MARK = 3;

    public static final UniqueTag DOUBLE_MARK = new UniqueTag(3);

    private final int tagId;

    private UniqueTag(int tagId) {
        this.tagId = tagId;
    }

    public String toString() {
        String name = switch(this.tagId) {
            case 1 ->
                "NOT_FOUND";
            case 2 ->
                "NULL_VALUE";
            case 3 ->
                "DOUBLE_MARK";
            default ->
                throw Kit.codeBug();
        };
        return super.toString() + ": " + name;
    }
}