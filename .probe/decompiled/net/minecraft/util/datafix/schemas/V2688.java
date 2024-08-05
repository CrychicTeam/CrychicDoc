package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V2688 extends NamespacedSchema {

    public V2688(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        schema0.register($$1, "minecraft:glow_squid", () -> V100.equipment(schema0));
        schema0.register($$1, "minecraft:glow_item_frame", p_264877_ -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema0)));
        return $$1;
    }
}