package net.minecraft.tags;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.RegistryLayer;

public class TagNetworkSerialization {

    public static Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> serializeTagsToNetwork(LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer0) {
        return (Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload>) RegistrySynchronization.networkSafeRegistries(layeredRegistryAccessRegistryLayer0).map(p_203949_ -> Pair.of(p_203949_.key(), serializeToNetwork(p_203949_.value()))).filter(p_203941_ -> !((TagNetworkSerialization.NetworkPayload) p_203941_.getSecond()).isEmpty()).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    private static <T> TagNetworkSerialization.NetworkPayload serializeToNetwork(Registry<T> registryT0) {
        Map<ResourceLocation, IntList> $$1 = new HashMap();
        registryT0.getTags().forEach(p_203947_ -> {
            HolderSet<T> $$3 = (HolderSet<T>) p_203947_.getSecond();
            IntList $$4 = new IntArrayList($$3.size());
            for (Holder<T> $$5 : $$3) {
                if ($$5.kind() != Holder.Kind.REFERENCE) {
                    throw new IllegalStateException("Can't serialize unregistered value " + $$5);
                }
                $$4.add(registryT0.getId($$5.value()));
            }
            $$1.put(((TagKey) p_203947_.getFirst()).location(), $$4);
        });
        return new TagNetworkSerialization.NetworkPayload($$1);
    }

    public static <T> void deserializeTagsFromNetwork(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, Registry<T> registryT1, TagNetworkSerialization.NetworkPayload tagNetworkSerializationNetworkPayload2, TagNetworkSerialization.TagOutput<T> tagNetworkSerializationTagOutputT3) {
        tagNetworkSerializationNetworkPayload2.tags.forEach((p_248278_, p_248279_) -> {
            TagKey<T> $$5 = TagKey.create(resourceKeyExtendsRegistryT0, p_248278_);
            List<Holder<T>> $$6 = (List<Holder<T>>) p_248279_.intStream().mapToObj(registryT1::m_203300_).flatMap(Optional::stream).collect(Collectors.toUnmodifiableList());
            tagNetworkSerializationTagOutputT3.accept($$5, $$6);
        });
    }

    public static final class NetworkPayload {

        final Map<ResourceLocation, IntList> tags;

        NetworkPayload(Map<ResourceLocation, IntList> mapResourceLocationIntList0) {
            this.tags = mapResourceLocationIntList0;
        }

        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeMap(this.tags, FriendlyByteBuf::m_130085_, FriendlyByteBuf::m_178345_);
        }

        public static TagNetworkSerialization.NetworkPayload read(FriendlyByteBuf friendlyByteBuf0) {
            return new TagNetworkSerialization.NetworkPayload(friendlyByteBuf0.readMap(FriendlyByteBuf::m_130281_, FriendlyByteBuf::m_178338_));
        }

        public boolean isEmpty() {
            return this.tags.isEmpty();
        }
    }

    @FunctionalInterface
    public interface TagOutput<T> {

        void accept(TagKey<T> var1, List<Holder<T>> var2);
    }
}