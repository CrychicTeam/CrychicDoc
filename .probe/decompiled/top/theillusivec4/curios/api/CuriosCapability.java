package top.theillusivec4.curios.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class CuriosCapability {

    public static final Capability<ICuriosItemHandler> INVENTORY = CapabilityManager.get(new CapabilityToken<ICuriosItemHandler>() {
    });

    public static final Capability<ICurio> ITEM = CapabilityManager.get(new CapabilityToken<ICurio>() {
    });

    public static final ResourceLocation ID_INVENTORY = new ResourceLocation("curios", "inventory");

    public static final ResourceLocation ID_ITEM = new ResourceLocation("curios", "item");
}