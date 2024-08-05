package snownee.kiwi.customization.builder;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface BuilderRule {

    Codec<BuilderRule> CODEC = Codec.unit(null);

    Stream<Block> relatedBlocks();

    boolean matches(Player var1, ItemStack var2, BlockState var3);

    void apply(UseOnContext var1, List<BlockPos> var2);

    List<BlockPos> searchPositions(UseOnContext var1);
}