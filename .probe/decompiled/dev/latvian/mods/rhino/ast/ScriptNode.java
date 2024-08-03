package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScriptNode extends Scope {

    private final List<FunctionNode> EMPTY_LIST = Collections.emptyList();

    private String sourceName;

    private int endLineno = -1;

    private List<FunctionNode> functions;

    private List<RegExpLiteral> regexps;

    private List<TemplateLiteral> templateLiterals;

    private List<AstSymbol> symbols = new ArrayList(4);

    private int paramCount = 0;

    private String[] variableNames;

    private boolean[] isConsts;

    private int tempNumber = 0;

    private boolean inStrictMode;

    public ScriptNode() {
        this.top = this;
        this.type = 137;
    }

    public ScriptNode(int pos) {
        super(pos);
        this.top = this;
        this.type = 137;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getBaseLineno() {
        return this.lineno;
    }

    public void setBaseLineno(int lineno) {
        if (lineno < 0 || this.lineno >= 0) {
            codeBug();
        }
        this.lineno = lineno;
    }

    public int getEndLineno() {
        return this.endLineno;
    }

    public void setEndLineno(int lineno) {
        if (lineno < 0 || this.endLineno >= 0) {
            codeBug();
        }
        this.endLineno = lineno;
    }

    public int getFunctionCount() {
        return this.functions == null ? 0 : this.functions.size();
    }

    public FunctionNode getFunctionNode(int i) {
        return (FunctionNode) this.functions.get(i);
    }

    public List<FunctionNode> getFunctions() {
        return this.functions == null ? this.EMPTY_LIST : this.functions;
    }

    public int addFunction(FunctionNode fnNode) {
        if (fnNode == null) {
            codeBug();
        }
        if (this.functions == null) {
            this.functions = new ArrayList();
        }
        this.functions.add(fnNode);
        return this.functions.size() - 1;
    }

    public int getRegexpCount() {
        return this.regexps == null ? 0 : this.regexps.size();
    }

    public String getRegexpString(int index) {
        return ((RegExpLiteral) this.regexps.get(index)).getValue();
    }

    public String getRegexpFlags(int index) {
        return ((RegExpLiteral) this.regexps.get(index)).getFlags();
    }

    public void addRegExp(RegExpLiteral re) {
        if (re == null) {
            codeBug();
        }
        if (this.regexps == null) {
            this.regexps = new ArrayList();
        }
        this.regexps.add(re);
        re.putIntProp(4, this.regexps.size() - 1);
    }

    public int getTemplateLiteralCount() {
        return this.templateLiterals == null ? 0 : this.templateLiterals.size();
    }

    public List<TemplateCharacters> getTemplateLiteralStrings(int index) {
        return ((TemplateLiteral) this.templateLiterals.get(index)).getTemplateStrings();
    }

    public void addTemplateLiteral(TemplateLiteral templateLiteral) {
        if (templateLiteral == null) {
            codeBug();
        }
        if (this.templateLiterals == null) {
            this.templateLiterals = new ArrayList();
        }
        this.templateLiterals.add(templateLiteral);
        templateLiteral.putIntProp(28, this.templateLiterals.size() - 1);
    }

    public int getIndexForNameNode(Node nameNode) {
        if (this.variableNames == null) {
            codeBug();
        }
        Scope node = nameNode.getScope();
        AstSymbol symbol = null;
        if (node != null && nameNode instanceof Name) {
            symbol = node.getSymbol(((Name) nameNode).getIdentifier());
        }
        return symbol == null ? -1 : symbol.getIndex();
    }

    public String getParamOrVarName(int index) {
        if (this.variableNames == null) {
            codeBug();
        }
        return this.variableNames[index];
    }

    public int getParamCount() {
        return this.paramCount;
    }

    public int getParamAndVarCount() {
        if (this.variableNames == null) {
            codeBug();
        }
        return this.symbols.size();
    }

    public String[] getParamAndVarNames() {
        if (this.variableNames == null) {
            codeBug();
        }
        return this.variableNames;
    }

    public boolean[] getParamAndVarConst() {
        if (this.variableNames == null) {
            codeBug();
        }
        return this.isConsts;
    }

    void addSymbol(AstSymbol symbol) {
        if (this.variableNames != null) {
            codeBug();
        }
        if (symbol.getDeclType() == 88) {
            this.paramCount++;
        }
        this.symbols.add(symbol);
    }

    public List<AstSymbol> getSymbols() {
        return this.symbols;
    }

    public void setSymbols(List<AstSymbol> symbols) {
        this.symbols = symbols;
    }

    public void flattenSymbolTable(boolean flattenAllTables) {
        if (!flattenAllTables) {
            List<AstSymbol> newSymbols = new ArrayList();
            if (this.symbolTable != null) {
                for (int i = 0; i < this.symbols.size(); i++) {
                    AstSymbol symbol = (AstSymbol) this.symbols.get(i);
                    if (symbol.getContainingTable() == this) {
                        newSymbols.add(symbol);
                    }
                }
            }
            this.symbols = newSymbols;
        }
        this.variableNames = new String[this.symbols.size()];
        this.isConsts = new boolean[this.symbols.size()];
        for (int ix = 0; ix < this.symbols.size(); ix++) {
            AstSymbol symbol = (AstSymbol) this.symbols.get(ix);
            this.variableNames[ix] = symbol.getName();
            this.isConsts[ix] = symbol.getDeclType() == 155;
            symbol.setIndex(ix);
        }
    }

    public String getNextTempName() {
        return "$" + this.tempNumber++;
    }

    public boolean isInStrictMode() {
        return this.inStrictMode;
    }

    public void setInStrictMode(boolean inStrictMode) {
        this.inStrictMode = inStrictMode;
    }
}