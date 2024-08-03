package harmonised.pmmo.config.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;

public record SkillData(Optional<Integer> color, Optional<Boolean> afkExempt, Optional<Boolean> displayGroupName, Optional<Boolean> useTotalLevels, Optional<Map<String, Double>> groupedSkills, Optional<Integer> maxLevel, Optional<ResourceLocation> icon, Optional<Integer> iconSize) {

    public static Codec<SkillData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.optionalFieldOf("color").forGetter(SkillData::color), Codec.BOOL.optionalFieldOf("noAfkPenalty").forGetter(SkillData::afkExempt), Codec.BOOL.optionalFieldOf("displayGroupName").forGetter(SkillData::displayGroupName), Codec.BOOL.optionalFieldOf("useTotalLevels").forGetter(SkillData::useTotalLevels), CodecTypes.DOUBLE_CODEC.optionalFieldOf("groupFor").forGetter(SkillData::groupedSkills), Codec.INT.optionalFieldOf("maxLevel").forGetter(SkillData::maxLevel), ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(SkillData::icon), Codec.INT.optionalFieldOf("iconSize").forGetter(SkillData::iconSize)).apply(instance, SkillData::new));

    public int getColor() {
        return (Integer) this.color.orElse(16777215);
    }

    public boolean getAfkExempt() {
        return (Boolean) this.afkExempt.orElse(false);
    }

    public boolean getDisplayGroupName() {
        return (Boolean) this.displayGroupName.orElse(false);
    }

    public boolean getUseTotalLevels() {
        return (Boolean) this.useTotalLevels.orElse(false);
    }

    public int getMaxLevel() {
        return (Integer) this.maxLevel.orElse(Config.MAX_LEVEL.get());
    }

    public ResourceLocation getIcon() {
        return (ResourceLocation) this.icon.orElse(new ResourceLocation("pmmo", "textures/skills/missing_icon.png"));
    }

    public int getIconSize() {
        return (Integer) this.iconSize.orElse(18);
    }

    public boolean isSkillGroup() {
        return !this.getGroup().isEmpty();
    }

    public Map<String, Double> getGroup() {
        return (Map<String, Double>) this.groupedSkills.orElse(new HashMap());
    }

    public Map<String, Long> getGroupXP(long xp) {
        Map<String, Long> outMap = new HashMap();
        double denominator = this.getGroup().values().stream().mapToDouble(value -> value).sum();
        this.getGroup().forEach((skill, ratio) -> outMap.put(skill, Double.valueOf(ratio / denominator * (double) xp).longValue()));
        new HashMap(outMap).forEach((skill, value) -> {
            SkillData skillCheck = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault());
            if (skillCheck.isSkillGroup()) {
                outMap.remove(skill);
                skillCheck.getGroupXP(value).forEach((s, x) -> outMap.merge(s, x, Long::sum));
            }
        });
        return outMap;
    }

    public Map<String, Integer> getGroupReq(int level) {
        Map<String, Integer> outMap = new HashMap();
        double denominator = this.getGroup().values().stream().mapToDouble(value -> value).sum();
        this.getGroup().forEach((skill, ratio) -> outMap.put(skill, (int) (ratio / denominator * (double) level)));
        new HashMap(outMap).forEach((skill, value) -> {
            SkillData skillCheck = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault());
            if (skillCheck.isSkillGroup()) {
                outMap.remove(skill);
                skillCheck.getGroupReq(value).forEach((s, x) -> outMap.merge(s, x, Integer::sum));
            }
        });
        return outMap;
    }

    public Map<String, Double> getGroupBonus(double bonus) {
        Map<String, Double> outMap = new HashMap();
        double denominator = this.getGroup().values().stream().mapToDouble(value -> value).sum();
        double gainLossModifier = bonus >= 1.0 ? 1.0 : 0.0;
        this.getGroup().forEach((skill, ratio) -> outMap.put(skill, gainLossModifier + ratio / denominator * bonus));
        new HashMap(outMap).forEach((skill, value) -> {
            SkillData skillCheck = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault());
            if (skillCheck.isSkillGroup()) {
                outMap.remove(skill);
                skillCheck.getGroupBonus(value).forEach((s, x) -> outMap.merge(s, x, Double::sum));
            }
        });
        return outMap;
    }

    public static class Builder {

        int color = 16777215;

        int maxLevel = Integer.MAX_VALUE;

        int iconSize;

        boolean afkExempt = false;

        boolean displayName = false;

        boolean useTotal = false;

        ResourceLocation icon = new ResourceLocation("pmmo", "textures/skills/missing_icon.png");

        Map<String, Double> groupOf;

        private Builder() {
            this.iconSize = 18;
            this.groupOf = new HashMap();
        }

        public static SkillData getDefault() {
            return new SkillData(Optional.of(16777215), Optional.of(false), Optional.of(false), Optional.of(false), Optional.empty(), Optional.of(Config.MAX_LEVEL.get()), Optional.of(new ResourceLocation("pmmo", "textures/skills/missing_icon.png")), Optional.of(18));
        }

        public static SkillData.Builder start() {
            return new SkillData.Builder();
        }

        public SkillData.Builder withColor(int color) {
            this.color = color;
            return this;
        }

        public SkillData.Builder withIcon(ResourceLocation icon) {
            this.icon = icon;
            return this;
        }

        public SkillData.Builder withIconSize(int size) {
            this.iconSize = size;
            return this;
        }

        public SkillData.Builder withMaxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }

        public SkillData.Builder withAfkExempt(boolean afkExempt) {
            this.afkExempt = afkExempt;
            return this;
        }

        public SkillData.Builder withDisplayName(boolean displayGroupName) {
            this.displayName = displayGroupName;
            return this;
        }

        public SkillData.Builder withUseTotal(boolean useTotalLevels) {
            this.useTotal = useTotalLevels;
            return this;
        }

        public SkillData.Builder setGroupOf(Map<String, Double> group) {
            this.groupOf = group;
            return this;
        }

        public SkillData build() {
            return new SkillData(Optional.of(this.color), Optional.of(this.afkExempt), Optional.of(this.displayName), Optional.of(this.useTotal), this.groupOf.isEmpty() ? Optional.empty() : Optional.of(this.groupOf), Optional.of(this.maxLevel), Optional.of(this.icon), Optional.of(this.iconSize));
        }
    }
}