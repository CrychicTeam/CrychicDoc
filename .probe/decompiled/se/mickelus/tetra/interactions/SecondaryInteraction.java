package se.mickelus.tetra.interactions;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface SecondaryInteraction {

    String getKey();

    String getLabel();

    boolean canPerform(Player var1, Level var2, @Nullable BlockPos var3, @Nullable Entity var4);

    void perform(Player var1, Level var2, @Nullable BlockPos var3, @Nullable Entity var4);

    PerformSide getPerformSide();
}