package com.mna.capabilities.playerdata.rote;

import com.mna.Registries;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.events.MasteryGainedEvent;
import com.mna.api.events.RoteProgressGainedEvent;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.tools.math.MathUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.IForgeRegistry;

public class PlayerRoteSpells implements IPlayerRoteSpells {

    public static final float MAX_MASTERY = 0.5F;

    private HashMap<ISpellComponent, Float> _roteParts;

    private HashMap<ISpellComponent, Float> _mastery;

    private boolean isDirty = true;

    public PlayerRoteSpells() {
        this._roteParts = new HashMap();
        this._mastery = new HashMap();
    }

    @Override
    public boolean isRote(ISpellComponent part) {
        return !this._roteParts.containsKey(part) ? false : (Float) this._roteParts.get(part) >= (float) part.requiredXPForRote();
    }

    @Override
    public boolean addRoteXP(Player player, ISpellComponent part) {
        return this.addRoteXP(player, part, 1.0F);
    }

    @Override
    public boolean addRoteXP(Player player, ISpellComponent part, float xp) {
        if (!this.isRote(part) && part != null) {
            if (player != null) {
                RoteProgressGainedEvent evt = new RoteProgressGainedEvent(player, part, xp);
                if (MinecraftForge.EVENT_BUS.post(evt)) {
                    return false;
                }
                xp = evt.getAmount();
            }
            if (!this._roteParts.containsKey(part)) {
                this._roteParts.put(part, xp);
            } else {
                this._roteParts.put(part, Math.max((Float) this._roteParts.get(part) + xp, 0.0F));
            }
            this.setDirty();
            return this.isRote(part);
        } else {
            return false;
        }
    }

    @Override
    public void setRoteXP(ResourceLocation rLoc, float xp) {
        ISpellComponent comp = this.lookupSpellComponent(rLoc);
        if (comp != null) {
            this._roteParts.put(comp, xp);
        }
    }

    @Override
    public void resetRote() {
        this._roteParts.clear();
        this.setDirty();
    }

    @Override
    public void resetMastery() {
        this._mastery.clear();
        this.setDirty();
    }

    private ISpellComponent lookupSpellComponent(ResourceLocation rLoc) {
        if (rLoc == null) {
            return null;
        } else if (((IForgeRegistry) Registries.Shape.get()).containsKey(rLoc)) {
            return (ISpellComponent) ((IForgeRegistry) Registries.Shape.get()).getValue(rLoc);
        } else if (((IForgeRegistry) Registries.SpellEffect.get()).containsKey(rLoc)) {
            return (ISpellComponent) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(rLoc);
        } else {
            return ((IForgeRegistry) Registries.Modifier.get()).containsKey(rLoc) ? (ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(rLoc) : null;
        }
    }

    @Override
    public List<Shape> getRoteShapes() {
        return (List<Shape>) this._roteParts.entrySet().stream().filter(e -> e.getKey() instanceof Shape && (Float) e.getValue() >= (float) ((ISpellComponent) e.getKey()).requiredXPForRote()).map(e -> (Shape) e.getKey()).collect(Collectors.toList());
    }

    @Override
    public List<SpellEffect> getRoteComponents() {
        return (List<SpellEffect>) this._roteParts.entrySet().stream().filter(e -> e.getKey() instanceof SpellEffect && (Float) e.getValue() >= (float) ((ISpellComponent) e.getKey()).requiredXPForRote()).map(e -> (SpellEffect) e.getKey()).collect(Collectors.toList());
    }

    @Override
    public List<Modifier> getRoteModifiers() {
        return (List<Modifier>) this._roteParts.entrySet().stream().filter(e -> e.getKey() instanceof Modifier && (Float) e.getValue() >= (float) ((ISpellComponent) e.getKey()).requiredXPForRote()).map(e -> (Modifier) e.getKey()).collect(Collectors.toList());
    }

    @Override
    public HashMap<ISpellComponent, Float> getRoteData() {
        return this._roteParts;
    }

    @Override
    public boolean isDirty() {
        return this.isDirty;
    }

    @Override
    public void setDirty() {
        this.isDirty = true;
    }

    @Override
    public void clearDirty() {
        this.isDirty = false;
    }

    @Override
    public void copyFrom(IPlayerRoteSpells other) {
        this._roteParts = new HashMap();
        for (Entry<ISpellComponent, Float> p : other.getRoteData().entrySet()) {
            this._roteParts.put((ISpellComponent) p.getKey(), (Float) p.getValue());
        }
        this._mastery = new HashMap();
        for (Entry<ISpellComponent, Float> p : other.getMasteryData().entrySet()) {
            this._mastery.put((ISpellComponent) p.getKey(), (Float) p.getValue());
        }
        this.setDirty();
    }

    @Override
    public float getRoteProgression(ISpellComponent spellPart) {
        return !this._roteParts.containsKey(spellPart) ? 0.0F : MathUtils.clamp01((Float) this._roteParts.get(spellPart) / (float) spellPart.requiredXPForRote());
    }

    @Override
    public float getMastery(ISpellComponent part) {
        return (Float) this._mastery.getOrDefault(part, 0.0F);
    }

    @Override
    public boolean addMastery(Player player, ISpellComponent part, float amount) {
        float existing = (Float) this._mastery.getOrDefault(part, 0.0F);
        float amt = existing + amount;
        if (player != null) {
            MasteryGainedEvent evt = new MasteryGainedEvent(player, part, amt);
            if (MinecraftForge.EVENT_BUS.post(evt)) {
                return false;
            }
            amt = evt.getAmount();
        }
        existing = MathUtils.clamp(amt, 0.0F, 0.5F);
        this._mastery.put(part, existing);
        this.setDirty();
        return true;
    }

    @Override
    public HashMap<ISpellComponent, Float> getMasteryData() {
        return this._mastery;
    }

    @Override
    public void setMastery(ResourceLocation identifier, float amount) {
        ISpellComponent part = this.lookupSpellComponent(identifier);
        if (part != null) {
            this._mastery.put(part, MathUtils.clamp(amount, 0.0F, 0.5F));
            this.setDirty();
        }
    }
}