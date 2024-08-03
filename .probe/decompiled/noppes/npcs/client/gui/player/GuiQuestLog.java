package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.gui.player.tabs.InventoryTabFactions;
import noppes.npcs.client.gui.player.tabs.InventoryTabQuests;
import noppes.npcs.client.gui.player.tabs.InventoryTabVanilla;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.shared.client.gui.components.GuiButtonNextPage;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiMenuSideButton;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.ITopButtonListener;
import noppes.npcs.shared.common.util.NaturalOrderComparator;

public class GuiQuestLog extends GuiNPCInterface implements ITopButtonListener, ICustomScrollListener {

    private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/standardbg.png");

    public HashMap<String, List<Quest>> activeQuests = new HashMap();

    private HashMap<String, Quest> categoryQuests = new HashMap();

    public Quest selectedQuest = null;

    public Component selectedCategory = Component.empty();

    private Player player;

    private GuiCustomScrollNop scroll;

    private HashMap<Integer, GuiMenuSideButton> sideButtons = new HashMap();

    private boolean noQuests = false;

    private final int maxLines = 10;

    private int currentPage = 0;

    private int maxPages = 1;

    TextBlockClient textblock = null;

    private Minecraft mc = Minecraft.getInstance();

    public GuiQuestLog(Player player) {
        this.player = player;
        this.imageWidth = 280;
        this.imageHeight = 180;
        this.drawDefaultBackground = false;
    }

    @Override
    public void init() {
        super.m_7856_();
        for (Quest quest : PlayerQuestController.getActiveQuests(this.player)) {
            String category = quest.category.title;
            if (!this.activeQuests.containsKey(category)) {
                this.activeQuests.put(category, new ArrayList());
            }
            List<Quest> list = (List<Quest>) this.activeQuests.get(category);
            list.add(quest);
        }
        this.sideButtons.clear();
        this.guiTop += 10;
        this.m_142416_(new InventoryTabVanilla().init(this));
        this.m_142416_(new InventoryTabFactions().init(this));
        this.m_142416_(new InventoryTabQuests().init(this));
        this.noQuests = false;
        if (this.activeQuests.isEmpty()) {
            this.noQuests = true;
        } else {
            List<String> categories = new ArrayList();
            categories.addAll(this.activeQuests.keySet());
            Collections.sort(categories, new NaturalOrderComparator());
            int i = 0;
            for (String category : categories) {
                if (this.selectedCategory == Component.empty()) {
                    this.selectedCategory = Component.translatable(category);
                }
                this.sideButtons.put(i, new GuiMenuSideButton(this, i, this.guiLeft - 69, this.guiTop + 2 + i * 21, 70, 22, category));
                i++;
            }
            ((GuiMenuSideButton) this.sideButtons.get(categories.indexOf(this.selectedCategory.getString()))).active = true;
            if (this.scroll == null) {
                this.scroll = new GuiCustomScrollNop(this, 0);
            }
            HashMap<String, Quest> categoryQuests = new HashMap();
            for (Quest q : (List) this.activeQuests.get(this.selectedCategory.getString())) {
                categoryQuests.put(q.title, q);
            }
            this.categoryQuests = categoryQuests;
            this.scroll.setList(new ArrayList(categoryQuests.keySet()));
            this.scroll.setSize(134, 174);
            this.scroll.guiLeft = this.guiLeft + 5;
            this.scroll.guiTop = this.guiTop + 15;
            this.addScroll(this.scroll);
            this.addButton(new GuiButtonNextPage(this, 1, this.guiLeft + 286, this.guiTop + 114, true, b -> {
                this.currentPage++;
                this.init();
            }));
            this.addButton(new GuiButtonNextPage(this, 2, this.guiLeft + 144, this.guiTop + 114, false, b -> {
                this.currentPage--;
                this.init();
            }));
            this.getButton(1).f_93624_ = this.selectedQuest != null && this.currentPage < this.maxPages - 1;
            this.getButton(2).f_93624_ = this.selectedQuest != null && this.currentPage > 0;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.scroll != null) {
            this.scroll.visible = !this.noQuests;
        }
        PoseStack matrixStack = graphics.pose();
        this.m_280273_(graphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.resource);
        graphics.blit(this.resource, this.guiLeft, this.guiTop, 0, 0, 252, 195);
        graphics.blit(this.resource, this.guiLeft + 252, this.guiTop, 188, 0, 67, 195);
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        if (this.noQuests) {
            graphics.drawString(this.mc.font, Component.translatable("quest.noquests"), this.guiLeft + 84, this.guiTop + 80, CustomNpcResourceListener.DefaultTextColor);
        } else {
            for (GuiMenuSideButton button : (GuiMenuSideButton[]) this.sideButtons.values().toArray(new GuiMenuSideButton[this.sideButtons.size()])) {
                button.render(graphics, mouseX, mouseY, partialTicks);
            }
            graphics.drawString(this.mc.font, this.selectedCategory, this.guiLeft + 5, this.guiTop + 5, CustomNpcResourceListener.DefaultTextColor);
            if (this.selectedQuest != null) {
                this.drawProgress(graphics);
                this.drawQuestText(graphics);
                matrixStack.pushPose();
                matrixStack.translate((float) (this.guiLeft + 148), (float) this.guiTop, 0.0F);
                matrixStack.scale(1.24F, 1.24F, 1.24F);
                Component title = Component.translatable(this.selectedQuest.title);
                graphics.drawString(this.mc.font, title, (130 - this.f_96547_.width(title)) / 2, 4, CustomNpcResourceListener.DefaultTextColor);
                matrixStack.popPose();
                graphics.hLine(this.guiLeft + 142, this.guiLeft + 312, this.guiTop + 17, -16777216 + CustomNpcResourceListener.DefaultTextColor);
            }
        }
    }

