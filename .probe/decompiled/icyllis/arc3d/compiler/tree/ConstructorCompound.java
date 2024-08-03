package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ConstructorCompound extends ConstructorCall {

    private ConstructorCompound(int position, Type type, Expression[] arguments) {
        super(position, type, arguments);
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int pos, @Nonnull Type type, @Nonnull List<Expression> args) {
        assert type.isVector() || type.isMatrix();
        if (args.size() == 1) {
            Expression argument = (Expression) args.get(0);
            if (type.isVector() && argument.getType().isVector() && argument.getType().getComponentType().matches(type.getComponentType()) && argument.getType().getComponents() > type.getComponents()) {
                String swizzleHint = switch(type.getComponents()) {
                    case 2 ->
                        "; use '.xy' instead";
                    case 3 ->
                        "; use '.xyz' instead";
                    default ->
                        "";
                };
                context.error(pos, "'" + argument.getType() + "' is not a valid parameter to '" + type + "' constructor" + swizzleHint);
                return null;
            }
            if (argument.getType().isScalar()) {
                Expression typecast = ConstructorScalarCast.convert(context, pos, type.getComponentType(), args);
                if (typecast == null) {
                    return null;
                }
                return type.isMatrix() ? ConstructorScalar2Matrix.make(pos, type, typecast) : ConstructorScalar2Vector.make(pos, type, typecast);
            }
            if (argument.getType().isVector()) {
                if (type.isVector() && argument.getType().getRows() == type.getRows()) {
                    return ConstructorCompoundCast.make(pos, type, argument);
                }
            } else if (argument.getType().isMatrix()) {
                if (type.isMatrix()) {
                    Type typecastType = type.getComponentType().toCompound(context, argument.getType().getCols(), argument.getType().getRows());
                    argument = ConstructorCompoundCast.make(pos, typecastType, argument);
                    return ConstructorMatrix2Matrix.make(pos, type, argument);
                }
                if (type.isVector() && type.getRows() == 4 && argument.getType().getComponents() == 4) {
                    Type vectorType = argument.getType().getComponentType().toVector(context, 4);
                    Expression vecCtor = make(context, pos, vectorType, (Expression[]) args.toArray(new Expression[0]));
                    return ConstructorCompoundCast.make(pos, type, vecCtor);
                }
            }
        }
        int expected = type.getRows() * type.getCols();
        int actual = 0;
        ListIterator<Expression> it = args.listIterator();
        while (it.hasNext()) {
            Expression arg = (Expression) it.next();
            if (!arg.getType().isScalar() && !arg.getType().isVector()) {
                context.error(pos, "'" + arg.getType() + "' is not a valid parameter to '" + type + "' constructor");
                return null;
            }
            Type ctorType = type.getComponentType().toVector(context, arg.getType().getRows());
            List<Expression> ctorArg = new ArrayList(1);
            ctorArg.add(arg);
            arg = ConstructorCall.convert(context, pos, ctorType, ctorArg);
            if (arg == null) {
                return null;
            }
            it.set(arg);
            actual += ctorType.getRows();
        }
        if (actual != expected) {
            context.error(pos, "invalid arguments to '" + type + "' constructor (expected " + expected + " scalars, but found " + actual + ")");
            return null;
        } else {
            return make(context, pos, type, (Expression[]) args.toArray(new Expression[0]));
        }
    }

    @Nonnull
    public static Expression make(@Nonnull Context context, int position, Type type, Expression[] arguments) {
        int n = 0;
        for (Expression arg : arguments) {
            Type argType = arg.getType();
            assert (argType.isScalar() || argType.isVector() || argType.isMatrix()) && argType.getComponentType().matches(type.getComponentType());
            n += argType.getComponents();
        }
        assert type.getComponents() == n;
        if (arguments.length == 1) {
            Expression arg = arguments[0];
            if (type.isScalar()) {
                assert arg.getType().matches(type);
                arg.mPosition = position;
                return arg;
            }
            if (type.isVector() && arg.getType().matches(type)) {
                arg.mPosition = position;
                return arg;
            }
        }
        assert type.isVector() || type.isMatrix();
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = ConstantFolder.makeConstantValueForVariable(position, arguments[i]);
        }
        return new ConstructorCompound(position, type, arguments);
    }

    @Nonnull
    public static Expression makeFromConstants(@Nonnull Context context, int pos, Type type, double[] values) {
        int components = type.getComponents();
        Expression[] args = new Expression[components];
        for (int index = 0; index < components; index++) {
            args[index] = Literal.make(pos, values[index], type.getComponentType());
        }
        return make(context, pos, type, args);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_COMPOUND;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorCompound(position, this.getType(), this.cloneArguments());
    }
}