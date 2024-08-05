package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import java.util.function.UnaryOperator;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class NamespacedTypeRenameFix extends DataFix {

    private final String name;

    private final TypeReference type;

    private final UnaryOperator<String> renamer;

    public NamespacedTypeRenameFix(Schema schema0, String string1, TypeReference typeReference2, UnaryOperator<String> unaryOperatorString3) {
        super(schema0, false);
        this.name = string1;
        this.type = typeReference2;
        this.renamer = unaryOperatorString3;
    }

    protected TypeRewriteRule makeRule() {
        Type<Pair<String, String>> $$0 = DSL.named(this.type.typeName(), NamespacedSchema.namespacedString());
        if (!Objects.equals($$0, this.getInputSchema().getType(this.type))) {
            throw new IllegalStateException("\"" + this.type.typeName() + "\" is not what was expected.");
        } else {
            return this.fixTypeEverywhere(this.name, $$0, p_278028_ -> p_277944_ -> p_277944_.mapSecond(this.renamer));
        }
    }
}