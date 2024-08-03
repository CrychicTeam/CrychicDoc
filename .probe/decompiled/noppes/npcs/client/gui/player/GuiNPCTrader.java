package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerNPCTrader;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;

public class GuiNPCTrader extends GuiContainerNPCInterface<ContainerNPCTrader> {

    private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/trader.png");

    private final ResourceLocation slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");

    private RoleTrader role;

    private ContainerNPCTrader container;

    public GuiNPCTrader(ContainerNPCTrader container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        this.container = container;
        this.role = (RoleTrader) this.npc.role;
        this.f_97727_ = 224;
        this.f_97726_ = 223;
        this.title = "role.trader";
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.m_280273_(graphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.resource);
        graphics.blit(this.resource, this.guiLeft, this.guiTop, 0, 0, this.f_97726_, this.f_97727_);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.slot);
        for (int slot = 0; slot < 18; slot++) {
            int i = this.guiLeft + slot % 3 * 72 + 10;
            int j = this.guiTop + slot / 3 * 21 + 6;
            ItemStack item = this.role.inventoryCurrency.items.get(slot);
            ItemStack item2 = this.role.inventoryCurrency.items.get(slot + 18);
            if (NoppesUtilServer.IsItemStackNull(item)) {
                item = item2;
                item2 = ItemStack.EMPTY;
            }
            if (NoppesUtilPlayer.compareItems(item, item2, false, false)) {
                item = item.copy();
                item.setCount(item.getCount() + item2.getCount());
                item2 = ItemStack.EMPTY;
            }
            ItemStack sold = this.role.inventorySold.items.get(slot);
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, this.slot);
            graphics.blit(this.slot, i + 42, j, 0, 0, 18, 18);
            if (!NoppesUtilServer.IsItemStackNull(item) && !NoppesUtilServer.IsItemStackNull(sold)) {
                if (!NoppesUtilServer.IsItemStackNull(item2)) {
                    graphics.renderItem(item2, i, j + 1);
                    graphics.renderItemDecorations(this.f_96547_, item2, i, j + 1);
                }
                graphics.renderItem(item, i + 18, j + 1);
                graphics.renderItemDecorations(this.f_96547_, item, i + 18, j + 1);
                graphics.drawString(this.f_96547_, "=", i + 36, j + 5, CustomNpcResourceListener.DefaultTextColor);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        for (int slot = 0; slot < 18; slot++) {
            int i = slot % 3 * 72 + 10;
            int j = slot / 3 * 21 + 6;
            ItemStack item = this.role.inventoryCurrency.items.get(slot);
            ItemStack item2 = this.role.inventoryCurrency.items.get(slot + 18);
            if (NoppesUtilServer.IsItemStackNull(item)) {
                item = item2;
                item2 = ItemStack.EMPTY;
            }
            if (NoppesUtilPlayer.compareItems(item, item2, this.role.ignoreDamage, this.role.ignoreNBT)) {
                item = item.copy();
                item.setCount(item.getCount() + item2.getCount());
                item2 = ItemStack.EMPTY;
            }
            ItemStack sold = this.role.inventorySold.items.get(slot);
            if (!NoppesUtilServer.IsItemStackNull(sold)) {
                if (this.m_6774_(i + 43, j + 1, 16, 16, (double) x, (double) y)) {
                    if (!this.container.canBuy(item, item2, this.player)) {
                        graphics.pose().translate(0.0F, 0.0F, 300.0F);
                        if (!item.isEmpty() && !NoppesUtilPlayer.compareItems(this.player, item, this.role.ignoreDamage, this.role.ignoreNBT)) {
                            graphics.fillGradient(i + 17, j, i + 35, j + 18, 1886851088, 1886851088);
                        }
                        if (!item2.isEmpty() && !NoppesUtilPlayer.compareItems(this.player, item2, this.role.ignoreDamage, this.role.ignoreNBT)) {
                            graphics.fillGradient(i - 1, j, i + 17, j + 18, 1886851088, 1886851088);
                        }
                        String title = I18n.get("trader.insufficient");
                        graphics.drawString(this.f_96547_, title, (this.f_97726_ - this.f_96547_.width(title)) / 2, 131, 14483456);
                        graphics.pose().translate(0.0F, 0.0F, -300.0F);
                    } else {
                        String title = I18n.get("trader.sufficient");
                        graphics.drawString(this.f_96547_, title, (this.f_97726_ - this.f_96547_.width(title)) / 2, 131, 56576);
                    }
                }
                if (this.m_6774_(i, j, 16, 16, (double) x, (double) y) && !NoppesUtilServer.IsItemStackNull(item2)) {
                    graphics.renderTooltip(this.f_96547_, item2, x - this.guiLeft, y - this.guiTop);
                }
                if (this.m_6774_(i + 18, j, 16, 16, (double) x, (double) y)) {
                    graphics.renderTooltip(this.f_96547_, item, x - this.guiLeft, y - this.guiTop);
                }
            }
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
    }

    @Override
    public void save() {
    }
}