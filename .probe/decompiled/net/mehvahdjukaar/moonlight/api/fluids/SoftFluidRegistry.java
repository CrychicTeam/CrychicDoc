package net.mehvahdjukaar.moonlight.api.fluids;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SoftFluidRegistry {

    public static final ResourceKey<Registry<SoftFluid>> KEY = ResourceKey.createRegistryKey(Moonlight.res("soft_fluids"));

    public static Holder<SoftFluid> getEmpty() {
        return BuiltInSoftFluids.EMPTY.getHolder();
    }

    public static SoftFluid empty() {
        return BuiltInSoftFluids.EMPTY.get();
    }

    public static Registry<SoftFluid> hackyGetRegistry() {
        return Utils.hackyGetRegistry(KEY);
    }

    public static Registry<SoftFluid> getRegistry(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(KEY);
    }

    public static Collection<SoftFluid> getValues() {
        return hackyGetRegistry().stream().toList();
    }

    public static Collection<Holder.Reference<SoftFluid>> getHolders() {
        return hackyGetRegistry().holders().toList();
    }

    public static Set<Entry<ResourceKey<SoftFluid>, SoftFluid>> getEntries() {
        return hackyGetRegistry().entrySet();
    }

    public static Holder<SoftFluid> getHolder(ResourceLocation id) {
        Optional<Holder.Reference<SoftFluid>> opt = getOptionalHolder(id);
        return opt.isPresent() ? (Holder) opt.get() : getEmpty();
    }

    public static Optional<Holder.Reference<SoftFluid>> getOptionalHolder(ResourceLocation id) {
        id = backwardsCompat(id);
        return hackyGetRegistry().getHolder(ResourceKey.create(KEY, id));
    }

    @NotNull
    private static ResourceLocation backwardsCompat(ResourceLocation id) {
        String namespace = id.getNamespace();
        if (namespace.equals("selene") || namespace.equals("minecraft")) {
            id = Moonlight.res(id.getPath());
        }
        return id;
    }
}