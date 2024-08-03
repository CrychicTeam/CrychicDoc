package snownee.jade.overlay;

import com.google.common.base.Suppliers;
import com.mojang.text2speech.Narrator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import snownee.jade.Jade;
import snownee.jade.api.Accessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.callback.JadeRayTraceCallback;
import snownee.jade.api.callback.JadeTooltipCollectedCallback;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.gui.BaseOptionsScreen;
import snownee.jade.impl.ObjectDataCenter;
import snownee.jade.impl.Tooltip;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.util.ClientProxy;

public class WailaTickHandler {

    private static WailaTickHandler INSTANCE = new WailaTickHandler();

    private static final Supplier<Narrator> NARRATOR = Suppliers.memoize(Narrator::getNarrator);

    private static String lastNarration = "";

    private static long lastNarrationTime = 0L;

    public TooltipRenderer tooltipRenderer = null;

    public ProgressTracker progressTracker = new ProgressTracker();

    public static WailaTickHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new WailaTickHandler();
        }
        return INSTANCE;
    }

    public static void narrate(ITooltip tooltip, boolean dedupe) {
        if (((Narrator) NARRATOR.get()).active() && !tooltip.isEmpty()) {
            if (System.currentTimeMillis() - lastNarrationTime >= 500L) {
                String narration = tooltip.getMessage();
                if (!dedupe || !narration.equals(lastNarration)) {
                    CompletableFuture.runAsync(() -> {
                        Narrator narrator = (Narrator) NARRATOR.get();
                        narrator.clear();
                        narrator.say(StringUtil.stripColor(narration), false);
                    });
                    lastNarration = narration;
                    lastNarrationTime = System.currentTimeMillis();
                }
            }
        }
    }

    public static void clearLastNarration() {
        lastNarration = "";
    }

    public void tickClient() {
        this.progressTracker.tick();
        IWailaConfig.IConfigGeneral config = Jade.CONFIG.get().getGeneral();
        if (!config.shouldDisplayTooltip()) {
            this.tooltipRenderer = null;
        } else {
            Minecraft client = Minecraft.getInstance();
            if (ClientProxy.shouldShowWithOverlay(client, client.screen)) {
                Level world = client.level;
                Entity entity = client.getCameraEntity();
                if (world != null && entity != null) {
                    RayTracing.INSTANCE.fire();
                    HitResult target = RayTracing.INSTANCE.getTarget();
                    Tooltip tooltip = new Tooltip();
                    if (target == null) {
                        this.tooltipRenderer = null;
                    } else {
                        Accessor<?> accessor;
                        label86: {
                            accessor = null;
                            if (target instanceof BlockHitResult blockTarget && blockTarget.getType() != HitResult.Type.MISS) {
                                BlockState state = RayTracing.wrapBlock(world, blockTarget, CollisionContext.of(entity));
                                BlockEntity tileEntity = world.getBlockEntity(blockTarget.getBlockPos());
                                accessor = WailaClientRegistration.INSTANCE.blockAccessor().blockState(state).blockEntity(tileEntity).hit(blockTarget).requireVerification().build();
                                break label86;
                            }
                            if (target instanceof EntityHitResult entityTarget) {
                                accessor = WailaClientRegistration.INSTANCE.entityAccessor().hit(entityTarget).entity(entityTarget.getEntity()).requireVerification().build();
                            } else if (client.screen instanceof BaseOptionsScreen) {
                                accessor = WailaClientRegistration.INSTANCE.blockAccessor().blockState(Blocks.GRASS_BLOCK.defaultBlockState()).hit(new BlockHitResult(entity.position(), Direction.UP, entity.blockPosition(), false)).build();
                            }
                        }
                        Accessor<?> originalAccessor = accessor;
                        for (JadeRayTraceCallback callback : WailaClientRegistration.INSTANCE.rayTraceCallback.callbacks()) {
                            accessor = callback.onRayTrace(target, accessor, originalAccessor);
                        }
                        ObjectDataCenter.set(accessor);
                        if (accessor != null && accessor.getHitResult() != null) {
                            Accessor.ClientHandler<Accessor<?>> handler = WailaClientRegistration.INSTANCE.getAccessorHandler(accessor.getAccessorType());
                            if (!handler.shouldDisplay(accessor)) {
                                this.tooltipRenderer = null;
                            } else {
                                if (accessor.isServerConnected()) {
                                    if (!accessor.verifyData(accessor.getServerData())) {
                                        accessor.getServerData().getAllKeys().clear();
                                    }
                                    boolean request = handler.shouldRequestData(accessor);
                                    if (ObjectDataCenter.isTimeElapsed((long) ObjectDataCenter.rateLimiter)) {
                                        ObjectDataCenter.resetTimer();
                                        if (request) {
                                            handler.requestData(accessor);
                                        }
                                    }
                                    if (request && ObjectDataCenter.getServerData() == null) {
                                        return;
                                    }
                                }
                                if (config.getDisplayMode() == IWailaConfig.DisplayMode.LITE && !ClientProxy.isShowDetailsPressed()) {
                                    Tooltip dummyTooltip = new Tooltip();
                                    handler.gatherComponents(accessor, $ -> Math.abs(WailaCommonRegistration.INSTANCE.priorities.byValue($)) > 5000 ? tooltip : dummyTooltip);
                                    if (!dummyTooltip.isEmpty()) {
                                        tooltip.sneakyDetails = true;
                                    }
                                } else {
                                    handler.gatherComponents(accessor, $ -> tooltip);
                                }
                                for (JadeTooltipCollectedCallback callback : WailaClientRegistration.INSTANCE.tooltipCollectedCallback.callbacks()) {
                                    callback.onTooltipCollected(tooltip, accessor);
                                }
                                this.tooltipRenderer = new TooltipRenderer(tooltip, true);
                                this.tooltipRenderer.setPaddingFromTheme(IThemeHelper.get().theme());
                            }
                        } else {
                            this.tooltipRenderer = null;
                        }
                    }
                } else {
                    this.tooltipRenderer = null;
                }
            }
        }
    }
}