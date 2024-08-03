package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;

public final class FunctionDefinition extends TopLevelElement {

    private final FunctionDecl mFunctionDecl;

    private final boolean mBuiltin;

    private BlockStatement mBody;

    private FunctionDefinition(int position, FunctionDecl functionDecl, boolean builtin, BlockStatement body) {
        super(position);
        this.mFunctionDecl = functionDecl;
        this.mBuiltin = builtin;
        this.mBody = body;
    }

    public static FunctionDefinition convert(@Nonnull Context context, int pos, FunctionDecl functionDecl, boolean builtin, Statement body) {
        if (functionDecl.isIntrinsic()) {
            context.error(pos, "Intrinsic function '" + functionDecl.getName() + "' should not have a definition");
            return null;
        } else {
            if (body == null || !(body instanceof BlockStatement block) || !block.isScoped()) {
                context.error(pos, "function body '" + functionDecl + "' must be a braced block");
                return null;
            }
            if (functionDecl.getDefinition() != null) {
                context.error(pos, "function '" + functionDecl + "' was already defined");
                return null;
            } else {
                return make(pos, functionDecl, builtin, block);
            }
        }
    }

    public static FunctionDefinition make(int pos, FunctionDecl functionDecl, boolean builtin, BlockStatement body) {
        return new FunctionDefinition(pos, functionDecl, builtin, body);
    }

    public FunctionDecl getFunctionDecl() {
        return this.mFunctionDecl;
    }

    public boolean isBuiltin() {
        return this.mBuiltin;
    }

    public BlockStatement getBody() {
        return this.mBody;
    }

    public void setBody(BlockStatement body) {
        this.mBody = body;
    }

    @Override
    public Node.ElementKind getKind() {
        return Node.ElementKind.FUNCTION_DEFINITION;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitFunctionDefinition(this) ? true : this.mBody.accept(visitor);
    }

    @Nonnull
    @Override
    public String toString() {
        return this.mFunctionDecl.toString() + " " + this.mBody.toString();
    }
}