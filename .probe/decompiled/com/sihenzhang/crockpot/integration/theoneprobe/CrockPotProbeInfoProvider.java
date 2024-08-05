package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.util.RLUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

public class CrockPotProbeInfoProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerProvider(this);
        return null;
    }

    public ResourceLocation getID() {
        return RLUtils.createRL("crock_pot");
    }

    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData data) {
        if (level.getBlockEntity(data.getPos()) instanceof CrockPotBlockEntity crockPotTileEntity) {
            boolean needDrawInputs = false;
            ItemStackHandler itemHandler = crockPotTileEntity.getItemHandler();
            for (int i = 0; i < 4; i++) {
                if (!itemHandler.getStackInSlot(i).isEmpty()) {
                    needDrawInputs = true;
                    break;
                }
            }
            if (needDrawInputs) {
                ItemStack[] inputStacks = new ItemStack[4];
                for (int ix = 0; ix < 4; ix++) {
                    inputStacks[ix] = itemHandler.getStackInSlot(ix);
                }
                IProbeInfo inputs = probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(-6710887).spacing(0));
                Arrays.stream(inputStacks).forEach(inputs::item);
                if (player.m_6144_()) {
                    IProbeInfo foodValues = probeInfo.vertical(probeInfo.defaultLayoutStyle().spacing(0));
                    FoodValues mergedFoodValues = FoodValues.merge((Collection<FoodValues>) Arrays.stream(inputStacks).filter(stack -> !stack.isEmpty()).map(stack -> FoodValuesDefinition.getFoodValues(stack, level)).collect(Collectors.toList()));
                    IProbeInfo foodValuesHorizontal = null;
                    int categoryCount = 0;
                    for (Pair<FoodCategory, Float> entry : mergedFoodValues.entrySet()) {
                        Component suffix = Component.literal("×" + entry.getValue());
                        if (categoryCount % 2 == 0) {
                            foodValuesHorizontal = foodValues.horizontal(probeInfo.defaultLayoutStyle().spacing(4));
                        }
                        foodValuesHorizontal.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(FoodCategory.getItemStack((FoodCategory) entry.getKey())).text(suffix);
                        categoryCount++;
                    }
                }
            }
            if (crockPotTileEntity.isCooking()) {
                ItemStack result = crockPotTileEntity.getResult();
                if (!result.isEmpty()) {
                    Component prefix = Component.translatable("integration.crockpot.top.recipe");
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(prefix).item(result).itemLabel(result);
                }
                float progress = crockPotTileEntity.getCookingProgress();
                if (progress > 1.0E-6F) {
                    probeInfo.progress((int) (progress * 100.0F), 100, probeInfo.defaultProgressStyle().suffix("%"));
                }
            }
        }
    }
}