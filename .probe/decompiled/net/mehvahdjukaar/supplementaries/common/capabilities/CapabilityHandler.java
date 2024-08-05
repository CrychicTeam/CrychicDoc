package net.mehvahdjukaar.supplementaries.common.capabilities;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.block.IWashable;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.api.IAntiqueTextProvider;
import net.mehvahdjukaar.supplementaries.api.ICatchableMob;
import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.common.items.AntiqueInkItem;
import net.mehvahdjukaar.supplementaries.common.items.forge.QuiverItemImpl;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public class CapabilityHandler {

    private static final Map<Class<?>, Capability<?>> TOKENS = new Object2ObjectOpenHashMap();

    public static final Capability<ICatchableMob> CATCHABLE_MOB_CAP = CapabilityManager.get(new CapabilityToken<ICatchableMob>() {
    });

    public static final Capability<IAntiqueTextProvider> ANTIQUE_TEXT_CAP = CapabilityManager.get(new CapabilityToken<IAntiqueTextProvider>() {
    });

    public static final Capability<IWashable> SOAP_WASHABLE_CAPABILITY = CapabilityManager.get(new CapabilityToken<IWashable>() {
    });

    public static final Capability<QuiverItemImpl.QuiverCapability> QUIVER_ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<QuiverItemImpl.QuiverCapability>() {
    });

    public static final Capability<IQuiverEntity> QUIVER_PLAYER = CapabilityManager.get(new CapabilityToken<IQuiverEntity>() {
    });

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(ICatchableMob.class);
        event.register(IAntiqueTextProvider.class);
        event.register(IWashable.class);
        TOKENS.put(ICatchableMob.class, CATCHABLE_MOB_CAP);
        TOKENS.put(IAntiqueTextProvider.class, ANTIQUE_TEXT_CAP);
        TOKENS.put(IWashable.class, SOAP_WASHABLE_CAPABILITY);
    }

    public static void attachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (AntiqueInkItem.isEnabled() && (event.getObject() instanceof SignBlockEntity || event.getObject() instanceof HangingSignBlockEntity)) {
            event.addCapability(Supplementaries.res("antique_ink"), new AntiqueInkProvider());
        }
    }

    @Nullable
    public static <T> Capability<T> getToken(Class<T> capClass) {
        return (Capability<T>) TOKENS.get(capClass);
    }

    @Nullable
    public static <T> T get(ICapabilityProvider provider, Capability<T> cap) {
        return provider.getCapability(cap).orElse(null);
    }

    @Nullable
    public static <T> T get(ICapabilityProvider provider, Capability<T> cap, Direction dir) {
        return provider.getCapability(cap, dir).orElse(null);
    }
}