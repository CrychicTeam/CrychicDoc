package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public final class EmptyStatement extends Statement {

    public EmptyStatement(int position) {
        super(position);
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.EMPTY;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitEmpty(this);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Nonnull
    @Override
    public String toString() {
        return ";";
    }
}