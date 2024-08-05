package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V1451_3 extends NamespacedSchema {

    public V1451_3(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        schema0.registerSimple($$1, "minecraft:egg");
        schema0.registerSimple($$1, "minecraft:ender_pearl");
        schema0.registerSimple($$1, "minecraft:fireball");
        schema0.register($$1, "minecraft:potion", p_17450_ -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema0)));
        schema0.registerSimple($$1, "minecraft:small_fireball");
        schema0.registerSimple($$1, "minecraft:snowball");
        schema0.registerSimple($$1, "minecraft:wither_skull");
        schema0.registerSimple($$1, "minecraft:xp_bottle");
        schema0.register($$1, "minecraft:arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema0)));
        schema0.register($$1, "minecraft:enderman", () -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(schema0), V100.equipment(schema0)));
        schema0.register($$1, "minecraft:falling_block", () -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(schema0), "TileEntityData", References.BLOCK_ENTITY.in(schema0)));
        schema0.register($$1, "minecraft:spectral_arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema0)));
        schema0.register($$1, "minecraft:chest_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        schema0.register($$1, "minecraft:commandblock_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0)));
        schema0.register($$1, "minecraft:furnace_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0)));
        schema0.register($$1, "minecraft:hopper_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        schema0.register($$1, "minecraft:minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0)));
        schema0.register($$1, "minecraft:spawner_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0), References.UNTAGGED_SPAWNER.in(schema0)));
        schema0.register($$1, "minecraft:tnt_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0)));
        return $$1;
    }
}