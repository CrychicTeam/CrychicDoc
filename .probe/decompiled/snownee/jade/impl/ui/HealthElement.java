package snownee.jade.impl.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Identifiers;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.Element;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.overlay.DisplayHelper;
import snownee.jade.overlay.IconUI;

public class HealthElement extends Element {

    private final float maxHealth;

    private final float health;

    private String text;

    public HealthElement(float maxHealth, float health) {
        if (!PluginConfig.INSTANCE.get(Identifiers.MC_ENTITY_HEALTH_SHOW_FRACTIONS)) {
            maxHealth = (float) Mth.ceil(maxHealth);
            health = (float) Mth.ceil(health);
        }
        this.maxHealth = maxHealth;
        this.health = health;
        this.text = String.format("  %s/%s", DisplayHelper.dfCommas.format((double) health), DisplayHelper.dfCommas.format((double) maxHealth));
    }

    @Override
    public Vec2 getSize() {
        if (this.maxHealth > (float) PluginConfig.INSTANCE.getInt(Identifiers.MC_ENTITY_HEALTH_MAX_FOR_RENDER)) {
            Font font = Minecraft.getInstance().font;
            return new Vec2((float) (8 + font.width(this.text)), 10.0F);
        } else {
            float maxHearts = (float) PluginConfig.INSTANCE.getInt(Identifiers.MC_ENTITY_HEALTH_ICONS_PER_LINE);
            float maxHealth = this.maxHealth * 0.5F;
            int heartsPerLine = (int) Math.min((double) maxHearts, Math.ceil((double) maxHealth));
            int lineCount = (int) Math.ceil((double) (maxHealth / maxHearts));
            return new Vec2((float) (8 * heartsPerLine), (float) (10 * lineCount));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        float maxHearts = (float) PluginConfig.INSTANCE.getInt(Identifiers.MC_ENTITY_HEALTH_ICONS_PER_LINE);
        int maxHeartsForRender = PluginConfig.INSTANCE.getInt(Identifiers.MC_ENTITY_HEALTH_MAX_FOR_RENDER);
        boolean showNumbers = this.maxHealth > (float) maxHeartsForRender;
        int heartCount = showNumbers ? 1 : Mth.ceil(this.maxHealth * 0.5F);
        float health = showNumbers ? 1.0F : this.health * 0.5F;
        int heartsPerLine = (int) Math.min((double) maxHearts, Math.ceil((double) this.maxHealth));
        int xOffset = 0;
        for (int i = 1; i <= heartCount; i++) {
            if (i <= Mth.floor(health)) {
                DisplayHelper.renderIcon(guiGraphics, x + (float) xOffset, y, 8, 8, IconUI.HEART);
                xOffset += 8;
            }
            if ((float) i > health && (float) i < health + 1.0F) {
                DisplayHelper.renderIcon(guiGraphics, x + (float) xOffset, y, 8, 8, IconUI.HALF_HEART);
                xOffset += 8;
            }
            if ((float) i >= health + 1.0F) {
                DisplayHelper.renderIcon(guiGraphics, x + (float) xOffset, y, 8, 8, IconUI.EMPTY_HEART);
                xOffset += 8;
            }
            if (!showNumbers && i % heartsPerLine == 0) {
                y += 10.0F;
                xOffset = 0;
            }
        }
        if (showNumbers) {
            DisplayHelper.INSTANCE.drawText(guiGraphics, this.text, x + 8.0F, y, IThemeHelper.get().getNormalColor());
        }
    }

    @Nullable
    @Override
    public String getMessage() {
        return I18n.get("narration.jade.health", DisplayHelper.dfCommas.format((double) this.health));
    }
}