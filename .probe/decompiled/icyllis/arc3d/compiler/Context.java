package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.tree.AnonymousField;
import icyllis.arc3d.compiler.tree.Expression;
import icyllis.arc3d.compiler.tree.FieldAccess;
import icyllis.arc3d.compiler.tree.FunctionDecl;
import icyllis.arc3d.compiler.tree.FunctionReference;
import icyllis.arc3d.compiler.tree.Symbol;
import icyllis.arc3d.compiler.tree.Type;
import icyllis.arc3d.compiler.tree.TypeReference;
import icyllis.arc3d.compiler.tree.Variable;
import icyllis.arc3d.compiler.tree.VariableReference;
import java.util.Objects;
import javax.annotation.Nullable;

public final class Context {

    private ShaderKind mKind;

    private CompileOptions mOptions;

    private boolean mIsBuiltin;

    private boolean mIsModule;

    private final BuiltinTypes mTypes = ModuleLoader.getInstance().getBuiltinTypes();

    private SymbolTable mSymbolTable;

    ErrorHandler mErrorHandler;

    private boolean mActive;

    Context(ErrorHandler errorHandler) {
        this.mErrorHandler = errorHandler;
    }

    void start(ShaderKind kind, CompileOptions options, ModuleUnit parent, boolean isBuiltin, boolean isModule) {
        if (this.isActive()) {
            throw new IllegalStateException("DSL is already started");
        } else {
            this.mKind = (ShaderKind) Objects.requireNonNull(kind);
            this.mOptions = (CompileOptions) Objects.requireNonNull(options);
            this.mIsBuiltin = isBuiltin;
            this.mIsModule = isModule;
            if (parent != null) {
                this.mSymbolTable = parent.mSymbols.enterModule(isBuiltin);
            }
            this.mActive = true;
        }
    }

    void end() {
        this.mKind = null;
        this.mOptions = null;
        this.mSymbolTable = null;
        this.mActive = false;
    }

    public boolean isActive() {
        return this.mActive;
    }

    public ShaderKind getKind() {
        return this.mKind;
    }

    public CompileOptions getOptions() {
        return this.mOptions;
    }

    public boolean isBuiltin() {
        return this.mIsBuiltin;
    }

    public boolean isModule() {
        return this.mIsModule;
    }

    public BuiltinTypes getTypes() {
        return this.mTypes;
    }

    public SymbolTable getSymbolTable() {
        return this.mSymbolTable;
    }

    public void enterScope() {
        this.mSymbolTable = this.mSymbolTable.enterScope();
    }

    public void leaveScope() {
        this.mSymbolTable = this.mSymbolTable.leaveScope();
    }

    public void error(int position, String msg) {
        this.mErrorHandler.error(position, msg);
    }

    public void error(int start, int end, String msg) {
        this.mErrorHandler.error(start, end, msg);
    }

    public void warning(int position, String msg) {
        this.mErrorHandler.warning(position, msg);
    }

    public void warning(int start, int end, String msg) {
        this.mErrorHandler.warning(start, end, msg);
    }

    public ErrorHandler getErrorHandler() {
        return this.mErrorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.mErrorHandler = (ErrorHandler) Objects.requireNonNull(errorHandler);
    }

    @Nullable
    public Expression convertIdentifier(int position, String name) {
        Symbol result = this.mSymbolTable.find(name);
        if (result == null) {
            this.error(position, "identifier '" + name + "' is undefined");
            return null;
        } else {
            ???;
        }
    }
}