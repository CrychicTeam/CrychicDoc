package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class BlockNameFlatteningFix extends DataFix {

    public BlockNameFlatteningFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.BLOCK_NAME);
        Type<?> $$1 = this.getOutputSchema().getType(References.BLOCK_NAME);
        Type<Pair<String, Either<Integer, String>>> $$2 = DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString()));
        Type<Pair<String, String>> $$3 = DSL.named(References.BLOCK_NAME.typeName(), NamespacedSchema.namespacedString());
        if (Objects.equals($$0, $$2) && Objects.equals($$1, $$3)) {
            return this.fixTypeEverywhere("BlockNameFlatteningFix", $$2, $$3, p_14904_ -> p_145141_ -> p_145141_.mapSecond(p_145139_ -> (String) p_145139_.map(BlockStateData::m_14940_, p_145143_ -> BlockStateData.upgradeBlock(NamespacedSchema.ensureNamespaced(p_145143_)))));
        } else {
            throw new IllegalStateException("Expected and actual types don't match.");
        }
    }
}