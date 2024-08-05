package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.ast.ArrayComprehension;
import dev.latvian.mods.rhino.ast.ArrayComprehensionLoop;
import dev.latvian.mods.rhino.ast.ArrayLiteral;
import dev.latvian.mods.rhino.ast.Assignment;
import dev.latvian.mods.rhino.ast.AstNode;
import dev.latvian.mods.rhino.ast.AstRoot;
import dev.latvian.mods.rhino.ast.AstSymbol;
import dev.latvian.mods.rhino.ast.Block;
import dev.latvian.mods.rhino.ast.CatchClause;
import dev.latvian.mods.rhino.ast.ConditionalExpression;
import dev.latvian.mods.rhino.ast.ContinueStatement;
import dev.latvian.mods.rhino.ast.DestructuringForm;
import dev.latvian.mods.rhino.ast.DoLoop;
import dev.latvian.mods.rhino.ast.ElementGet;
import dev.latvian.mods.rhino.ast.EmptyExpression;
import dev.latvian.mods.rhino.ast.ExpressionStatement;
import dev.latvian.mods.rhino.ast.ForInLoop;
import dev.latvian.mods.rhino.ast.ForLoop;
import dev.latvian.mods.rhino.ast.FunctionCall;
import dev.latvian.mods.rhino.ast.FunctionNode;
import dev.latvian.mods.rhino.ast.GeneratorExpression;
import dev.latvian.mods.rhino.ast.GeneratorExpressionLoop;
import dev.latvian.mods.rhino.ast.IfStatement;
import dev.latvian.mods.rhino.ast.InfixExpression;
import dev.latvian.mods.rhino.ast.Jump;
import dev.latvian.mods.rhino.ast.Label;
import dev.latvian.mods.rhino.ast.LabeledStatement;
import dev.latvian.mods.rhino.ast.LetNode;
import dev.latvian.mods.rhino.ast.Name;
import dev.latvian.mods.rhino.ast.NewExpression;
import dev.latvian.mods.rhino.ast.NumberLiteral;
import dev.latvian.mods.rhino.ast.ObjectLiteral;
import dev.latvian.mods.rhino.ast.ObjectProperty;
import dev.latvian.mods.rhino.ast.ParenthesizedExpression;
import dev.latvian.mods.rhino.ast.PropertyGet;
import dev.latvian.mods.rhino.ast.RegExpLiteral;
import dev.latvian.mods.rhino.ast.ReturnStatement;
import dev.latvian.mods.rhino.ast.Scope;
import dev.latvian.mods.rhino.ast.ScriptNode;
import dev.latvian.mods.rhino.ast.StringLiteral;
import dev.latvian.mods.rhino.ast.SwitchCase;
import dev.latvian.mods.rhino.ast.SwitchStatement;
import dev.latvian.mods.rhino.ast.TaggedTemplateLiteral;
import dev.latvian.mods.rhino.ast.TemplateCharacters;
import dev.latvian.mods.rhino.ast.TemplateLiteral;
import dev.latvian.mods.rhino.ast.ThrowStatement;
import dev.latvian.mods.rhino.ast.TryStatement;
import dev.latvian.mods.rhino.ast.UnaryExpression;
import dev.latvian.mods.rhino.ast.VariableDeclaration;
import dev.latvian.mods.rhino.ast.VariableInitializer;
import dev.latvian.mods.rhino.ast.WhileLoop;
import dev.latvian.mods.rhino.ast.WithStatement;
import dev.latvian.mods.rhino.ast.Yield;
import java.util.ArrayList;
import java.util.List;

public final class IRFactory extends Parser {

    private static final int LOOP_DO_WHILE = 0;

    private static final int LOOP_WHILE = 1;

    private static final int LOOP_FOR = 2;

    private static final int ALWAYS_TRUE_BOOLEAN = 1;

    private static final int ALWAYS_FALSE_BOOLEAN = -1;

    private static void addSwitchCase(Node switchBlock, Node caseExpression, Node statements) {
        if (switchBlock.getType() != 130) {
            throw Kit.codeBug();
        } else {
            Jump switchNode = (Jump) switchBlock.getFirstChild();
            if (switchNode.getType() != 115) {
                throw Kit.codeBug();
            } else {
                Node gotoTarget = Node.newTarget();
                if (caseExpression != null) {
                    Jump caseNode = new Jump(116, caseExpression);
                    caseNode.target = gotoTarget;
                    switchNode.addChildToBack(caseNode);
                } else {
                    switchNode.setDefault(gotoTarget);
                }
                switchBlock.addChildToBack(gotoTarget);
                switchBlock.addChildToBack(statements);
            }
        }
    }

    private static void closeSwitch(Node switchBlock) {
        if (switchBlock.getType() != 130) {
            throw Kit.codeBug();
        } else {
            Jump switchNode = (Jump) switchBlock.getFirstChild();
            if (switchNode.getType() != 115) {
                throw Kit.codeBug();
            } else {
                Node switchBreakTarget = Node.newTarget();
                switchNode.target = switchBreakTarget;
                Node defaultTarget = switchNode.getDefault();
                if (defaultTarget == null) {
                    defaultTarget = switchBreakTarget;
                }
                switchBlock.addChildAfter(makeJump(5, defaultTarget), switchNode);
                switchBlock.addChildToBack(switchBreakTarget);
            }
        }
    }

    private static Node createExprStatementNoReturn(Node expr, int lineno) {
        return new Node(134, expr, lineno);
    }

    private static Node createString(String string) {
        return Node.newString(string);
    }

