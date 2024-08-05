package net.minecraft.world.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ShieldDecorationRecipe extends CustomRecipe {

    public ShieldDecorationRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        ItemStack $$2 = ItemStack.EMPTY;
        ItemStack $$3 = ItemStack.EMPTY;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if ($$5.getItem() instanceof BannerItem) {
                    if (!$$3.isEmpty()) {
                        return false;
                    }
                    $$3 = $$5;
                } else {
                    if (!$$5.is(Items.SHIELD)) {
                        return false;
                    }
                    if (!$$2.isEmpty()) {
                        return false;
                    }
                    if (BlockItem.getBlockEntityData($$5) != null) {
                        return false;
                    }
                    $$2 = $$5;
                }
            }
        }
        return !$$2.isEmpty() && !$$3.isEmpty();
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        ItemStack $$2 = ItemStack.EMPTY;
        ItemStack $$3 = ItemStack.EMPTY;
        for (int $$4 = 0; $$4 < craftingContainer0.m_6643_(); $$4++) {
            ItemStack $$5 = craftingContainer0.m_8020_($$4);
            if (!$$5.isEmpty()) {
                if ($$5.getItem() instanceof BannerItem) {
                    $$2 = $$5;
                } else if ($$5.is(Items.SHIELD)) {
                    $$3 = $$5.copy();
                }
            }
        }
        if ($$3.isEmpty()) {
            return $$3;
        } else {
            CompoundTag $$6 = BlockItem.getBlockEntityData($$2);
            CompoundTag $$7 = $$6 == null ? new CompoundTag() : $$6.copy();
            $$7.putInt("Base", ((BannerItem) $$2.getItem()).getColor().getId());
            BlockItem.setBlockEntityData($$3, BlockEntityType.BANNER, $$7);
            return $$3;
        }
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHIELD_DECORATION;
    }
}