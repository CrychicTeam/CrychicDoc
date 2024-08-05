package fuzs.puzzleslib.api.resources.v1;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.flag.FeatureFlagSet;

public final class PackResourcesHelper {

    private PackResourcesHelper() {
    }

    @Deprecated(forRemoval = true)
    public static RepositorySource buildClientPack(Supplier<AbstractModPackResources> factory, String id, Component title, Component description, boolean required, boolean fixedPosition) {
        return buildClientPack(new ResourceLocation(id, "main"), factory, title, description, required, fixedPosition, false);
    }

    public static RepositorySource buildClientPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, boolean hidden) {
        return buildClientPack(id, factory, Component.literal("Generated Resource Pack"), Component.literal("Dynamic Resources (" + id.getNamespace() + ")"), true, hidden, hidden);
    }

    public static RepositorySource buildClientPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, Component title, Component description, boolean required, boolean fixedPosition, boolean hidden) {
        return consumer -> consumer.accept(AbstractModPackResources.buildPack(PackType.CLIENT_RESOURCES, id, factory, title, description, required, fixedPosition, hidden, FeatureFlagSet.of()));
    }

    @Deprecated(forRemoval = true)
    public static RepositorySource buildServerPack(Supplier<AbstractModPackResources> factory, String id, Component title, Component description, boolean required, boolean fixedPosition) {
        return buildServerPack(new ResourceLocation(id, "main"), factory, title, description, required, fixedPosition, false);
    }

    public static RepositorySource buildServerPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, boolean hidden) {
        return buildServerPack(id, factory, Component.literal("Generated Data Pack"), Component.literal("Dynamic Resources (" + id.getNamespace() + ")"), true, hidden, hidden);
    }

    public static RepositorySource buildServerPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, Component title, Component description, boolean required, boolean fixedPosition, boolean hidden) {
        return consumer -> consumer.accept(AbstractModPackResources.buildPack(PackType.SERVER_DATA, id, factory, title, description, required, fixedPosition, hidden, FeatureFlagSet.of()));
    }
}