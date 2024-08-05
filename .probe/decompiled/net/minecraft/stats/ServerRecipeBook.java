package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ResourceLocationException;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.slf4j.Logger;

public class ServerRecipeBook extends RecipeBook {

    public static final String RECIPE_BOOK_TAG = "recipeBook";

    private static final Logger LOGGER = LogUtils.getLogger();

    public int addRecipes(Collection<Recipe<?>> collectionRecipe0, ServerPlayer serverPlayer1) {
        List<ResourceLocation> $$2 = Lists.newArrayList();
        int $$3 = 0;
        for (Recipe<?> $$4 : collectionRecipe0) {
            ResourceLocation $$5 = $$4.getId();
            if (!this.f_12680_.contains($$5) && !$$4.isSpecial()) {
                this.m_12702_($$5);
                this.m_12719_($$5);
                $$2.add($$5);
                CriteriaTriggers.RECIPE_UNLOCKED.trigger(serverPlayer1, $$4);
                $$3++;
            }
        }
        if ($$2.size() > 0) {
            this.sendRecipes(ClientboundRecipePacket.State.ADD, serverPlayer1, $$2);
        }
        return $$3;
    }

    public int removeRecipes(Collection<Recipe<?>> collectionRecipe0, ServerPlayer serverPlayer1) {
        List<ResourceLocation> $$2 = Lists.newArrayList();
        int $$3 = 0;
        for (Recipe<?> $$4 : collectionRecipe0) {
            ResourceLocation $$5 = $$4.getId();
            if (this.f_12680_.contains($$5)) {
                this.m_12715_($$5);
                $$2.add($$5);
                $$3++;
            }
        }
        this.sendRecipes(ClientboundRecipePacket.State.REMOVE, serverPlayer1, $$2);
        return $$3;
    }

    private void sendRecipes(ClientboundRecipePacket.State clientboundRecipePacketState0, ServerPlayer serverPlayer1, List<ResourceLocation> listResourceLocation2) {
        serverPlayer1.connection.send(new ClientboundRecipePacket(clientboundRecipePacketState0, listResourceLocation2, Collections.emptyList(), this.m_12684_()));
    }

    public CompoundTag toNbt() {
        CompoundTag $$0 = new CompoundTag();
        this.m_12684_().write($$0);
        ListTag $$1 = new ListTag();
        for (ResourceLocation $$2 : this.f_12680_) {
            $$1.add(StringTag.valueOf($$2.toString()));
        }
        $$0.put("recipes", $$1);
        ListTag $$3 = new ListTag();
        for (ResourceLocation $$4 : this.f_12681_) {
            $$3.add(StringTag.valueOf($$4.toString()));
        }
        $$0.put("toBeDisplayed", $$3);
        return $$0;
    }

    public void fromNbt(CompoundTag compoundTag0, RecipeManager recipeManager1) {
        this.m_12687_(RecipeBookSettings.read(compoundTag0));
        ListTag $$2 = compoundTag0.getList("recipes", 8);
        this.loadRecipes($$2, this::m_12700_, recipeManager1);
        ListTag $$3 = compoundTag0.getList("toBeDisplayed", 8);
        this.loadRecipes($$3, this::m_12723_, recipeManager1);
    }

    private void loadRecipes(ListTag listTag0, Consumer<Recipe<?>> consumerRecipe1, RecipeManager recipeManager2) {
        for (int $$3 = 0; $$3 < listTag0.size(); $$3++) {
            String $$4 = listTag0.getString($$3);
            try {
                ResourceLocation $$5 = new ResourceLocation($$4);
                Optional<? extends Recipe<?>> $$6 = recipeManager2.byKey($$5);
                if (!$$6.isPresent()) {
                    LOGGER.error("Tried to load unrecognized recipe: {} removed now.", $$5);
                } else {
                    consumerRecipe1.accept((Recipe) $$6.get());
                }
            } catch (ResourceLocationException var8) {
                LOGGER.error("Tried to load improperly formatted recipe: {} removed now.", $$4);
            }
        }
    }

    public void sendInitialRecipeBook(ServerPlayer serverPlayer0) {
        serverPlayer0.connection.send(new ClientboundRecipePacket(ClientboundRecipePacket.State.INIT, this.f_12680_, this.f_12681_, this.m_12684_()));
    }
}