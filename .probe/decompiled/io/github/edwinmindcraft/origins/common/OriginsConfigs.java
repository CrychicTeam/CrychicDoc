package io.github.edwinmindcraft.origins.common;

import com.electronwill.nightconfig.core.Config;
import com.google.common.collect.ImmutableList;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.registry.ApoliDynamicRegistries;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class OriginsConfigs {

    public static final ForgeConfigSpec COMMON_SPECS;

    public static final ForgeConfigSpec CLIENT_SPECS;

    public static final ForgeConfigSpec SERVER_SPECS;

    public static final OriginsConfigs.Common COMMON;

    public static final OriginsConfigs.Client CLIENT;

    public static final OriginsConfigs.Server SERVER;

    static {
        Pair<OriginsConfigs.Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(OriginsConfigs.Common::new);
        Pair<OriginsConfigs.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(OriginsConfigs.Client::new);
        Pair<OriginsConfigs.Server, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(OriginsConfigs.Server::new);
        COMMON_SPECS = (ForgeConfigSpec) common.getRight();
        CLIENT_SPECS = (ForgeConfigSpec) client.getRight();
        SERVER_SPECS = (ForgeConfigSpec) server.getRight();
        COMMON = (OriginsConfigs.Common) common.getLeft();
        CLIENT = (OriginsConfigs.Client) client.getLeft();
        SERVER = (OriginsConfigs.Server) server.getLeft();
    }

    public static class Client {

        public Client(ForgeConfigSpec.Builder builder) {
        }
    }

    public static class Common {

        private final ForgeConfigSpec.ConfigValue<Config> origins;

        public Common(ForgeConfigSpec.Builder builder) {
            this.origins = builder.define(ImmutableList.of("origins"), Config::inMemory, x -> x instanceof Config, Config.class);
        }

        public boolean isOriginEnabled(ResourceLocation origin) {
            return (Boolean) this.origins.get().getOrElse(ImmutableList.of(origin.toString(), "enabled"), true);
        }

        public boolean isPowerEnabled(ResourceLocation origin, ResourceLocation power) {
            return (Boolean) this.origins.get().getOrElse(ImmutableList.of(origin.toString(), power.toString()), true);
        }

        public boolean updateOriginList(ICalioDynamicRegistryManager registryManager, Iterable<Origin> origins) {
            boolean changed = false;
            WritableRegistry<Origin> registry = registryManager.get(OriginsDynamicRegistries.ORIGINS_REGISTRY);
            WritableRegistry<ConfiguredPower<?, ?>> powers = registryManager.get(ApoliDynamicRegistries.CONFIGURED_POWER_KEY);
            for (Origin origin : origins) {
                ResourceLocation registryName = registry.m_7981_(origin);
                if (!origin.isSpecial() && registryName != null) {
                    if (this.origins.get().add(ImmutableList.of(registryName.toString(), "enabled"), true)) {
                        changed = true;
                    }
                    for (Holder<ConfiguredPower<?, ?>> holder : origin.getValidPowers().toList()) {
                        Optional<ResourceKey<ConfiguredPower<?, ?>>> key = (Optional<ResourceKey<ConfiguredPower<?, ?>>>) holder.unwrap().map(Optional::of, powers::m_7854_);
                        if (key.isPresent() && this.origins.get().add(ImmutableList.of(registryName.toString(), ((ResourceKey) key.get()).location().toString()), true)) {
                            changed = true;
                        }
                    }
                }
            }
            return changed;
        }
    }

    public static class Server {

        public Server(ForgeConfigSpec.Builder builder) {
        }
    }
}