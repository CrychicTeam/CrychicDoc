package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public final class VariableReference extends Expression {

    public static final int kRead_ReferenceKind = 0;

    public static final int kWrite_ReferenceKind = 1;

    public static final int kReadWrite_ReferenceKind = 2;

    public static final int kPointer_ReferenceKind = 3;

    private Variable mVariable;

    private int mReferenceKind;

    private VariableReference(int position, Variable variable, int referenceKind) {
        super(position, variable.getType());
        this.mVariable = variable;
        this.mReferenceKind = referenceKind;
    }

    @Nonnull
    public static Expression make(int position, Variable variable, int referenceKind) {
        return new VariableReference(position, variable, referenceKind);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.VARIABLE_REFERENCE;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitVariableReference(this);
    }

    public Variable getVariable() {
        return this.mVariable;
    }

    public void setVariable(Variable variable) {
        this.mVariable = variable;
    }

    public int getReferenceKind() {
        return this.mReferenceKind;
    }

    public void setReferenceKind(int referenceKind) {
        this.mReferenceKind = referenceKind;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new VariableReference(position, this.mVariable, this.mReferenceKind);
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        return this.mVariable.getName();
    }
}