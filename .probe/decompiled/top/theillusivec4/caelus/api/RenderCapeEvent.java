package top.theillusivec4.caelus.api;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class RenderCapeEvent extends PlayerEvent {

    public RenderCapeEvent(Player player) {
        super(player);
    }
}