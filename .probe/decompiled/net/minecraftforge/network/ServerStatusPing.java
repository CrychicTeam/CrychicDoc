package net.minecraftforge.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;

public record ServerStatusPing(Map<ResourceLocation, ServerStatusPing.ChannelData> channels, Map<String, String> mods, int fmlNetworkVer, boolean truncated) {

    private static final Codec<ByteBuf> BYTE_BUF_CODEC = Codec.STRING.xmap(ServerStatusPing::decodeOptimized, ServerStatusPing::encodeOptimized);

    public static final Codec<ServerStatusPing> CODEC = RecordCodecBuilder.create(in -> in.group(Codec.INT.fieldOf("fmlNetworkVersion").forGetter(ServerStatusPing::getFMLNetworkVersion), BYTE_BUF_CODEC.optionalFieldOf("d").forGetter(ping -> Optional.of(ping.toBuf())), ServerStatusPing.ChannelData.CODEC.listOf().optionalFieldOf("channels").forGetter(ping -> Optional.of(List.of())), ServerStatusPing.ModInfo.CODEC.listOf().optionalFieldOf("mods").forGetter(ping -> Optional.of(List.of())), Codec.BOOL.optionalFieldOf("truncated").forGetter(ping -> Optional.of(ping.isTruncated()))).apply(in, (fmlVer, buf, channels, mods, truncated) -> (ServerStatusPing) buf.map(byteBuf -> deserializeOptimized(fmlVer, byteBuf)).orElseGet(() -> new ServerStatusPing((Map<ResourceLocation, ServerStatusPing.ChannelData>) ((List) channels.orElseGet(List::of)).stream().collect(Collectors.toMap(ServerStatusPing.ChannelData::res, Function.identity())), (Map<String, String>) ((List) mods.orElseGet(List::of)).stream().collect(Collectors.toMap(ServerStatusPing.ModInfo::modId, ServerStatusPing.ModInfo::modmarker)), fmlVer, (Boolean) truncated.orElse(false)))));

    private static final int VERSION_FLAG_IGNORESERVERONLY = 1;

