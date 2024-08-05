package journeymap.client.ui.minimap;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import journeymap.client.JourneymapClient;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.theme.ThemeLabelSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.phys.Vec2;

public class Effect implements Selectable {

    private static Effect INSTANCE;

    private int x;

    private int y;

    private int height;

    private int width;

    private int dragOffsetX = 0;

    private int dragOffsetY = 0;

    private boolean dragging = false;

    public static Effect getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Effect();
        }
        return INSTANCE;
    }

    public static int[] effectProcessor(int k, int l, int j, int i, MobEffect mobEffect) {
        MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
        int x = k;
        int y = l;
        if (prop.moveEffectIcons.get()) {
            boolean vertical = prop.effectVertical.get();
            boolean reversed = prop.effectReversed.get();
            x = Minecraft.getInstance().getWindow().getGuiScaledWidth() - 25;
            int var13 = 1;
            boolean beneficial = mobEffect.isBeneficial();
            int multi = 25 * (beneficial ? j - 1 : i - 1);
            if (vertical) {
                x = beneficial ? x : x - 26;
                y = reversed ? var13 - multi : var13 + multi;
            } else {
                if (!reversed) {
                    return new int[] { k, l };
                }
                y = beneficial ? var13 : var13 + 26;
                x += multi;
            }
        }
        return new int[] { x, y };
    }

    @Override
    public void renderBorder(GuiGraphics graphics, int color) {
        MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
        boolean vertical = prop.moveEffectIcons.get() ? prop.effectVertical.get() : false;
        boolean reversed = prop.moveEffectIcons.get() ? prop.effectReversed.get() : false;
        color = prop.moveEffectIcons.get() ? color : -65536;
        Window window = Minecraft.getInstance().getWindow();
        this.width = vertical ? 51 : 100;
        this.x = reversed && !vertical ? window.getGuiScaledWidth() + 75 : window.getGuiScaledWidth();
        int endX = this.x - this.width - 2;
        this.height = vertical ? (reversed ? -74 : 102) : 52;
        this.y = reversed && vertical ? 26 : 0;
        Vec2 location = this.getPotionEffectsLocation();
        graphics.pose().pushPose();
        graphics.pose().translate(location.x, location.y - 1.0F, 0.0F);
        int bottomOffset = reversed && vertical ? -1 : 1;
        int topOffset = reversed && vertical ? 2 : 0;
        graphics.fill(this.x, this.height - 1, endX + 1, this.height + 1, color);
        graphics.fill(this.x - 1, this.height + bottomOffset, this.x + 1, this.y + topOffset, color);
        graphics.fill(this.x, this.y, endX + 1, this.y + 2, color);
        graphics.fill(endX, this.height + bottomOffset, endX + 2, this.y + topOffset, color);
        graphics.pose().popPose();
    }

    public boolean withinBounds(double mouseX, double mouseY) {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
        boolean vertical = prop.effectVertical.get();
        boolean reversed = prop.effectReversed.get();
        Vec2 location = this.getPotionEffectsLocation();
        int leftX = (int) ((float) screenWidth - ((float) (this.width - 2) - location.x));
        int topY = (int) ((float) this.y + location.y);
        int rightX = (int) ((float) screenWidth + location.x);
        int bottomY = (int) ((float) this.height + location.y);
        if (reversed && !vertical) {
            leftX = (int) ((float) this.x - ((float) (this.width - 2) - location.x));
            rightX = (int) ((float) this.x + location.x);
        }
        return mouseX < (double) rightX && mouseX > (double) leftX && (reversed && vertical ? mouseY < (double) topY : mouseY > (double) topY) && (reversed && vertical ? mouseY > (double) bottomY : mouseY < (double) bottomY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if (JourneymapClient.getInstance().getActiveMiniMapProperties().moveEffectIcons.get() && this.withinBounds(mouseX, mouseY) && !this.dragging) {
            MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            this.dragging = true;
            this.dragOffsetX = (int) (mouseX - (double) (prop.effectTranslateX.get() + screenWidth));
            this.dragOffsetY = (int) (mouseY - (double) prop.effectTranslateY.get().intValue());
            return true;
        } else {
            this.dragging = false;
            return false;
        }
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.dragging) {
            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
            int posX = (int) (pMouseX - (double) this.dragOffsetX) - screenWidth;
            int posY = (int) (pMouseY - (double) this.dragOffsetY);
            Vec2 loc = this.withinScreenBounds((double) posX, (double) posY);
            prop.effectTranslateX.set(Integer.valueOf((int) loc.x));
            prop.effectTranslateY.set(Integer.valueOf((int) loc.y));
            return true;
        } else {
            return false;
        }
    }

    public Vec2 withinScreenBounds(double pMouseX, double pMouseY) {
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
        boolean vertical = prop.effectVertical.get();
        boolean reversed = prop.effectReversed.get();
        double x = pMouseX > 0.0 ? 0.0 : (pMouseX - (double) this.width < (double) (-screenWidth) ? (double) (-screenWidth + this.width) : pMouseX);
        double y = pMouseY < 0.0 ? 0.0 : (pMouseY + (double) this.height > (double) screenHeight ? (double) (screenHeight - this.height) : pMouseY);
        if (reversed && !vertical) {
            x = pMouseX + 75.0 > 0.0 ? -75.0 : (pMouseX - 26.0 < (double) (-screenWidth) ? (double) (-screenWidth + 26) : pMouseX);
        } else if (reversed) {
            y = pMouseY - 75.0 < 0.0 ? 75.0 : (pMouseY + 26.0 > (double) screenHeight ? (double) (screenHeight - 26) : pMouseY);
        }
        return new Vec2((float) x, (float) y);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (this.dragging) {
            this.dragOffsetX = 0;
            this.dragOffsetY = 0;
            this.dragging = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        long windowId = Minecraft.getInstance().getWindow().getWindow();
        int speed = (int) (JourneymapClient.getInstance().getActiveMiniMapProperties().minimapKeyMovementSpeed.get() * 1000.0F);
        if (InputConstants.isKeyDown(windowId, 265)) {
            this.moveOnKey(0, -speed);
        } else if (InputConstants.isKeyDown(windowId, 264)) {
            this.moveOnKey(0, speed);
        } else if (InputConstants.isKeyDown(windowId, 263)) {
            this.moveOnKey(-speed, 0);
        } else if (InputConstants.isKeyDown(windowId, 262)) {
            this.moveOnKey(speed, 0);
        }
    }

    private void moveOnKey(int incX, int incY) {
        MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
        int posX = prop.effectTranslateX.get();
        int posY = prop.effectTranslateY.get();
        Vec2 loc = this.withinScreenBounds((double) (posX + incX), (double) (posY + incY));
        prop.effectTranslateX.set(Integer.valueOf((int) loc.x));
        prop.effectTranslateY.set(Integer.valueOf((int) loc.y));
    }

    public Vec2 getPotionEffectsLocation() {
        int x = 0;
        int y = 0;
        Window window = Minecraft.getInstance().getWindow();
        DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
        MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
        if (this.canPotionShift()) {
            x = prop.effectTranslateX.get();
            y = prop.effectTranslateY.get();
            if (x != 0 || y != 0) {
                return this.withinScreenBounds((double) x, (double) y);
            }
            x = -(window.getGuiScaledWidth() - (int) ((double) vars.textureX / window.getGuiScale()));
            prop.effectTranslateX.set(Integer.valueOf(x));
        }
        return new Vec2((float) x, (float) y);
    }

    public boolean canPotionShift() {
        DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
        MiniMapProperties prop = JourneymapClient.getInstance().getActiveMiniMapProperties();
        return prop.moveEffectIcons.get() || Position.Custom.equals(vars.position);
    }

    private boolean inDefaultPotionArea() {
        Window window = Minecraft.getInstance().getWindow();
        DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
        MiniMapProperties miniMapProperties = JourneymapClient.getInstance().getActiveMiniMapProperties();
        int topInfoLabelsHeight = vars.getInfoLabelAreaHeight(Minecraft.getInstance().font, vars.minimapSpec.labelTop, (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info1Label.get()), (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info2Label.get()));
        double scale = window.getGuiScale();
        int zoneTop = (int) (50.0 * scale) + 10;
        int zoneRight = window.getGuiScaledWidth() - (int) ((double) window.getGuiScaledWidth() * 0.05 * scale);
        int right = (int) ((double) (vars.textureX + vars.minimapWidth) / scale);
        int top = vars.textureY - topInfoLabelsHeight;
        return right > zoneRight && top < zoneTop;
    }
}