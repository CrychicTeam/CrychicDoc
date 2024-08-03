package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V2842 extends NamespacedSchema {

    public V2842(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        super.registerTypes(schema0, mapStringSupplierTypeTemplate1, mapStringSupplierTypeTemplate2);
        schema0.registerType(false, References.CHUNK, () -> DSL.optionalFields("entities", DSL.list(References.ENTITY_TREE.in(schema0)), "block_entities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema0), DSL.remainder())), "block_ticks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema0))), "sections", DSL.list(DSL.optionalFields("biomes", DSL.optionalFields("palette", DSL.list(References.BIOME.in(schema0))), "block_states", DSL.optionalFields("palette", DSL.list(References.BLOCK_STATE.in(schema0))))), "structures", DSL.optionalFields("starts", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema0)))));
    }
}