package io.redspace.ironsspellbooks.capabilities.magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;

public class ClientSpellTargetingData {

    public ArrayList<UUID> targetUUIDs = new ArrayList();

    public String spellId;

    public ClientSpellTargetingData() {
    }

    public ClientSpellTargetingData(String spellId, UUID... targetUUID) {
        this();
        this.targetUUIDs.addAll(Arrays.asList(targetUUID));
        this.spellId = spellId;
    }

    public ClientSpellTargetingData(String spellId, List<UUID> uuids) {
        this();
        this.targetUUIDs.addAll(uuids);
        this.spellId = spellId;
    }

    public boolean isTargeted(LivingEntity livingEntity) {
        return this.targetUUIDs.contains(livingEntity.m_20148_());
    }

    public boolean isTargeted(UUID uuid) {
        return this.targetUUIDs.contains(uuid);
    }

    public String toString() {
        return this.targetUUIDs.toString();
    }
}