package snownee.jade.impl;

import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import snownee.jade.api.Accessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IJadeProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.impl.ui.ElementHelper;
import snownee.jade.impl.ui.ItemStackElement;
import snownee.jade.overlay.RayTracing;
import snownee.jade.util.ClientProxy;
import snownee.jade.util.CommonProxy;
import snownee.jade.util.WailaExceptionHandler;

public class EntityAccessorClientHandler implements Accessor.ClientHandler<EntityAccessor> {

    public boolean shouldDisplay(EntityAccessor accessor) {
        IWailaConfig.IConfigGeneral general = IWailaConfig.get().getGeneral();
        return !general.getDisplayEntities() ? false : general.getDisplayBosses() || !CommonProxy.isBoss(accessor.getEntity());
    }

    public boolean shouldRequestData(EntityAccessor accessor) {
        return !WailaCommonRegistration.INSTANCE.getEntityNBTProviders(accessor.getEntity()).isEmpty();
    }

    public void requestData(EntityAccessor accessor) {
        ClientProxy.requestEntityData(accessor);
    }

    public IElement getIcon(EntityAccessor accessor) {
        IElement icon = null;
        Entity entity = accessor.getEntity();
        if (entity instanceof ItemEntity) {
            icon = ItemStackElement.of(((ItemEntity) entity).getItem());
        } else {
            ItemStack stack = accessor.getPickedResult();
            if (!(stack.getItem() instanceof SpawnEggItem) || !(entity instanceof LivingEntity)) {
                icon = ItemStackElement.of(stack);
            }
        }
        for (IEntityComponentProvider provider : WailaClientRegistration.INSTANCE.getEntityIconProviders(entity, PluginConfig.INSTANCE::get)) {
            try {
                IElement element = provider.getIcon(accessor, PluginConfig.INSTANCE, icon);
                if (!RayTracing.isEmptyElement(element)) {
                    icon = element;
                }
            } catch (Throwable var7) {
                WailaExceptionHandler.handleErr(var7, provider, null, null);
            }
        }
        return icon;
    }

    public void gatherComponents(EntityAccessor accessor, Function<IJadeProvider, ITooltip> tooltipProvider) {
        for (IEntityComponentProvider provider : WailaClientRegistration.INSTANCE.getEntityProviders(accessor.getEntity(), PluginConfig.INSTANCE::get)) {
            ITooltip tooltip = (ITooltip) tooltipProvider.apply(provider);
            try {
                ElementHelper.INSTANCE.setCurrentUid(provider.getUid());
                provider.appendTooltip(tooltip, accessor, PluginConfig.INSTANCE);
            } catch (Throwable var11) {
                WailaExceptionHandler.handleErr(var11, provider, tooltip, null);
            } finally {
                ElementHelper.INSTANCE.setCurrentUid(null);
            }
        }
    }
}