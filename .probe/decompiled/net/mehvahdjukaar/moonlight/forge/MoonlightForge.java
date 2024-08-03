package net.mehvahdjukaar.moonlight.forge;

import java.lang.ref.WeakReference;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.forge.ConfigSpecWrapper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.fake_player.FPClientAccess;
import net.mehvahdjukaar.moonlight.core.fluid.SoftFluidInternal;
import net.mehvahdjukaar.moonlight.core.misc.DummyWorld;
import net.mehvahdjukaar.moonlight.core.misc.forge.ModLootConditions;
import net.mehvahdjukaar.moonlight.core.misc.forge.ModLootModifiers;
import net.mehvahdjukaar.moonlight.core.network.ClientBoundSendLoginPacket;
import net.mehvahdjukaar.moonlight.core.network.ModMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

@Mod("moonlight")
public class MoonlightForge {

    public static final String MOD_ID = "moonlight";

    public static final ForgeConfigSpec SPEC = ((ConfigSpecWrapper) ConfigBuilder.create("moonlight", ConfigType.COMMON).buildAndRegister()).getSpec();

    @Nullable
    private static WeakReference<ICondition.IContext> context = null;

    public MoonlightForge() {
        Moonlight.commonInit();
        MinecraftForge.EVENT_BUS.register(MoonlightForge.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MoonlightForge::configsLoaded);
        ModLootModifiers.register();
        ModLootConditions.register();
        if (PlatHelper.getPhysicalSide().isClient()) {
            MoonlightForgeClient.init();
        }
    }

    public static void configsLoaded(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC && !ModLoader.get().hasCompletedState("LOAD_REGISTRIES")) {
            throw new IllegalStateException("Some OTHER mod has forcefully loaded ALL other mods configs before the registry phase. This should not be done. Dont report this to Moonlight. Refusing to proceed further");
        }
    }

    @SubscribeEvent
    public static void onTagUpdated(TagsUpdatedEvent event) {
        Moonlight.afterDataReload(event.getRegistryAccess());
    }

    @Nullable
    public static ICondition.IContext getConditionContext() {
        return context == null ? null : (ICondition.IContext) context.get();
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        context = new WeakReference(event.getConditionContext());
    }

    @SubscribeEvent
    public static void beforeServerStart(ServerAboutToStartEvent event) {
        Moonlight.beforeServerStart();
    }

    @SubscribeEvent
    public static void beforeServerStart(ServerStoppedEvent event) {
        DummyWorld.clearInstance();
    }

    @SubscribeEvent
    public static void onDataSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            SoftFluidInternal.onDataSyncToPlayer(event.getPlayer(), true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            try {
                ModMessages.CHANNEL.sendToClientPlayer(player, new ClientBoundSendLoginPacket());
            } catch (Exception var3) {
            }
        } else {
            Moonlight.checkDatapackRegistry();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDimensionUnload(LevelEvent.Unload event) {
        LevelAccessor level = event.getLevel();
        try {
            if (level.m_5776_()) {
                FPClientAccess.unloadLevel(level);
            }
        } catch (Exception var3) {
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Moonlight.onPlayerCloned(event.getOriginal(), event.getEntity(), event.isWasDeath());
    }

    @SubscribeEvent
    public static void onLevelLoaded(LevelEvent.Load event) {
        if (!event.getLevel().m_5776_()) {
            Moonlight.checkDatapackRegistry();
        }
    }
}