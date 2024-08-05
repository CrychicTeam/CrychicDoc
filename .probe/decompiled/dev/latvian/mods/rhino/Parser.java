package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.ast.ArrayComprehension;
import dev.latvian.mods.rhino.ast.ArrayComprehensionLoop;
import dev.latvian.mods.rhino.ast.ArrayLiteral;
import dev.latvian.mods.rhino.ast.Assignment;
import dev.latvian.mods.rhino.ast.AstNode;
import dev.latvian.mods.rhino.ast.AstRoot;
import dev.latvian.mods.rhino.ast.AstSymbol;
import dev.latvian.mods.rhino.ast.Block;
import dev.latvian.mods.rhino.ast.BreakStatement;
import dev.latvian.mods.rhino.ast.CatchClause;
import dev.latvian.mods.rhino.ast.Comment;
import dev.latvian.mods.rhino.ast.ConditionalExpression;
import dev.latvian.mods.rhino.ast.ContinueStatement;
import dev.latvian.mods.rhino.ast.DestructuringForm;
import dev.latvian.mods.rhino.ast.DoLoop;
import dev.latvian.mods.rhino.ast.ElementGet;
import dev.latvian.mods.rhino.ast.EmptyExpression;
import dev.latvian.mods.rhino.ast.EmptyStatement;
import dev.latvian.mods.rhino.ast.ErrorNode;
import dev.latvian.mods.rhino.ast.ExpressionStatement;
import dev.latvian.mods.rhino.ast.ForInLoop;
import dev.latvian.mods.rhino.ast.ForLoop;
import dev.latvian.mods.rhino.ast.FunctionCall;
import dev.latvian.mods.rhino.ast.FunctionNode;
import dev.latvian.mods.rhino.ast.GeneratorExpression;
import dev.latvian.mods.rhino.ast.GeneratorExpressionLoop;
import dev.latvian.mods.rhino.ast.IdeErrorReporter;
import dev.latvian.mods.rhino.ast.IfStatement;
import dev.latvian.mods.rhino.ast.InfixExpression;
import dev.latvian.mods.rhino.ast.Jump;
import dev.latvian.mods.rhino.ast.KeywordLiteral;
import dev.latvian.mods.rhino.ast.Label;
import dev.latvian.mods.rhino.ast.LabeledStatement;
import dev.latvian.mods.rhino.ast.LetNode;
import dev.latvian.mods.rhino.ast.Loop;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Parser {

    public static final int ARGC_LIMIT = 65536;

    public static final int CLEAR_TI_MASK = 65535;

    public static final int TI_AFTER_EOL = 65536;

    public static final int TI_CHECK_LABEL = 131072;

    private static final int PROP_ENTRY = 1;

    private static final int GET_ENTRY = 2;

    private static final int SET_ENTRY = 4;

    private static final int METHOD_ENTRY = 8;

    protected final Context cx;

    private final ErrorReporter errorReporter;

    protected int nestingOfFunction;

    protected boolean inUseStrictDirective;

    CompilerEnvirons compilerEnv;

    boolean calledByCompileFunction;

    ScriptNode currentScriptOrFn;

    Scope currentScope;

    private IdeErrorReporter errorCollector;

    private String sourceURI;

    private boolean parseFinished;

    private TokenStream ts;

    private int currentFlaggedToken = 0;

    private int currentToken;

    private int syntaxErrorCount;

    private List<Comment> scannedComments;

    private Comment currentJsDocComment;

    private LabeledStatement currentLabel;

    private boolean inDestructuringAssignment;

    private int endFlags;

    private boolean inForInit;

    private Map<String, LabeledStatement> labelSet;

    private List<Loop> loopSet;

    private List<Jump> loopAndSwitchSet;

    private int prevNameTokenStart;

    private String prevNameTokenString = "";

    private int prevNameTokenLineno;

    private boolean defaultUseStrictDirective;

    private static int getNodeEnd(AstNode n) {
        return n.getPosition() + n.getLength();
    }

    private static String getDirective(AstNode n) {
        if (n instanceof ExpressionStatement) {
            AstNode e = ((ExpressionStatement) n).getExpression();
            if (e instanceof StringLiteral) {
                return ((StringLiteral) e).getValue();
            }
        }
        return null;
    }

    private static boolean nowAllSet(int before, int after, int mask) {
        return (before & mask) != mask && (after & mask) == mask;
    }

    private static int nodeEnd(AstNode node) {
        return node.getPosition() + node.getLength();
    }

    public Parser(Context cx) {
        this(cx, new CompilerEnvirons());
    }

    public Parser(Context cx, CompilerEnvirons compilerEnv) {
        this(cx, compilerEnv, compilerEnv.getErrorReporter());
    }

    public Parser(Context cx, CompilerEnvirons compilerEnv, ErrorReporter errorReporter) {
        this.cx = cx;
        this.compilerEnv = compilerEnv;
        this.errorReporter = errorReporter;
        if (errorReporter instanceof IdeErrorReporter) {
            this.errorCollector = (IdeErrorReporter) errorReporter;
        }
    }

    void addStrictWarning(String messageId, String messageArg) {
        int beg = -1;
        int end = -1;
        if (this.ts != null) {
            beg = this.ts.tokenBeg;
            end = this.ts.tokenEnd - this.ts.tokenBeg;
        }
        this.addStrictWarning(messageId, messageArg, beg, end);
    }

    void addStrictWarning(String messageId, String messageArg, int position, int length) {
        if (this.compilerEnv.isStrictMode()) {
            this.addWarning(messageId, messageArg, position, length);
        }
    }

    void addWarning(String messageId, String messageArg) {
        int beg = -1;
        int end = -1;
        if (this.ts != null) {
            beg = this.ts.tokenBeg;
            end = this.ts.tokenEnd - this.ts.tokenBeg;
        }
        this.addWarning(messageId, messageArg, beg, end);
    }

    void addWarning(String messageId, int position, int length) {
        this.addWarning(messageId, null, position, length);
    }

    void addWarning(String messageId, String messageArg, int position, int length) {
        String message = this.lookupMessage(messageId, messageArg);
        if (this.errorCollector != null) {
            this.errorCollector.warning(message, this.sourceURI, position, length);
        } else {
            this.errorReporter.warning(message, this.sourceURI, this.ts.getLineno(), this.ts.getLine(), this.ts.getOffset());
        }
    }

    void addError(String messageId) {
        this.addError(messageId, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }

    void addError(String messageId, int position, int length) {
        this.addError(messageId, null, position, length);
    }

    void addError(String messageId, String messageArg) {
        this.addError(messageId, messageArg, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }

    void addError(String messageId, int c) {
        String messageArg = Character.toString((char) c);
        this.addError(messageId, messageArg, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }

    void addError(String messageId, String messageArg, int position, int length) {
        this.syntaxErrorCount++;
        String message = this.lookupMessage(messageId, messageArg);
        if (this.errorCollector != null) {
            this.errorCollector.error(message, this.sourceURI, position, length);
        } else {
            int lineno = 1;
            int offset = 1;
            String line = "";
            if (this.ts != null) {
                lineno = this.ts.getLineno();
                line = this.ts.getLine();
                offset = this.ts.getOffset();
            }
            this.errorReporter.error(this.cx, message, this.sourceURI, lineno, line, offset);
        }
    }

    private void addStrictWarning(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
        if (this.compilerEnv.isStrictMode()) {
            this.addWarning(messageId, messageArg, position, length, line, lineSource, lineOffset);
        }
    }

    private void addWarning(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
        String message = this.lookupMessage(messageId, messageArg);
        if (this.errorCollector != null) {
            this.errorCollector.warning(message, this.sourceURI, position, length);
        } else {
            this.errorReporter.warning(message, this.sourceURI, line, lineSource, lineOffset);
        }
    }

    private void addError(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
        this.syntaxErrorCount++;
        String message = this.lookupMessage(messageId, messageArg);
        if (this.errorCollector != null) {
            this.errorCollector.error(message, this.sourceURI, position, length);
        } else {
            this.errorReporter.error(this.cx, message, this.sourceURI, line, lineSource, lineOffset);
        }
    }

    String lookupMessage(String messageId) {
        return this.lookupMessage(messageId, null);
    }

    String lookupMessage(String messageId, String messageArg) {
        return messageArg == null ? ScriptRuntime.getMessage0(messageId) : ScriptRuntime.getMessage1(messageId, messageArg);
    }

    void reportError(String messageId) {
        this.reportError(messageId, null);
    }

    void reportError(String messageId, String messageArg) {
        if (this.ts == null) {
            this.reportError(messageId, messageArg, 1, 1);
        } else {
            this.reportError(messageId, messageArg, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
        }
    }

    void reportError(String messageId, int position, int length) {
        this.reportError(messageId, null, position, length);
    }

    void reportError(String messageId, String messageArg, int position, int length) {
        this.addError(messageId, messageArg, position, length);
        throw new Parser.ParserException();
    }

    private void recordComment(int lineno, String comment) {
        if (this.scannedComments == null) {
            this.scannedComments = new ArrayList();
        }
        Comment commentNode = new Comment(this.ts.tokenBeg, this.ts.getTokenLength(), this.ts.commentType, comment);
        commentNode.setLineno(lineno);
        this.scannedComments.add(commentNode);
    }

    private Comment getAndResetJsDoc() {
        Comment saved = this.currentJsDocComment;
        this.currentJsDocComment = null;
        return saved;
    }

    private int peekToken() throws IOException {
        if (this.currentFlaggedToken != 0) {
            return this.currentToken;
        } else {
            int lineno = this.ts.getLineno();
            int tt = this.ts.getToken();
            boolean sawEOL = false;
            while (tt == 1 || tt == 162) {
                if (tt == 1) {
                    lineno++;
                    sawEOL = true;
                    tt = this.ts.getToken();
                } else {
                    tt = this.ts.getToken();
                }
            }
            this.currentToken = tt;
            this.currentFlaggedToken = tt | (sawEOL ? 65536 : 0);
            return this.currentToken;
        }
    }

    private int peekFlaggedToken() throws IOException {
        this.peekToken();
        return this.currentFlaggedToken;
    }

    private void consumeToken() {
        this.currentFlaggedToken = 0;
    }

    private int nextToken() throws IOException {
        int tt = this.peekToken();
        this.consumeToken();
        return tt;
    }

    private boolean matchToken(int toMatch, boolean ignoreComment) throws IOException {
        int tt;
        for (tt = this.peekToken(); tt == 162 && ignoreComment; tt = this.peekToken()) {
            this.consumeToken();
        }
        if (tt != toMatch) {
            return false;
        } else {
            this.consumeToken();
            return true;
        }
    }

    private int peekTokenOrEOL() throws IOException {
        int tt = this.peekToken();
        if ((this.currentFlaggedToken & 65536) != 0) {
            tt = 1;
        }
        return tt;
    }

    private boolean mustMatchToken(int toMatch, String messageId, boolean ignoreComment) throws IOException {
        return this.mustMatchToken(toMatch, messageId, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg, ignoreComment);
    }

    private boolean mustMatchToken(int toMatch, String msgId, int pos, int len, boolean ignoreComment) throws IOException {
        if (this.matchToken(toMatch, ignoreComment)) {
            return true;
        } else {
            this.reportError(msgId, pos, len);
            return false;
        }
    }

    public boolean eof() {
        return this.ts.eof();
    }

    boolean insideFunction() {
        return this.nestingOfFunction != 0;
    }

    void pushScope(Scope scope) {
        Scope parent = scope.getParentScope();
        if (parent != null) {
            if (parent != this.currentScope) {
                this.codeBug();
            }
        } else {
            this.currentScope.addChildScope(scope);
        }
        this.currentScope = scope;
    }

    void popScope() {
        this.currentScope = this.currentScope.getParentScope();
    }

    private void enterLoop(Loop loop) {
        if (this.loopSet == null) {
            this.loopSet = new ArrayList();
        }
        this.loopSet.add(loop);
        if (this.loopAndSwitchSet == null) {
            this.loopAndSwitchSet = new ArrayList();
        }
        this.loopAndSwitchSet.add(loop);
        this.pushScope(loop);
        if (this.currentLabel != null) {
            this.currentLabel.setStatement(loop);
            this.currentLabel.getFirstLabel().setLoop(loop);
            loop.setRelative(-this.currentLabel.getPosition());
        }
    }

    private void exitLoop() {
        Loop loop = (Loop) this.loopSet.remove(this.loopSet.size() - 1);
        this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
        if (loop.getParent() != null) {
            loop.setRelative(loop.getParent().getPosition());
        }
        this.popScope();
    }

    private void enterSwitch(SwitchStatement node) {
        if (this.loopAndSwitchSet == null) {
            this.loopAndSwitchSet = new ArrayList();
        }
        this.loopAndSwitchSet.add(node);
    }

    private void exitSwitch() {
        this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
    }

    public AstRoot parse(String sourceString, String sourceURI, int lineno) {
        if (this.parseFinished) {
            throw new IllegalStateException("parser reused");
        } else {
            this.sourceURI = sourceURI;
            this.ts = new TokenStream(this, sourceString, lineno);
            AstRoot iox;
            try {
                iox = this.parse();
            } catch (IOException var8) {
                throw new IllegalStateException();
            } finally {
                this.parseFinished = true;
            }
            return iox;
        }
    }

    private AstRoot parse() throws IOException {
        int pos = 0;
        AstRoot root = new AstRoot(pos);
        this.currentScope = this.currentScriptOrFn = root;
        int baseLineno = this.ts.lineno;
        int end = pos;
        boolean inDirectivePrologue = true;
        boolean savedStrictMode = this.inUseStrictDirective;
        this.inUseStrictDirective = this.defaultUseStrictDirective;
        if (this.inUseStrictDirective) {
            root.setInStrictMode(true);
        }
        try {
            while (true) {
                int tt = this.peekToken();
                if (tt <= 0) {
                    break;
                }
                AstNode n;
                if (tt == 110) {
                    this.consumeToken();
                    try {
                        n = this.function(this.calledByCompileFunction ? 2 : 1);
                    } catch (Parser.ParserException var14) {
                        break;
                    }
                } else if (tt == 162) {
                    n = (AstNode) this.scannedComments.get(this.scannedComments.size() - 1);
                    this.consumeToken();
                } else {
                    n = this.statement();
                    if (inDirectivePrologue) {
                        String directive = getDirective(n);
                        if (directive == null) {
                            inDirectivePrologue = false;
                        } else if (directive.equals("use strict")) {
                            this.inUseStrictDirective = true;
                            root.setInStrictMode(true);
                        }
                    }
                }
                end = getNodeEnd(n);
                root.addChildToBack(n);
                n.setParent(root);
            }
        } catch (StackOverflowError var15) {
            String msg = this.lookupMessage("msg.too.deep.parser.recursion");
            throw Context.reportRuntimeError(this.cx, msg, this.sourceURI, this.ts.lineno, null, 0);
        } finally {
            this.inUseStrictDirective = savedStrictMode;
        }
        if (this.syntaxErrorCount != 0) {
            String msg = String.valueOf(this.syntaxErrorCount);
            msg = this.lookupMessage("msg.got.syntax.errors", msg);
            throw this.errorReporter.runtimeError(this.cx, msg, this.sourceURI, baseLineno, null, 0);
        } else {
            if (this.scannedComments != null) {
                int last = this.scannedComments.size() - 1;
                end = Math.max(end, getNodeEnd((AstNode) this.scannedComments.get(last)));
                for (Comment c : this.scannedComments) {
                    root.addComment(c);
                }
            }
            root.setLength(end - pos);
            root.setSourceName(this.sourceURI);
            root.setBaseLineno(baseLineno);
            root.setEndLineno(this.ts.lineno);
            return root;
        }
    }

    private AstNode parseFunctionBody(int type, FunctionNode fnNode) throws IOException {
        boolean isExpressionClosure = !this.matchToken(86, true);
        boolean isArrow = type == 4;
        this.nestingOfFunction++;
        int pos = this.ts.tokenBeg;
        Block pn = new Block(pos);
        boolean inDirectivePrologue = true;
        boolean savedStrictMode = this.inUseStrictDirective;
        this.inUseStrictDirective = false;
        pn.setLineno(this.ts.lineno);
        try {
            if (isExpressionClosure) {
                AstNode returnValue = this.assignExpr();
                ReturnStatement n = new ReturnStatement(returnValue.getPosition(), returnValue.getLength(), returnValue);
                n.putProp(25, Boolean.TRUE);
                pn.putProp(25, Boolean.TRUE);
                if (isArrow) {
                    n.putProp(27, Boolean.TRUE);
                }
                pn.addStatement(n);
            } else {
                label89: while (true) {
                    int tt = this.peekToken();
                    AstNode n;
                    switch(tt) {
                        case -1:
                        case 0:
                        case 87:
                            break label89;
                        case 110:
                            this.consumeToken();
                            n = this.function(1);
                            break;
                        case 162:
                            this.consumeToken();
                            n = (AstNode) this.scannedComments.get(this.scannedComments.size() - 1);
                            break;
                        default:
                            n = this.statement();
                            if (inDirectivePrologue) {
                                String directive = getDirective(n);
                                if (directive == null) {
                                    inDirectivePrologue = false;
                                } else if (directive.equals("use strict")) {
                                    this.inUseStrictDirective = true;
                                    fnNode.setInStrictMode(true);
                                    if (!savedStrictMode) {
                                        this.setRequiresActivation();
                                    }
                                }
                            }
                    }
                    pn.addStatement(n);
                }
            }
        } catch (Parser.ParserException var15) {
        } finally {
            this.nestingOfFunction--;
            this.inUseStrictDirective = savedStrictMode;
        }
        int end = this.ts.tokenEnd;
        this.getAndResetJsDoc();
        if (!isExpressionClosure && this.mustMatchToken(87, "msg.no.brace.after.body", true)) {
            end = this.ts.tokenEnd;
        }
        pn.setLength(end - pos);
        return pn;
    }

    private void parseFunctionParams(FunctionNode fnNode) throws IOException {
        if (this.matchToken(89, true)) {
            fnNode.setRp(this.ts.tokenBeg - fnNode.getPosition());
        } else {
            Map<String, Node> destructuring = null;
            Set<String> paramNames = new HashSet();
            do {
                int tt = this.peekToken();
                if (tt == 84 || tt == 86) {
                    AstNode expr = this.destructuringPrimaryExpr();
                    this.markDestructuring(expr);
                    fnNode.addParam(expr);
                    if (destructuring == null) {
                        destructuring = new HashMap();
                    }
                    String pname = this.currentScriptOrFn.getNextTempName();
                    this.defineSymbol(88, pname);
                    destructuring.put(pname, expr);
                } else if (this.mustMatchToken(39, "msg.no.parm", true)) {
                    Name paramNameNode = this.createNameNode();
                    Comment jsdocNodeForName = this.getAndResetJsDoc();
                    if (jsdocNodeForName != null) {
                        paramNameNode.setJsDocNode(jsdocNodeForName);
                    }
                    fnNode.addParam(paramNameNode);
                    String paramName = this.ts.getString();
                    this.defineSymbol(88, paramName);
                    if (this.inUseStrictDirective) {
                        if ("eval".equals(paramName) || "arguments".equals(paramName)) {
                            this.reportError("msg.bad.id.strict", paramName);
                        }
                        if (paramNames.contains(paramName)) {
                            this.addError("msg.dup.param.strict", paramName);
                        }
                        paramNames.add(paramName);
                    }
                } else {
                    fnNode.addParam(this.makeErrorNode());
                }
            } while (this.matchToken(90, true));
            if (destructuring != null) {
                Node destructuringNode = new Node(90);
                for (Entry<String, Node> param : destructuring.entrySet()) {
                    Node assign = this.createDestructuringAssignment(123, (Node) param.getValue(), this.createName((String) param.getKey()));
                    destructuringNode.addChildToBack(assign);
                }
                fnNode.putProp(23, destructuringNode);
            }
            if (this.mustMatchToken(89, "msg.no.paren.after.parms", true)) {
                fnNode.setRp(this.ts.tokenBeg - fnNode.getPosition());
            }
        }
    }

    private FunctionNode function(int type) throws IOException {
        return this.function(type, false);
    }

    private FunctionNode function(int type, boolean isGenerator) throws IOException {
        int syntheticType = type;
        int baseLineno = this.ts.lineno;
        int functionSourceStart = this.ts.tokenBeg;
        Name name = null;
        AstNode memberExprNode = null;
        if (this.matchToken(39, true)) {
            name = this.createNameNode(true, 39);
            if (this.inUseStrictDirective) {
                String id = name.getIdentifier();
                if ("eval".equals(id) || "arguments".equals(id)) {
                    this.reportError("msg.bad.id.strict", id);
                }
            }
            if (!this.matchToken(88, true)) {
                this.mustMatchToken(88, "msg.no.paren.parms", true);
            }
        } else if (!this.matchToken(88, true)) {
            if (this.matchToken(23, true)) {
                return this.function(type, true);
            }
            this.mustMatchToken(88, "msg.no.paren.parms", true);
        }
        int lpPos = this.currentToken == 88 ? this.ts.tokenBeg : -1;
        if (memberExprNode != null) {
            syntheticType = 2;
        }
        if (syntheticType != 2 && name != null && name.length() > 0) {
            this.defineSymbol(110, name.getIdentifier());
        }
        FunctionNode fnNode = new FunctionNode(functionSourceStart, name);
        fnNode.setFunctionType(type);
        if (isGenerator) {
            fnNode.setIsES6Generator();
        }
        if (lpPos != -1) {
            fnNode.setLp(lpPos - functionSourceStart);
        }
        fnNode.setJsDocNode(this.getAndResetJsDoc());
        Parser.PerFunctionVariables savedVars = new Parser.PerFunctionVariables(fnNode);
        try {
            this.parseFunctionParams(fnNode);
            fnNode.setBody(this.parseFunctionBody(type, fnNode));
            fnNode.setLength(this.ts.tokenEnd - functionSourceStart);
            if (this.compilerEnv.isStrictMode() && !fnNode.getBody().hasConsistentReturnUsage()) {
                String msg = name != null && name.length() > 0 ? "msg.no.return.value" : "msg.anon.no.return.value";
                this.addStrictWarning(msg, name == null ? "" : name.getIdentifier());
            }
        } finally {
            savedVars.restore();
        }
        if (memberExprNode != null) {
            Kit.codeBug();
            fnNode.setMemberExprNode(memberExprNode);
        }
        fnNode.setSourceName(this.sourceURI);
        fnNode.setBaseLineno(baseLineno);
        fnNode.setEndLineno(this.ts.lineno);
        return fnNode;
    }

    private AstNode arrowFunction(AstNode params) throws IOException {
        int baseLineno = this.ts.lineno;
        int functionSourceStart = params != null ? params.getPosition() : -1;
        FunctionNode fnNode = new FunctionNode(functionSourceStart);
        fnNode.setFunctionType(4);
        fnNode.setJsDocNode(this.getAndResetJsDoc());
        Map<String, Node> destructuring = new HashMap();
        Set<String> paramNames = new HashSet();
        Parser.PerFunctionVariables savedVars = new Parser.PerFunctionVariables(fnNode);
        try {
            if (params instanceof ParenthesizedExpression) {
                fnNode.setParens(0, params.getLength());
                AstNode p = ((ParenthesizedExpression) params).getExpression();
                if (!(p instanceof EmptyExpression)) {
                    this.arrowFunctionParams(fnNode, p, destructuring, paramNames);
                }
            } else {
                this.arrowFunctionParams(fnNode, params, destructuring, paramNames);
            }
            if (!destructuring.isEmpty()) {
                Node destructuringNode = new Node(90);
                for (Entry<String, Node> param : destructuring.entrySet()) {
                    Node assign = this.createDestructuringAssignment(123, (Node) param.getValue(), this.createName((String) param.getKey()));
                    destructuringNode.addChildToBack(assign);
                }
                fnNode.putProp(23, destructuringNode);
            }
            fnNode.setBody(this.parseFunctionBody(4, fnNode));
            fnNode.setLength(this.ts.tokenEnd - functionSourceStart);
        } finally {
            savedVars.restore();
        }
        if (fnNode.isGenerator()) {
            this.reportError("msg.arrowfunction.generator");
            return this.makeErrorNode();
        } else {
            fnNode.setSourceName(this.sourceURI);
            fnNode.setBaseLineno(baseLineno);
            fnNode.setEndLineno(this.ts.lineno);
            return fnNode;
        }
    }

    private void arrowFunctionParams(FunctionNode fnNode, AstNode params, Map<String, Node> destructuring, Set<String> paramNames) {
        if (!(params instanceof ArrayLiteral) && !(params instanceof ObjectLiteral)) {
            if (params instanceof InfixExpression && params.getType() == 90) {
                this.arrowFunctionParams(fnNode, ((InfixExpression) params).getLeft(), destructuring, paramNames);
                this.arrowFunctionParams(fnNode, ((InfixExpression) params).getRight(), destructuring, paramNames);
            } else if (params instanceof Name) {
                fnNode.addParam(params);
                String paramName = ((Name) params).getIdentifier();
                this.defineSymbol(88, paramName);
                if (this.inUseStrictDirective) {
                    if ("eval".equals(paramName) || "arguments".equals(paramName)) {
                        this.reportError("msg.bad.id.strict", paramName);
                    }
                    if (paramNames.contains(paramName)) {
                        this.addError("msg.dup.param.strict", paramName);
                    }
                    paramNames.add(paramName);
                }
            } else {
                this.reportError("msg.no.parm", params.getPosition(), params.getLength());
                fnNode.addParam(this.makeErrorNode());
            }
        } else {
            this.markDestructuring(params);
            fnNode.addParam(params);
            String pname = this.currentScriptOrFn.getNextTempName();
            this.defineSymbol(88, pname);
            destructuring.put(pname, params);
        }
    }

    private AstNode statements(AstNode parent) throws IOException {
        if (this.currentToken != 86) {
            this.codeBug();
        }
        int pos = this.ts.tokenBeg;
        AstNode block = (AstNode) (parent != null ? parent : new Block(pos));
        block.setLineno(this.ts.lineno);
        int tt;
        while ((tt = this.peekToken()) > 0 && tt != 87) {
            block.addChild(this.statement());
        }
        block.setLength(this.ts.tokenBeg - pos);
        return block;
    }

    private AstNode statements() throws IOException {
        return this.statements(null);
    }

    private Parser.ConditionData condition() throws IOException {
        Parser.ConditionData data = new Parser.ConditionData();
        if (this.mustMatchToken(88, "msg.no.paren.cond", true)) {
            data.lp = this.ts.tokenBeg;
        }
        data.condition = this.expr();
        if (this.mustMatchToken(89, "msg.no.paren.after.cond", true)) {
            data.rp = this.ts.tokenBeg;
        }
        if (data.condition instanceof Assignment) {
            this.addStrictWarning("msg.equal.as.assign", "", data.condition.getPosition(), data.condition.getLength());
        }
        return data;
    }

    private AstNode statement() throws IOException {
        int pos = this.ts.tokenBeg;
        try {
            AstNode pn = this.statementHelper();
            if (pn != null) {
                if (this.compilerEnv.isStrictMode() && !pn.hasSideEffects()) {
                    int beg = pn.getPosition();
                    this.addStrictWarning(pn instanceof EmptyStatement ? "msg.extra.trailing.semi" : "msg.no.side.effects", "", beg, nodeEnd(pn) - beg);
                }
                int ntt = this.peekToken();
                if (ntt == 162 && pn.getLineno() == ((Comment) this.scannedComments.get(this.scannedComments.size() - 1)).getLineno()) {
                    pn.setInlineComment((AstNode) this.scannedComments.get(this.scannedComments.size() - 1));
                    this.consumeToken();
                }
                return pn;
            }
        } catch (Parser.ParserException var4) {
        }
        while (true) {
            int tt = this.peekTokenOrEOL();
            this.consumeToken();
            switch(tt) {
                case -1:
                case 0:
                case 1:
                case 83:
                    return new EmptyStatement(pos, this.ts.tokenBeg - pos);
            }
        }
    }

    private AstNode statementHelper() throws IOException {
        if (this.currentLabel != null && this.currentLabel.getStatement() != null) {
            this.currentLabel = null;
        }
        AstNode pn = null;
        int tt = this.peekToken();
        int pos = this.ts.tokenBeg;
        switch(tt) {
            case -1:
                this.consumeToken();
                return this.makeErrorNode();
            case 4:
            case 73:
                pn = this.returnOrYield(tt, false);
                break;
            case 39:
                pn = this.nameOrLabel();
                if (!(pn instanceof ExpressionStatement)) {
                    return pn;
                }
                break;
            case 50:
                pn = this.throwStatement();
                break;
            case 82:
                return this.tryStatement();
            case 83:
                this.consumeToken();
                pos = this.ts.tokenBeg;
                AstNode var6 = new EmptyStatement(pos, this.ts.tokenEnd - pos);
                var6.setLineno(this.ts.lineno);
                return var6;
            case 86:
                return this.block();
            case 110:
                this.consumeToken();
                return this.function(3);
            case 113:
                return this.ifStatement();
            case 115:
                return this.switchStatement();
            case 118:
                return this.whileLoop();
            case 119:
                return this.doLoop();
            case 120:
                return this.forLoop();
            case 121:
                pn = this.breakStatement();
                break;
            case 122:
                pn = this.continueStatement();
                break;
            case 123:
            case 155:
                {
                    this.consumeToken();
                    int lineno = this.ts.lineno;
                    pn = this.variables(this.currentToken, this.ts.tokenBeg, true);
                    pn.setLineno(lineno);
                    break;
                }
            case 124:
                if (this.inUseStrictDirective) {
                    this.reportError("msg.no.with.strict");
                }
                return this.withStatement();
            case 154:
                pn = this.letStatement();
                if (!(pn instanceof VariableDeclaration) || this.peekToken() != 83) {
                    return pn;
                }
                break;
            case 162:
                return (AstNode) this.scannedComments.get(this.scannedComments.size() - 1);
            default:
                {
                    int lineno = this.ts.lineno;
                    pn = new ExpressionStatement(this.expr(), !this.insideFunction());
                    pn.setLineno(lineno);
                }
        }
        this.autoInsertSemicolon(pn);
        return pn;
    }

    private void autoInsertSemicolon(AstNode pn) throws IOException {
        int ttFlagged = this.peekFlaggedToken();
        int pos = pn.getPosition();
        switch(ttFlagged & 65535) {
            case -1:
            case 0:
            case 87:
                this.warnMissingSemi(pos, Math.max(pos + 1, nodeEnd(pn)));
                break;
            case 83:
                this.consumeToken();
                pn.setLength(this.ts.tokenEnd - pos);
                break;
            default:
                if ((ttFlagged & 65536) == 0) {
                    this.reportError("msg.no.semi.stmt");
                } else {
                    this.warnMissingSemi(pos, nodeEnd(pn));
                }
        }
    }

    private IfStatement ifStatement() throws IOException {
        if (this.currentToken != 113) {
            this.codeBug();
        }
        this.consumeToken();
        int pos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        int elsePos = -1;
        IfStatement pn = new IfStatement(pos);
        Parser.ConditionData data = this.condition();
        AstNode ifTrue = this.getNextStatementAfterInlineComments(pn);
        AstNode ifFalse = null;
        if (this.matchToken(114, true)) {
            int tt = this.peekToken();
            if (tt == 162) {
                pn.setElseKeyWordInlineComment((AstNode) this.scannedComments.get(this.scannedComments.size() - 1));
                this.consumeToken();
            }
            elsePos = this.ts.tokenBeg - pos;
            ifFalse = this.statement();
        }
        int end = getNodeEnd(ifFalse != null ? ifFalse : ifTrue);
        pn.setLength(end - pos);
        pn.setCondition(data.condition);
        pn.setParens(data.lp - pos, data.rp - pos);
        pn.setThenPart(ifTrue);
        pn.setElsePart(ifFalse);
        pn.setElsePosition(elsePos);
        pn.setLineno(lineno);
        return pn;
    }

    private SwitchStatement switchStatement() throws IOException {
        if (this.currentToken != 115) {
            this.codeBug();
        }
        this.consumeToken();
        int pos = this.ts.tokenBeg;
        SwitchStatement pn = new SwitchStatement(pos);
        if (this.mustMatchToken(88, "msg.no.paren.switch", true)) {
            pn.setLp(this.ts.tokenBeg - pos);
        }
        pn.setLineno(this.ts.lineno);
        AstNode discriminant = this.expr();
        pn.setExpression(discriminant);
        this.enterSwitch(pn);
        try {
            if (this.mustMatchToken(89, "msg.no.paren.after.switch", true)) {
                pn.setRp(this.ts.tokenBeg - pos);
            }
            this.mustMatchToken(86, "msg.no.brace.switch", true);
            boolean hasDefault = false;
            while (true) {
                int tt = this.nextToken();
                int casePos = this.ts.tokenBeg;
                int caseLineno = this.ts.lineno;
                AstNode caseExpression = null;
                switch(tt) {
                    case 87:
                        pn.setLength(this.ts.tokenEnd - pos);
                        return pn;
                    case 116:
                        caseExpression = this.expr();
                        this.mustMatchToken(104, "msg.no.colon.case", true);
                        break;
                    case 117:
                        if (hasDefault) {
                            this.reportError("msg.double.switch.default");
                        }
                        hasDefault = true;
                        this.mustMatchToken(104, "msg.no.colon.case", true);
                        break;
                    case 162:
                        AstNode n = (AstNode) this.scannedComments.get(this.scannedComments.size() - 1);
                        pn.addChild(n);
                        continue;
                    default:
                        this.reportError("msg.bad.switch");
                        return pn;
                }
                SwitchCase caseNode = new SwitchCase(casePos);
                caseNode.setExpression(caseExpression);
                caseNode.setLength(this.ts.tokenEnd - pos);
                caseNode.setLineno(caseLineno);
                while ((tt = this.peekToken()) != 87 && tt != 116 && tt != 117 && tt != false) {
                    if (tt != 162) {
                        AstNode nextStmt = this.statement();
                        caseNode.addStatement(nextStmt);
                    } else {
                        Comment inlineComment = (Comment) this.scannedComments.get(this.scannedComments.size() - 1);
                        if (caseNode.getInlineComment() == null && inlineComment.getLineno() == caseNode.getLineno()) {
                            caseNode.setInlineComment(inlineComment);
                        } else {
                            caseNode.addStatement(inlineComment);
                        }
                        this.consumeToken();
                    }
                }
                pn.addCase(caseNode);
            }
        } finally {
            this.exitSwitch();
        }
    }

    private WhileLoop whileLoop() throws IOException {
        if (this.currentToken != 118) {
            this.codeBug();
        }
        this.consumeToken();
        int pos = this.ts.tokenBeg;
        WhileLoop pn = new WhileLoop(pos);
        pn.setLineno(this.ts.lineno);
        this.enterLoop(pn);
        try {
            Parser.ConditionData data = this.condition();
            pn.setCondition(data.condition);
            pn.setParens(data.lp - pos, data.rp - pos);
            AstNode body = this.getNextStatementAfterInlineComments(pn);
            pn.setLength(getNodeEnd(body) - pos);
            pn.setBody(body);
        } finally {
            this.exitLoop();
        }
        return pn;
    }

    private DoLoop doLoop() throws IOException {
        if (this.currentToken != 119) {
            this.codeBug();
        }
        this.consumeToken();
        int pos = this.ts.tokenBeg;
        DoLoop pn = new DoLoop(pos);
        pn.setLineno(this.ts.lineno);
        this.enterLoop(pn);
        int end;
        try {
            AstNode body = this.getNextStatementAfterInlineComments(pn);
            this.mustMatchToken(118, "msg.no.while.do", true);
            pn.setWhilePosition(this.ts.tokenBeg - pos);
            Parser.ConditionData data = this.condition();
            pn.setCondition(data.condition);
            pn.setParens(data.lp - pos, data.rp - pos);
            end = getNodeEnd(body);
            pn.setBody(body);
        } finally {
            this.exitLoop();
        }
        if (this.matchToken(83, true)) {
            end = this.ts.tokenEnd;
        }
        pn.setLength(end - pos);
        return pn;
    }

    private int peekUntilNonComment(int tt) throws IOException {
        while (tt == 162) {
            this.consumeToken();
            tt = this.peekToken();
        }
        return tt;
    }

    private AstNode getNextStatementAfterInlineComments(AstNode pn) throws IOException {
        AstNode body = this.statement();
        if (162 == body.getType()) {
            AstNode commentNode = body;
            body = this.statement();
            if (pn != null) {
                pn.setInlineComment(commentNode);
            } else {
                body.setInlineComment(commentNode);
            }
        }
        return body;
    }

    private Loop forLoop() throws IOException {
        if (this.currentToken != 120) {
            this.codeBug();
        }
        this.consumeToken();
        int forPos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        boolean isForEach = false;
        boolean isForIn = false;
        boolean isForOf = false;
        int eachPos = -1;
        int inPos = -1;
        int lp = -1;
        int rp = -1;
        AstNode incr = null;
        Scope tempScope = new Scope();
        this.pushScope(tempScope);
        Loop pn;
        try {
            if (this.matchToken(39, true)) {
                if ("each".equals(this.ts.getString())) {
                    isForEach = true;
                    eachPos = this.ts.tokenBeg - forPos;
                } else {
                    this.reportError("msg.no.paren.for");
                }
            }
            if (this.mustMatchToken(88, "msg.no.paren.for", true)) {
                lp = this.ts.tokenBeg - forPos;
            }
            int tt = this.peekToken();
            AstNode init = this.forLoopInit(tt);
            AstNode cond;
            if (this.matchToken(52, true)) {
                isForIn = true;
                inPos = this.ts.tokenBeg - forPos;
                cond = this.expr();
            } else if (this.matchToken(39, true) && "of".equals(this.ts.getString())) {
                isForOf = true;
                inPos = this.ts.tokenBeg - forPos;
                cond = this.expr();
            } else {
                this.mustMatchToken(83, "msg.no.semi.for", true);
                if (this.peekToken() == 83) {
                    cond = new EmptyExpression(this.ts.tokenBeg, 1);
                    cond.setLineno(this.ts.lineno);
                } else {
                    cond = this.expr();
                }
                this.mustMatchToken(83, "msg.no.semi.for.cond", true);
                int tmpPos = this.ts.tokenEnd;
                if (this.peekToken() == 89) {
                    incr = new EmptyExpression(tmpPos, 1);
                    incr.setLineno(this.ts.lineno);
                } else {
                    incr = this.expr();
                }
            }
            if (this.mustMatchToken(89, "msg.no.paren.for.ctrl", true)) {
                rp = this.ts.tokenBeg - forPos;
            }
            if (!isForIn && !isForOf) {
                ForLoop fl = new ForLoop(forPos);
                fl.setInitializer(init);
                fl.setCondition(cond);
                fl.setIncrement(incr);
                pn = fl;
            } else {
                ForInLoop fis = new ForInLoop(forPos);
                if (init instanceof VariableDeclaration && ((VariableDeclaration) init).getVariables().size() > 1) {
                    this.reportError("msg.mult.index");
                }
                if (isForOf && isForEach) {
                    this.reportError("msg.invalid.for.each");
                }
                fis.setIterator(init);
                fis.setIteratedObject(cond);
                fis.setInPosition(inPos);
                fis.setIsForEach(isForEach);
                fis.setEachPosition(eachPos);
                fis.setIsForOf(isForOf);
                pn = fis;
            }
            this.currentScope.replaceWith(pn);
            this.popScope();
            this.enterLoop(pn);
            try {
                AstNode body = this.getNextStatementAfterInlineComments(pn);
                pn.setLength(getNodeEnd(body) - forPos);
                pn.setBody(body);
            } finally {
                this.exitLoop();
            }
        } finally {
            if (this.currentScope == tempScope) {
                this.popScope();
            }
        }
        pn.setParens(lp, rp);
        pn.setLineno(lineno);
        return pn;
    }

    private AstNode forLoopInit(int tt) throws IOException {
        try {
            this.inForInit = true;
            AstNode init;
            if (tt == 83) {
                init = new EmptyExpression(this.ts.tokenBeg, 1);
                init.setLineno(this.ts.lineno);
            } else if (tt != 123 && tt != 154 && tt != 155) {
                init = this.expr();
                if (init instanceof Name) {
                    return this.nameToVariableDeclaration((Name) init, this.ts.tokenBeg, false);
                }
                this.markDestructuring(init);
            } else {
                this.consumeToken();
                init = this.variables(154, this.ts.tokenBeg, false);
            }
            return init;
        } finally {
            this.inForInit = false;
        }
    }

    private AstNode nameToVariableDeclaration(Name name, int pos, boolean isStatement) throws IOException {
        this.markDestructuring(name);
        return name;
    }

    private TryStatement tryStatement() throws IOException {
        if (this.currentToken != 82) {
            this.codeBug();
        }
        this.consumeToken();
        Comment jsdocNode = this.getAndResetJsDoc();
        int tryPos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        int finallyPos = -1;
        TryStatement pn = new TryStatement(tryPos);
        int lctt = this.peekToken();
        if (lctt == 162) {
            Comment commentNode = (Comment) this.scannedComments.get(this.scannedComments.size() - 1);
            pn.setInlineComment(commentNode);
            this.consumeToken();
            lctt = this.peekToken();
        }
        if (lctt != 86) {
            this.reportError("msg.no.brace.try");
        }
        AstNode tryBlock = this.getNextStatementAfterInlineComments(pn);
        int tryEnd = getNodeEnd(tryBlock);
        List<CatchClause> clauses = null;
        boolean sawDefaultCatch = false;
        int peek = this.peekToken();
        if (peek == 125) {
            while (this.matchToken(125, true)) {
                int catchLineNum = this.ts.lineno;
                if (sawDefaultCatch) {
                    this.reportError("msg.catch.unreachable");
                }
                int catchPos = this.ts.tokenBeg;
                int lp = -1;
                int rp = -1;
                int guardPos = -1;
                if (this.mustMatchToken(88, "msg.no.paren.catch", true)) {
                    lp = this.ts.tokenBeg;
                }
                this.mustMatchToken(39, "msg.bad.catchcond", true);
                Name varName = this.createNameNode();
                Comment jsdocNodeForName = this.getAndResetJsDoc();
                if (jsdocNodeForName != null) {
                    varName.setJsDocNode(jsdocNodeForName);
                }
                String varNameString = varName.getIdentifier();
                if (this.inUseStrictDirective && ("eval".equals(varNameString) || "arguments".equals(varNameString))) {
                    this.reportError("msg.bad.id.strict", varNameString);
                }
                AstNode catchCond = null;
                if (this.matchToken(113, true)) {
                    guardPos = this.ts.tokenBeg;
                    catchCond = this.expr();
                } else {
                    sawDefaultCatch = true;
                }
                if (this.mustMatchToken(89, "msg.bad.catchcond", true)) {
                    rp = this.ts.tokenBeg;
                }
                this.mustMatchToken(86, "msg.no.brace.catchblock", true);
                Block catchBlock = (Block) this.statements();
                tryEnd = getNodeEnd(catchBlock);
                CatchClause catchNode = new CatchClause(catchPos);
                catchNode.setVarName(varName);
                catchNode.setCatchCondition(catchCond);
                catchNode.setBody(catchBlock);
                if (guardPos != -1) {
                    catchNode.setIfPosition(guardPos - catchPos);
                }
                catchNode.setParens(lp, rp);
                catchNode.setLineno(catchLineNum);
                if (this.mustMatchToken(87, "msg.no.brace.after.body", true)) {
                    tryEnd = this.ts.tokenEnd;
                }
                catchNode.setLength(tryEnd - catchPos);
                if (clauses == null) {
                    clauses = new ArrayList();
                }
                clauses.add(catchNode);
            }
        } else if (peek != 126) {
            this.mustMatchToken(126, "msg.try.no.catchfinally", true);
        }
        AstNode finallyBlock = null;
        if (this.matchToken(126, true)) {
            finallyPos = this.ts.tokenBeg;
            finallyBlock = this.statement();
            tryEnd = getNodeEnd(finallyBlock);
        }
        pn.setLength(tryEnd - tryPos);
        pn.setTryBlock(tryBlock);
        pn.setCatchClauses(clauses);
        pn.setFinallyBlock(finallyBlock);
        if (finallyPos != -1) {
            pn.setFinallyPosition(finallyPos - tryPos);
        }
        pn.setLineno(lineno);
        if (jsdocNode != null) {
            pn.setJsDocNode(jsdocNode);
        }
        return pn;
    }

    private ThrowStatement throwStatement() throws IOException {
        if (this.currentToken != 50) {
            this.codeBug();
        }
        this.consumeToken();
        int pos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        if (this.peekTokenOrEOL() == 1) {
            this.reportError("msg.bad.throw.eol");
        }
        AstNode expr = this.expr();
        ThrowStatement pn = new ThrowStatement(pos, expr);
        pn.setLineno(lineno);
        return pn;
    }

    private LabeledStatement matchJumpLabelName() throws IOException {
        LabeledStatement label = null;
        if (this.peekTokenOrEOL() == 39) {
            this.consumeToken();
            if (this.labelSet != null) {
                label = (LabeledStatement) this.labelSet.get(this.ts.getString());
            }
            if (label == null) {
                this.reportError("msg.undef.label");
            }
        }
        return label;
    }

    private BreakStatement breakStatement() throws IOException {
        if (this.currentToken != 121) {
            this.codeBug();
        }
        this.consumeToken();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        Name breakLabel = null;
        if (this.peekTokenOrEOL() == 39) {
            breakLabel = this.createNameNode();
            end = getNodeEnd(breakLabel);
        }
        LabeledStatement labels = this.matchJumpLabelName();
        Jump breakTarget = labels == null ? null : labels.getFirstLabel();
        if (breakTarget == null && breakLabel == null) {
            if (this.loopAndSwitchSet != null && this.loopAndSwitchSet.size() != 0) {
                breakTarget = (Jump) this.loopAndSwitchSet.get(this.loopAndSwitchSet.size() - 1);
            } else {
                this.reportError("msg.bad.break", pos, end - pos);
            }
        }
        BreakStatement pn = new BreakStatement(pos, end - pos);
        pn.setBreakLabel(breakLabel);
        if (breakTarget != null) {
            pn.setBreakTarget(breakTarget);
        }
        pn.setLineno(lineno);
        return pn;
    }

    private ContinueStatement continueStatement() throws IOException {
        if (this.currentToken != 122) {
            this.codeBug();
        }
        this.consumeToken();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        Name label = null;
        if (this.peekTokenOrEOL() == 39) {
            label = this.createNameNode();
            end = getNodeEnd(label);
        }
        LabeledStatement labels = this.matchJumpLabelName();
        Loop target = null;
        if (labels == null && label == null) {
            if (this.loopSet != null && this.loopSet.size() != 0) {
                target = (Loop) this.loopSet.get(this.loopSet.size() - 1);
            } else {
                this.reportError("msg.continue.outside");
            }
        } else {
            if (labels == null || !(labels.getStatement() instanceof Loop)) {
                this.reportError("msg.continue.nonloop", pos, end - pos);
            }
            target = labels == null ? null : (Loop) labels.getStatement();
        }
        ContinueStatement pn = new ContinueStatement(pos, end - pos);
        if (target != null) {
            pn.setTarget(target);
        }
        pn.setLabel(label);
        pn.setLineno(lineno);
        return pn;
    }

    private WithStatement withStatement() throws IOException {
        if (this.currentToken != 124) {
            this.codeBug();
        }
        this.consumeToken();
        Comment withComment = this.getAndResetJsDoc();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        int lp = -1;
        int rp = -1;
        if (this.mustMatchToken(88, "msg.no.paren.with", true)) {
            lp = this.ts.tokenBeg;
        }
        AstNode obj = this.expr();
        if (this.mustMatchToken(89, "msg.no.paren.after.with", true)) {
            rp = this.ts.tokenBeg;
        }
        WithStatement pn = new WithStatement(pos);
        AstNode body = this.getNextStatementAfterInlineComments(pn);
        pn.setLength(getNodeEnd(body) - pos);
        pn.setJsDocNode(withComment);
        pn.setExpression(obj);
        pn.setStatement(body);
        pn.setParens(lp, rp);
        pn.setLineno(lineno);
        return pn;
    }

    private AstNode letStatement() throws IOException {
        if (this.currentToken != 154) {
            this.codeBug();
        }
        this.consumeToken();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        AstNode pn;
        if (this.peekToken() == 88) {
            pn = this.let(true, pos);
        } else {
            pn = this.variables(154, pos, true);
        }
        pn.setLineno(lineno);
        return pn;
    }

    private AstNode returnOrYield(int tt, boolean exprContext) throws IOException {
        if (!this.insideFunction()) {
            this.reportError(tt == 4 ? "msg.bad.return" : "msg.bad.yield");
        }
        this.consumeToken();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        boolean yieldStar = false;
        if (tt == 73 && this.peekToken() == 23) {
            yieldStar = true;
            this.consumeToken();
        }
        AstNode e = null;
        switch(this.peekTokenOrEOL()) {
            case 73:
            default:
                e = this.expr();
                end = getNodeEnd(e);
            case -1:
            case 0:
            case 1:
            case 83:
            case 85:
            case 87:
            case 89:
                int before = this.endFlags;
                AstNode ret;
                if (tt == 4) {
                    this.endFlags |= e == null ? 2 : 4;
                    ret = new ReturnStatement(pos, end - pos, e);
                    if (nowAllSet(before, this.endFlags, 6)) {
                        this.addStrictWarning("msg.return.inconsistent", "", pos, end - pos);
                    }
                } else {
                    if (!this.insideFunction()) {
                        this.reportError("msg.bad.yield");
                    }
                    this.endFlags |= 8;
                    ret = new Yield(pos, end - pos, e, yieldStar);
                    this.setRequiresActivation();
                    this.setIsGenerator();
                    if (!exprContext) {
                        ret = new ExpressionStatement(ret);
                    }
                }
                if (this.insideFunction() && nowAllSet(before, this.endFlags, 12)) {
                    FunctionNode fn = (FunctionNode) this.currentScriptOrFn;
                    if (!fn.isES6Generator()) {
                        Name name = ((FunctionNode) this.currentScriptOrFn).getFunctionName();
                        if (name != null && name.length() != 0) {
                            this.addError("msg.generator.returns", name.getIdentifier());
                        } else {
                            this.addError("msg.anon.generator.returns", "");
                        }
                    }
                }
                ret.setLineno(lineno);
                return ret;
        }
    }

    private AstNode block() throws IOException {
        if (this.currentToken != 86) {
            this.codeBug();
        }
        this.consumeToken();
        int pos = this.ts.tokenBeg;
        Scope block = new Scope(pos);
        block.setLineno(this.ts.lineno);
        this.pushScope(block);
        Scope var3;
        try {
            this.statements(block);
            this.mustMatchToken(87, "msg.no.brace.block", true);
            block.setLength(this.ts.tokenEnd - pos);
            var3 = block;
        } finally {
            this.popScope();
        }
        return var3;
    }

    private void recordLabel(Label label, LabeledStatement bundle) throws IOException {
        if (this.peekToken() != 104) {
            this.codeBug();
        }
        this.consumeToken();
        String name = label.getName();
        if (this.labelSet == null) {
            this.labelSet = new HashMap();
        } else {
            LabeledStatement ls = (LabeledStatement) this.labelSet.get(name);
            if (ls != null) {
                this.reportError("msg.dup.label", label.getPosition(), label.getLength());
            }
        }
        bundle.addLabel(label);
        this.labelSet.put(name, bundle);
    }

    private AstNode nameOrLabel() throws IOException {
        if (this.currentToken != 39) {
            throw this.codeBug();
        } else {
            int pos = this.ts.tokenBeg;
            this.currentFlaggedToken |= 131072;
            AstNode expr = this.expr();
            if (expr.getType() != 131) {
                AstNode n = new ExpressionStatement(expr, !this.insideFunction());
                n.lineno = expr.lineno;
                return n;
            } else {
                LabeledStatement bundle = new LabeledStatement(pos);
                this.recordLabel((Label) expr, bundle);
                bundle.setLineno(this.ts.lineno);
                AstNode stmt = null;
                while (this.peekToken() == 39) {
                    this.currentFlaggedToken |= 131072;
                    expr = this.expr();
                    if (expr.getType() != 131) {
                        stmt = new ExpressionStatement(expr, !this.insideFunction());
                        this.autoInsertSemicolon(stmt);
                        break;
                    }
                    this.recordLabel((Label) expr, bundle);
                }
                try {
                    this.currentLabel = bundle;
                    if (stmt == null) {
                        stmt = this.statementHelper();
                        int ntt = this.peekToken();
                        if (ntt == 162 && stmt.getLineno() == ((Comment) this.scannedComments.get(this.scannedComments.size() - 1)).getLineno()) {
                            stmt.setInlineComment((AstNode) this.scannedComments.get(this.scannedComments.size() - 1));
                            this.consumeToken();
                        }
                    }
                } finally {
                    this.currentLabel = null;
                    for (Label lb : bundle.getLabels()) {
                        this.labelSet.remove(lb.getName());
                    }
                }
                bundle.setLength(stmt.getParent() == null ? getNodeEnd(stmt) - pos : getNodeEnd(stmt));
                bundle.setStatement(stmt);
                return bundle;
            }
        }
    }

    private VariableDeclaration variables(int declType, int pos, boolean isStatement) throws IOException {
        VariableDeclaration pn = new VariableDeclaration(pos);
        pn.setType(declType);
        pn.setLineno(this.ts.lineno);
        Comment varjsdocNode = this.getAndResetJsDoc();
        if (varjsdocNode != null) {
            pn.setJsDocNode(varjsdocNode);
        }
        int end;
        do {
            AstNode destructuring = null;
            Name name = null;
            int tt = this.peekToken();
            int kidPos = this.ts.tokenBeg;
            end = this.ts.tokenEnd;
            if (tt != 84 && tt != 86) {
                this.mustMatchToken(39, "msg.bad.var", true);
                name = this.createNameNode();
                name.setLineno(this.ts.getLineno());
                if (this.inUseStrictDirective) {
                    String id = this.ts.getString();
                    if ("eval".equals(id) || "arguments".equals(this.ts.getString())) {
                        this.reportError("msg.bad.id.strict", id);
                    }
                }
                this.defineSymbol(declType, this.ts.getString(), this.inForInit);
            } else {
                destructuring = this.destructuringPrimaryExpr();
                end = getNodeEnd(destructuring);
                if (!(destructuring instanceof DestructuringForm)) {
                    this.reportError("msg.bad.assign.left", kidPos, end - kidPos);
                }
                this.markDestructuring(destructuring);
            }
            int lineno = this.ts.lineno;
            Comment jsdocNode = this.getAndResetJsDoc();
            AstNode init = null;
            if (this.matchToken(91, true)) {
                init = this.assignExpr();
                end = getNodeEnd(init);
            }
            VariableInitializer vi = new VariableInitializer(kidPos, end - kidPos);
            if (destructuring != null) {
                if (init == null && !this.inForInit) {
                    this.reportError("msg.destruct.assign.no.init");
                }
                vi.setTarget(destructuring);
            } else {
                vi.setTarget(name);
            }
            vi.setInitializer(init);
            vi.setType(declType);
            vi.setJsDocNode(jsdocNode);
            vi.setLineno(lineno);
            pn.addVariable(vi);
        } while (this.matchToken(90, true));
        pn.setLength(end - pos);
        pn.setIsStatement(isStatement);
        return pn;
    }

    private AstNode let(boolean isStatement, int pos) throws IOException {
        LetNode pn = new LetNode(pos);
        pn.setLineno(this.ts.lineno);
        if (this.mustMatchToken(88, "msg.no.paren.after.let", true)) {
            pn.setLp(this.ts.tokenBeg - pos);
        }
        this.pushScope(pn);
        ExpressionStatement var7;
        try {
            VariableDeclaration vars = this.variables(154, this.ts.tokenBeg, isStatement);
            pn.setVariables(vars);
            if (this.mustMatchToken(89, "msg.no.paren.let", true)) {
                pn.setRp(this.ts.tokenBeg - pos);
            }
            if (isStatement && this.peekToken() == 86) {
                this.consumeToken();
                int beg = this.ts.tokenBeg;
                AstNode stmt = this.statements();
                this.mustMatchToken(87, "msg.no.curly.let", true);
                stmt.setLength(this.ts.tokenEnd - beg);
                pn.setLength(this.ts.tokenEnd - pos);
                pn.setBody(stmt);
                pn.setType(154);
                return pn;
            }
            AstNode expr = this.expr();
            pn.setLength(getNodeEnd(expr) - pos);
            pn.setBody(expr);
            if (!isStatement) {
                return pn;
            }
            ExpressionStatement es = new ExpressionStatement(pn, !this.insideFunction());
            es.setLineno(pn.getLineno());
            var7 = es;
        } finally {
            this.popScope();
        }
        return var7;
    }

    void defineSymbol(int declType, String name) {
        this.defineSymbol(declType, name, false);
    }

    void defineSymbol(int declType, String name, boolean ignoreNotInBlock) {
        if (name == null) {
            this.codeBug();
        }
        Scope definingScope = this.currentScope.getDefiningScope(name);
        AstSymbol symbol = definingScope != null ? definingScope.getSymbol(name) : null;
        if (symbol != null && definingScope == this.currentScope) {
            this.addError(switch(symbol.getDeclType()) {
                case 110 ->
                    "msg.fn.redecl";
                case 123 ->
                    "msg.var.redecl";
                case 154 ->
                    "msg.let.redecl";
                case 155 ->
                    "msg.const.redecl";
                default ->
                    "msg.parm.redecl";
            }, name);
        } else {
            switch(declType) {
                case 88:
                case 110:
                case 123:
                case 154:
                case 155:
                    this.currentScope.putSymbol(new AstSymbol(declType, name));
                    return;
                default:
                    throw Kit.codeBug();
            }
        }
    }

    private AstNode expr() throws IOException {
        AstNode pn = this.assignExpr();
        int pos = pn.getPosition();
        while (this.matchToken(90, true)) {
            int opPos = this.ts.tokenBeg;
            if (this.compilerEnv.isStrictMode() && !pn.hasSideEffects()) {
                this.addStrictWarning("msg.no.side.effects", "", pos, nodeEnd(pn) - pos);
            }
            if (this.peekToken() == 73) {
                this.reportError("msg.yield.parenthesized");
            }
            pn = new InfixExpression(90, pn, this.assignExpr(), opPos);
        }
        return pn;
    }

    private AstNode assignExpr() throws IOException {
        int tt = this.peekToken();
        if (tt == 73) {
            return this.returnOrYield(tt, true);
        } else {
            AstNode pn = this.condExpr();
            boolean hasEOL = false;
            tt = this.peekTokenOrEOL();
            if (tt == 1) {
                hasEOL = true;
                tt = this.peekToken();
            }
            if (91 <= tt && tt <= 102) {
                if (this.inDestructuringAssignment) {
                    this.reportError("msg.destruct.default.vals");
                }
                this.consumeToken();
                Comment jsdocNode = this.getAndResetJsDoc();
                this.markDestructuring(pn);
                int opPos = this.ts.tokenBeg;
                pn = new Assignment(tt, pn, this.assignExpr(), opPos);
                if (jsdocNode != null) {
                    pn.setJsDocNode(jsdocNode);
                }
            } else if (tt == 83) {
                if (this.currentJsDocComment != null) {
                    pn.setJsDocNode(this.getAndResetJsDoc());
                }
            } else if (!hasEOL && tt == 165) {
                this.consumeToken();
                pn = this.arrowFunction(pn);
            }
            return pn;
        }
    }

    private AstNode condExpr() throws IOException {
        AstNode pn = this.ncoExpr();
        if (this.matchToken(103, true)) {
            int line = this.ts.lineno;
            int qmarkPos = this.ts.tokenBeg;
            int colonPos = -1;
            boolean wasInForInit = this.inForInit;
            this.inForInit = false;
            AstNode ifTrue;
            try {
                ifTrue = this.assignExpr();
            } finally {
                this.inForInit = wasInForInit;
            }
            if (this.mustMatchToken(104, "msg.no.colon.cond", true)) {
                colonPos = this.ts.tokenBeg;
            }
            AstNode ifFalse = this.assignExpr();
            int beg = pn.getPosition();
            int len = getNodeEnd(ifFalse) - beg;
            ConditionalExpression ce = new ConditionalExpression(beg, len);
            ce.setLineno(line);
            ce.setTestExpression(pn);
            ce.setTrueExpression(ifTrue);
            ce.setFalseExpression(ifFalse);
            ce.setQuestionMarkPosition(qmarkPos - beg);
            ce.setColonPosition(colonPos - beg);
            return ce;
        } else {
            return pn;
        }
    }

    private AstNode ncoExpr() throws IOException {
        AstNode pn = this.orExpr();
        if (this.matchToken(75, true)) {
            int opPos = this.ts.tokenBeg;
            pn = new InfixExpression(75, pn, this.ncoExpr(), opPos);
        }
        return pn;
    }

    private AstNode orExpr() throws IOException {
        AstNode pn = this.andExpr();
        if (this.matchToken(105, true)) {
            int opPos = this.ts.tokenBeg;
            pn = new InfixExpression(105, pn, this.orExpr(), opPos);
        }
        return pn;
    }

    private AstNode andExpr() throws IOException {
        AstNode pn = this.powExpr();
        if (this.matchToken(106, true)) {
            int opPos = this.ts.tokenBeg;
            pn = new InfixExpression(106, pn, this.andExpr(), opPos);
        }
        return pn;
    }

    private AstNode powExpr() throws IOException {
        AstNode pn = this.bitOrExpr();
        while (this.matchToken(76, true)) {
            int opPos = this.ts.tokenBeg;
            pn = new InfixExpression(76, pn, this.bitOrExpr(), opPos);
        }
        return pn;
    }

    private AstNode bitOrExpr() throws IOException {
        AstNode pn = this.bitXorExpr();
        while (this.matchToken(9, true)) {
            int opPos = this.ts.tokenBeg;
            pn = new InfixExpression(9, pn, this.bitXorExpr(), opPos);
        }
        return pn;
    }

    private AstNode bitXorExpr() throws IOException {
        AstNode pn = this.bitAndExpr();
        while (this.matchToken(10, true)) {
            int opPos = this.ts.tokenBeg;
            pn = new InfixExpression(10, pn, this.bitAndExpr(), opPos);
        }
        return pn;
    }

    private AstNode bitAndExpr() throws IOException {
        AstNode pn = this.eqExpr();
        while (this.matchToken(11, true)) {
            int opPos = this.ts.tokenBeg;
            pn = new InfixExpression(11, pn, this.eqExpr(), opPos);
        }
        return pn;
    }

    private AstNode eqExpr() throws IOException {
        AstNode pn = this.relExpr();
        while (true) {
            int tt = this.peekToken();
            int opPos = this.ts.tokenBeg;
            switch(tt) {
                case 12:
                case 13:
                case 46:
                case 47:
                    this.consumeToken();
                    pn = new InfixExpression(tt, pn, this.relExpr(), opPos);
                    break;
                default:
                    return pn;
            }
        }
    }

    private AstNode relExpr() throws IOException {
        AstNode pn = this.shiftExpr();
        while (true) {
            int tt = this.peekToken();
            int opPos = this.ts.tokenBeg;
            switch(tt) {
                case 52:
                    if (this.inForInit) {
                        return pn;
                    }
                case 14:
                case 15:
                case 16:
                case 17:
                case 53:
                    this.consumeToken();
                    pn = new InfixExpression(tt, pn, this.shiftExpr(), opPos);
                    break;
                default:
                    return pn;
            }
        }
    }

    private AstNode shiftExpr() throws IOException {
        AstNode pn = this.addExpr();
        while (true) {
            int tt = this.peekToken();
            int opPos = this.ts.tokenBeg;
            switch(tt) {
                case 18:
                case 19:
                case 20:
                    this.consumeToken();
                    pn = new InfixExpression(tt, pn, this.addExpr(), opPos);
                    break;
                default:
                    return pn;
            }
        }
    }

    private AstNode addExpr() throws IOException {
        AstNode pn = this.mulExpr();
        while (true) {
            int tt = this.peekToken();
            int opPos = this.ts.tokenBeg;
            if (tt != 21 && tt != 22) {
                return pn;
            }
            this.consumeToken();
            pn = new InfixExpression(tt, pn, this.mulExpr(), opPos);
        }
    }

    private AstNode mulExpr() throws IOException {
        AstNode pn = this.unaryExpr();
        while (true) {
            int tt = this.peekToken();
            int opPos = this.ts.tokenBeg;
            switch(tt) {
                case 23:
                case 24:
                case 25:
                    this.consumeToken();
                    pn = new InfixExpression(tt, pn, this.unaryExpr(), opPos);
                    break;
                default:
                    return pn;
            }
        }
    }

    private AstNode unaryExpr() throws IOException {
        int tt = this.peekToken();
        if (tt == 162) {
            this.consumeToken();
            tt = this.peekUntilNonComment(tt);
        }
        int line = this.ts.lineno;
        switch(tt) {
            case -1:
                this.consumeToken();
                return this.makeErrorNode();
            case 21:
                {
                    this.consumeToken();
                    AstNode node = new UnaryExpression(28, this.ts.tokenBeg, this.unaryExpr());
                    node.setLineno(line);
                    return node;
                }
            case 22:
                {
                    this.consumeToken();
                    AstNode node = new UnaryExpression(29, this.ts.tokenBeg, this.unaryExpr());
                    node.setLineno(line);
                    return node;
                }
            case 26:
            case 27:
            case 31:
            case 32:
            case 127:
                {
                    this.consumeToken();
                    AstNode node = new UnaryExpression(tt, this.ts.tokenBeg, this.unaryExpr());
                    node.setLineno(line);
                    return node;
                }
            case 107:
            case 108:
                this.consumeToken();
                UnaryExpression expr = new UnaryExpression(tt, this.ts.tokenBeg, this.memberExpr(true));
                expr.setLineno(line);
                this.checkBadIncDec(expr);
                return expr;
            default:
                AstNode pn = this.memberExpr(true);
                tt = this.peekTokenOrEOL();
                if (tt != 107 && tt != 108) {
                    return pn;
                } else {
                    this.consumeToken();
                    UnaryExpression uexpr = new UnaryExpression(tt, this.ts.tokenBeg, pn, true);
                    uexpr.setLineno(line);
                    this.checkBadIncDec(uexpr);
                    return uexpr;
                }
        }
    }

    private List<AstNode> argumentList() throws IOException {
        if (this.matchToken(89, true)) {
            return null;
        } else {
            List<AstNode> result = new ArrayList();
            boolean wasInForInit = this.inForInit;
            this.inForInit = false;
            try {
                while (this.peekToken() != 89) {
                    if (this.peekToken() == 73) {
                        this.reportError("msg.yield.parenthesized");
                    }
                    AstNode en = this.assignExpr();
                    if (this.peekToken() == 120) {
                        try {
                            result.add(this.generatorExpression(en, 0, true));
                        } catch (IOException var8) {
                        }
                    } else {
                        result.add(en);
                    }
                    if (!this.matchToken(90, true)) {
                        break;
                    }
                }
            } finally {
                this.inForInit = wasInForInit;
            }
            this.mustMatchToken(89, "msg.no.paren.arg", true);
            return result;
        }
    }

    private AstNode memberExpr(boolean allowCallSyntax) throws IOException {
        int tt = this.peekToken();
        int lineno = this.ts.lineno;
        AstNode pn;
        if (tt != 30) {
            pn = this.primaryExpr();
        } else {
            this.consumeToken();
            int pos = this.ts.tokenBeg;
            NewExpression nx = new NewExpression(pos);
            AstNode target = this.memberExpr(false);
            int end = getNodeEnd(target);
            nx.setTarget(target);
            int lp = -1;
            if (this.matchToken(88, true)) {
                lp = this.ts.tokenBeg;
                List<AstNode> args = this.argumentList();
                if (args != null && args.size() > 65536) {
                    this.reportError("msg.too.many.constructor.args");
                }
                int rp = this.ts.tokenBeg;
                end = this.ts.tokenEnd;
                if (args != null) {
                    nx.setArguments(args);
                }
                nx.setParens(lp - pos, rp - pos);
            }
            if (this.matchToken(86, true)) {
                ObjectLiteral initializer = this.objectLiteral();
                end = getNodeEnd(initializer);
                nx.setInitializer(initializer);
            }
            nx.setLength(end - pos);
            pn = nx;
        }
        pn.setLineno(lineno);
        return this.memberExprTail(allowCallSyntax, pn);
    }

    private AstNode memberExprTail(boolean allowCallSyntax, AstNode pn) throws IOException {
        if (pn == null) {
            this.codeBug();
        }
        int pos = pn.getPosition();
        while (true) {
            int tt = this.peekToken();
            switch(tt) {
                case 77:
                case 109:
                    {
                        int lineno = this.ts.lineno;
                        pn = this.propertyAccess(tt, pn);
                        pn.setLineno(lineno);
                        break;
                    }
                case 84:
                    {
                        this.consumeToken();
                        int lb = this.ts.tokenBeg;
                        int rb = -1;
                        int lineno = this.ts.lineno;
                        AstNode expr = this.expr();
                        int end = getNodeEnd(expr);
                        if (this.mustMatchToken(85, "msg.no.bracket.index", true)) {
                            rb = this.ts.tokenBeg;
                            end = this.ts.tokenEnd;
                        }
                        ElementGet g = new ElementGet(pos, end - pos);
                        g.setTarget(pn);
                        g.setElement(expr);
                        g.setParens(lb, rb);
                        g.setLineno(lineno);
                        pn = g;
                        break;
                    }
                case 88:
                    {
                        if (!allowCallSyntax) {
                            return pn;
                        }
                        int lineno = this.ts.lineno;
                        this.consumeToken();
                        this.checkCallRequiresActivation(pn);
                        FunctionCall f = new FunctionCall(pos);
                        f.setTarget(pn);
                        f.setLineno(lineno);
                        f.setLp(this.ts.tokenBeg - pos);
                        List<AstNode> args = this.argumentList();
                        if (args != null && args.size() > 65536) {
                            this.reportError("msg.too.many.function.args");
                        }
                        f.setArguments(args);
                        f.setRp(this.ts.tokenBeg - pos);
                        f.setLength(this.ts.tokenEnd - pos);
                        pn = f;
                        break;
                    }
                case 162:
                    int currentFlagTOken = this.currentFlaggedToken;
                    this.peekUntilNonComment(tt);
                    this.currentFlaggedToken = (this.currentFlaggedToken & 65536) != 0 ? this.currentFlaggedToken : currentFlagTOken;
                    break;
                case 167:
                    this.consumeToken();
                    pn = this.taggedTemplateLiteral(pn);
                    break;
                default:
                    return pn;
            }
        }
    }

    private AstNode taggedTemplateLiteral(AstNode pn) throws IOException {
        AstNode templateLiteral = this.templateLiteral();
        TaggedTemplateLiteral tagged = new TaggedTemplateLiteral();
        tagged.setTarget(pn);
        tagged.setTemplateLiteral(templateLiteral);
        return tagged;
    }

    private AstNode propertyAccess(int tt, AstNode pn) throws IOException {
        if (pn == null) {
            this.codeBug();
        }
        int lineno = this.ts.lineno;
        int dotPos = this.ts.tokenBeg;
        this.consumeToken();
        int maybeName = this.nextToken();
        if (maybeName != 39 && !TokenStream.isKeyword(this.ts.getString(), this.inUseStrictDirective)) {
            this.reportError("msg.no.name.after.dot");
        }
        Name name = this.createNameNode(true, 33);
        PropertyGet pg = new PropertyGet(pn, name, dotPos);
        if (tt == 77) {
            pg.setType(78);
        }
        pg.setLineno(lineno);
        return pg;
    }

    private AstNode destructuringPrimaryExpr() throws IOException, Parser.ParserException {
        AstNode var1;
        try {
            this.inDestructuringAssignment = true;
            var1 = this.primaryExpr();
        } finally {
            this.inDestructuringAssignment = false;
        }
        return var1;
    }

    private AstNode primaryExpr() throws IOException {
        int ttFlagged = this.peekFlaggedToken();
        int tt = ttFlagged & 65535;
        switch(tt) {
            case -1:
                this.consumeToken();
                break;
            case 0:
                this.consumeToken();
                this.reportError("msg.unexpected.eof");
                break;
            case 24:
            case 101:
                {
                    this.consumeToken();
                    this.ts.readRegExp(tt);
                    int pos = this.ts.tokenBeg;
                    int end = this.ts.tokenEnd;
                    RegExpLiteral re = new RegExpLiteral(pos, end - pos);
                    re.setValue(this.ts.getString());
                    re.setFlags(this.ts.readAndClearRegExpFlags());
                    return re;
                }
            case 39:
                this.consumeToken();
                return this.name(ttFlagged, tt);
            case 40:
                this.consumeToken();
                String s = this.ts.getString();
                if (this.inUseStrictDirective && this.ts.isNumberOldOctal()) {
                    this.reportError("msg.no.old.octal.strict");
                }
                if (this.ts.isNumberBinary()) {
                    s = "0b" + s;
                }
                if (this.ts.isNumberOldOctal()) {
                    s = "0" + s;
                }
                if (this.ts.isNumberOctal()) {
                    s = "0o" + s;
                }
                if (this.ts.isNumberHex()) {
                    s = "0x" + s;
                }
                return new NumberLiteral(this.ts.tokenBeg, s, this.ts.getNumber());
            case 41:
                this.consumeToken();
                return this.createStringLiteral();
            case 42:
            case 43:
            case 44:
            case 45:
                {
                    this.consumeToken();
                    int pos = this.ts.tokenBeg;
                    int end = this.ts.tokenEnd;
                    return new KeywordLiteral(pos, end - pos, tt);
                }
            case 84:
                this.consumeToken();
                return this.arrayLiteral();
            case 86:
                this.consumeToken();
                return this.objectLiteral();
            case 88:
                this.consumeToken();
                return this.parenExpr();
            case 110:
                this.consumeToken();
                return this.function(2);
            case 128:
                this.consumeToken();
                this.reportError("msg.reserved.id", this.ts.getString());
                break;
            case 154:
                this.consumeToken();
                return this.let(false, this.ts.tokenBeg);
            case 167:
                this.consumeToken();
                return this.templateLiteral();
            default:
                this.consumeToken();
                this.reportError("msg.syntax");
        }
        this.consumeToken();
        return this.makeErrorNode();
    }

    private AstNode parenExpr() throws IOException {
        boolean wasInForInit = this.inForInit;
        this.inForInit = false;
        ErrorNode length;
        try {
            Comment jsdocNode = this.getAndResetJsDoc();
            int lineno = this.ts.lineno;
            int begin = this.ts.tokenBeg;
            AstNode e = (AstNode) (this.peekToken() == 89 ? new EmptyExpression(begin) : this.expr());
            if (this.peekToken() == 120) {
                return this.generatorExpression(e, begin);
            }
            this.mustMatchToken(89, "msg.no.paren", true);
            if (e.getType() != 129 || this.peekToken() == 165) {
                int lengthx = this.ts.tokenEnd - begin;
                ParenthesizedExpression pn = new ParenthesizedExpression(begin, lengthx, e);
                pn.setLineno(lineno);
                if (jsdocNode == null) {
                    jsdocNode = this.getAndResetJsDoc();
                }
                if (jsdocNode != null) {
                    pn.setJsDocNode(jsdocNode);
                }
                return pn;
            }
            this.reportError("msg.syntax");
            length = this.makeErrorNode();
        } finally {
            this.inForInit = wasInForInit;
        }
        return length;
    }

    private AstNode name(int ttFlagged, int tt) throws IOException {
        String nameString = this.ts.getString();
        int namePos = this.ts.tokenBeg;
        int nameLineno = this.ts.lineno;
        if (0 != (ttFlagged & 131072) && this.peekToken() == 104) {
            Label label = new Label(namePos, this.ts.tokenEnd - namePos);
            label.setName(nameString);
            label.setLineno(this.ts.lineno);
            return label;
        } else {
            this.saveNameTokenData(namePos, nameString, nameLineno);
            return this.createNameNode(true, 39);
        }
    }

    private AstNode arrayLiteral() throws IOException {
        if (this.currentToken != 84) {
            this.codeBug();
        }
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        List<AstNode> elements = new ArrayList();
        ArrayLiteral pn = new ArrayLiteral(pos);
        boolean after_lb_or_comma = true;
        int afterComma = -1;
        int skipCount = 0;
        while (true) {
            int tt = this.peekToken();
            if (tt == 90) {
                this.consumeToken();
                afterComma = this.ts.tokenEnd;
                if (!after_lb_or_comma) {
                    after_lb_or_comma = true;
                } else {
                    elements.add(new EmptyExpression(this.ts.tokenBeg, 1));
                    skipCount++;
                }
            } else if (tt != 162) {
                if (tt == 85) {
                    this.consumeToken();
                    end = this.ts.tokenEnd;
                    pn.setDestructuringLength(elements.size() + (after_lb_or_comma ? 1 : 0));
                    pn.setSkipCount(skipCount);
                    if (afterComma != -1) {
                        this.warnTrailingComma(pos, elements, afterComma);
                    }
                    break;
                }
                if (tt == 120 && !after_lb_or_comma && elements.size() == 1) {
                    return this.arrayComprehension((AstNode) elements.get(0), pos);
                }
                if (tt == 0) {
                    this.reportError("msg.no.bracket.arg");
                    break;
                }
                if (!after_lb_or_comma) {
                    this.reportError("msg.no.bracket.arg");
                }
                elements.add(this.assignExpr());
                after_lb_or_comma = false;
                afterComma = -1;
            } else {
                this.consumeToken();
            }
        }
        for (AstNode e : elements) {
            pn.addElement(e);
        }
        pn.setLength(end - pos);
        return pn;
    }

    private AstNode arrayComprehension(AstNode result, int pos) throws IOException {
        List<ArrayComprehensionLoop> loops = new ArrayList();
        while (this.peekToken() == 120) {
            loops.add(this.arrayComprehensionLoop());
        }
        int ifPos = -1;
        Parser.ConditionData data = null;
        if (this.peekToken() == 113) {
            this.consumeToken();
            ifPos = this.ts.tokenBeg - pos;
            data = this.condition();
        }
        this.mustMatchToken(85, "msg.no.bracket.arg", true);
        ArrayComprehension pn = new ArrayComprehension(pos, this.ts.tokenEnd - pos);
        pn.setResult(result);
        pn.setLoops(loops);
        if (data != null) {
            pn.setIfPosition(ifPos);
            pn.setFilter(data.condition);
            pn.setFilterLp(data.lp - pos);
            pn.setFilterRp(data.rp - pos);
        }
        return pn;
    }

    private ArrayComprehensionLoop arrayComprehensionLoop() throws IOException {
        if (this.nextToken() != 120) {
            this.codeBug();
        }
        int pos = this.ts.tokenBeg;
        int eachPos = -1;
        int lp = -1;
        int rp = -1;
        int inPos = -1;
        boolean isForOf = false;
        ArrayComprehensionLoop pn = new ArrayComprehensionLoop(pos);
        this.pushScope(pn);
        ArrayComprehensionLoop var10;
        try {
            if (this.matchToken(39, true)) {
                if (this.ts.getString().equals("each")) {
                    eachPos = this.ts.tokenBeg - pos;
                } else {
                    this.reportError("msg.no.paren.for");
                }
            }
            if (this.mustMatchToken(88, "msg.no.paren.for", true)) {
                lp = this.ts.tokenBeg - pos;
            }
            AstNode iter = null;
            switch(this.peekToken()) {
                case 39:
                    this.consumeToken();
                    iter = this.createNameNode();
                    break;
                case 84:
                case 86:
                    iter = this.destructuringPrimaryExpr();
                    this.markDestructuring(iter);
                    break;
                default:
                    this.reportError("msg.bad.var");
            }
            if (iter.getType() == 39) {
                this.defineSymbol(154, this.ts.getString(), true);
            }
            switch(this.nextToken()) {
                case 39:
                    if ("of".equals(this.ts.getString())) {
                        if (eachPos != -1) {
                            this.reportError("msg.invalid.for.each");
                        }
                        inPos = this.ts.tokenBeg - pos;
                        isForOf = true;
                        break;
                    }
                default:
                    this.reportError("msg.in.after.for.name");
                    break;
                case 52:
                    inPos = this.ts.tokenBeg - pos;
            }
            AstNode obj = this.expr();
            if (this.mustMatchToken(89, "msg.no.paren.for.ctrl", true)) {
                rp = this.ts.tokenBeg - pos;
            }
            pn.setLength(this.ts.tokenEnd - pos);
            pn.setIterator(iter);
            pn.setIteratedObject(obj);
            pn.setInPosition(inPos);
            pn.setEachPosition(eachPos);
            pn.setIsForEach(eachPos != -1);
            pn.setParens(lp, rp);
            pn.setIsForOf(isForOf);
            var10 = pn;
        } finally {
            this.popScope();
        }
        return var10;
    }

    private AstNode generatorExpression(AstNode result, int pos) throws IOException {
        return this.generatorExpression(result, pos, false);
    }

    private AstNode generatorExpression(AstNode result, int pos, boolean inFunctionParams) throws IOException {
        List<GeneratorExpressionLoop> loops = new ArrayList();
        while (this.peekToken() == 120) {
            loops.add(this.generatorExpressionLoop());
        }
        int ifPos = -1;
        Parser.ConditionData data = null;
        if (this.peekToken() == 113) {
            this.consumeToken();
            ifPos = this.ts.tokenBeg - pos;
            data = this.condition();
        }
        if (!inFunctionParams) {
            this.mustMatchToken(89, "msg.no.paren.let", true);
        }
        GeneratorExpression pn = new GeneratorExpression(pos, this.ts.tokenEnd - pos);
        pn.setResult(result);
        pn.setLoops(loops);
        if (data != null) {
            pn.setIfPosition(ifPos);
            pn.setFilter(data.condition);
            pn.setFilterLp(data.lp - pos);
            pn.setFilterRp(data.rp - pos);
        }
        return pn;
    }

    private GeneratorExpressionLoop generatorExpressionLoop() throws IOException {
        if (this.nextToken() != 120) {
            this.codeBug();
        }
        int pos = this.ts.tokenBeg;
        int lp = -1;
        int rp = -1;
        int inPos = -1;
        GeneratorExpressionLoop pn = new GeneratorExpressionLoop(pos);
        this.pushScope(pn);
        GeneratorExpressionLoop var8;
        try {
            if (this.mustMatchToken(88, "msg.no.paren.for", true)) {
                lp = this.ts.tokenBeg - pos;
            }
            AstNode iter = null;
            switch(this.peekToken()) {
                case 39:
                    this.consumeToken();
                    iter = this.createNameNode();
                    break;
                case 84:
                case 86:
                    iter = this.destructuringPrimaryExpr();
                    this.markDestructuring(iter);
                    break;
                default:
                    this.reportError("msg.bad.var");
            }
            if (iter.getType() == 39) {
                this.defineSymbol(154, this.ts.getString(), true);
            }
            if (this.mustMatchToken(52, "msg.in.after.for.name", true)) {
                inPos = this.ts.tokenBeg - pos;
            }
            AstNode obj = this.expr();
            if (this.mustMatchToken(89, "msg.no.paren.for.ctrl", true)) {
                rp = this.ts.tokenBeg - pos;
            }
            pn.setLength(this.ts.tokenEnd - pos);
            pn.setIterator(iter);
            pn.setIteratedObject(obj);
            pn.setInPosition(inPos);
            pn.setParens(lp, rp);
            var8 = pn;
        } finally {
            this.popScope();
        }
        return var8;
    }

    private ObjectLiteral objectLiteral() throws IOException {
        int pos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        int afterComma = -1;
        List<ObjectProperty> elems = new ArrayList();
        Set<String> getterNames = null;
        Set<String> setterNames = null;
        if (this.inUseStrictDirective) {
            getterNames = new HashSet();
            setterNames = new HashSet();
        }
        Comment objJsdocNode = this.getAndResetJsDoc();
        while (true) {
            String propertyName = null;
            int entryKind = 1;
            int tt = this.peekToken();
            Comment jsdocNode = this.getAndResetJsDoc();
            if (tt == 162) {
                this.consumeToken();
                tt = this.peekUntilNonComment(tt);
            }
            if (tt == 87) {
                if (afterComma != -1) {
                    this.warnTrailingComma(pos, elems, afterComma);
                }
                break;
            }
            AstNode pname = this.objliteralProperty();
            if (pname == null) {
                this.reportError("msg.bad.prop");
            } else {
                propertyName = this.ts.getString();
                int ppos = this.ts.tokenBeg;
                this.consumeToken();
                int peeked = this.peekToken();
                if (peeked != 90 && peeked != 104 && peeked != 87) {
                    if (peeked == 88) {
                        entryKind = 8;
                    } else if (pname.getType() == 39) {
                        if ("get".equals(propertyName)) {
                            entryKind = 2;
                        } else if ("set".equals(propertyName)) {
                            entryKind = 4;
                        }
                    }
                    if (entryKind == 2 || entryKind == 4) {
                        pname = this.objliteralProperty();
                        if (pname == null) {
                            this.reportError("msg.bad.prop");
                        }
                        this.consumeToken();
                    }
                    if (pname == null) {
                        propertyName = null;
                    } else {
                        propertyName = this.ts.getString();
                        ObjectProperty objectProp = this.methodDefinition(ppos, pname, entryKind);
                        pname.setJsDocNode(jsdocNode);
                        elems.add(objectProp);
                    }
                } else {
                    pname.setJsDocNode(jsdocNode);
                    elems.add(this.plainProperty(pname, tt));
                }
            }
            if (this.inUseStrictDirective && propertyName != null) {
                switch(entryKind) {
                    case 1:
                    case 8:
                        if (getterNames.contains(propertyName) || setterNames.contains(propertyName)) {
                            this.addError("msg.dup.obj.lit.prop.strict", propertyName);
                        }
                        getterNames.add(propertyName);
                        setterNames.add(propertyName);
                        break;
                    case 2:
                        if (getterNames.contains(propertyName)) {
                            this.addError("msg.dup.obj.lit.prop.strict", propertyName);
                        }
                        getterNames.add(propertyName);
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        break;
                    case 4:
                        if (setterNames.contains(propertyName)) {
                            this.addError("msg.dup.obj.lit.prop.strict", propertyName);
                        }
                        setterNames.add(propertyName);
                }
            }
            this.getAndResetJsDoc();
            if (!this.matchToken(90, true)) {
                break;
            }
            afterComma = this.ts.tokenEnd;
        }
        this.mustMatchToken(87, "msg.no.brace.prop", true);
        ObjectLiteral pn = new ObjectLiteral(pos, this.ts.tokenEnd - pos);
        if (objJsdocNode != null) {
            pn.setJsDocNode(objJsdocNode);
        }
        pn.setElements(elems);
        pn.setLineno(lineno);
        return pn;
    }

    private AstNode objliteralProperty() throws IOException {
        int tt = this.peekToken();
        AstNode pname;
        switch(tt) {
            case 39:
                pname = this.createNameNode();
                break;
            case 40:
                pname = new NumberLiteral(this.ts.tokenBeg, this.ts.getString(), this.ts.getNumber());
                break;
            case 41:
                pname = this.createStringLiteral();
                break;
            default:
                if (!TokenStream.isKeyword(this.ts.getString(), this.inUseStrictDirective)) {
                    return null;
                }
                pname = this.createNameNode();
        }
        return pname;
    }

    private ObjectProperty plainProperty(AstNode property, int ptt) throws IOException {
        int tt = this.peekToken();
        if ((tt == 90 || tt == 87) && ptt == 39) {
            if (!this.inDestructuringAssignment) {
                this.reportError("msg.bad.object.init");
            }
            AstNode nn = new Name(property.getPosition(), property.getString());
            ObjectProperty pn = new ObjectProperty();
            pn.putProp(26, Boolean.TRUE);
            pn.setLeftAndRight(property, nn);
            return pn;
        } else {
            this.mustMatchToken(104, "msg.no.colon.prop", true);
            ObjectProperty pn = new ObjectProperty();
            pn.setOperatorPosition(this.ts.tokenBeg);
            pn.setLeftAndRight(property, this.assignExpr());
            return pn;
        }
    }

    private ObjectProperty methodDefinition(int pos, AstNode propName, int entryKind) throws IOException {
        FunctionNode fn = this.function(2);
        Name name = fn.getFunctionName();
        if (name != null && name.length() != 0) {
            this.reportError("msg.bad.prop");
        }
        ObjectProperty pn = new ObjectProperty(pos);
        switch(entryKind) {
            case 2:
                pn.setIsGetterMethod();
                fn.setFunctionIsGetterMethod();
                break;
            case 4:
                pn.setIsSetterMethod();
                fn.setFunctionIsSetterMethod();
                break;
            case 8:
                pn.setIsNormalMethod();
                fn.setFunctionIsNormalMethod();
        }
        int end = getNodeEnd(fn);
        pn.setLeft(propName);
        pn.setRight(fn);
        pn.setLength(end - pos);
        return pn;
    }

    private Name createNameNode() {
        return this.createNameNode(false, 39);
    }

    private Name createNameNode(boolean checkActivation, int token) {
        int beg = this.ts.tokenBeg;
        String s = this.ts.getString();
        int lineno = this.ts.lineno;
        if (!"".equals(this.prevNameTokenString)) {
            beg = this.prevNameTokenStart;
            s = this.prevNameTokenString;
            lineno = this.prevNameTokenLineno;
            this.prevNameTokenStart = 0;
            this.prevNameTokenString = "";
            this.prevNameTokenLineno = 0;
        }
        if (s == null) {
            this.codeBug();
        }
        Name name = new Name(beg, s);
        name.setLineno(lineno);
        if (checkActivation) {
            this.checkActivationName(s, token);
        }
        return name;
    }

    private StringLiteral createStringLiteral() {
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        StringLiteral s = new StringLiteral(pos, end - pos);
        s.setLineno(this.ts.lineno);
        s.setValue(this.ts.getString());
        s.setQuoteCharacter(this.ts.getQuoteChar());
        return s;
    }

    private AstNode templateLiteral() throws IOException {
        if (this.currentToken != 167) {
            this.codeBug();
        }
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        List<AstNode> elements = new ArrayList();
        TemplateLiteral pn = new TemplateLiteral(pos);
        int posChars = this.ts.tokenBeg + 1;
        int tt;
        for (tt = this.ts.readTemplateLiteral(); tt == 169; tt = this.ts.readTemplateLiteral()) {
            elements.add(this.createTemplateLiteralCharacters(posChars));
            elements.add(this.expr());
            this.mustMatchToken(87, "msg.syntax", true);
            posChars = this.ts.tokenBeg + 1;
        }
        if (tt == -1) {
            return this.makeErrorNode();
        } else {
            assert tt == 167;
            elements.add(this.createTemplateLiteralCharacters(posChars));
            end = this.ts.tokenEnd;
            pn.setElements(elements);
            pn.setLength(end - pos);
            return pn;
        }
    }

    private TemplateCharacters createTemplateLiteralCharacters(int pos) {
        TemplateCharacters chars = new TemplateCharacters(pos, this.ts.tokenEnd - pos - 1);
        chars.setValue(this.ts.getString());
        chars.setRawValue(this.ts.getRawString());
        return chars;
    }

    protected void checkActivationName(String name, int token) {
        if (this.insideFunction()) {
            boolean activation = "arguments".equals(name) && ((FunctionNode) this.currentScriptOrFn).getFunctionType() != 4;
            if (activation) {
                this.setRequiresActivation();
            }
        }
    }

    protected void setRequiresActivation() {
        if (this.insideFunction()) {
            ((FunctionNode) this.currentScriptOrFn).setRequiresActivation();
        }
    }

    private void checkCallRequiresActivation(AstNode pn) {
        if (pn.getType() == 39 && "eval".equals(((Name) pn).getIdentifier()) || pn.getType() == 33 && "eval".equals(((PropertyGet) pn).getProperty().getIdentifier())) {
            this.setRequiresActivation();
        }
    }

    protected void setIsGenerator() {
        if (this.insideFunction()) {
            ((FunctionNode) this.currentScriptOrFn).setIsGenerator();
        }
    }

    private void checkBadIncDec(UnaryExpression expr) {
        AstNode op = this.removeParens(expr.getOperand());
        int tt = op.getType();
        if (tt != 39 && tt != 33 && tt != 36 && tt != 68 && tt != 38) {
            this.reportError(expr.getType() == 107 ? "msg.bad.incr" : "msg.bad.decr");
        }
    }

    private ErrorNode makeErrorNode() {
        ErrorNode pn = new ErrorNode(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
        pn.setLineno(this.ts.lineno);
        return pn;
    }

    private void saveNameTokenData(int pos, String name, int lineno) {
        this.prevNameTokenStart = pos;
        this.prevNameTokenString = name;
        this.prevNameTokenLineno = lineno;
    }

    private void warnMissingSemi(int pos, int end) {
        if (this.compilerEnv.isStrictMode()) {
            int[] linep = new int[2];
            String line = this.ts.getLine(end, linep);
            if (line != null) {
                this.addStrictWarning("msg.missing.semi", "", pos, end - pos, linep[0], line, linep[1]);
            } else {
                this.addStrictWarning("msg.missing.semi", "", pos, end - pos);
            }
        }
    }

    private void warnTrailingComma(int pos, List<?> elems, int commaPos) {
    }

    Node createDestructuringAssignment(int type, Node left, Node right) {
        String tempName = this.currentScriptOrFn.getNextTempName();
        Node result = this.destructuringAssignmentHelper(type, left, right, tempName);
        Node comma = result.getLastChild();
        comma.addChildToBack(this.createName(tempName));
        return result;
    }

    Node destructuringAssignmentHelper(int variableType, Node left, Node right, String tempName) {
        Scope result = this.createScopeNode(159, left.getLineno());
        result.addChildToFront(new Node(154, this.createName(39, tempName, right)));
        try {
            this.pushScope(result);
            this.defineSymbol(154, tempName, true);
        } finally {
            this.popScope();
        }
        Node comma;
        ArrayList destructuringNames;
        boolean empty;
        comma = new Node(90);
        result.addChildToBack(comma);
        destructuringNames = new ArrayList();
        empty = true;
        label36: switch(left.getType()) {
            case 33:
            case 36:
                switch(variableType) {
                    case 123:
                    case 154:
                    case 155:
                        this.reportError("msg.bad.assign.left");
                    default:
                        comma.addChildToBack(this.simpleAssignment(left, this.createName(tempName)));
                        break label36;
                }
            case 66:
                empty = this.destructuringArray((ArrayLiteral) left, variableType, tempName, comma, destructuringNames);
                break;
            case 67:
                empty = this.destructuringObject((ObjectLiteral) left, variableType, tempName, comma, destructuringNames);
                break;
            default:
                this.reportError("msg.bad.assign.left");
        }
        if (empty) {
            comma.addChildToBack(this.createNumber(0.0));
        }
        result.putProp(22, destructuringNames);
        return result;
    }

    boolean destructuringArray(ArrayLiteral array, int variableType, String tempName, Node parent, List<String> destructuringNames) {
        boolean empty = true;
        int setOp = variableType == 155 ? 156 : 8;
        int index = 0;
        for (AstNode n : array.getElements()) {
            if (n.getType() == 129) {
                index++;
            } else {
                Node rightElem = new Node(36, this.createName(tempName), this.createNumber((double) index));
                if (n.getType() == 39) {
                    String name = n.getString();
                    parent.addChildToBack(new Node(setOp, this.createName(49, name, null), rightElem));
                    if (variableType != -1) {
                        this.defineSymbol(variableType, name, true);
                        destructuringNames.add(name);
                    }
                } else {
                    parent.addChildToBack(this.destructuringAssignmentHelper(variableType, n, rightElem, this.currentScriptOrFn.getNextTempName()));
                }
                index++;
                empty = false;
            }
        }
        return empty;
    }

    boolean destructuringObject(ObjectLiteral node, int variableType, String tempName, Node parent, List<String> destructuringNames) {
        boolean empty = true;
        int setOp = variableType == 155 ? 156 : 8;
        for (ObjectProperty prop : node.getElements()) {
            int lineno = 0;
            if (this.ts != null) {
                lineno = this.ts.lineno;
            }
            AstNode id = prop.getLeft();
            Node rightElem = null;
            if (id instanceof Name) {
                Node s = Node.newString(((Name) id).getIdentifier());
                rightElem = new Node(33, this.createName(tempName), s);
            } else if (id instanceof StringLiteral) {
                Node s = Node.newString(((StringLiteral) id).getValue());
                rightElem = new Node(33, this.createName(tempName), s);
            } else {
                if (!(id instanceof NumberLiteral)) {
                    throw this.codeBug();
                }
                Node s = this.createNumber((double) ((int) ((NumberLiteral) id).getNumber()));
                rightElem = new Node(36, this.createName(tempName), s);
            }
            rightElem.setLineno(lineno);
            AstNode value = prop.getRight();
            if (value.getType() == 39) {
                String name = ((Name) value).getIdentifier();
                parent.addChildToBack(new Node(setOp, this.createName(49, name, null), rightElem));
                if (variableType != -1) {
                    this.defineSymbol(variableType, name, true);
                    destructuringNames.add(name);
                }
            } else {
                parent.addChildToBack(this.destructuringAssignmentHelper(variableType, value, rightElem, this.currentScriptOrFn.getNextTempName()));
            }
            empty = false;
        }
        return empty;
    }

    protected Node createName(String name) {
        this.checkActivationName(name, 39);
        return Node.newString(39, name);
    }

    protected Node createName(int type, String name, Node child) {
        Node result = this.createName(name);
        result.setType(type);
        if (child != null) {
            result.addChildToBack(child);
        }
        return result;
    }

    protected Node createNumber(double number) {
        return Node.newNumber(number);
    }

    protected Scope createScopeNode(int token, int lineno) {
        Scope scope = new Scope();
        scope.setType(token);
        scope.setLineno(lineno);
        return scope;
    }

    protected Node simpleAssignment(Node left, Node right) {
        int nodeType = left.getType();
        switch(nodeType) {
            case 33:
            case 36:
                Node id;
                Node obj;
                if (left instanceof PropertyGet) {
                    obj = ((PropertyGet) left).getTarget();
                    id = ((PropertyGet) left).getProperty();
                } else if (left instanceof ElementGet) {
                    obj = ((ElementGet) left).getTarget();
                    id = ((ElementGet) left).getElement();
                } else {
                    obj = left.getFirstChild();
                    id = left.getLastChild();
                }
                int type;
                if (nodeType == 33) {
                    type = 35;
                    id.setType(41);
                } else {
                    type = 37;
                }
                return new Node(type, obj, id, right);
            case 39:
                String name = ((Name) left).getIdentifier();
                if (this.inUseStrictDirective && ("eval".equals(name) || "arguments".equals(name))) {
                    this.reportError("msg.bad.id.strict", name);
                }
                left.setType(49);
                return new Node(8, left, right);
            case 68:
                Node ref = left.getFirstChild();
                this.checkMutableReference(ref);
                return new Node(69, ref, right);
            default:
                throw this.codeBug();
        }
    }

    protected void checkMutableReference(Node n) {
        int memberTypeFlags = n.getIntProp(16, 0);
        if ((memberTypeFlags & 4) != 0) {
            this.reportError("msg.bad.assign.left");
        }
    }

    protected AstNode removeParens(AstNode node) {
        while (node instanceof ParenthesizedExpression) {
            node = ((ParenthesizedExpression) node).getExpression();
        }
        return node;
    }

    void markDestructuring(AstNode node) {
        if (node instanceof DestructuringForm) {
            ((DestructuringForm) node).setIsDestructuring(true);
        } else if (node instanceof ParenthesizedExpression) {
            this.markDestructuring(((ParenthesizedExpression) node).getExpression());
        }
    }

    private RuntimeException codeBug() throws RuntimeException {
        throw Kit.codeBug("ts.cursor=" + this.ts.cursor + ", ts.tokenBeg=" + this.ts.tokenBeg + ", currentToken=" + this.currentToken);
    }

    public void setDefaultUseStrictDirective(boolean useStrict) {
        this.defaultUseStrictDirective = useStrict;
    }

    public boolean inUseStrictDirective() {
        return this.inUseStrictDirective;
    }

    private static class ConditionData {

        AstNode condition;

        int lp = -1;

        int rp = -1;
    }

    private static class ParserException extends RuntimeException {

        private static final long serialVersionUID = 5882582646773765630L;
    }

    protected class PerFunctionVariables {

        private final ScriptNode savedCurrentScriptOrFn = Parser.this.currentScriptOrFn;

        private final Scope savedCurrentScope;

        private final int savedEndFlags;

        private final boolean savedInForInit;

        private final Map<String, LabeledStatement> savedLabelSet;

        private final List<Loop> savedLoopSet;

        private final List<Jump> savedLoopAndSwitchSet;

        PerFunctionVariables(FunctionNode fnNode) {
            Parser.this.currentScriptOrFn = fnNode;
            this.savedCurrentScope = Parser.this.currentScope;
            Parser.this.currentScope = fnNode;
            this.savedLabelSet = Parser.this.labelSet;
            Parser.this.labelSet = null;
            this.savedLoopSet = Parser.this.loopSet;
            Parser.this.loopSet = null;
            this.savedLoopAndSwitchSet = Parser.this.loopAndSwitchSet;
            Parser.this.loopAndSwitchSet = null;
            this.savedEndFlags = Parser.this.endFlags;
            Parser.this.endFlags = 0;
            this.savedInForInit = Parser.this.inForInit;
            Parser.this.inForInit = false;
        }

        void restore() {
            Parser.this.currentScriptOrFn = this.savedCurrentScriptOrFn;
            Parser.this.currentScope = this.savedCurrentScope;
            Parser.this.labelSet = this.savedLabelSet;
            Parser.this.loopSet = this.savedLoopSet;
            Parser.this.loopAndSwitchSet = this.savedLoopAndSwitchSet;
            Parser.this.endFlags = this.savedEndFlags;
            Parser.this.inForInit = this.savedInForInit;
        }
    }
}