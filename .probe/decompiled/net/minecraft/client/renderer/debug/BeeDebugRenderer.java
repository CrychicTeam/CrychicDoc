package net.minecraft.client.renderer.debug;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

public class BeeDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final boolean SHOW_GOAL_FOR_ALL_BEES = true;

    private static final boolean SHOW_NAME_FOR_ALL_BEES = true;

    private static final boolean SHOW_HIVE_FOR_ALL_BEES = true;

    private static final boolean SHOW_FLOWER_POS_FOR_ALL_BEES = true;

    private static final boolean SHOW_TRAVEL_TICKS_FOR_ALL_BEES = true;

    private static final boolean SHOW_PATH_FOR_ALL_BEES = false;

    private static final boolean SHOW_GOAL_FOR_SELECTED_BEE = true;

    private static final boolean SHOW_NAME_FOR_SELECTED_BEE = true;

    private static final boolean SHOW_HIVE_FOR_SELECTED_BEE = true;

    private static final boolean SHOW_FLOWER_POS_FOR_SELECTED_BEE = true;

    private static final boolean SHOW_TRAVEL_TICKS_FOR_SELECTED_BEE = true;

    private static final boolean SHOW_PATH_FOR_SELECTED_BEE = true;

    private static final boolean SHOW_HIVE_MEMBERS = true;

    private static final boolean SHOW_BLACKLISTS = true;

    private static final int MAX_RENDER_DIST_FOR_HIVE_OVERLAY = 30;

    private static final int MAX_RENDER_DIST_FOR_BEE_OVERLAY = 30;

    private static final int MAX_TARGETING_DIST = 8;

    private static final int HIVE_TIMEOUT = 20;

    private static final float TEXT_SCALE = 0.02F;

    private static final int WHITE = -1;

    private static final int YELLOW = -256;

    private static final int ORANGE = -23296;

    private static final int GREEN = -16711936;

    private static final int GRAY = -3355444;

    private static final int PINK = -98404;

    private static final int RED = -65536;

    private final Minecraft minecraft;

    private final Map<BlockPos, BeeDebugRenderer.HiveInfo> hives = Maps.newHashMap();

    private final Map<UUID, BeeDebugRenderer.BeeInfo> beeInfosPerEntity = Maps.newHashMap();

    private UUID lastLookedAtUuid;

    public BeeDebugRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void clear() {
        this.hives.clear();
        this.beeInfosPerEntity.clear();
        this.lastLookedAtUuid = null;
    }

    public void addOrUpdateHiveInfo(BeeDebugRenderer.HiveInfo beeDebugRendererHiveInfo0) {
        this.hives.put(beeDebugRendererHiveInfo0.pos, beeDebugRendererHiveInfo0);
    }

    public void addOrUpdateBeeInfo(BeeDebugRenderer.BeeInfo beeDebugRendererBeeInfo0) {
        this.beeInfosPerEntity.put(beeDebugRendererBeeInfo0.uuid, beeDebugRendererBeeInfo0);
    }

    public void removeBeeInfo(int int0) {
        this.beeInfosPerEntity.values().removeIf(p_173767_ -> p_173767_.id == int0);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        this.clearRemovedHives();
        this.clearRemovedBees();
        this.doRender(poseStack0, multiBufferSource1);
        if (!this.minecraft.player.m_5833_()) {
            this.updateLastLookedAtUuid();
        }
    }

    private void clearRemovedBees() {
        this.beeInfosPerEntity.entrySet().removeIf(p_113132_ -> this.minecraft.level.getEntity(((BeeDebugRenderer.BeeInfo) p_113132_.getValue()).id) == null);
    }

    private void clearRemovedHives() {
        long $$0 = this.minecraft.level.m_46467_() - 20L;
        this.hives.entrySet().removeIf(p_113057_ -> ((BeeDebugRenderer.HiveInfo) p_113057_.getValue()).lastSeen < $$0);
    }

    private void doRender(PoseStack poseStack0, MultiBufferSource multiBufferSource1) {
        BlockPos $$2 = this.getCamera().getBlockPosition();
        this.beeInfosPerEntity.values().forEach(p_269703_ -> {
            if (this.isPlayerCloseEnoughToMob(p_269703_)) {
                this.renderBeeInfo(poseStack0, multiBufferSource1, p_269703_);
            }
        });
        this.renderFlowerInfos(poseStack0, multiBufferSource1);
        for (BlockPos $$3 : this.hives.keySet()) {
            if ($$2.m_123314_($$3, 30.0)) {
                highlightHive(poseStack0, multiBufferSource1, $$3);
            }
        }
        Map<BlockPos, Set<UUID>> $$4 = this.createHiveBlacklistMap();
        this.hives.values().forEach(p_269692_ -> {
            if ($$2.m_123314_(p_269692_.pos, 30.0)) {
                Set<UUID> $$5 = (Set<UUID>) $$4.get(p_269692_.pos);
                this.renderHiveInfo(poseStack0, multiBufferSource1, p_269692_, (Collection<UUID>) ($$5 == null ? Sets.newHashSet() : $$5));
            }
        });
        this.getGhostHives().forEach((p_269699_, p_269700_) -> {
            if ($$2.m_123314_(p_269699_, 30.0)) {
                this.renderGhostHive(poseStack0, multiBufferSource1, p_269699_, p_269700_);
            }
        });
    }

    private Map<BlockPos, Set<UUID>> createHiveBlacklistMap() {
        Map<BlockPos, Set<UUID>> $$0 = Maps.newHashMap();
        this.beeInfosPerEntity.values().forEach(p_113135_ -> p_113135_.blacklistedHives.forEach(p_173771_ -> ((Set) $$0.computeIfAbsent(p_173771_, p_173777_ -> Sets.newHashSet())).add(p_113135_.getUuid())));
        return $$0;
    }

    private void renderFlowerInfos(PoseStack poseStack0, MultiBufferSource multiBufferSource1) {
        Map<BlockPos, Set<UUID>> $$2 = Maps.newHashMap();
        this.beeInfosPerEntity.values().stream().filter(BeeDebugRenderer.BeeInfo::m_113178_).forEach(p_113121_ -> ((Set) $$2.computeIfAbsent(p_113121_.flowerPos, p_173775_ -> Sets.newHashSet())).add(p_113121_.getUuid()));
        $$2.entrySet().forEach(p_269695_ -> {
            BlockPos $$3 = (BlockPos) p_269695_.getKey();
            Set<UUID> $$4 = (Set<UUID>) p_269695_.getValue();
            Set<String> $$5 = (Set<String>) $$4.stream().map(DebugEntityNameGenerator::m_133668_).collect(Collectors.toSet());
            int $$6 = 1;
            renderTextOverPos(poseStack0, multiBufferSource1, $$5.toString(), $$3, $$6++, -256);
            renderTextOverPos(poseStack0, multiBufferSource1, "Flower", $$3, $$6++, -1);
            float $$7 = 0.05F;
            DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, $$3, 0.05F, 0.8F, 0.8F, 0.0F, 0.3F);
        });
    }

    private static String getBeeUuidsAsString(Collection<UUID> collectionUUID0) {
        if (collectionUUID0.isEmpty()) {
            return "-";
        } else {
            return collectionUUID0.size() > 3 ? collectionUUID0.size() + " bees" : ((Set) collectionUUID0.stream().map(DebugEntityNameGenerator::m_133668_).collect(Collectors.toSet())).toString();
        }
    }

    private static void highlightHive(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BlockPos blockPos2) {
        float $$3 = 0.05F;
        DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, blockPos2, 0.05F, 0.2F, 0.2F, 1.0F, 0.3F);
    }

    private void renderGhostHive(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BlockPos blockPos2, List<String> listString3) {
        float $$4 = 0.05F;
        DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, blockPos2, 0.05F, 0.2F, 0.2F, 1.0F, 0.3F);
        renderTextOverPos(poseStack0, multiBufferSource1, listString3 + "", blockPos2, 0, -256);
        renderTextOverPos(poseStack0, multiBufferSource1, "Ghost Hive", blockPos2, 1, -65536);
    }

    private void renderHiveInfo(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BeeDebugRenderer.HiveInfo beeDebugRendererHiveInfo2, Collection<UUID> collectionUUID3) {
        int $$4 = 0;
        if (!collectionUUID3.isEmpty()) {
            renderTextOverHive(poseStack0, multiBufferSource1, "Blacklisted by " + getBeeUuidsAsString(collectionUUID3), beeDebugRendererHiveInfo2, $$4++, -65536);
        }
        renderTextOverHive(poseStack0, multiBufferSource1, "Out: " + getBeeUuidsAsString(this.getHiveMembers(beeDebugRendererHiveInfo2.pos)), beeDebugRendererHiveInfo2, $$4++, -3355444);
        if (beeDebugRendererHiveInfo2.occupantCount == 0) {
            renderTextOverHive(poseStack0, multiBufferSource1, "In: -", beeDebugRendererHiveInfo2, $$4++, -256);
        } else if (beeDebugRendererHiveInfo2.occupantCount == 1) {
            renderTextOverHive(poseStack0, multiBufferSource1, "In: 1 bee", beeDebugRendererHiveInfo2, $$4++, -256);
        } else {
            renderTextOverHive(poseStack0, multiBufferSource1, "In: " + beeDebugRendererHiveInfo2.occupantCount + " bees", beeDebugRendererHiveInfo2, $$4++, -256);
        }
        renderTextOverHive(poseStack0, multiBufferSource1, "Honey: " + beeDebugRendererHiveInfo2.honeyLevel, beeDebugRendererHiveInfo2, $$4++, -23296);
        renderTextOverHive(poseStack0, multiBufferSource1, beeDebugRendererHiveInfo2.hiveType + (beeDebugRendererHiveInfo2.sedated ? " (sedated)" : ""), beeDebugRendererHiveInfo2, $$4++, -1);
    }

    private void renderPath(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BeeDebugRenderer.BeeInfo beeDebugRendererBeeInfo2) {
        if (beeDebugRendererBeeInfo2.path != null) {
            PathfindingRenderer.renderPath(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2.path, 0.5F, false, false, this.getCamera().getPosition().x(), this.getCamera().getPosition().y(), this.getCamera().getPosition().z());
        }
    }

    private void renderBeeInfo(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BeeDebugRenderer.BeeInfo beeDebugRendererBeeInfo2) {
        boolean $$3 = this.isBeeSelected(beeDebugRendererBeeInfo2);
        int $$4 = 0;
        renderTextOverMob(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2.pos, $$4++, beeDebugRendererBeeInfo2.toString(), -1, 0.03F);
        if (beeDebugRendererBeeInfo2.hivePos == null) {
            renderTextOverMob(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2.pos, $$4++, "No hive", -98404, 0.02F);
        } else {
            renderTextOverMob(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2.pos, $$4++, "Hive: " + this.getPosDescription(beeDebugRendererBeeInfo2, beeDebugRendererBeeInfo2.hivePos), -256, 0.02F);
        }
        if (beeDebugRendererBeeInfo2.flowerPos == null) {
            renderTextOverMob(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2.pos, $$4++, "No flower", -98404, 0.02F);
        } else {
            renderTextOverMob(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2.pos, $$4++, "Flower: " + this.getPosDescription(beeDebugRendererBeeInfo2, beeDebugRendererBeeInfo2.flowerPos), -256, 0.02F);
        }
        for (String $$5 : beeDebugRendererBeeInfo2.goals) {
            renderTextOverMob(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2.pos, $$4++, $$5, -16711936, 0.02F);
        }
        if ($$3) {
            this.renderPath(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2);
        }
        if (beeDebugRendererBeeInfo2.travelTicks > 0) {
            int $$6 = beeDebugRendererBeeInfo2.travelTicks < 600 ? -3355444 : -23296;
            renderTextOverMob(poseStack0, multiBufferSource1, beeDebugRendererBeeInfo2.pos, $$4++, "Travelling: " + beeDebugRendererBeeInfo2.travelTicks + " ticks", $$6, 0.02F);
        }
    }

    private static void renderTextOverHive(PoseStack poseStack0, MultiBufferSource multiBufferSource1, String string2, BeeDebugRenderer.HiveInfo beeDebugRendererHiveInfo3, int int4, int int5) {
        BlockPos $$6 = beeDebugRendererHiveInfo3.pos;
        renderTextOverPos(poseStack0, multiBufferSource1, string2, $$6, int4, int5);
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

    private Camera getCamera() {
        return this.minecraft.gameRenderer.getMainCamera();
    }

    private Set<String> getHiveMemberNames(BeeDebugRenderer.HiveInfo beeDebugRendererHiveInfo0) {
        return (Set<String>) this.getHiveMembers(beeDebugRendererHiveInfo0.pos).stream().map(DebugEntityNameGenerator::m_133668_).collect(Collectors.toSet());
    }

    private String getPosDescription(BeeDebugRenderer.BeeInfo beeDebugRendererBeeInfo0, BlockPos blockPos1) {
        double $$2 = Math.sqrt(blockPos1.m_203193_(beeDebugRendererBeeInfo0.pos));
        double $$3 = (double) Math.round($$2 * 10.0) / 10.0;
        return blockPos1.m_123344_() + " (dist " + $$3 + ")";
    }

    private boolean isBeeSelected(BeeDebugRenderer.BeeInfo beeDebugRendererBeeInfo0) {
        return Objects.equals(this.lastLookedAtUuid, beeDebugRendererBeeInfo0.uuid);
    }

    private boolean isPlayerCloseEnoughToMob(BeeDebugRenderer.BeeInfo beeDebugRendererBeeInfo0) {
        Player $$1 = this.minecraft.player;
        BlockPos $$2 = BlockPos.containing($$1.m_20185_(), beeDebugRendererBeeInfo0.pos.y(), $$1.m_20189_());
        BlockPos $$3 = BlockPos.containing(beeDebugRendererBeeInfo0.pos);
        return $$2.m_123314_($$3, 30.0);
    }

    private Collection<UUID> getHiveMembers(BlockPos blockPos0) {
        return (Collection<UUID>) this.beeInfosPerEntity.values().stream().filter(p_113087_ -> p_113087_.hasHive(blockPos0)).map(BeeDebugRenderer.BeeInfo::m_113174_).collect(Collectors.toSet());
    }

    private Map<BlockPos, List<String>> getGhostHives() {
        Map<BlockPos, List<String>> $$0 = Maps.newHashMap();
        for (BeeDebugRenderer.BeeInfo $$1 : this.beeInfosPerEntity.values()) {
            if ($$1.hivePos != null && !this.hives.containsKey($$1.hivePos)) {
                ((List) $$0.computeIfAbsent($$1.hivePos, p_113140_ -> Lists.newArrayList())).add($$1.getName());
            }
        }
        return $$0;
    }

    private void updateLastLookedAtUuid() {
        DebugRenderer.getTargetedEntity(this.minecraft.getCameraEntity(), 8).ifPresent(p_113059_ -> this.lastLookedAtUuid = p_113059_.getUUID());
    }

    public static class BeeInfo {

        public final UUID uuid;

        public final int id;

        public final Position pos;

        @Nullable
        public final Path path;

        @Nullable
        public final BlockPos hivePos;

        @Nullable
        public final BlockPos flowerPos;

        public final int travelTicks;

        public final List<String> goals = Lists.newArrayList();

        public final Set<BlockPos> blacklistedHives = Sets.newHashSet();

        public BeeInfo(UUID uUID0, int int1, Position position2, @Nullable Path path3, @Nullable BlockPos blockPos4, @Nullable BlockPos blockPos5, int int6) {
            this.uuid = uUID0;
            this.id = int1;
            this.pos = position2;
            this.path = path3;
            this.hivePos = blockPos4;
            this.flowerPos = blockPos5;
            this.travelTicks = int6;
        }

        public boolean hasHive(BlockPos blockPos0) {
            return this.hivePos != null && this.hivePos.equals(blockPos0);
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public String getName() {
            return DebugEntityNameGenerator.getEntityName(this.uuid);
        }

        public String toString() {
            return this.getName();
        }

        public boolean hasFlower() {
            return this.flowerPos != null;
        }
    }

    public static class HiveInfo {

        public final BlockPos pos;

        public final String hiveType;

        public final int occupantCount;

        public final int honeyLevel;

        public final boolean sedated;

        public final long lastSeen;

        public HiveInfo(BlockPos blockPos0, String string1, int int2, int int3, boolean boolean4, long long5) {
            this.pos = blockPos0;
            this.hiveType = string1;
            this.occupantCount = int2;
            this.honeyLevel = int3;
            this.sedated = boolean4;
            this.lastSeen = long5;
        }
    }
}