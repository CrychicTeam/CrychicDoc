package noppes.npcs.roles;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.entity.data.role.IJobBard;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.MusicManagerMixin;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class JobBard extends JobInterface implements IJobBard {

    public int minRange = 2;

    public int maxRange = 64;

    public boolean isStreamer = true;

    public boolean isLooping = false;

    public boolean hasOffRange = true;

    public String song = "";

    public JobBard(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.putString("BardSong", this.song);
        nbttagcompound.putInt("BardMinRange", this.minRange);
        nbttagcompound.putInt("BardMaxRange", this.maxRange);
        nbttagcompound.putBoolean("BardStreamer", this.isStreamer);
        nbttagcompound.putBoolean("BardLoops", this.isLooping);
        nbttagcompound.putBoolean("BardHasOff", this.hasOffRange);
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.song = nbttagcompound.getString("BardSong");
        this.minRange = nbttagcompound.getInt("BardMinRange");
        this.maxRange = nbttagcompound.getInt("BardMaxRange");
        this.isStreamer = nbttagcompound.getBoolean("BardStreamer");
        this.isLooping = nbttagcompound.getBoolean("BardLoops");
        this.hasOffRange = nbttagcompound.getBoolean("BardHasOff");
    }

    public void aiStep() {
        if (this.npc.isClientSide() && !this.song.isEmpty()) {
            if (!MusicController.Instance.isPlaying(this.song)) {
                List<Player> list = this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate((double) this.minRange, (double) (this.minRange / 2), (double) this.minRange));
                if (!list.contains(CustomNpcs.proxy.getPlayer())) {
                    return;
                }
                if (this.isStreamer) {
                    MusicController.Instance.playStreaming(this.song, this.npc, this.isLooping);
                } else {
                    MusicController.Instance.playMusic(this.song, this.npc, this.isLooping);
                }
            } else if (MusicController.Instance.playingEntity != this.npc) {
                Player player = CustomNpcs.proxy.getPlayer();
                if (this.npc.m_20280_(player) < MusicController.Instance.playingEntity.distanceToSqr(player)) {
                    MusicController.Instance.playingEntity = this.npc;
                }
            } else if (this.hasOffRange) {
                List<Player> listx = this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate((double) this.maxRange, (double) (this.maxRange / 2), (double) this.maxRange));
                if (!listx.contains(CustomNpcs.proxy.getPlayer())) {
                    MusicController.Instance.stopMusic();
                }
            }
            if (MusicController.Instance.isPlaying(this.song)) {
                ((MusicManagerMixin) Minecraft.getInstance().getMusicManager()).nextSongDelay(12000);
            }
        }
    }

    @Override
    public void killed() {
        this.delete();
    }

    @Override
    public void delete() {
        if (this.npc.m_9236_().isClientSide && this.hasOffRange && MusicController.Instance.isPlaying(this.song)) {
            MusicController.Instance.stopMusic();
        }
    }

    @Override
    public String getSong() {
        return NoppesStringUtils.cleanResource(this.song);
    }

    @Override
    public void setSong(String song) {
        this.song = NoppesStringUtils.cleanResource(song);
        this.npc.updateClient = true;
    }

    @Override
    public int getType() {
        return 1;
    }
}