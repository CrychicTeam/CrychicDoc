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
import java.util.Objects;
import java.util.Optional;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class ItemShulkerBoxColorFix extends DataFix {

    public static final String[] NAMES_BY_COLOR = new String[] { "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box" };

    public ItemShulkerBoxColorFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.ITEM_STACK);
        OpticFinder<Pair<String, String>> $$1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        OpticFinder<?> $$2 = $$0.findField("tag");
        OpticFinder<?> $$3 = $$2.type().findField("BlockEntityTag");
        return this.fixTypeEverywhereTyped("ItemShulkerBoxColorFix", $$0, p_16029_ -> {
            Optional<Pair<String, String>> $$4 = p_16029_.getOptional($$1);
            if ($$4.isPresent() && Objects.equals(((Pair) $$4.get()).getSecond(), "minecraft:shulker_box")) {
                Optional<? extends Typed<?>> $$5 = p_16029_.getOptionalTyped($$2);
                if ($$5.isPresent()) {
                    Typed<?> $$6 = (Typed<?>) $$5.get();
                    Optional<? extends Typed<?>> $$7 = $$6.getOptionalTyped($$3);
                    if ($$7.isPresent()) {
                        Typed<?> $$8 = (Typed<?>) $$7.get();
                        Dynamic<?> $$9 = (Dynamic<?>) $$8.get(DSL.remainderFinder());
                        int $$10 = $$9.get("Color").asInt(0);
                        $$9.remove("Color");
                        return p_16029_.set($$2, $$6.set($$3, $$8.set(DSL.remainderFinder(), $$9))).set($$1, Pair.of(References.ITEM_NAME.typeName(), NAMES_BY_COLOR[$$10 % 16]));
                    }
                }
            }
            return p_16029_;
        });
    }
}