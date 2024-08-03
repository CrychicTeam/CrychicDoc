package com.nameless.indestructible.main;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.nameless.indestructible.client.UIConfig;
import com.nameless.indestructible.client.gui.StatusIndicator;
import com.nameless.indestructible.command.AHPatchPlayAnimationCommand;
import com.nameless.indestructible.command.AHPatchSetLookAtCommand;
import com.nameless.indestructible.command.AHPatchSetPhaseCommand;
import com.nameless.indestructible.data.AdvancedMobpatchReloader;
import com.nameless.indestructible.gameasset.GuardAnimations;
import com.nameless.indestructible.network.SPDatapackSync;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.client.gui.EntityIndicator;
import yesman.epicfight.network.EpicFightNetworkManager;

@Mod("indestructible")
public class Indestructible {

    public static final String MOD_ID = "indestructible";

    public static final Logger LOGGER = LogManager.getLogger("indestructible");

    public Indestructible() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(GuardAnimations::registerAnimations);
        bus.addListener(this::doCommonStuff);
        bus.addListener(this::doClientStuff);
        ModLoadingContext.get().registerConfig(Type.CLIENT, UIConfig.SPEC);
        MinecraftForge.EVENT_BUS.addListener(this::reloadListnerEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onDatapackSync);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void doCommonStuff(FMLCommonSetupEvent event) {
        EpicFightNetworkManager.INSTANCE.registerMessage(99, SPDatapackSync.class, SPDatapackSync::toBytes, SPDatapackSync::fromBytes, SPDatapackSync::handle);
    }

    private void doClientStuff(FMLClientSetupEvent event) {
        EntityIndicator.ENTITY_INDICATOR_RENDERERS.add(new StatusIndicator());
    }

    private void reloadListnerEvent(AddReloadListenerEvent event) {
        event.addListener(new AdvancedMobpatchReloader());
    }

    private void onDatapackSync(OnDatapackSyncEvent event) {
        ServerPlayer player = event.getPlayer();
        if (player != null) {
            if (!player.m_20194_().isSingleplayerOwner(player.m_36316_())) {
                SPDatapackSync mobPatchPacket = new SPDatapackSync(AdvancedMobpatchReloader.getTagCount());
                AdvancedMobpatchReloader.getDataStream().forEach(mobPatchPacket::write);
                EpicFightNetworkManager.sendToPlayer(mobPatchPacket, player);
            }
        } else {
            event.getPlayerList().getPlayers().forEach(serverPlayer -> {
                SPDatapackSync mobPatchPacketx = new SPDatapackSync(AdvancedMobpatchReloader.getTagCount());
                AdvancedMobpatchReloader.getDataStream().forEach(mobPatchPacketx::write);
                EpicFightNetworkManager.sendToPlayer(mobPatchPacketx, serverPlayer);
            });
        }
    }

    private void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) LiteralArgumentBuilder.literal("indestructible").requires(source -> source.hasPermission(2))).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("living_entity", EntityArgument.entity()).then(AHPatchSetPhaseCommand.register())).then(AHPatchSetLookAtCommand.register())).then(AHPatchPlayAnimationCommand.register())));
    }
}