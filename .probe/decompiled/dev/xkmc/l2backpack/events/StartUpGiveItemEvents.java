package dev.xkmc.l2backpack.events;

import dev.xkmc.l2backpack.content.backpack.BackpackItem;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2backpack", bus = Bus.FORGE)
public class StartUpGiveItemEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player.f_19797_ == 10) {
            if (event.player instanceof ServerPlayer sp) {
                int time = sp.getStats().m_13015_(Stats.CUSTOM.get(Stats.PLAY_TIME));
                if (time <= 100) {
                    Advancement adv = sp.server.getAdvancements().getAdvancement(new ResourceLocation("l2backpack", "detection"));
                    if (adv != null) {
                        AdvancementProgress prog = sp.getAdvancements().getOrStartProgress(adv);
                        if (!prog.isDone()) {
                            int target = BackpackConfig.COMMON.startupBackpackCondition.get();
                            NonNullList<ItemStack> list = sp.m_150109_().items;
                            int count = 0;
                            for (ItemStack stack : list) {
                                if (!stack.isEmpty()) {
                                    count++;
                                }
                            }
                            if (count >= target) {
                                int initialRow = Math.max(BackpackConfig.COMMON.initialRows.get(), (count - 1) / 9 + 1);
                                ItemStack stackx = BackpackItem.setRow(BackpackItems.BACKPACKS[DyeColor.WHITE.ordinal()].asStack(), initialRow);
                                NonNullList<ItemStack> ans = NonNullList.withSize(initialRow * 9, ItemStack.EMPTY);
                                int index = 0;
                                for (int i = 0; i < list.size(); i++) {
                                    if (!list.get(i).isEmpty() && list.get(i).getItem().canFitInsideContainerItems()) {
                                        ans.set(index++, list.get(i).copy());
                                        list.set(i, ItemStack.EMPTY);
                                    }
                                }
                                BackpackItem.setItems(stackx, ans);
                                sp.m_150109_().placeItemBackInInventory(stackx);
                            }
                        }
                    }
                }
            }
        }
    }
}