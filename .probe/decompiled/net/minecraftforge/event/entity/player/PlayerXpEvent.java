package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;

public class PlayerXpEvent extends PlayerEvent {

    public PlayerXpEvent(Player player) {
        super(player);
    }

    @Cancelable
    public static class LevelChange extends PlayerXpEvent {

        private int levels;

        public LevelChange(Player player, int levels) {
            super(player);
            this.levels = levels;
        }

        public int getLevels() {
            return this.levels;
        }

        public void setLevels(int levels) {
            this.levels = levels;
        }
    }

    @Cancelable
    public static class PickupXp extends PlayerXpEvent {

        private final ExperienceOrb orb;

        public PickupXp(Player player, ExperienceOrb orb) {
            super(player);
            this.orb = orb;
        }

        public ExperienceOrb getOrb() {
            return this.orb;
        }
    }

    @Cancelable
    public static class XpChange extends PlayerXpEvent {

        private int amount;

        public XpChange(Player player, int amount) {
            super(player);
            this.amount = amount;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}