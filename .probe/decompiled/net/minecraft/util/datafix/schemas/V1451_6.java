package net.minecraft.util.datafix.schemas;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.templates.Hook.HookFunction;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;

public class V1451_6 extends NamespacedSchema {

    public static final String SPECIAL_OBJECTIVE_MARKER = "_special";

    protected static final HookFunction UNPACK_OBJECTIVE_ID = new HookFunction() {

        public <T> T apply(DynamicOps<T> p_181096_, T p_181097_) {
            Dynamic<T> $$2 = new Dynamic(p_181096_, p_181097_);
            return (T) ((Dynamic) DataFixUtils.orElse($$2.get("CriteriaName").asString().get().left().map(p_181094_ -> {
                int $$1 = p_181094_.indexOf(58);
                if ($$1 < 0) {
                    return Pair.of("_special", p_181094_);
                } else {
                    try {
                        ResourceLocation $$2x = ResourceLocation.of(p_181094_.substring(0, $$1), '.');
                        ResourceLocation $$3 = ResourceLocation.of(p_181094_.substring($$1 + 1), '.');
                        return Pair.of($$2x.toString(), $$3.toString());
                    } catch (Exception var4) {
                        return Pair.of("_special", p_181094_);
                    }
                }
            }).map(p_181092_ -> $$2.set("CriteriaType", $$2.createMap(ImmutableMap.of($$2.createString("type"), $$2.createString((String) p_181092_.getFirst()), $$2.createString("id"), $$2.createString((String) p_181092_.getSecond()))))), $$2)).getValue();
        }
    };

    protected static final HookFunction REPACK_OBJECTIVE_ID = new HookFunction() {

        private String packWithDot(String p_181103_) {
            ResourceLocation $$1 = ResourceLocation.tryParse(p_181103_);
            return $$1 != null ? $$1.getNamespace() + "." + $$1.getPath() : p_181103_;
        }

        public <T> T apply(DynamicOps<T> p_181105_, T p_181106_) {
            Dynamic<T> $$2 = new Dynamic(p_181105_, p_181106_);
            Optional<Dynamic<T>> $$3 = $$2.get("CriteriaType").get().get().left().flatMap(p_181109_ -> {
                Optional<String> $$2x = p_181109_.get("type").asString().get().left();
                Optional<String> $$3x = p_181109_.get("id").asString().get().left();
                if ($$2x.isPresent() && $$3x.isPresent()) {
                    String $$4 = (String) $$2x.get();
                    return $$4.equals("_special") ? Optional.of($$2.createString((String) $$3x.get())) : Optional.of(p_181109_.createString(this.packWithDot($$4) + ":" + this.packWithDot((String) $$3x.get())));
                } else {
                    return Optional.empty();
                }
            });
            return (T) ((Dynamic) DataFixUtils.orElse($$3.map(p_181101_ -> $$2.set("CriteriaName", p_181101_).remove("CriteriaType")), $$2)).getValue();
        }
    };

    public V1451_6(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        super.registerTypes(schema0, mapStringSupplierTypeTemplate1, mapStringSupplierTypeTemplate2);
        Supplier<TypeTemplate> $$3 = () -> DSL.compoundList(References.ITEM_NAME.in(schema0), DSL.constType(DSL.intType()));
        schema0.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema0), DSL.constType(DSL.intType())), "minecraft:crafted", (TypeTemplate) $$3.get(), "minecraft:used", (TypeTemplate) $$3.get(), "minecraft:broken", (TypeTemplate) $$3.get(), "minecraft:picked_up", (TypeTemplate) $$3.get(), DSL.optionalFields("minecraft:dropped", (TypeTemplate) $$3.get(), "minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema0), DSL.constType(DSL.intType())), "minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema0), DSL.constType(DSL.intType())), "minecraft:custom", DSL.compoundList(DSL.constType(m_17310_()), DSL.constType(DSL.intType()))))));
        Map<String, Supplier<TypeTemplate>> $$4 = createCriterionTypes(schema0);
        schema0.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", DSL.taggedChoiceLazy("type", DSL.string(), $$4)), UNPACK_OBJECTIVE_ID, REPACK_OBJECTIVE_ID));
    }

    protected static Map<String, Supplier<TypeTemplate>> createCriterionTypes(Schema schema0) {
        Supplier<TypeTemplate> $$1 = () -> DSL.optionalFields("id", References.ITEM_NAME.in(schema0));
        Supplier<TypeTemplate> $$2 = () -> DSL.optionalFields("id", References.BLOCK_NAME.in(schema0));
        Supplier<TypeTemplate> $$3 = () -> DSL.optionalFields("id", References.ENTITY_NAME.in(schema0));
        Map<String, Supplier<TypeTemplate>> $$4 = Maps.newHashMap();
        $$4.put("minecraft:mined", $$2);
        $$4.put("minecraft:crafted", $$1);
        $$4.put("minecraft:used", $$1);
        $$4.put("minecraft:broken", $$1);
        $$4.put("minecraft:picked_up", $$1);
        $$4.put("minecraft:dropped", $$1);
        $$4.put("minecraft:killed", $$3);
        $$4.put("minecraft:killed_by", $$3);
        $$4.put("minecraft:custom", (Supplier) () -> DSL.optionalFields("id", DSL.constType(m_17310_())));
        $$4.put("_special", (Supplier) () -> DSL.optionalFields("id", DSL.constType(DSL.string())));
        return $$4;
    }
}