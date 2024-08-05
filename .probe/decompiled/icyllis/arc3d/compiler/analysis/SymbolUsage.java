package icyllis.arc3d.compiler.analysis;

import icyllis.arc3d.compiler.tree.Expression;
import icyllis.arc3d.compiler.tree.FunctionCall;
import icyllis.arc3d.compiler.tree.FunctionDecl;
import icyllis.arc3d.compiler.tree.FunctionDefinition;
import icyllis.arc3d.compiler.tree.InterfaceBlock;
import icyllis.arc3d.compiler.tree.Node;
import icyllis.arc3d.compiler.tree.TreeVisitor;
import icyllis.arc3d.compiler.tree.Type;
import icyllis.arc3d.compiler.tree.Variable;
import icyllis.arc3d.compiler.tree.VariableDecl;
import icyllis.arc3d.compiler.tree.VariableReference;
import java.util.IdentityHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SymbolUsage extends TreeVisitor {

    public final IdentityHashMap<Type, SymbolUsage.Count> mStructCounts = new IdentityHashMap();

    public final IdentityHashMap<FunctionDecl, SymbolUsage.Count> mFunctionCounts = new IdentityHashMap();

    public final IdentityHashMap<Variable, SymbolUsage.VariableCounts> mVariableCounts = new IdentityHashMap();

    private int mDelta;

    @Nonnull
    public SymbolUsage.Count computeStructCount(Type typeSymbol) {
        return (SymbolUsage.Count) this.mStructCounts.computeIfAbsent(typeSymbol, __ -> new SymbolUsage.Count());
    }

    @Nullable
    public SymbolUsage.Count findStructCount(Type typeSymbol) {
        return (SymbolUsage.Count) this.mStructCounts.get(typeSymbol);
    }

    public int getStructCount(Type typeSymbol) {
        SymbolUsage.Count count = this.findStructCount(typeSymbol);
        return count != null ? count.use : 0;
    }

    @Nonnull
    public SymbolUsage.Count computeFunctionCount(FunctionDecl functionSymbol) {
        return (SymbolUsage.Count) this.mFunctionCounts.computeIfAbsent(functionSymbol, __ -> new SymbolUsage.Count());
    }

    @Nullable
    public SymbolUsage.Count findFunctionCount(FunctionDecl functionSymbol) {
        return (SymbolUsage.Count) this.mFunctionCounts.get(functionSymbol);
    }

    public int getFunctionCount(FunctionDecl functionSymbol) {
        SymbolUsage.Count count = this.findFunctionCount(functionSymbol);
        return count != null ? count.use : 0;
    }

    @Nonnull
    public SymbolUsage.VariableCounts computeVariableCounts(Variable varSymbol) {
        return (SymbolUsage.VariableCounts) this.mVariableCounts.computeIfAbsent(varSymbol, __ -> new SymbolUsage.VariableCounts());
    }

    @Nullable
    public SymbolUsage.VariableCounts findVariableCounts(Variable varSymbol) {
        return (SymbolUsage.VariableCounts) this.mVariableCounts.get(varSymbol);
    }

    public void add(@Nonnull Node node) {
        this.mDelta = 1;
        node.accept(this);
    }

    public void remove(@Nonnull Node node) {
        this.mDelta = -1;
        node.accept(this);
    }

    @Override
    public boolean visitFunctionDefinition(FunctionDefinition definition) {
        for (Variable param : definition.getFunctionDecl().getParameters()) {
            SymbolUsage.VariableCounts counts = this.computeVariableCounts(param);
            counts.decl = counts.decl + this.mDelta;
            this.visitType(param.getType());
        }
        return super.visitFunctionDefinition(definition);
    }

    @Override
    public boolean visitInterfaceBlock(InterfaceBlock interfaceBlock) {
        this.computeVariableCounts(interfaceBlock.getVariable());
        this.visitType(interfaceBlock.getVariable().getType());
        return super.visitInterfaceBlock(interfaceBlock);
    }

    @Override
    public boolean visitFunctionCall(FunctionCall expr) {
        SymbolUsage.Count count = this.computeFunctionCount(expr.getFunction());
        count.use = count.use + this.mDelta;
        assert count.use >= 0;
        return super.visitFunctionCall(expr);
    }

    @Override
    public boolean visitVariableReference(VariableReference expr) {
        SymbolUsage.VariableCounts counts = this.computeVariableCounts(expr.getVariable());
        switch(expr.getReferenceKind()) {
            case 0:
                counts.read = counts.read + this.mDelta;
                break;
            case 1:
                counts.write = counts.write + this.mDelta;
                break;
            case 2:
            case 3:
                counts.read = counts.read + this.mDelta;
                counts.write = counts.write + this.mDelta;
        }
        assert counts.read >= 0 && counts.write >= 0;
        return super.visitVariableReference(expr);
    }

    @Override
    protected boolean visitExpression(Expression expr) {
        this.visitType(expr.getType());
        return super.visitExpression(expr);
    }

    @Override
    public boolean visitVariableDecl(VariableDecl variableDecl) {
        SymbolUsage.VariableCounts counts = this.computeVariableCounts(variableDecl.getVariable());
        counts.decl = counts.decl + this.mDelta;
        assert counts.decl == 0 || counts.decl == 1;
        if (variableDecl.getInit() != null) {
            counts.write = counts.write + this.mDelta;
        }
        this.visitType(variableDecl.getVariable().getType());
        return super.visitVariableDecl(variableDecl);
    }

    private void visitType(Type t) {
        if (t.isArray()) {
            this.visitType(t.getElementType());
        } else if (t.isStruct()) {
            SymbolUsage.Count count = this.computeStructCount(t);
            count.use = count.use + this.mDelta;
            assert count.use >= 0;
            for (Type.Field field : t.getFields()) {
                this.visitType(field.type());
            }
        }
    }

    public String toString() {
        return "ModuleUsage{mStructCounts=" + this.mStructCounts + ", mFunctionCounts=" + this.mFunctionCounts + ", mVariableCounts=" + this.mVariableCounts + "}";
    }

    public static class Count {

        int use;

        public String toString() {
            return Integer.toString(this.use);
        }
    }

    public static class VariableCounts {

        public int decl;

        public int read;

        public int write;

        public String toString() {
            return "Counts{decl=" + this.decl + ", read=" + this.read + ", write=" + this.write + "}";
        }
    }
}