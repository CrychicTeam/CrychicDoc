package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;

public class EntityZombieSplitFix extends SimpleEntityRenameFix {

    public EntityZombieSplitFix(Schema schema0, boolean boolean1) {
        super("EntityZombieSplitFix", schema0, boolean1);
    }

    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(String string0, Dynamic<?> dynamic1) {
        if (Objects.equals("Zombie", string0)) {
            String $$2 = "Zombie";
            int $$3 = dynamic1.get("ZombieType").asInt(0);
            switch($$3) {
                case 0:
                default:
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    $$2 = "ZombieVillager";
                    dynamic1 = dynamic1.set("Profession", dynamic1.createInt($$3 - 1));
                    break;
                case 6:
                    $$2 = "Husk";
            }
            dynamic1 = dynamic1.remove("ZombieType");
            return Pair.of($$2, dynamic1);
        } else {
            return Pair.of(string0, dynamic1);
        }
    }
}