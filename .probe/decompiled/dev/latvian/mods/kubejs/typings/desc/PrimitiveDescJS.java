package dev.latvian.mods.kubejs.typings.desc;

public record PrimitiveDescJS(String type) implements TypeDescJS {

    @Override
    public void build(StringBuilder builder) {
        builder.append(this.type);
    }

    @Override
    public String build() {
        return this.type;
    }

    public String toString() {
        return this.type;
    }
}