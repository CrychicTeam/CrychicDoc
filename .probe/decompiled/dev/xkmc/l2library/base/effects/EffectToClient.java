package dev.xkmc.l2library.base.effects;

import dev.xkmc.l2library.init.events.ClientEffectRenderEvents;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class EffectToClient extends SerialPacketBase {

    @SerialField
    public int entity;

    @SerialField
    public MobEffect effect;

    @SerialField
    public boolean exist;

    @SerialField
    public int level;

    public EffectToClient(int entity, MobEffect effect, boolean exist, int level) {
        this.entity = entity;
        this.effect = effect;
        this.exist = exist;
        this.level = level;
    }

    @Deprecated
    public EffectToClient() {
    }

    public void handle(NetworkEvent.Context context) {
        ClientEffectRenderEvents.sync(this);
    }
}