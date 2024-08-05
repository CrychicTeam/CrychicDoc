package fr.frinn.custommachinery.forge.integration.jade;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.common.init.CustomMachineBlock;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.init.Registration;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class CMWailaPlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(CustomMachineComponentProvider.INSTANCE, CustomMachineBlock.class);
        registration.usePickedResult((Block) Registration.CUSTOM_MACHINE_BLOCK.get());
        CustomMachinery.CUSTOM_BLOCK_MACHINES.values().forEach(registration::usePickedResult);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CustomMachineServerDataProvider.INSTANCE, CustomMachineTile.class);
    }
}