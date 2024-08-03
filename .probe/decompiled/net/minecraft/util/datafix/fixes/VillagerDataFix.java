package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;

public class VillagerDataFix extends NamedEntityFix {

    public VillagerDataFix(Schema schema0, String string1) {
        super(schema0, false, "Villager profession data fix (" + string1 + ")", References.ENTITY, string1);
    }

    @Override
    protected Typed<?> fix(Typed<?> typed0) {
        Dynamic<?> $$1 = (Dynamic<?>) typed0.get(DSL.remainderFinder());
        return typed0.set(DSL.remainderFinder(), $$1.remove("Profession").remove("Career").remove("CareerLevel").set("VillagerData", $$1.createMap(ImmutableMap.of($$1.createString("type"), $$1.createString("minecraft:plains"), $$1.createString("profession"), $$1.createString(upgradeData($$1.get("Profession").asInt(0), $$1.get("Career").asInt(0))), $$1.createString("level"), (Dynamic) DataFixUtils.orElse($$1.get("CareerLevel").result(), $$1.createInt(1))))));
    }

    private static String upgradeData(int int0, int int1) {
        if (int0 == 0) {
            if (int1 == 2) {
                return "minecraft:fisherman";
            } else if (int1 == 3) {
                return "minecraft:shepherd";
            } else {
                return int1 == 4 ? "minecraft:fletcher" : "minecraft:farmer";
            }
        } else if (int0 == 1) {
            return int1 == 2 ? "minecraft:cartographer" : "minecraft:librarian";
        } else if (int0 == 2) {
            return "minecraft:cleric";
        } else if (int0 == 3) {
            if (int1 == 2) {
                return "minecraft:weaponsmith";
            } else {
                return int1 == 3 ? "minecraft:toolsmith" : "minecraft:armorer";
            }
        } else if (int0 == 4) {
            return int1 == 2 ? "minecraft:leatherworker" : "minecraft:butcher";
        } else {
            return int0 == 5 ? "minecraft:nitwit" : "minecraft:none";
        }
    }
}