    private static Node initFunction(FunctionNode fnNode, int functionIndex, Node statements, int functionType) {
        fnNode.setFunctionType(functionType);
        fnNode.addChildToBack(statements);
        int functionCount = fnNode.getFunctionCount();
        if (functionCount != 0) {
            fnNode.setRequiresActivation();
        }
        if (functionType == 2) {
            Name name = fnNode.getFunctionName();
            if (name != null && name.length() != 0 && fnNode.getSymbol(name.getIdentifier()) == null) {
                fnNode.putSymbol(new AstSymbol(110, name.getIdentifier()));
                Node setFn = new Node(134, new Node(8, Node.newString(49, name.getIdentifier()), new Node(64)));
                statements.addChildrenToFront(setFn);
            }
        }
        Node lastStmt = statements.getLastChild();
        if (lastStmt == null || lastStmt.getType() != 4) {
            statements.addChildToBack(new Node(4));
        }
        Node result = Node.newString(110, fnNode.getName());
        result.putIntProp(1, functionIndex);
        return result;
    }

    private static Node createFor(Scope loop, Node init, Node test, Node incr, Node body) {
        if (init.getType() == 154) {
            Scope let = Scope.splitScope(loop);
            let.setType(154);
            let.addChildrenToBack(init);
            let.addChildToBack(createLoop(loop, 2, body, test, new Node(129), incr));
            return let;
        } else {
            return createLoop(loop, 2, body, test, init, incr);
        }
    }

    private static Node createLoop(Jump loop, int loopType, Node body, Node cond, Node init, Node incr) {
        Node bodyTarget = Node.newTarget();
        Node condTarget = Node.newTarget();
        if (loopType == 2 && cond.getType() == 129) {
            cond = new Node(45);
        }
        Jump IFEQ = new Jump(6, cond);
        IFEQ.target = bodyTarget;
        Node breakTarget = Node.newTarget();
        loop.addChildToBack(bodyTarget);
        loop.addChildrenToBack(body);
        if (loopType == 1 || loopType == 2) {
            loop.addChildrenToBack(new Node(129, loop.getLineno()));
        }
        loop.addChildToBack(condTarget);
        loop.addChildToBack(IFEQ);
        loop.addChildToBack(breakTarget);
        loop.target = breakTarget;
        Node continueTarget = condTarget;
        if (loopType == 1 || loopType == 2) {
            loop.addChildToFront(makeJump(5, condTarget));
            if (loopType == 2) {
                int initType = init.getType();
                if (initType != 129) {
                    if (initType != 123 && initType != 154) {
                        init = new Node(134, init);
                    }
                    loop.addChildToFront(init);
                }
                Node incrTarget = Node.newTarget();
                loop.addChildAfter(incrTarget, body);
                if (incr.getType() != 129) {
                    incr = new Node(134, incr);
                    loop.addChildAfter(incr, incrTarget);
                }
                continueTarget = incrTarget;
            }
        }
        loop.setContinue(continueTarget);
        return loop;
    }

    private static Node createIf(Node cond, Node ifTrue, Node ifFalse, int lineno) {
        int condStatus = isAlwaysDefinedBoolean(cond);
        if (condStatus == 1) {
            return ifTrue;
        } else if (condStatus == -1) {
            return ifFalse != null ? ifFalse : new Node(130, lineno);
        } else {
            Node result = new Node(130, lineno);
            Node ifNotTarget = Node.newTarget();
            Jump IFNE = new Jump(7, cond);
            IFNE.target = ifNotTarget;
            result.addChildToBack(IFNE);
            result.addChildrenToBack(ifTrue);
            if (ifFalse != null) {
                Node endTarget = Node.newTarget();
                result.addChildToBack(makeJump(5, endTarget));
                result.addChildToBack(ifNotTarget);
                result.addChildrenToBack(ifFalse);
                result.addChildToBack(endTarget);
            } else {
                result.addChildToBack(ifNotTarget);
            }
            return result;
        }
    }

    private static Node createCondExpr(Node cond, Node ifTrue, Node ifFalse) {
        int condStatus = isAlwaysDefinedBoolean(cond);
        if (condStatus == 1) {
            return ifTrue;
        } else {
            return condStatus == -1 ? ifFalse : new Node(103, cond, ifTrue, ifFalse);
        }
    }

    private static Node createUnary(int nodeType, Node child) {
        int childType = child.getType();
        switch(nodeType) {
            case 26:
                int status = isAlwaysDefinedBoolean(child);
                if (status != 0) {
                    int type;
                    if (status == 1) {
                        type = 44;
                    } else {
                        type = 45;
                    }
                    if (childType != 45 && childType != 44) {
                        return new Node(type);
                    }
                    child.setType(type);
                    return child;
                }
                break;
            case 27:
                if (childType == 40) {
                    int value = ScriptRuntime.toInt32(child.getDouble());
                    child.setDouble((double) (~value));
                    return child;
                }
            case 28:
            case 30:
            default:
                break;
            case 29:
                if (childType == 40) {
                    child.setDouble(-child.getDouble());
                    return child;
                }
                break;
            case 31:
                Node n;
                if (childType == 39) {
                    child.setType(49);
                    Node right = Node.newString(child.getString());
                    n = new Node(nodeType, child, right);
                } else if (childType == 33 || childType == 36) {
                    Node left = child.getFirstChild();
                    Node right = child.getLastChild();
                    child.removeChild(left);
                    child.removeChild(right);
                    n = new Node(nodeType, left, right);
                } else if (childType == 68) {
                    Node ref = child.getFirstChild();
                    child.removeChild(ref);
                    n = new Node(70, ref);
                } else {
                    n = new Node(nodeType, new Node(45), child);
                }
                return n;
            case 32:
                if (childType == 39) {
                    child.setType(138);
                    return child;
                }
        }
        return new Node(nodeType, child);
    }

    private static Node createIncDec(int nodeType, boolean post, Node child) {
        child = makeReference(child);
        int childType = child.getType();
        switch(childType) {
            case 33:
            case 36:
            case 39:
            case 68:
                Node n = new Node(nodeType, child);
                int incrDecrMask = 0;
                if (nodeType == 108) {
                    incrDecrMask |= 1;
                }
                if (post) {
                    incrDecrMask |= 2;
                }
                n.putIntProp(13, incrDecrMask);
                return n;
            default:
                throw Kit.codeBug();
        }
    }

