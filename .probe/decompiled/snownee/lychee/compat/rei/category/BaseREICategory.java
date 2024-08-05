package snownee.lychee.compat.rei.category;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import snownee.lychee.client.core.post.PostActionRenderer;
import snownee.lychee.client.gui.AllGuiTextures;
import snownee.lychee.compat.IngredientInfo;
import snownee.lychee.compat.JEIREI;
import snownee.lychee.compat.rei.LEntryWidget;
import snownee.lychee.compat.rei.REICompat;
import snownee.lychee.compat.rei.ReactiveWidget;
import snownee.lychee.compat.rei.display.BaseREIDisplay;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.post.CompoundAction;
import snownee.lychee.core.post.DropItem;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.post.RandomSelect;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.util.ClientProxy;

public abstract class BaseREICategory<C extends LycheeContext, T extends LycheeRecipe<C>, D extends BaseREIDisplay<T>> implements DisplayCategory<D> {

    public static final int width = 150;

    public static final int height = 59;

    protected final List<LycheeRecipeType<C, T>> recipeTypes;

    public Renderer icon;

    public List<T> initialRecipes;

    public CategoryIdentifier<D> categoryIdentifier;

    protected Rect2i infoRect;

    public BaseREICategory(LycheeRecipeType<C, T> recipeType) {
        this(List.of(recipeType));
    }

    public BaseREICategory(List<LycheeRecipeType<C, T>> recipeTypes) {
        this.recipeTypes = recipeTypes;
        this.infoRect = new Rect2i(4, 25, 8, 8);
    }

    public static <T> void slotGroup(List<Widget> widgets, Point startPoint, int x, int y, List<T> items, BaseREICategory.SlotLayoutFunction<T> layoutFunction) {
        int size = Math.min(items.size(), 9);
        int gridX = (int) Math.ceil(Math.sqrt((double) size));
        int gridY = (int) Math.ceil((double) ((float) size / (float) gridX));
        x -= gridX * 9;
        y -= gridY * 9;
        int index = 0;
        for (int i = 0; i < gridY; i++) {
            for (int j = 0; j < gridX && index < size; j++) {
                layoutFunction.apply(widgets, startPoint, (T) items.get(index), x + j * 19, y + i * 19);
                index++;
            }
        }
    }

    public static void actionSlot(List<Widget> widgets, Point startPoint, PostAction action, int x, int y) {
        LEntryWidget slot = REICompat.slot(startPoint, x, y, action.getConditions().isEmpty() ? REICompat.SlotType.NORMAL : REICompat.SlotType.CHANCE);
        slot.markOutput();
        List<EntryStack<?>> entries = Lists.newArrayList();
        Map<EntryStack<ItemStack>, PostAction> itemMap = Maps.newHashMap();
        buildActionSlot(entries, action, itemMap);
        slot.entries(entries);
        widgets.add(slot);
        slot.addTooltipCallback(tooltip -> {
            if (tooltip == null) {
                return null;
            } else {
                Object raw = tooltip.getContextStack();
                if (!itemMap.containsKey(raw)) {
                    return tooltip;
                } else {
                    tooltip.entries().clear();
                    raw = itemMap.get(raw);
                    List<Component> list;
                    if (action instanceof RandomSelect randomSelect) {
                        list = PostActionRenderer.getTooltipsFromRandom(randomSelect, (PostAction) raw);
                    } else {
                        list = PostActionRenderer.<PostAction>of(action).getTooltips(action);
                    }
                    tooltip.entries().addAll(list.stream().map(Tooltip::entry).toList());
                    return tooltip;
                }
            }
        });
    }

    private static void buildActionSlot(List<EntryStack<?>> entries, PostAction action, Map<EntryStack<ItemStack>, PostAction> itemMap) {
        if (action instanceof DropItem dropitem) {
            EntryStack<ItemStack> entry = EntryStacks.of(dropitem.stack);
            entries.add(entry);
            itemMap.put(entry, dropitem);
        } else if (action instanceof CompoundAction compoundAction) {
            compoundAction.getChildActions().filter(Predicate.not(PostAction::isHidden)).forEach(child -> buildActionSlot(entries, child, itemMap));
        } else {
            entries.add(EntryStack.of(REICompat.POST_ACTION, action));
        }
    }

