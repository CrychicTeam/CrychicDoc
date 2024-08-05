package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.moonlight.api.map.CustomMapData;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.WeatheredMap;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EmptyMapItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

public class SliceMapItem extends EmptyMapItem {

    private static final String DEPTH_LOCK_KEY = "depth_lock";

    public static final CustomMapData.Type<SliceMapItem.DepthMapData> DEPTH_DATA_KEY = MapDataRegistry.registerCustomMapSavedData(Supplementaries.res("depth_lock"), SliceMapItem.DepthMapData::new);

    private static final RandomSource RAND = RandomSource.createNewThreadLocalInstance();

    public SliceMapItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.m_21120_(usedHand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(itemStack);
        } else {
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.m_9236_().playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0F, 1.0F);
            int slice = (int) player.m_20186_() + 1;
            ItemStack itemStack2 = createSliced(level, player.m_146903_(), player.m_146907_(), (byte) 0, true, false, slice);
            if (itemStack.isEmpty()) {
                return InteractionResultHolder.consume(itemStack2);
            } else {
                if (!player.getInventory().add(itemStack2.copy())) {
                    player.drop(itemStack2, false);
                }
                return InteractionResultHolder.consume(itemStack);
            }
        }
    }

    public static ItemStack createSliced(Level level, int x, int z, byte scale, boolean trackingPosition, boolean unlimitedTracking, int slice) {
        ItemStack itemStack = new ItemStack(Items.FILLED_MAP);
        MapItemSavedData data = MapItemSavedData.createFresh((double) x, (double) z, scale, trackingPosition, unlimitedTracking, level.dimension());
        SliceMapItem.DepthMapData instance = DEPTH_DATA_KEY.get(data);
        instance.set(slice);
        instance.setDirty(data, CustomMapData.SimpleDirtyCounter::markDirty);
        int mapId = level.getFreeMapId();
        level.setMapData(MapItem.makeKey(mapId), data);
        itemStack.getOrCreateTag().putInt("map", mapId);
        return itemStack;
    }

    public static void init() {
    }

    public static int getMapHeight(MapItemSavedData data) {
        SliceMapItem.DepthMapData depth = DEPTH_DATA_KEY.get(data);
        return depth.height == null ? Integer.MAX_VALUE : depth.height;
    }

    public static MapColor getCutoffColor(BlockPos pos, BlockGetter level) {
        return (pos.m_123341_() + pos.m_123343_()) % 2 == 0 ? MapColor.NONE : WeatheredMap.ANTIQUE_LIGHT;
    }

    public static double getRangeMultiplier() {
        return (Double) CommonConfigs.Tools.SLICE_MAP_RANGE.get();
    }

    public static boolean canPlayerSee(int targetY, Entity entity) {
        Level level = entity.level();
        int py = entity.getBlockY();
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        int spread = 3;
        p.set(entity.blockPosition().offset(RAND.nextInt(spread) - RAND.nextInt(spread), 0, RAND.nextInt(spread) - RAND.nextInt(spread)));
        int direction = Integer.compare(targetY, py);
        while (p.m_123342_() != targetY) {
            if (level.getBlockState(p).m_284242_(level, p) != MapColor.NONE) {
                return false;
            }
            p.setY(p.m_123342_() + direction);
        }
        return true;
    }

    private static class DepthMapData implements CustomMapData<CustomMapData.SimpleDirtyCounter> {

        private Integer height = null;

        @Override
        public void load(CompoundTag tag) {
            if (tag.contains("depth_lock")) {
                this.height = tag.getInt("depth_lock");
                if (this.height == Integer.MAX_VALUE) {
                    this.height = null;
                }
            } else {
                this.height = null;
            }
        }

        @Override
        public void loadUpdateTag(CompoundTag tag) {
            if (tag.contains("depth_lock")) {
                this.height = tag.getInt("depth_lock");
                if (this.height == Integer.MAX_VALUE) {
                    this.height = null;
                }
            }
        }

        @Override
        public void save(CompoundTag tag) {
            if (this.height != null) {
                tag.putInt("depth_lock", this.height);
            }
        }

        public void saveToUpdateTag(CompoundTag tag, CustomMapData.SimpleDirtyCounter dirtyCounter) {
            tag.putInt("depth_lock", this.height == null ? Integer.MAX_VALUE : this.height);
        }

        @Override
        public CustomMapData.Type<SliceMapItem.DepthMapData> getType() {
            return SliceMapItem.DEPTH_DATA_KEY;
        }

        @Nullable
        @Override
        public Component onItemTooltip(MapItemSavedData data, ItemStack stack) {
            return this.height == null ? null : Component.translatable("filled_map.sliced.tooltip", this.height).withStyle(ChatFormatting.GRAY);
        }

        public void set(int slice) {
            this.height = slice;
        }

        public CustomMapData.SimpleDirtyCounter createDirtyCounter() {
            return new CustomMapData.SimpleDirtyCounter();
        }
    }
}