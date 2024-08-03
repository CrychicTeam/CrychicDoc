package net.minecraft.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.FileUtil;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.advancements.AdvancementVisibilityEvaluator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;

public class PlayerAdvancements {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.Serializer()).registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).setPrettyPrinting().create();

    private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> TYPE_TOKEN = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() {
    };

    private final DataFixer dataFixer;

    private final PlayerList playerList;

    private final Path playerSavePath;

    private final Map<Advancement, AdvancementProgress> progress = new LinkedHashMap();

    private final Set<Advancement> visible = new HashSet();

    private final Set<Advancement> progressChanged = new HashSet();

    private final Set<Advancement> rootsToUpdate = new HashSet();

    private ServerPlayer player;

    @Nullable
    private Advancement lastSelectedTab;

    private boolean isFirstPacket = true;

    public PlayerAdvancements(DataFixer dataFixer0, PlayerList playerList1, ServerAdvancementManager serverAdvancementManager2, Path path3, ServerPlayer serverPlayer4) {
        this.dataFixer = dataFixer0;
        this.playerList = playerList1;
        this.playerSavePath = path3;
        this.player = serverPlayer4;
        this.load(serverAdvancementManager2);
    }

    public void setPlayer(ServerPlayer serverPlayer0) {
        this.player = serverPlayer0;
    }

    public void stopListening() {
        for (CriterionTrigger<?> $$0 : CriteriaTriggers.all()) {
            $$0.removePlayerListeners(this);
        }
    }

    public void reload(ServerAdvancementManager serverAdvancementManager0) {
        this.stopListening();
        this.progress.clear();
        this.visible.clear();
        this.rootsToUpdate.clear();
        this.progressChanged.clear();
        this.isFirstPacket = true;
        this.lastSelectedTab = null;
        this.load(serverAdvancementManager0);
    }

    private void registerListeners(ServerAdvancementManager serverAdvancementManager0) {
        for (Advancement $$1 : serverAdvancementManager0.getAllAdvancements()) {
            this.registerListeners($$1);
        }
    }

    private void checkForAutomaticTriggers(ServerAdvancementManager serverAdvancementManager0) {
        for (Advancement $$1 : serverAdvancementManager0.getAllAdvancements()) {
            if ($$1.getCriteria().isEmpty()) {
                this.award($$1, "");
                $$1.getRewards().grant(this.player);
            }
        }
    }

    private void load(ServerAdvancementManager serverAdvancementManager0) {
        if (Files.isRegularFile(this.playerSavePath, new LinkOption[0])) {
            try {
                JsonReader $$1 = new JsonReader(Files.newBufferedReader(this.playerSavePath, StandardCharsets.UTF_8));
                try {
                    $$1.setLenient(false);
                    Dynamic<JsonElement> $$2 = new Dynamic(JsonOps.INSTANCE, Streams.parse($$1));
                    int $$3 = $$2.get("DataVersion").asInt(1343);
                    $$2 = $$2.remove("DataVersion");
                    $$2 = DataFixTypes.ADVANCEMENTS.updateToCurrentVersion(this.dataFixer, $$2, $$3);
                    Map<ResourceLocation, AdvancementProgress> $$4 = (Map<ResourceLocation, AdvancementProgress>) GSON.getAdapter(TYPE_TOKEN).fromJsonTree((JsonElement) $$2.getValue());
                    if ($$4 == null) {
                        throw new JsonParseException("Found null for advancements");
                    }
                    $$4.entrySet().stream().sorted(Entry.comparingByValue()).forEach(p_265663_ -> {
                        Advancement $$2x = serverAdvancementManager0.getAdvancement((ResourceLocation) p_265663_.getKey());
                        if ($$2x == null) {
                            LOGGER.warn("Ignored advancement '{}' in progress file {} - it doesn't exist anymore?", p_265663_.getKey(), this.playerSavePath);
                        } else {
                            this.startProgress($$2x, (AdvancementProgress) p_265663_.getValue());
                            this.progressChanged.add($$2x);
                            this.markForVisibilityUpdate($$2x);
                        }
                    });
                } catch (Throwable var7) {
                    try {
                        $$1.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                    throw var7;
                }
                $$1.close();
            } catch (JsonParseException var8) {
                LOGGER.error("Couldn't parse player advancements in {}", this.playerSavePath, var8);
            } catch (IOException var9) {
                LOGGER.error("Couldn't access player advancements in {}", this.playerSavePath, var9);
            }
        }
        this.checkForAutomaticTriggers(serverAdvancementManager0);
        this.registerListeners(serverAdvancementManager0);
    }

    public void save() {
        Map<ResourceLocation, AdvancementProgress> $$0 = new LinkedHashMap();
        for (Entry<Advancement, AdvancementProgress> $$1 : this.progress.entrySet()) {
            AdvancementProgress $$2 = (AdvancementProgress) $$1.getValue();
            if ($$2.hasProgress()) {
                $$0.put(((Advancement) $$1.getKey()).getId(), $$2);
            }
        }
        JsonElement $$3 = GSON.toJsonTree($$0);
        $$3.getAsJsonObject().addProperty("DataVersion", SharedConstants.getCurrentVersion().getDataVersion().getVersion());
        try {
            FileUtil.createDirectoriesSafe(this.playerSavePath.getParent());
            Writer $$4 = Files.newBufferedWriter(this.playerSavePath, StandardCharsets.UTF_8);
            try {
                GSON.toJson($$3, $$4);
            } catch (Throwable var7) {
                if ($$4 != null) {
                    try {
                        $$4.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if ($$4 != null) {
                $$4.close();
            }
        } catch (IOException var8) {
            LOGGER.error("Couldn't save player advancements to {}", this.playerSavePath, var8);
        }
    }

    public boolean award(Advancement advancement0, String string1) {
        boolean $$2 = false;
        AdvancementProgress $$3 = this.getOrStartProgress(advancement0);
        boolean $$4 = $$3.isDone();
        if ($$3.grantProgress(string1)) {
            this.unregisterListeners(advancement0);
            this.progressChanged.add(advancement0);
            $$2 = true;
            if (!$$4 && $$3.isDone()) {
                advancement0.getRewards().grant(this.player);
                if (advancement0.getDisplay() != null && advancement0.getDisplay().shouldAnnounceChat() && this.player.m_9236_().getGameRules().getBoolean(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)) {
                    this.playerList.broadcastSystemMessage(Component.translatable("chat.type.advancement." + advancement0.getDisplay().getFrame().getName(), this.player.m_5446_(), advancement0.getChatComponent()), false);
                }
            }
        }
        if (!$$4 && $$3.isDone()) {
            this.markForVisibilityUpdate(advancement0);
        }
        return $$2;
    }

    public boolean revoke(Advancement advancement0, String string1) {
        boolean $$2 = false;
        AdvancementProgress $$3 = this.getOrStartProgress(advancement0);
        boolean $$4 = $$3.isDone();
        if ($$3.revokeProgress(string1)) {
            this.registerListeners(advancement0);
            this.progressChanged.add(advancement0);
            $$2 = true;
        }
        if ($$4 && !$$3.isDone()) {
            this.markForVisibilityUpdate(advancement0);
        }
        return $$2;
    }

    private void markForVisibilityUpdate(Advancement advancement0) {
        this.rootsToUpdate.add(advancement0.getRoot());
    }

    private void registerListeners(Advancement advancement0) {
        AdvancementProgress $$1 = this.getOrStartProgress(advancement0);
        if (!$$1.isDone()) {
            for (Entry<String, Criterion> $$2 : advancement0.getCriteria().entrySet()) {
                CriterionProgress $$3 = $$1.getCriterion((String) $$2.getKey());
                if ($$3 != null && !$$3.isDone()) {
                    CriterionTriggerInstance $$4 = ((Criterion) $$2.getValue()).getTrigger();
                    if ($$4 != null) {
                        CriterionTrigger<CriterionTriggerInstance> $$5 = CriteriaTriggers.getCriterion($$4.getCriterion());
                        if ($$5 != null) {
                            $$5.addPlayerListener(this, new CriterionTrigger.Listener<>($$4, advancement0, (String) $$2.getKey()));
                        }
                    }
                }
            }
        }
    }

    private void unregisterListeners(Advancement advancement0) {
        AdvancementProgress $$1 = this.getOrStartProgress(advancement0);
        for (Entry<String, Criterion> $$2 : advancement0.getCriteria().entrySet()) {
            CriterionProgress $$3 = $$1.getCriterion((String) $$2.getKey());
            if ($$3 != null && ($$3.isDone() || $$1.isDone())) {
                CriterionTriggerInstance $$4 = ((Criterion) $$2.getValue()).getTrigger();
                if ($$4 != null) {
                    CriterionTrigger<CriterionTriggerInstance> $$5 = CriteriaTriggers.getCriterion($$4.getCriterion());
                    if ($$5 != null) {
                        $$5.removePlayerListener(this, new CriterionTrigger.Listener<>($$4, advancement0, (String) $$2.getKey()));
                    }
                }
            }
        }
    }

    public void flushDirty(ServerPlayer serverPlayer0) {
        if (this.isFirstPacket || !this.rootsToUpdate.isEmpty() || !this.progressChanged.isEmpty()) {
            Map<ResourceLocation, AdvancementProgress> $$1 = new HashMap();
            Set<Advancement> $$2 = new HashSet();
            Set<ResourceLocation> $$3 = new HashSet();
            for (Advancement $$4 : this.rootsToUpdate) {
                this.updateTreeVisibility($$4, $$2, $$3);
            }
            this.rootsToUpdate.clear();
            for (Advancement $$5 : this.progressChanged) {
                if (this.visible.contains($$5)) {
                    $$1.put($$5.getId(), (AdvancementProgress) this.progress.get($$5));
                }
            }
            this.progressChanged.clear();
            if (!$$1.isEmpty() || !$$2.isEmpty() || !$$3.isEmpty()) {
                serverPlayer0.connection.send(new ClientboundUpdateAdvancementsPacket(this.isFirstPacket, $$2, $$3, $$1));
            }
        }
        this.isFirstPacket = false;
    }

    public void setSelectedTab(@Nullable Advancement advancement0) {
        Advancement $$1 = this.lastSelectedTab;
        if (advancement0 != null && advancement0.getParent() == null && advancement0.getDisplay() != null) {
            this.lastSelectedTab = advancement0;
        } else {
            this.lastSelectedTab = null;
        }
        if ($$1 != this.lastSelectedTab) {
            this.player.connection.send(new ClientboundSelectAdvancementsTabPacket(this.lastSelectedTab == null ? null : this.lastSelectedTab.getId()));
        }
    }

    public AdvancementProgress getOrStartProgress(Advancement advancement0) {
        AdvancementProgress $$1 = (AdvancementProgress) this.progress.get(advancement0);
        if ($$1 == null) {
            $$1 = new AdvancementProgress();
            this.startProgress(advancement0, $$1);
        }
        return $$1;
    }

    private void startProgress(Advancement advancement0, AdvancementProgress advancementProgress1) {
        advancementProgress1.update(advancement0.getCriteria(), advancement0.getRequirements());
        this.progress.put(advancement0, advancementProgress1);
    }

    private void updateTreeVisibility(Advancement advancement0, Set<Advancement> setAdvancement1, Set<ResourceLocation> setResourceLocation2) {
        AdvancementVisibilityEvaluator.evaluateVisibility(advancement0, p_265787_ -> this.getOrStartProgress(p_265787_).isDone(), (p_265247_, p_265330_) -> {
            if (p_265330_) {
                if (this.visible.add(p_265247_)) {
                    setAdvancement1.add(p_265247_);
                    if (this.progress.containsKey(p_265247_)) {
                        this.progressChanged.add(p_265247_);
                    }
                }
            } else if (this.visible.remove(p_265247_)) {
                setResourceLocation2.add(p_265247_.getId());
            }
        });
    }
}