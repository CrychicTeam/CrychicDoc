package net.mehvahdjukaar.supplementaries.common.block.faucet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.misc.RegistryAccessJsonReloadListener;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FaucetBlockTile;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class FaucetBehaviorsManager extends RegistryAccessJsonReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final FaucetBehaviorsManager RELOAD_INSTANCE = new FaucetBehaviorsManager();

    private final Set<Object> dataInteractions = new HashSet();

    private static final Codec<Either<DataItemInteraction, DataFluidInteraction>> CODEC = Codec.either(DataItemInteraction.CODEC, DataFluidInteraction.CODEC);

    public FaucetBehaviorsManager() {
        super(GSON, "faucet_interactions");
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> map, RegistryAccess registryAccess) {
        FaucetBlockTile.removeDataInteractions(this.dataInteractions);
        this.dataInteractions.clear();
        map.forEach((key, json) -> {
            try {
                DataResult<Either<DataItemInteraction, DataFluidInteraction>> result = CODEC.parse(RegistryOps.create(JsonOps.INSTANCE, registryAccess), json);
                Either<DataItemInteraction, DataFluidInteraction> d = (Either<DataItemInteraction, DataFluidInteraction>) result.getOrThrow(false, ex -> Supplementaries.LOGGER.error("Failed to fluid interaction: {}", ex));
                Optional<DataItemInteraction> l = d.left();
                Object o;
                if (l.isPresent()) {
                    o = l.get();
                } else {
                    o = d.right().get();
                }
                this.dataInteractions.add(o);
                FaucetBlockTile.registerInteraction(o);
            } catch (Exception var8) {
                Supplementaries.LOGGER.error("Failed to parse JSON object for faucet interaction " + key);
            }
        });
        if (!this.dataInteractions.isEmpty()) {
            Supplementaries.LOGGER.info("Loaded  " + this.dataInteractions.size() + " custom faucet interactions");
        }
    }

    public static void registerBehaviors() {
        FaucetBlockTile.registerInteraction(new SoftFluidProviderInteraction());
        FaucetBlockTile.registerInteraction(new WaterCauldronInteraction());
        FaucetBlockTile.registerInteraction(new LavaCauldronInteraction());
        FaucetBlockTile.registerInteraction(new PowderSnowCauldronInteraction());
        FaucetBlockTile.registerInteraction(new BeehiveInteraction());
        FaucetBlockTile.registerInteraction(new SoftFluidTankInteraction());
        FaucetBlockTile.registerInteraction(new ForgeFluidTankInteraction());
        FaucetBlockTile.registerInteraction(new BrewingStandInteraction());
        FaucetBlockTile.registerInteraction(new FiniteFluidInteraction());
        FaucetBlockTile.registerInteraction(new LiquidBlockInteraction());
        FaucetBlockTile.registerInteraction(new SpongeInteraction());
        FaucetBlockTile.registerInteraction(new XPDroppingInteraction());
        FaucetBlockTile.registerInteraction(new ConcreteInteraction());
        FaucetBlockTile.registerInteraction(new MudInteraction());
        FaucetBlockTile.registerInteraction(new ContainerItemInteraction());
        if (CompatHandler.AUTUMNITY) {
            FaucetBlockTile.registerInteraction(new SappyLogInteraction());
        }
        if (CompatHandler.FARMERS_RESPRITE) {
            FaucetBlockTile.registerInteraction(new KettleInteraction());
        }
    }

    @Deprecated(forRemoval = true)
    public static void prepareToTransferBottle(SoftFluidTank tempFluidHolder, SoftFluid softFluid, @Nullable CompoundTag tag) {
    }
}