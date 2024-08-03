package com.simibubi.create.content.equipment.zapper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.Vector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ZapperScreen extends AbstractSimiScreen {

    protected final Component patternSection = Lang.translateDirect("gui.terrainzapper.patternSection");

    protected AllGuiTextures background;

    protected ItemStack zapper;

    protected InteractionHand hand;

    protected float animationProgress;

    protected Component title;

    protected Vector<IconButton> patternButtons = new Vector(6);

    private IconButton confirmButton;

    protected int brightColor;

    protected int fontColor;

    protected PlacementPatterns currentPattern;

    public ZapperScreen(AllGuiTextures background, ItemStack zapper, InteractionHand hand) {
        this.background = background;
        this.zapper = zapper;
        this.hand = hand;
        this.title = Components.immutableEmpty();
        this.brightColor = 16711422;
        this.fontColor = 5726074;
        CompoundTag nbt = zapper.getOrCreateTag();
        this.currentPattern = NBTHelper.readEnum(nbt, "Pattern", PlacementPatterns.class);
    }

    @Override
    protected void init() {
        this.setWindowSize(this.background.width, this.background.height);
        this.setWindowOffset(-10, 0);
        super.init();
        this.animationProgress = 0.0F;
        int x = this.guiLeft;
        int y = this.guiTop;
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.m_7379_());
        this.m_142416_(this.confirmButton);
        this.patternButtons.clear();
        for (int row = 0; row <= 1; row++) {
            for (int col = 0; col <= 2; col++) {
                int id = this.patternButtons.size();
                PlacementPatterns pattern = PlacementPatterns.values()[id];
                IconButton patternButton = new IconButton(x + this.background.width - 76 + col * 18, y + 21 + row * 18, pattern.icon);
                patternButton.withCallback(() -> {
                    this.patternButtons.forEach(b -> b.f_93623_ = true);
                    patternButton.f_93623_ = false;
                    this.currentPattern = pattern;
                });
                patternButton.setToolTip(Lang.translateDirect("gui.terrainzapper.pattern." + pattern.translationKey));
                this.patternButtons.add(patternButton);
            }
        }
        ((IconButton) this.patternButtons.get(this.currentPattern.ordinal())).f_93623_ = false;
        this.addRenderableWidgets(this.patternButtons);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        this.background.render(graphics, x, y);
        this.drawOnBackground(graphics, x, y);
        this.renderBlock(graphics, x, y);
        this.renderZapper(graphics, x, y);
    }

    protected void drawOnBackground(GuiGraphics graphics, int x, int y) {
        graphics.drawString(this.f_96547_, this.title, x + 11, y + 4, 5513551, false);
    }

    @Override
    public void tick() {
        super.tick();
        this.animationProgress += 5.0F;
    }

    @Override
    public void removed() {
        ConfigureZapperPacket packet = this.getConfigurationPacket();
        packet.configureZapper(this.zapper);
        AllPackets.getChannel().sendToServer(packet);
    }

    protected void renderZapper(GuiGraphics graphics, int x, int y) {
        GuiGameElement.of(this.zapper).scale(4.0).<RenderElement>at((float) (x + this.background.width), (float) (y + this.background.height - 48), -200.0F).render(graphics);
    }

    protected void renderBlock(GuiGraphics graphics, int x, int y) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((float) (x + 32), (float) (y + 42), 120.0F);
        ms.mulPose(Axis.XP.rotationDegrees(-25.0F));
        ms.mulPose(Axis.YP.rotationDegrees(-45.0F));
        ms.scale(20.0F, 20.0F, 20.0F);
        BlockState state = Blocks.AIR.defaultBlockState();
        if (this.zapper.hasTag() && this.zapper.getTag().contains("BlockUsed")) {
            state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.m_255303_(), this.zapper.getTag().getCompound("BlockUsed"));
        }
        GuiGameElement.of(state).render(graphics);
        ms.popPose();
    }

    protected abstract ConfigureZapperPacket getConfigurationPacket();
}