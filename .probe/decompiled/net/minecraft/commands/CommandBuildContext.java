package net.minecraft.commands;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;

public interface CommandBuildContext {

    <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<T>> var1);

    static CommandBuildContext simple(final HolderLookup.Provider holderLookupProvider0, final FeatureFlagSet featureFlagSet1) {
        return new CommandBuildContext() {

            @Override
            public <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<T>> p_255791_) {
                return holderLookupProvider0.lookupOrThrow(p_255791_).filterFeatures(featureFlagSet1);
            }
        };
    }

    static CommandBuildContext.Configurable configurable(final RegistryAccess registryAccess0, final FeatureFlagSet featureFlagSet1) {
        return new CommandBuildContext.Configurable() {

            CommandBuildContext.MissingTagAccessPolicy missingTagAccessPolicy = CommandBuildContext.MissingTagAccessPolicy.FAIL;

            @Override
            public void missingTagAccessPolicy(CommandBuildContext.MissingTagAccessPolicy p_256626_) {
                this.missingTagAccessPolicy = p_256626_;
            }

            @Override
            public <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<T>> p_256616_) {
                Registry<T> $$1 = registryAccess0.registryOrThrow(p_256616_);
                final HolderLookup.RegistryLookup<T> $$2 = $$1.asLookup();
                final HolderLookup.RegistryLookup<T> $$3 = $$1.asTagAddingLookup();
                HolderLookup.RegistryLookup<T> $$4 = new HolderLookup.RegistryLookup.Delegate<T>() {

                    @Override
                    protected HolderLookup.RegistryLookup<T> parent() {
                        return switch(missingTagAccessPolicy) {
                            case FAIL ->
                                $$2;
                            case CREATE_NEW ->
                                $$3;
                        };
                    }
                };
                return $$4.filterFeatures(featureFlagSet1);
            }
        };
    }

    public interface Configurable extends CommandBuildContext {

        void missingTagAccessPolicy(CommandBuildContext.MissingTagAccessPolicy var1);
    }

    public static enum MissingTagAccessPolicy {

        CREATE_NEW, FAIL
    }
}