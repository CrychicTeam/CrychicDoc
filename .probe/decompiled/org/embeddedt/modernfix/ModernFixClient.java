package org.embeddedt.modernfix;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MemoryReserve;
import net.minecraft.world.entity.Entity;
import org.embeddedt.modernfix.api.entrypoint.ModernFixClientIntegration;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.embeddedt.modernfix.packet.EntityIDSyncPacket;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;
import org.embeddedt.modernfix.searchtree.JEIBackedSearchTree;
import org.embeddedt.modernfix.searchtree.REIBackedSearchTree;
import org.embeddedt.modernfix.searchtree.SearchTreeProviderRegistry;
import org.embeddedt.modernfix.util.ClassInfoManager;
import org.embeddedt.modernfix.world.IntegratedWatchdog;

public class ModernFixClient {

    public static ModernFixClient INSTANCE;

    public static long worldLoadStartTime = -1L;

    private static int numRenderTicks;

    public static float gameStartTimeSeconds = -1.0F;

    public static boolean recipesUpdated;

    public static boolean tagsUpdated = false;

    public String brandingString = null;

    public static List<ModernFixClientIntegration> CLIENT_INTEGRATIONS = new CopyOnWriteArrayList();

    public static final Set<SynchedEntityData> allEntityDatas = Collections.newSetFromMap(new WeakHashMap());

    private static final Field entriesArrayField;

    public ModernFixClient() {
        INSTANCE = this;
        MemoryReserve.release();
        if (ModernFixMixinPlugin.instance.isOptionEnabled("feature.branding.F3Screen")) {
            this.brandingString = ModernFix.NAME + " " + ModernFixPlatformHooks.INSTANCE.getVersionString();
        }
        SearchTreeProviderRegistry.register(JEIBackedSearchTree.PROVIDER);
        SearchTreeProviderRegistry.register(REIBackedSearchTree.PROVIDER);
        for (String className : ModernFixPlatformHooks.INSTANCE.getCustomModOptions().get("client_entrypoint")) {
            try {
                CLIENT_INTEGRATIONS.add((ModernFixClientIntegration) Class.forName(className).getDeclaredConstructor().newInstance());
            } catch (ClassCastException | ReflectiveOperationException var4) {
                ModernFix.LOGGER.error("Could not instantiate integration {}", className, var4);
            }
        }
        if (ModernFixMixinPlugin.instance.isOptionEnabled("perf.dynamic_resources.FireIntegrationHook")) {
            for (ModernFixClientIntegration integration : CLIENT_INTEGRATIONS) {
                integration.onDynamicResourcesStatusChange(true);
            }
        }
    }

    public void resetWorldLoadStateMachine() {
        numRenderTicks = 0;
        worldLoadStartTime = -1L;
        recipesUpdated = false;
        tagsUpdated = false;
    }

    public void onGameLaunchFinish() {
        if (!(gameStartTimeSeconds >= 0.0F)) {
            gameStartTimeSeconds = (float) ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0F;
            if (ModernFixMixinPlugin.instance.isOptionEnabled("feature.measure_time.GameLoad")) {
                ModernFix.LOGGER.warn("Game took " + gameStartTimeSeconds + " seconds to start");
            }
            ModernFixPlatformHooks.INSTANCE.onLaunchComplete();
            ClassInfoManager.clear();
        }
    }

    public void onRecipesUpdated() {
        recipesUpdated = true;
    }

    public void onTagsUpdated() {
        tagsUpdated = true;
    }

    public void onRenderTickEnd() {
        if (recipesUpdated && tagsUpdated && worldLoadStartTime != -1L && Minecraft.getInstance().player != null && numRenderTicks++ >= 10) {
            float timeSpentLoading = (float) (System.nanoTime() - worldLoadStartTime) / 1.0E9F;
            if (ModernFixMixinPlugin.instance.isOptionEnabled("feature.measure_time.WorldLoad")) {
                ModernFix.LOGGER.warn("Time from main menu to in-game was " + timeSpentLoading + " seconds");
                ModernFix.LOGGER.warn("Total time to load game and open world was " + (timeSpentLoading + gameStartTimeSeconds) + " seconds");
            }
            this.resetWorldLoadStateMachine();
        }
    }