    private void drawQuestText(GuiGraphics graphics) {
        if (this.textblock != null) {
            int yoffset = this.guiTop + 5;
            for (int i = 0; i < 10; i++) {
                int index = i + this.currentPage * 10;
                if (index < this.textblock.lines.size()) {
                    Component text = (Component) this.textblock.lines.get(index);
                    graphics.drawString(this.f_96547_, text, this.guiLeft + 142, this.guiTop + 20 + i * 9, CustomNpcResourceListener.DefaultTextColor);
                }
            }
        }
    }

    private void drawProgress(GuiGraphics graphics) {
        Component title = Component.translatable("quest.objectives").append(":");
        graphics.drawString(this.mc.font, title, this.guiLeft + 142, this.guiTop + 130, CustomNpcResourceListener.DefaultTextColor);
        graphics.hLine(this.guiLeft + 142, this.guiLeft + 312, this.guiTop + 140, -16777216 + CustomNpcResourceListener.DefaultTextColor);
        int yoffset = this.guiTop + 144;
        for (IQuestObjective objective : this.selectedQuest.questInterface.getObjectives(this.player)) {
            graphics.drawString(this.mc.font, Component.literal("- ").append(objective.getMCText()), this.guiLeft + 142, yoffset, CustomNpcResourceListener.DefaultTextColor);
            yoffset += 10;
        }
        graphics.hLine(this.guiLeft + 142, this.guiLeft + 312, this.guiTop + 178, -16777216 + CustomNpcResourceListener.DefaultTextColor);
        String complete = this.selectedQuest.getNpcName();
        if (complete != null && !complete.isEmpty()) {
            graphics.drawString(this.mc.font, Component.translatable("quest.completewith", complete), this.guiLeft + 142, this.guiTop + 182, CustomNpcResourceListener.DefaultTextColor);
        }
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        super.m_6375_(i, j, k);
        if (k == 0) {
            if (this.scroll != null) {
                this.scroll.mouseClicked(i, j, k);
            }
            for (GuiMenuSideButton button : new ArrayList(this.sideButtons.values())) {
                if (button.mouseClicked(i, j, k)) {
                    this.sideButtonPressed(button);
                    return true;
                }
            }
        }
        return false;
    }

    private void sideButtonPressed(GuiMenuSideButton button) {
        if (!button.active) {
            NoppesUtil.clickSound();
            this.selectedCategory = button.m_6035_();
            this.selectedQuest = null;
            this.init();
        }
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
        if (scroll.hasSelected()) {
            this.selectedQuest = (Quest) this.categoryQuests.get(scroll.getSelected());
            this.textblock = new TextBlockClient(this.selectedQuest.getLogText(), 172, true, this.player);
            if (this.textblock.lines.size() > 10) {
                this.maxPages = Mth.ceil(1.0F * (float) this.textblock.lines.size() / 10.0F);
            }
            this.currentPage = 0;
            this.init();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void save() {
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}