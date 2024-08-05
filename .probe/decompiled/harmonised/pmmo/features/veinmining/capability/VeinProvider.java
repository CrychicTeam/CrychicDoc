package harmonised.pmmo.features.veinmining.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VeinProvider implements ICapabilitySerializable<DoubleTag> {

    public static final ResourceLocation VEIN_CAP_ID = new ResourceLocation("pmmo", "veind_data");

    public static final Capability<VeinHandler> VEIN_CAP = CapabilityManager.get(new CapabilityToken<VeinHandler>() {
    });

    private final VeinHandler backend = new VeinHandler();

    private LazyOptional<VeinHandler> instance = LazyOptional.of(() -> this.backend);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == VEIN_CAP ? this.instance.cast() : LazyOptional.empty();
    }

    public DoubleTag serializeNBT() {
        return DoubleTag.valueOf(this.instance.orElse(this.backend).getCharge());
    }

    public void deserializeNBT(DoubleTag nbt) {
        this.getCapability(VEIN_CAP, null).orElse(this.backend).setCharge(nbt.getAsDouble());
    }
}