package me.shedaniel.clothconfig2.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ScissorsHandler;
import me.shedaniel.clothconfig2.api.ScrollingContainer;
import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.clothconfig2.api.animator.ValueAnimator;
import me.shedaniel.clothconfig2.gui.entries.EmptyEntry;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import me.shedaniel.clothconfig2.gui.widget.SearchFieldEntry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class ClothConfigScreen extends AbstractTabbedConfigScreen {

    private final ScrollingContainer tabsScroller = new ScrollingContainer() {

        @Override
        public Rectangle getBounds() {
            return new Rectangle(0, 0, 1, ClothConfigScreen.this.f_96543_ - 40);
        }

        @Override
        public int getMaxScrollHeight() {
            return (int) ClothConfigScreen.this.getTabsMaximumScrolled();
        }

        @Override
        public void updatePosition(float delta) {
            super.updatePosition(delta);
            this.scrollAmount = this.clamp(this.scrollAmount, 0.0);
        }
    };

    public ClothConfigScreen.ListWidget<AbstractConfigEntry<AbstractConfigEntry<?>>> listWidget;

    private final LinkedHashMap<Component, List<AbstractConfigEntry<?>>> categorizedEntries = Maps.newLinkedHashMap();

    private final List<Tuple<Component, Integer>> tabs;

    private SearchFieldEntry searchFieldEntry;

    private AbstractWidget buttonLeftTab;

    private AbstractWidget buttonRightTab;

    private Rectangle tabsBounds;

    private Rectangle tabsLeftBounds;

    private Rectangle tabsRightBounds;

    private double tabsMaximumScrolled = -1.0;

    private final List<ClothConfigTabButton> tabButtons = Lists.newArrayList();

    private final Map<String, ConfigCategory> categoryMap;

    @Internal
    public ClothConfigScreen(Screen parent, Component title, Map<String, ConfigCategory> categoryMap, ResourceLocation backgroundLocation) {
        super(parent, title, backgroundLocation);
        categoryMap.forEach((categoryName, category) -> {
            List<AbstractConfigEntry<?>> entries = Lists.newArrayList();
            for (Object object : category.getEntries()) {
                AbstractConfigListEntry<?> entry;
                if (object instanceof Tuple) {
                    entry = (AbstractConfigListEntry<?>) ((Tuple) object).getB();
                } else {
                    entry = (AbstractConfigListEntry<?>) object;
                }
                entry.setScreen(this);
                entries.add(entry);
            }
            this.categorizedEntries.put(category.getCategoryKey(), entries);
            if (category.getBackground() != null) {
                this.registerCategoryBackground(category.getCategoryKey().getString(), category.getBackground());
            }
        });
        this.tabs = (List<Tuple<Component, Integer>>) this.categorizedEntries.keySet().stream().map(s -> new Tuple<>(s, Minecraft.getInstance().font.width(s) + 8)).collect(Collectors.toList());
        this.categoryMap = categoryMap;
    }

    @Override
    public Component getSelectedCategory() {
        return (Component) ((Tuple) this.tabs.get(this.selectedCategoryIndex)).getA();
    }

    @Override
    public Map<Component, List<AbstractConfigEntry<?>>> getCategorizedEntries() {
        return this.categorizedEntries;
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.tabButtons.clear();
        this.childrenL().add(this.listWidget = new ClothConfigScreen.ListWidget<>(this, this.f_96541_, this.f_96543_, this.f_96544_, this.isShowingTabs() ? 70 : 30, this.f_96544_ - 32, this.getBackgroundLocation()));
        this.listWidget.children().add(new EmptyEntry(5));
        this.listWidget.children().add(this.searchFieldEntry = new SearchFieldEntry(this, this.listWidget));
        this.listWidget.children().add(new EmptyEntry(5));
        if (this.categorizedEntries.size() > this.selectedCategoryIndex) {
            this.listWidget.children().addAll((List) Lists.newArrayList(this.categorizedEntries.values()).get(this.selectedCategoryIndex));
        }
        int buttonWidths = Math.min(200, (this.f_96543_ - 50 - 12) / 3);
        this.m_142416_(Button.builder(this.isEdited() ? Component.translatable("text.cloth-config.cancel_discard") : Component.translatable("gui.cancel"), widget -> this.quit()).bounds(this.f_96543_ / 2 - buttonWidths - 3, this.f_96544_ - 26, buttonWidths, 20).build());
        this.m_142416_(new Button(this.f_96543_ / 2 + 3, this.f_96544_ - 26, buttonWidths, 20, Component.empty(), button -> this.saveAll(true), Supplier::get) {

            @Override
            public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
                boolean hasErrors = false;
                label40: for (List<AbstractConfigEntry<?>> entries : Lists.newArrayList(ClothConfigScreen.this.categorizedEntries.values())) {
                    Iterator var8 = entries.iterator();
                    while (true) {
                        if (var8.hasNext()) {
                            AbstractConfigEntry<?> entry = (AbstractConfigEntry<?>) var8.next();
                            if (!entry.getConfigError().isPresent()) {
                                continue;
                            }
                            hasErrors = true;
                        }
                        if (hasErrors) {
                            break label40;
                        }
                        break;
                    }
                }
                this.f_93623_ = ClothConfigScreen.this.isEdited() && !hasErrors;
                this.m_93666_(hasErrors ? Component.translatable("text.cloth-config.error_cannot_save") : Component.translatable("text.cloth-config.save_and_done"));
                super.m_88315_(graphics, mouseX, mouseY, delta);
            }
        });
        if (this.isShowingTabs()) {
            this.tabsBounds = new Rectangle(0, 41, this.f_96543_, 24);
            this.tabsLeftBounds = new Rectangle(0, 41, 18, 24);
            this.tabsRightBounds = new Rectangle(this.f_96543_ - 18, 41, 18, 24);
            this.childrenL().add(this.buttonLeftTab = new Button(4, 44, 12, 18, Component.empty(), button -> this.tabsScroller.scrollTo(0.0, true), Supplier::get) {

                @Override
                public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
                    RenderSystem.setShader(GameRenderer::m_172817_);
                    RenderSystem.setShaderTexture(0, AbstractConfigScreen.CONFIG_TEX);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFuncSeparate(770, 771, 0, 1);
                    RenderSystem.blendFunc(770, 771);
                    graphics.blit(AbstractConfigScreen.CONFIG_TEX, this.m_252754_(), this.m_252907_(), 12, 18 * (!this.m_142518_() ? 0 : (this.m_198029_() ? 2 : 1)), this.f_93618_, this.f_93619_);
                }
            });
            int j = 0;
            for (Tuple<Component, Integer> tab : this.tabs) {
                this.tabButtons.add(new ClothConfigTabButton(this, j, -100, 43, tab.getB(), 20, tab.getA(), ((ConfigCategory) this.categoryMap.get(tab.getA().getString())).getDescription()));
                j++;
            }
            this.childrenL().addAll(this.tabButtons);
            this.childrenL().add(this.buttonRightTab = new Button(this.f_96543_ - 16, 44, 12, 18, Component.empty(), button -> this.tabsScroller.scrollTo((double) this.tabsScroller.getMaxScroll(), true), Supplier::get) {

                @Override
                public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
                    RenderSystem.setShader(GameRenderer::m_172817_);
                    RenderSystem.setShaderTexture(0, AbstractConfigScreen.CONFIG_TEX);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFuncSeparate(770, 771, 0, 1);
                    RenderSystem.blendFunc(770, 771);
                    graphics.blit(AbstractConfigScreen.CONFIG_TEX, this.m_252754_(), this.m_252907_(), 0, 18 * (!this.m_142518_() ? 0 : (this.m_198029_() ? 2 : 1)), this.f_93618_, this.f_93619_);
                }
            });
        } else {
            this.tabsBounds = this.tabsLeftBounds = this.tabsRightBounds = new Rectangle();
        }
        Optional.ofNullable(this.afterInitConsumer).ifPresent(consumer -> consumer.accept(this));
    }

    @Override
    public boolean matchesSearch(Iterator<String> tags) {
        return this.searchFieldEntry.matchesSearch(tags);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.tabsBounds.contains(mouseX, mouseY) && !this.tabsLeftBounds.contains(mouseX, mouseY) && !this.tabsRightBounds.contains(mouseX, mouseY) && amount != 0.0) {
            this.tabsScroller.offset(-amount * 16.0, true);
            return true;
        } else {
            return super.m_6050_(mouseX, mouseY, amount);
        }
    }

    public double getTabsMaximumScrolled() {
        if (this.tabsMaximumScrolled == -1.0) {
            int[] i = new int[] { 0 };
            for (Tuple<Component, Integer> pair : this.tabs) {
                i[0] += pair.getB() + 2;
            }
            this.tabsMaximumScrolled = (double) i[0];
        }
        return this.tabsMaximumScrolled + 6.0;
    }

    public void resetTabsMaximumScrolled() {
        this.tabsMaximumScrolled = -1.0;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.isShowingTabs()) {
            this.tabsScroller.updatePosition(delta * 3.0F);
            int xx = 24 - (int) this.tabsScroller.scrollAmount;
            for (ClothConfigTabButton tabButton : this.tabButtons) {
                tabButton.m_252865_(xx);
                xx += tabButton.m_5711_() + 2;
            }
            this.buttonLeftTab.active = this.tabsScroller.scrollAmount > 0.0;
            this.buttonRightTab.active = this.tabsScroller.scrollAmount < this.getTabsMaximumScrolled() - (double) this.f_96543_ + 40.0;
        }
        if (this.isTransparentBackground()) {
            graphics.fillGradient(0, 0, this.f_96543_, this.f_96544_, -1072689136, -804253680);
        } else {
            this.m_280039_(graphics);
        }
        this.listWidget.m_88315_(graphics, mouseX, mouseY, delta);
        ScissorsHandler.INSTANCE.scissor(new Rectangle(this.listWidget.left, this.listWidget.top, this.listWidget.width, this.listWidget.bottom - this.listWidget.top));
        for (AbstractConfigEntry child : this.listWidget.children()) {
            child.lateRender(graphics, mouseX, mouseY, delta);
        }
        ScissorsHandler.INSTANCE.removeLastScissor();
        if (this.isShowingTabs()) {
            graphics.drawCenteredString(this.f_96541_.font, this.f_96539_, this.f_96543_ / 2, 18, -1);
            Rectangle onlyInnerTabBounds = new Rectangle(this.tabsBounds.x + 20, this.tabsBounds.y, this.tabsBounds.width - 40, this.tabsBounds.height);
            ScissorsHandler.INSTANCE.scissor(onlyInnerTabBounds);
            if (this.isTransparentBackground()) {
                graphics.fillGradient(onlyInnerTabBounds.x, onlyInnerTabBounds.y, onlyInnerTabBounds.getMaxX(), onlyInnerTabBounds.getMaxY(), 1744830464, 1744830464);
            } else {
                this.overlayBackground(graphics, onlyInnerTabBounds, 32, 32, 32, 255, 255);
            }
            this.tabButtons.forEach(widget -> widget.render(graphics, mouseX, mouseY, delta));
            this.drawTabsShades(graphics, 0, this.isTransparentBackground() ? 120 : 255);
            ScissorsHandler.INSTANCE.removeLastScissor();
            this.buttonLeftTab.render(graphics, mouseX, mouseY, delta);
            this.buttonRightTab.render(graphics, mouseX, mouseY, delta);
        } else {
            graphics.drawCenteredString(this.f_96541_.font, this.f_96539_, this.f_96543_ / 2, 12, -1);
        }
        if (this.isEditable()) {
            List<Component> errors = Lists.newArrayList();
            for (List<AbstractConfigEntry<?>> entries : Lists.newArrayList(this.categorizedEntries.values())) {
                for (AbstractConfigEntry<?> entry : entries) {
                    if (entry.getConfigError().isPresent()) {
                        errors.add((Component) entry.getConfigError().get());
                    }
                }
            }
            if (errors.size() > 0) {
                RenderSystem.setShader(GameRenderer::m_172817_);
                RenderSystem.setShaderTexture(0, CONFIG_TEX);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                String text = "§c" + (errors.size() == 1 ? ((Component) errors.get(0)).plainCopy().getString() : I18n.get("text.cloth-config.multi_error"));
                if (this.isTransparentBackground()) {
                    int stringWidth = this.f_96541_.font.width(text);
                    graphics.fillGradient(8, 9, 20 + stringWidth, 14 + 9, 1744830464, 1744830464);
                }
                graphics.blit(CONFIG_TEX, 10, 10, 0, 54, 3, 11);
                graphics.drawString(this.f_96541_.font, text, 18, 12, -1);
                if (errors.size() > 1) {
                    int stringWidth = this.f_96541_.font.width(text);
                    if (mouseX >= 10 && mouseY >= 10 && mouseX <= 18 + stringWidth && mouseY <= 14 + 9) {
                        this.addTooltip(Tooltip.of(new Point(mouseX, mouseY), (Component[]) errors.toArray(new Component[0])));
                    }
                }
            }
        } else if (!this.isEditable()) {
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderTexture(0, CONFIG_TEX);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            String textx = "§c" + I18n.get("text.cloth-config.not_editable");
            if (this.isTransparentBackground()) {
                int stringWidth = this.f_96541_.font.width(textx);
                graphics.fillGradient(8, 9, 20 + stringWidth, 14 + 9, 1744830464, 1744830464);
            }
            graphics.blit(CONFIG_TEX, 10, 10, 0, 54, 3, 11);
            graphics.drawString(this.f_96541_.font, textx, 18, 12, -1);
        }
        super.m_88315_(graphics, mouseX, mouseY, delta);
    }

    private void drawTabsShades(GuiGraphics graphics, int lightColor, int darkColor) {
        this.drawTabsShades(graphics.pose(), lightColor, darkColor);
    }

    private void drawTabsShades(PoseStack matrices, int lightColor, int darkColor) {
        this.drawTabsShades(matrices.last().pose(), lightColor, darkColor);
    }

    private void drawTabsShades(Matrix4f matrix, int lightColor, int darkColor) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 0, 1);
        RenderSystem.setShader(GameRenderer::m_172820_);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.m_252986_(matrix, (float) (this.tabsBounds.getMinX() + 20), (float) (this.tabsBounds.getMinY() + 4), 0.0F).uv(0.0F, 1.0F).color(0, 0, 0, lightColor).endVertex();
        buffer.m_252986_(matrix, (float) (this.tabsBounds.getMaxX() - 20), (float) (this.tabsBounds.getMinY() + 4), 0.0F).uv(1.0F, 1.0F).color(0, 0, 0, lightColor).endVertex();
        buffer.m_252986_(matrix, (float) (this.tabsBounds.getMaxX() - 20), (float) this.tabsBounds.getMinY(), 0.0F).uv(1.0F, 0.0F).color(0, 0, 0, darkColor).endVertex();
        buffer.m_252986_(matrix, (float) (this.tabsBounds.getMinX() + 20), (float) this.tabsBounds.getMinY(), 0.0F).uv(0.0F, 0.0F).color(0, 0, 0, darkColor).endVertex();
        buffer.m_252986_(matrix, (float) (this.tabsBounds.getMinX() + 20), (float) this.tabsBounds.getMaxY(), 0.0F).uv(0.0F, 1.0F).color(0, 0, 0, darkColor).endVertex();
        buffer.m_252986_(matrix, (float) (this.tabsBounds.getMaxX() - 20), (float) this.tabsBounds.getMaxY(), 0.0F).uv(1.0F, 1.0F).color(0, 0, 0, darkColor).endVertex();
        buffer.m_252986_(matrix, (float) (this.tabsBounds.getMaxX() - 20), (float) (this.tabsBounds.getMaxY() - 4), 0.0F).uv(1.0F, 0.0F).color(0, 0, 0, lightColor).endVertex();
        buffer.m_252986_(matrix, (float) (this.tabsBounds.getMinX() + 20), (float) (this.tabsBounds.getMaxY() - 4), 0.0F).uv(0.0F, 0.0F).color(0, 0, 0, lightColor).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();
    }

    @Override
    public void save() {
        super.save();
    }

    @Override
    public boolean isEditable() {
        return super.isEditable();
    }

    public static class ListWidget<R extends DynamicElementListWidget.ElementEntry<R>> extends DynamicElementListWidget<R> {

        private final AbstractConfigScreen screen;

        private final ValueAnimator<Rectangle> currentBounds = ValueAnimator.ofRectangle();

        public UnaryOperator<List<R>> entriesTransformer = UnaryOperator.identity();

        public Rectangle thisTimeTarget;

        public long lastTouch;

        public ListWidget(AbstractConfigScreen screen, Minecraft client, int width, int height, int top, int bottom, ResourceLocation backgroundLocation) {
            super(client, width, height, top, bottom, backgroundLocation);
            this.setRenderSelection(false);
            this.screen = screen;
        }

        @Override
        public int getItemWidth() {
            return this.width - 80;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.left + this.width - 36;
        }

        protected void renderItem(GuiGraphics graphics, R item, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            if (item instanceof AbstractConfigEntry) {
                ((AbstractConfigEntry) item).updateSelected(this.getFocused() == item);
            }
            super.renderItem(graphics, item, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
        }

        @Override
        protected void renderList(GuiGraphics graphics, int startX, int startY, int mouseX, int mouseY, float delta) {
            this.thisTimeTarget = null;
            Rectangle hoverBounds = this.currentBounds.value();
            if (!hoverBounds.isEmpty()) {
                long timePast = System.currentTimeMillis() - this.lastTouch;
                int alpha = timePast <= 200L ? 255 : Mth.ceil(255.0 - (double) (Math.min((float) (timePast - 200L), 500.0F) / 500.0F) * 255.0);
                alpha = alpha * 36 / 255 << 24;
                graphics.fillGradient(hoverBounds.x, hoverBounds.y - (int) this.scroll, hoverBounds.getMaxX(), hoverBounds.getMaxY() - (int) this.scroll, 16777215 | alpha, 16777215 | alpha);
            }
            super.renderList(graphics, startX, startY, mouseX, mouseY, delta);
            if (this.thisTimeTarget != null && this.m_5953_((double) mouseX, (double) mouseY)) {
                this.lastTouch = System.currentTimeMillis();
            }
            if (this.thisTimeTarget != null && !this.thisTimeTarget.equals(this.currentBounds.target())) {
                this.currentBounds.setTo(this.thisTimeTarget, 100L);
            } else if (!this.currentBounds.target().isEmpty()) {
                this.currentBounds.update((double) delta);
            }
        }

        protected static void fillGradient(Matrix4f matrix4f, BufferBuilder bufferBuilder, double xStart, double yStart, double xEnd, double yEnd, int i, int j, int k) {
            float f = (float) (j >> 24 & 0xFF) / 255.0F;
            float g = (float) (j >> 16 & 0xFF) / 255.0F;
            float h = (float) (j >> 8 & 0xFF) / 255.0F;
            float l = (float) (j & 0xFF) / 255.0F;
            float m = (float) (k >> 24 & 0xFF) / 255.0F;
            float n = (float) (k >> 16 & 0xFF) / 255.0F;
            float o = (float) (k >> 8 & 0xFF) / 255.0F;
            float p = (float) (k & 0xFF) / 255.0F;
            bufferBuilder.m_252986_(matrix4f, (float) xEnd, (float) yStart, (float) i).color(g, h, l, f).endVertex();
            bufferBuilder.m_252986_(matrix4f, (float) xStart, (float) yStart, (float) i).color(g, h, l, f).endVertex();
            bufferBuilder.m_252986_(matrix4f, (float) xStart, (float) yEnd, (float) i).color(n, o, p, m).endVertex();
            bufferBuilder.m_252986_(matrix4f, (float) xEnd, (float) yEnd, (float) i).color(n, o, p, m).endVertex();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.updateScrollingState(mouseX, mouseY, button);
            if (!this.m_5953_(mouseX, mouseY)) {
                return false;
            } else {
                for (R entry : this.children()) {
                    if (entry.mouseClicked(mouseX, mouseY, button)) {
                        this.m_7522_(entry);
                        this.m_7897_(true);
                        return true;
                    }
                }
                if (button == 0) {
                    this.clickedHeader((int) (mouseX - (double) (this.left + this.width / 2 - this.getItemWidth() / 2)), (int) (mouseY - (double) this.top) + (int) this.getScroll() - 4);
                    return true;
                } else {
                    return this.scrolling;
                }
            }
        }

        @Override
        protected void renderBackBackground(GuiGraphics graphics, BufferBuilder buffer, Tesselator tessellator) {
            if (!this.screen.isTransparentBackground()) {
                super.renderBackBackground(graphics, buffer, tessellator);
            } else {
                graphics.fillGradient(this.left, this.top, this.right, this.bottom, 1744830464, 1744830464);
            }
        }

        @Override
        protected void renderHoleBackground(GuiGraphics graphics, int y1, int y2, int alpha1, int alpha2) {
            if (!this.screen.isTransparentBackground()) {
                super.renderHoleBackground(graphics, y1, y2, alpha1, alpha2);
            }
        }

        @Override
        public List<R> children() {
            return (List<R>) this.entriesTransformer.apply(super.m_6702_());
        }
    }
}