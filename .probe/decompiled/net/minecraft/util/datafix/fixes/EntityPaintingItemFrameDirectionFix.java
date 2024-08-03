package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;

public class EntityPaintingItemFrameDirectionFix extends DataFix {

    private static final int[][] DIRECTIONS = new int[][] { { 0, 0, 1 }, { -1, 0, 0 }, { 0, 0, -1 }, { 1, 0, 0 } };

    public EntityPaintingItemFrameDirectionFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    private Dynamic<?> doFix(Dynamic<?> dynamic0, boolean boolean1, boolean boolean2) {
        if ((boolean1 || boolean2) && !dynamic0.get("Facing").asNumber().result().isPresent()) {
            int $$3;
            if (dynamic0.get("Direction").asNumber().result().isPresent()) {
                $$3 = dynamic0.get("Direction").asByte((byte) 0) % DIRECTIONS.length;
                int[] $$4 = DIRECTIONS[$$3];
                dynamic0 = dynamic0.set("TileX", dynamic0.createInt(dynamic0.get("TileX").asInt(0) + $$4[0]));
                dynamic0 = dynamic0.set("TileY", dynamic0.createInt(dynamic0.get("TileY").asInt(0) + $$4[1]));
                dynamic0 = dynamic0.set("TileZ", dynamic0.createInt(dynamic0.get("TileZ").asInt(0) + $$4[2]));
                dynamic0 = dynamic0.remove("Direction");
                if (boolean2 && dynamic0.get("ItemRotation").asNumber().result().isPresent()) {
                    dynamic0 = dynamic0.set("ItemRotation", dynamic0.createByte((byte) (dynamic0.get("ItemRotation").asByte((byte) 0) * 2)));
                }
            } else {
                $$3 = dynamic0.get("Dir").asByte((byte) 0) % DIRECTIONS.length;
                dynamic0 = dynamic0.remove("Dir");
            }
            dynamic0 = dynamic0.set("Facing", dynamic0.createByte((byte) $$3));
        }
        return dynamic0;
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getChoiceType(References.ENTITY, "Painting");
        OpticFinder<?> $$1 = DSL.namedChoice("Painting", $$0);
        Type<?> $$2 = this.getInputSchema().getChoiceType(References.ENTITY, "ItemFrame");
        OpticFinder<?> $$3 = DSL.namedChoice("ItemFrame", $$2);
        Type<?> $$4 = this.getInputSchema().getType(References.ENTITY);
        TypeRewriteRule $$5 = this.fixTypeEverywhereTyped("EntityPaintingFix", $$4, p_15516_ -> p_15516_.updateTyped($$1, $$0, p_145300_ -> p_145300_.update(DSL.remainderFinder(), p_145302_ -> this.doFix(p_145302_, true, false))));
        TypeRewriteRule $$6 = this.fixTypeEverywhereTyped("EntityItemFrameFix", $$4, p_15504_ -> p_15504_.updateTyped($$3, $$2, p_145296_ -> p_145296_.update(DSL.remainderFinder(), p_145298_ -> this.doFix(p_145298_, false, true))));
        return TypeRewriteRule.seq($$5, $$6);
    }
}