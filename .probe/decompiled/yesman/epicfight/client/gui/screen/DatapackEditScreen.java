package yesman.epicfight.client.gui.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.compress.utils.Lists;
import yesman.epicfight.api.utils.ParseUtil;
import yesman.epicfight.client.gui.component.ComboBox;
import yesman.epicfight.client.gui.component.Grid;
import yesman.epicfight.client.gui.component.PopupBox;
import yesman.epicfight.client.gui.component.ResizableButton;
import yesman.epicfight.client.gui.component.ResizableComponent;
import yesman.epicfight.client.gui.component.ResizableEditBox;
import yesman.epicfight.client.gui.component.Static;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

@OnlyIn(Dist.CLIENT)
public class DatapackEditScreen extends Screen {

    public static final Component GUI_EXPORT = Component.translatable("gui.epicfight.export");

    private GridLayout bottomButtons;

    private TabNavigationBar tabNavigationBar;

    protected final Screen lastScreen;

    private final TabManager tabManager = new TabManager(x$0 -> {
        AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
    }, p_267853_ -> this.m_169411_(p_267853_)) {

        @Override
        public void setCurrentTab(Tab tab, boolean playSound) {
            if (this.m_267695_() instanceof DatapackEditScreen.DatapackTab<?> datapackTab) {
                DatapackEditScreen.this.m_169411_(datapackTab.grid);
                DatapackEditScreen.this.m_169411_(datapackTab.inputComponentsList);
            }
            super.setCurrentTab(tab, playSound);
            if (tab instanceof DatapackEditScreen.DatapackTab<?> datapackTab) {
                DatapackEditScreen.this.m_142416_(datapackTab.grid);
                DatapackEditScreen.this.m_142416_(datapackTab.inputComponentsList);
            }
        }
    };

