package noppes.npcs.roles;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.data.role.IJobFollower;
import noppes.npcs.entity.EntityNPCInterface;

public class JobFollower extends JobInterface implements IJobFollower {

    public EntityNPCInterface following = null;

    private int ticks = 40;

    private int range = 20;

    public String name = "";

    public JobFollower(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putString("FollowingEntityName", this.name);
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        this.name = compound.getString("FollowingEntityName");
    }

    @Override
    public boolean aiShouldExecute() {
        if (this.npc.isAttacking()) {
            return false;
        } else {
            this.ticks--;
            if (this.ticks > 0) {
                return false;
            } else {
                this.ticks = 10;
                this.following = null;
                for (EntityNPCInterface entity : this.npc.m_9236_().m_45976_(EntityNPCInterface.class, this.npc.m_20191_().inflate((double) this.getRange(), (double) this.getRange(), (double) this.getRange()))) {
                    if (entity != this.npc && !entity.isKilled() && entity.display.getName().equalsIgnoreCase(this.name)) {
                        this.following = entity;
                        break;
                    }
                }
                return false;
            }
        }
    }

    private int getRange() {
        return this.range > CustomNpcs.NpcNavRange ? CustomNpcs.NpcNavRange : this.range;
    }

    @Override
    public boolean isFollowing() {
        return this.following != null;
    }

    @Override
    public void reset() {
    }

    @Override
    public void stop() {
        this.following = null;
    }

    public boolean hasOwner() {
        return !this.name.isEmpty();
    }

    @Override
    public String getFollowing() {
        return this.name;
    }

    @Override
    public void setFollowing(String name) {
        this.name = name;
    }

    @Override
    public ICustomNpc getFollowingNpc() {
        return this.following == null ? null : this.following.wrappedNPC;
    }

    @Override
    public int getType() {
        return 5;
    }
}