    private static Node createBinary(int nodeType, Node left, Node right, Context cx) {
        String s2;
        label82: {
            switch(nodeType) {
                case 21:
                    if (left.type == 41) {
                        if (right.type == 41) {
                            s2 = right.getString();
                            break label82;
                        }
                        if (right.type == 40) {
                            s2 = ScriptRuntime.numberToString(cx, right.getDouble(), 10);
                            break label82;
                        }
                    } else if (left.type == 40) {
                        if (right.type == 40) {
                            left.setDouble(left.getDouble() + right.getDouble());
                            return left;
                        }
                        if (right.type == 41) {
                            s2 = ScriptRuntime.numberToString(cx, left.getDouble(), 10);
                            String s2x = right.getString();
                            right.setString(s2.concat(s2x));
                            return right;
                        }
                    }
                    break;
                case 22:
                    if (left.type == 40) {
                        double ldx = left.getDouble();
                        if (right.type == 40) {
                            left.setDouble(ldx - right.getDouble());
                            return left;
                        }
                        if (ldx == 0.0) {
                            return new Node(29, right);
                        }
                    } else if (right.type == 40 && right.getDouble() == 0.0) {
                        return new Node(28, left);
                    }
                    break;
                case 23:
                    if (left.type == 40) {
                        double ld = left.getDouble();
                        if (right.type == 40) {
                            left.setDouble(ld * right.getDouble());
                            return left;
                        }
                        if (ld == 1.0) {
                            return new Node(28, right);
                        }
                    } else if (right.type == 40 && right.getDouble() == 1.0) {
                        return new Node(28, left);
                    }
                    break;
                case 24:
                    if (right.type == 40) {
                        double rd = right.getDouble();
                        if (left.type == 40) {
                            left.setDouble(left.getDouble() / rd);
                            return left;
                        }
                        if (rd == 1.0) {
                            return new Node(28, left);
                        }
                    }
                    break;
                case 105:
                    int leftStatus = isAlwaysDefinedBoolean(left);
                    if (leftStatus == 1) {
                        return left;
                    }
                    if (leftStatus == -1) {
                        return right;
                    }
                    break;
                case 106:
                    int leftStatusx = isAlwaysDefinedBoolean(left);
                    if (leftStatusx == -1) {
                        return left;
                    }
                    if (leftStatusx == 1) {
                        return right;
                    }
            }
            return new Node(nodeType, left, right);
        }
        String s1 = left.getString();
        left.setString(s1.concat(s2));
        return left;
    }

    private static Node createUseLocal(Node localBlock) {
        if (142 != localBlock.getType()) {
            throw Kit.codeBug();
        } else {
            Node result = new Node(54);
            result.putProp(3, localBlock);
            return result;
        }
    }

    private static Jump makeJump(int type, Node target) {
        Jump n = new Jump(type);
        n.target = target;
        return n;
    }

    private static Node makeReference(Node node) {
        int type = node.getType();
        switch(type) {
            case 33:
            case 36:
            case 39:
            case 68:
                return node;
            case 38:
                node.setType(71);
                return new Node(68, node);
            default:
                return null;
        }
    }

    private static int isAlwaysDefinedBoolean(Node node) {
        switch(node.getType()) {
            case 40:
                double num = node.getDouble();
                if (!Double.isNaN(num) && num != 0.0) {
                    return 1;
                }
                return -1;
            case 41:
            case 43:
            default:
                return 0;
            case 42:
            case 44:
                return -1;
            case 45:
                return 1;
        }
    }

    public IRFactory(Context cx) {
        super(cx);
    }

    public IRFactory(Context cx, CompilerEnvirons env) {
        this(cx, env, env.getErrorReporter());
    }

    public IRFactory(Context cx, CompilerEnvirons env, ErrorReporter errorReporter) {
        super(cx, env, errorReporter);
    }

    public ScriptNode transformTree(AstRoot root) {
        this.currentScriptOrFn = root;
        this.inUseStrictDirective = root.isInStrictMode();
        return (ScriptNode) this.transform(root);
    }

    public Node transform(AstNode node) {
        ???;
    }

    private Node transformArrayComp(ArrayComprehension node) {
        int lineno = node.getLineno();
        Scope scopeNode = this.createScopeNode(158, lineno);
        String arrayName = this.currentScriptOrFn.getNextTempName();
        this.pushScope(scopeNode);
        Scope var8;
        try {
            this.defineSymbol(154, arrayName);
            Node block = new Node(130, lineno);
            Node newArray = this.createCallOrNew(30, this.createName("Array"));
            Node init = new Node(134, this.createAssignment(91, this.createName(arrayName), newArray), lineno);
            block.addChildToBack(init);
            block.addChildToBack(this.arrayCompTransformHelper(node, arrayName));
            scopeNode.addChildToBack(block);
            scopeNode.addChildToBack(this.createName(arrayName));
            var8 = scopeNode;
        } finally {
            this.popScope();
        }
        return var8;
    }

