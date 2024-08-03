package snownee.loquat.client;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.math.LongMath;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import snownee.loquat.AreaTypes;
import snownee.loquat.core.RestrictInstance;
import snownee.loquat.core.area.AABBArea;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.select.PosSelection;
import snownee.loquat.core.select.SelectionManager;
import snownee.loquat.util.Color;
import snownee.loquat.util.RenderUtil;

public class LoquatClient {

    private static final LoquatClient INSTANCE = new LoquatClient();

    public final Map<UUID, LoquatClient.RenderDebugData> normalOutlines = Maps.newConcurrentMap();

    public final Map<UUID, LoquatClient.RenderDebugData> highlightOutlines = Maps.newConcurrentMap();

    public final Map<Area.Type<?>, BiConsumer<LoquatClient.RenderDebugContext, LoquatClient.RenderDebugData>> renderers = Maps.newHashMap();

    public final RestrictInstance restrictInstance = new RestrictInstance();

    private long lastNotifyRestrictionTime = Long.MIN_VALUE;

    private final List<String> recentZoneNames = Lists.newArrayList();

    private LoquatClient() {
        this.renderers.put(AreaTypes.BOX, (BiConsumer) (ctx, data) -> {
            AABB aabb = ((AABBArea) data.area).getAabb().inflate(0.01).move(ctx.pos);
            Color color = data.type.color;
            float alpha = 1.0F;
            if (ctx.time() + 10L > data.expire) {
                alpha *= (float) (data.expire - ctx.time()) / 10.0F;
            }
            RenderUtil.renderLineBox(ctx.poseStack, aabb, (float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            DebugRenderer.renderFilledBox(ctx.poseStack, ctx.bufferSource, aabb, (float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, alpha * 0.2F);
        });
    }

    public static LoquatClient get() {
        return INSTANCE;
    }

    public void suggestAddZone(Consumer<String> builder) {
        this.recentZoneNames.forEach(builder);
    }

    public void newZoneAdded(String newZone) {
        this.recentZoneNames.remove(newZone);
        this.recentZoneNames.add(0, newZone);
        if (this.recentZoneNames.size() > 10) {
            this.recentZoneNames.remove(this.recentZoneNames.size() - 1);
        }
    }

    public void suggestRemoveZone(Consumer<String> builder) {
        List<UUID> selectedAreas = SelectionManager.of(ClientHooks.getPlayer()).getSelectedAreas();
        if (!selectedAreas.isEmpty()) {
            selectedAreas.stream().map(this.normalOutlines::get).filter(data -> data != null && data.type == LoquatClient.DebugAreaType.SELECTED).flatMap(data -> data.area.getZones().keySet().stream()).distinct().forEach(builder);
        }
    }

    public void notifyRestriction(RestrictInstance.RestrictBehavior behavior) {
        long millis = Util.getMillis();
        if (LongMath.saturatedSubtract(millis, this.lastNotifyRestrictionTime) >= 5000L) {
            this.lastNotifyRestrictionTime = millis;
            Minecraft.getInstance().getChatListener().handleSystemMessage(behavior.getNotificationMessage(), true);
        }
    }

    public void render(PoseStack matrixStack, MultiBufferSource.BufferSource bufferSource, Vec3 pos) {
        ClientLevel level = Minecraft.getInstance().level;
        LocalPlayer player = Minecraft.getInstance().player;
        if (level != null && player != null) {
            List<PosSelection> selections = SelectionManager.of(player).getSelections();
            Map<UUID, LoquatClient.RenderDebugData> outlines = this.highlightOutlines.isEmpty() ? this.normalOutlines : this.highlightOutlines;
            if (!outlines.isEmpty() || !selections.isEmpty()) {
                long time = level.m_46467_();
                LoquatClient.RenderDebugContext context = new LoquatClient.RenderDebugContext(matrixStack, bufferSource, time, pos);
                if (!outlines.isEmpty()) {
                    this.renderAreas(context, level, player, outlines);
                }
                if (!selections.isEmpty()) {
                    this.renderSelections(context, level, selections);
                }
            }
        }
    }

    private void renderAreas(LoquatClient.RenderDebugContext context, ClientLevel level, LocalPlayer player, Map<UUID, LoquatClient.RenderDebugData> outlines) {
        outlines.values().removeIf(datax -> context.time >= datax.expire);
        SelectionManager selectionManager = SelectionManager.of(player);
        for (LoquatClient.RenderDebugData data : outlines.values()) {
            if (!(data.area.distanceToSqr(context.pos.reverse()) > 16384.0)) {
                Vec3 center = data.area.getCenter();
                if (player.isShiftKeyDown() && !data.area.getTags().isEmpty()) {
                    String tags = Joiner.on(", ").join(data.area.getTags());
                    DebugRenderer.renderFloatingText(context.poseStack, context.bufferSource, tags, center.x, center.y, center.z, 0, 0.045F, true, 0.0F, true);
                }
                if (data.type != LoquatClient.DebugAreaType.HIGHLIGHT) {
                    data.type = selectionManager.isSelected(data.area) ? LoquatClient.DebugAreaType.SELECTED : LoquatClient.DebugAreaType.NORMAL;
                }
                if (data.type == LoquatClient.DebugAreaType.SELECTED) {
                    data.area.getZones().forEach((name, zone) -> {
                        float alpha = 1.0F;
                        if (context.time() + 10L > data.expire) {
                            alpha *= (float) (data.expire - context.time()) / 10.0F;
                        }
                        for (AABB aabb : zone.aabbs()) {
                            Vec3 centerx = aabb.getCenter();
                            if (aabb.getYsize() > 1.0) {
                                aabb = aabb.inflate(0.02);
                            } else {
                                aabb = aabb.deflate(0.2, 0.5, 0.2).move(0.0, -0.48, 0.0);
                            }
                            RenderUtil.renderLineBox(context.poseStack, aabb.move(context.pos), 1.0F, 0.6F, 0.0F, alpha);
                            DebugRenderer.renderFloatingText(context.poseStack, context.bufferSource, name, centerx.x, centerx.y, centerx.z, 0, 0.045F);
                        }
                    });
                }
                ((BiConsumer) Objects.requireNonNull((BiConsumer) this.renderers.get(data.area.getType()))).accept(context, data);
            }
        }
    }

    private void renderSelections(LoquatClient.RenderDebugContext context, ClientLevel level, List<PosSelection> selections) {
        for (PosSelection selection : selections) {
            AABB aabb = selection.toAABB().inflate(0.01);
            RenderUtil.renderLineBox(context.poseStack, aabb.move(context.pos), 0.4F, 0.4F, 1.0F, 1.0F);
        }
    }

    public void clearDebugAreas() {
        this.normalOutlines.clear();
    }

    public static enum DebugAreaType {

        NORMAL(Color.rgb(255, 255, 255)), HIGHLIGHT(Color.rgb(255, 255, 255)), SELECTED(Color.rgb(180, 255, 180));

        private final Color color;

        private DebugAreaType(Color color) {
            this.color = color;
        }
    }

    public static record RenderDebugContext(PoseStack poseStack, MultiBufferSource bufferSource, long time, Vec3 pos) {
    }

    public static class RenderDebugData {

        public final Area area;

        public LoquatClient.DebugAreaType type;

        public long expire;

        public RenderDebugData(Area area, LoquatClient.DebugAreaType type, long expire) {
            this.area = area;
            this.type = type;
            this.expire = expire;
        }
    }
}