package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public final class FunctionPrototype extends TopLevelElement {

    private final FunctionDecl mFunctionDecl;

    private final boolean mBuiltin;

    public FunctionPrototype(int position, FunctionDecl functionDecl, boolean builtin) {
        super(position);
        this.mFunctionDecl = functionDecl;
        this.mBuiltin = builtin;
    }

    public FunctionDecl getFunctionDecl() {
        return this.mFunctionDecl;
    }

    public boolean isBuiltin() {
        return this.mBuiltin;
    }

    @Override
    public Node.ElementKind getKind() {
        return Node.ElementKind.FUNCTION_PROTOTYPE;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitFunctionPrototype(this);
    }

    @Nonnull
    @Override
    public String toString() {
        return this.mFunctionDecl.toString() + ";";
    }
}