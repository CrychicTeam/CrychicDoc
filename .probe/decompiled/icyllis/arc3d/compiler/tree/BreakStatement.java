package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public final class BreakStatement extends Statement {

    private BreakStatement(int position) {
        super(position);
    }

    public static Statement make(int pos) {
        return new BreakStatement(pos);
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.BREAK;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitBreak(this);
    }

    @Nonnull
    @Override
    public String toString() {
        return "break;";
    }
}