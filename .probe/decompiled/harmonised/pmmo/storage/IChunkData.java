package harmonised.pmmo.storage;

import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface IChunkData {

    void addPos(BlockPos var1, UUID var2);

    void delPos(BlockPos var1);

    UUID checkPos(BlockPos var1);

    boolean playerMatchesPos(Player var1, BlockPos var2);

    Map<BlockPos, UUID> getMap();

    void setMap(Map<BlockPos, UUID> var1);

    UUID getBreaker(BlockPos var1);

    void setBreaker(BlockPos var1, UUID var2);
}