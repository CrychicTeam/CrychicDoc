package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;

public final class TypeReference extends Expression {

    private final Type mValue;

    private TypeReference(int position, Type value, Type type) {
        super(position, type);
        this.mValue = value;
    }

    @Nonnull
    public static TypeReference make(@Nonnull Context context, int position, @Nonnull Type value) {
        return new TypeReference(position, value, context.getTypes().mInvalid);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.TYPE_REFERENCE;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitTypeReference(this);
    }

    public Type getValue() {
        return this.mValue;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new TypeReference(position, this.mValue, this.getType());
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        return this.mValue.getName();
    }
}