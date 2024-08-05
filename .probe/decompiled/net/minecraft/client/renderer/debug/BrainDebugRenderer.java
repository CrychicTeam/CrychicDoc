package net.minecraft.client.renderer.debug;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import org.slf4j.Logger;

public class BrainDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final boolean SHOW_NAME_FOR_ALL = true;

    private static final boolean SHOW_PROFESSION_FOR_ALL = false;

    private static final boolean SHOW_BEHAVIORS_FOR_ALL = false;

    private static final boolean SHOW_ACTIVITIES_FOR_ALL = false;

    private static final boolean SHOW_INVENTORY_FOR_ALL = false;

    private static final boolean SHOW_GOSSIPS_FOR_ALL = false;

    private static final boolean SHOW_PATH_FOR_ALL = false;

    private static final boolean SHOW_HEALTH_FOR_ALL = false;

    private static final boolean SHOW_WANTS_GOLEM_FOR_ALL = true;

    private static final boolean SHOW_ANGER_LEVEL_FOR_ALL = false;

    private static final boolean SHOW_NAME_FOR_SELECTED = true;

    private static final boolean SHOW_PROFESSION_FOR_SELECTED = true;

    private static final boolean SHOW_BEHAVIORS_FOR_SELECTED = true;

    private static final boolean SHOW_ACTIVITIES_FOR_SELECTED = true;

    private static final boolean SHOW_MEMORIES_FOR_SELECTED = true;

    private static final boolean SHOW_INVENTORY_FOR_SELECTED = true;

    private static final boolean SHOW_GOSSIPS_FOR_SELECTED = true;

    private static final boolean SHOW_PATH_FOR_SELECTED = true;

    private static final boolean SHOW_HEALTH_FOR_SELECTED = true;

    private static final boolean SHOW_WANTS_GOLEM_FOR_SELECTED = true;

    private static final boolean SHOW_ANGER_LEVEL_FOR_SELECTED = true;

    private static final boolean SHOW_POI_INFO = true;

    private static final int MAX_RENDER_DIST_FOR_BRAIN_INFO = 30;

    private static final int MAX_RENDER_DIST_FOR_POI_INFO = 30;

    private static final int MAX_TARGETING_DIST = 8;

    private static final float TEXT_SCALE = 0.02F;

    private static final int WHITE = -1;

    private static final int YELLOW = -256;

    private static final int CYAN = -16711681;

    private static final int GREEN = -16711936;

    private static final int GRAY = -3355444;

    private static final int PINK = -98404;

    private static final int RED = -65536;

    private static final int ORANGE = -23296;

    private final Minecraft minecraft;

    private final Map<BlockPos, BrainDebugRenderer.PoiInfo> pois = Maps.newHashMap();

    private final Map<UUID, BrainDebugRenderer.BrainDump> brainDumpsPerEntity = Maps.newHashMap();

    @Nullable
    private UUID lastLookedAtUuid;

    public BrainDebugRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void clear() {
        this.pois.clear();
        this.brainDumpsPerEntity.clear();
        this.lastLookedAtUuid = null;
    }

    public void addPoi(BrainDebugRenderer.PoiInfo brainDebugRendererPoiInfo0) {
        this.pois.put(brainDebugRendererPoiInfo0.pos, brainDebugRendererPoiInfo0);
    }

    public void removePoi(BlockPos blockPos0) {
        this.pois.remove(blockPos0);
    }

    public void setFreeTicketCount(BlockPos blockPos0, int int1) {
        BrainDebugRenderer.PoiInfo $$2 = (BrainDebugRenderer.PoiInfo) this.pois.get(blockPos0);
        if ($$2 == null) {
            LOGGER.warn("Strange, setFreeTicketCount was called for an unknown POI: {}", blockPos0);
        } else {
            $$2.freeTicketCount = int1;
        }
    }

    public void addOrUpdateBrainDump(BrainDebugRenderer.BrainDump brainDebugRendererBrainDump0) {
        this.brainDumpsPerEntity.put(brainDebugRendererBrainDump0.uuid, brainDebugRendererBrainDump0);
    }

    public void removeBrainDump(int int0) {
        this.brainDumpsPerEntity.values().removeIf(p_173814_ -> p_173814_.id == int0);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        this.clearRemovedEntities();
        this.doRender(poseStack0, multiBufferSource1, double2, double3, double4);
        if (!this.minecraft.player.m_5833_()) {
            this.updateLastLookedAtUuid();
        }
    }

    private void clearRemovedEntities() {
        this.brainDumpsPerEntity.entrySet().removeIf(p_113263_ -> {
            Entity $$1 = this.minecraft.level.getEntity(((BrainDebugRenderer.BrainDump) p_113263_.getValue()).id);
            return $$1 == null || $$1.isRemoved();
        });
    }

    private void doRender(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        BlockPos $$5 = BlockPos.containing(double2, double3, double4);
        this.brainDumpsPerEntity.values().forEach(p_269714_ -> {
            if (this.isPlayerCloseEnoughToMob(p_269714_)) {
                this.renderBrainInfo(poseStack0, multiBufferSource1, p_269714_, double2, double3, double4);
            }
        });
        for (BlockPos $$6 : this.pois.keySet()) {
            if ($$5.m_123314_($$6, 30.0)) {
                highlightPoi(poseStack0, multiBufferSource1, $$6);
            }
        }
        this.pois.values().forEach(p_269718_ -> {
            if ($$5.m_123314_(p_269718_.pos, 30.0)) {
                this.renderPoiInfo(poseStack0, multiBufferSource1, p_269718_);
            }
        });
        this.getGhostPois().forEach((p_269707_, p_269708_) -> {
            if ($$5.m_123314_(p_269707_, 30.0)) {
                this.renderGhostPoi(poseStack0, multiBufferSource1, p_269707_, p_269708_);
            }
        });
    }

    private static void highlightPoi(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BlockPos blockPos2) {
        float $$3 = 0.05F;
        DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, blockPos2, 0.05F, 0.2F, 0.2F, 1.0F, 0.3F);
    }

    private void renderGhostPoi(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BlockPos blockPos2, List<String> listString3) {
        float $$4 = 0.05F;
        DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, blockPos2, 0.05F, 0.2F, 0.2F, 1.0F, 0.3F);
        renderTextOverPos(poseStack0, multiBufferSource1, listString3 + "", blockPos2, 0, -256);
        renderTextOverPos(poseStack0, multiBufferSource1, "Ghost POI", blockPos2, 1, -65536);
    }

    private void renderPoiInfo(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BrainDebugRenderer.PoiInfo brainDebugRendererPoiInfo2) {
        int $$3 = 0;
        Set<String> $$4 = this.getTicketHolderNames(brainDebugRendererPoiInfo2);
        if ($$4.size() < 4) {
            renderTextOverPoi(poseStack0, multiBufferSource1, "Owners: " + $$4, brainDebugRendererPoiInfo2, $$3, -256);
        } else {
            renderTextOverPoi(poseStack0, multiBufferSource1, $$4.size() + " ticket holders", brainDebugRendererPoiInfo2, $$3, -256);
        }
        $$3++;
        Set<String> $$5 = this.getPotentialTicketHolderNames(brainDebugRendererPoiInfo2);
        if ($$5.size() < 4) {
            renderTextOverPoi(poseStack0, multiBufferSource1, "Candidates: " + $$5, brainDebugRendererPoiInfo2, $$3, -23296);
        } else {
            renderTextOverPoi(poseStack0, multiBufferSource1, $$5.size() + " potential owners", brainDebugRendererPoiInfo2, $$3, -23296);
        }
        renderTextOverPoi(poseStack0, multiBufferSource1, "Free tickets: " + brainDebugRendererPoiInfo2.freeTicketCount, brainDebugRendererPoiInfo2, ++$$3, -256);
        renderTextOverPoi(poseStack0, multiBufferSource1, brainDebugRendererPoiInfo2.type, brainDebugRendererPoiInfo2, ++$$3, -1);
    }

    private void renderPath(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BrainDebugRenderer.BrainDump brainDebugRendererBrainDump2, double double3, double double4, double double5) {
        if (brainDebugRendererBrainDump2.path != null) {
            PathfindingRenderer.renderPath(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.path, 0.5F, false, false, double3, double4, double5);
        }
    }

    private void renderBrainInfo(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BrainDebugRenderer.BrainDump brainDebugRendererBrainDump2, double double3, double double4, double double5) {
        boolean $$6 = this.isMobSelected(brainDebugRendererBrainDump2);
        int $$7 = 0;
        renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, brainDebugRendererBrainDump2.name, -1, 0.03F);
        $$7++;
        if ($$6) {
            renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, brainDebugRendererBrainDump2.profession + " " + brainDebugRendererBrainDump2.xp + " xp", -1, 0.02F);
            $$7++;
        }
        if ($$6) {
            int $$8 = brainDebugRendererBrainDump2.health < brainDebugRendererBrainDump2.maxHealth ? -23296 : -1;
            renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, "health: " + String.format(Locale.ROOT, "%.1f", brainDebugRendererBrainDump2.health) + " / " + String.format(Locale.ROOT, "%.1f", brainDebugRendererBrainDump2.maxHealth), $$8, 0.02F);
            $$7++;
        }
        if ($$6 && !brainDebugRendererBrainDump2.inventory.equals("")) {
            renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, brainDebugRendererBrainDump2.inventory, -98404, 0.02F);
            $$7++;
        }
        if ($$6) {
            for (String $$9 : brainDebugRendererBrainDump2.behaviors) {
                renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, $$9, -16711681, 0.02F);
                $$7++;
            }
        }
        if ($$6) {
            for (String $$10 : brainDebugRendererBrainDump2.activities) {
                renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, $$10, -16711936, 0.02F);
                $$7++;
            }
        }
        if (brainDebugRendererBrainDump2.wantsGolem) {
            renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, "Wants Golem", -23296, 0.02F);
            $$7++;
        }
        if ($$6 && brainDebugRendererBrainDump2.angerLevel != -1) {
            renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, "Anger Level: " + brainDebugRendererBrainDump2.angerLevel, -98404, 0.02F);
            $$7++;
        }
        if ($$6) {
            for (String $$11 : brainDebugRendererBrainDump2.gossips) {
                if ($$11.startsWith(brainDebugRendererBrainDump2.name)) {
                    renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, $$11, -1, 0.02F);
                } else {
                    renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, $$11, -23296, 0.02F);
                }
                $$7++;
            }
        }
        if ($$6) {
            for (String $$12 : Lists.reverse(brainDebugRendererBrainDump2.memories)) {
                renderTextOverMob(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2.pos, $$7, $$12, -3355444, 0.02F);
                $$7++;
            }
        }
        if ($$6) {
            this.renderPath(poseStack0, multiBufferSource1, brainDebugRendererBrainDump2, double3, double4, double5);
        }
    }

    private static void renderTextOverPoi(PoseStack poseStack0, MultiBufferSource multiBufferSource1, String string2, BrainDebugRenderer.PoiInfo brainDebugRendererPoiInfo3, int int4, int int5) {
        renderTextOverPos(poseStack0, multiBufferSource1, string2, brainDebugRendererPoiInfo3.pos, int4, int5);
    }

    private static void renderTextOverPos(PoseStack poseStack0, MultiBufferSource multiBufferSource1, String string2, BlockPos blockPos3, int int4, int int5) {
        double $$6 = 1.3;
        double $$7 = 0.2;
        double $$8 = (double) blockPos3.m_123341_() + 0.5;
        double $$9 = (double) blockPos3.m_123342_() + 1.3 + (double) int4 * 0.2;
        double $$10 = (double) blockPos3.m_123343_() + 0.5;
        DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, string2, $$8, $$9, $$10, int5, 0.02F, true, 0.0F, true);
    }

    private static void renderTextOverMob(PoseStack poseStack0, MultiBufferSource multiBufferSource1, Position position2, int int3, String string4, int int5, float float6) {
        double $$7 = 2.4;
        double $$8 = 0.25;
        BlockPos $$9 = BlockPos.containing(position2);
        double $$10 = (double) $$9.m_123341_() + 0.5;
        double $$11 = position2.y() + 2.4 + (double) int3 * 0.25;
        double $$12 = (double) $$9.m_123343_() + 0.5;
        float $$13 = 0.5F;
        DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, string4, $$10, $$11, $$12, int5, float6, false, 0.5F, true);
    }

    private Set<String> getTicketHolderNames(BrainDebugRenderer.PoiInfo brainDebugRendererPoiInfo0) {
        return (Set<String>) this.getTicketHolders(brainDebugRendererPoiInfo0.pos).stream().map(DebugEntityNameGenerator::m_133668_).collect(Collectors.toSet());
    }

    private Set<String> getPotentialTicketHolderNames(BrainDebugRenderer.PoiInfo brainDebugRendererPoiInfo0) {
        return (Set<String>) this.getPotentialTicketHolders(brainDebugRendererPoiInfo0.pos).stream().map(DebugEntityNameGenerator::m_133668_).collect(Collectors.toSet());
    }

    private boolean isMobSelected(BrainDebugRenderer.BrainDump brainDebugRendererBrainDump0) {
        return Objects.equals(this.lastLookedAtUuid, brainDebugRendererBrainDump0.uuid);
    }

    private boolean isPlayerCloseEnoughToMob(BrainDebugRenderer.BrainDump brainDebugRendererBrainDump0) {
        Player $$1 = this.minecraft.player;
        BlockPos $$2 = BlockPos.containing($$1.m_20185_(), brainDebugRendererBrainDump0.pos.y(), $$1.m_20189_());
        BlockPos $$3 = BlockPos.containing(brainDebugRendererBrainDump0.pos);
        return $$2.m_123314_($$3, 30.0);
    }

    private Collection<UUID> getTicketHolders(BlockPos blockPos0) {
        return (Collection<UUID>) this.brainDumpsPerEntity.values().stream().filter(p_113278_ -> p_113278_.hasPoi(blockPos0)).map(BrainDebugRenderer.BrainDump::m_113322_).collect(Collectors.toSet());
    }

    private Collection<UUID> getPotentialTicketHolders(BlockPos blockPos0) {
        return (Collection<UUID>) this.brainDumpsPerEntity.values().stream().filter(p_113235_ -> p_113235_.hasPotentialPoi(blockPos0)).map(BrainDebugRenderer.BrainDump::m_113322_).collect(Collectors.toSet());
    }

    private Map<BlockPos, List<String>> getGhostPois() {
        Map<BlockPos, List<String>> $$0 = Maps.newHashMap();
        for (BrainDebugRenderer.BrainDump $$1 : this.brainDumpsPerEntity.values()) {
            for (BlockPos $$2 : Iterables.concat($$1.pois, $$1.potentialPois)) {
                if (!this.pois.containsKey($$2)) {
                    ((List) $$0.computeIfAbsent($$2, p_113292_ -> Lists.newArrayList())).add($$1.name);
                }
            }
        }
        return $$0;
    }

    private void updateLastLookedAtUuid() {
        DebugRenderer.getTargetedEntity(this.minecraft.getCameraEntity(), 8).ifPresent(p_113212_ -> this.lastLookedAtUuid = p_113212_.getUUID());
    }

    public static class BrainDump {

        public final UUID uuid;

        public final int id;

        public final String name;

        public final String profession;

        public final int xp;

        public final float health;

        public final float maxHealth;

        public final Position pos;

        public final String inventory;

        public final Path path;

        public final boolean wantsGolem;

        public final int angerLevel;

        public final List<String> activities = Lists.newArrayList();

        public final List<String> behaviors = Lists.newArrayList();

        public final List<String> memories = Lists.newArrayList();

        public final List<String> gossips = Lists.newArrayList();

        public final Set<BlockPos> pois = Sets.newHashSet();

        public final Set<BlockPos> potentialPois = Sets.newHashSet();

        public BrainDump(UUID uUID0, int int1, String string2, String string3, int int4, float float5, float float6, Position position7, String string8, @Nullable Path path9, boolean boolean10, int int11) {
            this.uuid = uUID0;
            this.id = int1;
            this.name = string2;
            this.profession = string3;
            this.xp = int4;
            this.health = float5;
            this.maxHealth = float6;
            this.pos = position7;
            this.inventory = string8;
            this.path = path9;
            this.wantsGolem = boolean10;
            this.angerLevel = int11;
        }

        boolean hasPoi(BlockPos blockPos0) {
            return this.pois.stream().anyMatch(blockPos0::equals);
        }

        boolean hasPotentialPoi(BlockPos blockPos0) {
            return this.potentialPois.contains(blockPos0);
        }

        public UUID getUuid() {
            return this.uuid;
        }
    }

    public static class PoiInfo {

        public final BlockPos pos;

        public String type;

        public int freeTicketCount;

        public PoiInfo(BlockPos blockPos0, String string1, int int2) {
            this.pos = blockPos0;
            this.type = string1;
            this.freeTicketCount = int2;
        }
    }
}