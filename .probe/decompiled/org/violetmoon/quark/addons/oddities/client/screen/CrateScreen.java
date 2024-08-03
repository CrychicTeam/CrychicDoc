package org.violetmoon.quark.addons.oddities.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.inventory.CrateMenu;
import org.violetmoon.quark.addons.oddities.module.CrateModule;
import org.violetmoon.quark.api.IQuarkButtonAllowed;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.quark.base.client.handler.InventoryButtonHandler;
import org.violetmoon.quark.content.client.module.ChestSearchingModule;

public class CrateScreen extends AbstractContainerScreen<CrateMenu> implements IQuarkButtonAllowed {

    private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/gui/crate.png");

    private int lastScroll;

    private int scrollOffs;

    private boolean scrolling;

    private List<Rect2i> extraAreas;

    public CrateScreen(CrateMenu container, Inventory inv, Component component) {
        super(container, inv, component);
        int inventoryRows = 6;
        this.f_97727_ = 114 + inventoryRows * 18;
        this.f_97731_ = this.f_97727_ - 94;
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        this.extraAreas = Lists.newArrayList(new Rect2i[] { new Rect2i(i + this.f_97726_, j, 23, 136) });
    }

    public List<Rect2i> getExtraAreas() {
        return this.extraAreas;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(guiGraphics, mouseX, mouseY);
    }

    private boolean canScroll() {
        return ((CrateMenu) this.f_97732_).getStackCount() / 9 > 0;
    }

    private float getPxPerScroll() {
        return 95.0F / (float) (((CrateMenu) this.f_97732_).getStackCount() / 9);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        ((CrateMenu) this.f_97732_).scroll(delta < 0.0, true);
        this.lastScroll = this.scrollOffs = Math.round((float) (((CrateMenu) this.f_97732_).scroll / 9) * this.getPxPerScroll());
        return true;
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (int2 == 0 && this.insideScrollbar(double0, double1)) {
            this.scrolling = this.canScroll();
            return true;
        } else {
            return super.mouseClicked(double0, double1, int2);
        }
    }

    protected boolean insideScrollbar(double mouseX, double mouseY) {
        int left = this.f_97735_ + 175;
        int top = this.f_97736_ + 18;
        int right = left + 14;
        int bottom = top + 112;
        return mouseX >= (double) left && mouseY >= (double) top && mouseX < (double) right && mouseY < (double) bottom;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int int0, double double1, double double2) {
        if (this.scrolling) {
            int top = this.f_97736_ + 18;
            double relative = mouseY - (double) top - 6.0;
            if (relative < 0.0) {
                relative = 0.0;
            } else if (relative > 95.0) {
                relative = 95.0;
            }
            this.scrollOffs = (int) relative;
            float diff = (float) (this.scrollOffs - this.lastScroll);
            for (float pixelsNeeded = this.getPxPerScroll(); Math.abs(diff) >= pixelsNeeded; diff = (float) (this.scrollOffs - this.lastScroll)) {
                boolean up = diff > 0.0F;
                ((CrateMenu) this.f_97732_).scroll(up, true);
                this.lastScroll = Math.round((float) (((CrateMenu) this.f_97732_).scroll / 9) * pixelsNeeded);
            }
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, int0, double1, double2);
        }
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        if (int2 == 0) {
            this.scrolling = false;
        }
        return super.mouseReleased(double0, double1, int2);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.f_97726_ + 20, this.f_97727_);
        int maxScroll = ((CrateMenu) this.f_97732_).getStackCount() / 9 * 9;
        int u = 232 + (maxScroll == 0 ? 12 : 0);
        int by = j + 18 + this.scrollOffs;
        guiGraphics.blit(TEXTURE, i + this.f_97726_, by, u, 0, 12, 15);
        if (!Quark.ZETA.modules.<ChestSearchingModule>get(ChestSearchingModule.class).searchBarShown()) {
            String s = ((CrateMenu) this.f_97732_).getTotal() + "/" + CrateModule.maxItems;
            int color = ClientUtil.getGuiTextColor("crate_count");
            guiGraphics.drawString(this.f_96547_, s, i + this.f_97726_ - this.f_96547_.width(s) - 8 - InventoryButtonHandler.getActiveButtons(InventoryButtonHandler.ButtonTargetType.CONTAINER_INVENTORY).size() * 12, j + 6, color, false);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int color = ClientUtil.getGuiTextColor("crate_count");
        guiGraphics.drawString(this.f_96547_, this.f_96539_, this.f_97728_, this.f_97729_, color, false);
        guiGraphics.drawString(this.f_96547_, this.f_169604_, this.f_97730_, this.f_97731_, color, false);
    }
}