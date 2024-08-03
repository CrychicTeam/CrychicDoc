package net.minecraftforge.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ComputeFovModifierEvent extends Event {

    private final Player player;

    private final float fovModifier;

    private float newFovModifier;

    @Internal
    public ComputeFovModifierEvent(Player player, float fovModifier) {
        this.player = player;
        this.fovModifier = fovModifier;
        this.setNewFovModifier((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0, (double) fovModifier));
    }

    public Player getPlayer() {
        return this.player;
    }

    public float getFovModifier() {
        return this.fovModifier;
    }

    public float getNewFovModifier() {
        return this.newFovModifier;
    }

    public void setNewFovModifier(float newFovModifier) {
        this.newFovModifier = newFovModifier;
    }
}