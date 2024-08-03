package com.xiaohunao.createheatjs;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.TriPredicate;
import org.apache.commons.compress.utils.Lists;

public class HeatData {

    private final String name;

    private final int prior;

    private final int color;

    private Map<Pair<String, String>, TriPredicate<Level, BlockPos, BlockState>> heatSource = Maps.newHashMap();

    private boolean jeiTip = false;

    private HeatCondition heatCondition;

    private BlazeBurnerBlock.HeatLevel heatLevel;

    private HeatData(String name, int prior, int color) {
        this.name = name;
        this.prior = prior;
        this.color = color;
    }

    public static HeatData getHeatData(String heatLevel) {
        if (heatLevel.equals(HeatCondition.NONE.toString())) {
            return (HeatData) CreateHeatJS.heatDataMap.get(BlazeBurnerBlock.HeatLevel.NONE.toString());
        } else if (heatLevel.equals(HeatCondition.HEATED.toString())) {
            return (HeatData) CreateHeatJS.heatDataMap.get(BlazeBurnerBlock.HeatLevel.KINDLED.toString());
        } else {
            return heatLevel.equals(HeatCondition.SUPERHEATED.toString()) ? (HeatData) CreateHeatJS.heatDataMap.get(BlazeBurnerBlock.HeatLevel.SEETHING.toString()) : (HeatData) CreateHeatJS.heatDataMap.get(heatLevel);
        }
    }

    public static Collection<HeatData> getHeatDataByPriority(int prior) {
        Collection<HeatData> heatDataList = Lists.newArrayList();
        CreateHeatJS.heatDataMap.forEach((level, heatData) -> {
            if (heatData.getPrior() >= prior) {
                heatDataList.add(heatData);
            }
        });
        return heatDataList.stream().sorted(Comparator.comparingInt(HeatData::getPrior)).toList();
    }

    public String getName() {
        return this.name;
    }

    public int getPrior() {
        return this.prior;
    }

    public int getColor() {
        return this.color;
    }

    public Map<Pair<String, String>, TriPredicate<Level, BlockPos, BlockState>> getHeatSource() {
        return this.heatSource;
    }

    public TriPredicate<Level, BlockPos, BlockState> getHeatSource(String blockState) {
        return (TriPredicate<Level, BlockPos, BlockState>) this.heatSource.get(blockState);
    }

    public HeatData setHeatSource(Map<Pair<String, String>, TriPredicate<Level, BlockPos, BlockState>> blockStates) {
        this.heatSource = blockStates;
        return this;
    }

    public HeatData addHeatSource(String block, String jeiBlockStack, TriPredicate<Level, BlockPos, BlockState> predicate) {
        this.heatSource.put(new Pair(block, jeiBlockStack), predicate);
        return this;
    }

    public HeatData addHeatSource(String block, TriPredicate<Level, BlockPos, BlockState> predicate) {
        this.addHeatSource(block, block, predicate);
        return this;
    }

    public HeatData addHeatSource(String block, String jeiBlockStack) {
        this.addHeatSource(block, jeiBlockStack, (level, pos, blockState) -> true);
        return this;
    }

    public HeatData addHeatSource(String block) {
        this.addHeatSource(block, block);
        return this;
    }

    public boolean isJeiTip() {
        return this.jeiTip;
    }

    public HeatData setJeiTip(boolean jeiTip) {
        this.jeiTip = jeiTip;
        return this;
    }

    public HeatData srtJeiTip(boolean jeiTip) {
        this.jeiTip = jeiTip;
        return this;
    }

    public BlazeBurnerBlock.HeatLevel getHeatLevel() {
        return this.heatLevel;
    }

    public HeatData setHeatLevel(BlazeBurnerBlock.HeatLevel heatLevel) {
        this.heatLevel = heatLevel;
        return this;
    }

    public HeatCondition getHeatCondition() {
        return this.heatCondition;
    }

    public HeatData setHeatCondition(HeatCondition heatCondition) {
        this.heatCondition = heatCondition;
        return this;
    }

    public static class Builder {

        private final HeatData heatData;

        public Builder(String name, int prior, int color) {
            this.heatData = new HeatData(name, prior, color);
        }

        public HeatData register() {
            CreateHeatJS.heatDataMap.put(this.heatData.name, this.heatData);
            return this.heatData;
        }

        public HeatData.Builder addHeatSource(String block, String jeiBlockStack, TriPredicate<Level, BlockPos, BlockState> predicate) {
            this.heatData.heatSource.put(new Pair(block, jeiBlockStack), predicate);
            return this;
        }

        public HeatData.Builder addHeatSource(String block, TriPredicate<Level, BlockPos, BlockState> predicate) {
            this.addHeatSource(block, block, predicate);
            return this;
        }

        public HeatData.Builder addHeatSource(String block) {
            this.addHeatSource(block, block, (level, pos, blockState) -> true);
            return this;
        }

        public HeatData.Builder jeiTip() {
            this.heatData.setJeiTip(true);
            return this;
        }
    }
}