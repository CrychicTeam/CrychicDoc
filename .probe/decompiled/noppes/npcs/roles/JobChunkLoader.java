package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import noppes.npcs.controllers.ChunkController;
import noppes.npcs.entity.EntityNPCInterface;

public class JobChunkLoader extends JobInterface {

    private List<ChunkPos> chunks = new ArrayList();

    private int ticks = 20;

    private long playerLastSeen = -1L;

    public JobChunkLoader(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putLong("ChunkPlayerLastSeen", this.playerLastSeen);
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        this.playerLastSeen = compound.getLong("ChunkPlayerLastSeen");
    }

    @Override
    public boolean aiShouldExecute() {
        this.ticks--;
        if (this.ticks > 0) {
            return false;
        } else {
            this.ticks = 20;
            List players = this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate(48.0, 48.0, 48.0));
            if (!players.isEmpty()) {
                this.playerLastSeen = System.currentTimeMillis();
            }
            if (this.playerLastSeen < 0L) {
                return false;
            } else if (System.currentTimeMillis() > this.playerLastSeen + 600000L) {
                ChunkController.instance.unload((ServerLevel) this.npc.m_9236_(), this.npc.m_20148_(), this.npc.m_146902_().x, this.npc.m_146902_().z);
                this.chunks.clear();
                this.playerLastSeen = -1L;
                return false;
            } else {
                double x = this.npc.m_20185_() / 16.0;
                double z = this.npc.m_20189_() / 16.0;
                List<ChunkPos> list = new ArrayList();
                list.add(new ChunkPos(Mth.floor(x), Mth.floor(z)));
                list.add(new ChunkPos(Mth.ceil(x), Mth.ceil(z)));
                list.add(new ChunkPos(Mth.floor(x), Mth.ceil(z)));
                list.add(new ChunkPos(Mth.ceil(x), Mth.floor(z)));
                for (ChunkPos chunk : list) {
                    if (!this.chunks.contains(chunk)) {
                        ChunkController.instance.load((ServerLevel) this.npc.m_9236_(), this.npc.m_20148_(), chunk.x, chunk.z);
                    }
                    this.chunks.remove(chunk);
                }
                for (ChunkPos chunk : this.chunks) {
                    ChunkController.instance.unload((ServerLevel) this.npc.m_9236_(), this.npc.m_20148_(), chunk.x, chunk.z);
                }
                this.chunks = list;
                return false;
            }
        }
    }

    @Override
    public boolean aiContinueExecute() {
        return false;
    }

    @Override
    public void reset() {
        if (this.npc.m_9236_() instanceof ServerLevel) {
            ChunkController.instance.unload((ServerLevel) this.npc.m_9236_(), this.npc.m_20148_(), this.npc.m_146902_().x, this.npc.m_146902_().z);
            this.chunks.clear();
            this.playerLastSeen = 0L;
        }
    }

    @Override
    public void delete() {
    }

    @Override
    public int getType() {
        return 8;
    }
}