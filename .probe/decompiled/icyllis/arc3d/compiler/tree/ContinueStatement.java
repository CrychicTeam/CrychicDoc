package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public final class ContinueStatement extends Statement {

    private ContinueStatement(int position) {
        super(position);
    }

    public static Statement make(int pos) {
        return new ContinueStatement(pos);
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.CONTINUE;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitContinue(this);
    }

    @Nonnull
    @Override
    public String toString() {
        return "continue;";
    }
}