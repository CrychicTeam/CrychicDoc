package harmonised.pmmo.storage;

import com.mojang.serialization.Codec;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.util.MsLoggy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ChunkDataProvider implements ICapabilitySerializable<CompoundTag> {

    public static final ResourceLocation CHUNK_CAP_ID = new ResourceLocation("pmmo", "placed_data");

    public static final Capability<IChunkData> CHUNK_CAP = CapabilityManager.get(new CapabilityToken<IChunkData>() {
    });

    private final ChunkDataHandler backend = new ChunkDataHandler();

    private LazyOptional<IChunkData> instance = LazyOptional.of(() -> this.backend);

    private static final Codec<Map<BlockPos, UUID>> CODEC = Codec.unboundedMap(CodecTypes.BLOCKPOS_CODEC, UUIDUtil.CODEC);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CHUNK_CAP ? this.instance.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        Map<BlockPos, UUID> unserializedMap = this.getCapability(CHUNK_CAP, null).orElse(this.backend).getMap();
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.CHUNK, "Serialized Chunk Cap: " + MsLoggy.mapToString(unserializedMap));
        CompoundTag nbt = (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, unserializedMap).resultOrPartial(msg -> MsLoggy.ERROR.log(MsLoggy.LOG_CODE.CHUNK, msg)).orElse(new CompoundTag());
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.CHUNK, "Serialized Chunk Cap NBT: " + nbt.toString());
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        Map<BlockPos, UUID> deserializedMap = new HashMap((Map) CODEC.parse(NbtOps.INSTANCE, nbt).result().orElse(new HashMap()));
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.CHUNK, "Deserialized Chunk Cap: " + MsLoggy.mapToString(deserializedMap));
        this.getCapability(CHUNK_CAP, null).orElse(this.backend).setMap(deserializedMap);
    }
}