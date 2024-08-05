package io.redspace.ironsspellbooks.capabilities.magic;

import com.google.common.collect.Maps;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.network.ClientboundSyncCooldowns;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

public class PlayerCooldowns {

    public static final String SPELL_ID = "id";

    public static final String SPELL_COOLDOWN = "scd";

    public static final String COOLDOWN_REMAINING = "cdr";

    private final Map<String, CooldownInstance> spellCooldowns;

    private int tickBuffer = 0;

    public PlayerCooldowns() {
        this.spellCooldowns = Maps.newHashMap();
    }

    public void setTickBuffer(int tickBuffer) {
        this.tickBuffer = tickBuffer;
    }

    public void tick(int actualTicks) {
        List<Entry<String, CooldownInstance>> spells = this.spellCooldowns.entrySet().stream().filter(x -> this.decrementCooldown((CooldownInstance) x.getValue(), actualTicks)).toList();
        spells.forEach(spell -> this.spellCooldowns.remove(spell.getKey()));
    }

    public boolean hasCooldownsActive() {
        return !this.spellCooldowns.isEmpty();
    }

    public Map<String, CooldownInstance> getSpellCooldowns() {
        return this.spellCooldowns;
    }

    public boolean removeCooldown(String spellId) {
        return this.spellCooldowns.remove(spellId) != null;
    }

    public void clearCooldowns() {
        this.spellCooldowns.clear();
    }

    public void addCooldown(AbstractSpell spell, int durationTicks) {
        this.spellCooldowns.put(spell.getSpellId(), new CooldownInstance(durationTicks));
    }

    public void addCooldown(AbstractSpell spell, int durationTicks, int remaining) {
        this.spellCooldowns.put(spell.getSpellId(), new CooldownInstance(durationTicks, remaining));
    }

    public void addCooldown(String spellID, int durationTicks) {
        this.spellCooldowns.put(spellID, new CooldownInstance(durationTicks));
    }

    public void addCooldown(String spellID, int durationTicks, int remaining) {
        this.spellCooldowns.put(spellID, new CooldownInstance(durationTicks, remaining));
    }

    public boolean isOnCooldown(AbstractSpell spell) {
        return this.spellCooldowns.containsKey(spell.getSpellId());
    }

    public float getCooldownPercent(AbstractSpell spell) {
        return ((CooldownInstance) this.spellCooldowns.getOrDefault(spell.getSpellId(), new CooldownInstance(0))).getCooldownPercent();
    }

    public boolean decrementCooldown(CooldownInstance c, int amount) {
        c.decrementBy(amount);
        return c.getCooldownRemaining() <= this.tickBuffer;
    }

    public void syncToPlayer(ServerPlayer serverPlayer) {
        Messages.sendToPlayer(new ClientboundSyncCooldowns(this.spellCooldowns), serverPlayer);
    }

    public ListTag saveNBTData() {
        ListTag listTag = new ListTag();
        this.spellCooldowns.forEach((spellId, cooldown) -> {
            if (cooldown.getCooldownRemaining() > 0) {
                CompoundTag ct = new CompoundTag();
                ct.putString("id", spellId);
                ct.putInt("scd", cooldown.getSpellCooldown());
                ct.putInt("cdr", cooldown.getCooldownRemaining());
                listTag.add(ct);
            }
        });
        return listTag;
    }

    public void loadNBTData(ListTag listTag) {
        if (listTag != null) {
            listTag.forEach(tag -> {
                CompoundTag t = (CompoundTag) tag;
                String spellId = t.getString("id");
                int spellCooldown = t.getInt("scd");
                int cooldownRemaining = t.getInt("cdr");
                this.spellCooldowns.put(spellId, new CooldownInstance(spellCooldown, cooldownRemaining));
            });
        }
    }
}