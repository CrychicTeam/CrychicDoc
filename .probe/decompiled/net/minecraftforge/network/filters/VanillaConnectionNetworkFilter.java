package net.minecraftforge.network.filters;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mojang.logging.LogUtils;
import io.netty.channel.ChannelHandler.Sharable;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.gametest.framework.TestClassNameArgument;
import net.minecraft.gametest.framework.TestFunctionArgument;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagNetworkSerialization;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Sharable
public class VanillaConnectionNetworkFilter extends VanillaPacketFilter {

    private static final Logger LOGGER = LogUtils.getLogger();

    public VanillaConnectionNetworkFilter() {
        super(ImmutableMap.builder().put(handler(ClientboundUpdateAttributesPacket.class, VanillaConnectionNetworkFilter::filterEntityProperties)).put(handler(ClientboundCommandsPacket.class, VanillaConnectionNetworkFilter::filterCommandList)).put(handler(ClientboundUpdateTagsPacket.class, VanillaConnectionNetworkFilter::filterCustomTagTypes)).build());
    }

    @Override
    protected boolean isNecessary(Connection manager) {
        return NetworkHooks.isVanillaConnection(manager);
    }

    @NotNull
    private static ClientboundUpdateAttributesPacket filterEntityProperties(ClientboundUpdateAttributesPacket msg) {
        ClientboundUpdateAttributesPacket newPacket = new ClientboundUpdateAttributesPacket(msg.getEntityId(), Collections.emptyList());
        msg.getValues().stream().filter(snapshot -> {
            ResourceLocation key = ForgeRegistries.ATTRIBUTES.getKey(snapshot.getAttribute());
            return key != null && key.getNamespace().equals("minecraft");
        }).forEach(snapshot -> newPacket.getValues().add(snapshot));
        return newPacket;
    }

    @NotNull
    private static ClientboundCommandsPacket filterCommandList(ClientboundCommandsPacket packet) {
        CommandBuildContext commandBuildContext = Commands.createValidationContext(VanillaRegistries.createLookup());
        RootCommandNode<SharedSuggestionProvider> root = packet.getRoot(commandBuildContext);
        RootCommandNode<SharedSuggestionProvider> newRoot = CommandTreeCleaner.cleanArgumentTypes(root, argType -> {
            if (!(argType instanceof TestFunctionArgument) && !(argType instanceof TestClassNameArgument)) {
                ArgumentTypeInfo<?, ?> info = ArgumentTypeInfos.byClass(argType);
                ResourceLocation id = BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getKey(info);
                return id != null && (id.getNamespace().equals("minecraft") || id.getNamespace().equals("brigadier"));
            } else {
                return false;
            }
        });
        return new ClientboundCommandsPacket(newRoot);
    }

    private static ClientboundUpdateTagsPacket filterCustomTagTypes(ClientboundUpdateTagsPacket packet) {
        Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags = (Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload>) packet.getTags().entrySet().stream().filter(e -> isVanillaRegistry(((ResourceKey) e.getKey()).location())).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        return new ClientboundUpdateTagsPacket(tags);
    }

    private static boolean isVanillaRegistry(ResourceLocation location) {
        return RegistryManager.getVanillaRegistryKeys().contains(location) || VanillaRegistries.DATAPACK_REGISTRY_KEYS.stream().anyMatch(k -> k.location().equals(location));
    }
}