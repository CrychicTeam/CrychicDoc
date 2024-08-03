package io.github.lightman314.lightmanscurrency.common.villager_merchant;

import com.google.common.collect.ImmutableSet;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.ITallBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModRegistries;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CustomPointsOfInterest {

    public static final RegistryObject<PoiType> BANKER = ModRegistries.POI_TYPES.register("banker", () -> new PoiType(getBlockStates(ModBlocks.ATM.get()), 1, 1));

    public static final ResourceKey<PoiType> BANKER_KEY = ResourceKey.create(ForgeRegistries.Keys.POI_TYPES, new ResourceLocation("lightmanscurrency", "banker"));

    public static final RegistryObject<PoiType> CASHIER = ModRegistries.POI_TYPES.register("cashier", () -> new PoiType(getBlockStates(ModBlocks.CASH_REGISTER.get()), 1, 1));

    public static final ResourceKey<PoiType> CASHIER_KEY = ResourceKey.create(ForgeRegistries.Keys.POI_TYPES, new ResourceLocation("lightmanscurrency", "cashier"));

    public static void init() {
    }

    private static Set<BlockState> getBlockStates(Block block) {
        return block instanceof ITallBlock tallBlock ? ImmutableSet.copyOf((Collection) block.getStateDefinition().getPossibleStates().stream().filter(tallBlock::getIsBottom).collect(Collectors.toSet())) : ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}