package io.redspace.ironsspellbooks.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistry;

public class SpellFilter {

    SchoolType schoolType = null;

    List<AbstractSpell> spells = new ArrayList();

    static final LazyOptional<List<AbstractSpell>> DEFAULT_SPELLS = LazyOptional.of(() -> ((IForgeRegistry) SpellRegistry.REGISTRY.get()).getValues().stream().filter(AbstractSpell::allowLooting).toList());

    static final LazyOptional<Map<SchoolType, List<AbstractSpell>>> SPELLS_FOR_SCHOOL = LazyOptional.of(() -> (Map<SchoolType, List<AbstractSpell>>) ((IForgeRegistry) SchoolRegistry.REGISTRY.get()).getValues().stream().collect(Collectors.toMap(school -> school, school -> SpellRegistry.getSpellsForSchool(school).stream().filter(AbstractSpell::allowLooting).toList())));

    public SpellFilter(SchoolType schoolType) {
        this.schoolType = schoolType;
    }

    public SpellFilter(List<AbstractSpell> spells) {
        this.spells = spells;
    }

    public SpellFilter() {
    }

    public boolean isFiltered() {
        return this.schoolType != null || !this.spells.isEmpty();
    }

    public List<AbstractSpell> getApplicableSpells() {
        if (!this.spells.isEmpty()) {
            return this.spells;
        } else {
            if (this.schoolType != null) {
                List<AbstractSpell> spells = (List<AbstractSpell>) ((Map) SPELLS_FOR_SCHOOL.resolve().get()).get(this.schoolType);
                if (!spells.isEmpty()) {
                    return spells;
                }
            } else {
                List<AbstractSpell> spells = (List<AbstractSpell>) DEFAULT_SPELLS.resolve().get();
                if (!spells.isEmpty()) {
                    return spells;
                }
            }
            return List.of(SpellRegistry.none());
        }
    }

    public AbstractSpell getRandomSpell(RandomSource random, Predicate<AbstractSpell> filter) {
        List<AbstractSpell> spells = this.getApplicableSpells().stream().filter(filter).toList();
        return (AbstractSpell) (spells.isEmpty() ? SpellRegistry.none() : (AbstractSpell) spells.get(random.nextInt(spells.size())));
    }

    public AbstractSpell getRandomSpell(RandomSource randomSource) {
        return this.getRandomSpell(randomSource, spell -> spell.isEnabled() && spell != SpellRegistry.none() && spell.allowLooting());
    }

    public static SpellFilter deserializeSpellFilter(JsonObject json) {
        if (GsonHelper.isValidNode(json, "school")) {
            String schoolType = GsonHelper.getAsString(json, "school");
            return new SpellFilter(SchoolRegistry.getSchool(new ResourceLocation(schoolType)));
        } else if (GsonHelper.isArrayNode(json, "spells")) {
            JsonArray spellsFromJson = GsonHelper.getAsJsonArray(json, "spells");
            List<AbstractSpell> applicableSpellList = new ArrayList();
            for (JsonElement element : spellsFromJson) {
                String spellId = element.getAsString();
                AbstractSpell spell = SpellRegistry.getSpell(spellId);
                if (spell != SpellRegistry.none()) {
                    applicableSpellList.add(spell);
                }
            }
            return new SpellFilter(applicableSpellList);
        } else {
            return new SpellFilter();
        }
    }

    public void serialize(JsonObject json) {
        if (this.schoolType != null) {
            json.addProperty("school", this.schoolType.getId().toString());
        } else if (!this.spells.isEmpty()) {
            JsonArray elements = new JsonArray();
            for (AbstractSpell spell : this.spells) {
                elements.add(spell.getSpellId());
            }
            json.add("spells", elements);
        }
    }
}