package org.embeddedt.modernfix.forge.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.embeddedt.modernfix.ModernFixClient;
import org.embeddedt.modernfix.forge.config.NightConfigFixer;
import org.embeddedt.modernfix.screen.ModernFixConfigScreen;

public class ModernFixClientForge {

    private static ModernFixClient commonMod;

    private KeyMapping configKey;

    private static final List<String> brandingList = new ArrayList();

    public ModernFixClientForge() {
        commonMod = new ModernFixClient();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::keyBindRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new ModernFixConfigScreen(screen)));
    }

    private void keyBindRegister(RegisterKeyMappingsEvent event) {
        this.configKey = new KeyMapping("key.modernfix.config", KeyConflictContext.UNIVERSAL, InputConstants.UNKNOWN, "key.modernfix");
        event.register(this.configKey);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public void onConfigKey(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && this.configKey != null && this.configKey.consumeClick()) {
            Minecraft.getInstance().setScreen(new ModernFixConfigScreen(Minecraft.getInstance().screen));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onClientChat(RegisterClientCommandsEvent event) {
        event.getDispatcher().register((LiteralArgumentBuilder) LiteralArgumentBuilder.literal("mfrc").executes(context -> {
            NightConfigFixer.runReloads();
            return 1;
        }));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderOverlay(CustomizeGuiOverlayEvent.DebugText event) {
        if (commonMod.brandingString != null && Minecraft.getInstance().options.renderDebug) {
            if (brandingList.size() == 0) {
                brandingList.add("");
                brandingList.add(commonMod.brandingString);
            }
            int targetIdx = 0;
            int numSeenBlanks = 0;
            List<String> right;
            for (right = event.getRight(); targetIdx < right.size(); targetIdx++) {
                String s = (String) right.get(targetIdx);
                if (s == null || s.length() == 0) {
                    numSeenBlanks++;
                }
                if (numSeenBlanks == 3) {
                    break;
                }
            }
            right.addAll(targetIdx, brandingList);
        }
    }

    @SubscribeEvent
    public void onDisconnect(LevelEvent.Unload event) {
        if (event.getLevel().m_5776_()) {
            DebugScreenOverlay overlay = (DebugScreenOverlay) ObfuscationReflectionHelper.getPrivateValue(ForgeGui.class, (ForgeGui) Minecraft.getInstance().gui, "debugOverlay");
            if (overlay != null) {
                Minecraft.getInstance().m_6937_(overlay::m_94040_);
            }
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartedEvent event) {
        commonMod.onServerStarted(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderTickEnd(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            commonMod.onRenderTickEnd();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRecipes(RecipesUpdatedEvent e) {
        commonMod.onRecipesUpdated();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTags(TagsUpdatedEvent e) {
        commonMod.onTagsUpdated();
    }
}