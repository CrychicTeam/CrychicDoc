package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import org.slf4j.Logger;

public class SavedDataUUIDFix extends AbstractUUIDFix {

    private static final Logger LOGGER = LogUtils.getLogger();

    public SavedDataUUIDFix(Schema schema0) {
        super(schema0, References.SAVED_DATA);
    }

    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("SavedDataUUIDFix", this.getInputSchema().getType(this.f_14569_), p_16865_ -> p_16865_.updateTyped(p_16865_.getType().findField("data"), p_145672_ -> p_145672_.update(DSL.remainderFinder(), p_145674_ -> p_145674_.update("Raids", p_145676_ -> p_145676_.createList(p_145676_.asStream().map(p_145678_ -> p_145678_.update("HeroesOfTheVillage", p_145680_ -> p_145680_.createList(p_145680_.asStream().map(p_145682_ -> (Dynamic) m_14621_(p_145682_, "UUIDMost", "UUIDLeast").orElseGet(() -> {
            LOGGER.warn("HeroesOfTheVillage contained invalid UUIDs.");
            return p_145682_;
        }))))))))));
    }
}