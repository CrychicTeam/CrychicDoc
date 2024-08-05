package org.violetmoon.zeta.event.play.loading;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.violetmoon.zeta.capability.ZetaCapability;
import org.violetmoon.zeta.capability.ZetaCapabilityManager;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZAttachCapabilities<T> extends IZetaPlayEvent {

    ZetaCapabilityManager getCapabilityManager();

    T getObject();

    <C> void addCapability(ResourceLocation var1, ZetaCapability<C> var2, C var3);

    @Deprecated
    void addCapabilityForgeApi(ResourceLocation var1, ICapabilityProvider var2);

    public interface BlockEntityCaps extends ZAttachCapabilities<BlockEntity> {
    }

    public interface ItemStackCaps extends ZAttachCapabilities<ItemStack> {
    }

    public interface LevelCaps extends ZAttachCapabilities<Level> {
    }
}