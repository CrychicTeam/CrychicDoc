package moe.wolfgirl.probejs.lang.typescript.code.member;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.utils.NameUtils;

public final class ParamDecl {

    public String name;

    public BaseType type;

    public boolean varArg;

    public boolean optional;

    public ParamDecl(String name, BaseType type, boolean varArg, boolean optional) {
        this.name = name;
        this.type = type;
        this.varArg = varArg;
        this.optional = optional;
    }

    public String format(int index, Declaration declaration) {
        String result = NameUtils.isNameSafe(this.name) ? this.name : "arg%d".formatted(index);
        if (this.varArg) {
            result = "...%s".formatted(result);
        }
        return "%s%s: %s".formatted(result, this.optional ? "?" : "", this.type.line(declaration, BaseType.FormatType.INPUT));
    }

    public static String formatParams(List<ParamDecl> params, Declaration declaration) {
        List<String> formattedParams = new ArrayList();
        ListIterator<ParamDecl> it = params.listIterator();
        while (it.hasNext()) {
            int index = it.nextIndex();
            ParamDecl param = (ParamDecl) it.next();
            formattedParams.add(param.format(index, declaration));
        }
        return "(%s)".formatted(String.join(", ", formattedParams));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            ParamDecl that = (ParamDecl) obj;
            return Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type) && this.varArg == that.varArg && this.optional == that.optional;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.type, this.varArg, this.optional });
    }

    public String toString() {
        return "ParamDecl[name=" + this.name + ", type=" + this.type + ", varArg=" + this.varArg + ", optional=" + this.optional + "]";
    }
}