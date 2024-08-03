package net.minecraft.world.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapItem extends ComplexItem {

    public static final int IMAGE_WIDTH = 128;

    public static final int IMAGE_HEIGHT = 128;

    private static final int DEFAULT_MAP_COLOR = -12173266;

    private static final String TAG_MAP = "map";

    public static final String MAP_SCALE_TAG = "map_scale_direction";

    public static final String MAP_LOCK_TAG = "map_to_lock";

    public MapItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    public static ItemStack create(Level level0, int int1, int int2, byte byte3, boolean boolean4, boolean boolean5) {
        ItemStack $$6 = new ItemStack(Items.FILLED_MAP);
        createAndStoreSavedData($$6, level0, int1, int2, byte3, boolean4, boolean5, level0.dimension());
        return $$6;
    }

    @Nullable
    public static MapItemSavedData getSavedData(@Nullable Integer integer0, Level level1) {
        return integer0 == null ? null : level1.getMapData(makeKey(integer0));
    }

    @Nullable
    public static MapItemSavedData getSavedData(ItemStack itemStack0, Level level1) {
        Integer $$2 = getMapId(itemStack0);
        return getSavedData($$2, level1);
    }

    @Nullable
    public static Integer getMapId(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        return $$1 != null && $$1.contains("map", 99) ? $$1.getInt("map") : null;
    }

    private static int createNewSavedData(Level level0, int int1, int int2, int int3, boolean boolean4, boolean boolean5, ResourceKey<Level> resourceKeyLevel6) {
        MapItemSavedData $$7 = MapItemSavedData.createFresh((double) int1, (double) int2, (byte) int3, boolean4, boolean5, resourceKeyLevel6);
        int $$8 = level0.getFreeMapId();
        level0.setMapData(makeKey($$8), $$7);
        return $$8;
    }

    private static void storeMapData(ItemStack itemStack0, int int1) {
        itemStack0.getOrCreateTag().putInt("map", int1);
    }

    private static void createAndStoreSavedData(ItemStack itemStack0, Level level1, int int2, int int3, int int4, boolean boolean5, boolean boolean6, ResourceKey<Level> resourceKeyLevel7) {
        int $$8 = createNewSavedData(level1, int2, int3, int4, boolean5, boolean6, resourceKeyLevel7);
        storeMapData(itemStack0, $$8);
    }

    public static String makeKey(int int0) {
        return "map_" + int0;
    }

    public void update(Level level0, Entity entity1, MapItemSavedData mapItemSavedData2) {
        if (level0.dimension() == mapItemSavedData2.dimension && entity1 instanceof Player) {
            int $$3 = 1 << mapItemSavedData2.scale;
            int $$4 = mapItemSavedData2.centerX;
            int $$5 = mapItemSavedData2.centerZ;
            int $$6 = Mth.floor(entity1.getX() - (double) $$4) / $$3 + 64;
            int $$7 = Mth.floor(entity1.getZ() - (double) $$5) / $$3 + 64;
            int $$8 = 128 / $$3;
            if (level0.dimensionType().hasCeiling()) {
                $$8 /= 2;
            }
            MapItemSavedData.HoldingPlayer $$9 = mapItemSavedData2.getHoldingPlayer((Player) entity1);
            $$9.step++;
            BlockPos.MutableBlockPos $$10 = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos $$11 = new BlockPos.MutableBlockPos();
            boolean $$12 = false;
            for (int $$13 = $$6 - $$8 + 1; $$13 < $$6 + $$8; $$13++) {
                if (($$13 & 15) == ($$9.step & 15) || $$12) {
                    $$12 = false;
                    double $$14 = 0.0;
                    for (int $$15 = $$7 - $$8 - 1; $$15 < $$7 + $$8; $$15++) {
                        if ($$13 >= 0 && $$15 >= -1 && $$13 < 128 && $$15 < 128) {
                            int $$16 = Mth.square($$13 - $$6) + Mth.square($$15 - $$7);
                            boolean $$17 = $$16 > ($$8 - 2) * ($$8 - 2);
                            int $$18 = ($$4 / $$3 + $$13 - 64) * $$3;
                            int $$19 = ($$5 / $$3 + $$15 - 64) * $$3;
                            Multiset<MapColor> $$20 = LinkedHashMultiset.create();
                            LevelChunk $$21 = level0.getChunk(SectionPos.blockToSectionCoord($$18), SectionPos.blockToSectionCoord($$19));
                            if (!$$21.isEmpty()) {
                                int $$22 = 0;
                                double $$23 = 0.0;
                                if (level0.dimensionType().hasCeiling()) {
                                    int $$24 = $$18 + $$19 * 231871;
                                    $$24 = $$24 * $$24 * 31287121 + $$24 * 11;
                                    if (($$24 >> 20 & 1) == 0) {
                                        $$20.add(Blocks.DIRT.defaultBlockState().m_284242_(level0, BlockPos.ZERO), 10);
                                    } else {
                                        $$20.add(Blocks.STONE.defaultBlockState().m_284242_(level0, BlockPos.ZERO), 100);
                                    }
                                    $$23 = 100.0;
                                } else {
                                    for (int $$25 = 0; $$25 < $$3; $$25++) {
                                        for (int $$26 = 0; $$26 < $$3; $$26++) {
                                            $$10.set($$18 + $$25, 0, $$19 + $$26);
                                            int $$27 = $$21.m_5885_(Heightmap.Types.WORLD_SURFACE, $$10.m_123341_(), $$10.m_123343_()) + 1;
                                            BlockState $$31;
                                            if ($$27 <= level0.m_141937_() + 1) {
                                                $$31 = Blocks.BEDROCK.defaultBlockState();
                                            } else {
                                                do {
                                                    $$10.setY(--$$27);
                                                    $$31 = $$21.getBlockState($$10);
                                                } while ($$31.m_284242_(level0, $$10) == MapColor.NONE && $$27 > level0.m_141937_());
                                                if ($$27 > level0.m_141937_() && !$$31.m_60819_().isEmpty()) {
                                                    int $$29 = $$27 - 1;
                                                    $$11.set($$10);
                                                    BlockState $$30;
                                                    do {
                                                        $$11.setY($$29--);
                                                        $$30 = $$21.getBlockState($$11);
                                                        $$22++;
                                                    } while ($$29 > level0.m_141937_() && !$$30.m_60819_().isEmpty());
                                                    $$31 = this.getCorrectStateForFluidBlock(level0, $$31, $$10);
                                                }
                                            }
                                            mapItemSavedData2.checkBanners(level0, $$10.m_123341_(), $$10.m_123343_());
                                            $$23 += (double) $$27 / (double) ($$3 * $$3);
                                            $$20.add($$31.m_284242_(level0, $$10));
                                        }
                                    }
                                }
                                $$22 /= $$3 * $$3;
                                MapColor $$32 = (MapColor) Iterables.getFirst(Multisets.copyHighestCountFirst($$20), MapColor.NONE);
                                MapColor.Brightness $$34;
                                if ($$32 == MapColor.WATER) {
                                    double $$33 = (double) $$22 * 0.1 + (double) ($$13 + $$15 & 1) * 0.2;
                                    if ($$33 < 0.5) {
                                        $$34 = MapColor.Brightness.HIGH;
                                    } else if ($$33 > 0.9) {
                                        $$34 = MapColor.Brightness.LOW;
                                    } else {
                                        $$34 = MapColor.Brightness.NORMAL;
                                    }
                                } else {
                                    double $$37 = ($$23 - $$14) * 4.0 / (double) ($$3 + 4) + ((double) ($$13 + $$15 & 1) - 0.5) * 0.4;
                                    if ($$37 > 0.6) {
                                        $$34 = MapColor.Brightness.HIGH;
                                    } else if ($$37 < -0.6) {
                                        $$34 = MapColor.Brightness.LOW;
                                    } else {
                                        $$34 = MapColor.Brightness.NORMAL;
                                    }
                                }
                                $$14 = $$23;
                                if ($$15 >= 0 && $$16 < $$8 * $$8 && (!$$17 || ($$13 + $$15 & 1) != 0)) {
                                    $$12 |= mapItemSavedData2.updateColor($$13, $$15, $$32.getPackedId($$34));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockState getCorrectStateForFluidBlock(Level level0, BlockState blockState1, BlockPos blockPos2) {
        FluidState $$3 = blockState1.m_60819_();
        return !$$3.isEmpty() && !blockState1.m_60783_(level0, blockPos2, Direction.UP) ? $$3.createLegacyBlock() : blockState1;
    }

    private static boolean isBiomeWatery(boolean[] boolean0, int int1, int int2) {
        return boolean0[int2 * 128 + int1];
    }

    public static void renderBiomePreviewMap(ServerLevel serverLevel0, ItemStack itemStack1) {
        MapItemSavedData $$2 = getSavedData(itemStack1, serverLevel0);
        if ($$2 != null) {
            if (serverLevel0.m_46472_() == $$2.dimension) {
                int $$3 = 1 << $$2.scale;
                int $$4 = $$2.centerX;
                int $$5 = $$2.centerZ;
                boolean[] $$6 = new boolean[16384];
                int $$7 = $$4 / $$3 - 64;
                int $$8 = $$5 / $$3 - 64;
                BlockPos.MutableBlockPos $$9 = new BlockPos.MutableBlockPos();
                for (int $$10 = 0; $$10 < 128; $$10++) {
                    for (int $$11 = 0; $$11 < 128; $$11++) {
                        Holder<Biome> $$12 = serverLevel0.m_204166_($$9.set(($$7 + $$11) * $$3, 0, ($$8 + $$10) * $$3));
                        $$6[$$10 * 128 + $$11] = $$12.is(BiomeTags.WATER_ON_MAP_OUTLINES);
                    }
                }
                for (int $$13 = 1; $$13 < 127; $$13++) {
                    for (int $$14 = 1; $$14 < 127; $$14++) {
                        int $$15 = 0;
                        for (int $$16 = -1; $$16 < 2; $$16++) {
                            for (int $$17 = -1; $$17 < 2; $$17++) {
                                if (($$16 != 0 || $$17 != 0) && isBiomeWatery($$6, $$13 + $$16, $$14 + $$17)) {
                                    $$15++;
                                }
                            }
                        }
                        MapColor.Brightness $$18 = MapColor.Brightness.LOWEST;
                        MapColor $$19 = MapColor.NONE;
                        if (isBiomeWatery($$6, $$13, $$14)) {
                            $$19 = MapColor.COLOR_ORANGE;
                            if ($$15 > 7 && $$14 % 2 == 0) {
                                switch(($$13 + (int) (Mth.sin((float) $$14 + 0.0F) * 7.0F)) / 8 % 5) {
                                    case 0:
                                    case 4:
                                        $$18 = MapColor.Brightness.LOW;
                                        break;
                                    case 1:
                                    case 3:
                                        $$18 = MapColor.Brightness.NORMAL;
                                        break;
                                    case 2:
                                        $$18 = MapColor.Brightness.HIGH;
                                }
                            } else if ($$15 > 7) {
                                $$19 = MapColor.NONE;
                            } else if ($$15 > 5) {
                                $$18 = MapColor.Brightness.NORMAL;
                            } else if ($$15 > 3) {
                                $$18 = MapColor.Brightness.LOW;
                            } else if ($$15 > 1) {
                                $$18 = MapColor.Brightness.LOW;
                            }
                        } else if ($$15 > 0) {
                            $$19 = MapColor.COLOR_BROWN;
                            if ($$15 > 3) {
                                $$18 = MapColor.Brightness.NORMAL;
                            } else {
                                $$18 = MapColor.Brightness.LOWEST;
                            }
                        }
                        if ($$19 != MapColor.NONE) {
                            $$2.setColor($$13, $$14, $$19.getPackedId($$18));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack0, Level level1, Entity entity2, int int3, boolean boolean4) {
        if (!level1.isClientSide) {
            MapItemSavedData $$5 = getSavedData(itemStack0, level1);
            if ($$5 != null) {
                if (entity2 instanceof Player $$6) {
                    $$5.tickCarriedBy($$6, itemStack0);
                }
                if (!$$5.locked && (boolean4 || entity2 instanceof Player && ((Player) entity2).m_21206_() == itemStack0)) {
                    this.update(level1, entity2, $$5);
                }
            }
        }
    }

    @Nullable
    @Override
    public Packet<?> getUpdatePacket(ItemStack itemStack0, Level level1, Player player2) {
        Integer $$3 = getMapId(itemStack0);
        MapItemSavedData $$4 = getSavedData($$3, level1);
        return $$4 != null ? $$4.getUpdatePacket($$3, player2) : null;
    }

    @Override
    public void onCraftedBy(ItemStack itemStack0, Level level1, Player player2) {
        CompoundTag $$3 = itemStack0.getTag();
        if ($$3 != null && $$3.contains("map_scale_direction", 99)) {
            scaleMap(itemStack0, level1, $$3.getInt("map_scale_direction"));
            $$3.remove("map_scale_direction");
        } else if ($$3 != null && $$3.contains("map_to_lock", 1) && $$3.getBoolean("map_to_lock")) {
            lockMap(level1, itemStack0);
            $$3.remove("map_to_lock");
        }
    }

    private static void scaleMap(ItemStack itemStack0, Level level1, int int2) {
        MapItemSavedData $$3 = getSavedData(itemStack0, level1);
        if ($$3 != null) {
            int $$4 = level1.getFreeMapId();
            level1.setMapData(makeKey($$4), $$3.scaled(int2));
            storeMapData(itemStack0, $$4);
        }
    }

    public static void lockMap(Level level0, ItemStack itemStack1) {
        MapItemSavedData $$2 = getSavedData(itemStack1, level0);
        if ($$2 != null) {
            int $$3 = level0.getFreeMapId();
            String $$4 = makeKey($$3);
            MapItemSavedData $$5 = $$2.locked();
            level0.setMapData($$4, $$5);
            storeMapData(itemStack1, $$3);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        Integer $$4 = getMapId(itemStack0);
        MapItemSavedData $$5 = level1 == null ? null : getSavedData($$4, level1);
        CompoundTag $$6 = itemStack0.getTag();
        boolean $$7;
        byte $$8;
        if ($$6 != null) {
            $$7 = $$6.getBoolean("map_to_lock");
            $$8 = $$6.getByte("map_scale_direction");
        } else {
            $$7 = false;
            $$8 = 0;
        }
        if ($$5 != null && ($$5.locked || $$7)) {
            listComponent2.add(Component.translatable("filled_map.locked", $$4).withStyle(ChatFormatting.GRAY));
        }
        if (tooltipFlag3.isAdvanced()) {
            if ($$5 != null) {
                if (!$$7 && $$8 == 0) {
                    listComponent2.add(Component.translatable("filled_map.id", $$4).withStyle(ChatFormatting.GRAY));
                }
                int $$11 = Math.min($$5.scale + $$8, 4);
                listComponent2.add(Component.translatable("filled_map.scale", 1 << $$11).withStyle(ChatFormatting.GRAY));
                listComponent2.add(Component.translatable("filled_map.level", $$11, 4).withStyle(ChatFormatting.GRAY));
            } else {
                listComponent2.add(Component.translatable("filled_map.unknown").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public static int getColor(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTagElement("display");
        if ($$1 != null && $$1.contains("MapColor", 99)) {
            int $$2 = $$1.getInt("MapColor");
            return 0xFF000000 | $$2 & 16777215;
        } else {
            return -12173266;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        BlockState $$1 = useOnContext0.getLevel().getBlockState(useOnContext0.getClickedPos());
        if ($$1.m_204336_(BlockTags.BANNERS)) {
            if (!useOnContext0.getLevel().isClientSide) {
                MapItemSavedData $$2 = getSavedData(useOnContext0.getItemInHand(), useOnContext0.getLevel());
                if ($$2 != null && !$$2.toggleBanner(useOnContext0.getLevel(), useOnContext0.getClickedPos())) {
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.sidedSuccess(useOnContext0.getLevel().isClientSide);
        } else {
            return super.m_6225_(useOnContext0);
        }
    }
}