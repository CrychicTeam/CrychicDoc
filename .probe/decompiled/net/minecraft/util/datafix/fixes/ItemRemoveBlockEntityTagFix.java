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
import java.util.Set;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class ItemRemoveBlockEntityTagFix extends DataFix {

    private final Set<String> items;

    public ItemRemoveBlockEntityTagFix(Schema schema0, boolean boolean1, Set<String> setString2) {
        super(schema0, boolean1);
        this.items = setString2;
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.ITEM_STACK);
        OpticFinder<Pair<String, String>> $$1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        OpticFinder<?> $$2 = $$0.findField("tag");
        OpticFinder<?> $$3 = $$2.type().findField("BlockEntityTag");
        return this.fixTypeEverywhereTyped("ItemRemoveBlockEntityTagFix", $$0, p_242866_ -> {
            Optional<Pair<String, String>> $$4 = p_242866_.getOptional($$1);
            if ($$4.isPresent() && this.items.contains(((Pair) $$4.get()).getSecond())) {
                Optional<? extends Typed<?>> $$5 = p_242866_.getOptionalTyped($$2);
                if ($$5.isPresent()) {
                    Typed<?> $$6 = (Typed<?>) $$5.get();
                    Optional<? extends Typed<?>> $$7 = $$6.getOptionalTyped($$3);
                    if ($$7.isPresent()) {
                        Optional<? extends Dynamic<?>> $$8 = $$6.write().result();
                        Dynamic<?> $$9 = $$8.isPresent() ? (Dynamic) $$8.get() : (Dynamic) $$6.get(DSL.remainderFinder());
                        Dynamic<?> $$10 = $$9.remove("BlockEntityTag");
                        Optional<? extends Pair<? extends Typed<?>, ?>> $$11 = $$2.type().readTyped($$10).result();
                        if ($$11.isEmpty()) {
                            return p_242866_;
                        }
                        return p_242866_.set($$2, (Typed) ((Pair) $$11.get()).getFirst());
                    }
                }
            }
            return p_242866_;
        });
    }
}