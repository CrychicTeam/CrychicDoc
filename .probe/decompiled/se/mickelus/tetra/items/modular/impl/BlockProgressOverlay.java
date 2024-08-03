package se.mickelus.tetra.items.modular.impl;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class BlockProgressOverlay implements IGuiOverlay {

    private final Minecraft mc;

    private final GuiBlockProgress gui;

    public BlockProgressOverlay(Minecraft mc) {
        this.mc = mc;
        this.gui = new GuiBlockProgress(mc);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.START == event.phase && this.mc.player != null) {
            ItemStack activeStack = this.mc.player.m_21211_();
            this.gui.setProgress((Float) CastOptional.cast(activeStack.getItem(), ItemModularHandheld.class).map(item -> item.getBlockProgress(activeStack, this.mc.player)).orElse(0.0F));
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        this.gui.draw(guiGraphics);
    }
}