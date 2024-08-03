package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class ItemStackUUIDFix extends AbstractUUIDFix {

    public ItemStackUUIDFix(Schema schema0) {
        super(schema0, References.ITEM_STACK);
    }

    public TypeRewriteRule makeRule() {
        OpticFinder<Pair<String, String>> $$0 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        return this.fixTypeEverywhereTyped("ItemStackUUIDFix", this.getInputSchema().getType(this.f_14569_), p_16132_ -> {
            OpticFinder<?> $$2 = p_16132_.getType().findField("tag");
            return p_16132_.updateTyped($$2, p_145429_ -> p_145429_.update(DSL.remainderFinder(), p_145433_ -> {
                p_145433_ = this.updateAttributeModifiers(p_145433_);
                if ((Boolean) p_16132_.getOptional($$0).map(p_145435_ -> "minecraft:player_head".equals(p_145435_.getSecond())).orElse(false)) {
                    p_145433_ = this.updateSkullOwner(p_145433_);
                }
                return p_145433_;
            }));
        });
    }

    private Dynamic<?> updateAttributeModifiers(Dynamic<?> dynamic0) {
        return dynamic0.update("AttributeModifiers", p_16145_ -> dynamic0.createList(p_16145_.asStream().map(p_145437_ -> (Dynamic) m_14617_(p_145437_, "UUID", "UUID").orElse(p_145437_))));
    }

    private Dynamic<?> updateSkullOwner(Dynamic<?> dynamic0) {
        return dynamic0.update("SkullOwner", p_16151_ -> (Dynamic) m_14590_(p_16151_, "Id", "Id").orElse(p_16151_));
    }
}