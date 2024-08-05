package dev.xkmc.l2hostility.content.menu.tab;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.base.overlay.InfoSideBar;
import dev.xkmc.l2library.base.overlay.SideBar;
import dev.xkmc.l2library.util.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public class DifficultyOverlay extends InfoSideBar<SideBar.IntSignature> {

    public DifficultyOverlay() {
        super(40.0F, 3.0F);
    }

    @Override
    protected List<Component> getText() {
        List<Pair<Component, Supplier<List<Component>>>> comp = new ArrayList();
        DifficultyScreen.addDifficultyInfo(comp, ChatFormatting.RED, ChatFormatting.GREEN, ChatFormatting.GOLD);
        return comp.stream().map(Pair::getFirst).toList();
    }

    @Override
    protected boolean isOnHold() {
        return true;
    }

    public SideBar.IntSignature getSignature() {
        return new SideBar.IntSignature(0);
    }

    @Override
    public boolean isScreenOn() {
        if (Minecraft.getInstance().screen != null) {
            return false;
        } else {
            LocalPlayer player = Proxy.getClientPlayer();
            return player == null ? false : player.m_21205_().is((Item) LHItems.DETECTOR.get()) || player.m_21206_().is((Item) LHItems.DETECTOR.get());
        }
    }
}