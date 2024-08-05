package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class AdvancementRewards {

    public static final AdvancementRewards EMPTY = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], CommandFunction.CacheableFunction.NONE);

    private final int experience;

    private final ResourceLocation[] loot;

    private final ResourceLocation[] recipes;

    private final CommandFunction.CacheableFunction function;

    public AdvancementRewards(int int0, ResourceLocation[] resourceLocation1, ResourceLocation[] resourceLocation2, CommandFunction.CacheableFunction commandFunctionCacheableFunction3) {
        this.experience = int0;
        this.loot = resourceLocation1;
        this.recipes = resourceLocation2;
        this.function = commandFunctionCacheableFunction3;
    }

    public ResourceLocation[] getRecipes() {
        return this.recipes;
    }

    public void grant(ServerPlayer serverPlayer0) {
        serverPlayer0.giveExperiencePoints(this.experience);
        LootParams $$1 = new LootParams.Builder(serverPlayer0.serverLevel()).withParameter(LootContextParams.THIS_ENTITY, serverPlayer0).withParameter(LootContextParams.ORIGIN, serverPlayer0.m_20182_()).create(LootContextParamSets.ADVANCEMENT_REWARD);
        boolean $$2 = false;
        for (ResourceLocation $$3 : this.loot) {
            ObjectListIterator var8 = serverPlayer0.server.getLootData().m_278676_($$3).getRandomItems($$1).iterator();
            while (var8.hasNext()) {
                ItemStack $$4 = (ItemStack) var8.next();
                if (serverPlayer0.m_36356_($$4)) {
                    serverPlayer0.m_9236_().playSound(null, serverPlayer0.m_20185_(), serverPlayer0.m_20186_(), serverPlayer0.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((serverPlayer0.m_217043_().nextFloat() - serverPlayer0.m_217043_().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    $$2 = true;
                } else {
                    ItemEntity $$5 = serverPlayer0.m_36176_($$4, false);
                    if ($$5 != null) {
                        $$5.setNoPickUpDelay();
                        $$5.setTarget(serverPlayer0.m_20148_());
                    }
                }
            }
        }
        if ($$2) {
            serverPlayer0.f_36096_.broadcastChanges();
        }
        if (this.recipes.length > 0) {
            serverPlayer0.awardRecipesByKey(this.recipes);
        }
        MinecraftServer $$6 = serverPlayer0.server;
        this.function.get($$6.getFunctions()).ifPresent(p_289236_ -> $$6.getFunctions().execute(p_289236_, serverPlayer0.m_20203_().withSuppressedOutput().withPermission(2)));
    }

    public String toString() {
        return "AdvancementRewards{experience=" + this.experience + ", loot=" + Arrays.toString(this.loot) + ", recipes=" + Arrays.toString(this.recipes) + ", function=" + this.function + "}";
    }

    public JsonElement serializeToJson() {
        if (this == EMPTY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            if (this.experience != 0) {
                $$0.addProperty("experience", this.experience);
            }
            if (this.loot.length > 0) {
                JsonArray $$1 = new JsonArray();
                for (ResourceLocation $$2 : this.loot) {
                    $$1.add($$2.toString());
                }
                $$0.add("loot", $$1);
            }
            if (this.recipes.length > 0) {
                JsonArray $$3 = new JsonArray();
                for (ResourceLocation $$4 : this.recipes) {
                    $$3.add($$4.toString());
                }
                $$0.add("recipes", $$3);
            }
            if (this.function.getId() != null) {
                $$0.addProperty("function", this.function.getId().toString());
            }
            return $$0;
        }
    }

    public static AdvancementRewards deserialize(JsonObject jsonObject0) throws JsonParseException {
        int $$1 = GsonHelper.getAsInt(jsonObject0, "experience", 0);
        JsonArray $$2 = GsonHelper.getAsJsonArray(jsonObject0, "loot", new JsonArray());
        ResourceLocation[] $$3 = new ResourceLocation[$$2.size()];
        for (int $$4 = 0; $$4 < $$3.length; $$4++) {
            $$3[$$4] = new ResourceLocation(GsonHelper.convertToString($$2.get($$4), "loot[" + $$4 + "]"));
        }
        JsonArray $$5 = GsonHelper.getAsJsonArray(jsonObject0, "recipes", new JsonArray());
        ResourceLocation[] $$6 = new ResourceLocation[$$5.size()];
        for (int $$7 = 0; $$7 < $$6.length; $$7++) {
            $$6[$$7] = new ResourceLocation(GsonHelper.convertToString($$5.get($$7), "recipes[" + $$7 + "]"));
        }
        CommandFunction.CacheableFunction $$8;
        if (jsonObject0.has("function")) {
            $$8 = new CommandFunction.CacheableFunction(new ResourceLocation(GsonHelper.getAsString(jsonObject0, "function")));
        } else {
            $$8 = CommandFunction.CacheableFunction.NONE;
        }
        return new AdvancementRewards($$1, $$3, $$6, $$8);
    }

    public static class Builder {

        private int experience;

        private final List<ResourceLocation> loot = Lists.newArrayList();

        private final List<ResourceLocation> recipes = Lists.newArrayList();

        @Nullable
        private ResourceLocation function;

        public static AdvancementRewards.Builder experience(int int0) {
            return new AdvancementRewards.Builder().addExperience(int0);
        }

        public AdvancementRewards.Builder addExperience(int int0) {
            this.experience += int0;
            return this;
        }

        public static AdvancementRewards.Builder loot(ResourceLocation resourceLocation0) {
            return new AdvancementRewards.Builder().addLootTable(resourceLocation0);
        }

        public AdvancementRewards.Builder addLootTable(ResourceLocation resourceLocation0) {
            this.loot.add(resourceLocation0);
            return this;
        }

        public static AdvancementRewards.Builder recipe(ResourceLocation resourceLocation0) {
            return new AdvancementRewards.Builder().addRecipe(resourceLocation0);
        }

        public AdvancementRewards.Builder addRecipe(ResourceLocation resourceLocation0) {
            this.recipes.add(resourceLocation0);
            return this;
        }

        public static AdvancementRewards.Builder function(ResourceLocation resourceLocation0) {
            return new AdvancementRewards.Builder().runs(resourceLocation0);
        }

        public AdvancementRewards.Builder runs(ResourceLocation resourceLocation0) {
            this.function = resourceLocation0;
            return this;
        }

        public AdvancementRewards build() {
            return new AdvancementRewards(this.experience, (ResourceLocation[]) this.loot.toArray(new ResourceLocation[0]), (ResourceLocation[]) this.recipes.toArray(new ResourceLocation[0]), this.function == null ? CommandFunction.CacheableFunction.NONE : new CommandFunction.CacheableFunction(this.function));
        }
    }
}