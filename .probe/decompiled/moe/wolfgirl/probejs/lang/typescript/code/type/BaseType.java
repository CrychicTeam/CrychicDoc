package moe.wolfgirl.probejs.lang.typescript.code.type;

import java.util.List;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.Code;

public abstract class BaseType extends Code {

    @Override
    public final List<String> format(Declaration declaration) {
        return this.format(declaration, BaseType.FormatType.RETURN);
    }

    public abstract List<String> format(Declaration declaration, BaseType.FormatType input);

    public String line(Declaration declaration, BaseType.FormatType input) {
        return (String) this.format(declaration, input).get(0);
    }

    public TSArrayType asArray() {
        return new TSArrayType(this);
    }

    public static enum FormatType {

        INPUT, RETURN, VARIABLE
    }
}