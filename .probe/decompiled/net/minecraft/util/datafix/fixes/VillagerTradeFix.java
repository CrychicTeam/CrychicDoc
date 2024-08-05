package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class VillagerTradeFix extends NamedEntityFix {

    public VillagerTradeFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1, "Villager trade fix", References.ENTITY, "minecraft:villager");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        OpticFinder<?> $$1 = typed0.getType().findField("Offers");
        OpticFinder<?> $$2 = $$1.type().findField("Recipes");
        if (!($$2.type() instanceof ListType<?> $$4)) {
            throw new IllegalStateException("Recipes are expected to be a list.");
        } else {
            Type<?> $$5 = $$4.getElement();
            OpticFinder<?> $$6 = DSL.typeFinder($$5);
            OpticFinder<?> $$7 = $$5.findField("buy");
            OpticFinder<?> $$8 = $$5.findField("buyB");
            OpticFinder<?> $$9 = $$5.findField("sell");
            OpticFinder<Pair<String, String>> $$10 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
            Function<Typed<?>, Typed<?>> $$11 = p_17150_ -> this.updateItemStack($$10, p_17150_);
            return typed0.updateTyped($$1, p_17125_ -> p_17125_.updateTyped($$2, p_145782_ -> p_145782_.updateTyped($$6, p_145788_ -> p_145788_.updateTyped($$7, $$11).updateTyped($$8, $$11).updateTyped($$9, $$11))));
        }
    }

    private Typed<?> updateItemStack(OpticFinder<Pair<String, String>> opticFinderPairStringString0, Typed<?> typed1) {
        return typed1.update(opticFinderPairStringString0, p_17145_ -> p_17145_.mapSecond(p_145790_ -> Objects.equals(p_145790_, "minecraft:carved_pumpkin") ? "minecraft:pumpkin" : p_145790_));
    }
}