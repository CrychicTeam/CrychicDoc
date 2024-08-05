package dev.xkmc.modulargolems.content.item.golem;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.ModularGolems;
import io.netty.util.collection.IntObjectHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "modulargolems", bus = Bus.FORGE)
public class ClientHolderManager {

    private static final int LIFE = 200;

    private static final IntObjectHashMap<ClientHolderManager.TimedCache> CACHE = new IntObjectHashMap();

    @SubscribeEvent
    public static void tickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            if (CACHE.size() > 100) {
                ModularGolems.LOGGER.error("Golem cache overflow. Clearing...");
                CACHE.clear();
            } else {
                CACHE.entrySet().removeIf(e -> {
                    ClientHolderManager.TimedCache var10000 = (ClientHolderManager.TimedCache) e.getValue();
                    int var1 = var10000.life;
                    var10000.life -= 1;
                    return var1 <= 0;
                });
            }
        }
    }

    @Nullable
    public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> T getEntityForDisplay(GolemHolder<T, P> holder, ItemStack stack) {
        CompoundTag root = stack.getTag();
        if (root == null) {
            return null;
        } else if (!root.contains("golem_entity") && !root.contains("golem_icon")) {
            return null;
        } else {
            int hash = stack.hashCode();
            if (CACHE.containsKey(hash)) {
                AbstractGolemEntity<?, ?> ans = ((ClientHolderManager.TimedCache) CACHE.get(stack.hashCode())).entity;
                return (T) (ans == null ? null : Wrappers.cast(ans));
            } else {
                T golem = getEntityForDisplayInternal(holder, stack);
                ClientHolderManager.TimedCache cache = new ClientHolderManager.TimedCache(200, golem);
                CACHE.put(hash, cache);
                return golem;
            }
        }
    }

    @Nullable
    private static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> T getEntityForDisplayInternal(GolemHolder<T, P> holder, ItemStack stack) {
        CompoundTag root = stack.getTag();
        if (root == null) {
            return null;
        } else {
            T ans = null;
            if (root.contains("golem_entity")) {
                CompoundTag entity = root.getCompound("golem_entity");
                ans = holder.getEntityType().createForDisplay(entity);
            } else if (root.contains("golem_icon")) {
                AbstractGolemEntity<?, ?> golem = holder.getEntityType().create(Proxy.getClientWorld());
                golem.onCreate(GolemHolder.getMaterial(stack), GolemHolder.getUpgrades(stack), null);
                ItemCompoundTag tag = ItemCompoundTag.of(stack);
                ListTag list = tag.getSubList("golem_icon", 10).getOrCreate();
                for (int i = 0; i < list.size(); i++) {
                    ItemStack e = ItemStack.of(list.getCompound(i));
                    golem.m_255207_(e);
                }
                ans = (T) Wrappers.cast(golem);
            }
            if (ans == null) {
                return null;
            } else {
                ans.f_20916_ = 0;
                return ans;
            }
        }
    }

    static final class TimedCache {

        @Nullable
        private final AbstractGolemEntity<?, ?> entity;

        private int life;

        TimedCache(int life, @Nullable AbstractGolemEntity<?, ?> entity) {
            this.life = life;
            this.entity = entity;
            if (entity != null) {
                entity.m_20049_("ClientOnly");
            }
        }
    }
}