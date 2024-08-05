package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class BedItemColorFix extends DataFix {

    public BedItemColorFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        OpticFinder<Pair<String, String>> $$0 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        return this.fixTypeEverywhereTyped("BedItemColorFix", this.getInputSchema().getType(References.ITEM_STACK), p_14724_ -> {
            Optional<Pair<String, String>> $$2 = p_14724_.getOptional($$0);
            if ($$2.isPresent() && Objects.equals(((Pair) $$2.get()).getSecond(), "minecraft:bed")) {
                Dynamic<?> $$3 = (Dynamic<?>) p_14724_.get(DSL.remainderFinder());
                if ($$3.get("Damage").asInt(0) == 0) {
                    return p_14724_.set(DSL.remainderFinder(), $$3.set("Damage", $$3.createShort((short) 14)));
                }
            }
            return p_14724_;
        });
    }
}