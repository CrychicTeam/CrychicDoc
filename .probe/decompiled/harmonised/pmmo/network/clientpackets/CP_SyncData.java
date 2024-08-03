package harmonised.pmmo.network.clientpackets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.config.codecs.DataSource;
import harmonised.pmmo.config.codecs.EnhancementsData;
import harmonised.pmmo.config.codecs.LocationData;
import harmonised.pmmo.config.codecs.ObjectData;
import harmonised.pmmo.config.codecs.PlayerData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public record CP_SyncData(ObjectType type, Map<ResourceLocation, ? extends DataSource<?>> data) {

    private static final Codec<DataSource<?>> CODEC = ExtraCodecs.lazyInitializedCodec(() -> ObjectType.CODEC.dispatch("type", s -> {
        if (s instanceof ObjectData) {
            return ObjectType.ITEM;
        } else if (s instanceof LocationData) {
            return ObjectType.BIOME;
        } else {
            return s instanceof EnhancementsData ? ObjectType.EFFECT : ObjectType.PLAYER;
        }
    }, x -> {
        return switch(x) {
            case ITEM ->
                ObjectData.CODEC;
            case BIOME ->
                LocationData.CODEC;
            case EFFECT ->
                EnhancementsData.CODEC;
            default ->
                PlayerData.CODEC;
        };
    }));

    private static final Codec<CP_SyncData> MAPPER = RecordCodecBuilder.create(instance -> instance.group(ObjectType.CODEC.fieldOf("type").forGetter(CP_SyncData::type), Codec.unboundedMap(ResourceLocation.CODEC, CODEC).fieldOf("data").forGetter(pkt -> pkt.data())).apply(instance, CP_SyncData::new));

    public static CP_SyncData decode(FriendlyByteBuf buf) {
        return (CP_SyncData) MAPPER.parse(NbtOps.INSTANCE, buf.readNbt(NbtAccounter.UNLIMITED)).result().orElse(new CP_SyncData(ObjectType.ITEM, new HashMap()));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt((CompoundTag) MAPPER.encodeStart(NbtOps.INSTANCE, this).result().orElse(new CompoundTag()));
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.NETWORK, "Payload for {}/{} is {}", this.getClass().getSimpleName(), this.type().name(), buf.readableBytes());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Map<ResourceLocation, DataSource<?>> map = (Map<ResourceLocation, DataSource<?>>) Core.get(LogicalSide.CLIENT).getLoader().getLoader(this.type()).getData();
            this.data().forEach((key, value) -> map.put(key, value));
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}