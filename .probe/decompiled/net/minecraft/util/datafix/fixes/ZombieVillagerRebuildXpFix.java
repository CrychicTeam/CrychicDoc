package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import java.util.Optional;

public class ZombieVillagerRebuildXpFix extends NamedEntityFix {

    public ZombieVillagerRebuildXpFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1, "Zombie Villager XP rebuild", References.ENTITY, "minecraft:zombie_villager");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_17303_ -> {
            Optional<Number> $$1 = p_17303_.get("Xp").asNumber().result();
            if (!$$1.isPresent()) {
                int $$2 = p_17303_.get("VillagerData").get("level").asInt(1);
                return p_17303_.set("Xp", p_17303_.createInt(VillagerRebuildLevelAndXpFix.getMinXpPerLevel($$2)));
            } else {
                return p_17303_;
            }
        });
    }
}