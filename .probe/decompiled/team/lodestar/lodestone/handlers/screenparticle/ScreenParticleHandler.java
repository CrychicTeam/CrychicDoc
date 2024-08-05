package team.lodestar.lodestone.handlers.screenparticle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import org.joml.Matrix4f;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleItemStackKey;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleItemStackRetrievalKey;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

public class ScreenParticleHandler {

    public static final Map<ScreenParticleItemStackKey, ScreenParticleHolder> ITEM_PARTICLES = new HashMap();

    public static final Map<ScreenParticleItemStackRetrievalKey, ItemStack> ITEM_STACK_CACHE = new HashMap();

    public static final Collection<ScreenParticleItemStackRetrievalKey> ACTIVELY_ACCESSED_KEYS = new ArrayList();

    public static ScreenParticleHolder cachedItemParticles = null;

    public static int currentItemX;

    public static int currentItemY;

    public static final Tesselator TESSELATOR = new Tesselator();

    public static boolean canSpawnParticles;

    public static boolean renderingHotbar;

    public static void tickParticles() {
        if (ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            ITEM_PARTICLES.values().forEach(ScreenParticleHolder::tick);
            ITEM_PARTICLES.values().removeIf(ScreenParticleHolder::isEmpty);
            ITEM_STACK_CACHE.keySet().removeIf(k -> !ACTIVELY_ACCESSED_KEYS.contains(k));
            ACTIVELY_ACCESSED_KEYS.clear();
            canSpawnParticles = true;
        }
    }

    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            canSpawnParticles = false;
        }
    }

    public static void renderItemStackEarly(PoseStack poseStack, ItemStack stack, int x, int y) {
        if (ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null && minecraft.player != null) {
                if (minecraft.isPaused()) {
                    return;
                }
                if (!stack.isEmpty()) {
                    List<ParticleEmitterHandler.ItemParticleSupplier> emitters = (List<ParticleEmitterHandler.ItemParticleSupplier>) ParticleEmitterHandler.EMITTERS.get(stack.getItem());
                    if (emitters != null) {
                        currentItemX = x + 8;
                        currentItemY = y + 8;
                        if (currentItemX == 8 && currentItemY == 8) {
                            Matrix4f pose = poseStack.last().pose();
                            float xOffset = pose.m30();
                            float yOffset = pose.m31();
                            currentItemX = (int) ((float) currentItemX + xOffset);
                            currentItemY = (int) ((float) currentItemY + yOffset);
                        } else if (!renderingHotbar && minecraft.screen instanceof AbstractContainerScreen<?> containerScreen) {
                            currentItemX = currentItemX + containerScreen.leftPos;
                            currentItemY = currentItemY + containerScreen.topPos;
                        }
                        for (ParticleEmitterHandler.ItemParticleSupplier emitter : emitters) {
                            renderParticles(spawnAndPullParticles(minecraft.level, emitter, stack, false));
                            cachedItemParticles = spawnAndPullParticles(minecraft.level, emitter, stack, true);
                        }
                    }
                }
            }
        }
    }

    public static ScreenParticleHolder spawnAndPullParticles(ClientLevel level, ParticleEmitterHandler.ItemParticleSupplier emitter, ItemStack stack, boolean isRenderedAfterItem) {
        ScreenParticleItemStackRetrievalKey cacheKey = new ScreenParticleItemStackRetrievalKey(renderingHotbar, isRenderedAfterItem, currentItemX, currentItemY);
        ScreenParticleHolder target = (ScreenParticleHolder) ITEM_PARTICLES.computeIfAbsent(new ScreenParticleItemStackKey(renderingHotbar, isRenderedAfterItem, stack), s -> new ScreenParticleHolder());
        pullFromParticleVault(cacheKey, stack, target, isRenderedAfterItem);
        if (canSpawnParticles) {
            if (isRenderedAfterItem) {
                float var10005 = (float) currentItemX;
                emitter.spawnLateParticles(target, level, Minecraft.getInstance().timer.partialTick, stack, var10005, (float) currentItemY);
            } else {
                float var6 = (float) currentItemX;
                emitter.spawnEarlyParticles(target, level, Minecraft.getInstance().timer.partialTick, stack, var6, (float) currentItemY);
            }
        }
        ACTIVELY_ACCESSED_KEYS.add(cacheKey);
        return target;
    }

    public static void pullFromParticleVault(ScreenParticleItemStackRetrievalKey cacheKey, ItemStack currentStack, ScreenParticleHolder target, boolean isRenderedAfterItem) {
        if (ITEM_STACK_CACHE.containsKey(cacheKey)) {
            ItemStack oldStack = (ItemStack) ITEM_STACK_CACHE.get(cacheKey);
            if (oldStack != currentStack && oldStack.getItem().equals(currentStack.getItem())) {
                ScreenParticleItemStackKey oldKey = new ScreenParticleItemStackKey(renderingHotbar, isRenderedAfterItem, oldStack);
                ScreenParticleHolder oldParticles = (ScreenParticleHolder) ITEM_PARTICLES.get(oldKey);
                if (oldParticles != null) {
                    target.addFrom(oldParticles);
                }
                ITEM_STACK_CACHE.remove(cacheKey);
                ITEM_PARTICLES.remove(oldKey);
            }
        }
        ITEM_STACK_CACHE.put(cacheKey, currentStack);
    }

    public static void renderItemStackLate() {
        if (cachedItemParticles != null) {
            renderParticles(cachedItemParticles);
            cachedItemParticles = null;
        }
    }

    public static void renderParticles(ScreenParticleHolder screenParticleTarget) {
        screenParticleTarget.particles.forEach((renderType, particles) -> {
            renderType.begin(TESSELATOR.getBuilder(), Minecraft.getInstance().textureManager);
            for (ScreenParticle next : particles) {
                next.render(TESSELATOR.getBuilder());
            }
            renderType.end(TESSELATOR);
        });
    }

    public static void clearParticles() {
        ITEM_PARTICLES.values().forEach(ScreenParticleHandler::clearParticles);
    }

    public static void clearParticles(ScreenParticleHolder screenParticleTarget) {
        screenParticleTarget.particles.values().forEach(ArrayList::clear);
    }

    public static <T extends ScreenParticleOptions> ScreenParticle addParticle(ScreenParticleHolder screenParticleTarget, T options, double x, double y, double xMotion, double yMotion) {
        Minecraft minecraft = Minecraft.getInstance();
        ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
        ScreenParticle particle = type.provider.createParticle(minecraft.level, options, x, y, xMotion, yMotion);
        ArrayList<ScreenParticle> list = (ArrayList<ScreenParticle>) screenParticleTarget.particles.computeIfAbsent(options.renderType, a -> new ArrayList());
        list.add(particle);
        return particle;
    }
}