package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PlayerContainerEvent extends PlayerEvent {

    private final AbstractContainerMenu container;

    public PlayerContainerEvent(Player player, AbstractContainerMenu container) {
        super(player);
        this.container = container;
    }

    public AbstractContainerMenu getContainer() {
        return this.container;
    }

    public static class Close extends PlayerContainerEvent {

        public Close(Player player, AbstractContainerMenu container) {
            super(player, container);
        }
    }

    public static class Open extends PlayerContainerEvent {

        public Open(Player player, AbstractContainerMenu container) {
            super(player, container);
        }
    }
}