    private Node arrayCompTransformHelper(ArrayComprehension node, String arrayName) {
        int lineno = node.getLineno();
        Node expr = this.transform(node.getResult());
        List<ArrayComprehensionLoop> loops = node.getLoops();
        int numLoops = loops.size();
        Node[] iterators = new Node[numLoops];
        Node[] iteratedObjs = new Node[numLoops];
        for (int i = 0; i < numLoops; i++) {
            ArrayComprehensionLoop acl = (ArrayComprehensionLoop) loops.get(i);
            AstNode iter = acl.getIterator();
            String name;
            if (iter.getType() == 39) {
                name = iter.getString();
            } else {
                name = this.currentScriptOrFn.getNextTempName();
                this.defineSymbol(88, name);
                expr = createBinary(90, this.createAssignment(91, iter, this.createName(name)), expr, this.cx);
            }
            Node init = this.createName(name);
            this.defineSymbol(154, name);
            iterators[i] = init;
            iteratedObjs[i] = this.transform(acl.getIteratedObject());
        }
        Node call = this.createCallOrNew(38, this.createPropertyGet(this.createName(arrayName), null, "push", 0));
        Node body = new Node(134, call, lineno);
        if (node.getFilter() != null) {
            body = createIf(this.transform(node.getFilter()), body, null, lineno);
        }
        int pushed = 0;
        boolean var18 = false;
        try {
            var18 = true;
            for (int var23 = numLoops - 1; var23 >= 0; var23--) {
                ArrayComprehensionLoop acl = (ArrayComprehensionLoop) loops.get(var23);
                Scope loop = this.createLoopNode(null, acl.getLineno());
                this.pushScope(loop);
                pushed++;
                body = this.createForIn(154, loop, iterators[var23], iteratedObjs[var23], body, acl.isForEach(), acl.isForOf());
            }
            var18 = false;
        } finally {
            if (var18) {
                for (int i = 0; i < pushed; i++) {
                    this.popScope();
                }
            }
        }
        for (int i = 0; i < pushed; i++) {
            this.popScope();
        }
        call.addChildToBack(expr);
        return body;
    }

    private Node transformArrayLiteral(ArrayLiteral node) {
        if (node.isDestructuring()) {
            return node;
        } else {
            List<AstNode> elems = node.getElements();
            Node array = new Node(66);
            List<Integer> skipIndexes = null;
            for (int i = 0; i < elems.size(); i++) {
                AstNode elem = (AstNode) elems.get(i);
                if (elem.getType() != 129) {
                    array.addChildToBack(this.transform(elem));
                } else {
                    if (skipIndexes == null) {
                        skipIndexes = new ArrayList();
                    }
                    skipIndexes.add(i);
                }
            }
            array.putIntProp(21, node.getDestructuringLength());
            if (skipIndexes != null) {
                int[] skips = new int[skipIndexes.size()];
                for (int ix = 0; ix < skipIndexes.size(); ix++) {
                    skips[ix] = (Integer) skipIndexes.get(ix);
                }
                array.putProp(11, skips);
            }
            return array;
        }
    }

    private Node transformAssignment(Assignment node) {
        AstNode left = this.removeParens(node.getLeft());
        Node target;
        if (this.isDestructuring(left)) {
            target = left;
        } else {
            target = this.transform(left);
        }
        return this.createAssignment(node.getType(), target, this.transform(node.getRight()));
    }

    private Node transformBlock(AstNode node) {
        if (node instanceof Scope) {
            this.pushScope((Scope) node);
        }
        AstNode var9;
        try {
            List<Node> kids = new ArrayList();
            for (Node kid : node) {
                kids.add(this.transform((AstNode) kid));
            }
            node.removeChildren();
            for (Node kid : kids) {
                node.addChildToBack(kid);
            }
            var9 = node;
        } finally {
            if (node instanceof Scope) {
                this.popScope();
            }
        }
        return var9;
    }

    private Node transformCondExpr(ConditionalExpression node) {
        Node test = this.transform(node.getTestExpression());
        Node ifTrue = this.transform(node.getTrueExpression());
        Node ifFalse = this.transform(node.getFalseExpression());
        return createCondExpr(test, ifTrue, ifFalse);
    }

    private Node transformContinue(ContinueStatement node) {
        return node;
    }

    private Node transformDoLoop(DoLoop loop) {
        loop.setType(133);
        this.pushScope(loop);
        Node var4;
        try {
            Node body = this.transform(loop.getBody());
            Node cond = this.transform(loop.getCondition());
            var4 = createLoop(loop, 0, body, cond, null, null);
        } finally {
            this.popScope();
        }
        return var4;
    }

    private Node transformElementGet(ElementGet node) {
        Node target = this.transform(node.getTarget());
        Node element = this.transform(node.getElement());
        return new Node(36, target, element);
    }

    private Node transformExprStmt(ExpressionStatement node) {
        Node expr = this.transform(node.getExpression());
        return new Node(node.getType(), expr, node.getLineno());
    }

    private Node transformForInLoop(ForInLoop loop) {
        loop.setType(133);
        this.pushScope(loop);
        Node var7;
        try {
            int declType = -1;
            AstNode iter = loop.getIterator();
            if (iter instanceof VariableDeclaration) {
                declType = iter.getType();
            }
            Node lhs = this.transform(iter);
            Node obj = this.transform(loop.getIteratedObject());
            Node body = this.transform(loop.getBody());
            var7 = this.createForIn(declType, loop, lhs, obj, body, loop.isForEach(), loop.isForOf());
        } finally {
            this.popScope();
        }
        return var7;
    }

    private Node transformForLoop(ForLoop loop) {
        loop.setType(133);
        Scope savedScope = this.currentScope;
        this.currentScope = loop;
        Node var7;
        try {
            Node init = this.transform(loop.getInitializer());
            Node test = this.transform(loop.getCondition());
            Node incr = this.transform(loop.getIncrement());
            Node body = this.transform(loop.getBody());
            var7 = createFor(loop, init, test, incr, body);
        } finally {
            this.currentScope = savedScope;
        }
        return var7;
    }

    private Node transformFunction(FunctionNode fn) {
        int index = this.currentScriptOrFn.addFunction(fn);
        Parser.PerFunctionVariables savedVars = new Parser.PerFunctionVariables(fn);
        Node var9;
        try {
            Node destructuring = (Node) fn.getProp(23);
            fn.removeProp(23);
            int lineno = fn.getBody().getLineno();
            this.nestingOfFunction++;
            Node body = this.transform(fn.getBody());
            if (destructuring != null) {
                body.addChildToFront(new Node(134, destructuring, lineno));
            }
            int syntheticType = fn.getFunctionType();
            Node pn = initFunction(fn, index, body, syntheticType);
            var9 = pn;
        } finally {
            this.nestingOfFunction--;
            savedVars.restore();
        }
        return var9;
    }

