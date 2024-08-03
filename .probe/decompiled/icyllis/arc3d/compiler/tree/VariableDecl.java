package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class VariableDecl extends Statement {

    private Variable mVariable;

    private Expression mInit;

    public VariableDecl(Variable variable, Expression init) {
        super(variable.mPosition);
        this.mVariable = variable;
        this.mInit = init;
    }

    public static void checkError(int pos, Modifiers modifiers, Type type, Type baseType, byte storage) {
        assert type.isArray() ? baseType.matches(type.getElementType()) : baseType.matches(type);
    }

    @Nullable
    public static VariableDecl convert(@Nonnull Context context, int pos, @Nonnull Modifiers modifiers, @Nonnull Type type, @Nonnull String name, byte storage, @Nullable Expression init) {
        assert storage != 2;
        if (init != null && type.isUnsizedArray() && init.getType().isArray()) {
            int arraySize = init.getType().getArraySize();
            if (arraySize > 0) {
                type = context.getSymbolTable().getArrayType(type.getElementType(), arraySize);
            }
        }
        Variable variable = Variable.convert(context, pos, modifiers, type, name, storage);
        return convert(context, variable, init);
    }

    @Nullable
    public static VariableDecl convert(@Nonnull Context context, @Nonnull Variable variable, @Nullable Expression init) {
        Type baseType = variable.getType();
        if (baseType.isArray()) {
            baseType = baseType.getElementType();
        }
        if (baseType.matches(context.getTypes().mInvalid)) {
            context.error(variable.mPosition, "invalid type");
            return null;
        } else if (baseType.isVoid()) {
            context.error(variable.mPosition, "variables of type 'void' are not allowed");
            return null;
        } else {
            checkError(variable.mPosition, variable.getModifiers(), variable.getType(), baseType, variable.getStorage());
            if (init != null) {
                if ((variable.getModifiers().flags() & 32) != 0) {
                    context.error(init.mPosition, "'in' variables cannot use initializer expressions");
                    return null;
                }
                if ((variable.getModifiers().flags() & 16) != 0) {
                    context.error(init.mPosition, "'uniform' variables cannot use initializer expressions");
                    return null;
                }
                init = variable.getType().coerceExpression(context, init);
                if (init == null) {
                    return null;
                }
            }
            if ((variable.getModifiers().flags() & 8) != 0 && init == null) {
                context.error(variable.mPosition, "'const' variables must be initialized");
                return null;
            } else {
                VariableDecl variableDecl = make(variable, init);
                context.getSymbolTable().insert(context, variable);
                return variableDecl;
            }
        }
    }

    @Nonnull
    public static VariableDecl make(Variable variable, Expression init) {
        VariableDecl result = new VariableDecl(variable, init);
        variable.setVariableDecl(result);
        return result;
    }

    public Variable getVariable() {
        return this.mVariable;
    }

    public void setVariable(Variable variable) {
        this.mVariable = variable;
    }

    public Expression getInit() {
        return this.mInit;
    }

    public void setInit(Expression init) {
        this.mInit = init;
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.VARIABLE_DECL;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitVariableDecl(this) ? true : this.mInit != null && this.mInit.accept(visitor);
    }

    @Nonnull
    @Override
    public String toString() {
        String result = this.mVariable.toString();
        if (this.mInit != null) {
            result = result + " = " + this.mInit;
        }
        return result + ";";
    }
}