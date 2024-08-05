package com.illusivesoulworks.polymorph.common.capability;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon;
import com.illusivesoulworks.polymorph.api.common.capability.IStackRecipeData;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class StackRecipeData extends AbstractRecipeData<ItemStack> implements IStackRecipeData {

    public StackRecipeData(ItemStack owner) {
        super(owner);
    }

    @Override
    public Set<ServerPlayer> getListeners() {
        Set<ServerPlayer> players = new HashSet();
        IPolymorphCommon commonApi = PolymorphApi.common();
        commonApi.getServer().ifPresent(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                commonApi.getRecipeDataFromItemStack(player.f_36096_).ifPresent(recipeData -> {
                    if (recipeData == this) {
                        players.add(player);
                    }
                });
            }
        });
        return players;
    }
}