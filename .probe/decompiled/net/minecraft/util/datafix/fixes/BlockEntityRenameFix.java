package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import java.util.function.UnaryOperator;

public class BlockEntityRenameFix extends DataFix {

    private final String name;

    private final UnaryOperator<String> nameChangeLookup;

    private BlockEntityRenameFix(Schema schema0, String string1, UnaryOperator<String> unaryOperatorString2) {
        super(schema0, true);
        this.name = string1;
        this.nameChangeLookup = unaryOperatorString2;
    }

    public TypeRewriteRule makeRule() {
        TaggedChoiceType<String> $$0 = this.getInputSchema().findChoiceType(References.BLOCK_ENTITY);
        TaggedChoiceType<String> $$1 = this.getOutputSchema().findChoiceType(References.BLOCK_ENTITY);
        return this.fixTypeEverywhere(this.name, $$0, $$1, p_277946_ -> p_277512_ -> p_277512_.mapFirst(this.nameChangeLookup));
    }

    public static DataFix create(Schema schema0, String string1, UnaryOperator<String> unaryOperatorString2) {
        return new BlockEntityRenameFix(schema0, string1, unaryOperatorString2);
    }
}