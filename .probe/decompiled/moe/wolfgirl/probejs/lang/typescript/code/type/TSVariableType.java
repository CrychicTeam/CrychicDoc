package moe.wolfgirl.probejs.lang.typescript.code.type;

import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import org.jetbrains.annotations.Nullable;

public class TSVariableType extends BaseType {

    public final String symbol;

    public BaseType extendsType;

    public TSVariableType(String symbol, @Nullable BaseType extendsType) {
        this.symbol = symbol;
        this.extendsType = extendsType == Types.ANY ? null : extendsType;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return (Collection<ClassPath>) (this.extendsType == null ? List.of() : this.extendsType.getUsedClassPaths());
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return List.of(switch(input) {
            case INPUT, RETURN ->
                this.symbol;
            case VARIABLE ->
                this.extendsType == null ? this.symbol : "%s extends %s".formatted(this.symbol, this.extendsType.line(declaration, BaseType.FormatType.RETURN));
        });
    }
}