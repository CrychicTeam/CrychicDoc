package org.violetmoon.quark.addons.oddities.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.addons.oddities.inventory.BackpackMenu;
import org.violetmoon.quark.addons.oddities.module.BackpackModule;
import org.violetmoon.quark.api.IQuarkButtonAllowed;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.network.message.oddities.HandleBackpackMessage;

public class BackpackInventoryScreen extends InventoryScreen implements IQuarkButtonAllowed {

    private static final ResourceLocation BACKPACK_INVENTORY_BACKGROUND = new ResourceLocation("quark", "textures/misc/backpack_gui.png");

    private final Player player;

    private final Map<Button, Integer> buttonYs = new HashMap();

    private boolean closeHack = false;

    private static InventoryMenu oldContainer;

    public BackpackInventoryScreen(InventoryMenu backpack, Inventory inventory, Component component) {
        super(setBackpackContainer(inventory.player, backpack));
        this.player = inventory.player;
        setBackpackContainer(this.player, oldContainer);
    }

    public static Player setBackpackContainer(Player entity, InventoryMenu container) {
        oldContainer = entity.inventoryMenu;
        entity.inventoryMenu = container;
        return entity;
    }

    @Override
    public void init() {
        this.f_97727_ = 224;
        super.init();
        this.buttonYs.clear();
        for (Renderable renderable : this.f_169369_) {
            if (renderable instanceof Button) {
                Button b = (Button) renderable;
                if (b.getClass().getName().contains("GuiButtonInventoryBook") && !this.buttonYs.containsKey(b)) {
                    b.m_253211_(b.m_252907_() - 29);
                    this.buttonYs.put(b, b.m_252907_());
                }
            }
        }
    }

    @Override
    public void containerTick() {
        this.buttonYs.forEach(AbstractWidget::m_253211_);
        super.containerTick();
        if (!BackpackModule.isEntityWearingBackpack(this.player)) {
            ItemStack curr = this.player.containerMenu.getCarried();
            BackpackMenu.saveCraftingInventory(this.player);
            this.closeHack = true;
            QuarkClient.ZETA_CLIENT.sendToServer(new HandleBackpackMessage(false));
            this.f_96541_.setScreen(new InventoryScreen(this.player));
            this.player.inventoryMenu.m_142503_(curr);
        }
    }

    @Override
    public void removed() {
        if (this.closeHack) {
            this.closeHack = false;
        } else {
            super.m_7861_();
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.f_97735_;
        int j = this.f_97736_;
        guiGraphics.blit(BACKPACK_INVENTORY_BACKGROUND, i, j, 0, 0, this.f_97726_, this.f_97727_);
        m_274545_(guiGraphics, i + 51, j + 75, 30, (float) (i + 51 - mouseX), (float) (j + 75 - 50 - mouseY), this.f_96541_.player);
        this.moveCharmsButtons();
    }

    private void moveCharmsButtons() {
        for (Renderable renderable : this.f_169369_) {
            if (renderable instanceof ImageButton) {
                ImageButton img = (ImageButton) renderable;
                if (img.m_252907_() == this.f_96544_ / 2 - 22) {
                    img.m_264152_(img.m_252754_(), img.m_252907_() - 29);
                }
            }
        }
    }
}