package snownee.jade.addon.vanilla;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.addon.universal.ItemStorageProvider;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum ChiseledBookshelfProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    private static ItemStack getHitBook(BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof ChiseledBookShelfBlockEntity)) {
            return ItemStack.EMPTY;
        } else if (!accessor.getServerData().contains("Bookshelf")) {
            return ItemStack.EMPTY;
        } else {
            Optional<Vec2> optional = ChiseledBookShelfBlock.getRelativeHitCoordinatesForBlockFace(accessor.getHitResult(), (Direction) accessor.getBlockState().m_61143_(HorizontalDirectionalBlock.FACING));
            if (optional.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int i = ChiseledBookShelfBlock.getHitSlot((Vec2) optional.get());
                NonNullList<ItemStack> items = NonNullList.withSize(((ChiseledBookShelfBlockEntity) accessor.getBlockEntity()).getContainerSize(), ItemStack.EMPTY);
                ContainerHelper.loadAllItems(accessor.getServerData().getCompound("Bookshelf"), items);
                return i >= items.size() ? ItemStack.EMPTY : items.get(i);
            }
        }
    }

    @Override
    public IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        if (accessor.showDetails()) {
            return null;
        } else {
            ItemStack item = getHitBook(accessor);
            return item.isEmpty() ? null : IElementHelper.get().item(item);
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!accessor.showDetails()) {
            ItemStack item = getHitBook(accessor);
            if (!item.isEmpty()) {
                tooltip.remove(Identifiers.UNIVERSAL_ITEM_STORAGE);
                tooltip.add(IDisplayHelper.get().stripColor(item.getHoverName()));
                if (item.getTag() != null && item.getTag().contains("StoredEnchantments")) {
                    List<Component> list = Lists.newArrayList();
                    ItemStack.appendEnchantmentNames(list, EnchantedBookItem.getEnchantments(item));
                    tooltip.addAll(list);
                }
            }
        }
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        ChiseledBookShelfBlockEntity be = (ChiseledBookShelfBlockEntity) accessor.getBlockEntity();
        if (!be.isEmpty()) {
            data.put("Bookshelf", be.m_187482_());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.MC_CHISELED_BOOKSHELF;
    }

    @Override
    public int getDefaultPriority() {
        return ItemStorageProvider.INSTANCE.getDefaultPriority() + 1;
    }
}