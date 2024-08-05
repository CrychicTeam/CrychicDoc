package team.lodestar.lodestone.capability;

import java.util.ArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class LodestoneWorldDataCapability implements LodestoneCapability {

    public static Capability<LodestoneWorldDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<LodestoneWorldDataCapability>() {
    });

    public final ArrayList<WorldEventInstance> activeWorldEvents = new ArrayList();

    public final ArrayList<WorldEventInstance> inboundWorldEvents = new ArrayList();

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(LodestoneWorldDataCapability.class);
    }

    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        LodestoneWorldDataCapability capability = new LodestoneWorldDataCapability();
        event.addCapability(LodestoneLib.lodestonePath("world_data"), new LodestoneCapabilityProvider<>(CAPABILITY, () -> capability));
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        WorldEventHandler.serializeNBT(this, tag);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        WorldEventHandler.deserializeNBT(this, nbt);
    }

    public static LazyOptional<LodestoneWorldDataCapability> getCapabilityOptional(Level level) {
        return level.getCapability(CAPABILITY);
    }

    public static LodestoneWorldDataCapability getCapability(Level level) {
        return level.getCapability(CAPABILITY).orElse(new LodestoneWorldDataCapability());
    }
}