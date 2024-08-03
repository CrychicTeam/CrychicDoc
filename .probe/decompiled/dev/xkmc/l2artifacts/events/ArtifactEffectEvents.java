package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

@EventBusSubscriber(modid = "l2artifacts", bus = Bus.FORGE)
public class ArtifactEffectEvents {

    public static <T> void postEvent(LivingEntity entity, T event, ArtifactEffectEvents.EventConsumer<T> cons) {
        Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(entity).resolve();
        if (!opt.isEmpty()) {
            for (SlotResult result : ((ICuriosItemHandler) opt.get()).findCurios(stackx -> stackx.getItem() instanceof BaseArtifact)) {
                ItemStack stack = result.stack();
                BaseArtifact base = (BaseArtifact) stack.getItem();
                ((ArtifactSet) base.set.get()).propagateEvent(result.slotContext(), event, cons);
            }
        }
    }

    public static <T> boolean postEvent(LivingEntity entity, T event, ArtifactEffectEvents.EventPredicate<T> cons) {
        Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(entity).resolve();
        if (opt.isEmpty()) {
            return false;
        } else {
            List<SlotResult> list = ((ICuriosItemHandler) opt.get()).findCurios(stackx -> stackx.getItem() instanceof BaseArtifact);
            boolean ans = false;
            for (SlotResult result : list) {
                ItemStack stack = result.stack();
                BaseArtifact base = (BaseArtifact) stack.getItem();
                ans |= ((ArtifactSet) base.set.get()).propagateEvent(result.slotContext(), event, cons);
            }
            return ans;
        }
    }

    @SubscribeEvent
    public static void onKillEvent(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity player) {
            postEvent(player, event, SetEffect::playerKillOpponentEvent);
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        postEvent(event.getEntity(), event, SetEffect::playerShieldBlock);
    }

    public interface EventConsumer<T> {

        void apply(SetEffect var1, LivingEntity var2, ArtifactSetConfig.Entry var3, int var4, T var5);
    }

    public interface EventPredicate<T> {

        boolean apply(SetEffect var1, LivingEntity var2, ArtifactSetConfig.Entry var3, int var4, T var5);
    }
}