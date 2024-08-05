package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ClientboundUpdateAttributesPacket implements Packet<ClientGamePacketListener> {

    private final int entityId;

    private final List<ClientboundUpdateAttributesPacket.AttributeSnapshot> attributes;

    public ClientboundUpdateAttributesPacket(int int0, Collection<AttributeInstance> collectionAttributeInstance1) {
        this.entityId = int0;
        this.attributes = Lists.newArrayList();
        for (AttributeInstance $$2 : collectionAttributeInstance1) {
            this.attributes.add(new ClientboundUpdateAttributesPacket.AttributeSnapshot($$2.getAttribute(), $$2.getBaseValue(), $$2.getModifiers()));
        }
    }

    public ClientboundUpdateAttributesPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entityId = friendlyByteBuf0.readVarInt();
        this.attributes = friendlyByteBuf0.readList(p_258211_ -> {
            ResourceLocation $$1 = p_258211_.readResourceLocation();
            Attribute $$2 = BuiltInRegistries.ATTRIBUTE.get($$1);
            double $$3 = p_258211_.readDouble();
            List<AttributeModifier> $$4 = p_258211_.readList(p_179457_ -> new AttributeModifier(p_179457_.readUUID(), "Unknown synced attribute modifier", p_179457_.readDouble(), AttributeModifier.Operation.fromValue(p_179457_.readByte())));
            return new ClientboundUpdateAttributesPacket.AttributeSnapshot($$2, $$3, $$4);
        });
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.entityId);
        friendlyByteBuf0.writeCollection(this.attributes, (p_258212_, p_258213_) -> {
            p_258212_.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(p_258213_.getAttribute()));
            p_258212_.writeDouble(p_258213_.getBase());
            p_258212_.writeCollection(p_258213_.getModifiers(), (p_179449_, p_179450_) -> {
                p_179449_.writeUUID(p_179450_.getId());
                p_179449_.writeDouble(p_179450_.getAmount());
                p_179449_.writeByte(p_179450_.getOperation().toValue());
            });
        });
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleUpdateAttributes(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public List<ClientboundUpdateAttributesPacket.AttributeSnapshot> getValues() {
        return this.attributes;
    }

    public static class AttributeSnapshot {

        private final Attribute attribute;

        private final double base;

        private final Collection<AttributeModifier> modifiers;

        public AttributeSnapshot(Attribute attribute0, double double1, Collection<AttributeModifier> collectionAttributeModifier2) {
            this.attribute = attribute0;
            this.base = double1;
            this.modifiers = collectionAttributeModifier2;
        }

        public Attribute getAttribute() {
            return this.attribute;
        }

        public double getBase() {
            return this.base;
        }

        public Collection<AttributeModifier> getModifiers() {
            return this.modifiers;
        }
    }
}