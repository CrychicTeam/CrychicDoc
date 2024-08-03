package dev.ftb.mods.ftblibrary.util;

import dev.ftb.mods.ftblibrary.FTBLibrary;
import dev.ftb.mods.ftblibrary.core.DisplayInfoFTBL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class KnownServerRegistries {

    public static KnownServerRegistries client;

    public static KnownServerRegistries server;

    public final List<ResourceLocation> dimensions;

    public final Map<ResourceLocation, KnownServerRegistries.AdvancementInfo> advancements;

    public KnownServerRegistries(FriendlyByteBuf buffer) {
        int s = buffer.readVarInt();
        this.dimensions = new ArrayList(s);
        for (int i = 0; i < s; i++) {
            this.dimensions.add(buffer.readResourceLocation());
        }
        s = buffer.readVarInt();
        this.advancements = new LinkedHashMap(s);
        for (int i = 0; i < s; i++) {
            KnownServerRegistries.AdvancementInfo info = new KnownServerRegistries.AdvancementInfo();
            info.id = buffer.readResourceLocation();
            info.name = buffer.readComponent();
            info.icon = buffer.readItem();
            this.advancements.put(info.id, info);
        }
        FTBLibrary.LOGGER.debug("Received server registries");
    }

    public KnownServerRegistries(MinecraftServer server) {
        this.dimensions = new ArrayList();
        for (ServerLevel level : server.getAllLevels()) {
            this.dimensions.add(level.m_46472_().location());
        }
        this.dimensions.sort(null);
        List<KnownServerRegistries.AdvancementInfo> advancementList = new ArrayList();
        for (Advancement advancement : server.getAdvancements().getAllAdvancements()) {
            if (advancement.getDisplay() instanceof DisplayInfoFTBL) {
                KnownServerRegistries.AdvancementInfo info = new KnownServerRegistries.AdvancementInfo();
                info.id = advancement.getId();
                info.name = advancement.getDisplay().getTitle();
                info.icon = ((DisplayInfoFTBL) advancement.getDisplay()).getIconStackFTBL();
                advancementList.add(info);
            }
        }
        advancementList.sort(Comparator.comparing(o -> o.id));
        this.advancements = new LinkedHashMap(advancementList.size());
        for (KnownServerRegistries.AdvancementInfo info : advancementList) {
            this.advancements.put(info.id, info);
        }
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.dimensions.size());
        for (ResourceLocation id : this.dimensions) {
            buffer.writeResourceLocation(id);
        }
        buffer.writeVarInt(this.advancements.size());
        for (KnownServerRegistries.AdvancementInfo info : this.advancements.values()) {
            buffer.writeResourceLocation(info.id);
            buffer.writeComponent(info.name);
            buffer.writeItem(info.icon);
        }
    }

    public static class AdvancementInfo {

        public ResourceLocation id;

        public Component name;

        public ItemStack icon;
    }
}