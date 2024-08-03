package dev.latvian.mods.kubejs.typings.desc;

public record GenericDescJS(TypeDescJS type, TypeDescJS... types) implements TypeDescJS {

    @Override
    public void build(StringBuilder builder) {
        this.type.build(builder);
        builder.append('<');
        for (int i = 0; i < this.types.length; i++) {
            if (i > 0) {
                builder.append(',');
                builder.append(' ');
            }
            this.types[i].build(builder);
        }
        builder.append('>');
    }

    public String toString() {
        return this.build();
    }
}