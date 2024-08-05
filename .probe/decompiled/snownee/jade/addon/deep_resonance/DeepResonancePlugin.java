package snownee.jade.addon.deep_resonance;

import mcjty.deepresonance.modules.core.block.ResonatingCrystalBlock;
import mcjty.deepresonance.modules.core.block.ResonatingCrystalTileEntity;
import mcjty.deepresonance.modules.generator.block.GeneratorPartBlock;
import mcjty.deepresonance.modules.generator.block.GeneratorPartTileEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin("deepresonance")
public class DeepResonancePlugin implements IWailaPlugin {

    public static final String ID = "deepresonance";

    public static final ResourceLocation CRYSTAL = new ResourceLocation("deepresonance", "crystal");

    public static final ResourceLocation GENERATOR_PART = new ResourceLocation("deepresonance", "generator_part");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CrystalProvider.INSTANCE, ResonatingCrystalTileEntity.class);
        registration.registerBlockDataProvider(GeneratorPartProvider.INSTANCE, GeneratorPartTileEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(CrystalProvider.INSTANCE, ResonatingCrystalBlock.class);
        registration.registerBlockComponent(GeneratorPartProvider.INSTANCE, GeneratorPartBlock.class);
    }
}