package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public class CrockPotProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final CrockPotProvider INSTANCE = new CrockPotProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (config.get(ModIntegrationJade.CROCK_POT)) {
            CompoundTag serverData = accessor.getServerData();
            if (serverData.contains("CrockPotItemHandler")) {
                ItemStackHandler itemHandler = new ItemStackHandler();
                itemHandler.deserializeNBT(serverData.getCompound("CrockPotItemHandler"));
                IElementHelper helper = tooltip.getElementHelper();
                boolean needDrawInputs = false;
                for (int i = 0; i < 4; i++) {
                    if (!itemHandler.getStackInSlot(i).isEmpty()) {
                        needDrawInputs = true;
                        break;
                    }
                }
                if (needDrawInputs) {
                    List<ItemStack> inputStacks = new ArrayList();
                    List<IElement> inputs = new ArrayList();
                    for (int ix = 0; ix < 4; ix++) {
                        ItemStack stackInSlot = itemHandler.getStackInSlot(ix);
                        inputStacks.add(stackInSlot);
                        inputs.add(helper.item(stackInSlot));
                    }
                    tooltip.add(inputs);
                    if (serverData.getBoolean("DrawFoodValue")) {
                        FoodValues mergedFoodValues = FoodValues.merge((Collection<FoodValues>) inputStacks.stream().filter(stack -> !stack.isEmpty()).map(stack -> FoodValuesDefinition.getFoodValues(stack, accessor.getLevel())).collect(Collectors.toList()));
                        int categoryCount = 0;
                        for (Pair<FoodCategory, Float> entry : mergedFoodValues.entrySet()) {
                            if (categoryCount % 3 == 0) {
                                tooltip.add(helper.item(FoodCategory.getItemStack((FoodCategory) entry.getKey())));
                            } else {
                                tooltip.append(helper.spacer(2, 0));
                                tooltip.append(helper.item(FoodCategory.getItemStack((FoodCategory) entry.getKey())));
                            }
                            tooltip.append(helper.text(Component.literal("×" + entry.getValue())));
                            categoryCount++;
                        }
                    }
                }
                if (serverData.contains("Result")) {
                    ItemStack result = ItemStack.of(serverData.getCompound("Result"));
                    tooltip.add(helper.text(Component.translatable("integration.crockpot.top.recipe")));
                    tooltip.append(helper.item(result));
                    tooltip.append(helper.text(result.getHoverName()));
                }
                if (serverData.contains("CookingProgress")) {
                    float progress = serverData.getFloat("CookingProgress");
                    tooltip.add(helper.progress(progress, null, helper.progressStyle(), BoxStyle.DEFAULT, false));
                }
            }
        }
    }

    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof CrockPotBlockEntity crockPotTileEntity) {
            tag.remove("jadeHandler");
            tag.put("CrockPotItemHandler", crockPotTileEntity.getItemHandler().serializeNBT());
            tag.putBoolean("DrawFoodValue", accessor.getPlayer().m_6144_());
            if (crockPotTileEntity.isCooking()) {
                tag.put("Result", crockPotTileEntity.getResult().serializeNBT());
                tag.putFloat("CookingProgress", crockPotTileEntity.getCookingProgress());
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("crockpot", "crock_pot");
    }
}