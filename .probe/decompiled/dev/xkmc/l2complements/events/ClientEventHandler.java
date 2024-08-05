package dev.xkmc.l2complements.events;

import com.mojang.datafixers.util.Either;
import dev.xkmc.l2complements.content.enchantment.core.CustomDescEnchantment;
import dev.xkmc.l2complements.content.feature.EntityFeature;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.data.LCKeys;
import dev.xkmc.l2complements.network.RotateDiggerToServer;
import dev.xkmc.l2itemselector.events.GenericKeyEvent;
import dev.xkmc.l2library.util.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2complements", bus = Bus.FORGE)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onScreenEffect(RenderBlockScreenEffectEvent event) {
        if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.FIRE && EntityFeature.FIRE_REJECT.test(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onInput(GenericKeyEvent event) {
        if (event.test(LCKeys.DIG.map.getKey()) && event.getAction() == 1 && Proxy.getClientPlayer() != null && Proxy.getClientPlayer().m_21205_().isEnchanted()) {
            L2Complements.HANDLER.toServer(new RotateDiggerToServer(Screen.hasShiftDown()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void modifyItemTooltip(ItemTooltipEvent event) {
        List<Component> list = event.getToolTip();
        int n = list.size();
        if (event.getItemStack().isEnchanted() || event.getItemStack().is(Items.ENCHANTED_BOOK)) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(event.getItemStack());
            String prefix = "enchantment.l2complements.";
            String suffix = ".desc";
            boolean alt = Screen.hasAltDown();
            boolean flag = false;
            boolean book = event.getItemStack().is(Items.ENCHANTED_BOOK);
            List<Either<Component, List<Component>>> compound = new ArrayList();
            for (Component e : list) {
                compound.add(Either.left(e));
            }
            for (int i = 0; i < n; i++) {
                Component lit;
                Component comp;
                label40: {
                    comp = (Component) list.get(i);
                    if (comp.getContents() instanceof LiteralContents txt && comp.getSiblings().size() == 1) {
                        comp = (Component) comp.getSiblings().get(0);
                        lit = Component.literal(txt.text());
                        break label40;
                    }
                    lit = Component.empty();
                }
                if (comp.getContents() instanceof TranslatableContents tr && tr.getKey().startsWith(prefix) && tr.getKey().endsWith(suffix)) {
                    String id = tr.getKey().substring("enchantment.l2complements.".length(), tr.getKey().length() - suffix.length());
                    Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("l2complements", id));
                    if (ench instanceof CustomDescEnchantment base) {
                        int lv = (Integer) map.getOrDefault(ench, 0);
                        List<Component> es = base.descFull(lv, tr.getKey(), alt, book);
                        compound.set(i, Either.right(es.stream().map(e -> lit.copy().append(e)).toList()));
                        flag = true;
                    }
                }
            }
            if (flag) {
                list.clear();
                list.addAll(compound.stream().flatMap(e -> (Stream) e.map(Stream::of, Collection::stream)).toList());
            }
        }
    }
}