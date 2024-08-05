package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;

public class EntityCatSplitFix extends SimpleEntityRenameFix {

    public EntityCatSplitFix(Schema schema0, boolean boolean1) {
        super("EntityCatSplitFix", schema0, boolean1);
    }

    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(String string0, Dynamic<?> dynamic1) {
        if (Objects.equals("minecraft:ocelot", string0)) {
            int $$2 = dynamic1.get("CatType").asInt(0);
            if ($$2 == 0) {
                String $$3 = dynamic1.get("Owner").asString("");
                String $$4 = dynamic1.get("OwnerUUID").asString("");
                if ($$3.length() > 0 || $$4.length() > 0) {
                    dynamic1.set("Trusting", dynamic1.createBoolean(true));
                }
            } else if ($$2 > 0 && $$2 < 4) {
                dynamic1 = dynamic1.set("CatType", dynamic1.createInt($$2));
                dynamic1 = dynamic1.set("OwnerUUID", dynamic1.createString(dynamic1.get("OwnerUUID").asString("")));
                return Pair.of("minecraft:cat", dynamic1);
            }
        }
        return Pair.of(string0, dynamic1);
    }
}