package vazkii.patchouli.api;

import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import org.jetbrains.annotations.Nullable;

public interface IMultiblock {

    IMultiblock offset(int var1, int var2, int var3);

    IMultiblock offsetView(int var1, int var2, int var3);

    IMultiblock setSymmetrical(boolean var1);

    IMultiblock setId(ResourceLocation var1);

    boolean isSymmetrical();

    ResourceLocation getID();

    void place(Level var1, BlockPos var2, Rotation var3);

    Pair<BlockPos, Collection<IMultiblock.SimulateResult>> simulate(Level var1, BlockPos var2, Rotation var3, boolean var4);

    @Nullable
    Rotation validate(Level var1, BlockPos var2);

    boolean validate(Level var1, BlockPos var2, Rotation var3);

    boolean test(Level var1, BlockPos var2, int var3, int var4, int var5, Rotation var6);

    Vec3i getSize();

    public interface SimulateResult {

        BlockPos getWorldPosition();

        IStateMatcher getStateMatcher();

        @Nullable
        Character getCharacter();

        boolean test(Level var1, Rotation var2);
    }
}