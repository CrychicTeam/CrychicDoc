package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ConstructorArray extends ConstructorCall {

    private ConstructorArray(int position, Type type, Expression[] arguments) {
        super(position, type, arguments);
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int position, @Nonnull Type type, @Nonnull List<Expression> arguments) {
        assert type.isArray();
        if (arguments.size() == 1) {
            Expression arg = (Expression) arguments.get(0);
            Type argType = arg.getType();
            if (argType.isArray() && argType.canCoerceTo(type, false)) {
                return ConstructorArrayCast.make(context, position, type, arg);
            }
        }
        if (type.isUnsizedArray()) {
            if (arguments.isEmpty()) {
                context.error(position, "implicitly sized array constructor must have at least one argument");
                return null;
            }
            type = context.getSymbolTable().getArrayType(type.getElementType(), arguments.size());
        } else if (type.getArraySize() != arguments.size()) {
            context.error(position, String.format("invalid arguments to '%s' constructor (expected %d elements, but found %d)", type.getName(), type.getArraySize(), arguments.size()));
            return null;
        }
        Type baseType = type.getElementType();
        Expression[] immutableArgs = new Expression[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            immutableArgs[i] = baseType.coerceExpression(context, (Expression) arguments.get(i));
            if (immutableArgs[i] == null) {
                return null;
            }
        }
        return make(position, type, immutableArgs);
    }

    @Nonnull
    public static Expression make(int position, @Nonnull Type type, @Nonnull Expression[] arguments) {
        assert type.getArraySize() == arguments.length;
        for (Expression arg : arguments) {
            assert type.getElementType().matches(arg.getType());
        }
        return new ConstructorArray(position, type, arguments);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_ARRAY;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorArray(position, this.getType(), this.cloneArguments());
    }
}