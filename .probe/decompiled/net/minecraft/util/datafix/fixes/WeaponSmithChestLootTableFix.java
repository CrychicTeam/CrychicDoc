package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class WeaponSmithChestLootTableFix extends NamedEntityFix {

    public WeaponSmithChestLootTableFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1, "WeaponSmithChestLootTableFix", References.BLOCK_ENTITY, "minecraft:chest");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        return typed0.update(DSL.remainderFinder(), p_203116_ -> {
            String $$1 = p_203116_.get("LootTable").asString("");
            return $$1.equals("minecraft:chests/village_blacksmith") ? p_203116_.set("LootTable", p_203116_.createString("minecraft:chests/village/village_weaponsmith")) : p_203116_;
        });
    }
}