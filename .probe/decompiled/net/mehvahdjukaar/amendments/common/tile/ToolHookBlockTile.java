package net.mehvahdjukaar.amendments.common.tile;

import net.mehvahdjukaar.amendments.configs.ClientConfigs;
import net.mehvahdjukaar.amendments.reg.ModBlockProperties;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.amendments.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.block.DynamicRenderedItemDisplayTile;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ToolHookBlockTile extends DynamicRenderedItemDisplayTile {

    public static final ModelDataKey<ItemStack> ITEM = ModBlockProperties.ITEM;

    public ToolHookBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.TOOL_HOOK_TILE.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("tool hook");
    }

    @Override
    public boolean isNeverFancy() {
        return (Boolean) ClientConfigs.FAST_HOOKS.get();
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        super.addExtraModelData(builder);
        builder.with(ITEM, this.getDisplayedItem());
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        Item item = stack.getItem();
        return isValidTool(item) ? super.m_7013_(index, stack) : false;
    }

    public static boolean isValidTool(Item item) {
        return item instanceof DiggerItem || item instanceof SwordItem || item instanceof TridentItem || item.builtInRegistryHolder().is(ModTags.GOES_IN_TRIPWIRE_HOOK);
    }

    @Override
    public void updateTileOnInventoryChanged() {
        super.updateTileOnInventoryChanged();
        if (this.getDisplayedItem().isEmpty() && this.f_58857_ != null) {
            this.f_58857_.setBlockAndUpdate(this.f_58858_, Blocks.TRIPWIRE_HOOK.withPropertiesOf(this.m_58900_()));
        }
    }

    @Override
    public void updateClientVisualsOnLoad() {
        super.updateClientVisualsOnLoad();
        this.requestModelReload();
    }
}