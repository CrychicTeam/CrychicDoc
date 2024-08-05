package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import org.slf4j.Logger;

public class TrappedChestBlockEntityFix extends DataFix {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int SIZE = 4096;

    private static final short SIZE_BITS = 12;

    public TrappedChestBlockEntityFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getOutputSchema().getType(References.CHUNK);
        Type<?> $$1 = $$0.findFieldType("Level");
        if (!($$1.findFieldType("TileEntities") instanceof ListType<?> $$3)) {
            throw new IllegalStateException("Tile entity type is not a list type.");
        } else {
            OpticFinder<? extends List<?>> $$4 = DSL.fieldFinder("TileEntities", $$3);
            Type<?> $$5 = this.getInputSchema().getType(References.CHUNK);
            OpticFinder<?> $$6 = $$5.findField("Level");
            OpticFinder<?> $$7 = $$6.type().findField("Sections");
            Type<?> $$8 = $$7.type();
            if (!($$8 instanceof ListType)) {
                throw new IllegalStateException("Expecting sections to be a list.");
            } else {
                Type<?> $$9 = ((ListType) $$8).getElement();
                OpticFinder<?> $$10 = DSL.typeFinder($$9);
                return TypeRewriteRule.seq(new AddNewChoices(this.getOutputSchema(), "AddTrappedChestFix", References.BLOCK_ENTITY).makeRule(), this.fixTypeEverywhereTyped("Trapped Chest fix", $$5, p_17031_ -> p_17031_.updateTyped($$6, p_145746_ -> {
                    Optional<? extends Typed<?>> $$4x = p_145746_.getOptionalTyped($$7);
                    if (!$$4x.isPresent()) {
                        return p_145746_;
                    } else {
                        List<? extends Typed<?>> $$5x = ((Typed) $$4x.get()).getAllTyped($$10);
                        IntSet $$6x = new IntOpenHashSet();
                        for (Typed<?> $$7x : $$5x) {
                            TrappedChestBlockEntityFix.TrappedChestSection $$8x = new TrappedChestBlockEntityFix.TrappedChestSection($$7x, this.getInputSchema());
                            if (!$$8x.m_16298_()) {
                                for (int $$9x = 0; $$9x < 4096; $$9x++) {
                                    int $$10x = $$8x.m_16302_($$9x);
                                    if ($$8x.isTrappedChest($$10x)) {
                                        $$6x.add($$8x.m_16301_() << 12 | $$9x);
                                    }
                                }
                            }
                        }
                        Dynamic<?> $$11 = (Dynamic<?>) p_145746_.get(DSL.remainderFinder());
                        int $$12 = $$11.get("xPos").asInt(0);
                        int $$13 = $$11.get("zPos").asInt(0);
                        TaggedChoiceType<String> $$14 = this.getInputSchema().findChoiceType(References.BLOCK_ENTITY);
                        return p_145746_.updateTyped($$4, p_145752_ -> p_145752_.updateTyped($$14.finder(), p_145741_ -> {
                            Dynamic<?> $$5xx = (Dynamic<?>) p_145741_.getOrCreate(DSL.remainderFinder());
                            int $$6xx = $$5xx.get("x").asInt(0) - ($$12 << 4);
                            int $$7xx = $$5xx.get("y").asInt(0);
                            int $$8xx = $$5xx.get("z").asInt(0) - ($$13 << 4);
                            return $$6x.contains(LeavesFix.getIndex($$6xx, $$7xx, $$8xx)) ? p_145741_.update($$14.finder(), p_145754_ -> p_145754_.mapFirst(p_145756_ -> {
                                if (!Objects.equals(p_145756_, "minecraft:chest")) {
                                    LOGGER.warn("Block Entity was expected to be a chest");
                                }
                                return "minecraft:trapped_chest";
                            })) : p_145741_;
                        }));
                    }
                })));
            }
        }
    }

    public static final class TrappedChestSection extends LeavesFix.Section {

        @Nullable
        private IntSet chestIds;

        public TrappedChestSection(Typed<?> typed0, Schema schema1) {
            super(typed0, schema1);
        }

        @Override
        protected boolean skippable() {
            this.chestIds = new IntOpenHashSet();
            for (int $$0 = 0; $$0 < this.f_16281_.size(); $$0++) {
                Dynamic<?> $$1 = (Dynamic<?>) this.f_16281_.get($$0);
                String $$2 = $$1.get("Name").asString("");
                if (Objects.equals($$2, "minecraft:trapped_chest")) {
                    this.chestIds.add($$0);
                }
            }
            return this.chestIds.isEmpty();
        }

        public boolean isTrappedChest(int int0) {
            return this.chestIds.contains(int0);
        }
    }
}