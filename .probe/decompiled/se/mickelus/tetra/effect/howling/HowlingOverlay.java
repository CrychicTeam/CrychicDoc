package se.mickelus.tetra.effect.howling;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class HowlingOverlay implements IGuiOverlay {

    private final Minecraft mc;

    private final HowlingProgressGui gui;

    public HowlingOverlay(Minecraft mc) {
        this.mc = mc;
        this.gui = new HowlingProgressGui(mc);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.START == event.phase) {
            int amplifier = (Integer) Optional.ofNullable(this.mc.player).map(player -> player.m_21124_(HowlingPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
            this.gui.updateAmplifier(amplifier);
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        this.gui.draw(graphics);
    }
}