    private Node transformFunctionCall(FunctionCall node) {
        Node call = this.createCallOrNew(38, this.transform(node.getTarget()));
        call.setLineno(node.getLineno());
        for (AstNode arg : node.getArguments()) {
            call.addChildToBack(this.transform(arg));
        }
        return call;
    }

    private Node transformGenExpr(GeneratorExpression node) {
        FunctionNode fn = new FunctionNode();
        fn.setSourceName(this.currentScriptOrFn.getNextTempName());
        fn.setIsGenerator();
        fn.setFunctionType(2);
        fn.setRequiresActivation();
        int index = this.currentScriptOrFn.addFunction(fn);
        Parser.PerFunctionVariables savedVars = new Parser.PerFunctionVariables(fn);
        Node pn;
        try {
            Node destructuring = (Node) fn.getProp(23);
            fn.removeProp(23);
            int lineno = node.lineno;
            this.nestingOfFunction++;
            Node body = this.genExprTransformHelper(node);
            if (destructuring != null) {
                body.addChildToFront(new Node(134, destructuring, lineno));
            }
            int syntheticType = fn.getFunctionType();
            pn = initFunction(fn, index, body, syntheticType);
        } finally {
            this.nestingOfFunction--;
            savedVars.restore();
        }
        Node call = this.createCallOrNew(38, pn);
        call.setLineno(node.getLineno());
        return call;
    }

    private Node genExprTransformHelper(GeneratorExpression node) {
        int lineno = node.getLineno();
        Node expr = this.transform(node.getResult());
        List<GeneratorExpressionLoop> loops = node.getLoops();
        int numLoops = loops.size();
        Node[] iterators = new Node[numLoops];
        Node[] iteratedObjs = new Node[numLoops];
        for (int i = 0; i < numLoops; i++) {
            GeneratorExpressionLoop acl = (GeneratorExpressionLoop) loops.get(i);
            AstNode iter = acl.getIterator();
            String name;
            if (iter.getType() == 39) {
                name = iter.getString();
            } else {
                name = this.currentScriptOrFn.getNextTempName();
                this.defineSymbol(88, name);
                expr = createBinary(90, this.createAssignment(91, iter, this.createName(name)), expr, this.cx);
            }
            Node init = this.createName(name);
            this.defineSymbol(154, name);
            iterators[i] = init;
            iteratedObjs[i] = this.transform(acl.getIteratedObject());
        }
        Node yield = new Node(73, expr, node.getLineno());
        Node body = new Node(134, yield, lineno);
        if (node.getFilter() != null) {
            body = createIf(this.transform(node.getFilter()), body, null, lineno);
        }
        int pushed = 0;
        boolean var17 = false;
        try {
            var17 = true;
            for (int var22 = numLoops - 1; var22 >= 0; var22--) {
                GeneratorExpressionLoop acl = (GeneratorExpressionLoop) loops.get(var22);
                Scope loop = this.createLoopNode(null, acl.getLineno());
                this.pushScope(loop);
                pushed++;
                body = this.createForIn(154, loop, iterators[var22], iteratedObjs[var22], body, acl.isForEach(), acl.isForOf());
            }
            var17 = false;
        } finally {
            if (var17) {
                for (int i = 0; i < pushed; i++) {
                    this.popScope();
                }
            }
        }
        for (int i = 0; i < pushed; i++) {
            this.popScope();
        }
        return body;
    }

    private Node transformIf(IfStatement n) {
        Node cond = this.transform(n.getCondition());
        Node ifTrue = this.transform(n.getThenPart());
        Node ifFalse = null;
        if (n.getElsePart() != null) {
            ifFalse = this.transform(n.getElsePart());
        }
        return createIf(cond, ifTrue, ifFalse, n.getLineno());
    }

    private Node transformInfix(InfixExpression node) {
        Node left = this.transform(node.getLeft());
        Node right = this.transform(node.getRight());
        return createBinary(node.getType(), left, right, this.cx);
    }

    private Node transformLabeledStatement(LabeledStatement ls) {
        Label label = ls.getFirstLabel();
        Node statement = this.transform(ls.getStatement());
        Node breakTarget = Node.newTarget();
        Node block = new Node(130, label, statement, breakTarget);
        label.target = breakTarget;
        return block;
    }

    private Node transformLetNode(LetNode node) {
        this.pushScope(node);
        LetNode var3;
        try {
            Node vars = this.transformVariableInitializers(node.getVariables());
            node.addChildToBack(vars);
            if (node.getBody() != null) {
                node.addChildToBack(this.transform(node.getBody()));
            }
            var3 = node;
        } finally {
            this.popScope();
        }
        return var3;
    }

    private Node transformNewExpr(NewExpression node) {
        Node nx = this.createCallOrNew(30, this.transform(node.getTarget()));
        nx.setLineno(node.getLineno());
        for (AstNode arg : node.getArguments()) {
            nx.addChildToBack(this.transform(arg));
        }
        if (node.getInitializer() != null) {
            nx.addChildToBack(this.transformObjectLiteral(node.getInitializer()));
        }
        return nx;
    }

