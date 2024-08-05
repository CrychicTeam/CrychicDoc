package net.blay09.mods.waystones.compat;

import java.util.function.Function;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;
import org.jetbrains.annotations.Nullable;

public class TheOneProbeIntegration {

    public TheOneProbeIntegration() {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", TheOneProbeIntegration.TopInitializer::new);
    }

    public static class ProbeInfoProvider implements IProbeInfoProvider {

        public ResourceLocation getID() {
            return new ResourceLocation("waystones", "top");
        }

        public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data) {
            BlockEntity tileEntity = level.getBlockEntity(data.getPos());
            if (!(tileEntity instanceof WarpPlateBlockEntity) && tileEntity instanceof WaystoneBlockEntityBase) {
                IWaystone waystone = ((WaystoneBlockEntityBase) tileEntity).getWaystone();
                boolean isActivated = !waystone.getWaystoneType().equals(WaystoneTypes.WAYSTONE) || PlayerWaystoneManager.isWaystoneActivated(player, waystone);
                if (isActivated && waystone.hasName() && waystone.isValid()) {
                    info.text(Component.literal(waystone.getName()));
                } else {
                    info.text(Component.translatable("tooltip.waystones.undiscovered"));
                }
            }
        }
    }

    public static class TopInitializer implements Function<ITheOneProbe, Void> {

        @Nullable
        public Void apply(@Nullable ITheOneProbe top) {
            if (top != null) {
                top.registerProvider(new TheOneProbeIntegration.ProbeInfoProvider());
            }
            return null;
        }
    }
}