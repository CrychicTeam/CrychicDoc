package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.chat.Component;

public class ObjectiveDisplayNameFix extends DataFix {

    public ObjectiveDisplayNameFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.OBJECTIVE);
        return this.fixTypeEverywhereTyped("ObjectiveDisplayNameFix", $$0, p_181039_ -> p_181039_.update(DSL.remainderFinder(), p_145556_ -> p_145556_.update("DisplayName", p_145559_ -> (Dynamic) DataFixUtils.orElse(p_145559_.asString().map(p_145561_ -> Component.Serializer.toJson(Component.literal(p_145561_))).map(p_145556_::createString).result(), p_145559_))));
    }
}