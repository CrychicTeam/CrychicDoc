package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.client.tooltip.ItemTooltip;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabToken;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.token.ArtifactFilter;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.stacked.CellEntry;
import dev.xkmc.l2library.base.menu.stacked.StackedRenderHandle;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public abstract class StackedScreen extends Screen implements IFilterScreen {

    public final ArtifactChestToken token;

    private final SpriteManager manager;

    private final FilterTabToken<?> tab;

    private int imageWidth;

    private int imageHeight;

    private int leftPos;

    private int topPos;

    protected boolean pressed = false;

    @Nullable
    private StackedScreen.FilterHover hover;

    public static void renderHighlight(GuiGraphics g, int x, int y, int w, int h, int c) {
        g.fillGradient(RenderType.guiOverlay(), x, y, x + w, y + h, c, c, 0);
    }

    protected StackedScreen(Component title, SpriteManager manager, FilterTabToken<?> tab, ArtifactChestToken token) {
        super(title);
        this.token = token;
        this.tab = tab;
        this.manager = manager;
    }

    @Override
    protected void init() {
        this.imageWidth = this.manager.get().getWidth();
        this.imageHeight = this.manager.get().getHeight();
        this.leftPos = (this.f_96543_ - this.imageWidth) / 2;
        this.topPos = (this.f_96544_ - this.imageHeight) / 2;
        new FilterTabManager(this, this.token).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, this.tab);
    }

    protected void renderInit() {
    }

    protected void renderPost(GuiGraphics pose) {
    }

    protected abstract void renderText(StackedRenderHandle var1, int var2, int var3, int var4);

    protected abstract boolean isAvailable(int var1, int var2);

    protected void renderItem(GuiGraphics g, StackedScreen.FilterHover hover) {
        if (hover.item instanceof IArtifactFeature.Sprite icon) {
            g.blit(icon.getIcon(), hover.x, hover.y, 0.0F, 0.0F, 16, 16, 16, 16);
        } else if (hover.item instanceof IArtifactFeature.ItemIcon icon) {
            this.renderSlotItem(g, hover.x, hover.y, icon.getItemIcon().getDefaultInstance());
        }
    }

    @Override
    public final void render(GuiGraphics g, int mx, int my, float pTick) {
        this.renderBg(g, pTick, mx, my);
        g.pose().pushPose();
        g.pose().translate((float) this.leftPos, (float) this.topPos, 0.0F);
        StackedRenderHandle handle = new StackedRenderHandle(this, g, this.manager.get());
        this.hover = null;
        this.renderInit();
        List<StackedScreen.FilterHover> list = new ArrayList();
        for (int i = 0; i < this.token.filters.size(); i++) {
            ArtifactFilter<?> filter = (ArtifactFilter<?>) this.token.filters.get(i);
            this.renderText(handle, i, mx, my);
            for (int j = 0; j < filter.allEntries.size(); j++) {
                boolean selected = filter.getSelected(j);
                IArtifactFeature item = (IArtifactFeature) filter.allEntries.get(j);
                CellEntry entry = handle.addCell(selected, !this.isAvailable(i, j));
                StackedScreen.FilterHover obj = new StackedScreen.FilterHover(item, i, j, entry.x(), entry.y());
                if (this.isHovering(entry.x(), entry.y(), 16, 16, (double) mx, (double) my)) {
                    this.hover = obj;
                }
                list.add(obj);
            }
        }
        handle.flushText();
        list.forEach(e -> this.renderItem(g, e));
        if (this.hover != null) {
            renderHighlight(g, this.hover.x, this.hover.y, 16, 16, -2130706433);
            List<Component> texts = new ArrayList();
            texts.add(this.hover.item().getDesc());
            Optional<TooltipComponent> comp = Optional.ofNullable(this.hover.item().getTooltipItems()).map(ItemTooltip::new);
            g.renderTooltip(this.f_96547_, texts, comp, mx - this.leftPos, my - this.topPos);
        }
        this.renderPost(g);
        g.pose().popPose();
        super.render(g, mx, my, pTick);
    }

    private void renderBg(GuiGraphics g, float pt, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = this.manager.get().new ScreenRenderer(this, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
        sr.start(g);
    }

    protected boolean isHovering(int x, int y, int w, int h, double mx, double my) {
        int i = this.leftPos;
        int j = this.topPos;
        mx -= (double) i;
        my -= (double) j;
        return mx >= (double) (x - 1) && mx < (double) (x + w + 1) && my >= (double) (y - 1) && my < (double) (y + h + 1);
    }

    private void renderSlotItem(GuiGraphics g, int x, int y, ItemStack stack) {
        assert this.f_96541_ != null;
        assert this.f_96541_.player != null;
        g.renderItem(stack, x, y, x + y * this.imageWidth);
        g.renderItemDecorations(this.f_96547_, stack, x, y, null);
    }

    protected abstract void clickHover(int var1, int var2);

    @Override
    public void onSwitch() {
        CompoundTag filter = TagCodec.toTag(new CompoundTag(), this.token);
        assert filter != null;
        ArtifactChestItem.setFilter(Proxy.getClientPlayer().m_150109_().getItem(this.token.invSlot), filter);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        this.pressed = true;
        if (super.m_6375_(mx, my, button)) {
            return true;
        } else if (this.hover != null) {
            this.clickHover(this.hover.i, this.hover.j);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.pressed = false;
        return super.m_6348_(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public int getGuiLeft() {
        return this.leftPos;
    }

    @Override
    public int getGuiTop() {
        return this.topPos;
    }

    @Override
    public int screenWidth() {
        return this.f_96543_;
    }

    @Override
    public int screenHeight() {
        return this.f_96544_;
    }

    @Override
    public int getXSize() {
        return this.imageWidth;
    }

    @Override
    public int getYSize() {
        return this.imageHeight;
    }

    protected static record FilterHover(IArtifactFeature item, int i, int j, int x, int y) {
    }
}