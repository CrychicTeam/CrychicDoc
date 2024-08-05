package com.mna.items.sorcery;

import com.mna.ManaAndArtifice;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.gui.containers.providers.NamedSpellRecipe;
import com.mna.items.base.IItemWithGui;
import com.mna.spells.crafting.SpellRecipe;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSpellRecipe extends Item implements IItemWithGui<ItemSpellRecipe>, ICanContainSpell {

    public ItemSpellRecipe(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedSpellRecipe();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldStack = playerIn.m_21120_(handIn);
        return heldStack.getItem() == this && this.openGuiIfModifierPressed(heldStack, playerIn, worldIn) ? InteractionResultHolder.success(heldStack) : InteractionResultHolder.fail(heldStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        if (player != null) {
            ArrayList<Component> flavorText = this.getFlavorText(stack);
            if (flavorText != null) {
                tooltip.addAll(flavorText);
            }
            SpellRecipe recipe = SpellRecipe.fromNBT(this.getSpellCompound(stack, player));
            recipe.addItemTooltip(stack, worldIn, tooltip, player);
            IItemWithGui.super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }

    @Override
    public boolean requiresModifierKey() {
        return false;
    }

    @Nullable
    protected ArrayList<Component> getFlavorText(ItemStack stack) {
        return null;
    }

    @Override
    public Component getName(ItemStack stack) {
        SpellRecipe recipe = SpellRecipe.fromNBT(this.getSpellCompound(stack, null));
        return recipe.isValid() && recipe.getComponents().stream().noneMatch(c -> c.getPart() == Components.LMNOP) ? this.getTranslatedDisplayName(recipe) : super.getName(stack);
    }

    protected Component getTranslatedDisplayName(SpellRecipe recipe) {
        StringBuilder sb = new StringBuilder();
        recipe.iterateComponents(c -> {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(Component.translatable(((SpellEffect) c.getPart()).getRegistryName().toString()).getString());
        });
        return Component.translatable("item.mna.spell.recipe_display_name", Component.translatable(recipe.getShape().getPart().getRegistryName().toString()).getString(), sb.toString());
    }

    @Override
    public boolean shouldOverrideMultiplayerNbt() {
        return true;
    }

    public CompoundTag getSpellCompound(ItemStack stack, @Nullable Player player) {
        return stack.getTag();
    }
}