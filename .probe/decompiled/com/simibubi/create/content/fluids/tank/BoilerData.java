package com.simibubi.create.content.fluids.tank;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlockEntity;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class BoilerData {

    static final int SAMPLE_RATE = 5;

    private static final int waterSupplyPerLevel = 10;

    private static final float passiveEngineEfficiency = 0.125F;

    int gatheredSupply;

    float[] supplyOverTime = new float[10];

    int ticksUntilNextSample;

    int currentIndex;

    public boolean needsHeatLevelUpdate;

    public boolean passiveHeat;

    public int activeHeat;

    public float waterSupply;

    public int attachedEngines;

    public int attachedWhistles;

    private int maxHeatForSize = 0;

    private int maxHeatForWater = 0;

    private int minValue = 0;

    private int maxValue = 0;

    public LerpedFloat gauge = LerpedFloat.linear();

    public void tick(FluidTankBlockEntity controller) {
        if (this.isActive()) {
            if (controller.m_58904_().isClientSide) {
                this.gauge.tickChaser();
                float current = this.gauge.getValue(1.0F);
                if (current > 1.0F && Create.RANDOM.nextFloat() < 0.5F) {
                    this.gauge.setValueNoUpdate((double) (current + Math.min(-(current - 1.0F) * Create.RANDOM.nextFloat(), 0.0F)));
                }
            } else {
                if (this.needsHeatLevelUpdate && this.updateTemperature(controller)) {
                    controller.notifyUpdate();
                }
                this.ticksUntilNextSample--;
                if (this.ticksUntilNextSample <= 0) {
                    int capacity = controller.tankInventory.getCapacity();
                    if (capacity != 0) {
                        this.ticksUntilNextSample = 5;
                        this.supplyOverTime[this.currentIndex] = (float) this.gatheredSupply / 5.0F;
                        this.waterSupply = Math.max(this.waterSupply, this.supplyOverTime[this.currentIndex]);
                        this.currentIndex = (this.currentIndex + 1) % this.supplyOverTime.length;
                        this.gatheredSupply = 0;
                        if (this.currentIndex == 0) {
                            this.waterSupply = 0.0F;
                            for (float i : this.supplyOverTime) {
                                this.waterSupply = Math.max(i, this.waterSupply);
                            }
                        }
                        if (controller instanceof CreativeFluidTankBlockEntity) {
                            this.waterSupply = 200.0F;
                        }
                        if (this.getActualHeat(controller.getTotalTankSize()) == 18) {
                            controller.award(AllAdvancements.STEAM_ENGINE_MAXED);
                        }
                        controller.notifyUpdate();
                    }
                }
            }
        }
    }

    public int getTheoreticalHeatLevel() {
        return this.activeHeat;
    }

    public int getMaxHeatLevelForBoilerSize(int boilerSize) {
        return Math.min(18, boilerSize / 4);
    }

    public int getMaxHeatLevelForWaterSupply() {
        return Math.min(18, Mth.ceil(this.waterSupply) / 10);
    }

    public boolean isPassive() {
        return this.passiveHeat && this.maxHeatForSize > 0 && this.maxHeatForWater > 0;
    }

    public boolean isPassive(int boilerSize) {
        this.calcMinMaxForSize(boilerSize);
        return this.isPassive();
    }

    public float getEngineEfficiency(int boilerSize) {
        if (this.isPassive(boilerSize)) {
            return 0.125F / (float) this.attachedEngines;
        } else if (this.activeHeat == 0) {
            return 0.0F;
        } else {
            int actualHeat = this.getActualHeat(boilerSize);
            return this.attachedEngines <= actualHeat ? 1.0F : (float) actualHeat / (float) this.attachedEngines;
        }
    }

    private int getActualHeat(int boilerSize) {
        int forBoilerSize = this.getMaxHeatLevelForBoilerSize(boilerSize);
        int forWaterSupply = this.getMaxHeatLevelForWaterSupply();
        return Math.min(this.activeHeat, Math.min(forWaterSupply, forBoilerSize));
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, int boilerSize) {
        if (!this.isActive()) {
            return false;
        } else {
            Component indent = Components.literal("    ");
            Component indent2 = Components.literal("     ");
            this.calcMinMaxForSize(boilerSize);
            tooltip.add(indent.plainCopy().append(Lang.translateDirect("boiler.status", this.getHeatLevelTextComponent().withStyle(ChatFormatting.GREEN))));
            tooltip.add(indent2.plainCopy().append(this.getSizeComponent(true, false)));
            tooltip.add(indent2.plainCopy().append(this.getWaterComponent(true, false)));
            tooltip.add(indent2.plainCopy().append(this.getHeatComponent(true, false)));
            if (this.attachedEngines == 0) {
                return true;
            } else {
                int boilerLevel = Math.min(this.activeHeat, Math.min(this.maxHeatForWater, this.maxHeatForSize));
                double totalSU = (double) (this.getEngineEfficiency(boilerSize) * 16.0F * (float) Math.max(boilerLevel, this.attachedEngines)) * BlockStressValues.getCapacity((Block) AllBlocks.STEAM_ENGINE.get());
                tooltip.add(Components.immutableEmpty());
                if (this.attachedEngines > 0 && this.maxHeatForSize > 0 && this.maxHeatForWater == 0 && (this.passiveHeat ? 1 : this.activeHeat) > 0) {
                    Lang.translate("boiler.water_input_rate").style(ChatFormatting.GRAY).forGoggles(tooltip);
                    Lang.number((double) this.waterSupply).style(ChatFormatting.BLUE).add(Lang.translate("generic.unit.millibuckets")).add(Lang.text(" / ").style(ChatFormatting.GRAY)).add(Lang.translate("boiler.per_tick", Lang.number(10.0).add(Lang.translate("generic.unit.millibuckets"))).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                    return true;
                } else {
                    Lang.translate("tooltip.capacityProvided").style(ChatFormatting.GRAY).forGoggles(tooltip);
                    Lang.number(totalSU).translate("generic.unit.stress").style(ChatFormatting.AQUA).space().add((this.attachedEngines == 1 ? Lang.translate("boiler.via_one_engine") : Lang.translate("boiler.via_engines", this.attachedEngines)).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                    return true;
                }
            }
        }
    }

    public void calcMinMaxForSize(int boilerSize) {
        this.maxHeatForSize = this.getMaxHeatLevelForBoilerSize(boilerSize);
        this.maxHeatForWater = this.getMaxHeatLevelForWaterSupply();
        this.minValue = Math.min(this.passiveHeat ? 1 : this.activeHeat, Math.min(this.maxHeatForWater, this.maxHeatForSize));
        this.maxValue = Math.max(this.passiveHeat ? 1 : this.activeHeat, Math.max(this.maxHeatForWater, this.maxHeatForSize));
    }

    @NotNull
    public MutableComponent getHeatLevelTextComponent() {
        int boilerLevel = Math.min(this.activeHeat, Math.min(this.maxHeatForWater, this.maxHeatForSize));
        return this.isPassive() ? Lang.translateDirect("boiler.passive") : (boilerLevel == 0 ? Lang.translateDirect("boiler.idle") : (boilerLevel == 18 ? Lang.translateDirect("boiler.max_lvl") : Lang.translateDirect("boiler.lvl", String.valueOf(boilerLevel))));
    }

    public MutableComponent getSizeComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return this.componentHelper("size", this.maxHeatForSize, forGoggles, useBlocksAsBars, styles);
    }

    public MutableComponent getWaterComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return this.componentHelper("water", this.maxHeatForWater, forGoggles, useBlocksAsBars, styles);
    }

    public MutableComponent getHeatComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return this.componentHelper("heat", this.passiveHeat ? 1 : this.activeHeat, forGoggles, useBlocksAsBars, styles);
    }

    private MutableComponent componentHelper(String label, int level, boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        MutableComponent base = useBlocksAsBars ? this.blockComponent(level) : this.barComponent(level);
        if (!forGoggles) {
            return base;
        } else {
            ChatFormatting style1 = styles.length >= 1 ? styles[0] : ChatFormatting.GRAY;
            ChatFormatting style2 = styles.length >= 2 ? styles[1] : ChatFormatting.DARK_GRAY;
            return Lang.translateDirect("boiler." + label).withStyle(style1).append(Lang.translateDirect("boiler." + label + "_dots").withStyle(style2)).append(base);
        }
    }

    private MutableComponent blockComponent(int level) {
        return Components.literal("█".repeat(this.minValue) + "▒".repeat(level - this.minValue) + "░".repeat(this.maxValue - level));
    }

    private MutableComponent barComponent(int level) {
        return Components.empty().append(this.bars(Math.max(0, this.minValue - 1), ChatFormatting.DARK_GREEN)).append(this.bars(this.minValue > 0 ? 1 : 0, ChatFormatting.GREEN)).append(this.bars(Math.max(0, level - this.minValue), ChatFormatting.DARK_GREEN)).append(this.bars(Math.max(0, this.maxValue - level), ChatFormatting.DARK_RED)).append(this.bars(Math.max(0, Math.min(18 - this.maxValue, (this.maxValue / 5 + 1) * 5 - this.maxValue)), ChatFormatting.DARK_GRAY));
    }

    private MutableComponent bars(int level, ChatFormatting format) {
        return Components.literal(Strings.repeat('|', level)).withStyle(format);
    }

    public boolean evaluate(FluidTankBlockEntity controller) {
        BlockPos controllerPos = controller.m_58899_();
        Level level = controller.m_58904_();
        int prevEngines = this.attachedEngines;
        int prevWhistles = this.attachedWhistles;
        this.attachedEngines = 0;
        this.attachedWhistles = 0;
        for (int yOffset = 0; yOffset < controller.height; yOffset++) {
            for (int xOffset = 0; xOffset < controller.width; xOffset++) {
                for (int zOffset = 0; zOffset < controller.width; zOffset++) {
                    BlockPos pos = controllerPos.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (FluidTankBlock.isTank(blockState)) {
                        for (Direction d : Iterate.directions) {
                            BlockPos attachedPos = pos.relative(d);
                            BlockState attachedState = level.getBlockState(attachedPos);
                            if (AllBlocks.STEAM_ENGINE.has(attachedState) && SteamEngineBlock.getFacing(attachedState) == d) {
                                this.attachedEngines++;
                            }
                            if (AllBlocks.STEAM_WHISTLE.has(attachedState) && WhistleBlock.getAttachedDirection(attachedState).getOpposite() == d) {
                                this.attachedWhistles++;
                            }
                        }
                    }
                }
            }
        }
        this.needsHeatLevelUpdate = true;
        return prevEngines != this.attachedEngines || prevWhistles != this.attachedWhistles;
    }

    public void checkPipeOrganAdvancement(FluidTankBlockEntity controller) {
        if (controller.getBehaviour(AdvancementBehaviour.TYPE).isOwnerPresent()) {
            BlockPos controllerPos = controller.m_58899_();
            Level level = controller.m_58904_();
            Set<Integer> whistlePitches = new HashSet();
            for (int yOffset = 0; yOffset < controller.height; yOffset++) {
                for (int xOffset = 0; xOffset < controller.width; xOffset++) {
                    for (int zOffset = 0; zOffset < controller.width; zOffset++) {
                        BlockPos pos = controllerPos.offset(xOffset, yOffset, zOffset);
                        BlockState blockState = level.getBlockState(pos);
                        if (FluidTankBlock.isTank(blockState)) {
                            for (Direction d : Iterate.directions) {
                                BlockPos attachedPos = pos.relative(d);
                                BlockState attachedState = level.getBlockState(attachedPos);
                                if (AllBlocks.STEAM_WHISTLE.has(attachedState) && WhistleBlock.getAttachedDirection(attachedState).getOpposite() == d && level.getBlockEntity(attachedPos) instanceof WhistleBlockEntity wbe) {
                                    whistlePitches.add(wbe.getPitchId());
                                }
                            }
                        }
                    }
                }
            }
            if (whistlePitches.size() >= 12) {
                controller.award(AllAdvancements.PIPE_ORGAN);
            }
        }
    }

    public boolean updateTemperature(FluidTankBlockEntity controller) {
        BlockPos controllerPos = controller.m_58899_();
        Level level = controller.m_58904_();
        this.needsHeatLevelUpdate = false;
        boolean prevPassive = this.passiveHeat;
        int prevActive = this.activeHeat;
        this.passiveHeat = false;
        this.activeHeat = 0;
        for (int xOffset = 0; xOffset < controller.width; xOffset++) {
            for (int zOffset = 0; zOffset < controller.width; zOffset++) {
                BlockPos pos = controllerPos.offset(xOffset, -1, zOffset);
                BlockState blockState = level.getBlockState(pos);
                float heat = BoilerHeaters.getActiveHeat(level, pos, blockState);
                if (heat == 0.0F) {
                    this.passiveHeat = true;
                } else if (heat > 0.0F) {
                    this.activeHeat = (int) ((float) this.activeHeat + heat);
                }
            }
        }
        this.passiveHeat = this.passiveHeat & this.activeHeat == 0;
        return prevActive != this.activeHeat || prevPassive != this.passiveHeat;
    }

    public boolean isActive() {
        return this.attachedEngines > 0 || this.attachedWhistles > 0;
    }

    public void clear() {
        this.waterSupply = 0.0F;
        this.activeHeat = 0;
        this.passiveHeat = false;
        this.attachedEngines = 0;
        Arrays.fill(this.supplyOverTime, 0.0F);
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("Supply", this.waterSupply);
        nbt.putInt("ActiveHeat", this.activeHeat);
        nbt.putBoolean("PassiveHeat", this.passiveHeat);
        nbt.putInt("Engines", this.attachedEngines);
        nbt.putInt("Whistles", this.attachedWhistles);
        nbt.putBoolean("Update", this.needsHeatLevelUpdate);
        return nbt;
    }

    public void read(CompoundTag nbt, int boilerSize) {
        this.waterSupply = nbt.getFloat("Supply");
        this.activeHeat = nbt.getInt("ActiveHeat");
        this.passiveHeat = nbt.getBoolean("PassiveHeat");
        this.attachedEngines = nbt.getInt("Engines");
        this.attachedWhistles = nbt.getInt("Whistles");
        this.needsHeatLevelUpdate = nbt.getBoolean("Update");
        Arrays.fill(this.supplyOverTime, (float) ((int) this.waterSupply));
        int forBoilerSize = this.getMaxHeatLevelForBoilerSize(boilerSize);
        int forWaterSupply = this.getMaxHeatLevelForWaterSupply();
        int actualHeat = Math.min(this.activeHeat, Math.min(forWaterSupply, forBoilerSize));
        float target = this.isPassive(boilerSize) ? 0.125F : (forBoilerSize == 0 ? 0.0F : (float) actualHeat / ((float) forBoilerSize * 1.0F));
        this.gauge.chase((double) target, 0.125, LerpedFloat.Chaser.EXP);
    }

    public BoilerData.BoilerFluidHandler createHandler() {
        return new BoilerData.BoilerFluidHandler();
    }

    public class BoilerFluidHandler implements IFluidHandler {

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return 10000;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return FluidHelper.isWater(stack.getFluid());
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            if (!this.isFluidValid(0, resource)) {
                return 0;
            } else {
                int amount = resource.getAmount();
                if (action.execute()) {
                    BoilerData.this.gatheredSupply += amount;
                }
                return amount;
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            return FluidStack.EMPTY;
        }
    }
}