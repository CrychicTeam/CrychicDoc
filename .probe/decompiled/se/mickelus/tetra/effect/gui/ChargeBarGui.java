package se.mickelus.tetra.effect.gui;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.ChargedAbilityEffect;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.InvertColorGui;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class ChargeBarGui extends GuiElement {

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private final GuiElement container;

    private final GuiElement overchargeContainer;

    private final ChargeBarGui.Bar bar;

    private final ChargeBarGui.Bar[] overchargeBars;

    public ChargeBarGui() {
        super(-1, 22, 17, 2);
        this.setAttachment(GuiAttachment.middleCenter);
        this.container = new InvertColorGui(0, 0, this.width, this.height).setOpacity(0.0F);
        this.addChild(this.container);
        this.bar = new ChargeBarGui.Bar(0, 0, this.width, 2);
        this.container.addChild(this.bar);
        this.overchargeContainer = new GuiElement(0, 3, this.width, 2);
        this.container.addChild(this.overchargeContainer);
        this.overchargeBars = new ChargeBarGui.Bar[3];
        for (int i = 0; i < this.overchargeBars.length; i++) {
            this.overchargeBars[i] = new ChargeBarGui.Bar(i * 6, 0, 5, 2);
            this.overchargeContainer.addChild(this.overchargeBars[i]);
        }
        this.showAnimation = new KeyframeAnimation(60, this.container).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX(0.0F));
        this.hideAnimation = new KeyframeAnimation(100, this.container).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX(-2.0F)).withDelay(1000);
    }

    public void setProgress(float progress, boolean canOvercharge) {
        if (progress > 0.0F) {
            this.bar.setProgress((double) progress);
            this.overchargeContainer.setVisible(canOvercharge);
            if (canOvercharge) {
                double overchargeProgress = ChargedAbilityEffect.getOverchargeProgress(progress - 1.0F);
                for (int i = 0; i < 3; i++) {
                    this.overchargeBars[i].setProgress(overchargeProgress - (double) i);
                }
            }
            if (!this.showAnimation.isActive() && this.container.getOpacity() < 1.0F) {
                this.showAnimation.start();
            }
            this.hideAnimation.stop();
        } else {
            if (!this.hideAnimation.isActive() && this.container.getOpacity() > 0.0F) {
                this.hideAnimation.start();
            }
            this.showAnimation.stop();
        }
    }

    public void update(Player player) {
        ItemStack activeStack = player.m_21211_();
        ItemModularHandheld item = (ItemModularHandheld) CastOptional.cast(activeStack.getItem(), ItemModularHandheld.class).orElse(null);
        ChargedAbilityEffect ability = (ChargedAbilityEffect) Optional.ofNullable(item).map(i -> i.getChargeableAbility(activeStack)).orElse(null);
        if (ability != null) {
            this.setProgress(this.getProgress(player, item, activeStack, ability), ability.canOvercharge(item, activeStack));
        } else {
            this.setProgress(0.0F, false);
        }
    }

    private float getProgress(Player player, ItemModularHandheld item, ItemStack itemStack, ChargedAbilityEffect ability) {
        return ability != null ? (float) (itemStack.getUseDuration() - player.m_21212_()) * 1.0F / (float) ability.getChargeTime(player, item, itemStack) : 0.0F;
    }

    static class Bar extends GuiElement {

        private final GuiTexture bar;

        private final GuiTexture background;

        public Bar(int x, int y, int width, int height) {
            super(x, y, width, height);
            this.bar = new GuiTexture(0, 0, 0, height, 3, 0, GuiTextures.hud);
            this.addChild(this.bar);
            this.background = new GuiTexture(0, 0, width, height, 3, 2, GuiTextures.hud);
            this.background.setAttachment(GuiAttachment.topRight);
            this.addChild(this.background);
        }

        public void setProgress(double progress) {
            int barWidth = Mth.clamp((int) (progress * (double) this.width), 0, this.width);
            this.bar.setWidth(barWidth);
            this.background.setWidth(Math.max(0, this.width - barWidth));
        }
    }
}