package se.mickelus.tetra.blocks.salvage;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class InteractiveOutlineGui extends GuiElement {

    private static final ResourceLocation texture = new ResourceLocation("tetra", "textures/gui/block-interaction.png");

    private final BlockInteraction blockInteraction;

    private final GuiTexture topLeft;

    private final GuiTexture topRight;

    private final GuiTexture bottomLeft;

    private final GuiTexture bottomRight;

    private InteractiveToolGui tool;

    public InteractiveOutlineGui(BlockInteraction blockInteraction, Player player) {
        super((int) blockInteraction.minX * 4, (int) blockInteraction.minY * 4, (int) (blockInteraction.maxX - blockInteraction.minX) * 4, (int) (blockInteraction.maxY - blockInteraction.minY) * 4);
        this.blockInteraction = blockInteraction;
        this.opacity = 0.5F;
        this.topLeft = new GuiTexture(-2, -2, 4, 4, 0, 0, texture);
        this.addChild(this.topLeft);
        new KeyframeAnimation(100, this.topLeft).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX(0.0F, -2.0F), new Applier.TranslateY(0.0F, -2.0F)).withDelay(500).start();
        this.topRight = new GuiTexture(2, -2, 4, 4, 3, 0, texture);
        this.topRight.setAttachment(GuiAttachment.topRight);
        this.addChild(this.topRight);
        new KeyframeAnimation(100, this.topRight).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX(0.0F, 2.0F), new Applier.TranslateY(0.0F, -2.0F)).withDelay(650).start();
        this.bottomLeft = new GuiTexture(-2, 2, 4, 4, 3, 0, texture);
        this.bottomLeft.setAttachment(GuiAttachment.bottomLeft);
        this.addChild(this.bottomLeft);
        new KeyframeAnimation(100, this.bottomLeft).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX(0.0F, -2.0F), new Applier.TranslateY(0.0F, 2.0F)).withDelay(500).start();
        this.bottomRight = new GuiTexture(2, 2, 4, 4, 0, 0, texture);
        this.bottomRight.setAttachment(GuiAttachment.bottomRight);
        this.addChild(this.bottomRight);
        new KeyframeAnimation(100, this.bottomRight).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX(0.0F, 2.0F), new Applier.TranslateY(0.0F, 2.0F)).withDelay(650).onStop(complete -> {
            if (this.tool != null) {
                this.tool.updateFadeTime();
            }
        }).start();
        if (blockInteraction.requiredTool != null) {
            this.tool = new InteractiveToolGui(0, 0, blockInteraction.requiredTool, blockInteraction.requiredLevel, player);
            this.addChild(this.tool);
            float centerY = (float) this.y + (float) this.height / 2.0F;
            float centerX = (float) this.x + (float) this.width / 2.0F;
            if (Math.abs(centerX - 16.0F) > Math.abs(centerY - 16.0F)) {
                if (centerX < 16.0F) {
                    this.tool.setAttachmentPoint(GuiAttachment.middleLeft);
                    this.tool.setAttachmentAnchor(GuiAttachment.middleRight);
                    this.tool.setX(-1);
                } else {
                    this.tool.setAttachmentPoint(GuiAttachment.middleRight);
                    this.tool.setAttachmentAnchor(GuiAttachment.middleLeft);
                }
            } else if (centerY < 16.0F) {
                this.tool.setAttachmentPoint(GuiAttachment.topCenter);
                this.tool.setAttachmentAnchor(GuiAttachment.bottomCenter);
                this.tool.setY(1);
            } else {
                this.tool.setAttachmentPoint(GuiAttachment.bottomCenter);
                this.tool.setAttachmentAnchor(GuiAttachment.topCenter);
                this.tool.setY(-2);
            }
        }
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        if (this.tool != null) {
            this.tool.show();
        }
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        if (this.tool != null) {
            this.tool.hide();
        }
    }

    public BlockInteraction getBlockInteraction() {
        return this.blockInteraction;
    }

    public void transitionOut(Runnable onStop) {
        new KeyframeAnimation(200, this.topLeft).applyTo(new Applier.Opacity(1.0F, 0.0F), new Applier.TranslateX(-5.0F), new Applier.TranslateY(-5.0F)).start();
        new KeyframeAnimation(200, this.topRight).applyTo(new Applier.Opacity(1.0F, 0.0F), new Applier.TranslateX(5.0F), new Applier.TranslateY(-5.0F)).start();
        new KeyframeAnimation(200, this.bottomLeft).applyTo(new Applier.Opacity(1.0F, 0.0F), new Applier.TranslateX(-5.0F), new Applier.TranslateY(5.0F)).start();
        new KeyframeAnimation(200, this.bottomRight).applyTo(new Applier.Opacity(1.0F, 0.0F), new Applier.TranslateX(5.0F), new Applier.TranslateY(5.0F)).onStop(finished -> onStop.run()).start();
    }
}