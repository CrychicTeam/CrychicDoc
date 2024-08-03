package se.mickelus.tetra.effect.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.FocusEffect;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.InvertColorGui;
import se.mickelus.tetra.items.modular.ModularItem;

@ParametersAreNonnullByDefault
public class FocusGui extends GuiElement {

    private final GuiElement container;

    private final GuiTexture left;

    private final GuiTexture right;

    public FocusGui() {
        super(-1, 1, 0, 0);
        this.setAttachment(GuiAttachment.middleCenter);
        this.container = new InvertColorGui(0, 0, this.width, this.height).setOpacity(0.0F);
        this.addChild(this.container);
        this.left = new GuiTexture(-1, 0, 2, 2, 0, 1, GuiTextures.hud);
        this.left.setUseDefaultBlending(false);
        this.left.setAttachment(GuiAttachment.topRight);
        this.container.addChild(this.left);
        this.right = new GuiTexture(2, 0, 2, 2, 1, 1, GuiTextures.hud);
        this.right.setUseDefaultBlending(false);
        this.container.addChild(this.right);
    }

    public void setSpread(float progress, float cap) {
        if (progress != -1.0F) {
            int offset = (int) Mth.lerp(progress, Math.min(6.0F, 3.0F + cap / 3.0F), 0.0F);
            this.left.setX(-1 - offset);
            this.left.setY(offset);
            this.right.setX(2 + offset);
            this.right.setY(offset);
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
    }

    public void update(Player player) {
        if (player.m_6047_() && FocusEffect.hasApplicableItem(player)) {
            float spread = this.getSpread(player.m_21205_());
            float spreadReduction;
            if (spread <= -1.0F) {
                spread = this.getSpread(player.m_21206_());
                spreadReduction = FocusEffect.getSpreadReduction(player, player.m_21206_());
            } else {
                spreadReduction = FocusEffect.getSpreadReduction(player, player.m_21205_());
            }
            if (spread > 0.0F) {
                this.setSpread(Mth.clamp(spreadReduction / (100.0F - spread), 0.0F, 1.0F), Math.max(0.0F, 100.0F - spread));
                return;
            }
        }
        this.setSpread(-1.0F, 0.0F);
    }

    private float getSpread(ItemStack itemStack) {
        return (Float) CastOptional.cast(itemStack.getItem(), ModularItem.class).map(item -> item.getEffectEfficiency(itemStack, ItemEffect.spread)).orElse(-1.0F);
    }
}