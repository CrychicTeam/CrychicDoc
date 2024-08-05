package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.menus.MintMenu;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MintScreen extends EasyMenuScreen<MintMenu> {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/coinmint.png");

    public static final Sprite ARROW_SPRITE = Sprite.LockedSprite(GUI_TEXTURE, 176, 0, 22, 16);

    public MintScreen(MintMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.resize(176, 138);
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);
        gui.drawString(this.f_96539_, 8, 6, 4210752);
        gui.drawString(this.f_169604_, 8, this.getYSize() - 94, 4210752);
        gui.blitSpriteFadeHoriz(ARROW_SPRITE, ScreenPosition.of(80, 21), ((MintMenu) this.f_97732_).blockEntity.getMintProgress());
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
    }
}