    public static void drawInfoBadge(List<Widget> widgets, ILycheeRecipe<?> recipe, Point startPoint, Rect2i rect) {
        if (!recipe.getContextualHolder().getConditions().isEmpty() || !Strings.isNullOrEmpty(recipe.getComment())) {
            widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
                PoseStack matrixStack = graphics.pose();
                matrixStack.pushPose();
                matrixStack.translate((float) (startPoint.x + rect.getX()), (float) (startPoint.y + rect.getY()), 0.0F);
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                AllGuiTextures.INFO.render(graphics, 0, 0);
                matrixStack.popPose();
            }));
            ReactiveWidget reactive = new ReactiveWidget(REICompat.offsetRect(startPoint, rect));
            reactive.setTooltipFunction($ -> (Component[]) JEIREI.getRecipeTooltip(recipe).toArray(new Component[0]));
            reactive.setOnClick((widget, button) -> ClientProxy.postInfoBadgeClickEvent(recipe, button));
            widgets.add(reactive);
        }
    }

    public CategoryIdentifier<? extends D> getCategoryIdentifier() {
        return this.categoryIdentifier;
    }

    public Renderer getIcon() {
        return this.icon;
    }

    public abstract Renderer createIcon(List<T> var1);

    public Component getTitle() {
        return JEIREI.makeTitle(this.getIdentifier());
    }

    public int getDisplayHeight() {
        return 67;
    }

    public int getDisplayWidth(D display) {
        return 150;
    }

    public int getRealWidth() {
        return 120;
    }

    public void actionGroup(List<Widget> widgets, Point startPoint, T recipe, int x, int y) {
        slotGroup(widgets, startPoint, x, y, ILycheeRecipe.filterHidden(recipe.getPostActions()).toList(), BaseREICategory::actionSlot);
    }

    public void ingredientGroup(List<Widget> widgets, Point startPoint, T recipe, int x, int y) {
        List<IngredientInfo> ingredients = JEIREI.generateShapelessInputs(recipe);
        slotGroup(widgets, startPoint, x, y, ingredients, (widgets0, startPoint0, ingredient, x0, y0) -> {
            ItemStack[] items = ingredient.ingredient.getItems();
            LEntryWidget slot = REICompat.slot(startPoint, x0, y0, ingredient.isCatalyst ? REICompat.SlotType.CATALYST : REICompat.SlotType.NORMAL);
            slot.entries(EntryIngredients.ofItemStacks(Stream.of(items).map($ -> ingredient.count == 1 ? $ : $.copy()).peek($ -> $.setCount(ingredient.count)).toList()));
            slot.markInput();
            if (!ingredient.tooltips.isEmpty()) {
                slot.addTooltipCallback(tooltip -> {
                    if (tooltip == null) {
                        tooltip = Tooltip.create(new Component[0]);
                    }
                    ingredient.tooltips.forEach(tooltip::add);
                    return tooltip;
                });
            }
            widgets.add(slot);
        });
    }

    public List<Widget> setupDisplay(D display, Rectangle bounds) {
        return Lists.newArrayList(super.setupDisplay(display, bounds));
    }

    public void drawInfoBadge(List<Widget> widgets, D display, Point startPoint) {
        drawInfoBadge(widgets, display.recipe, startPoint, this.infoRect);
    }

    public boolean clickBlock(BlockState state, int button) {
        if (state.m_60713_(Blocks.CHIPPED_ANVIL) || state.m_60713_(Blocks.DAMAGED_ANVIL)) {
            state = Blocks.ANVIL.defaultBlockState();
        }
        ItemStack stack = state.m_60734_().asItem().getDefaultInstance();
        EntryStack<?> entry;
        if (!stack.isEmpty()) {
            entry = EntryStacks.of(stack);
        } else {
            if (!(state.m_60734_() instanceof LiquidBlock)) {
                return false;
            }
            entry = EntryStacks.of(state.m_60819_().getType());
        }
        ViewSearchBuilder searchBuilder = ViewSearchBuilder.builder();
        if (button == 0) {
            searchBuilder.addRecipesFor(entry);
        } else {
            if (button != 1) {
                return false;
            }
            searchBuilder.addUsagesFor(entry);
        }
        searchBuilder.open();
        return true;
    }

    @FunctionalInterface
    public interface SlotLayoutFunction<T> {

        void apply(List<Widget> var1, Point var2, T var3, int var4, int var5);
    }
}