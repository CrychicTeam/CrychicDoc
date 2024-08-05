package com.rekindled.embers.network.message;

import com.rekindled.embers.datagen.EmbersSounds;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.Validate;

public class MessageItemSound {

    private final int id;

    private final Item item;

    private final SoundEvent sound;

    private final SoundSource source;

    private final boolean repeat;

    private final float volume;

    private final float pitch;

    public MessageItemSound(Entity entity, Item item, SoundEvent sound, SoundSource source, boolean repeat, float volume, float pitch) {
        this(entity.getId(), item, sound, source, repeat, volume, pitch);
    }

    public MessageItemSound(int id, Item item, SoundEvent sound, SoundSource source, boolean repeat, float volume, float pitch) {
        Validate.notNull(sound, "sound", new Object[0]);
        this.item = item;
        this.sound = sound;
        this.source = source;
        this.id = id;
        this.repeat = repeat;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static void encode(MessageItemSound msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.id);
        buf.writeId(BuiltInRegistries.ITEM, msg.item);
        buf.writeId(BuiltInRegistries.SOUND_EVENT, msg.sound);
        buf.writeEnum(msg.source);
        buf.writeBoolean(msg.repeat);
        buf.writeFloat(msg.volume);
        buf.writeFloat(msg.pitch);
    }

    public static MessageItemSound decode(FriendlyByteBuf buf) {
        return new MessageItemSound(buf.readVarInt(), buf.readById(BuiltInRegistries.ITEM), buf.readById(BuiltInRegistries.SOUND_EVENT), buf.readEnum(SoundSource.class), buf.readBoolean(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(MessageItemSound msg, Supplier<NetworkEvent.Context> ctx) {
        if (((NetworkEvent.Context) ctx.get()).getDirection().getReceptionSide().isClient()) {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
                if (Minecraft.getInstance().level.getEntity(msg.id) instanceof LivingEntity entity) {
                    EmbersSounds.playItemSoundClient(entity, msg.item, msg.sound, msg.source, msg.repeat, msg.volume, msg.pitch);
                }
            });
        }
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}