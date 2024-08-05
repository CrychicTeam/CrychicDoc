package dev.shadowsoffire.placebo.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;

public class TOPCompat {

    private static List<TOPCompat.Provider> providers = new ArrayList();

    public static void register() {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPCompat.GetTheOneProbe::new);
    }

    public static void registerProvider(TOPCompat.Provider p) {
        providers.add(p);
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        public Void apply(ITheOneProbe probe) {
            probe.registerProvider(new IProbeInfoProvider() {

                public ResourceLocation getID() {
                    return new ResourceLocation("placebo", "plugin");
                }

                public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData) {
                    TOPCompat.providers.forEach(p -> p.addProbeInfo(mode, info, player, level, state, hitData));
                }
            });
            probe.registerEntityProvider(new IProbeInfoEntityProvider() {

                public String getID() {
                    return "placebo:plugin";
                }

                public void addProbeEntityInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, Entity entity, IProbeHitEntityData hitData) {
                    TOPCompat.providers.forEach(p -> p.addProbeEntityInfo(mode, info, player, level, entity, hitData));
                }
            });
            return null;
        }
    }

    public interface Provider {

        default void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData) {
        }

        default void addProbeEntityInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, Entity entity, IProbeHitEntityData hitData) {
        }
    }
}