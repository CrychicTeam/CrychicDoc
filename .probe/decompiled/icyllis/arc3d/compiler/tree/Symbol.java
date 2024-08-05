package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public abstract class Symbol extends Node {

    private String mName;

    protected Symbol(int position, String name) {
        super(position);
        this.mName = name;
    }

    @Override
    public final boolean accept(@Nonnull TreeVisitor visitor) {
        throw new AssertionError();
    }

    @Nonnull
    public abstract Node.SymbolKind getKind();

    @Nonnull
    public final String getName() {
        return this.mName;
    }

    public final void setName(@Nonnull String name) {
        this.mName = name;
    }

    @Nonnull
    public abstract Type getType();
}