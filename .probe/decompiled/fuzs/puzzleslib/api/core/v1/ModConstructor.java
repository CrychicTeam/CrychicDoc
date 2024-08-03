package fuzs.puzzleslib.api.core.v1;

import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.core.v1.context.BiomeModificationsContext;
import fuzs.puzzleslib.api.core.v1.context.BlockInteractionsContext;
import fuzs.puzzleslib.api.core.v1.context.BuildCreativeModeTabContentsContext;
import fuzs.puzzleslib.api.core.v1.context.CreativeModeTabContext;
import fuzs.puzzleslib.api.core.v1.context.EntityAttributesCreateContext;
import fuzs.puzzleslib.api.core.v1.context.EntityAttributesModifyContext;
import fuzs.puzzleslib.api.core.v1.context.FlammableBlocksContext;
import fuzs.puzzleslib.api.core.v1.context.FuelBurnTimesContext;
import fuzs.puzzleslib.api.core.v1.context.ModLifecycleContext;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.core.v1.context.SpawnPlacementsContext;
import fuzs.puzzleslib.impl.PuzzlesLib;
import fuzs.puzzleslib.impl.core.CommonFactories;
import fuzs.puzzleslib.impl.core.ModContext;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.util.Strings;

public interface ModConstructor extends BaseModConstructor {

    static void construct(String modId, Supplier<ModConstructor> supplier) {
        construct(modId, supplier);
    }

    @Deprecated(forRemoval = true)
    static void construct(String modId, Supplier<ModConstructor> supplier, ContentRegistrationFlags... flags) {
        if (Strings.isBlank(modId)) {
            throw new IllegalArgumentException("mod id is empty");
        } else {
            ModConstructor instance = (ModConstructor) supplier.get();
            ResourceLocation identifier = ModContext.getPairingIdentifier(modId, instance);
            PuzzlesLib.LOGGER.info("Constructing common components for {}", identifier);
            ModContext modContext = ModContext.get(modId);
            ContentRegistrationFlags[] builtInFlags = instance.getContentRegistrationFlags();
            Set<ContentRegistrationFlags> availableFlags = Set.of(builtInFlags.length != 0 ? builtInFlags : flags);
            Set<ContentRegistrationFlags> flagsToHandle = modContext.getFlagsToHandle(availableFlags);
            modContext.beforeModConstruction();
            CommonFactories.INSTANCE.constructMod(modId, instance, availableFlags, flagsToHandle);
            modContext.afterModConstruction(identifier);
        }
    }

    default void onConstructMod() {
    }

    @Deprecated(forRemoval = true)
    default void onCommonSetup(ModLifecycleContext context) {
    }

    default void onCommonSetup() {
        this.onCommonSetup(Runnable::run);
    }

    default void onRegisterSpawnPlacements(SpawnPlacementsContext context) {
    }

    default void onEntityAttributeCreation(EntityAttributesCreateContext context) {
    }

    default void onEntityAttributeModification(EntityAttributesModifyContext context) {
    }

    default void onRegisterFuelBurnTimes(FuelBurnTimesContext context) {
    }

    default void onRegisterBiomeModifications(BiomeModificationsContext context) {
    }

    default void onRegisterFlammableBlocks(FlammableBlocksContext context) {
    }

    default void onRegisterBlockInteractions(BlockInteractionsContext context) {
    }

    default void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
    }

    default void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsContext context) {
    }

    default void onAddDataPackFinders(PackRepositorySourcesContext context) {
    }

    default void onRegisterDataPackReloadListeners(AddReloadListenersContext context) {
    }
}