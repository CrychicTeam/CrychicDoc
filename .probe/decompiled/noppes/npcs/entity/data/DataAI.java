package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IPos;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.wrapper.BlockPosWrapper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBuilder;
import noppes.npcs.roles.JobFarmer;

public class DataAI implements INPCAi {

    private EntityNPCInterface npc;

    public int onAttack = 0;

    public int doorInteract = 2;

    public int findShelter = 2;

    public boolean canSwim = true;

    public boolean reactsToFire = false;

    public boolean avoidsWater = false;

    public boolean avoidsSun = false;

    public boolean returnToStart = true;

    public boolean directLOS = true;

    public boolean canLeap = false;

    public boolean canSprint = false;

    public boolean stopAndInteract = true;

    public boolean attackInvisible = false;

    public int movementType = 0;

    public int animationType = 0;

    private int standingType = 0;

    private int movingType = 0;

    public boolean npcInteracting = true;

    public int orientation = 0;

    public float bodyOffsetX = 5.0F;

    public float bodyOffsetY = 5.0F;

    public float bodyOffsetZ = 5.0F;

    public int walkingRange = 10;

    private int moveSpeed = 5;

    private List<int[]> movingPath = new ArrayList();

    private BlockPos startPos = BlockPos.ZERO;

    public int movingPos = 0;

    public int movingPattern = 0;

    public boolean movingPause = true;