    private static boolean compareAndSwitchIds(Class<? extends Entity> eClass, String fieldName, EntityDataAccessor<?> accessor, int newId) {
        if (accessor.id != newId) {
            ModernFix.LOGGER.warn("Corrected ID mismatch on {} field {}. Client had {} but server wants {}.", eClass, fieldName, accessor.id, newId);
            accessor.id = newId;
            return true;
        } else {
            ModernFix.LOGGER.debug("{} {} ID fine: {}", eClass, fieldName, newId);
            return false;
        }
    }

    public static void handleEntityIDSync(EntityIDSyncPacket packet) {
        Map<Class<? extends Entity>, List<Pair<String, Integer>>> info = packet.getFieldInfo();
        boolean fixNeeded = false;
        for (Entry<Class<? extends Entity>, List<Pair<String, Integer>>> entry : info.entrySet()) {
            Class<? extends Entity> eClass = (Class<? extends Entity>) entry.getKey();
            for (Pair<String, Integer> field : (List) entry.getValue()) {
                String fieldName = (String) field.getFirst();
                int newId = (Integer) field.getSecond();
                try {
                    Field f = eClass.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    EntityDataAccessor<?> accessor = (EntityDataAccessor<?>) f.get(null);
                    if (compareAndSwitchIds(eClass, fieldName, accessor, newId)) {
                        fixNeeded = true;
                    }
                } catch (NoSuchFieldException var19) {
                    ModernFix.LOGGER.warn("Couldn't find field on {}: {}", eClass, fieldName);
                } catch (ReflectiveOperationException var20) {
                    throw new RuntimeException("Unexpected exception", var20);
                }
            }
        }
        synchronized (allEntityDatas) {
            if (fixNeeded) {
                for (SynchedEntityData manager : new ArrayList(allEntityDatas)) {
                    Int2ObjectOpenHashMap<SynchedEntityData.DataItem<?>> fixedMap = new Int2ObjectOpenHashMap();
                    List<SynchedEntityData.DataItem<?>> items = new ArrayList(manager.itemsById.values());
                    for (SynchedEntityData.DataItem<?> item : items) {
                        fixedMap.put(item.getAccessor().id, item);
                    }
                    manager.lock.writeLock().lock();
                    try {
                        manager.itemsById.replaceAll((id, parameter) -> (SynchedEntityData.DataItem) fixedMap.get(id));
                        if (entriesArrayField != null) {
                            try {
                                SynchedEntityData.DataItem<?>[] dataArray = new SynchedEntityData.DataItem[items.size()];
                                for (int i = 0; i < dataArray.length; i++) {
                                    dataArray[i] = (SynchedEntityData.DataItem<?>) fixedMap.get(i);
                                }
                                entriesArrayField.set(manager, dataArray);
                            } catch (ReflectiveOperationException var21) {
                                ModernFix.LOGGER.error(var21);
                            }
                        }
                    } finally {
                        manager.lock.writeLock().unlock();
                    }
                }
            }
            allEntityDatas.clear();
        }
    }

    public void onServerStarted(MinecraftServer server) {
        if (ModernFixMixinPlugin.instance.isOptionEnabled("feature.integrated_server_watchdog.IntegratedWatchdog")) {
            IntegratedWatchdog watchdog = new IntegratedWatchdog(server);
            watchdog.start();
        }
    }

    static {
        Field field;
        try {
            field = SynchedEntityData.class.getDeclaredField("entriesArray");
            field.setAccessible(true);
        } catch (ReflectiveOperationException var2) {
            field = null;
        }
        entriesArrayField = field;
    }
}