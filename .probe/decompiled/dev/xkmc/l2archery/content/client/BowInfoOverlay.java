package dev.xkmc.l2archery.content.client;

import dev.xkmc.l2archery.content.feature.FeatureList;
import dev.xkmc.l2archery.content.item.ArrowData;
import dev.xkmc.l2archery.content.item.BowData;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.content.item.IBowConfig;
import dev.xkmc.l2archery.content.item.IGeneralConfig;
import dev.xkmc.l2archery.init.data.ArcheryConfig;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2library.base.overlay.InfoSideBar;
import dev.xkmc.l2library.base.overlay.SideBar;
import dev.xkmc.l2library.util.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;

public class BowInfoOverlay extends InfoSideBar<BowInfoOverlay.BowStackSignature> {

    public BowInfoOverlay() {
        super(40.0F, 3.0F);
    }

    @Override
    protected List<Component> getText() {
        LocalPlayer player = Proxy.getClientPlayer();
        assert player != null;
        ItemStack bowStack = player.m_21205_();
        ItemStack arrowStack = player.m_6298_(bowStack);
        if (bowStack.getItem() instanceof GenericBowItem bow) {
            ArrowData arrowData = bow.parseArrow(arrowStack);
            if (arrowData == null) {
                return List.of();
            } else {
                BowData bowData = BowData.of(bow, bowStack);
                FeatureList features = FeatureList.merge(bowData.getFeatures(), arrowData.getFeatures());
                List<Component> text = new ArrayList();
                addStat(text, bowData, arrowData.getItem().getConfig());
                features.addEffectsTooltip(text);
                features.addTooltip(text);
                return text;
            }
        } else {
            return List.of();
        }
    }

    private static void addStat(List<Component> list, BowData data, IGeneralConfig arrow) {
        IBowConfig bow = data.getConfig();
        double dmg = 2.0;
        HashMap<Enchantment, Integer> map = data.ench();
        int power = (Integer) map.getOrDefault(Enchantments.POWER_ARROWS, 0);
        if (power > 0) {
            dmg += (double) power * 0.5 + 0.5;
        }
        int punch = (Integer) map.getOrDefault(Enchantments.PUNCH_ARROWS, 0);
        dmg += (double) (bow.damage() + arrow.damage());
        dmg *= (double) bow.speed();
        dmg = Math.ceil(dmg);
        String result = ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(dmg) + "~" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(dmg + dmg / 2.0 + 2.0);
        list.add(LangData.STAT_DAMAGE.getWithColor(result, ChatFormatting.GREEN));
        list.add(LangData.STAT_PUNCH.getWithColor(punch + bow.punch() + arrow.punch(), ChatFormatting.GREEN));
        list.add(LangData.STAT_PULL_TIME.getWithColor((double) bow.pull_time() / 20.0, ChatFormatting.GREEN));
        list.add(LangData.STAT_SPEED.getWithColor(bow.speed() * 20.0F, ChatFormatting.GREEN));
        list.add(LangData.STAT_FOV.getWithColor(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double) (1.0F / (1.0F - bow.fov()))), ChatFormatting.GREEN));
    }

    public BowInfoOverlay.BowStackSignature getSignature() {
        LocalPlayer player = Proxy.getClientPlayer();
        assert player != null;
        ItemStack bowStack = player.m_21205_();
        ItemStack arrowStack = player.m_6298_(bowStack);
        return new BowInfoOverlay.BowStackSignature(player.m_150109_().selected, arrowStack);
    }

    @Override
    public boolean isScreenOn() {
        LocalPlayer player = Proxy.getClientPlayer();
        if (player == null) {
            return false;
        } else {
            ItemStack bowStack = player.m_21205_();
            if (!(bowStack.getItem() instanceof BowItem)) {
                return false;
            } else {
                ItemStack arrowStack = player.m_6298_(bowStack);
                if (!ArcheryConfig.CLIENT.showInfo.get()) {
                    return false;
                } else if (bowStack.getItem() instanceof GenericBowItem bow) {
                    ArrowData arrowData = bow.parseArrow(arrowStack);
                    return arrowData != null;
                } else {
                    return false;
                }
            }
        }
    }

    public static record BowStackSignature(int sel, ItemStack bow) implements SideBar.Signature<BowInfoOverlay.BowStackSignature> {

        public boolean shouldRefreshIdle(SideBar<?> sideBar, @Nullable BowInfoOverlay.BowStackSignature old) {
            if (old == null) {
                return true;
            } else {
                return this.sel != old.sel ? true : !ItemStack.isSameItemSameTags(this.bow, old.bow);
            }
        }
    }
}