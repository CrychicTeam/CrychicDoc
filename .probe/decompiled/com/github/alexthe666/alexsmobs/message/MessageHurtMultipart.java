package com.github.alexthe666.alexsmobs.message;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.IHurtableMultipart;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageHurtMultipart {

    public int part;

    public int parent;

    public float damage;

    public String damageType;

    public MessageHurtMultipart(int part, int parent, float damage) {
        this.part = part;
        this.parent = parent;
        this.damage = damage;
        this.damageType = "";
    }

    public MessageHurtMultipart(int part, int parent, float damage, String damageType) {
        this.part = part;
        this.parent = parent;
        this.damage = damage;
        this.damageType = damageType;
    }

    public MessageHurtMultipart() {
    }

    public static MessageHurtMultipart read(FriendlyByteBuf buf) {
        return new MessageHurtMultipart(buf.readInt(), buf.readInt(), buf.readFloat(), buf.readUtf());
    }

    public static void write(MessageHurtMultipart message, FriendlyByteBuf buf) {
        buf.writeInt(message.part);
        buf.writeInt(message.parent);
        buf.writeFloat(message.damage);
        buf.writeUtf(message.damageType);
    }

    public static class Handler {

        public static void handle(MessageHurtMultipart message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            Player player = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                player = AlexsMobs.PROXY.getClientSidePlayer();
            }
            if (player != null && player.m_9236_() != null) {
                Entity part = player.m_9236_().getEntity(message.part);
                Entity parent = player.m_9236_().getEntity(message.parent);
                Registry<DamageType> registry = (Registry<DamageType>) player.m_9236_().registryAccess().registry(Registries.DAMAGE_TYPE).get();
                DamageType dmg = registry.get(new ResourceLocation(message.damageType));
                if (dmg != null) {
                    Holder<DamageType> holder = (Holder<DamageType>) registry.getHolder(registry.getId(dmg)).orElseGet(null);
                    if (holder != null) {
                        DamageSource source = new DamageSource((Holder<DamageType>) registry.getHolder(registry.getId(dmg)).get());
                        if (part instanceof IHurtableMultipart && parent instanceof LivingEntity) {
                            ((IHurtableMultipart) part).onAttackedFromServer((LivingEntity) parent, message.damage, source);
                        }
                        if (part == null && parent != null && parent.isMultipartEntity()) {
                            parent.hurt(source, message.damage);
                        }
                    }
                }
            }
        }
    }
}