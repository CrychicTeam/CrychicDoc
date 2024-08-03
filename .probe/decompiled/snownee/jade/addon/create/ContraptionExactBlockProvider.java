package snownee.jade.addon.create;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.addon.core.ObjectNameProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.TextElement;

public enum ContraptionExactBlockProvider implements IEntityComponentProvider {

    INSTANCE;

    private final Cache<Entity, Accessor<?>> accessorCache = CacheBuilder.newBuilder().weakKeys().expireAfterAccess(100L, TimeUnit.MILLISECONDS).build();

    @Nullable
    @Override
    public IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
        Accessor<?> exact = (Accessor<?>) this.accessorCache.getIfPresent(accessor.getEntity());
        return exact == null ? null : CreatePlugin.client.getAccessorHandler(exact.getAccessorType()).getIcon(exact);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        Accessor<?> exact = (Accessor<?>) this.accessorCache.getIfPresent(accessor.getEntity());
        if (exact != null) {
            ITooltip dummy = IElementHelper.get().tooltip();
            if (exact instanceof BlockAccessor blockAccessor) {
                ObjectNameProvider.INSTANCE.appendTooltip(dummy, blockAccessor, config);
            } else if (exact instanceof EntityAccessor entityAccessor) {
                ObjectNameProvider.INSTANCE.appendTooltip(dummy, entityAccessor, config);
            }
            if (!dummy.isEmpty()) {
                tooltip.remove(Identifiers.CORE_OBJECT_NAME);
                tooltip.add(0, dummy.get(0, IElement.Align.LEFT).stream().map(e -> {
                    if (e instanceof TextElement text) {
                        e = IElementHelper.get().text(IThemeHelper.get().title(text.text.getString()).m_6881_().withStyle(ChatFormatting.ITALIC));
                    }
                    return e.tag(Identifiers.CORE_OBJECT_NAME);
                }).toList());
            }
        }
    }

    public void setHit(Entity entity, Accessor<?> accessor) {
        this.accessorCache.put(entity, accessor);
    }

    @Override
    public ResourceLocation getUid() {
        return CreatePlugin.CONTRAPTION_EXACT_BLOCK;
    }

    @Override
    public int getDefaultPriority() {
        return -10000;
    }
}