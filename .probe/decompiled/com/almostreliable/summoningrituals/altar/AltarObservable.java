package com.almostreliable.summoningrituals.altar;

import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class AltarObservable {

    private final List<AltarObservable.Observer> observers = new ArrayList();

    boolean invoke(ServerLevel level, BlockPos pos, AltarRecipe recipe, @Nullable ServerPlayer player) {
        for (AltarObservable.Observer o : this.observers) {
            if (o.run(level, pos, recipe, player)) {
                return false;
            }
        }
        return true;
    }

    public void register(AltarObservable.Observer observer) {
        this.observers.add(observer);
    }

    @FunctionalInterface
    public interface Observer {

        boolean run(ServerLevel var1, BlockPos var2, AltarRecipe var3, @Nullable ServerPlayer var4);
    }
}