    public DatapackEditScreen(Screen parentScreen) {
        super(Component.translatable("gui.epicfight.datapack_edit"));
        this.lastScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.f_96543_).addTabs(new DatapackEditScreen.WeaponTypeTab(), new DatapackEditScreen.ItemCapabilityTab(), new DatapackEditScreen.MobPatchTab()).build();
        this.tabNavigationBar.selectTab(0, false);
        this.m_142416_(this.tabNavigationBar);
        this.bottomButtons = new GridLayout().columnSpacing(10);
        GridLayout.RowHelper gridlayout$rowhelper = this.bottomButtons.createRowHelper(2);
        gridlayout$rowhelper.addChild(Button.builder(GUI_EXPORT, button -> {
        }).build());
        gridlayout$rowhelper.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.f_96541_.setScreen(this.lastScreen)).build());
        this.bottomButtons.m_264134_(button -> {
            button.setTabOrderGroup(1);
            this.m_142416_(button);
        });
        this.repositionElements();
    }

    @Override
    public void repositionElements() {
        if (this.tabNavigationBar != null && this.bottomButtons != null) {
            this.tabNavigationBar.setWidth(this.f_96543_);
            this.tabNavigationBar.arrangeElements();
            this.bottomButtons.arrangeElements();
            FrameLayout.centerInRectangle(this.bottomButtons, 0, this.f_96544_ - 36, this.f_96543_, 36);
            int i = this.tabNavigationBar.getRectangle().bottom();
            ScreenRectangle screenrectangle = new ScreenRectangle(0, i, this.f_96543_, this.bottomButtons.m_252907_() - i);
            this.tabManager.setTabArea(screenrectangle);
        }
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    @Override
    public boolean keyPressed(int keycode, int int0, int int1) {
        if (this.tabNavigationBar.keyPressed(keycode)) {
            return true;
        } else {
            return super.keyPressed(keycode, int0, int1) ? true : keycode == 257 || keycode == 335;
        }
    }

    @Override
    public void setFocused(@Nullable GuiEventListener target) {
        if (this.m_7222_() != target) {
            super.m_7522_(target);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.m_280273_(guiGraphics);
        guiGraphics.blit(CreateWorldScreen.FOOTER_SEPERATOR, 0, Mth.roundToward(this.f_96544_ - 36 - 2, 2), 0.0F, 0.0F, this.f_96543_, 2, 32, 2);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @OnlyIn(Dist.CLIENT)
    class DatapackTab<T> extends GridLayoutTab {

        protected Grid grid;

        protected DatapackEditScreen.DatapackTab<T>.InputComponentsList inputComponentsList;

        protected final IForgeRegistry<T> registry;

        public DatapackTab(Component title, @Nullable IForgeRegistry<T> registry) {
            super(title);
            ScreenRectangle screenRect = DatapackEditScreen.this.m_264198_();
            this.grid = Grid.builder(DatapackEditScreen.this).xy1(8, screenRect.top() + 14).xy2(150, screenRect.height() - screenRect.top() - 7).rowHeight(26).rowEditable(true).transparentBackground(true).addEditboxColumn("pack_item", "epicfight:", true, 150).onAddPress((grid, button) -> {
                if (registry != null) {
                    DatapackEditScreen.this.f_96541_.setScreen(new SelectFromRegistryScreen<>(DatapackEditScreen.this, registry, selItem -> {
                        int rowpositionx = grid.addRow();
                        grid.setGridFocus(rowpositionx, "pack_item");
                    }));
                } else {
                    int rowposition = grid.addRowWithDefaultValues("pack_item", "epicfight:");
                    grid.setGridFocus(rowposition, "pack_item");
                }
                DatapackEditScreen.this.setFocused(grid);
            }).onRemovePress((grid, button) -> {
                int rowposition = grid.removeRow();
                if (rowposition >= 0) {
                    grid.setSelected(rowposition);
                }
            }).buttonHorizontalSizing(ResizableComponent.HorizontalSizing.LEFT_WIDTH).build();
            this.registry = registry;
            this.inputComponentsList = new DatapackEditScreen.DatapackTab.InputComponentsList(screenRect.width() - 172, screenRect.height(), screenRect.top() + 14, screenRect.height() + 7, 30);
            this.inputComponentsList.m_93496_(false);
            this.inputComponentsList.m_93507_(164);
        }

        @Override
        public void doLayout(ScreenRectangle screenRectangle) {
            this.f_267367_.arrangeElements();
            this.f_267367_.m_253211_(screenRectangle.top());
            this.grid.updateSize(150, screenRectangle.height(), screenRectangle.top() + 14, screenRectangle.height() + 7);
            this.inputComponentsList.m_93437_(screenRectangle.width() - 172, screenRectangle.height(), screenRectangle.top() + 14, screenRectangle.height() + 7);
            this.grid.setLeftPos(8);
            this.inputComponentsList.m_93507_(164);
        }

        @Override
        public void tick() {
            this.grid.tick();
        }

        @OnlyIn(Dist.CLIENT)
        class InputComponentsList extends ContainerObjectSelectionList<DatapackEditScreen.DatapackTab<T>.InputComponentsList.InputComponentsEntry> {

            private DatapackEditScreen.DatapackTab<T>.InputComponentsList.InputComponentsEntry lastEntry;

            public InputComponentsList(int width, int height, int y0, int y1, int itemHeight) {
                super(DatapackEditScreen.this.f_96541_, width, height, y0, y1, itemHeight);
            }

            @Override
            public int getRowWidth() {
                return this.f_93388_;
            }

            @Override
            protected int getScrollbarPosition() {
                return this.f_93392_ - 6;
            }

            public int nextStart(int spacing) {
                int xPos;
                if (this.lastEntry.children.size() == 0) {
                    xPos = this.f_93393_;
                } else {
                    ResizableComponent lastWidget = (ResizableComponent) this.lastEntry.children.get(this.lastEntry.children.size() - 1);
                    xPos = lastWidget.getX() + lastWidget.getWidth();
                }
                return xPos + spacing;
            }

            public void addComponentCurrentRow(ResizableComponent inputWidget) {
                this.lastEntry.children.add(inputWidget);
            }

            public void newRow() {
                this.lastEntry = new DatapackEditScreen.DatapackTab.InputComponentsList.InputComponentsEntry();
                this.m_7085_(this.lastEntry);
            }

            @Override
            public boolean mouseClicked(double x, double y, int button) {
                if (!this.m_5953_(x, y)) {
                    return false;
                } else {
                    for (int i = 0; i < this.m_6702_().size(); i++) {
                        DatapackEditScreen.DatapackTab<T>.InputComponentsList.InputComponentsEntry entry = (DatapackEditScreen.DatapackTab.InputComponentsList.InputComponentsEntry) this.m_6702_().get(i);
                        int j1 = this.m_7610_(i);
                        int k1 = this.m_93485_(i);
                        if (k1 >= this.f_93390_ && j1 <= this.f_93391_ && entry.getChildAt(x, y).filter(component -> {
                            if (component.mouseClicked(x, y, button)) {
                                DatapackEditScreen.this.setFocused(component);
                                return true;
                            } else {
                                return false;
                            }
                        }).isPresent()) {
                            return false;
                        }
                    }
                    return false;
                }
            }

            @Override
            public boolean mouseScrolled(double x, double y, double amount) {
                for (int i = 0; i < this.m_6702_().size(); i++) {
                    DatapackEditScreen.DatapackTab<T>.InputComponentsList.InputComponentsEntry entry = (DatapackEditScreen.DatapackTab.InputComponentsList.InputComponentsEntry) this.m_6702_().get(i);
                    int j1 = this.m_7610_(i);
                    int k1 = this.m_93485_(i);
                    if (k1 >= this.f_93390_ && j1 <= this.f_93391_ && entry.getChildAt(x, y).filter(component -> component.mouseScrolled(x, y, amount)).isPresent()) {
                        return true;
                    }
                }
                return super.m_6050_(x, y, amount);
            }

            @OnlyIn(Dist.CLIENT)
            class InputComponentsEntry extends ContainerObjectSelectionList.Entry<DatapackEditScreen.DatapackTab<T>.InputComponentsList.InputComponentsEntry> {

                final List<ResizableComponent> children = Lists.newArrayList();

                @Override
                public Optional<GuiEventListener> getChildAt(double x, double y) {
                    for (GuiEventListener guieventlistener : this.children()) {
                        if (guieventlistener.isMouseOver(x, y)) {
                            return Optional.of(guieventlistener);
                        }
                    }
                    return Optional.empty();
                }

                @Override
                public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                    for (ResizableComponent widget : this.children) {
                        widget.relocateY(top + InputComponentsList.this.f_93387_ / 2 - widget.getHeight() / 2);
                        widget.render(guiGraphics, mouseX, mouseY, partialTicks);
                    }
                }

                @Override
                public List<? extends GuiEventListener> children() {
                    return this.children;
                }

                @Override
                public List<? extends NarratableEntry> narratables() {
                    return this.children;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ItemCapabilityTab extends DatapackEditScreen.DatapackTab<Item> {

        public ItemCapabilityTab() {
            super(Component.translatable("gui.epicfight.tab.datapack.item_capability"), ForgeRegistries.ITEMS);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class MobPatchTab extends DatapackEditScreen.DatapackTab<EntityType<?>> {

        public MobPatchTab() {
            super(Component.translatable("gui.epicfight.tab.datapack.mob_patch"), ForgeRegistries.ENTITY_TYPES);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class WeaponTypeTab extends DatapackEditScreen.DatapackTab<ResourceLocation> {

        public WeaponTypeTab() {
            super(Component.translatable("gui.epicfight.tab.datapack.weapon_type"), null);
            Font font = DatapackEditScreen.this.f_96547_;
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Category")));
            this.inputComponentsList.addComponentCurrentRow(new ComboBox(DatapackEditScreen.this, DatapackEditScreen.this.getMinecraft().font, this.inputComponentsList.nextStart(5), 124, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, 8, Component.translatable("datapack_edit.weapon_type.category"), new ArrayList(WeaponCategory.ENUM_MANAGER.universalValues()), e -> ParseUtil.makeFirstLetterToUpper(e.toString())));
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Hit Particle")));
            this.inputComponentsList.addComponentCurrentRow(new PopupBox<>(DatapackEditScreen.this, font, this.inputComponentsList.nextStart(5), 30, 130, 15, ResizableComponent.HorizontalSizing.LEFT_RIGHT, null, Component.translatable("datapack_edit.weapon_type.hit_particle"), ForgeRegistries.PARTICLE_TYPES));
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Hit Sound")));
            this.inputComponentsList.addComponentCurrentRow(new PopupBox.SoundPopupBox(DatapackEditScreen.this, font, this.inputComponentsList.nextStart(5), 30, 130, 15, ResizableComponent.HorizontalSizing.LEFT_RIGHT, null, Component.translatable("datapack_edit.weapon_type.hit_sound")));
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Swing Sound")));
            this.inputComponentsList.addComponentCurrentRow(new PopupBox.SoundPopupBox(DatapackEditScreen.this, font, this.inputComponentsList.nextStart(5), 30, 130, 15, ResizableComponent.HorizontalSizing.LEFT_RIGHT, null, Component.translatable("datapack_edit.weapon_type.swing_sound")));
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Styles")));
            this.inputComponentsList.addComponentCurrentRow(ResizableButton.builder(Component.literal("..."), button -> {
            }).bounds(this.inputComponentsList.nextStart(4), 0, 15, 15).build());
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Offhand Validator")));
            this.inputComponentsList.addComponentCurrentRow(ResizableButton.builder(Component.literal("..."), button -> {
            }).bounds(this.inputComponentsList.nextStart(4), 0, 15, 15).build());
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Collider")));
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(20), 0, 40, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Count")));
            this.inputComponentsList.addComponentCurrentRow(new ResizableEditBox(font, this.inputComponentsList.nextStart(5), 0, 40, 15, Component.translatable("datapack_edit.weapon_type.number_of_colliders"), ResizableComponent.HorizontalSizing.LEFT_WIDTH, null));
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(20), 0, 40, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Center")));
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(5), 0, 8, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.literal("X: ")));
            this.inputComponentsList.addComponentCurrentRow(new ResizableEditBox(font, this.inputComponentsList.nextStart(5), 0, 30, 15, Component.translatable("datapack_edit.weapon_type.center.x"), ResizableComponent.HorizontalSizing.LEFT_WIDTH, null));
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(8), 0, 8, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.literal("Y: ")));
            this.inputComponentsList.addComponentCurrentRow(new ResizableEditBox(font, this.inputComponentsList.nextStart(5), 0, 30, 15, Component.translatable("datapack_edit.weapon_type.center.y"), ResizableComponent.HorizontalSizing.LEFT_WIDTH, null));
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(8), 0, 8, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.literal("Z: ")));
            this.inputComponentsList.addComponentCurrentRow(new ResizableEditBox(font, this.inputComponentsList.nextStart(5), 0, 30, 15, Component.translatable("datapack_edit.weapon_type.center.z"), ResizableComponent.HorizontalSizing.LEFT_WIDTH, null));
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(20), 0, 40, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Size")));
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(5), 0, 8, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.literal("X: ")));
            this.inputComponentsList.addComponentCurrentRow(new ResizableEditBox(font, this.inputComponentsList.nextStart(5), 0, 30, 15, Component.translatable("datapack_edit.weapon_type.size.x"), ResizableComponent.HorizontalSizing.LEFT_WIDTH, null));
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(8), 0, 8, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.literal("Y: ")));
            this.inputComponentsList.addComponentCurrentRow(new ResizableEditBox(font, this.inputComponentsList.nextStart(5), 0, 30, 15, Component.translatable("datapack_edit.weapon_type.size.y"), ResizableComponent.HorizontalSizing.LEFT_WIDTH, null));
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(8), 0, 8, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.literal("Z: ")));
            this.inputComponentsList.addComponentCurrentRow(new ResizableEditBox(font, this.inputComponentsList.nextStart(5), 0, 30, 15, Component.translatable("datapack_edit.weapon_type.size.z"), ResizableComponent.HorizontalSizing.LEFT_WIDTH, null));
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Combos")));
            this.inputComponentsList.addComponentCurrentRow(ResizableButton.builder(Component.literal("..."), button -> {
            }).bounds(this.inputComponentsList.nextStart(4), 0, 15, 15).build());
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Innate Skill")));
            this.inputComponentsList.newRow();
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(Grid.builder(DatapackEditScreen.this).xy1(this.inputComponentsList.nextStart(5), 0).xy2(20, 90).horizontalSizing(ResizableComponent.HorizontalSizing.LEFT_RIGHT).rowHeight(26).rowEditable(true).transparentBackground(false).addEditboxColumn("pack_item", "epicfight:", true, 150).onAddPress((grid, button) -> {
                int rowposition = grid.addRowWithDefaultValues("pack_item", "epicfight:");
                grid.setGridFocus(rowposition, "pack_item");
                DatapackEditScreen.this.setFocused(grid);
            }).onRemovePress((grid, button) -> {
                int rowposition = grid.removeRow();
                if (rowposition >= 0) {
                    grid.setSelected(rowposition);
                }
            }).buttonHorizontalSizing(ResizableComponent.HorizontalSizing.WIDTH_RIGHT).build());
            this.inputComponentsList.newRow();
            this.inputComponentsList.newRow();
            this.inputComponentsList.addComponentCurrentRow(new Static(font, this.inputComponentsList.nextStart(4), 0, 100, 15, ResizableComponent.HorizontalSizing.LEFT_WIDTH, null, Component.translatable("Living Animations")));
            this.inputComponentsList.addComponentCurrentRow(ResizableButton.builder(Component.literal("..."), button -> {
            }).bounds(this.inputComponentsList.nextStart(4), 0, 15, 15).build());
        }

        @Override
        public void doLayout(ScreenRectangle screenRectangle) {
            super.doLayout(screenRectangle);
            for (DatapackEditScreen.DatapackTab.InputComponentsList.InputComponentsEntry entry : this.inputComponentsList.m_6702_()) {
                for (Object widget : entry.children()) {
                    if (widget instanceof ResizableComponent resizableComponent) {
                        resizableComponent.resize(screenRectangle);
                    }
                }
            }
        }
    }
}