package icyllis.arc3d.compiler.tree;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;

public abstract class TreeVisitor {

    public boolean visitFunctionPrototype(FunctionPrototype prototype) {
        return this.visitTopLevelElement(prototype);
    }

    public boolean visitFunctionDefinition(FunctionDefinition definition) {
        return this.visitTopLevelElement(definition);
    }

    public boolean visitGlobalVariableDecl(GlobalVariableDecl variableDecl) {
        return this.visitTopLevelElement(variableDecl);
    }

    public boolean visitInterfaceBlock(InterfaceBlock interfaceBlock) {
        return this.visitTopLevelElement(interfaceBlock);
    }

    @OverrideOnly
    protected boolean visitTopLevelElement(TopLevelElement e) {
        return false;
    }

    public boolean visitFunctionReference(FunctionReference expr) {
        return this.visitExpression(expr);
    }

    public boolean visitVariableReference(VariableReference expr) {
        return this.visitExpression(expr);
    }

    public boolean visitTypeReference(TypeReference expr) {
        return this.visitExpression(expr);
    }

    public boolean visitLiteral(Literal expr) {
        return this.visitExpression(expr);
    }

    public boolean visitFieldAccess(FieldAccess expr) {
        return this.visitExpression(expr);
    }

    public boolean visitIndex(IndexExpression expr) {
        return this.visitExpression(expr);
    }

    public boolean visitPostfix(PostfixExpression expr) {
        return this.visitExpression(expr);
    }

    public boolean visitPrefix(PrefixExpression expr) {
        return this.visitExpression(expr);
    }

    public boolean visitBinary(BinaryExpression expr) {
        return this.visitExpression(expr);
    }

    public boolean visitConditional(ConditionalExpression expr) {
        return this.visitExpression(expr);
    }

    public boolean visitSwizzle(Swizzle expr) {
        return this.visitExpression(expr);
    }

    public boolean visitFunctionCall(FunctionCall expr) {
        return this.visitExpression(expr);
    }

    public boolean visitConstructorCall(ConstructorCall expr) {
        return this.visitExpression(expr);
    }

    @OverrideOnly
    protected boolean visitExpression(Expression expr) {
        return false;
    }

    public boolean visitBlock(BlockStatement stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitBreak(BreakStatement stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitContinue(ContinueStatement stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitDiscard(DiscardStatement stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitEmpty(EmptyStatement stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitExpression(ExpressionStatement stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitForLoop(ForLoop stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitIf(IfStatement stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitReturn(ReturnStatement stmt) {
        return this.visitStatement(stmt);
    }

    public boolean visitVariableDecl(VariableDecl variableDecl) {
        return this.visitStatement(variableDecl);
    }

    @OverrideOnly
    protected boolean visitStatement(Statement stmt) {
        return false;
    }
}