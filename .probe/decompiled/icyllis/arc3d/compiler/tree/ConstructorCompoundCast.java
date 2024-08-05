package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import javax.annotation.Nonnull;

public final class ConstructorCompoundCast extends ConstructorCall {

    private ConstructorCompoundCast(int position, Type type, Expression... arguments) {
        super(position, type, arguments);
        assert arguments.length == 1;
    }

    @Nonnull
    public static Expression make(int position, @Nonnull Type type, @Nonnull Expression arg) {
        assert type.isVector() || type.isMatrix();
        assert arg.getType().isVector() == type.isVector();
        assert arg.getType().isMatrix() == type.isMatrix();
        assert type.getCols() == arg.getType().getCols();
        assert type.getRows() == arg.getType().getRows();
        if (type.matches(arg.getType())) {
            return arg;
        } else {
            arg = ConstantFolder.makeConstantValueForVariable(position, arg);
            return new ConstructorCompoundCast(position, type, arg);
        }
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_COMPOUND_CAST;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorCompoundCast(position, this.getType(), this.cloneArguments());
    }
}