    public DataAI(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public void readToNBT(CompoundTag compound) {
        this.canSwim = compound.getBoolean("CanSwim");
        this.reactsToFire = compound.getBoolean("ReactsToFire");
        this.setAvoidsWater(compound.getBoolean("AvoidsWater"));
        this.avoidsSun = compound.getBoolean("AvoidsSun");
        this.returnToStart = compound.getBoolean("ReturnToStart");
        this.onAttack = compound.getInt("OnAttack");
        this.doorInteract = compound.getInt("DoorInteract");
        this.findShelter = compound.getInt("FindShelter");
        this.directLOS = compound.getBoolean("DirectLOS");
        this.canLeap = compound.getBoolean("CanLeap");
        this.canSprint = compound.getBoolean("CanSprint");
        this.movingPause = compound.getBoolean("MovingPause");
        this.npcInteracting = compound.getBoolean("npcInteracting");
        this.stopAndInteract = compound.getBoolean("stopAndInteract");
        this.movementType = compound.getInt("MovementType");
        this.animationType = compound.getInt("MoveState");
        this.standingType = compound.getInt("StandingState");
        this.movingType = compound.getInt("MovingState");
        this.orientation = compound.getInt("Orientation");
        this.bodyOffsetY = compound.getFloat("PositionOffsetY");
        this.bodyOffsetZ = compound.getFloat("PositionOffsetZ");
        this.bodyOffsetX = compound.getFloat("PositionOffsetX");
        this.walkingRange = compound.getInt("WalkingRange");
        this.setWalkingSpeed(compound.getInt("MoveSpeed"));
        this.setMovingPath(NBTTags.getIntegerArraySet(compound.getList("MovingPathNew", 10)));
        this.movingPos = compound.getInt("MovingPos");
        this.movingPattern = compound.getInt("MovingPatern");
        this.attackInvisible = compound.getBoolean("AttackInvisible");
        if (compound.contains("StartPosNew")) {
            int[] startPos = compound.getIntArray("StartPosNew");
            this.setStartPos(new BlockPos(startPos[0], startPos[1], startPos[2]));
        }
    }

    public CompoundTag save(CompoundTag compound) {
        compound.putBoolean("CanSwim", this.canSwim);
        compound.putBoolean("ReactsToFire", this.reactsToFire);
        compound.putBoolean("AvoidsWater", this.avoidsWater);
        compound.putBoolean("AvoidsSun", this.avoidsSun);
        compound.putBoolean("ReturnToStart", this.returnToStart);
        compound.putInt("OnAttack", this.onAttack);
        compound.putInt("DoorInteract", this.doorInteract);
        compound.putInt("FindShelter", this.findShelter);
        compound.putBoolean("DirectLOS", this.directLOS);
        compound.putBoolean("CanLeap", this.canLeap);
        compound.putBoolean("CanSprint", this.canSprint);
        compound.putBoolean("MovingPause", this.movingPause);
        compound.putBoolean("npcInteracting", this.npcInteracting);
        compound.putBoolean("stopAndInteract", this.stopAndInteract);
        compound.putInt("MoveState", this.animationType);
        compound.putInt("StandingState", this.standingType);
        compound.putInt("MovingState", this.movingType);
        compound.putInt("MovementType", this.movementType);
        compound.putInt("Orientation", this.orientation);
        compound.putFloat("PositionOffsetX", this.bodyOffsetX);
        compound.putFloat("PositionOffsetY", this.bodyOffsetY);
        compound.putFloat("PositionOffsetZ", this.bodyOffsetZ);
        compound.putInt("WalkingRange", this.walkingRange);
        compound.putInt("MoveSpeed", this.moveSpeed);
        compound.put("MovingPathNew", NBTTags.nbtIntegerArraySet(this.movingPath));
        compound.putInt("MovingPos", this.movingPos);
        compound.putInt("MovingPatern", this.movingPattern);
        this.setAvoidsWater(this.avoidsWater);
        compound.putIntArray("StartPosNew", this.getStartArray());
        compound.putBoolean("AttackInvisible", this.attackInvisible);
        return compound;
    }

    public List<int[]> getMovingPath() {
        if (this.movingPath.isEmpty() && this.startPos != null) {
            this.movingPath.add(this.getStartArray());
        }
        return this.movingPath;
    }

    public void setMovingPath(List<int[]> list) {
        this.movingPath = list;
        if (!this.movingPath.isEmpty()) {
            int[] startPos = (int[]) this.movingPath.get(0);
            this.setStartPos(new BlockPos(startPos[0], startPos[1], startPos[2]));
        }
    }

    public BlockPos startPos() {
        if (this.startPos == null || this.startPos == BlockPos.ZERO) {
            this.setStartPos(this.npc.m_20183_());
        }
        return this.startPos;
    }

    public int[] getStartArray() {
        BlockPos pos = this.startPos();
        return new int[] { pos.m_123341_(), pos.m_123342_(), pos.m_123343_() };
    }

    public int[] getCurrentMovingPath() {
        List<int[]> list = this.getMovingPath();
        int size = list.size();
        if (size == 1) {
            return (int[]) list.get(0);
        } else {
            int pos = this.movingPos;
            if (this.movingPattern == 0 && pos >= size) {
                pos = this.movingPos = 0;
            }
            if (this.movingPattern == 1) {
                int size2 = size * 2 - 1;
                if (pos >= size2) {
                    pos = this.movingPos = 0;
                } else if (pos >= size) {
                    pos = size2 - pos;
                }
            }
            return (int[]) list.get(pos);
        }
    }

    public void clearMovingPath() {
        this.movingPath.clear();
        this.movingPos = 0;
    }

    public void setMovingPathPos(int m_pos, int[] pos) {
        if (m_pos < 0) {
            m_pos = 0;
        }
        this.movingPath.set(m_pos, pos);
    }

    public int[] getMovingPathPos(int m_pos) {
        return (int[]) this.movingPath.get(m_pos);
    }

    public void appendMovingPath(int[] pos) {
        this.movingPath.add(pos);
    }

    public int getMovingPos() {
        return this.movingPos;
    }

    public void setMovingPos(int pos) {
        this.movingPos = pos;
    }

    public int getMovingPathSize() {
        return this.movingPath.size();
    }

    public void incrementMovingPath() {
        List<int[]> list = this.getMovingPath();
        if (list.size() == 1) {
            this.movingPos = 0;
        } else {
            this.movingPos++;
            if (this.movingPattern == 0) {
                this.movingPos = this.movingPos % list.size();
            } else if (this.movingPattern == 1) {
                int size = list.size() * 2 - 1;
                this.movingPos %= size;
            }
        }
    }

    public void decreaseMovingPath() {
        List<int[]> list = this.getMovingPath();
        if (list.size() == 1) {
            this.movingPos = 0;
        } else {
            this.movingPos--;
            if (this.movingPos < 0) {
                if (this.movingPattern == 0) {
                    this.movingPos = list.size() - 1;
                } else if (this.movingPattern == 1) {
                    this.movingPos = list.size() * 2 - 2;
                }
            }
        }
    }

    public double distanceToSqrToPathPoint() {
        int[] pos = this.getCurrentMovingPath();
        return this.npc.m_20275_((double) pos[0] + 0.5, (double) pos[1], (double) pos[2] + 0.5);
    }

    public IPos getStartPos() {
        return new BlockPosWrapper(this.startPos());
    }

    public void setStartPos(BlockPos pos) {
        this.startPos = pos;
        this.npc.m_21446_(this.startPos, Math.max(this.npc.stats.aggroRange * 2, 64));
    }

    public void setStartPos(IPos pos) {
        this.setStartPos(pos.getMCBlockPos());
    }

    public void setStartPos(double x, double y, double z) {
        this.setStartPos(new BlockPos((int) x, (int) y, (int) z));
    }

    @Override
    public void setReturnsHome(boolean bo) {
        this.returnToStart = bo;
    }

    @Override
    public boolean getReturnsHome() {
        return this.returnToStart;
    }

    public boolean shouldReturnHome() {
        if (this.npc.job.getType() == 10 && ((JobBuilder) this.npc.job).isBuilding()) {
            return false;
        } else {
            return this.npc.job.getType() == 11 && ((JobFarmer) this.npc.job).isPlucking() ? false : this.returnToStart;
        }
    }

    @Override
    public int getAnimation() {
        return this.animationType;
    }

    @Override
    public int getCurrentAnimation() {
        return this.npc.currentAnimation;
    }

    @Override
    public void setAnimation(int type) {
        this.animationType = type;
    }

    @Override
    public int getRetaliateType() {
        return this.onAttack;
    }

    @Override
    public void setRetaliateType(int type) {
        if (type >= 0 && type <= 3) {
            this.onAttack = type;
            this.npc.updateAI = true;
        } else {
            throw new CustomNPCsException("Unknown retaliation type: " + type);
        }
    }

    @Override
    public int getMovingType() {
        return this.movingType;
    }

    @Override
    public void setMovingType(int type) {
        if (type >= 0 && type <= 2) {
            this.movingType = type;
            this.npc.updateAI = true;
        } else {
            throw new CustomNPCsException("Unknown moving type: " + type);
        }
    }

    @Override
    public int getStandingType() {
        return this.standingType;
    }

    @Override
    public void setStandingType(int type) {
        if (type >= 0 && type <= 3) {
            this.standingType = type;
            this.npc.updateAI = true;
        } else {
            throw new CustomNPCsException("Unknown standing type: " + type);
        }
    }

    @Override
    public boolean getAttackInvisible() {
        return this.attackInvisible;
    }

    @Override
    public void setAttackInvisible(boolean attack) {
        this.attackInvisible = attack;
    }

    @Override
    public int getWanderingRange() {
        return this.walkingRange;
    }

    @Override
    public void setWanderingRange(int range) {
        if (range >= 1 && range <= 50) {
            this.walkingRange = range;
        } else {
            throw new CustomNPCsException("Bad wandering range: " + range);
        }
    }

    @Override
    public boolean getInteractWithNPCs() {
        return this.npcInteracting;
    }

    @Override
    public void setInteractWithNPCs(boolean interact) {
        this.npcInteracting = interact;
    }

    @Override
    public boolean getStopOnInteract() {
        return this.stopAndInteract;
    }

    @Override
    public void setStopOnInteract(boolean stopOnInteract) {
        this.stopAndInteract = stopOnInteract;
    }

    @Override
    public int getWalkingSpeed() {
        return this.moveSpeed;
    }

    @Override
    public void setWalkingSpeed(int speed) {
        if (speed >= 0 && speed <= 10) {
            this.moveSpeed = speed;
            this.npc.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue((double) this.npc.getSpeed());
            this.npc.m_21051_(Attributes.FLYING_SPEED).setBaseValue((double) (this.npc.getSpeed() * 2.0F));
        } else {
            throw new CustomNPCsException("Wrong speed: " + speed);
        }
    }

    @Override
    public int getMovingPathType() {
        return this.movingPattern;
    }

    @Override
    public boolean getMovingPathPauses() {
        return this.movingPause;
    }

    @Override
    public void setMovingPathType(int type, boolean pauses) {
        if (type < 0 && type > 1) {
            throw new CustomNPCsException("Moving path type: " + type);
        } else {
            this.movingPattern = type;
            this.movingPause = pauses;
        }
    }

    @Override
    public int getDoorInteract() {
        return this.doorInteract;
    }

    @Override
    public void setDoorInteract(int type) {
        this.doorInteract = type;
        this.npc.updateAI = true;
    }

    @Override
    public boolean getCanSwim() {
        return this.canSwim;
    }

    @Override
    public void setCanSwim(boolean canSwim) {
        this.canSwim = canSwim;
    }

    @Override
    public int getSheltersFrom() {
        return this.findShelter;
    }

    @Override
    public void setSheltersFrom(int type) {
        this.findShelter = type;
        this.npc.updateAI = true;
    }

    @Override
    public boolean getAttackLOS() {
        return this.directLOS;
    }

    @Override
    public void setAttackLOS(boolean enabled) {
        this.directLOS = enabled;
        this.npc.updateAI = true;
    }

    @Override
    public boolean getAvoidsWater() {
        return this.avoidsWater;
    }

    @Override
    public void setAvoidsWater(boolean enabled) {
        this.npc.m_21441_(BlockPathTypes.WATER, this.movementType != 2 && enabled ? -1.0F : 0.0F);
        this.avoidsWater = enabled;
    }

    @Override
    public boolean getLeapAtTarget() {
        return this.canLeap;
    }

    @Override
    public void setLeapAtTarget(boolean leap) {
        this.canLeap = leap;
        this.npc.updateAI = true;
    }

    @Override
    public int getNavigationType() {
        return this.movementType;
    }

    @Override
    public void setNavigationType(int type) {
        this.movementType = type;
    }
}