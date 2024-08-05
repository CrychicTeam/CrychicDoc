package lio.playeranimatorapi.misc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;

public class ModEntityDataSerializers {

    public static final EntityDataSerializer<ResourceLocation> RESOURCE_LOCATION = EntityDataSerializer.simple(FriendlyByteBuf::m_130085_, FriendlyByteBuf::m_130281_);

    public static void init() {
        EntityDataSerializers.registerSerializer(RESOURCE_LOCATION);
    }
}