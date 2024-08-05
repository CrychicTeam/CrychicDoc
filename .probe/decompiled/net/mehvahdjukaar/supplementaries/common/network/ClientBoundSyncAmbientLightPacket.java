package net.mehvahdjukaar.supplementaries.common.network;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.supplementaries.common.misc.MapLightHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class ClientBoundSyncAmbientLightPacket implements Message {

    private final Object2IntMap<ResourceKey<Level>> ambientLight = new Object2IntArrayMap();

    public ClientBoundSyncAmbientLightPacket(RegistryAccess registryAccess) {
        for (Entry<ResourceKey<Level>, Level> d : ((Registry) registryAccess.registry(Registries.DIMENSION).get()).entrySet()) {
            Object obj = d.getValue();
            DimensionType type = null;
            if (obj instanceof LevelStem stem) {
                type = stem.type().value();
            } else if (obj instanceof Level l) {
                type = l.dimensionType();
            }
            if (type != null) {
                float light = type.hasSkyLight() ? type.ambientLight() : 1.0F;
                this.ambientLight.put((ResourceKey) d.getKey(), Mth.ceil(light * 15.0F));
            }
        }
    }

    public ClientBoundSyncAmbientLightPacket(FriendlyByteBuf buf) {
        this.ambientLight.putAll(buf.readMap(buf1 -> buf1.readResourceKey(Registries.DIMENSION), FriendlyByteBuf::m_130242_));
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeMap(this.ambientLight, FriendlyByteBuf::m_236858_, FriendlyByteBuf::m_130130_);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        MapLightHandler.setAmbientLight(this.ambientLight);
    }
}