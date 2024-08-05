package snownee.jade.addon.mcjty_lib;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin("mcjtylib")
public class McjtyLibPlugin implements IWailaPlugin {

    public static final String ID = "mcjtylib";

    public static final ResourceLocation GENERAL = new ResourceLocation("mcjtylib", "jadeaddons");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(BaseBlockProvider.INSTANCE, GenericTileEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BaseBlockProvider.INSTANCE, BaseBlock.class);
    }
}