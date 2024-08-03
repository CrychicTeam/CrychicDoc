package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;

public abstract class BlockRenameFixWithJigsaw extends BlockRenameFix {

    private final String name;

    public BlockRenameFixWithJigsaw(Schema schema0, String string1) {
        super(schema0, string1);
        this.name = string1;
    }

    @Override
    public TypeRewriteRule makeRule() {
        TypeReference $$0 = References.BLOCK_ENTITY;
        String $$1 = "minecraft:jigsaw";
        OpticFinder<?> $$2 = DSL.namedChoice("minecraft:jigsaw", this.getInputSchema().getChoiceType($$0, "minecraft:jigsaw"));
        TypeRewriteRule $$3 = this.fixTypeEverywhereTyped(this.name + " for jigsaw state", this.getInputSchema().getType($$0), this.getOutputSchema().getType($$0), p_145155_ -> p_145155_.updateTyped($$2, this.getOutputSchema().getChoiceType($$0, "minecraft:jigsaw"), p_145157_ -> p_145157_.update(DSL.remainderFinder(), p_145159_ -> p_145159_.update("final_state", p_145162_ -> (Dynamic) DataFixUtils.orElse(p_145162_.asString().result().map(p_145168_ -> {
            int $$1x = p_145168_.indexOf(91);
            int $$2x = p_145168_.indexOf(123);
            int $$3x = p_145168_.length();
            if ($$1x > 0) {
                $$3x = Math.min($$3x, $$1x);
            }
            if ($$2x > 0) {
                $$3x = Math.min($$3x, $$2x);
            }
            String $$4 = p_145168_.substring(0, $$3x);
            String $$5 = this.m_7384_($$4);
            return $$5 + p_145168_.substring($$3x);
        }).map(p_145159_::createString), p_145162_)))));
        return TypeRewriteRule.seq(super.makeRule(), $$3);
    }

    public static DataFix create(Schema schema0, String string1, final Function<String, String> functionStringString2) {
        return new BlockRenameFixWithJigsaw(schema0, string1) {

            @Override
            protected String fixBlock(String p_145176_) {
                return (String) functionStringString2.apply(p_145176_);
            }
        };
    }
}