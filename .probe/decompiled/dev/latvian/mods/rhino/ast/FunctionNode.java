package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionNode extends ScriptNode {

    public static final int FUNCTION_STATEMENT = 1;

    public static final int FUNCTION_EXPRESSION = 2;

    public static final int FUNCTION_EXPRESSION_STATEMENT = 3;

    public static final int ARROW_FUNCTION = 4;

    private static final List<AstNode> NO_PARAMS = Collections.unmodifiableList(new ArrayList());

    private Name functionName;

    private List<AstNode> params;

    private AstNode body;

    private boolean isExpressionClosure;

    private FunctionNode.Form functionForm = FunctionNode.Form.FUNCTION;

    private int lp = -1;

    private int rp = -1;

    private int functionType;

    private boolean needsActivation;

    private boolean isGenerator;

    private boolean isES6Generator;

    private List<Node> generatorResumePoints;

    private Map<Node, int[]> liveLocals;

    private AstNode memberExprNode;

    public FunctionNode() {
        this.type = 110;
    }

    public FunctionNode(int pos) {
        super(pos);
        this.type = 110;
    }

    public FunctionNode(int pos, Name name) {
        super(pos);
        this.type = 110;
        this.setFunctionName(name);
    }

    public Name getFunctionName() {
        return this.functionName;
    }

    public void setFunctionName(Name name) {
        this.functionName = name;
        if (name != null) {
            name.setParent(this);
        }
    }

    public String getName() {
        return this.functionName != null ? this.functionName.getIdentifier() : "";
    }

    public List<AstNode> getParams() {
        return this.params != null ? this.params : NO_PARAMS;
    }

    public void setParams(List<AstNode> params) {
        if (params == null) {
            this.params = null;
        } else {
            if (this.params != null) {
                this.params.clear();
            }
            for (AstNode param : params) {
                this.addParam(param);
            }
        }
    }

    public void addParam(AstNode param) {
        this.assertNotNull(param);
        if (this.params == null) {
            this.params = new ArrayList();
        }
        this.params.add(param);
        param.setParent(this);
    }

    public boolean isParam(AstNode node) {
        return this.params != null && this.params.contains(node);
    }

    public AstNode getBody() {
        return this.body;
    }

    public void setBody(AstNode body) {
        this.assertNotNull(body);
        this.body = body;
        if (Boolean.TRUE.equals(body.getProp(25))) {
            this.setIsExpressionClosure(true);
        }
        int absEnd = body.getPosition() + body.getLength();
        body.setParent(this);
        this.setLength(absEnd - this.position);
    }

    public int getLp() {
        return this.lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public int getRp() {
        return this.rp;
    }

    public void setRp(int rp) {
        this.rp = rp;
    }

    public void setParens(int lp, int rp) {
        this.lp = lp;
        this.rp = rp;
    }

    public boolean isExpressionClosure() {
        return this.isExpressionClosure;
    }

    public void setIsExpressionClosure(boolean isExpressionClosure) {
        this.isExpressionClosure = isExpressionClosure;
    }

    public boolean requiresActivation() {
        return this.needsActivation;
    }

    public void setRequiresActivation() {
        this.needsActivation = true;
    }

    public boolean isGenerator() {
        return this.isGenerator;
    }

    public void setIsGenerator() {
        this.isGenerator = true;
    }

    public boolean isES6Generator() {
        return this.isES6Generator;
    }

    public void setIsES6Generator() {
        this.isES6Generator = true;
        this.isGenerator = true;
    }

    public void addResumptionPoint(Node target) {
        if (this.generatorResumePoints == null) {
            this.generatorResumePoints = new ArrayList();
        }
        this.generatorResumePoints.add(target);
    }

    public List<Node> getResumptionPoints() {
        return this.generatorResumePoints;
    }

    public Map<Node, int[]> getLiveLocals() {
        return this.liveLocals;
    }

    public void addLiveLocals(Node node, int[] locals) {
        if (this.liveLocals == null) {
            this.liveLocals = new HashMap();
        }
        this.liveLocals.put(node, locals);
    }

    @Override
    public int addFunction(FunctionNode fnNode) {
        int result = super.addFunction(fnNode);
        if (this.getFunctionCount() > 0) {
            this.needsActivation = true;
        }
        return result;
    }

    public int getFunctionType() {
        return this.functionType;
    }

    public void setFunctionType(int type) {
        this.functionType = type;
    }

    public boolean isMethod() {
        return this.functionForm == FunctionNode.Form.GETTER || this.functionForm == FunctionNode.Form.SETTER || this.functionForm == FunctionNode.Form.METHOD;
    }

    public boolean isGetterMethod() {
        return this.functionForm == FunctionNode.Form.GETTER;
    }

    public boolean isSetterMethod() {
        return this.functionForm == FunctionNode.Form.SETTER;
    }

    public boolean isNormalMethod() {
        return this.functionForm == FunctionNode.Form.METHOD;
    }

    public void setFunctionIsGetterMethod() {
        this.functionForm = FunctionNode.Form.GETTER;
    }

    public void setFunctionIsSetterMethod() {
        this.functionForm = FunctionNode.Form.SETTER;
    }

    public void setFunctionIsNormalMethod() {
        this.functionForm = FunctionNode.Form.METHOD;
    }

    public AstNode getMemberExprNode() {
        return this.memberExprNode;
    }

    public void setMemberExprNode(AstNode node) {
        this.memberExprNode = node;
        if (node != null) {
            node.setParent(this);
        }
    }

    public static enum Form {

        FUNCTION, GETTER, SETTER, METHOD
    }
}