    private Node transformObjectLiteral(ObjectLiteral node) {
        if (node.isDestructuring()) {
            return node;
        } else {
            List<ObjectProperty> elems = node.getElements();
            Node object = new Node(67);
            Object[] properties;
            if (elems.isEmpty()) {
                properties = ScriptRuntime.EMPTY_OBJECTS;
            } else {
                int size = elems.size();
                int i = 0;
                properties = new Object[size];
                for (ObjectProperty prop : elems) {
                    properties[i++] = this.getPropKey(prop.getLeft());
                    Node right = this.transform(prop.getRight());
                    if (prop.isGetterMethod()) {
                        right = createUnary(152, right);
                    } else if (prop.isSetterMethod()) {
                        right = createUnary(153, right);
                    } else if (prop.isNormalMethod()) {
                        right = createUnary(164, right);
                    }
                    object.addChildToBack(right);
                }
            }
            object.putProp(12, properties);
            return object;
        }
    }

    private Object getPropKey(Node id) {
        Object key;
        if (id instanceof Name) {
            String s = ((Name) id).getIdentifier();
            key = ScriptRuntime.getIndexObject(s);
        } else if (id instanceof StringLiteral) {
            String s = ((StringLiteral) id).getValue();
            key = ScriptRuntime.getIndexObject(s);
        } else {
            if (!(id instanceof NumberLiteral)) {
                throw Kit.codeBug();
            }
            double n = ((NumberLiteral) id).getNumber();
            key = ScriptRuntime.getIndexObject(this.cx, n);
        }
        return key;
    }

    private Node transformParenExpr(ParenthesizedExpression node) {
        AstNode expr = node.getExpression();
        while (expr instanceof ParenthesizedExpression) {
            expr = ((ParenthesizedExpression) expr).getExpression();
        }
        Node result = this.transform(expr);
        result.putProp(19, Boolean.TRUE);
        return result;
    }

    private Node transformPropertyGet(PropertyGet node) {
        Node target = this.transform(node.getTarget());
        String name = node.getProperty().getIdentifier();
        return this.createPropertyGet(target, null, name, 0);
    }

    private Node transformTemplateLiteral(TemplateLiteral node) {
        List<AstNode> elems = node.getElements();
        Node pn = Node.newString("");
        for (AstNode elem : elems) {
            if (elem.getType() != 168) {
                pn = createBinary(21, pn, this.transform(elem), this.cx);
            } else {
                TemplateCharacters chars = (TemplateCharacters) elem;
                String value = chars.getValue();
                if (value.length() > 0) {
                    pn = createBinary(21, pn, Node.newString(value), this.cx);
                }
            }
        }
        return pn;
    }

    private Node transformTemplateLiteralCall(TaggedTemplateLiteral node) {
        Node call = this.createCallOrNew(38, this.transform(node.getTarget()));
        call.setLineno(node.getLineno());
        TemplateLiteral templateLiteral = (TemplateLiteral) node.getTemplateLiteral();
        List<AstNode> elems = templateLiteral.getElements();
        call.addChildToBack(templateLiteral);
        for (AstNode elem : elems) {
            if (elem.getType() != 168) {
                call.addChildToBack(this.transform(elem));
            } else {
                TemplateCharacters var7 = (TemplateCharacters) elem;
            }
        }
        this.currentScriptOrFn.addTemplateLiteral(templateLiteral);
        return call;
    }

    private Node transformRegExp(RegExpLiteral node) {
        this.currentScriptOrFn.addRegExp(node);
        return node;
    }

    private Node transformReturn(ReturnStatement node) {
        AstNode rv = node.getReturnValue();
        Node value = rv == null ? null : this.transform(rv);
        return rv == null ? new Node(4, node.getLineno()) : new Node(4, value, node.getLineno());
    }

    private Node transformScript(ScriptNode node) {
        if (this.currentScope != null) {
            Kit.codeBug();
        }
        this.currentScope = node;
        Node body = new Node(130);
        for (Node kid : node) {
            body.addChildToBack(this.transform((AstNode) kid));
        }
        node.removeChildren();
        Node children = body.getFirstChild();
        if (children != null) {
            node.addChildrenToBack(children);
        }
        return node;
    }

    private Node transformString(StringLiteral node) {
        return Node.newString(node.getValue());
    }

    private Node transformSwitch(SwitchStatement node) {
        Node switchExpr = this.transform(node.getExpression());
        node.addChildToBack(switchExpr);
        Node block = new Node(130, node, node.getLineno());
        for (SwitchCase sc : node.getCases()) {
            AstNode expr = sc.getExpression();
            Node caseExpr = null;
            if (expr != null) {
                caseExpr = this.transform(expr);
            }
            List<AstNode> stmts = sc.getStatements();
            Node body = new Block();
            if (stmts != null) {
                for (AstNode kid : stmts) {
                    body.addChildToBack(this.transform(kid));
                }
            }
            addSwitchCase(block, caseExpr, body);
        }
        closeSwitch(block);
        return block;
    }

    private Node transformThrow(ThrowStatement node) {
        Node value = this.transform(node.getExpression());
        return new Node(50, value, node.getLineno());
    }

    private Node transformTry(TryStatement node) {
        Node tryBlock = this.transform(node.getTryBlock());
        Node catchBlocks = new Block();
        for (CatchClause cc : node.getCatchClauses()) {
            String varName = cc.getVarName().getIdentifier();
            AstNode ccc = cc.getCatchCondition();
            Node catchCond;
            if (ccc != null) {
                catchCond = this.transform(ccc);
            } else {
                catchCond = new EmptyExpression();
            }
            Node body = this.transform(cc.getBody());
            catchBlocks.addChildToBack(this.createCatch(varName, catchCond, body, cc.getLineno()));
        }
        Node finallyBlock = null;
        if (node.getFinallyBlock() != null) {
            finallyBlock = this.transform(node.getFinallyBlock());
        }
        return this.createTryCatchFinally(tryBlock, catchBlocks, finallyBlock, node.getLineno());
    }

    private Node transformUnary(UnaryExpression node) {
        int type = node.getType();
        Node child = this.transform(node.getOperand());
        return type != 107 && type != 108 ? createUnary(type, child) : createIncDec(type, node.isPostfix(), child);
    }

