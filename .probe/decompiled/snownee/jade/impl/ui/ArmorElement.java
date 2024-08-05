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

public class ArmorElement extends Element {

    private final float armor;

    public ArmorElement(float armor) {
        if (!PluginConfig.INSTANCE.get(Identifiers.MC_ENTITY_HEALTH_SHOW_FRACTIONS)) {
            armor = (float) Mth.ceil(armor);
        }
        this.armor = armor;
    }

    @Override
    public Vec2 getSize() {
        if (this.armor > (float) PluginConfig.INSTANCE.getInt(Identifiers.MC_ENTITY_ARMOR_MAX_FOR_RENDER)) {
            String text = "  " + DisplayHelper.dfCommas.format((double) this.armor);
            Font font = Minecraft.getInstance().font;
            return new Vec2((float) (8 + font.width(text)), 10.0F);
        } else {
            int maxHearts = PluginConfig.INSTANCE.getInt(Identifiers.MC_ENTITY_HEALTH_ICONS_PER_LINE);
            int lineCount = (int) Math.ceil((double) (this.armor / (float) maxHearts * 0.5F));
            return new Vec2((float) (8 * maxHearts), (float) (10 * lineCount));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        if (this.armor > (float) PluginConfig.INSTANCE.getInt(Identifiers.MC_ENTITY_ARMOR_MAX_FOR_RENDER)) {
            DisplayHelper.renderIcon(guiGraphics, x, y, 8, 8, IconUI.ARMOR);
            String text = "  " + DisplayHelper.dfCommas.format((double) this.armor);
            DisplayHelper.INSTANCE.drawText(guiGraphics, text, x + 8.0F, y, IThemeHelper.get().getNormalColor());
        } else {
            float armor = this.armor * 0.5F;
            int maxHearts = PluginConfig.INSTANCE.getInt(Identifiers.MC_ENTITY_HEALTH_ICONS_PER_LINE);
            int lineCount = (int) Math.ceil((double) (armor / (float) maxHearts));
            int armorCount = lineCount * maxHearts;
            int xOffset = 0;
            for (int i = 1; i <= armorCount; i++) {
                if (i <= Mth.floor(armor)) {
                    DisplayHelper.renderIcon(guiGraphics, x + (float) xOffset, y, 8, 8, IconUI.ARMOR);
                    xOffset += 8;
                }
                if ((float) i > armor && (float) i < armor + 1.0F) {
                    DisplayHelper.renderIcon(guiGraphics, x + (float) xOffset, y, 8, 8, IconUI.HALF_ARMOR);
                    xOffset += 8;
                }
                if ((float) i >= armor + 1.0F) {
                    DisplayHelper.renderIcon(guiGraphics, x + (float) xOffset, y, 8, 8, IconUI.EMPTY_ARMOR);
                    xOffset += 8;
                }
                if (i % maxHearts == 0) {
                    y += 10.0F;
                    xOffset = 0;
                }
            }
        }
    }

    @Nullable
    @Override
    public String getMessage() {
        return I18n.get("narration.jade.armor", DisplayHelper.dfCommas.format((double) this.armor));
    }
}