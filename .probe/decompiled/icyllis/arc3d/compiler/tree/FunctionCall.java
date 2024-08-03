package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.analysis.Analysis;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FunctionCall extends Expression {

    private final FunctionDecl mFunction;

    private final Expression[] mArguments;

    private FunctionCall(int position, Type type, FunctionDecl function, Expression... arguments) {
        super(position, type);
        this.mFunction = function;
        this.mArguments = arguments;
    }

    private static String buildArgumentTypeList(List<Expression> arguments) {
        StringJoiner joiner = new StringJoiner(", ", "(", ")");
        for (Expression arg : arguments) {
            joiner.add(arg.getType().toString());
        }
        return joiner.toString();
    }

    @Nullable
    private static FunctionDecl findBestCandidate(@Nonnull FunctionDecl chain, @Nonnull List<Expression> arguments) {
        if (chain.getNextOverload() == null) {
            return chain;
        } else {
            long bestCost = Type.CoercionCost.saturate();
            FunctionDecl best = null;
            for (FunctionDecl f = chain; f != null; f = f.getNextOverload()) {
                long cost;
                if (f.getParameters().size() != arguments.size()) {
                    cost = Type.CoercionCost.saturate();
                } else {
                    List<Type> paramTypes = new ArrayList();
                    if (f.resolveParameterTypes(arguments, paramTypes) == null) {
                        cost = Type.CoercionCost.saturate();
                    } else {
                        long total = Type.CoercionCost.free();
                        for (int i = 0; i < arguments.size(); i++) {
                            total = Type.CoercionCost.plus(total, ((Expression) arguments.get(i)).getCoercionCost((Type) paramTypes.get(i)));
                        }
                        cost = total;
                    }
                }
                if (Type.CoercionCost.compare(cost, bestCost) <= 0) {
                    bestCost = cost;
                    best = f;
                }
            }
            return Type.CoercionCost.accept(bestCost, false) ? best : null;
        }
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int pos, @Nonnull Expression identifier, @Nonnull List<Expression> arguments) {
        return switch(identifier.getKind()) {
            case TYPE_REFERENCE ->
                {
                    TypeReference ref = (TypeReference) identifier;
                    ???;
                }
            case FUNCTION_REFERENCE ->
                {
                    FunctionReference ref = (FunctionReference) identifier;
                    FunctionDecl best = findBestCandidate(ref.getOverloadChain(), arguments);
                    if (best != null) {
                        ???;
                    } else {
                    }
                }
            case POISON ->
                {
                    identifier.mPosition = pos;
                    yield identifier;
                }
            default ->
                {
                }
        };
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int pos, @Nonnull FunctionDecl function, @Nonnull List<Expression> arguments) {
        if (function.getParameters().size() != arguments.size()) {
            String msg = "call to '" + function.getName() + "' expected " + function.getParameters().size() + " argument";
            if (function.getParameters().size() != 1) {
                msg = msg + "s";
            }
            msg = msg + ", but found " + arguments.size();
            context.error(pos, msg);
            return null;
        } else {
            List<Type> paramTypes = new ArrayList();
            Type returnType = function.resolveParameterTypes(arguments, paramTypes);
            if (returnType == null) {
                String msg = "no match for " + function.getName() + buildArgumentTypeList(arguments);
                context.error(pos, msg);
                return null;
            } else {
                for (int i = 0; i < arguments.size(); i++) {
                    arguments.set(i, ((Type) paramTypes.get(i)).coerceExpression(context, (Expression) arguments.get(i)));
                    if (arguments.get(i) == null) {
                        return null;
                    }
                    Modifiers paramFlags = ((Variable) function.getParameters().get(i)).getModifiers();
                    if ((paramFlags.flags() & 64) != 0) {
                        int refKind = (paramFlags.flags() & 32) != 0 ? 2 : 3;
                        if (!Analysis.updateVariableRefKind((Expression) arguments.get(i), refKind)) {
                            return null;
                        }
                    }
                }
                if (function.isEntryPoint()) {
                    context.error(pos, "call to entry point is not allowed");
                    return null;
                } else {
                    return make(pos, returnType, function, arguments);
                }
            }
        }
    }

    public static Expression make(int pos, Type returnType, FunctionDecl function, List<Expression> arguments) {
        assert function.getParameters().size() == arguments.size();
        return new FunctionCall(pos, returnType, function, (Expression[]) arguments.toArray(new Expression[0]));
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.FUNCTION_CALL;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        if (visitor.visitFunctionCall(this)) {
            return true;
        } else {
            for (Expression arg : this.mArguments) {
                if (arg.accept(visitor)) {
                    return true;
                }
            }
            return false;
        }
    }

    public FunctionDecl getFunction() {
        return this.mFunction;
    }

    public Expression[] getArguments() {
        return this.mArguments;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        Expression[] arguments = (Expression[]) this.mArguments.clone();
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].clone();
        }
        return new FunctionCall(position, this.getType(), this.mFunction, arguments);
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Expression arg : this.mArguments) {
            joiner.add(arg.toString(17));
        }
        return this.mFunction.getName() + "(" + joiner + ")";
    }
}