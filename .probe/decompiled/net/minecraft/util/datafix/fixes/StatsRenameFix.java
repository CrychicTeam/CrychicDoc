package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import java.util.Map;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class StatsRenameFix extends DataFix {

    private final String name;

    private final Map<String, String> renames;

    public StatsRenameFix(Schema schema0, String string1, Map<String, String> mapStringString2) {
        super(schema0, false);
        this.name = string1;
        this.renames = mapStringString2;
    }

    protected TypeRewriteRule makeRule() {
        return TypeRewriteRule.seq(this.createStatRule(), this.createCriteriaRule());
    }

    private TypeRewriteRule createCriteriaRule() {
        Type<?> $$0 = this.getOutputSchema().getType(References.OBJECTIVE);
        Type<?> $$1 = this.getInputSchema().getType(References.OBJECTIVE);
        OpticFinder<?> $$2 = $$1.findField("CriteriaType");
        TaggedChoiceType<?> $$3 = (TaggedChoiceType<?>) $$2.type().findChoiceType("type", -1).orElseThrow(() -> new IllegalStateException("Can't find choice type for criteria"));
        Type<?> $$4 = (Type<?>) $$3.types().get("minecraft:custom");
        if ($$4 == null) {
            throw new IllegalStateException("Failed to find custom criterion type variant");
        } else {
            OpticFinder<?> $$5 = DSL.namedChoice("minecraft:custom", $$4);
            OpticFinder<String> $$6 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
            return this.fixTypeEverywhereTyped(this.name, $$1, $$0, p_181062_ -> p_181062_.updateTyped($$2, p_181066_ -> p_181066_.updateTyped($$5, p_181069_ -> p_181069_.update($$6, p_181071_ -> (String) this.renames.getOrDefault(p_181071_, p_181071_)))));
        }
    }

    private TypeRewriteRule createStatRule() {
        Type<?> $$0 = this.getOutputSchema().getType(References.STATS);
        Type<?> $$1 = this.getInputSchema().getType(References.STATS);
        OpticFinder<?> $$2 = $$1.findField("stats");
        OpticFinder<?> $$3 = $$2.type().findField("minecraft:custom");
        OpticFinder<String> $$4 = NamespacedSchema.namespacedString().finder();
        return this.fixTypeEverywhereTyped(this.name, $$1, $$0, p_145712_ -> p_145712_.updateTyped($$2, p_145716_ -> p_145716_.updateTyped($$3, p_145719_ -> p_145719_.update($$4, p_145721_ -> (String) this.renames.getOrDefault(p_145721_, p_145721_)))));
    }
}