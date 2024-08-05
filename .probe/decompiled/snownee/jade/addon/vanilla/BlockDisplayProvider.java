package snownee.jade.addon.vanilla;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.ItemStackElement;
import snownee.jade.overlay.RayTracing;
import snownee.jade.util.ClientProxy;

public enum BlockDisplayProvider implements IEntityComponentProvider {

    INSTANCE;

    @Nullable
    @Override
    public IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
        Display.BlockDisplay itemDisplay = (Display.BlockDisplay) accessor.getEntity();
        Block block = itemDisplay.getBlockState().m_60734_();
        if (block.asItem() == Items.AIR) {
            return null;
        } else {
            IElement icon = ItemStackElement.of(new ItemStack(block));
            if (RayTracing.isEmptyElement(icon) && block instanceof LiquidBlock) {
                icon = ClientProxy.elementFromLiquid((LiquidBlock) block);
            }
            return icon;
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.MC_BLOCK_DISPLAY;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}