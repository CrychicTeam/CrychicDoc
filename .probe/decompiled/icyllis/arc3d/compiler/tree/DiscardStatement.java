package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DiscardStatement extends Statement {

    private DiscardStatement(int position) {
        super(position);
    }

    @Nullable
    public static Statement convert(@Nonnull Context context, int pos) {
        if (!context.getKind().isFragment()) {
            context.error(pos, "discard statement is only permitted in fragment shaders");
            return null;
        } else {
            return make(pos);
        }
    }

    @Nonnull
    public static Statement make(int pos) {
        return new DiscardStatement(pos);
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.DISCARD;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitDiscard(this);
    }

    @Nonnull
    @Override
    public String toString() {
        return "discard;";
    }
}