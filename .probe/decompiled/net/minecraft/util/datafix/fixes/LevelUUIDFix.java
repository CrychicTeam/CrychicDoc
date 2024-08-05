package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import org.slf4j.Logger;

public class LevelUUIDFix extends AbstractUUIDFix {

    private static final Logger LOGGER = LogUtils.getLogger();

    public LevelUUIDFix(Schema schema0) {
        super(schema0, References.LEVEL);
    }

    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("LevelUUIDFix", this.getInputSchema().getType(this.f_14569_), p_16362_ -> p_16362_.updateTyped(DSL.remainderFinder(), p_145496_ -> p_145496_.update(DSL.remainderFinder(), p_145510_ -> {
            p_145510_ = this.updateCustomBossEvents(p_145510_);
            p_145510_ = this.updateDragonFight(p_145510_);
            return this.updateWanderingTrader(p_145510_);
        })));
    }

    private Dynamic<?> updateWanderingTrader(Dynamic<?> dynamic0) {
        return (Dynamic<?>) m_14590_(dynamic0, "WanderingTraderId", "WanderingTraderId").orElse(dynamic0);
    }

    private Dynamic<?> updateDragonFight(Dynamic<?> dynamic0) {
        return dynamic0.update("DimensionData", p_16387_ -> p_16387_.updateMapValues(p_145498_ -> p_145498_.mapSecond(p_145506_ -> p_145506_.update("DragonFight", p_145508_ -> (Dynamic) m_14617_(p_145508_, "DragonUUID", "Dragon").orElse(p_145508_)))));
    }

    private Dynamic<?> updateCustomBossEvents(Dynamic<?> dynamic0) {
        return dynamic0.update("CustomBossEvents", p_16379_ -> p_16379_.updateMapValues(p_145491_ -> p_145491_.mapSecond(p_145500_ -> p_145500_.update("Players", p_145494_ -> p_145500_.createList(p_145494_.asStream().map(p_145502_ -> (Dynamic) m_14578_(p_145502_).orElseGet(() -> {
            LOGGER.warn("CustomBossEvents contains invalid UUIDs.");
            return p_145502_;
        })))))));
    }
}