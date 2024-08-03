package noppes.npcs.controllers.data;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobInterface;
import noppes.npcs.roles.RoleInterface;

public class DataTransform {

    public CompoundTag display;

    public CompoundTag ai;

    public CompoundTag advanced;

    public CompoundTag inv;

    public CompoundTag stats;

    public CompoundTag role;

    public CompoundTag job;

    public boolean hasDisplay;

    public boolean hasAi;

    public boolean hasAdvanced;

    public boolean hasInv;

    public boolean hasStats;

    public boolean hasRole;

    public boolean hasJob;

    public boolean isActive;

    private EntityNPCInterface npc;

    public boolean editingModus = false;

    public DataTransform(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public CompoundTag save(CompoundTag compound) {
        compound.putBoolean("TransformIsActive", this.isActive);
        this.writeOptions(compound);
        if (this.hasDisplay) {
            compound.put("TransformDisplay", this.display);
        }
        if (this.hasAi) {
            compound.put("TransformAI", this.ai);
        }
        if (this.hasAdvanced) {
            compound.put("TransformAdvanced", this.advanced);
        }
        if (this.hasInv) {
            compound.put("TransformInv", this.inv);
        }
        if (this.hasStats) {
            compound.put("TransformStats", this.stats);
        }
        if (this.hasRole) {
            compound.put("TransformRole", this.role);
        }
        if (this.hasJob) {
            compound.put("TransformJob", this.job);
        }
        return compound;
    }

    public CompoundTag writeOptions(CompoundTag compound) {
        compound.putBoolean("TransformHasDisplay", this.hasDisplay);
        compound.putBoolean("TransformHasAI", this.hasAi);
        compound.putBoolean("TransformHasAdvanced", this.hasAdvanced);
        compound.putBoolean("TransformHasInv", this.hasInv);
        compound.putBoolean("TransformHasStats", this.hasStats);
        compound.putBoolean("TransformHasRole", this.hasRole);
        compound.putBoolean("TransformHasJob", this.hasJob);
        compound.putBoolean("TransformEditingModus", this.editingModus);
        return compound;
    }

    public void readToNBT(CompoundTag compound) {
        this.isActive = compound.getBoolean("TransformIsActive");
        this.readOptions(compound);
        this.display = this.hasDisplay ? compound.getCompound("TransformDisplay") : this.getDisplay();
        this.ai = this.hasAi ? compound.getCompound("TransformAI") : this.npc.ais.save(new CompoundTag());
        this.advanced = this.hasAdvanced ? compound.getCompound("TransformAdvanced") : this.getAdvanced();
        this.inv = this.hasInv ? compound.getCompound("TransformInv") : this.npc.inventory.save(new CompoundTag());
        this.stats = this.hasStats ? compound.getCompound("TransformStats") : this.npc.stats.save(new CompoundTag());
        this.job = this.hasJob ? compound.getCompound("TransformJob") : this.getJob();
        this.role = this.hasRole ? compound.getCompound("TransformRole") : this.getRole();
    }

    public CompoundTag getJob() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("NpcJob", this.npc.job.getType());
        this.npc.job.save(compound);
        return compound;
    }

