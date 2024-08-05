package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;

public class PlayerUUIDFix extends AbstractUUIDFix {

    public PlayerUUIDFix(Schema schema0) {
        super(schema0, References.PLAYER);
    }

    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("PlayerUUIDFix", this.getInputSchema().getType(this.f_14569_), p_16686_ -> {
            OpticFinder<?> $$1 = p_16686_.getType().findField("RootVehicle");
            return p_16686_.updateTyped($$1, $$1.type(), p_145597_ -> p_145597_.update(DSL.remainderFinder(), p_145601_ -> (Dynamic) m_14617_(p_145601_, "Attach", "Attach").orElse(p_145601_))).update(DSL.remainderFinder(), p_145599_ -> EntityUUIDFix.updateEntityUUID(EntityUUIDFix.updateLivingEntity(p_145599_)));
        });
    }
}