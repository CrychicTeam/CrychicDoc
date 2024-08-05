package se.mickelus.tetra.items.modular.impl.bow;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class RangedProgressOverlay implements IGuiOverlay {

    private final Minecraft mc;

    private final GuiRangedProgress gui;

    public RangedProgressOverlay(Minecraft mc) {
        this.mc = mc;
        this.gui = new GuiRangedProgress(mc);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.END == event.phase && this.mc.player != null) {
            ItemStack activeStack = this.mc.player.m_21211_();
            if (activeStack.getItem() instanceof ModularBowItem) {
                ModularBowItem item = (ModularBowItem) activeStack.getItem();
                this.gui.setProgress(item.getProgress(activeStack, this.mc.player), item.getOverbowProgress(activeStack, this.mc.player));
            } else {
                this.gui.setProgress(0.0F, 0.0F);
            }
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        this.gui.draw(graphics);
    }
}