package com.rekindled.embers.network;

import com.rekindled.embers.network.message.MessageBeamCannonFX;
import com.rekindled.embers.network.message.MessageCasterOrb;
import com.rekindled.embers.network.message.MessageDialUpdateRequest;
import com.rekindled.embers.network.message.MessageEmberGenOffset;
import com.rekindled.embers.network.message.MessageEmberRayFX;
import com.rekindled.embers.network.message.MessageItemSound;
import com.rekindled.embers.network.message.MessageResearchData;
import com.rekindled.embers.network.message.MessageResearchTick;
import com.rekindled.embers.network.message.MessageScalesData;
import com.rekindled.embers.network.message.MessageWorldSeed;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("embers", "main"), () -> "1", "1"::equals, "1"::equals);

    static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(id++, MessageDialUpdateRequest.class, MessageDialUpdateRequest::encode, MessageDialUpdateRequest::decode, MessageDialUpdateRequest::handle);
        INSTANCE.registerMessage(id++, MessageResearchTick.class, MessageResearchTick::encode, MessageResearchTick::decode, MessageResearchTick::handle);
        INSTANCE.registerMessage(id++, MessageResearchData.class, MessageResearchData::encode, MessageResearchData::decode, MessageResearchData::handle);
        INSTANCE.registerMessage(id++, MessageWorldSeed.class, MessageWorldSeed::encode, MessageWorldSeed::decode, MessageWorldSeed::handle);
        INSTANCE.registerMessage(id++, MessageEmberRayFX.class, MessageEmberRayFX::encode, MessageEmberRayFX::decode, MessageEmberRayFX::handle);
        INSTANCE.registerMessage(id++, MessageItemSound.class, MessageItemSound::encode, MessageItemSound::decode, MessageItemSound::handle);
        INSTANCE.registerMessage(id++, MessageBeamCannonFX.class, MessageBeamCannonFX::encode, MessageBeamCannonFX::decode, MessageBeamCannonFX::handle);
        INSTANCE.registerMessage(id++, MessageEmberGenOffset.class, MessageEmberGenOffset::encode, MessageEmberGenOffset::decode, MessageEmberGenOffset::handle);
        INSTANCE.registerMessage(id++, MessageCasterOrb.class, MessageCasterOrb::encode, MessageCasterOrb::decode, MessageCasterOrb::handle);
        INSTANCE.registerMessage(id++, MessageScalesData.class, MessageScalesData::encode, MessageScalesData::decode, MessageScalesData::handle);
    }
}