    public ServerStatusPing() {
        this(NetworkRegistry.buildChannelVersionsForListPing(), Util.make(new HashMap(), map -> ModList.get().forEachModContainer((modid, mc) -> map.put(modid, (String) mc.getCustomExtension(DisplayTest.class).map(DisplayTest::suppliedVersion).map(Supplier::get).orElse("OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31")))), 3, false);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof ServerStatusPing that) ? false : this.fmlNetworkVer == that.fmlNetworkVer && this.channels.equals(that.channels) && this.mods.equals(that.mods);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.channels, this.mods, this.fmlNetworkVer });
    }

    private List<Entry<ResourceLocation, ServerStatusPing.ChannelData>> getChannelsForMod(String modId) {
        return this.channels.entrySet().stream().filter(c -> ((ResourceLocation) c.getKey()).getNamespace().equals(modId)).toList();
    }

    private List<Entry<ResourceLocation, ServerStatusPing.ChannelData>> getNonModChannels() {
        return this.channels.entrySet().stream().filter(c -> !this.mods.containsKey(((ResourceLocation) c.getKey()).getNamespace())).toList();
    }

    public ByteBuf toBuf() {
        boolean reachedSizeLimit = false;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(false);
        buf.writeShort(this.mods.size());
        int writtenCount = 0;
        for (Entry<String, String> modEntry : this.mods.entrySet()) {
            boolean isIgnoreServerOnly = ((String) modEntry.getValue()).equals("OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31");
            List<Entry<ResourceLocation, ServerStatusPing.ChannelData>> channelsForMod = this.getChannelsForMod((String) modEntry.getKey());
            int channelSizeAndVersionFlag = channelsForMod.size() << 1;
            if (isIgnoreServerOnly) {
                channelSizeAndVersionFlag |= 1;
            }
            buf.writeVarInt(channelSizeAndVersionFlag);
            buf.writeUtf((String) modEntry.getKey());
            if (!isIgnoreServerOnly) {
                buf.writeUtf((String) modEntry.getValue());
            }
            for (Entry<ResourceLocation, ServerStatusPing.ChannelData> entry : channelsForMod) {
                buf.writeUtf(((ResourceLocation) entry.getKey()).getPath());
                buf.writeUtf(((ServerStatusPing.ChannelData) entry.getValue()).version());
                buf.writeBoolean(((ServerStatusPing.ChannelData) entry.getValue()).required());
            }
            writtenCount++;
            if (buf.readableBytes() >= 60000) {
                reachedSizeLimit = true;
                break;
            }
        }
        if (!reachedSizeLimit) {
            List<Entry<ResourceLocation, ServerStatusPing.ChannelData>> nonModChannels = this.getNonModChannels();
            buf.writeVarInt(nonModChannels.size());
            for (Entry<ResourceLocation, ServerStatusPing.ChannelData> entry : nonModChannels) {
                buf.writeResourceLocation((ResourceLocation) entry.getKey());
                buf.writeUtf(((ServerStatusPing.ChannelData) entry.getValue()).version());
                buf.writeBoolean(((ServerStatusPing.ChannelData) entry.getValue()).required());
            }
        } else {
            buf.setShort(1, writtenCount);
            buf.writeVarInt(0);
        }
        buf.setBoolean(0, reachedSizeLimit);
        return buf;
    }

    private static ServerStatusPing deserializeOptimized(int fmlNetworkVersion, ByteBuf bbuf) {
        FriendlyByteBuf buf = new FriendlyByteBuf(bbuf);
        boolean truncated;
        Map<ResourceLocation, ServerStatusPing.ChannelData> channels;
        Map<String, String> mods;
        try {
            truncated = buf.readBoolean();
            int modsSize = buf.readUnsignedShort();
            mods = new HashMap();
            channels = new HashMap();
            for (int i = 0; i < modsSize; i++) {
                int channelSizeAndVersionFlag = buf.readVarInt();
                int channelSize = channelSizeAndVersionFlag >>> 1;
                boolean isIgnoreServerOnly = (channelSizeAndVersionFlag & 1) != 0;
                String modId = buf.readUtf();
                String modVersion = isIgnoreServerOnly ? "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31" : buf.readUtf();
                for (int i1 = 0; i1 < channelSize; i1++) {
                    String channelName = buf.readUtf();
                    String channelVersion = buf.readUtf();
                    boolean requiredOnClient = buf.readBoolean();
                    ResourceLocation id = new ResourceLocation(modId, channelName);
                    channels.put(id, new ServerStatusPing.ChannelData(id, channelVersion, requiredOnClient));
                }
                mods.put(modId, modVersion);
            }
            int nonModChannelCount = buf.readVarInt();
            for (int i = 0; i < nonModChannelCount; i++) {
                ResourceLocation channelName = buf.readResourceLocation();
                String channelVersion = buf.readUtf();
                boolean requiredOnClient = buf.readBoolean();
                channels.put(channelName, new ServerStatusPing.ChannelData(channelName, channelVersion, requiredOnClient));
            }
        } finally {
            buf.release();
        }
        return new ServerStatusPing(channels, mods, fmlNetworkVersion, truncated);
    }

    private static String encodeOptimized(ByteBuf buf) {
        int byteLength = buf.readableBytes();
        StringBuilder sb = new StringBuilder();
        sb.append((char) (byteLength & 32767));
        sb.append((char) (byteLength >>> 15 & 32767));
        int buffer = 0;
        int bitsInBuf;
        for (bitsInBuf = 0; buf.isReadable(); bitsInBuf += 8) {
            if (bitsInBuf >= 15) {
                char c = (char) (buffer & 32767);
                sb.append(c);
                buffer >>>= 15;
                bitsInBuf -= 15;
            }
            short b = buf.readUnsignedByte();
            buffer |= b << bitsInBuf;
        }
        buf.release();
        if (bitsInBuf > 0) {
            char c = (char) (buffer & 32767);
            sb.append(c);
        }
        return sb.toString();
    }

    private static ByteBuf decodeOptimized(String s) {
        int size0 = s.charAt(0);
        int size1 = s.charAt(1);
        int size = size0 | size1 << 15;
        ByteBuf buf = Unpooled.buffer(size);
        int stringIndex = 2;
        int buffer = 0;
        int bitsInBuf;
        for (bitsInBuf = 0; stringIndex < s.length(); stringIndex++) {
            while (bitsInBuf >= 8) {
                buf.writeByte(buffer);
                buffer >>>= 8;
                bitsInBuf -= 8;
            }
            char c = s.charAt(stringIndex);
            buffer |= (c & 32767) << bitsInBuf;
            bitsInBuf += 15;
        }
        while (buf.readableBytes() < size) {
            buf.writeByte(buffer);
            buffer >>>= 8;
            bitsInBuf -= 8;
        }
        return buf;
    }

    public Map<ResourceLocation, ServerStatusPing.ChannelData> getRemoteChannels() {
        return this.channels;
    }

    public Map<String, String> getRemoteModData() {
        return this.mods;
    }

    public int getFMLNetworkVersion() {
        return this.fmlNetworkVer;
    }

    public boolean isTruncated() {
        return this.truncated;
    }

    public static record ChannelData(ResourceLocation res, String version, boolean required) {

        public static final Codec<ServerStatusPing.ChannelData> CODEC = RecordCodecBuilder.create(in -> in.group(ResourceLocation.CODEC.fieldOf("res").forGetter(ServerStatusPing.ChannelData::res), Codec.STRING.fieldOf("version").forGetter(ServerStatusPing.ChannelData::version), Codec.BOOL.fieldOf("required").forGetter(ServerStatusPing.ChannelData::required)).apply(in, ServerStatusPing.ChannelData::new));
    }

    public static record ModInfo(String modId, String modmarker) {

        public static final Codec<ServerStatusPing.ModInfo> CODEC = RecordCodecBuilder.create(in -> in.group(Codec.STRING.fieldOf("modId").forGetter(ServerStatusPing.ModInfo::modId), Codec.STRING.fieldOf("modmarker").forGetter(ServerStatusPing.ModInfo::modmarker)).apply(in, ServerStatusPing.ModInfo::new));
    }
}