package com.mna.items.sorcery;

import com.mna.Registries;
import com.mna.api.items.TieredItem;
import com.mna.api.spells.parts.Modifier;
import com.mna.items.base.IRadialMenuItem;
import com.mna.items.renderers.books.AlterationBookRenderer;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.spells.ModifierRecipe;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemModifierBook extends TieredItem implements IRadialMenuItem {

    public static final String NBT_MODIFIER_ID = "key_modifier";

    public ItemModifierBook() {
        super(new Item.Properties());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new AlterationBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        getModifier(stack).ifPresent(m -> tooltip.add(Component.translatable(m.getRegistryName().toString()).withStyle(ChatFormatting.AQUA)));
        IRadialMenuItem.super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    public static final Optional<Modifier> getModifier(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("key_modifier")) {
            ResourceLocation rLoc = new ResourceLocation(stack.getTag().getString("key_modifier"));
            return Optional.ofNullable((Modifier) ((IForgeRegistry) Registries.Modifier.get()).getValue(rLoc));
        } else {
            return Optional.empty();
        }
    }

    public static final Optional<ModifierRecipe> getRecipe(Modifier modifier, Level world) {
        return world.getRecipeManager().<CraftingContainer, ModifierRecipe>getAllRecipesFor(RecipeInit.MODIFIER_TYPE.get()).stream().filter(m -> m.getComponent() == modifier).findFirst();
    }

    public static final void setModifier(ItemStack stack, ResourceLocation modifierRloc) {
        stack.getOrCreateTag().putString("key_modifier", modifierRloc.toString());
    }
}