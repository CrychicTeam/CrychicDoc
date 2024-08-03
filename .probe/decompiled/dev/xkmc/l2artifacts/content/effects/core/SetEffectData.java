package dev.xkmc.l2artifacts.content.effects.core;

import dev.xkmc.l2library.capability.conditionals.ConditionalToken;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class SetEffectData extends ConditionalToken {

    @SerialField
    public int life;

    @SerialField
    public int rank;

    @Override
    public boolean tick(Player player) {
        if (this.life > 0) {
            this.life--;
        }
        if (this.life == 0) {
            this.remove(player);
        }
        return this.life <= 0;
    }

    public void update(int time, int rank) {
        this.life = time;
        this.rank = rank;
    }

    protected void remove(Player player) {
    }
}