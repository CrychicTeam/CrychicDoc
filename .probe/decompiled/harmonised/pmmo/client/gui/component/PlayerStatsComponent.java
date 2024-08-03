package harmonised.pmmo.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.IDataStorage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;

public class PlayerStatsComponent extends AbstractWidget {

    protected static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("pmmo", "textures/gui/player_stats.png");

    protected static final Core core = Core.get(LogicalSide.CLIENT);

    protected Minecraft minecraft;

    private boolean visible;

    private int xOffset;

    private int width;

    private int height;

    private boolean widthTooNarrow;

    private int timesInventoryChanged;

    protected PlayerStatsComponent.PlayerStatsScroller statsScroller;

    public static final int IMAGE_WIDTH = 147;

    public static final int IMAGE_HEIGHT = 166;

    private static final int OFFSET_X_POSITION = 86;

    public PlayerStatsComponent() {
        super(0, 0, 0, 0, Component.empty());
    }

    public void init(int width, int height, Minecraft minecraft, boolean widthTooNarrow) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.widthTooNarrow = widthTooNarrow;
        this.timesInventoryChanged = minecraft.player.m_150109_().getTimesChanged();
        if (this.visible) {
            this.initVisuals();
        }
    }

    public void tick() {
        if (this.isVisible() && this.timesInventoryChanged != this.minecraft.player.m_150109_().getTimesChanged()) {
            this.timesInventoryChanged = this.minecraft.player.m_150109_().getTimesChanged();
        }
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible()) {
            graphics.pose().pushPose();
            graphics.pose().translate(0.0, 0.0, 120.0);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = (this.width - 147) / 2 - this.xOffset;
            int j = (this.height - 166) / 2;
            graphics.blit(TEXTURE_LOCATION, i, j, 0, 0, 147, 166);
            this.statsScroller.m_88315_(graphics, mouseX, mouseY, partialTicks);
            graphics.pose().popPose();
        }
    }

    public void initVisuals() {
        this.xOffset = this.widthTooNarrow ? 0 : 86;
        int i = (this.width - 147) / 2 - this.xOffset;
        int j = (this.height - 166) / 2;
        this.statsScroller = new PlayerStatsComponent.PlayerStatsScroller(Minecraft.getInstance(), 131, 150, j + 8, i + 8);
        this.statsScroller.populateAbilities(core, this.minecraft);
    }

    public int updateScreenPosition(int x, int y) {
        int i;
        if (this.isVisible() && !this.widthTooNarrow) {
            i = 177 + (x - y - 200) / 2;
        } else {
            i = (x - y) / 2;
        }
        return i;
    }

    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }

    public boolean isVisible() {
        return this.visible;
    }

    protected void setVisible(boolean visible) {
        if (visible) {
            this.initVisuals();
        }
        this.visible = visible;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return !this.isVisible() ? false : this.statsScroller.m_6375_(pMouseX, pMouseY, pButton) || super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return !this.isVisible() ? false : this.statsScroller.m_6050_(pMouseX, pMouseY, pDelta) || super.m_6050_(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return !this.isVisible() ? false : this.statsScroller.m_7979_(pMouseX, pMouseY, pButton, pDragX, pDragY) || super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @NotNull
    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }

    static class PlayerStatsScroller extends ScrollPanel {

        private final List<String> skillsKeys = new ArrayList();

        private final List<PlayerStatsComponent.StatComponent> abilities = new ArrayList();

        public PlayerStatsScroller(Minecraft client, int width, int height, int top, int left) {
            super(client, width, height, top, left, 1, 6, 16777215, 16777215, -2011094751, -16777216, -11184811);
        }

        public void populateAbilities(Core core, Minecraft minecraft) {
            IDataStorage dataStorage = core.getData();
            this.skillsKeys.addAll(dataStorage.getXpMap(null).keySet());
            this.skillsKeys.sort(Comparator.comparingLong(skill -> dataStorage.getXpRaw(null, skill)).reversed());
            for (String skillKey : this.skillsKeys) {
                SkillData skillData = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(skillKey, SkillData.Builder.getDefault());
                this.abilities.add(new PlayerStatsComponent.StatComponent(minecraft, this.left + 1, this.top, skillKey, skillData));
            }
        }

        @Override
        protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
            for (PlayerStatsComponent.StatComponent component : this.abilities) {
                component.m_264152_(component.m_252754_(), relativeY);
                component.m_88315_(guiGraphics, mouseX, mouseY, Minecraft.getInstance().getPartialTick());
                relativeY += 24 + this.border;
            }
        }

        @Override
        protected int getScrollAmount() {
            return 24 + this.border;
        }

        @Override
        protected int getContentHeight() {
            int height = this.abilities.size() * (24 + this.border);
            if (height < this.bottom - this.top - 1) {
                height = this.bottom - this.top - 1;
            }
            return height;
        }

        @NotNull
        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return NarratableEntry.NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        }
    }

    static class StatComponent extends ImageButton {

        private final Minecraft minecraft;

        private final String skillName;

        private final SkillData skillData;

        private final Color skillColor;

        private final int skillLevel;

        private final long skillCurrentXP;

        private final long skillXpToNext;

        private static final int BASE_HEIGHT = 24;

        public StatComponent(Minecraft minecraft, int pX, int pY, String skillKey, SkillData skillData) {
            super(pX, pY, 123, 24, 0, 167, 25, PlayerStatsComponent.TEXTURE_LOCATION, pButton -> {
            });
            this.minecraft = minecraft;
            this.skillName = Component.translatable("pmmo." + skillKey).getString();
            this.skillData = skillData;
            this.skillColor = new Color(skillData.getColor());
            this.skillCurrentXP = PlayerStatsComponent.core.getData().getXpRaw(null, skillKey);
            this.skillLevel = PlayerStatsComponent.core.getData().getLevelFromXP(this.skillCurrentXP);
            this.skillXpToNext = PlayerStatsComponent.core.getData().getBaseXpForLevel(this.skillLevel + 1) - this.skillCurrentXP;
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
            super.renderWidget(graphics, pMouseX, pMouseY, pPartialTick);
            graphics.blit(this.skillData.getIcon(), this.m_252754_() + 3, this.m_252907_() + 3, 18, 18, 0.0F, 0.0F, this.skillData.getIconSize(), this.skillData.getIconSize(), this.skillData.getIconSize(), this.skillData.getIconSize());
            this.renderProgressBar(graphics);
            graphics.drawString(this.minecraft.font, this.skillName, this.m_252754_() + 24, this.m_252907_() + 5, this.skillColor.getRGB());
            graphics.drawString(this.minecraft.font, String.valueOf(this.skillLevel), this.m_252754_() + this.f_93618_ - 5 - this.minecraft.font.width(String.valueOf(this.skillLevel)), this.m_252907_() + 5, this.skillColor.getRGB());
        }

        public void renderProgressBar(GuiGraphics graphics) {
            int renderX = this.m_252754_() + 24;
            int renderY = this.m_252907_() + 9 + 6;
            if (this.m_274382_()) {
                MutableComponent text = Component.literal("%s => %s".formatted(this.skillXpToNext, this.skillLevel + 1));
                graphics.drawString(this.minecraft.font, text, renderX, renderY - 1, this.skillColor.getRGB());
            } else {
                graphics.setColor((float) this.skillColor.getRed() / 255.0F, (float) this.skillColor.getGreen() / 255.0F, (float) this.skillColor.getBlue() / 255.0F, (float) this.skillColor.getAlpha() / 255.0F);
                graphics.blit(PlayerStatsComponent.TEXTURE_LOCATION, renderX, renderY, 94, 5, 0.0F, 217.0F, 102, 5, 256, 256);
                long baseXP = PlayerStatsComponent.core.getData().getBaseXpForLevel(this.skillLevel);
                long requiredXP = PlayerStatsComponent.core.getData().getBaseXpForLevel(this.skillLevel + 1);
                float percent = 100.0F / (float) (requiredXP - baseXP);
                int xp = (int) Math.min(Math.floor((double) (percent * (float) (this.skillCurrentXP - baseXP))), 94.0);
                graphics.blit(PlayerStatsComponent.TEXTURE_LOCATION, renderX, renderY, xp, 5, 0.0F, 223.0F, 102, 5, 256, 256);
                graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}