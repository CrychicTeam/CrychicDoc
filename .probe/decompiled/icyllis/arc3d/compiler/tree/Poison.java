package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;

public final class Poison extends Expression {

    private Poison(int position, Type type) {
        super(position, type);
    }

    @Nonnull
    public static Expression make(@Nonnull Context context, int position) {
        return new Poison(position, context.getTypes().mPoison);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.POISON;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return false;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new Poison(position, this.getType());
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        return "<POISON>";
    }
}