package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class EntityCustomNameToComponentFix extends DataFix {

    public EntityCustomNameToComponentFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        OpticFinder<String> $$0 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
        return this.fixTypeEverywhereTyped("EntityCustomNameToComponentFix", this.getInputSchema().getType(References.ENTITY), p_15402_ -> p_15402_.update(DSL.remainderFinder(), p_145277_ -> {
            Optional<String> $$3 = p_15402_.getOptional($$0);
            return $$3.isPresent() && Objects.equals($$3.get(), "minecraft:commandblock_minecart") ? p_145277_ : fixTagCustomName(p_145277_);
        }));
    }

    public static Dynamic<?> fixTagCustomName(Dynamic<?> dynamic0) {
        String $$1 = dynamic0.get("CustomName").asString("");
        return $$1.isEmpty() ? dynamic0.remove("CustomName") : dynamic0.set("CustomName", dynamic0.createString(Component.Serializer.toJson(Component.literal($$1))));
    }
}