package moe.wolfgirl.probejs.lang.typescript.code.ts;

import java.util.List;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;

public class ReexportDeclaration extends VariableDeclaration {

    public ReexportDeclaration(String symbol, BaseType type) {
        super(symbol, type);
    }

    @Override
    public List<String> formatRaw(Declaration declaration) {
        return List.of("export import %s = %s".formatted(this.symbol, this.type.line(declaration, BaseType.FormatType.RETURN)));
    }
}