package snownee.jade.addon.create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElementHelper;

public enum BlazeBurnerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag compound = accessor.getServerData();
        BlazeBurnerBlockEntity.FuelType activeFuel = BlazeBurnerBlockEntity.FuelType.NONE;
        boolean isCreative = compound.getBoolean("isCreative");
        if (isCreative) {
            BlazeBurnerBlock.HeatLevel heatLevel = BasinBlockEntity.getHeatLevelOf(accessor.getBlockState());
            if (heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING) {
                activeFuel = BlazeBurnerBlockEntity.FuelType.SPECIAL;
            } else if (heatLevel != BlazeBurnerBlock.HeatLevel.NONE) {
                activeFuel = BlazeBurnerBlockEntity.FuelType.NORMAL;
            }
        } else {
            activeFuel = BlazeBurnerBlockEntity.FuelType.values()[compound.getInt("fuelLevel")];
        }
        if (activeFuel != BlazeBurnerBlockEntity.FuelType.NONE) {
            ItemStack item = new ItemStack(activeFuel == BlazeBurnerBlockEntity.FuelType.SPECIAL ? Items.SOUL_CAMPFIRE : Items.CAMPFIRE);
            tooltip.add(IElementHelper.get().smallItem(item));
            if (isCreative) {
                tooltip.append(IThemeHelper.get().info(Component.translatable("jade.infinity")));
            } else {
                tooltip.append(IThemeHelper.get().seconds(compound.getInt("burnTimeRemaining")));
            }
        }
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlazeBurnerBlockEntity burner = (BlazeBurnerBlockEntity) accessor.getBlockEntity();
        if (burner.isCreative()) {
            data.putBoolean("isCreative", true);
        } else if (burner.getActiveFuel() != BlazeBurnerBlockEntity.FuelType.NONE) {
            data.putInt("fuelLevel", burner.getActiveFuel().ordinal());
            data.putInt("burnTimeRemaining", burner.getRemainingBurnTime());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CreatePlugin.BLAZE_BURNER;
    }
}