package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.TreeNodePosition;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.LootDataManager;
import org.slf4j.Logger;

public class ServerAdvancementManager extends SimpleJsonResourceReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().create();

    private AdvancementList advancements = new AdvancementList();

    private final LootDataManager lootData;

    public ServerAdvancementManager(LootDataManager lootDataManager0) {
        super(GSON, "advancements");
        this.lootData = lootDataManager0;
    }

    protected void apply(Map<ResourceLocation, JsonElement> mapResourceLocationJsonElement0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2) {
        Map<ResourceLocation, Advancement.Builder> $$3 = Maps.newHashMap();
        mapResourceLocationJsonElement0.forEach((p_278903_, p_278904_) -> {
            try {
                JsonObject $$3x = GsonHelper.convertToJsonObject(p_278904_, "advancement");
                Advancement.Builder $$4x = Advancement.Builder.fromJson($$3x, new DeserializationContext(p_278903_, this.lootData));
                $$3.put(p_278903_, $$4x);
            } catch (Exception var6) {
                LOGGER.error("Parsing error loading custom advancement {}: {}", p_278903_, var6.getMessage());
            }
        });
        AdvancementList $$4 = new AdvancementList();
        $$4.add($$3);
        for (Advancement $$5 : $$4.getRoots()) {
            if ($$5.getDisplay() != null) {
                TreeNodePosition.run($$5);
            }
        }
        this.advancements = $$4;
    }

    @Nullable
    public Advancement getAdvancement(ResourceLocation resourceLocation0) {
        return this.advancements.get(resourceLocation0);
    }

    public Collection<Advancement> getAllAdvancements() {
        return this.advancements.getAllAdvancements();
    }
}