package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;

public final class GlobalVariableDecl extends TopLevelElement {

    private final VariableDecl mVariableDecl;

    public GlobalVariableDecl(@Nonnull VariableDecl decl) {
        super(decl.mPosition);
        this.mVariableDecl = decl;
    }

    public VariableDecl getVariableDecl() {
        return this.mVariableDecl;
    }

    @Override
    public Node.ElementKind getKind() {
        return Node.ElementKind.GLOBAL_VARIABLE;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitGlobalVariableDecl(this) ? true : this.mVariableDecl.accept(visitor);
    }

    @Nonnull
    @Override
    public String toString() {
        return this.mVariableDecl.toString();
    }
}