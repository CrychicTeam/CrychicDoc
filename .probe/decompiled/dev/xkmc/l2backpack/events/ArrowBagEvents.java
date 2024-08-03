package dev.xkmc.l2backpack.events;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapManager;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapTypes;
import java.util.List;
import java.util.function.IntConsumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = "l2backpack", bus = Bus.FORGE)
public class ArrowBagEvents {

    public static final ThreadLocal<Pair<ItemStack, IntConsumer>> TEMP = new ThreadLocal();

    @SubscribeEvent
    public static void onProjectileSearch(LivingGetProjectileEvent event) {
        if (event.getProjectileWeaponItemStack().getItem() instanceof ProjectileWeaponItem weapon) {
            ArrowBagEvents.ArrowFindEvent var4 = new ArrowBagEvents.ArrowFindEvent(event.getProjectileWeaponItemStack(), weapon, event.getEntity());
            MinecraftForge.EVENT_BUS.post(var4);
            Pair<ItemStack, IntConsumer> arrow = var4.arrow;
            if (arrow != null) {
                TEMP.set(arrow);
                event.setProjectileItemStack((ItemStack) arrow.getFirst());
            }
        }
    }

    @SubscribeEvent
    public static void onArrowFind(ArrowBagEvents.ArrowFindEvent event) {
        if (event.getEntity() instanceof Player) {
            IQuickSwapToken<?> token = QuickSwapManager.getToken(event.getEntity(), event.getStack(), false);
            if (token != null) {
                if (token.type() == QuickSwapTypes.ARROW) {
                    List<? extends ISwapEntry<?>> arrows = (List<? extends ISwapEntry<?>>) token.getList();
                    int selected = token.getSelected();
                    ISwapEntry<?> entry = (ISwapEntry<?>) arrows.get(selected);
                    ItemStack stack = entry.getStack();
                    if (!stack.isEmpty()) {
                        event.setProjectile(Pair.of(stack, token::shrink));
                    }
                }
            }
        }
    }

    public static void shrink(ItemStack stack, int count) {
        if (TEMP.get() != null && ((Pair) TEMP.get()).getFirst() == stack) {
            ((IntConsumer) ((Pair) TEMP.get()).getSecond()).accept(count);
        }
    }

    public static class ArrowFindEvent extends Event {

        private final ItemStack stack;

        private final ProjectileWeaponItem weapon;

        private final LivingEntity entity;

        private Pair<ItemStack, IntConsumer> arrow;

        public ArrowFindEvent(ItemStack stack, ProjectileWeaponItem weapon, LivingEntity entity) {
            this.stack = stack;
            this.weapon = weapon;
            this.entity = entity;
        }

        public LivingEntity getEntity() {
            return this.entity;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public boolean setProjectile(Pair<ItemStack, IntConsumer> arrow) {
            if (this.weapon.getAllSupportedProjectiles().test((ItemStack) arrow.getFirst())) {
                this.arrow = arrow;
                return true;
            } else {
                return false;
            }
        }

        @Nullable
        public Pair<ItemStack, IntConsumer> getArrow() {
            return this.arrow;
        }
    }
}