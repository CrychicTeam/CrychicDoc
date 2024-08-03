package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.util.datafix.PackedBitStorage;

public class LeavesFix extends DataFix {

    private static final int NORTH_WEST_MASK = 128;

    private static final int WEST_MASK = 64;

    private static final int SOUTH_WEST_MASK = 32;

    private static final int SOUTH_MASK = 16;

    private static final int SOUTH_EAST_MASK = 8;

    private static final int EAST_MASK = 4;

    private static final int NORTH_EAST_MASK = 2;

    private static final int NORTH_MASK = 1;

    private static final int[][] DIRECTIONS = new int[][] { { -1, 0, 0 }, { 1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 } };

    private static final int DECAY_DISTANCE = 7;

    private static final int SIZE_BITS = 12;

    private static final int SIZE = 4096;

    static final Object2IntMap<String> LEAVES = (Object2IntMap<String>) DataFixUtils.make(new Object2IntOpenHashMap(), p_16235_ -> {
        p_16235_.put("minecraft:acacia_leaves", 0);
        p_16235_.put("minecraft:birch_leaves", 1);
        p_16235_.put("minecraft:dark_oak_leaves", 2);
        p_16235_.put("minecraft:jungle_leaves", 3);
        p_16235_.put("minecraft:oak_leaves", 4);
        p_16235_.put("minecraft:spruce_leaves", 5);
    });

    static final Set<String> LOGS = ImmutableSet.of("minecraft:acacia_bark", "minecraft:birch_bark", "minecraft:dark_oak_bark", "minecraft:jungle_bark", "minecraft:oak_bark", "minecraft:spruce_bark", new String[] { "minecraft:acacia_log", "minecraft:birch_log", "minecraft:dark_oak_log", "minecraft:jungle_log", "minecraft:oak_log", "minecraft:spruce_log", "minecraft:stripped_acacia_log", "minecraft:stripped_birch_log", "minecraft:stripped_dark_oak_log", "minecraft:stripped_jungle_log", "minecraft:stripped_oak_log", "minecraft:stripped_spruce_log" });

