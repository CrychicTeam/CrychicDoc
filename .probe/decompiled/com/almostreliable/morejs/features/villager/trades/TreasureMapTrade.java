package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import com.almostreliable.morejs.util.LevelUtils;
import com.almostreliable.morejs.util.ResourceOrTag;
import com.almostreliable.morejs.util.WeightedList;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class TreasureMapTrade extends TransformableTrade<TreasureMapTrade> {

    protected final MapPosInfo.Provider destinationPositionFunc;

    @Nullable
    protected Component displayName;

    protected MapDecoration.Type destinationType = MapDecoration.Type.RED_X;

    private boolean renderBiomePreviewMap = true;

    private byte mapViewScale = 2;

    public TreasureMapTrade(TradeItem[] inputs, MapPosInfo.Provider destinationPositionFunc) {
        super(inputs);
        this.destinationPositionFunc = destinationPositionFunc;
    }

    public static TreasureMapTrade forStructure(TradeItem[] input, WeightedList<Object> entries) {
        WeightedList<ResourceOrTag<Structure>> list = entries.map(o -> o == null ? null : ResourceOrTag.get(o.toString(), Registries.STRUCTURE));
        MapPosInfo.Provider func = (level, entity) -> {
            ResourceOrTag<Structure> roll = list.roll(level.f_46441_);
            BlockPos pos = LevelUtils.findStructure(entity.blockPosition(), level, roll, 100);
            return pos == null ? null : new MapPosInfo(pos, roll.getName());
        };
        return new TreasureMapTrade(input, func);
    }

    public static TreasureMapTrade forBiome(TradeItem[] input, WeightedList<Object> entries) {
        WeightedList<ResourceOrTag<Biome>> list = entries.map(o -> o == null ? null : ResourceOrTag.get(o.toString(), Registries.BIOME));
        MapPosInfo.Provider func = (level, entity) -> {
            ResourceOrTag<Biome> roll = list.roll(level.f_46441_);
            BlockPos pos = LevelUtils.findBiome(entity.blockPosition(), level, roll, 250);
            return pos == null ? null : new MapPosInfo(pos, roll.getName());
        };
        return new TreasureMapTrade(input, func);
    }

    public TreasureMapTrade displayName(Component name) {
        this.displayName = name;
        return this;
    }

    public TreasureMapTrade marker(MapDecoration.Type type) {
        this.destinationType = type;
        return this;
    }

    public TreasureMapTrade noPreview() {
        this.renderBiomePreviewMap = false;
        return this;
    }

    public TreasureMapTrade scale(byte scale) {
        this.mapViewScale = scale;
        return this;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity trader, RandomSource random) {
        if (trader.level() instanceof ServerLevel level) {
            MapPosInfo info = this.destinationPositionFunc.apply(level, trader);
            if (info == null) {
                return null;
            } else {
                ItemStack map = MapItem.create(level, info.pos().m_123341_(), info.pos().m_123343_(), this.mapViewScale, true, true);
                if (this.renderBiomePreviewMap) {
                    MapItem.renderBiomePreviewMap(level, map);
                }
                MapItemSavedData.addTargetDecoration(map, info.pos(), "+", this.destinationType);
                map.setHoverName(this.displayName == null ? info.name() : this.displayName);
                return this.createOffer(map, random);
            }
        } else {
            return null;
        }
    }
}