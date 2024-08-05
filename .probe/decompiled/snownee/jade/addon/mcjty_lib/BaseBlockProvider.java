package snownee.jade.addon.mcjty_lib;

import mcjty.lib.api.infusable.CapabilityInfusable;
import mcjty.lib.base.GeneralConfig;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

public enum BaseBlockProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        IThemeHelper t = IThemeHelper.get();
        if (data.contains("Infused")) {
            tooltip.add(Component.translatable("jadeaddons.mcjtylib.infused", t.info(data.getInt("Infused"))));
        }
        if (data.contains("SecurityChannel")) {
            int channel = data.getInt("SecurityChannel");
            String name = data.getString("OwnerName");
            if (channel == -1) {
                tooltip.add(Component.translatable("jadeaddons.mcjtylib.ownedBy", name));
            } else {
                tooltip.add(Component.translatable("jadeaddons.mcjtylib.ownedBy.withChannel", name, channel));
            }
            if (data.getBoolean("OwnerWarning")) {
                tooltip.add(t.warning(Component.translatable("jadeaddons.mcjtylib.ownedBy.warning")));
            }
        }
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlock() instanceof BaseBlock) {
            accessor.getBlockEntity().getCapability(CapabilityInfusable.INFUSABLE_CAPABILITY).ifPresent(h -> {
                int infused = h.getInfused();
                int pct = infused * 100 / GeneralConfig.maxInfuse.get();
                data.putInt("Infused", pct);
            });
            if (GeneralConfig.manageOwnership.get()) {
                GenericTileEntity generic = (GenericTileEntity) accessor.getBlockEntity();
                if (generic.getOwnerName() != null && !generic.getOwnerName().isEmpty()) {
                    data.putInt("SecurityChannel", generic.getSecurityChannel());
                    data.putString("OwnerName", generic.getOwnerName());
                    if (generic.getOwnerUUID() == null) {
                        data.putBoolean("OwnerWarning", true);
                    }
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return McjtyLibPlugin.GENERAL;
    }
}