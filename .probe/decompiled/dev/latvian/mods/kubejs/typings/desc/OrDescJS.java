package dev.latvian.mods.kubejs.typings.desc;

public record OrDescJS(TypeDescJS[] types) implements TypeDescJS {

    @Override
    public void build(StringBuilder builder) {
        for (int i = 0; i < this.types.length; i++) {
            if (i > 0) {
                builder.append(" | ");
            }
            this.types[i].build(builder);
        }
    }

    @Override
    public TypeDescJS or(TypeDescJS type) {
        if (type instanceof OrDescJS t) {
            TypeDescJS[] types1 = new TypeDescJS[this.types.length + t.types.length];
            System.arraycopy(this.types, 0, types1, 0, this.types.length);
            System.arraycopy(t.types, 0, types1, this.types.length, t.types.length);
            return new OrDescJS(types1);
        } else {
            TypeDescJS[] types1 = new TypeDescJS[this.types.length + 1];
            System.arraycopy(this.types, 0, types1, 0, this.types.length);
            types1[this.types.length] = type;
            return new OrDescJS(types1);
        }
    }

    public String toString() {
        return this.build();
    }
}