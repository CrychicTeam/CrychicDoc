package com.mna.entities.constructs.animated;

import com.mna.api.entities.construct.IConstructDiagnostics;
import java.util.LinkedList;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ConstructDiagnostics implements IConstructDiagnostics {

    private static final int MAX_QUEUE_SIZE = 20;

    private static final int MAX_TASK_HISTORY_SIZE = 3;

    private static final int DIRTY_POS = 1;

    private static final int DIRTY_QUEUE = 2;

    private static final int DIRTY_HISTORY = 4;

    private int dirtyFlag = 0;

    private LinkedList<ConstructDiagnostics.LogEntry> diagnosticQueue = new LinkedList();

    private LinkedList<ConstructDiagnostics.TaskHistoryEntry> taskHistory = new LinkedList();

    private Vec3 movePos;

    @Override
    public void pushDiagnosticMessage(String message, @Nullable ResourceLocation guiIcon, boolean allowDuplicates) {
        ConstructDiagnostics.LogEntry insert = new ConstructDiagnostics.LogEntry(message, guiIcon);
        this.pushDiagnosticMessage(insert, allowDuplicates);
    }

    private void pushDiagnosticMessage(ConstructDiagnostics.LogEntry insert, boolean allowDuplicates) {
        ConstructDiagnostics.LogEntry last = (ConstructDiagnostics.LogEntry) this.diagnosticQueue.peekLast();
        if (allowDuplicates || last == null || !last.equals(insert)) {
            this.diagnosticQueue.add(insert);
            this.dirtyFlag |= 2;
        }
        while (this.diagnosticQueue.size() > 20) {
            this.diagnosticQueue.pop();
        }
    }

    @Override
    public void pushTaskUpdate(String taskID, ResourceLocation taskIcon, IConstructDiagnostics.Status status, Vec3 pos) {
        if (taskID != null) {
            if (this.getOrCreateTask(taskID, taskIcon).update(status, pos)) {
                this.dirtyFlag |= 4;
            }
        }
    }

    @Override
    public void pushTaskUpdate(String taskID, ResourceLocation taskIcon, IConstructDiagnostics.Status status, AABB area) {
        if (taskID != null) {
            if (this.getOrCreateTask(taskID, taskIcon).update(status, area)) {
                this.dirtyFlag |= 4;
            }
        }
    }

    @Override
    public void pushTaskUpdate(String taskID, ResourceLocation taskIcon, IConstructDiagnostics.Status status, int entityID) {
        if (taskID != null) {
            if (this.getOrCreateTask(taskID, taskIcon).update(status, entityID)) {
                this.dirtyFlag |= 4;
            }
        }
    }

    @Override
    public void setMovePos(Vec3 target) {
        if (this.movePos != target) {
            this.movePos = target;
            this.dirtyFlag |= 1;
        }
    }

    @Override
    public boolean needsUpdate() {
        return this.dirtyFlag != 0;
    }

    private void resetDirty() {
        this.dirtyFlag = 0;
    }

    private ConstructDiagnostics.TaskHistoryEntry getOrCreateTask(String taskID, ResourceLocation taskIcon) {
        Optional<ConstructDiagnostics.TaskHistoryEntry> existing = this.taskHistory.stream().filter(entry -> entry.taskID.equals(taskID)).findFirst();
        if (existing.isPresent()) {
            return (ConstructDiagnostics.TaskHistoryEntry) existing.get();
        } else {
            ConstructDiagnostics.TaskHistoryEntry insert = new ConstructDiagnostics.TaskHistoryEntry(taskID, taskIcon);
            this.taskHistory.push(insert);
            while (this.taskHistory.size() > 6) {
                this.taskHistory.removeLast();
            }
            return insert;
        }
    }

    public LinkedList<ConstructDiagnostics.LogEntry> getMessages() {
        return this.diagnosticQueue;
    }

    public LinkedList<ConstructDiagnostics.TaskHistoryEntry> getTaskHistory() {
        return this.taskHistory;
    }

    public Vec3 getMovePos() {
        return this.movePos;
    }

    public CompoundTag writeToNBT() {
        CompoundTag tag = new CompoundTag();
        if ((this.dirtyFlag & 4) != 0) {
            ListTag th = new ListTag();
            for (int i = this.taskHistory.size() - 1; i >= 0; i--) {
                ConstructDiagnostics.TaskHistoryEntry the = (ConstructDiagnostics.TaskHistoryEntry) this.taskHistory.get(i);
                if (the != null) {
                    th.add(the.toTag());
                }
            }
            tag.put("th", th);
        }
        if ((this.dirtyFlag & 2) != 0) {
            ListTag dq = new ListTag();
            for (ConstructDiagnostics.LogEntry s : this.diagnosticQueue) {
                dq.add(s.toTag());
            }
            tag.put("dq", dq);
        }
        if ((this.dirtyFlag & 1) != 0 && this.movePos != null) {
            tag.putDouble("mpx", this.movePos.x);
            tag.putDouble("mpy", this.movePos.y);
            tag.putDouble("mpz", this.movePos.z);
        }
        this.resetDirty();
        return tag;
    }

    public void readFromNBT(CompoundTag tag) {
        if (tag.contains("th")) {
            this.taskHistory.clear();
            ListTag dqList = tag.getList("th", 10);
            if (dqList.getElementType() == 10) {
                dqList.forEach(n -> {
                    ConstructDiagnostics.TaskHistoryEntry entry = ConstructDiagnostics.TaskHistoryEntry.fromTag((CompoundTag) n);
                    if (entry != null) {
                        this.taskHistory.add(entry);
                    }
                });
            }
        }
        if (tag.contains("dq")) {
            ListTag dqList = tag.getList("dq", 10);
            this.diagnosticQueue.clear();
            if (dqList.getElementType() == 10) {
                dqList.forEach(n -> {
                    ConstructDiagnostics.LogEntry log = ConstructDiagnostics.LogEntry.fromTag((CompoundTag) n);
                    if (log != null) {
                        this.pushDiagnosticMessage(log, true);
                    }
                });
            }
        }
        if (tag.contains("mpx") && tag.contains("mpy") && tag.contains("mpz")) {
            this.movePos = new Vec3(tag.getDouble("mpx"), tag.getDouble("mpy"), tag.getDouble("mpz"));
        }
    }

    public static class LogEntry {

        public final String message;

        @Nullable
        public final ResourceLocation icon;

        public LogEntry(String message, @Nullable ResourceLocation icon) {
            this.message = message;
            this.icon = icon;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ConstructDiagnostics.LogEntry other)) {
                return false;
            } else {
                return this.icon == null ? other.message.equals(this.message) && other.icon == null : other.message.equals(this.message) && other.icon != null && other.icon.equals(this.icon);
            }
        }

        public int hashCode() {
            int hash = this.message.hashCode();
            if (this.icon != null) {
                hash += this.icon.hashCode();
            }
            return hash;
        }

        public CompoundTag toTag() {
            CompoundTag tag = new CompoundTag();
            tag.putString("message", this.message);
            if (this.icon != null) {
                tag.putString("icon", this.icon.toString());
            }
            return tag;
        }

        public static ConstructDiagnostics.LogEntry fromTag(CompoundTag tag) {
            if (tag.contains("message")) {
                ResourceLocation rLoc = null;
                if (tag.contains("icon")) {
                    rLoc = new ResourceLocation(tag.getString("icon"));
                }
                return new ConstructDiagnostics.LogEntry(tag.getString("message"), rLoc);
            } else {
                return null;
            }
        }
    }

    public static class TaskHistoryEntry {

        private final String taskID;

        private final ResourceLocation taskIcon;

        private IConstructDiagnostics.Status status = IConstructDiagnostics.Status.RUNNING;

        private int entityID = -1;

        private AABB area = null;

        private Vec3 pos = null;

        public TaskHistoryEntry(String taskID, ResourceLocation taskIcon) {
            this.taskID = taskID;
            this.taskIcon = taskIcon;
        }

        public boolean update(IConstructDiagnostics.Status status, int entityID) {
            if (this.status == status && this.entityID == entityID) {
                return false;
            } else {
                this.status = status;
                this.entityID = entityID;
                return true;
            }
        }

        public boolean update(IConstructDiagnostics.Status status, Vec3 pos) {
            if (this.status == status && this.pos == pos) {
                return false;
            } else {
                this.status = status;
                this.pos = pos;
                return true;
            }
        }

        public boolean update(IConstructDiagnostics.Status status, AABB area) {
            if (this.status == status && this.area == area) {
                return false;
            } else {
                this.status = status;
                this.area = area;
                return true;
            }
        }

        public ResourceLocation getIcon() {
            return this.taskIcon;
        }

        public IConstructDiagnostics.Status getStatus() {
            return this.status;
        }

        public int getEntityID() {
            return this.entityID;
        }

        @Nullable
        public AABB getArea() {
            return this.area;
        }

        @Nullable
        public Vec3 getPos() {
            return this.pos;
        }

        public CompoundTag toTag() {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", this.taskID);
            tag.putString("icon", this.taskIcon.toString());
            tag.putInt("status", this.status.ordinal());
            if (this.entityID > 0) {
                tag.putInt("entityID", this.entityID);
            }
            if (this.area != null) {
                tag.putDouble("area_lx", this.area.minX);
                tag.putDouble("area_ly", this.area.minY);
                tag.putDouble("area_lz", this.area.minZ);
                tag.putDouble("area_ux", this.area.maxX);
                tag.putDouble("area_uy", this.area.maxY);
                tag.putDouble("area_uz", this.area.maxZ);
            }
            if (this.pos != null) {
                tag.putDouble("pos_x", this.pos.x);
                tag.putDouble("pos_y", this.pos.y);
                tag.putDouble("pos_z", this.pos.z);
            }
            return tag;
        }

        public static ConstructDiagnostics.TaskHistoryEntry fromTag(CompoundTag tag) {
            if (tag.contains("id") && tag.contains("icon")) {
                ConstructDiagnostics.TaskHistoryEntry created = new ConstructDiagnostics.TaskHistoryEntry(tag.getString("id"), new ResourceLocation(tag.getString("icon")));
                if (tag.contains("status")) {
                    created.status = IConstructDiagnostics.Status.values()[tag.getInt("status") % IConstructDiagnostics.Status.values().length];
                }
                if (tag.contains("entityID")) {
                    created.entityID = tag.getInt("entityID");
                }
                if (tag.contains("area_lx") && tag.contains("area_ly") && tag.contains("area_lz") && tag.contains("area_ux") && tag.contains("area_uy") && tag.contains("area_uz")) {
                    Vec3 start = new Vec3(tag.getDouble("area_lx"), tag.getDouble("area_ly"), tag.getDouble("area_lz"));
                    Vec3 end = new Vec3(tag.getDouble("area_ux"), tag.getDouble("area_uy"), tag.getDouble("area_uz"));
                    created.area = new AABB(start, end);
                }
                if (tag.contains("pos_x") && tag.contains("pos_y") && tag.contains("pos_z")) {
                    created.pos = new Vec3(tag.getDouble("pos_x"), tag.getDouble("pos_y"), tag.getDouble("pos_z"));
                }
                return created;
            } else {
                return null;
            }
        }

        public float[] colorFromStatusCode() {
            switch(this.status) {
                case FAILURE:
                    return new float[] { 1.0F, 0.0F, 0.0F };
                case SUCCESS:
                    return new float[] { 0.0F, 1.0F, 0.0F };
                default:
                    return new float[] { 1.0F, 1.0F, 0.0F };
            }
        }
    }
}