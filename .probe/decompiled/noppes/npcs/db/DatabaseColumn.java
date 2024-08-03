package noppes.npcs.db;

public @interface DatabaseColumn {

    String name();

    String default_value() default "";

    DatabaseColumn.Type type();

    boolean isVirtual() default false;

    public static enum Type {

        INT,
        TEXT,
        VARCHAR,
        ENUM,
        UUID,
        SMALLINT,
        JSON,
        BOOLEAN
    }
}