package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;

public final class FunctionReference extends Expression {

    private final FunctionDecl mOverloadChain;

    private FunctionReference(int position, FunctionDecl overloadChain, Type type) {
        super(position, type);
        this.mOverloadChain = overloadChain;
    }

    @Nonnull
    public static Expression make(@Nonnull Context context, int position, FunctionDecl overloadChain) {
        return new FunctionReference(position, overloadChain, context.getTypes().mInvalid);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.FUNCTION_REFERENCE;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitFunctionReference(this);
    }

    public FunctionDecl getOverloadChain() {
        return this.mOverloadChain;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new FunctionReference(position, this.mOverloadChain, this.getType());
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        return "<function>";
    }
}