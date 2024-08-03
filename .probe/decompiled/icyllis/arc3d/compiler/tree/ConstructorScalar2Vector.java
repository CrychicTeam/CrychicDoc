package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import javax.annotation.Nonnull;

public final class ConstructorScalar2Vector extends ConstructorCall {

    private ConstructorScalar2Vector(int position, Type type, Expression... arguments) {
        super(position, type, arguments);
        assert arguments.length == 1;
    }

    @Nonnull
    public static Expression make(int position, @Nonnull Type type, @Nonnull Expression arg) {
        assert type.isScalar() || type.isVector();
        assert arg.getType().matches(type.getComponentType());
        assert arg.getType().isScalar();
        if (type.isScalar()) {
            arg.mPosition = position;
            return arg;
        } else {
            arg = ConstantFolder.makeConstantValueForVariable(position, arg);
            assert type.isVector();
            return new ConstructorScalar2Vector(position, type, arg);
        }
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_SCALAR_TO_VECTOR;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorScalar2Vector(position, this.getType(), this.cloneArguments());
    }
}