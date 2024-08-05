package net.minecraftforge.network;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.tuple.Pair;

public class ConnectionData {

    private ImmutableMap<String, Pair<String, String>> modData;

    private ImmutableMap<ResourceLocation, String> channels;

    ConnectionData(Map<String, Pair<String, String>> modData, Map<ResourceLocation, String> channels) {
        this.modData = ImmutableMap.copyOf(modData);
        this.channels = ImmutableMap.copyOf(channels);
    }

    public ImmutableList<String> getModList() {
        return this.modData.keySet().asList();
    }

    public ImmutableMap<String, Pair<String, String>> getModData() {
        return this.modData;
    }

    public ImmutableMap<ResourceLocation, String> getChannels() {
        return this.channels;
    }

    public static record ModMismatchData(Map<ResourceLocation, String> mismatchedModData, Map<ResourceLocation, Pair<String, String>> presentModData, boolean mismatchedDataFromServer) {

        public static ConnectionData.ModMismatchData channel(Map<ResourceLocation, String> mismatchedChannels, ConnectionData connectionData, boolean mismatchedDataFromServer) {
            Map<ResourceLocation, String> mismatchedChannelData = enhanceWithModVersion(mismatchedChannels, connectionData, mismatchedDataFromServer);
            Map<ResourceLocation, Pair<String, String>> presentChannelData = getPresentChannelData(mismatchedChannels.keySet(), connectionData, mismatchedDataFromServer);
            return new ConnectionData.ModMismatchData(mismatchedChannelData, presentChannelData, mismatchedDataFromServer);
        }

        public static ConnectionData.ModMismatchData registry(Multimap<ResourceLocation, ResourceLocation> mismatchedRegistryData, ConnectionData connectionData) {
            List<ResourceLocation> mismatchedRegistryMods = mismatchedRegistryData.values().stream().map(ResourceLocation::m_135827_).distinct().map(id -> new ResourceLocation(id, "")).toList();
            Map<ResourceLocation, String> mismatchedRegistryModData = (Map<ResourceLocation, String>) mismatchedRegistryMods.stream().map(id -> (Pair) ModList.get().getModContainerById(id.getNamespace()).map(modContainer -> Pair.of(id, modContainer.getModInfo().getVersion().toString())).orElse(Pair.of(id, NetworkRegistry.ABSENT.version()))).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
            Map<ResourceLocation, Pair<String, String>> presentModData = getServerSidePresentModData(mismatchedRegistryModData.keySet(), connectionData);
            return new ConnectionData.ModMismatchData(mismatchedRegistryModData, presentModData, false);
        }

        public boolean containsMismatches() {
            return this.mismatchedModData != null && !this.mismatchedModData.isEmpty();
        }

        private static Map<ResourceLocation, String> enhanceWithModVersion(Map<ResourceLocation, String> mismatchedChannels, ConnectionData connectionData, boolean mismatchedDataFromServer) {
            Map<String, String> mismatchedModVersions;
            if (mismatchedDataFromServer) {
                mismatchedModVersions = connectionData != null ? (Map) connectionData.getModData().entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> (String) ((Pair) e.getValue()).getRight())) : Map.of();
            } else {
                mismatchedModVersions = (Map<String, String>) ModList.get().getMods().stream().map(info -> Pair.of(info.getModId(), info.getVersion().toString())).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
            }
            return (Map<ResourceLocation, String>) mismatchedChannels.keySet().stream().map(channel -> Pair.of(channel, ((String) mismatchedChannels.get(channel)).equals(NetworkRegistry.ABSENT.version()) ? NetworkRegistry.ABSENT.version() : (String) mismatchedModVersions.getOrDefault(channel.getNamespace(), NetworkRegistry.ABSENT.version()))).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        }

        private static Map<ResourceLocation, Pair<String, String>> getPresentChannelData(Set<ResourceLocation> mismatchedChannelsFilter, ConnectionData connectionData, boolean mismatchedDataFromServer) {
            Map<ResourceLocation, String> channelData;
            if (mismatchedDataFromServer) {
                channelData = NetworkRegistry.buildChannelVersions();
            } else {
                channelData = (Map<ResourceLocation, String>) (connectionData != null ? connectionData.getChannels() : Map.of());
            }
            return (Map<ResourceLocation, Pair<String, String>>) channelData.keySet().stream().filter(mismatchedChannelsFilter::contains).map(id -> getPresentModDataFromChannel(id, connectionData, mismatchedDataFromServer)).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        }

        private static Pair<ResourceLocation, Pair<String, String>> getPresentModDataFromChannel(ResourceLocation channel, ConnectionData connectionData, boolean mismatchedDataFromServer) {
            if (mismatchedDataFromServer) {
                return (Pair<ResourceLocation, Pair<String, String>>) ModList.get().getModContainerById(channel.getNamespace()).map(modContainer -> Pair.of(channel, Pair.of(modContainer.getModInfo().getDisplayName(), modContainer.getModInfo().getVersion().toString()))).orElse(Pair.of(channel, Pair.of(channel.getNamespace(), "")));
            } else {
                Map<String, Pair<String, String>> modData = (Map<String, Pair<String, String>>) (connectionData != null ? connectionData.getModData() : Map.of());
                Pair<String, String> modDataFromChannel = (Pair<String, String>) modData.getOrDefault(channel.getNamespace(), Pair.of(channel.getNamespace(), ""));
                return Pair.of(channel, ((String) modDataFromChannel.getLeft()).isEmpty() ? Pair.of(channel.getNamespace(), (String) modDataFromChannel.getRight()) : modDataFromChannel);
            }
        }

        private static Map<ResourceLocation, Pair<String, String>> getServerSidePresentModData(Set<ResourceLocation> mismatchedModsFilter, ConnectionData connectionData) {
            Map<String, Pair<String, String>> serverModData = (Map<String, Pair<String, String>>) (connectionData != null ? connectionData.getModData() : Map.of());
            Set<String> modIdFilter = (Set<String>) mismatchedModsFilter.stream().map(ResourceLocation::m_135827_).collect(Collectors.toSet());
            return (Map<ResourceLocation, Pair<String, String>>) serverModData.entrySet().stream().filter(e -> modIdFilter.contains(e.getKey())).collect(Collectors.toMap(e -> new ResourceLocation((String) e.getKey(), ""), Entry::getValue));
        }
    }
}