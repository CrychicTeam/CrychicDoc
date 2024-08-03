package org.violetmoon.quark.content.world.module;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.MapColor;
import org.violetmoon.quark.base.handler.WoodSetHandler;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZAddReloadListener;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "world", antiOverlap = { "caverns_and_chasms" })
public class AzaleaWoodModule extends ZetaModule {

    private WoodSetHandler.WoodSet woodSet;

    @LoadEvent
    public final void register(ZRegister event) {
        this.woodSet = WoodSetHandler.addWoodSet(event, this, "azalea", MapColor.COLOR_LIGHT_GREEN, MapColor.COLOR_BROWN, true);
    }

    @LoadEvent
    public final void onServerReload(ZAddReloadListener e) {
        ConfiguredFeature<?, ?> azaleaFeature = (ConfiguredFeature<?, ?>) e.getRegistryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap(reg -> reg.getOptional(TreeFeatures.AZALEA_TREE)).orElse(null);
        if (this.woodSet != null && azaleaFeature != null && azaleaFeature.config() instanceof TreeConfiguration treeConfig) {
            if (this.enabled) {
                treeConfig.trunkProvider = BlockStateProvider.simple(this.woodSet.log);
            } else {
                treeConfig.trunkProvider = BlockStateProvider.simple(Blocks.OAK_LOG);
            }
        }
    }
}