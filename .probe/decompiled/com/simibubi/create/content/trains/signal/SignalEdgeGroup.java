package com.simibubi.create.content.trains.signal;

import com.google.common.base.Predicates;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.mutable.MutableInt;

public class SignalEdgeGroup {

    public UUID id;

    public EdgeGroupColor color;

    public Set<Train> trains;

    public SignalBoundary reserved;

    public Map<UUID, UUID> intersecting;

    public Set<SignalEdgeGroup> intersectingResolved;

    public Set<UUID> adjacent;

    public boolean fallbackGroup;

    public SignalEdgeGroup(UUID id) {
        this.id = id;
        this.trains = new HashSet();
        this.adjacent = new HashSet();
        this.intersecting = new HashMap();
        this.intersectingResolved = new HashSet();
        this.color = EdgeGroupColor.getDefault();
    }

    public SignalEdgeGroup asFallback() {
        this.fallbackGroup = true;
        return this;
    }

    public boolean isOccupiedUnless(Train train) {
        if (this.intersectingResolved.isEmpty()) {
            this.walkIntersecting(this.intersectingResolved::add);
        }
        for (SignalEdgeGroup group : this.intersectingResolved) {
            if (group.isThisOccupiedUnless(train)) {
                return true;
            }
        }
        return false;
    }

    private boolean isThisOccupiedUnless(Train train) {
        return this.reserved != null || this.trains.size() > 1 || !this.trains.contains(train) && !this.trains.isEmpty();
    }

    public boolean isOccupiedUnless(SignalBoundary boundary) {
        if (this.intersectingResolved.isEmpty()) {
            this.walkIntersecting(this.intersectingResolved::add);
        }
        for (SignalEdgeGroup group : this.intersectingResolved) {
            if (group.isThisOccupiedUnless(boundary)) {
                return true;
            }
        }
        return false;
    }

    private boolean isThisOccupiedUnless(SignalBoundary boundary) {
        return !this.trains.isEmpty() || this.reserved != null && this.reserved != boundary;
    }

    public void putIntersection(UUID intersectionId, UUID targetGroup) {
        this.intersecting.put(intersectionId, targetGroup);
        this.walkIntersecting(g -> g.intersectingResolved.clear());
        this.resolveColor();
    }

    public void removeIntersection(UUID intersectionId) {
        this.walkIntersecting(g -> g.intersectingResolved.clear());
        UUID removed = (UUID) this.intersecting.remove(intersectionId);
        SignalEdgeGroup other = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(removed);
        if (other != null) {
            other.intersecting.remove(intersectionId);
        }
        this.resolveColor();
    }

    public void putAdjacent(UUID adjacent) {
        this.adjacent.add(adjacent);
    }

    public void removeAdjacent(UUID adjacent) {
        this.adjacent.remove(adjacent);
    }

    public void resolveColor() {
        if (this.intersectingResolved.isEmpty()) {
            this.walkIntersecting(this.intersectingResolved::add);
        }
        MutableInt mask = new MutableInt(0);
        this.intersectingResolved.forEach(group -> group.adjacent.stream().map(Create.RAILWAYS.signalEdgeGroups::get).filter(Objects::nonNull).filter(Predicates.not(this.intersectingResolved::contains)).forEach(adjacent -> mask.setValue(adjacent.color.strikeFrom(mask.getValue()))));
        EdgeGroupColor newColour = EdgeGroupColor.findNextAvailable(mask.getValue());
        if (newColour != this.color) {
            this.walkIntersecting(group -> Create.RAILWAYS.sync.edgeGroupCreated(group.id, group.color = newColour));
            Create.RAILWAYS.markTracksDirty();
        }
    }

    private void walkIntersecting(Consumer<SignalEdgeGroup> callback) {
        this.walkIntersectingRec(new HashSet(), callback);
    }

    private void walkIntersectingRec(Set<SignalEdgeGroup> visited, Consumer<SignalEdgeGroup> callback) {
        if (visited.add(this)) {
            callback.accept(this);
            for (UUID uuid : this.intersecting.values()) {
                SignalEdgeGroup group = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(uuid);
                if (group != null) {
                    group.walkIntersectingRec(visited, callback);
                }
            }
        }
    }

    public static SignalEdgeGroup read(CompoundTag tag) {
        SignalEdgeGroup group = new SignalEdgeGroup(tag.getUUID("Id"));
        group.color = NBTHelper.readEnum(tag, "Color", EdgeGroupColor.class);
        NBTHelper.iterateCompoundList(tag.getList("Connected", 10), nbt -> group.intersecting.put(nbt.getUUID("Key"), nbt.getUUID("Value")));
        group.fallbackGroup = tag.getBoolean("Fallback");
        return group;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", this.id);
        NBTHelper.writeEnum(tag, "Color", this.color);
        tag.put("Connected", NBTHelper.writeCompoundList(this.intersecting.entrySet(), e -> {
            CompoundTag nbt = new CompoundTag();
            nbt.putUUID("Key", (UUID) e.getKey());
            nbt.putUUID("Value", (UUID) e.getValue());
            return nbt;
        }));
        tag.putBoolean("Fallback", this.fallbackGroup);
        return tag;
    }
}