    public CompoundTag getRole() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("Role", this.npc.role.getType());
        this.npc.role.save(compound);
        return compound;
    }

    public CompoundTag getDisplay() {
        CompoundTag compound = this.npc.display.save(new CompoundTag());
        if (this.npc instanceof EntityCustomNpc) {
            compound.put("ModelData", ((EntityCustomNpc) this.npc).modelData.save());
        }
        return compound;
    }

    public CompoundTag getAdvanced() {
        JobInterface jopType = this.npc.job;
        RoleInterface roleType = this.npc.role;
        this.npc.job = JobInterface.NONE;
        this.npc.role = RoleInterface.NONE;
        CompoundTag compound = this.npc.advanced.save(new CompoundTag());
        this.npc.job = jopType;
        this.npc.role = roleType;
        return compound;
    }

    public void readOptions(CompoundTag compound) {
        boolean hadDisplay = this.hasDisplay;
        boolean hadAI = this.hasAi;
        boolean hadAdvanced = this.hasAdvanced;
        boolean hadInv = this.hasInv;
        boolean hadStats = this.hasStats;
        boolean hadRole = this.hasRole;
        boolean hadJob = this.hasJob;
        this.hasDisplay = compound.getBoolean("TransformHasDisplay");
        this.hasAi = compound.getBoolean("TransformHasAI");
        this.hasAdvanced = compound.getBoolean("TransformHasAdvanced");
        this.hasInv = compound.getBoolean("TransformHasInv");
        this.hasStats = compound.getBoolean("TransformHasStats");
        this.hasRole = compound.getBoolean("TransformHasRole");
        this.hasJob = compound.getBoolean("TransformHasJob");
        this.editingModus = compound.getBoolean("TransformEditingModus");
        if (this.hasDisplay && !hadDisplay) {
            this.display = this.getDisplay();
        }
        if (this.hasAi && !hadAI) {
            this.ai = this.npc.ais.save(new CompoundTag());
        }
        if (this.hasStats && !hadStats) {
            this.stats = this.npc.stats.save(new CompoundTag());
        }
        if (this.hasInv && !hadInv) {
            this.inv = this.npc.inventory.save(new CompoundTag());
        }
        if (this.hasAdvanced && !hadAdvanced) {
            this.advanced = this.getAdvanced();
        }
        if (this.hasJob && !hadJob) {
            this.job = this.getJob();
        }
        if (this.hasRole && !hadRole) {
            this.role = this.getRole();
        }
    }

    public boolean isValid() {
        return this.hasAdvanced || this.hasAi || this.hasDisplay || this.hasInv || this.hasStats || this.hasJob || this.hasRole;
    }

    public CompoundTag processAdvanced(CompoundTag compoundAdv, CompoundTag compoundRole, CompoundTag compoundJob) {
        if (this.hasAdvanced) {
            compoundAdv = this.advanced;
        }
        if (this.hasRole) {
            compoundRole = this.role;
        }
        if (this.hasJob) {
            compoundJob = this.job;
        }
        for (String name : compoundRole.getAllKeys()) {
            compoundAdv.put(name, compoundRole.get(name));
        }
        for (String name : compoundJob.getAllKeys()) {
            compoundAdv.put(name, compoundJob.get(name));
        }
        return compoundAdv;
    }

    public void transform(boolean isActive) {
        if (this.isActive != isActive) {
            if (this.hasDisplay) {
                CompoundTag compound = this.getDisplay();
                this.npc.display.readToNBT(NBTTags.NBTMerge(compound, this.display));
                if (this.npc instanceof EntityCustomNpc) {
                    ((EntityCustomNpc) this.npc).modelData.load(NBTTags.NBTMerge(compound.getCompound("ModelData"), this.display.getCompound("ModelData")));
                }
                this.display = compound;
            }
            if (this.hasStats) {
                CompoundTag compound = this.npc.stats.save(new CompoundTag());
                this.npc.stats.readToNBT(NBTTags.NBTMerge(compound, this.stats));
                this.stats = compound;
            }
            if (this.hasAdvanced || this.hasJob || this.hasRole) {
                CompoundTag compoundAdv = this.getAdvanced();
                CompoundTag compoundRole = this.getRole();
                CompoundTag compoundJob = this.getJob();
                CompoundTag compound = this.processAdvanced(compoundAdv, compoundRole, compoundJob);
                this.npc.advanced.readToNBT(compound);
                if (this.npc.role.getType() != 0) {
                    this.npc.role.load(NBTTags.NBTMerge(compoundRole, compound));
                }
                if (this.npc.job.getType() != 0) {
                    this.npc.job.load(NBTTags.NBTMerge(compoundJob, compound));
                }
                if (this.hasAdvanced) {
                    this.advanced = compoundAdv;
                }
                if (this.hasRole) {
                    this.role = compoundRole;
                }
                if (this.hasJob) {
                    this.job = compoundJob;
                }
            }
            if (this.hasAi) {
                CompoundTag compoundx = this.npc.ais.save(new CompoundTag());
                this.npc.ais.readToNBT(NBTTags.NBTMerge(compoundx, this.ai));
                this.ai = compoundx;
                this.npc.setCurrentAnimation(0);
            }
            if (this.hasInv) {
                CompoundTag compoundx = this.npc.inventory.save(new CompoundTag());
                this.npc.inventory.load(NBTTags.NBTMerge(compoundx, this.inv));
                this.inv = compoundx;
            }
            this.npc.updateAI = true;
            this.isActive = isActive;
            this.npc.updateClient = true;
        }
    }
}