package dev.latvian.mods.kubejs.typings.desc;

public record ArrayDescJS(TypeDescJS type) implements TypeDescJS {

    @Override
    public void build(StringBuilder builder) {
        this.type.build(builder);
        builder.append('[');
        builder.append(']');
    }

    public String toString() {
        return this.build();
    }
}