    private Node transformVariables(VariableDeclaration node) {
        this.transformVariableInitializers(node);
        AstNode parent = node.getParent();
        return node;
    }

    private Node transformVariableInitializers(VariableDeclaration node) {
        for (VariableInitializer var : node.getVariables()) {
            AstNode target = var.getTarget();
            AstNode init = var.getInitializer();
            Node left;
            if (var.isDestructuring()) {
                left = target;
            } else {
                left = this.transform(target);
            }
            Node right = null;
            if (init != null) {
                right = this.transform(init);
            }
            if (var.isDestructuring()) {
                if (right == null) {
                    node.addChildToBack(left);
                } else {
                    Node d = this.createDestructuringAssignment(node.getType(), left, right);
                    node.addChildToBack(d);
                }
            } else {
                if (right != null) {
                    left.addChildToBack(right);
                }
                node.addChildToBack(left);
            }
        }
        return node;
    }

    private Node transformWhileLoop(WhileLoop loop) {
        loop.setType(133);
        this.pushScope(loop);
        Node var4;
        try {
            Node cond = this.transform(loop.getCondition());
            Node body = this.transform(loop.getBody());
            var4 = createLoop(loop, 1, body, cond, null, null);
        } finally {
            this.popScope();
        }
        return var4;
    }

    private Node transformWith(WithStatement node) {
        Node expr = this.transform(node.getExpression());
        Node stmt = this.transform(node.getStatement());
        return this.createWith(expr, stmt, node.getLineno());
    }

    private Node transformYield(Yield node) {
        Node kid = node.getValue() == null ? null : this.transform(node.getValue());
        return kid != null ? new Node(node.getType(), kid, node.getLineno()) : new Node(node.getType(), node.getLineno());
    }

    private Node createCatch(String varName, Node catchCond, Node stmts, int lineno) {
        if (catchCond == null) {
            catchCond = new Node(129);
        }
        return new Node(125, this.createName(varName), catchCond, stmts, lineno);
    }

    private Scope createLoopNode(Node loopLabel, int lineno) {
        Scope result = this.createScopeNode(133, lineno);
        if (loopLabel != null) {
            ((Jump) loopLabel).setLoop(result);
        }
        return result;
    }

    private Node createForIn(int declType, Node loop, Node lhs, Node obj, Node body, boolean isForEach, boolean isForOf) {
        int destructuring = -1;
        int destructuringLen = 0;
        int type = lhs.getType();
        Node lvalue;
        if (type == 123 || type == 154) {
            Node kid = lhs.getLastChild();
            int kidType = kid.getType();
            if (kidType != 66 && kidType != 67) {
                if (kidType != 39) {
                    this.reportError("msg.bad.for.in.lhs");
                    return null;
                }
                lvalue = Node.newString(39, kid.getString());
            } else {
                destructuring = kidType;
                type = kidType;
                lvalue = kid;
                if (kid instanceof ArrayLiteral) {
                    destructuringLen = ((ArrayLiteral) kid).getDestructuringLength();
                }
            }
        } else if (type != 66 && type != 67) {
            lvalue = makeReference(lhs);
            if (lvalue == null) {
                this.reportError("msg.bad.for.in.lhs");
                return null;
            }
        } else {
            destructuring = type;
            lvalue = lhs;
            if (lhs instanceof ArrayLiteral) {
                destructuringLen = ((ArrayLiteral) lhs).getDestructuringLength();
            }
        }
        Node localBlock = new Node(142);
        int initType = isForEach ? 59 : (isForOf ? 61 : (destructuring != -1 ? 60 : 58));
        Node init = new Node(initType, obj);
        init.putProp(3, localBlock);
        Node cond = new Node(62);
        cond.putProp(3, localBlock);
        Node id = new Node(63);
        id.putProp(3, localBlock);
        Node newBody = new Node(130);
        Node assign;
        if (destructuring != -1) {
            assign = this.createDestructuringAssignment(declType, lvalue, id);
            if (!isForEach && !isForOf && (destructuring == 67 || destructuringLen != 2)) {
                this.reportError("msg.bad.for.in.destruct");
            }
        } else {
            assign = this.simpleAssignment(lvalue, id);
        }
        newBody.addChildToBack(new Node(134, assign));
        newBody.addChildToBack(body);
        loop = createLoop((Jump) loop, 1, newBody, cond, null, null);
        loop.addChildToFront(init);
        if (type == 123 || type == 154) {
            loop.addChildToFront(lhs);
        }
        localBlock.addChildToBack(loop);
        return localBlock;
    }

