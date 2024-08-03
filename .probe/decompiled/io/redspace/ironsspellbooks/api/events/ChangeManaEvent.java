package io.redspace.ironsspellbooks.api.events;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ChangeManaEvent extends PlayerEvent {

    private final MagicData magicData;

    private final float oldMana;

    private float newMana;

    public ChangeManaEvent(Player player, MagicData magicData, float oldMana, float newMana) {
        super(player);
        this.magicData = magicData;
        this.oldMana = oldMana;
        this.newMana = newMana;
    }

    public boolean isCancelable() {
        return true;
    }

    public MagicData getMagicData() {
        return this.magicData;
    }

    public float getOldMana() {
        return this.oldMana;
    }

    public float getNewMana() {
        return this.newMana;
    }

    public void setNewMana(float newMana) {
        this.newMana = newMana;
    }
}