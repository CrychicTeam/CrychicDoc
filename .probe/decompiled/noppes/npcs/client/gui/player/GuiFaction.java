package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.player.tabs.InventoryTabFactions;
import noppes.npcs.client.gui.player.tabs.InventoryTabQuests;
import noppes.npcs.client.gui.player.tabs.InventoryTabVanilla;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerFactionData;
import noppes.npcs.shared.client.gui.components.GuiButtonNextPage;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;

public class GuiFaction extends GuiNPCInterface {

    private ArrayList<Faction> playerFactions = new ArrayList();

    private PlayerFactionData data;

    private int page = 0;

    private int pages = 1;

    private GuiButtonNextPage buttonNextPage;

    private GuiButtonNextPage buttonPreviousPage;

    private ResourceLocation indicator;

    public GuiFaction() {
        this.imageWidth = 200;
        this.imageHeight = 195;
        this.drawDefaultBackground = false;
        this.title = "";
        this.indicator = this.getResource("standardbg.png");
    }

    @Override
    public void init() {
        super.m_7856_();
        this.data = PlayerData.get(this.player).factionData;
        this.playerFactions = new ArrayList();
        for (int id : this.data.factionData.keySet()) {
            Faction faction = FactionController.instance.getFaction(id);
            if (faction != null && !faction.hideFaction) {
                this.playerFactions.add(faction);
            }
        }
        this.pages = (this.playerFactions.size() - 1) / 5;
        this.pages++;
        this.page = 1;
        this.guiLeft = (this.f_96543_ - this.imageWidth) / 2;
        this.guiTop += 12;
        this.m_142416_(new InventoryTabVanilla().init(this));
        this.m_142416_(new InventoryTabFactions().init(this));
        this.m_142416_(new InventoryTabQuests().init(this));
        this.addButton(this.buttonNextPage = new GuiButtonNextPage(this, 1, this.guiLeft + this.imageWidth - 43, this.guiTop + 180, true, button -> {
            this.page++;
            this.updateButtons();
        }));
        this.addButton(this.buttonPreviousPage = new GuiButtonNextPage(this, 2, this.guiLeft + 20, this.guiTop + 180, false, button -> {
            this.page--;
            this.updateButtons();
        }));
        this.updateButtons();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.indicator);
        graphics.blit(this.indicator, this.guiLeft, this.guiTop + 8, 0, 0, this.imageWidth, this.imageHeight);
        graphics.blit(this.indicator, this.guiLeft + 4, this.guiTop + 8, 56, 0, 200, this.imageHeight);
        if (this.playerFactions.isEmpty()) {
            Component noFaction = Component.translatable("faction.nostanding");
            Font font = Minecraft.getInstance().font;
            graphics.drawString(font, noFaction, this.guiLeft + (this.imageWidth - font.width(noFaction)) / 2, this.guiTop + 80, CustomNpcResourceListener.DefaultTextColor);
        } else {
            this.renderScreen(graphics);
        }
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    private void renderScreen(GuiGraphics graphics) {
        int size = 5;
        if (this.playerFactions.size() % 5 != 0 && this.page == this.pages) {
            size = this.playerFactions.size() % 5;
        }
        for (int id = 0; id < size; id++) {
            graphics.hLine(this.guiLeft + 2, this.guiLeft + this.imageWidth, this.guiTop + 14 + id * 30, -16777216 + CustomNpcResourceListener.DefaultTextColor);
            Faction faction = (Faction) this.playerFactions.get((this.page - 1) * 5 + id);
            Component name = Component.translatable(faction.name);
            int current = (Integer) this.data.factionData.get(faction.id);
            String points = " : " + current;
            Component standing = Component.translatable("faction.friendly");
            int color = 65280;
            if (current < faction.neutralPoints) {
                standing = Component.translatable("faction.unfriendly");
                color = 16711680;
                points = points + "/" + faction.neutralPoints;
            } else if (current < faction.friendlyPoints) {
                standing = Component.translatable("faction.neutral");
                color = 15924992;
                points = points + "/" + faction.friendlyPoints;
            } else {
                points = points + "/-";
            }
            graphics.drawString(this.f_96547_, name, this.guiLeft + (this.imageWidth - this.f_96547_.width(name)) / 2, this.guiTop + 19 + id * 30, faction.color);
            graphics.drawString(this.f_96547_, standing, this.f_96543_ / 2 - this.f_96547_.width(standing) - 1, this.guiTop + 33 + id * 30, color);
            graphics.drawString(this.f_96547_, points, this.f_96543_ / 2, this.guiTop + 33 + id * 30, CustomNpcResourceListener.DefaultTextColor);
        }
        graphics.hLine(this.guiLeft + 2, this.guiLeft + this.imageWidth, this.guiTop + 14 + size * 30, -16777216 + CustomNpcResourceListener.DefaultTextColor);
        if (this.pages > 1) {
            String s = this.page + "/" + this.pages;
            graphics.drawString(this.f_96547_, s, this.guiLeft + (this.imageWidth - this.f_96547_.width(s)) / 2, this.guiTop + 203, CustomNpcResourceListener.DefaultTextColor);
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton instanceof GuiButtonNextPage) {
            int id = guibutton.id;
            if (id == 1) {
                this.page++;
            }
            if (id == 2) {
                this.page--;
            }
            this.updateButtons();
        }
    }

    private void updateButtons() {
        this.buttonNextPage.f_93624_ = this.page < this.pages;
        this.buttonPreviousPage.f_93624_ = this.page > 1;
    }

    @Override
    public void save() {
    }
}