package yesman.epicfight.api.forgeevent;

import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;

public class SkillBuildEvent extends Event {

    protected final Map<ResourceLocation, Pair<? extends Skill.Builder<?>, Function<? extends Skill.Builder<?>, ? extends Skill>>> builders;

    protected final Map<ResourceLocation, Skill> skills;

    protected final Map<ResourceLocation, Skill> learnableSkills;

    public SkillBuildEvent(Map<ResourceLocation, Pair<? extends Skill.Builder<?>, Function<? extends Skill.Builder<?>, ? extends Skill>>> builders, Map<ResourceLocation, Skill> skills, Map<ResourceLocation, Skill> learnableSkills) {
        this.builders = builders;
        this.skills = skills;
        this.learnableSkills = learnableSkills;
    }

    public <T extends Skill, B extends Skill.Builder<T>> T build(String modid, String name) {
        try {
            ResourceLocation registryName = new ResourceLocation(modid, name);
            Pair<B, Function<B, T>> pair = (Pair<B, Function<B, T>>) this.builders.get(registryName);
            if (pair != null) {
                T skill = (T) ((Function) pair.getSecond()).apply((Skill.Builder) pair.getFirst());
                if (skill != null) {
                    this.skills.put(registryName, skill);
                    if (skill.getCategory().learnable()) {
                        this.learnableSkills.put(registryName, skill);
                    }
                }
                return skill;
            } else {
                if (this.builders.containsKey(registryName)) {
                    EpicFightMod.LOGGER.warn("Invalid builder registered for skill " + registryName);
                } else {
                    EpicFightMod.LOGGER.warn("Can't find the skill " + registryName + " in the registry. Here's all registered skills.");
                    for (Entry<ResourceLocation, Pair<? extends Skill.Builder<?>, Function<? extends Skill.Builder<?>, ? extends Skill>>> rl : this.builders.entrySet()) {
                        EpicFightMod.LOGGER.warn(rl);
                    }
                }
                throw new IllegalStateException("Illegal skill registry: " + registryName);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }
}