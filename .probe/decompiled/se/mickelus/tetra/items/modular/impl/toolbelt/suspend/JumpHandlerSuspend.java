package se.mickelus.tetra.items.modular.impl.toolbelt.suspend;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.TetraMod;

@ParametersAreNonnullByDefault
public class JumpHandlerSuspend {

    private final Minecraft mc;

    private final KeyMapping jumpKey;

    private boolean wasJumpKeyDown = false;

    public JumpHandlerSuspend(Minecraft mc) {
        this.mc = mc;
        this.jumpKey = mc.options.keyJump;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onKeyInput(InputEvent.Key event) {
        if (this.mc.isWindowActive()) {
            Player player = this.mc.player;
            if (this.jumpKey.isDown() && !this.wasJumpKeyDown && !player.m_20096_() && !player.isCreative() && !player.isSpectator()) {
                boolean isSuspended = player.m_21023_(SuspendPotionEffect.instance);
                if (!isSuspended || player.m_6144_()) {
                    SuspendEffect.toggleSuspend(player, !isSuspended);
                    TetraMod.packetHandler.sendToServer(new ToggleSuspendPacket(!isSuspended));
                }
            }
            this.wasJumpKeyDown = this.jumpKey.isDown();
        }
    }
}