    public LeavesFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    protected TypeRewriteRule makeRule() {
        Type<?> $$0 = this.getInputSchema().getType(References.CHUNK);
        OpticFinder<?> $$1 = $$0.findField("Level");
        OpticFinder<?> $$2 = $$1.type().findField("Sections");
        Type<?> $$3 = $$2.type();
        if (!($$3 instanceof ListType)) {
            throw new IllegalStateException("Expecting sections to be a list.");
        } else {
            Type<?> $$4 = ((ListType) $$3).getElement();
            OpticFinder<?> $$5 = DSL.typeFinder($$4);
            return this.fixTypeEverywhereTyped("Leaves fix", $$0, p_16220_ -> p_16220_.updateTyped($$1, p_145461_ -> {
                int[] $$3x = new int[] { 0 };
                Typed<?> $$4x = p_145461_.updateTyped($$2, p_145465_ -> {
                    Int2ObjectMap<LeavesFix.LeavesSection> $$3xx = new Int2ObjectOpenHashMap((Map) p_145465_.getAllTyped($$5).stream().map(p_145467_ -> new LeavesFix.LeavesSection(p_145467_, this.getInputSchema())).collect(Collectors.toMap(LeavesFix.Section::m_16301_, p_145457_ -> p_145457_)));
                    if ($$3xx.values().stream().allMatch(LeavesFix.Section::m_16298_)) {
                        return p_145465_;
                    } else {
                        List<IntSet> $$4xx = Lists.newArrayList();
                        for (int $$5x = 0; $$5x < 7; $$5x++) {
                            $$4xx.add(new IntOpenHashSet());
                        }
                        ObjectIterator var25 = $$3xx.values().iterator();
                        while (var25.hasNext()) {
                            LeavesFix.LeavesSection $$6 = (LeavesFix.LeavesSection) var25.next();
                            if (!$$6.m_16298_()) {
                                for (int $$7 = 0; $$7 < 4096; $$7++) {
                                    int $$8 = $$6.m_16302_($$7);
                                    if ($$6.isLog($$8)) {
                                        ((IntSet) $$4xx.get(0)).add($$6.m_16301_() << 12 | $$7);
                                    } else if ($$6.isLeaf($$8)) {
                                        int $$9 = this.getX($$7);
                                        int $$10 = this.getZ($$7);
                                        $$3x[0] |= getSideMask($$9 == 0, $$9 == 15, $$10 == 0, $$10 == 15);
                                    }
                                }
                            }
                        }
                        for (int $$11 = 1; $$11 < 7; $$11++) {
                            IntSet $$12 = (IntSet) $$4xx.get($$11 - 1);
                            IntSet $$13 = (IntSet) $$4xx.get($$11);
                            IntIterator $$14 = $$12.iterator();
                            while ($$14.hasNext()) {
                                int $$15 = $$14.nextInt();
                                int $$16 = this.getX($$15);
                                int $$17 = this.getY($$15);
                                int $$18 = this.getZ($$15);
                                for (int[] $$19 : DIRECTIONS) {
                                    int $$20 = $$16 + $$19[0];
                                    int $$21 = $$17 + $$19[1];
                                    int $$22 = $$18 + $$19[2];
                                    if ($$20 >= 0 && $$20 <= 15 && $$22 >= 0 && $$22 <= 15 && $$21 >= 0 && $$21 <= 255) {
                                        LeavesFix.LeavesSection $$23 = (LeavesFix.LeavesSection) $$3xx.get($$21 >> 4);
                                        if ($$23 != null && !$$23.m_16298_()) {
                                            int $$24 = getIndex($$20, $$21 & 15, $$22);
                                            int $$25 = $$23.m_16302_($$24);
                                            if ($$23.isLeaf($$25)) {
                                                int $$26 = $$23.getDistance($$25);
                                                if ($$26 > $$11) {
                                                    $$23.setDistance($$24, $$25, $$11);
                                                    $$13.add(getIndex($$20, $$21, $$22));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return p_145465_.updateTyped($$5, p_145470_ -> ((LeavesFix.LeavesSection) $$3xx.get(((Dynamic) p_145470_.get(DSL.remainderFinder())).get("Y").asInt(0))).m_16288_(p_145470_));
                    }
                });
                if ($$3x[0] != 0) {
                    $$4x = $$4x.update(DSL.remainderFinder(), p_145473_ -> {
                        Dynamic<?> $$2x = (Dynamic<?>) DataFixUtils.orElse(p_145473_.get("UpgradeData").result(), p_145473_.emptyMap());
                        return p_145473_.set("UpgradeData", $$2x.set("Sides", p_145473_.createByte((byte) ($$2x.get("Sides").asByte((byte) 0) | $$3x[0]))));
                    });
                }
                return $$4x;
            }));
        }
    }

    public static int getIndex(int int0, int int1, int int2) {
        return int1 << 8 | int2 << 4 | int0;
    }

    private int getX(int int0) {
        return int0 & 15;
    }

    private int getY(int int0) {
        return int0 >> 8 & 0xFF;
    }

    private int getZ(int int0) {
        return int0 >> 4 & 15;
    }

    public static int getSideMask(boolean boolean0, boolean boolean1, boolean boolean2, boolean boolean3) {
        int $$4 = 0;
        if (boolean2) {
            if (boolean1) {
                $$4 |= 2;
            } else if (boolean0) {
                $$4 |= 128;
            } else {
                $$4 |= 1;
            }
        } else if (boolean3) {
            if (boolean0) {
                $$4 |= 32;
            } else if (boolean1) {
                $$4 |= 8;
            } else {
                $$4 |= 16;
            }
        } else if (boolean1) {
            $$4 |= 4;
        } else if (boolean0) {
            $$4 |= 64;
        }
        return $$4;
    }

    public static final class LeavesSection extends LeavesFix.Section {

        private static final String PERSISTENT = "persistent";

        private static final String DECAYABLE = "decayable";

        private static final String DISTANCE = "distance";

        @Nullable
        private IntSet leaveIds;

        @Nullable
        private IntSet logIds;

        @Nullable
        private Int2IntMap stateToIdMap;

        public LeavesSection(Typed<?> typed0, Schema schema1) {
            super(typed0, schema1);
        }

        @Override
        protected boolean skippable() {
            this.leaveIds = new IntOpenHashSet();
            this.logIds = new IntOpenHashSet();
            this.stateToIdMap = new Int2IntOpenHashMap();
            for (int $$0 = 0; $$0 < this.f_16281_.size(); $$0++) {
                Dynamic<?> $$1 = (Dynamic<?>) this.f_16281_.get($$0);
                String $$2 = $$1.get("Name").asString("");
                if (LeavesFix.LEAVES.containsKey($$2)) {
                    boolean $$3 = Objects.equals($$1.get("Properties").get("decayable").asString(""), "false");
                    this.leaveIds.add($$0);
                    this.stateToIdMap.put(this.m_16292_($$2, $$3, 7), $$0);
                    this.f_16281_.set($$0, this.makeLeafTag($$1, $$2, $$3, 7));
                }
                if (LeavesFix.LOGS.contains($$2)) {
                    this.logIds.add($$0);
                }
            }
            return this.leaveIds.isEmpty() && this.logIds.isEmpty();
        }

        private Dynamic<?> makeLeafTag(Dynamic<?> dynamic0, String string1, boolean boolean2, int int3) {
            Dynamic<?> $$4 = dynamic0.emptyMap();
            $$4 = $$4.set("persistent", $$4.createString(boolean2 ? "true" : "false"));
            $$4 = $$4.set("distance", $$4.createString(Integer.toString(int3)));
            Dynamic<?> $$5 = dynamic0.emptyMap();
            $$5 = $$5.set("Properties", $$4);
            return $$5.set("Name", $$5.createString(string1));
        }

        public boolean isLog(int int0) {
            return this.logIds.contains(int0);
        }

        public boolean isLeaf(int int0) {
            return this.leaveIds.contains(int0);
        }

        int getDistance(int int0) {
            return this.isLog(int0) ? 0 : Integer.parseInt(((Dynamic) this.f_16281_.get(int0)).get("Properties").get("distance").asString(""));
        }

        void setDistance(int int0, int int1, int int2) {
            Dynamic<?> $$3 = (Dynamic<?>) this.f_16281_.get(int1);
            String $$4 = $$3.get("Name").asString("");
            boolean $$5 = Objects.equals($$3.get("Properties").get("persistent").asString(""), "true");
            int $$6 = this.m_16292_($$4, $$5, int2);
            if (!this.stateToIdMap.containsKey($$6)) {
                int $$7 = this.f_16281_.size();
                this.leaveIds.add($$7);
                this.stateToIdMap.put($$6, $$7);
                this.f_16281_.add(this.makeLeafTag($$3, $$4, $$5, int2));
            }
            int $$8 = this.stateToIdMap.get($$6);
            if (1 << this.f_16283_.getBits() <= $$8) {
                PackedBitStorage $$9 = new PackedBitStorage(this.f_16283_.getBits() + 1, 4096);
                for (int $$10 = 0; $$10 < 4096; $$10++) {
                    $$9.set($$10, this.f_16283_.get($$10));
                }
                this.f_16283_ = $$9;
            }
            this.f_16283_.set(int0, $$8);
        }
    }

    public abstract static class Section {

        protected static final String BLOCK_STATES_TAG = "BlockStates";

        protected static final String NAME_TAG = "Name";

        protected static final String PROPERTIES_TAG = "Properties";

        private final Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());

        protected final OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", DSL.list(this.blockStateType));

        protected final List<Dynamic<?>> palette;

        protected final int index;

        @Nullable
        protected PackedBitStorage storage;

        public Section(Typed<?> typed0, Schema schema1) {
            if (!Objects.equals(schema1.getType(References.BLOCK_STATE), this.blockStateType)) {
                throw new IllegalStateException("Block state type is not what was expected.");
            } else {
                Optional<List<Pair<String, Dynamic<?>>>> $$2 = typed0.getOptional(this.paletteFinder);
                this.palette = (List<Dynamic<?>>) $$2.map(p_16297_ -> (List) p_16297_.stream().map(Pair::getSecond).collect(Collectors.toList())).orElse(ImmutableList.of());
                Dynamic<?> $$3 = (Dynamic<?>) typed0.get(DSL.remainderFinder());
                this.index = $$3.get("Y").asInt(0);
                this.readStorage($$3);
            }
        }

        protected void readStorage(Dynamic<?> dynamic0) {
            if (this.skippable()) {
                this.storage = null;
            } else {
                long[] $$1 = dynamic0.get("BlockStates").asLongStream().toArray();
                int $$2 = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
                this.storage = new PackedBitStorage($$2, 4096, $$1);
            }
        }

        public Typed<?> write(Typed<?> typed0) {
            return this.isSkippable() ? typed0 : typed0.update(DSL.remainderFinder(), p_16305_ -> p_16305_.set("BlockStates", p_16305_.createLongList(Arrays.stream(this.storage.getRaw())))).set(this.paletteFinder, (List) this.palette.stream().map(p_16300_ -> Pair.of(References.BLOCK_STATE.typeName(), p_16300_)).collect(Collectors.toList()));
        }

        public boolean isSkippable() {
            return this.storage == null;
        }

        public int getBlock(int int0) {
            return this.storage.get(int0);
        }

        protected int getStateId(String string0, boolean boolean1, int int2) {
            return LeavesFix.LEAVES.get(string0) << 5 | (boolean1 ? 16 : 0) | int2;
        }

        int getIndex() {
            return this.index;
        }

        protected abstract boolean skippable();
    }
}