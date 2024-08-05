package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.analysis.Analysis;
import javax.annotation.Nonnull;

public final class ConstructorArrayCast extends ConstructorCall {

    private ConstructorArrayCast(int position, Type type, Expression... arguments) {
        super(position, type, arguments);
        assert arguments.length == 1;
    }

    @Nonnull
    public static Expression make(@Nonnull Context context, int position, @Nonnull Type type, @Nonnull Expression arg) {
        assert type.isArray();
        assert arg.getType().isArray();
        assert type.getArraySize() == arg.getType().getArraySize();
        if (type.matches(arg.getType())) {
            arg.mPosition = position;
            return arg;
        } else {
            arg = ConstantFolder.makeConstantValueForVariable(position, arg);
            if (Analysis.isCompileTimeConstant(arg)) {
                Type scalarType = type.getComponentType();
                Expression[] inputArgs = ((ConstructorArray) arg).getArguments();
                Expression[] typecastArgs = new Expression[inputArgs.length];
                for (int i = 0; i < inputArgs.length; i++) {
                    Expression inputArg = inputArgs[i];
                    if (inputArg.getType().isScalar()) {
                        typecastArgs[i] = ConstructorScalarCast.make(context, inputArg.mPosition, scalarType, inputArg);
                    } else {
                        typecastArgs[i] = ConstructorCompoundCast.make(inputArg.mPosition, scalarType, inputArg);
                    }
                }
                return ConstructorArray.make(position, type, typecastArgs);
            } else {
                return new ConstructorArrayCast(position, type, arg);
            }
        }
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_ARRAY_CAST;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorArrayCast(position, this.getType(), this.cloneArguments());
    }
}