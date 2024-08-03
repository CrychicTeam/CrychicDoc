package net.minecraft.world.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;

public class DecoratedPotRecipe extends CustomRecipe {

    public DecoratedPotRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        if (!this.canCraftInDimensions(craftingContainer0.getWidth(), craftingContainer0.getHeight())) {
            return false;
        } else {
            for (int $$2 = 0; $$2 < craftingContainer0.m_6643_(); $$2++) {
                ItemStack $$3 = craftingContainer0.m_8020_($$2);
                switch($$2) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        if (!$$3.is(ItemTags.DECORATED_POT_INGREDIENTS)) {
                            return false;
                        }
                        break;
                    case 2:
                    case 4:
                    case 6:
                    default:
                        if (!$$3.is(Items.AIR)) {
                            return false;
                        }
                }
            }
            return true;
        }
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        DecoratedPotBlockEntity.Decorations $$2 = new DecoratedPotBlockEntity.Decorations(craftingContainer0.m_8020_(1).getItem(), craftingContainer0.m_8020_(3).getItem(), craftingContainer0.m_8020_(5).getItem(), craftingContainer0.m_8020_(7).getItem());
        return createDecoratedPotItem($$2);
    }

    public static ItemStack createDecoratedPotItem(DecoratedPotBlockEntity.Decorations decoratedPotBlockEntityDecorations0) {
        ItemStack $$1 = Items.DECORATED_POT.getDefaultInstance();
        CompoundTag $$2 = decoratedPotBlockEntityDecorations0.save(new CompoundTag());
        BlockItem.setBlockEntityData($$1, BlockEntityType.DECORATED_POT, $$2);
        return $$1;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 == 3 && int1 == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.DECORATED_POT_RECIPE;
    }
}