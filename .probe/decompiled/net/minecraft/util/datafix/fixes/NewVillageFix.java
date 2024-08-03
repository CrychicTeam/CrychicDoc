package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.CompoundList.CompoundListType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class NewVillageFix extends DataFix {

    public NewVillageFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    protected TypeRewriteRule makeRule() {
        CompoundListType<String, ?> $$0 = DSL.compoundList(DSL.string(), this.getInputSchema().getType(References.STRUCTURE_FEATURE));
        OpticFinder<? extends List<? extends Pair<String, ?>>> $$1 = $$0.finder();
        return this.cap($$0);
    }

    private <SF> TypeRewriteRule cap(CompoundListType<String, SF> compoundListTypeStringSF0) {
        Type<?> $$1 = this.getInputSchema().getType(References.CHUNK);
        Type<?> $$2 = this.getInputSchema().getType(References.STRUCTURE_FEATURE);
        OpticFinder<?> $$3 = $$1.findField("Level");
        OpticFinder<?> $$4 = $$3.type().findField("Structures");
        OpticFinder<?> $$5 = $$4.type().findField("Starts");
        OpticFinder<List<Pair<String, SF>>> $$6 = compoundListTypeStringSF0.finder();
        return TypeRewriteRule.seq(this.fixTypeEverywhereTyped("NewVillageFix", $$1, p_16483_ -> p_16483_.updateTyped($$3, p_145526_ -> p_145526_.updateTyped($$4, p_145530_ -> p_145530_.updateTyped($$5, p_145533_ -> p_145533_.update($$6, p_145544_ -> (List) p_145544_.stream().filter(p_145546_ -> !Objects.equals(p_145546_.getFirst(), "Village")).map(p_145535_ -> p_145535_.mapFirst(p_145542_ -> p_145542_.equals("New_Village") ? "Village" : p_145542_)).collect(Collectors.toList()))).update(DSL.remainderFinder(), p_145550_ -> p_145550_.update("References", p_145552_ -> {
            Optional<? extends Dynamic<?>> $$1x = p_145552_.get("New_Village").result();
            return ((Dynamic) DataFixUtils.orElse($$1x.map(p_145540_ -> p_145552_.remove("New_Village").set("Village", p_145540_)), p_145552_)).remove("Village");
        }))))), this.fixTypeEverywhereTyped("NewVillageStartFix", $$2, p_16497_ -> p_16497_.update(DSL.remainderFinder(), p_145537_ -> p_145537_.update("id", p_145548_ -> Objects.equals(NamespacedSchema.ensureNamespaced(p_145548_.asString("")), "minecraft:new_village") ? p_145548_.createString("minecraft:village") : p_145548_))));
    }
}