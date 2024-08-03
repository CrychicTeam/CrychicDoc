package dev.xkmc.l2artifacts.content.search.tabs;

import dev.xkmc.l2artifacts.content.search.augment.AugmentTab;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveTab;
import dev.xkmc.l2artifacts.content.search.filter.FilterTab;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredTab;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleTab;
import dev.xkmc.l2artifacts.content.search.shape.ShapeTab;
import dev.xkmc.l2artifacts.content.search.sort.SortTab;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeTab;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class FilterTabManager {

    public static final FilterTabToken<FilteredTab> FILTERED = new FilterTabToken<>(FilteredTab::new, () -> Items.CHEST, LangData.TAB_FILTERED.get());

    public static final FilterTabToken<FilterTab> FILTER = new FilterTabToken<>(FilterTab::new, () -> Items.HOPPER, LangData.TAB_FILTER.get());

    public static final FilterTabToken<SortTab> SORT = new FilterTabToken<>(SortTab::new, () -> Items.COMPARATOR, LangData.TAB_SORT.get());

    public static final FilterTabToken<RecycleTab> RECYCLE = new FilterTabToken<>(RecycleTab::new, () -> Items.COMPOSTER, LangData.TAB_RECYCLE.get());

    public static final FilterTabToken<UpgradeTab> UPGRADE = new FilterTabToken<>(UpgradeTab::new, () -> Items.ANVIL, LangData.TAB_UPGRADE.get());

    public static final FilterTabToken<DissolveTab> DISSOLVE = new FilterTabToken<>(DissolveTab::new, () -> (Item) ArtifactItems.ITEM_STAT[4].get(), LangData.TAB_DISSOLVE.get());

    public static final FilterTabToken<AugmentTab> AUGMENT = new FilterTabToken<>(AugmentTab::new, () -> (Item) ArtifactItems.ITEM_BOOST_MAIN[4].get(), LangData.TAB_AUGMENT.get());

    public static final FilterTabToken<ShapeTab> SHAPE = new FilterTabToken<>(ShapeTab::new, ArtifactItems.SELECT::get, LangData.TAB_SHAPE.get());

    private static final List<FilterTabToken<?>> LIST_0 = List.of(FILTERED, FILTER, SORT, RECYCLE, UPGRADE);

    private static final List<FilterTabToken<?>> LIST_1 = List.of(FILTERED, FILTER, SORT, RECYCLE, DISSOLVE, AUGMENT, SHAPE);

    protected final List<FilterTabBase<?>> list = new ArrayList();

    public final IFilterScreen screen;

    public final ArtifactChestToken token;

    public int tabPage;

    public FilterTabToken<?> selected;

    public FilterTabManager(IFilterScreen screen, ArtifactChestToken token) {
        this.screen = screen;
        this.token = token;
    }

    public void init(Consumer<AbstractWidget> adder, FilterTabToken<?> selected) {
        List<FilterTabToken<?>> token_list = this.token.stack.getItem() == ArtifactItems.FILTER.get() ? LIST_0 : LIST_1;
        this.list.clear();
        this.selected = selected;
        int guiLeft = this.screen.getGuiLeft();
        int guiTop = this.screen.getGuiTop();
        int imgWidth = this.screen.getXSize();
        for (int i = 0; i < token_list.size(); i++) {
            FilterTabToken<?> token = (FilterTabToken<?>) token_list.get(i);
            FilterTabBase<?> tab = token.create(i, this);
            tab.m_252865_(guiLeft + imgWidth + FilterTabType.RIGHT.getX(tab.index));
            tab.m_253211_(guiTop + FilterTabType.RIGHT.getY(tab.index));
            adder.accept(tab);
            this.list.add(tab);
        }
        this.updateVisibility();
    }

    private void updateVisibility() {
        for (FilterTabBase<?> tab : this.list) {
            tab.f_93624_ = tab.index >= this.tabPage * 8 && tab.index < (this.tabPage + 1) * 8;
            tab.f_93623_ = tab.f_93624_;
        }
    }

    public Screen getScreen() {
        return this.screen.asScreen();
    }

    public void onToolTipRender(GuiGraphics stack, int mouseX, int mouseY) {
        for (FilterTabBase<?> tab : this.list) {
            if (tab.f_93624_ && tab.m_198029_()) {
                tab.onTooltip(stack, mouseX, mouseY);
            }
        }
    }
}