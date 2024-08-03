package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public final class ConstructorStruct extends ConstructorCall {

    private ConstructorStruct(int position, Type type, Expression[] arguments) {
        super(position, type, arguments);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_STRUCT;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorStruct(position, this.getType(), this.cloneArguments());
    }
}