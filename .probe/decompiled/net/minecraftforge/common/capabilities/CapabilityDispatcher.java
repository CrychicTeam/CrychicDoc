package net.minecraftforge.common.capabilities;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CapabilityDispatcher implements INBTSerializable<CompoundTag>, ICapabilityProvider {

    private ICapabilityProvider[] caps;

    private INBTSerializable<Tag>[] writers;

    private String[] names;

    private final List<Runnable> listeners;

    public CapabilityDispatcher(Map<ResourceLocation, ICapabilityProvider> list, List<Runnable> listeners) {
        this(list, listeners, null);
    }

    public CapabilityDispatcher(Map<ResourceLocation, ICapabilityProvider> list, List<Runnable> listeners, @Nullable ICapabilityProvider parent) {
        List<ICapabilityProvider> lstCaps = Lists.newArrayList();
        List<INBTSerializable<Tag>> lstWriters = Lists.newArrayList();
        List<String> lstNames = Lists.newArrayList();
        this.listeners = listeners;
        if (parent != null) {
            lstCaps.add(parent);
            if (parent instanceof INBTSerializable) {
                lstWriters.add((INBTSerializable) parent);
                lstNames.add("Parent");
            }
        }
        for (Entry<ResourceLocation, ICapabilityProvider> entry : list.entrySet()) {
            ICapabilityProvider prov = (ICapabilityProvider) entry.getValue();
            lstCaps.add(prov);
            if (prov instanceof INBTSerializable) {
                lstWriters.add((INBTSerializable) prov);
                lstNames.add(((ResourceLocation) entry.getKey()).toString());
            }
        }
        this.caps = (ICapabilityProvider[]) lstCaps.toArray(new ICapabilityProvider[lstCaps.size()]);
        this.writers = (INBTSerializable<Tag>[]) lstWriters.toArray(new INBTSerializable[lstWriters.size()]);
        this.names = (String[]) lstNames.toArray(new String[lstNames.size()]);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        for (ICapabilityProvider c : this.caps) {
            LazyOptional<T> ret = c.getCapability(cap, side);
            if (ret == null) {
                throw new RuntimeException(String.format(Locale.ENGLISH, "Provider %s.getCapability() returned null; return LazyOptional.empty() instead!", c.getClass().getTypeName()));
            }
            if (ret.isPresent()) {
                return ret;
            }
        }
        return LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        for (int x = 0; x < this.writers.length; x++) {
            nbt.put(this.names[x], this.writers[x].serializeNBT());
        }
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        for (int x = 0; x < this.writers.length; x++) {
            if (nbt.contains(this.names[x])) {
                this.writers[x].deserializeNBT(nbt.get(this.names[x]));
            }
        }
    }

    public boolean areCompatible(@Nullable CapabilityDispatcher other) {
        if (other == null) {
            return this.writers.length == 0;
        } else {
            return this.writers.length == 0 ? other.writers.length == 0 : this.serializeNBT().equals(other.serializeNBT());
        }
    }

    public void invalidate() {
        this.listeners.forEach(Runnable::run);
    }
}