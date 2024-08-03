package io.github.apace100.origins.badge;

import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.screen.tooltip.CraftingRecipeTooltipComponent;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public record CraftingRecipeBadge(ResourceLocation spriteId, Recipe<CraftingContainer> recipe, @Nullable Component prefix, @Nullable Component suffix) implements Badge {

    public CraftingRecipeBadge(Instance instance) {
        this(instance.getId("sprite"), (Recipe<CraftingContainer>) instance.get("recipe"), (Component) instance.get("prefix"), (Component) instance.get("suffix"));
    }

    @Override
    public boolean hasTooltip() {
        return true;
    }

    public NonNullList<ItemStack> peekInputs(float time) {
        int seed = Mth.floor(time / 30.0F);
        NonNullList<ItemStack> inputs = NonNullList.withSize(9, ItemStack.EMPTY);
        List<Ingredient> ingredients = this.recipe.getIngredients();
        for (int index = 0; index < ingredients.size(); index++) {
            ItemStack[] stacks = ((Ingredient) ingredients.get(index)).getItems();
            if (stacks.length > 0) {
                inputs.set(index, stacks[seed % stacks.length].copy());
            }
        }
        return inputs;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<ClientTooltipComponent> getTooltipComponents(ConfiguredPower<?, ?> powerType, int widthLimit, float time, Font textRenderer) {
        List<ClientTooltipComponent> tooltips = new LinkedList();
        if (Minecraft.getInstance().level == null) {
            Origins.LOGGER.warn("Could not construct crafting recipe badge, because world was null");
            return tooltips;
        } else {
            if (Minecraft.getInstance().options.advancedItemTooltips) {
                Component recipeIdText = Component.literal(this.recipe.getId().toString()).withStyle(ChatFormatting.DARK_GRAY);
                widthLimit = Math.max(130, textRenderer.width(recipeIdText));
                if (this.prefix != null) {
                    TooltipBadge.addLines(tooltips, this.prefix, textRenderer, widthLimit);
                }
                tooltips.add(new CraftingRecipeTooltipComponent(this.peekInputs(time), this.recipe.getResultItem(Minecraft.getInstance().level.m_9598_()).copy()));
                if (this.suffix != null) {
                    TooltipBadge.addLines(tooltips, this.suffix, textRenderer, widthLimit);
                }
                TooltipBadge.addLines(tooltips, recipeIdText, textRenderer, widthLimit);
            } else {
                int var8 = 130;
                if (this.prefix != null) {
                    TooltipBadge.addLines(tooltips, this.prefix, textRenderer, var8);
                }
                tooltips.add(new CraftingRecipeTooltipComponent(this.peekInputs(time), this.recipe.getResultItem(Minecraft.getInstance().level.m_9598_()).copy()));
                if (this.suffix != null) {
                    TooltipBadge.addLines(tooltips, this.suffix, textRenderer, var8);
                }
            }
            return tooltips;
        }
    }

    @Override
    public Instance toData(Instance instance) {
        instance.set("sprite", this.spriteId);
        instance.set("recipe", this.recipe);
        instance.set("prefix", this.prefix);
        instance.set("suffix", this.suffix);
        return instance;
    }

    @Override
    public BadgeFactory getBadgeFactory() {
        return BadgeFactories.CRAFTING_RECIPE.get();
    }
}