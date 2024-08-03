package harmonised.pmmo.client.gui;

import harmonised.pmmo.client.gui.component.PMMOButton;
import harmonised.pmmo.client.gui.component.PlayerStatsComponent;
import harmonised.pmmo.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;

public class PlayerStatsScreen extends EffectRenderingInventoryScreen<InventoryMenu> {

    public static final ResourceLocation PLAYER_STATS_LOCATION = new ResourceLocation("pmmo", "textures/gui/player_stats.png");

    private final PlayerStatsComponent playerStatsComponent = new PlayerStatsComponent();

    public float xMouse;

    public float yMouse;

    public boolean widthTooNarrow;

    public PlayerStatsScreen(Player player) {
        super(player.inventoryMenu, player.getInventory(), Component.translatable("container.crafting"));
        this.f_97728_ = 97;
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.widthTooNarrow = this.f_96543_ < 379;
        this.playerStatsComponent.init(this.f_96543_, this.f_96544_, this.f_96541_, this.widthTooNarrow);
        this.playerStatsComponent.toggleVisibility();
        this.f_97735_ = this.playerStatsComponent.updateScreenPosition(this.f_96543_, this.f_97726_);
        this.m_142416_(new PMMOButton(this, this.f_97735_ + Config.SKILL_BUTTON_X.get() - 22, this.f_96544_ / 2 + Config.SKILL_BUTTON_Y.get(), 20, 18, 148, 0, 19));
        this.m_7787_(this.playerStatsComponent);
    }

    @Override
    protected void containerTick() {
        this.playerStatsComponent.tick();
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.m_280273_(graphics);
        if (this.playerStatsComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
            this.playerStatsComponent.m_88315_(graphics, pMouseX, pMouseY, pPartialTick);
        } else {
            this.playerStatsComponent.m_88315_(graphics, pMouseX, pMouseY, pPartialTick);
            super.render(graphics, pMouseX, pMouseY, pPartialTick);
        }
        this.m_280072_(graphics, pMouseX, pMouseY);
        this.xMouse = (float) pMouseX;
        this.yMouse = (float) pMouseY;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.f_96547_, this.f_96539_, this.f_97728_, this.f_97729_, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float float0, int int1, int int2) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.f_97735_;
        int j = this.f_97736_;
        graphics.blit(f_97725_, i, j, 0, 0, this.f_97726_, this.f_97727_);
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, i + 51, j + 75, 30, (float) (i + 51) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, this.f_96541_.player);
    }

    @Override
    protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        return (!this.widthTooNarrow || !this.playerStatsComponent.isVisible()) && super.m_6774_(pX, pY, pWidth, pHeight, pMouseX, pMouseY);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.playerStatsComponent.mouseClicked(pMouseX, pMouseY, pButton)) {
            this.m_7522_(this.playerStatsComponent);
            return true;
        } else {
            return (!this.widthTooNarrow || !this.playerStatsComponent.isVisible()) && super.m_6375_(pMouseX, pMouseY, pButton);
        }
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return this.playerStatsComponent.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY) ? true : super.m_7979_(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return this.playerStatsComponent.mouseScrolled(pMouseX, pMouseY, pDelta) ? true : super.m_6050_(pMouseX, pMouseY, pDelta);
    }

    public PlayerStatsComponent getPlayerStatsComponent() {
        return this.playerStatsComponent;
    }
}