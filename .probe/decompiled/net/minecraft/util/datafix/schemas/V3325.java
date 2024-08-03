package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V3325 extends NamespacedSchema {

    public V3325(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        schema0.register($$1, "minecraft:item_display", p_270589_ -> DSL.optionalFields("item", References.ITEM_STACK.in(schema0)));
        schema0.register($$1, "minecraft:block_display", p_270174_ -> DSL.optionalFields("block_state", References.BLOCK_STATE.in(schema0)));
        schema0.registerSimple($$1, "minecraft:text_display");
        return $$1;
    }
}