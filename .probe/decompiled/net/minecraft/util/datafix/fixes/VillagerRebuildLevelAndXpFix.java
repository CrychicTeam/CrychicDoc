package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.util.Mth;

public class VillagerRebuildLevelAndXpFix extends DataFix {

    private static final int TRADES_PER_LEVEL = 2;

    private static final int[] LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 50, 100, 150 };

    public static int getMinXpPerLevel(int int0) {
        return LEVEL_XP_THRESHOLDS[Mth.clamp(int0 - 1, 0, LEVEL_XP_THRESHOLDS.length - 1)];
    }

    public VillagerRebuildLevelAndXpFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getChoiceType(References.ENTITY, "minecraft:villager");
        OpticFinder<?> $$1 = DSL.namedChoice("minecraft:villager", $$0);
        OpticFinder<?> $$2 = $$0.findField("Offers");
        Type<?> $$3 = $$2.type();
        OpticFinder<?> $$4 = $$3.findField("Recipes");
        ListType<?> $$5 = (ListType<?>) $$4.type();
        OpticFinder<?> $$6 = $$5.getElement().finder();
        return this.fixTypeEverywhereTyped("Villager level and xp rebuild", this.getInputSchema().getType(References.ENTITY), p_17098_ -> p_17098_.updateTyped($$1, $$0, p_145766_ -> {
            Dynamic<?> $$4x = (Dynamic<?>) p_145766_.get(DSL.remainderFinder());
            int $$5x = $$4x.get("VillagerData").get("level").asInt(0);
            Typed<?> $$6x = p_145766_;
            if ($$5x == 0 || $$5x == 1) {
                int $$7 = (Integer) p_145766_.getOptionalTyped($$2).flatMap(p_145772_ -> p_145772_.getOptionalTyped($$4)).map(p_145769_ -> p_145769_.getAllTyped($$6).size()).orElse(0);
                $$5x = Mth.clamp($$7 / 2, 1, 5);
                if ($$5x > 1) {
                    $$6x = addLevel(p_145766_, $$5x);
                }
            }
            Optional<Number> $$8 = $$4x.get("Xp").asNumber().result();
            if (!$$8.isPresent()) {
                $$6x = addXpFromLevel($$6x, $$5x);
            }
            return $$6x;
        }));
    }

    private static Typed<?> addLevel(Typed<?> typed0, int int1) {
        return typed0.update(DSL.remainderFinder(), p_17104_ -> p_17104_.update("VillagerData", p_145775_ -> p_145775_.set("level", p_145775_.createInt(int1))));
    }

    private static Typed<?> addXpFromLevel(Typed<?> typed0, int int1) {
        int $$2 = getMinXpPerLevel(int1);
        return typed0.update(DSL.remainderFinder(), p_17083_ -> p_17083_.set("Xp", p_17083_.createInt($$2)));
    }
}