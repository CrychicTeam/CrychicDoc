package fr.frinn.custommachinery.client.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.integration.jei.DisplayInfoTemplate;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfo;
import fr.frinn.custommachinery.api.integration.jei.IJEIElementRenderer;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.common.crafting.machine.CustomMachineRecipe;
import fr.frinn.custommachinery.common.init.CustomMachineItem;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.util.Comparators;
import fr.frinn.custommachinery.impl.integration.jei.GuiElementJEIRendererRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class AbstractRecipeCategory<T extends IMachineRecipe> implements IRecipeCategory<T> {

    protected static final int ICON_SIZE = 10;

    protected final CustomMachine machine;

    protected final RecipeType<T> recipeType;

    protected final IGuiHelper guiHelper;

    protected final RecipeHelper recipeHelper;

    protected final LoadingCache<IRequirement<?>, RequirementDisplayInfo> infoCache;

    protected LoadingCache<T, List<IJEIIngredientWrapper<?>>> wrapperCache;

    protected int offsetX;

    protected int offsetY;

    protected int width;

    protected int height;

    protected int rowY;

    protected int maxIconPerRow;

    public AbstractRecipeCategory(CustomMachine machine, RecipeType<T> type, IJeiHelpers helpers) {
        this.machine = machine;
        this.recipeType = type;
        this.guiHelper = helpers.getGuiHelper();
        this.recipeHelper = new RecipeHelper(machine, helpers);
        this.infoCache = CacheBuilder.newBuilder().build(new CacheLoader<IRequirement<?>, RequirementDisplayInfo>() {

            public RequirementDisplayInfo load(IRequirement<?> requirement) {
                RequirementDisplayInfo info = new RequirementDisplayInfo();
                requirement.getDisplayInfo(info);
                DisplayInfoTemplate template = requirement.getDisplayInfoTemplate();
                if (template != null) {
                    if (!template.getTooltips().isEmpty()) {
                        info.getTooltips().clear();
                    }
                    template.build(info);
                }
                return info;
            }
        });
        this.wrapperCache = CacheBuilder.newBuilder().build(new CacheLoader<T, List<IJEIIngredientWrapper<?>>>() {

            public List<IJEIIngredientWrapper<?>> load(T recipe) {
                Builder<IJEIIngredientWrapper<?>> wrappers = ImmutableList.builder();
                recipe.getJEIIngredientRequirements().forEach(requirement -> wrappers.addAll(requirement.getJEIIngredientWrappers(recipe)));
                return wrappers.build();
            }
        });
        this.setupRecipeDimensions();
    }

    private void setupRecipeDimensions() {
        if (Minecraft.getInstance().level != null) {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = 0;
            int maxY = 0;
            for (IGuiElement element : this.machine.getJeiElements().isEmpty() ? this.machine.getGuiElements() : this.machine.getJeiElements()) {
                if (GuiElementJEIRendererRegistry.hasJEIRenderer(element.getType()) && element.showInJei()) {
                    minX = Math.min(minX, element.getX());
                    minY = Math.min(minY, element.getY());
                    maxX = Math.max(maxX, element.getX() + element.getWidth());
                    maxY = Math.max(maxY, element.getY() + element.getHeight());
                }
            }
            this.rowY = Math.max(maxY - minY, 20);
            this.offsetX = Math.max(minX, 0);
            this.offsetY = Math.max(minY, 0);
            this.width = Math.max(maxX - minX, 20);
            this.maxIconPerRow = this.width / 12;
            long maxDisplayRequirement = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor((net.minecraft.world.item.crafting.RecipeType<T>) Registration.CUSTOM_MACHINE_RECIPE.get()).stream().filter(recipe -> recipe.getMachineId().equals(this.machine.getId())).mapToLong(recipe -> recipe.getDisplayInfoRequirements().stream().map(this.infoCache).filter(RequirementDisplayInfo::shouldRender).count()).max().orElse(1L);
            int rows = Math.toIntExact(maxDisplayRequirement) / this.maxIconPerRow + 1;
            this.height = this.rowY + 12 * rows;
        }
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return this.recipeType;
    }

    @Override
    public Component getTitle() {
        return this.machine.getName();
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(this.width, this.height);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, CustomMachineItem.makeMachineItem(this.machine.getId()));
    }

    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        builder.moveRecipeTransferButton(this.width - 11, this.height - 11);
        List<IJEIIngredientWrapper<?>> wrappers = new ArrayList((Collection) this.wrapperCache.getUnchecked(recipe));
        List<IGuiElement> elements = this.machine.getJeiElements().isEmpty() ? this.machine.getGuiElements() : this.machine.getJeiElements();
        if (recipe instanceof CustomMachineRecipe machineRecipe && !machineRecipe.getGuiElements().isEmpty()) {
            elements = machineRecipe.getCustomGuiElements(elements);
        }
        elements.forEach(element -> {
            Iterator<IJEIIngredientWrapper<?>> iterator = wrappers.iterator();
            while (iterator.hasNext()) {
                IJEIIngredientWrapper<?> wrapper = (IJEIIngredientWrapper<?>) iterator.next();
                if (wrapper.setupRecipe(builder, this.offsetX, this.offsetY, element, this.recipeHelper)) {
                    iterator.remove();
                    break;
                }
            }
        });
    }

    public void draw(T recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        List<IGuiElement> elements = this.machine.getJeiElements().isEmpty() ? this.machine.getGuiElements() : this.machine.getJeiElements();
        if (recipe instanceof CustomMachineRecipe machineRecipe && !machineRecipe.getGuiElements().isEmpty()) {
            elements = machineRecipe.getCustomGuiElements(elements);
        }
        elements.stream().filter(element -> GuiElementJEIRendererRegistry.hasJEIRenderer(element.getType()) && element.showInJei()).sorted(Comparators.GUI_ELEMENTS_COMPARATOR.reversed()).forEach(element -> {
            IJEIElementRenderer<IGuiElement> renderer = GuiElementJEIRendererRegistry.getJEIRenderer(element.getType());
            graphics.pose().pushPose();
            graphics.pose().translate((float) (-this.offsetX), (float) (-this.offsetY), 0.0F);
            renderer.renderElementInJEI(graphics, element, recipe, (int) mouseX, (int) mouseY);
            graphics.pose().popPose();
        });
        graphics.fill(-3, this.rowY, this.width + 3, this.rowY + 1, 805306368);
        AtomicInteger index = new AtomicInteger();
        AtomicInteger row = new AtomicInteger(0);
        recipe.getDisplayInfoRequirements().stream().map(this.infoCache).filter(RequirementDisplayInfo::shouldRender).forEach(info -> {
            int x = index.get() * 12 - 2;
            int y = this.rowY + 2 + 12 * row.get();
            if (index.incrementAndGet() >= this.maxIconPerRow) {
                index.set(0);
                row.incrementAndGet();
            }
            graphics.pose().pushPose();
            graphics.pose().translate((double) x, (double) y, 0.0);
            info.renderIcon(graphics, 10);
            graphics.pose().popPose();
        });
    }

    public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView view, double mouseX, double mouseY) {
        List<IGuiElement> elements = this.machine.getJeiElements().isEmpty() ? this.machine.getGuiElements() : this.machine.getJeiElements();
        if (recipe instanceof CustomMachineRecipe machineRecipe && !machineRecipe.getGuiElements().isEmpty()) {
            elements = machineRecipe.getCustomGuiElements(elements);
        }
        for (IGuiElement element : elements) {
            if (element.showInJei() && GuiElementJEIRendererRegistry.hasJEIRenderer(element.getType())) {
                IJEIElementRenderer<IGuiElement> renderer = GuiElementJEIRendererRegistry.getJEIRenderer(element.getType());
                int x = element.getX() - this.offsetX;
                int y = element.getY() - this.offsetY;
                if (renderer.isHoveredInJei(element, x, y, (int) mouseX, (int) mouseY)) {
                    return renderer.getJEITooltips(element, recipe);
                }
            }
        }
        int index = 0;
        int row = 0;
        for (RequirementDisplayInfo info : recipe.getDisplayInfoRequirements().stream().map(this.infoCache).filter(RequirementDisplayInfo::shouldRender).toList()) {
            int x = index * 12 - 2;
            int y = this.rowY + 2 + 12 * row;
            if (index++ >= this.maxIconPerRow) {
                index = 0;
                row++;
            }
            if (mouseX >= (double) x && mouseX <= (double) (x + 10) && mouseY >= (double) y && mouseY <= (double) (y + 10) && Minecraft.getInstance().screen != null) {
                return info.getTooltips().stream().filter(pair -> ((IDisplayInfo.TooltipPredicate) pair.getSecond()).shouldDisplay(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips)).map(Pair::getFirst).toList();
            }
        }
        return Collections.emptyList();
    }

    public boolean handleInput(T recipe, double mouseX, double mouseY, InputConstants.Key mouseButton) {
        if (mouseButton.getValue() != 0 && mouseButton.getValue() != 1 && mouseButton.getValue() != 2) {
            return false;
        } else {
            AtomicInteger index = new AtomicInteger();
            AtomicInteger row = new AtomicInteger(0);
            return recipe.getDisplayInfoRequirements().stream().map(this.infoCache).filter(RequirementDisplayInfo::shouldRender).anyMatch(info -> {
                int x = index.get() * 12 - 2;
                int y = this.rowY + 2 + 12 * row.get();
                if (index.incrementAndGet() >= this.maxIconPerRow) {
                    index.set(0);
                    row.incrementAndGet();
                }
                return mouseX >= (double) x && mouseX <= (double) (x + 10) && mouseY >= (double) y && mouseY <= (double) (y + 10) && Minecraft.getInstance().screen != null ? info.handleClick(this.machine, recipe, mouseButton.getValue()) : false;
            });
        }
    }
}