    private Node createTryCatchFinally(Node tryBlock, Node catchBlocks, Node finallyBlock, int lineno) {
        boolean hasFinally = finallyBlock != null && (finallyBlock.getType() != 130 || finallyBlock.hasChildren());
        if (tryBlock.getType() == 130 && !tryBlock.hasChildren() && !hasFinally) {
            return tryBlock;
        } else {
            boolean hasCatch = catchBlocks.hasChildren();
            if (!hasFinally && !hasCatch) {
                return tryBlock;
            } else {
                Node handlerBlock = new Node(142);
                Jump pn = new Jump(82, tryBlock, lineno);
                pn.putProp(3, handlerBlock);
                if (hasCatch) {
                    Node endCatch = Node.newTarget();
                    pn.addChildToBack(makeJump(5, endCatch));
                    Node catchTarget = Node.newTarget();
                    pn.target = catchTarget;
                    pn.addChildToBack(catchTarget);
                    Node catchScopeBlock = new Node(142);
                    Node cb = catchBlocks.getFirstChild();
                    boolean hasDefault = false;
                    for (int scopeIndex = 0; cb != null; scopeIndex++) {
                        int catchLineNo = cb.getLineno();
                        Node name = cb.getFirstChild();
                        Node cond = name.getNext();
                        Node catchStatement = cond.getNext();
                        cb.removeChild(name);
                        cb.removeChild(cond);
                        cb.removeChild(catchStatement);
                        catchStatement.addChildToBack(new Node(3));
                        catchStatement.addChildToBack(makeJump(5, endCatch));
                        Node condStmt;
                        if (cond.getType() == 129) {
                            condStmt = catchStatement;
                            hasDefault = true;
                        } else {
                            condStmt = createIf(cond, catchStatement, null, catchLineNo);
                        }
                        Node catchScope = new Node(57, name, createUseLocal(handlerBlock));
                        catchScope.putProp(3, catchScopeBlock);
                        catchScope.putIntProp(14, scopeIndex);
                        catchScopeBlock.addChildToBack(catchScope);
                        catchScopeBlock.addChildToBack(this.createWith(createUseLocal(catchScopeBlock), condStmt, catchLineNo));
                        cb = cb.getNext();
                    }
                    pn.addChildToBack(catchScopeBlock);
                    if (!hasDefault) {
                        Node rethrow = new Node(51);
                        rethrow.putProp(3, handlerBlock);
                        pn.addChildToBack(rethrow);
                    }
                    pn.addChildToBack(endCatch);
                }
                if (hasFinally) {
                    Node finallyTarget = Node.newTarget();
                    pn.setFinally(finallyTarget);
                    pn.addChildToBack(makeJump(136, finallyTarget));
                    Node finallyEnd = Node.newTarget();
                    pn.addChildToBack(makeJump(5, finallyEnd));
                    pn.addChildToBack(finallyTarget);
                    Node fBlock = new Node(126, finallyBlock);
                    fBlock.putProp(3, handlerBlock);
                    pn.addChildToBack(fBlock);
                    pn.addChildToBack(finallyEnd);
                }
                handlerBlock.addChildToBack(pn);
                return handlerBlock;
            }
        }
    }

    private Node createWith(Node obj, Node body, int lineno) {
        this.setRequiresActivation();
        Node result = new Node(130, lineno);
        result.addChildToBack(new Node(2, obj));
        Node bodyNode = new Node(124, body, lineno);
        result.addChildrenToBack(bodyNode);
        result.addChildToBack(new Node(3));
        return result;
    }

    private Node createCallOrNew(int nodeType, Node child) {
        int type = 0;
        if (child.getType() == 39) {
            String name = child.getString();
            if (name.equals("eval")) {
                type = 1;
            } else if (name.equals("With")) {
                type = 2;
            }
        } else if (child.getType() == 33) {
            String name = child.getLastChild().getString();
            if (name.equals("eval")) {
                type = 1;
            }
        }
        Node node = new Node(nodeType, child);
        if (type != 0) {
            this.setRequiresActivation();
            node.putIntProp(10, type);
        }
        return node;
    }

    private Node createPropertyGet(Node target, String namespace, String name, int memberTypeFlags) {
        if (namespace != null || memberTypeFlags != 0) {
            Node elem = Node.newString(name);
            memberTypeFlags |= 1;
            return this.createMemberRefGet(target, namespace, elem, memberTypeFlags);
        } else if (target == null) {
            return this.createName(name);
        } else {
            this.checkActivationName(name, 33);
            if (ScriptRuntime.isSpecialProperty(name)) {
                Node ref = new Node(72, target);
                ref.putProp(17, name);
                return new Node(68, ref);
            } else {
                return new Node(33, target, Node.newString(name));
            }
        }
    }

    private Node createMemberRefGet(Node target, String namespace, Node elem, int memberTypeFlags) {
        return new Node(68, elem);
    }

    private Node createAssignment(int assignType, Node left, Node right) {
        Node ref = makeReference(left);
        if (ref == null) {
            if (left.getType() != 66 && left.getType() != 67) {
                this.reportError("msg.bad.assign.left");
                return right;
            } else if (assignType != 91) {
                this.reportError("msg.bad.destruct.op");
                return right;
            } else {
                return this.createDestructuringAssignment(-1, left, right);
            }
        } else {
            int assignOp;
            switch(assignType) {
                case 91:
                    return this.simpleAssignment(ref, right);
                case 92:
                    assignOp = 9;
                    break;
                case 93:
                    assignOp = 10;
                    break;
                case 94:
                    assignOp = 11;
                    break;
                case 95:
                    assignOp = 18;
                    break;
                case 96:
                    assignOp = 19;
                    break;
                case 97:
                    assignOp = 20;
                    break;
                case 98:
                    assignOp = 21;
                    break;
                case 99:
                    assignOp = 22;
                    break;
                case 100:
                    assignOp = 23;
                    break;
                case 101:
                    assignOp = 24;
                    break;
                case 102:
                    assignOp = 25;
                    break;
                default:
                    throw Kit.codeBug();
            }
            int nodeType = ref.getType();
            switch(nodeType) {
                case 33:
                case 36:
                    {
                        Node obj = ref.getFirstChild();
                        Node id = ref.getLastChild();
                        int type = nodeType == 33 ? 140 : 141;
                        Node opLeft = new Node(139);
                        Node op = new Node(assignOp, opLeft, right);
                        return new Node(type, obj, id, op);
                    }
                case 39:
                    {
                        Node op = new Node(assignOp, ref, right);
                        Node lvalueLeft = Node.newString(49, ref.getString());
                        return new Node(8, lvalueLeft, op);
                    }
                case 68:
                    {
                        ref = ref.getFirstChild();
                        this.checkMutableReference(ref);
                        Node opLeft = new Node(139);
                        Node op = new Node(assignOp, opLeft, right);
                        return new Node(143, ref, op);
                    }
                default:
                    throw Kit.codeBug();
            }
        }
    }

    boolean isDestructuring(Node n) {
        return n instanceof DestructuringForm && ((DestructuringForm) n).isDestructuring();
    }
}