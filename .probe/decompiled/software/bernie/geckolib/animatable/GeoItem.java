package software.bernie.geckolib.animatable;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.AnimatableIdCache;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.ContextAwareAnimatableManager;
import software.bernie.geckolib.util.RenderUtils;

public interface GeoItem extends SingletonGeoAnimatable {

    String ID_NBT_KEY = "GeckoLibID";

    static void registerSyncedAnimatable(GeoAnimatable animatable) {
        SingletonGeoAnimatable.registerSyncedAnimatable(animatable);
    }

    static long getId(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? Long.MAX_VALUE : tag.getLong("GeckoLibID");
    }

    static long getOrAssignId(ItemStack stack, ServerLevel level) {
        CompoundTag tag = stack.getOrCreateTag();
        long id = tag.getLong("GeckoLibID");
        if (tag.contains("GeckoLibID", 99)) {
            return id;
        } else {
            id = AnimatableIdCache.getFreeId(level);
            tag.putLong("GeckoLibID", id);
            return id;
        }
    }

    @Override
    default double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }

    default boolean isPerspectiveAware() {
        return false;
    }

    @Nullable
    @Override
    default AnimatableInstanceCache animatableCacheOverride() {
        return (AnimatableInstanceCache) (this.isPerspectiveAware() ? new GeoItem.ContextBasedAnimatableInstanceCache(this) : SingletonGeoAnimatable.super.animatableCacheOverride());
    }

    public static class ContextBasedAnimatableInstanceCache extends SingletonAnimatableInstanceCache {

        public ContextBasedAnimatableInstanceCache(GeoAnimatable animatable) {
            super(animatable);
        }

        @Override
        public AnimatableManager<?> getManagerForId(long uniqueId) {
            if (!this.managers.containsKey(uniqueId)) {
                this.managers.put(uniqueId, new ContextAwareAnimatableManager<GeoItem, ItemDisplayContext>(this.animatable) {

                    @Override
                    protected Map<ItemDisplayContext, AnimatableManager<GeoItem>> buildContextOptions(GeoAnimatable animatable) {
                        Map<ItemDisplayContext, AnimatableManager<GeoItem>> map = new EnumMap(ItemDisplayContext.class);
                        for (ItemDisplayContext context : ItemDisplayContext.values()) {
                            map.put(context, new AnimatableManager(animatable));
                        }
                        return map;
                    }

                    public ItemDisplayContext getCurrentContext() {
                        ItemDisplayContext context = this.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);
                        return context == null ? ItemDisplayContext.NONE : context;
                    }
                });
            }
            return (AnimatableManager<?>) this.managers.get(uniqueId);
        }
    }
}