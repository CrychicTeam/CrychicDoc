package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class ItemWaterPotionFix extends DataFix {

    public ItemWaterPotionFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.ITEM_STACK);
        OpticFinder<Pair<String, String>> $$1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        OpticFinder<?> $$2 = $$0.findField("tag");
        return this.fixTypeEverywhereTyped("ItemWaterPotionFix", $$0, p_16161_ -> {
            Optional<Pair<String, String>> $$3 = p_16161_.getOptional($$1);
            if ($$3.isPresent()) {
                String $$4 = (String) ((Pair) $$3.get()).getSecond();
                if ("minecraft:potion".equals($$4) || "minecraft:splash_potion".equals($$4) || "minecraft:lingering_potion".equals($$4) || "minecraft:tipped_arrow".equals($$4)) {
                    Typed<?> $$5 = p_16161_.getOrCreateTyped($$2);
                    Dynamic<?> $$6 = (Dynamic<?>) $$5.get(DSL.remainderFinder());
                    if (!$$6.get("Potion").asString().result().isPresent()) {
                        $$6 = $$6.set("Potion", $$6.createString("minecraft:water"));
                    }
                    return p_16161_.set($$2, $$5.set(DSL.remainderFinder(), $$6));
                }
            }
            return p_16161_;
        });
    }
}