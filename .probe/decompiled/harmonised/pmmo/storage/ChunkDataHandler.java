package harmonised.pmmo.storage;

import harmonised.pmmo.util.Reference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class ChunkDataHandler implements IChunkData {

    private Map<BlockPos, UUID> placedMap = new HashMap();

    private Map<BlockPos, UUID> breakMap = new HashMap();

    @Override
    public void addPos(BlockPos blockPos, UUID uuid) {
        this.placedMap.put(blockPos, uuid);
    }

    @Override
    public void delPos(BlockPos blockPos) {
        this.placedMap.remove(blockPos);
    }

    @Override
    public UUID checkPos(BlockPos pos) {
        return (UUID) this.placedMap.getOrDefault(pos, Reference.NIL);
    }

    @Override
    public boolean playerMatchesPos(Player player, BlockPos pos) {
        return this.placedMap.containsKey(pos) && ((UUID) this.placedMap.get(pos)).equals(player.m_20148_());
    }

    @Override
    public Map<BlockPos, UUID> getMap() {
        return this.placedMap;
    }

    @Override
    public void setMap(Map<BlockPos, UUID> map) {
        this.placedMap = map;
    }

    @Override
    public UUID getBreaker(BlockPos pos) {
        return (UUID) this.breakMap.remove(pos);
    }

    @Override
    public void setBreaker(BlockPos pos, UUID breaker) {
        this.breakMap.put(pos, breaker);
    }
}