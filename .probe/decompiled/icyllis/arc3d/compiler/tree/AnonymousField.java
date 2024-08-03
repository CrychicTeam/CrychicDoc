package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public final class AnonymousField extends Symbol {

    private final Variable mContainer;

    private final int mFieldIndex;

    public AnonymousField(int position, Variable container, int fieldIndex) {
        super(position, container.getType().getFields()[fieldIndex].name());
        this.mContainer = container;
        this.mFieldIndex = fieldIndex;
    }

    @Nonnull
    @Override
    public Node.SymbolKind getKind() {
        return Node.SymbolKind.ANONYMOUS_FIELD;
    }

    @Nonnull
    @Override
    public Type getType() {
        return this.mContainer.getType().getFields()[this.mFieldIndex].type();
    }

    public int getFieldIndex() {
        return this.mFieldIndex;
    }

    public Variable getContainer() {
        return this.mContainer;
    }

    @Nonnull
    @Override
    public String toString() {
        return this.mContainer.toString() + "." + this.getName();
    }
}