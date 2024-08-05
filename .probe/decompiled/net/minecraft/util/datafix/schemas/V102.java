package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.templates.Hook.HookFunction;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V102 extends Schema {

    public V102(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        super.registerTypes(schema0, mapStringSupplierTypeTemplate1, mapStringSupplierTypeTemplate2);
        schema0.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema0), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema0), "BlockEntityTag", References.BLOCK_ENTITY.in(schema0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema0)), "Items", DSL.list(References.ITEM_STACK.in(schema0)))), V99.ADD_NAMES, HookFunction.IDENTITY));
    }
}