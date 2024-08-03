package se.mickelus.tetra.items.modular.impl.toolbelt.booster;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.TetraMod;

@ParametersAreNonnullByDefault
public class JumpHandlerBooster {

    private final Minecraft mc;

    private final KeyMapping jumpKey;

    private boolean wasJumpKeyDown = false;

    public JumpHandlerBooster(Minecraft mc) {
        this.mc = mc;
        this.jumpKey = mc.options.keyJump;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onKeyInput(InputEvent.Key event) {
        if (this.mc.isWindowActive()) {
            if (this.jumpKey.isDown() && this.mc.player.m_20096_() && this.mc.player.isCrouching()) {
                UpdateBoosterPacket packet = new UpdateBoosterPacket(true, true);
                TetraMod.packetHandler.sendToServer(packet);
            } else if (this.jumpKey.isDown() && !this.wasJumpKeyDown && !this.mc.player.m_20096_()) {
                UpdateBoosterPacket packet = new UpdateBoosterPacket(true);
                TetraMod.packetHandler.sendToServer(packet);
            } else if (!this.jumpKey.isDown() && this.wasJumpKeyDown) {
                UpdateBoosterPacket packet = new UpdateBoosterPacket(false);
                TetraMod.packetHandler.sendToServer(packet);
            }
            this.wasJumpKeyDown = this.jumpKey.isDown();
        }
    }
}