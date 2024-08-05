package net.minecraft.advancements;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.ArrayUtils;

public class Advancement {

    @Nullable
    private final Advancement parent;

    @Nullable
    private final DisplayInfo display;

    private final AdvancementRewards rewards;

    private final ResourceLocation id;

    private final Map<String, Criterion> criteria;

    private final String[][] requirements;

    private final Set<Advancement> children = Sets.newLinkedHashSet();

    private final Component chatComponent;

    private final boolean sendsTelemetryEvent;

    public Advancement(ResourceLocation resourceLocation0, @Nullable Advancement advancement1, @Nullable DisplayInfo displayInfo2, AdvancementRewards advancementRewards3, Map<String, Criterion> mapStringCriterion4, String[][] string5, boolean boolean6) {
        this.id = resourceLocation0;
        this.display = displayInfo2;
        this.criteria = ImmutableMap.copyOf(mapStringCriterion4);
        this.parent = advancement1;
        this.rewards = advancementRewards3;
        this.requirements = string5;
        this.sendsTelemetryEvent = boolean6;
        if (advancement1 != null) {
            advancement1.addChild(this);
        }
        if (displayInfo2 == null) {
            this.chatComponent = Component.literal(resourceLocation0.toString());
        } else {
            Component $$7 = displayInfo2.getTitle();
            ChatFormatting $$8 = displayInfo2.getFrame().getChatColor();
            Component $$9 = ComponentUtils.mergeStyles($$7.copy(), Style.EMPTY.withColor($$8)).append("\n").append(displayInfo2.getDescription());
            Component $$10 = $$7.copy().withStyle(p_138316_ -> p_138316_.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, $$9)));
            this.chatComponent = ComponentUtils.wrapInSquareBrackets($$10).withStyle($$8);
        }
    }

    public Advancement.Builder deconstruct() {
        return new Advancement.Builder(this.parent == null ? null : this.parent.getId(), this.display, this.rewards, this.criteria, this.requirements, this.sendsTelemetryEvent);
    }

    @Nullable
    public Advancement getParent() {
        return this.parent;
    }

    public Advancement getRoot() {
        return getRoot(this);
    }

    public static Advancement getRoot(Advancement advancement0) {
        Advancement $$1 = advancement0;
        while (true) {
            Advancement $$2 = $$1.getParent();
            if ($$2 == null) {
                return $$1;
            }
            $$1 = $$2;
        }
    }

    @Nullable
    public DisplayInfo getDisplay() {
        return this.display;
    }

    public boolean sendsTelemetryEvent() {
        return this.sendsTelemetryEvent;
    }

    public AdvancementRewards getRewards() {
        return this.rewards;
    }

    public String toString() {
        return "SimpleAdvancement{id=" + this.getId() + ", parent=" + (this.parent == null ? "null" : this.parent.getId()) + ", display=" + this.display + ", rewards=" + this.rewards + ", criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + ", sendsTelemetryEvent=" + this.sendsTelemetryEvent + "}";
    }

    public Iterable<Advancement> getChildren() {
        return this.children;
    }

    public Map<String, Criterion> getCriteria() {
        return this.criteria;
    }

    public int getMaxCriteraRequired() {
        return this.requirements.length;
    }

    public void addChild(Advancement advancement0) {
        this.children.add(advancement0);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof Advancement $$1) ? false : this.id.equals($$1.id);
        }
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public String[][] getRequirements() {
        return this.requirements;
    }

    public Component getChatComponent() {
        return this.chatComponent;
    }

    public static class Builder {

        @Nullable
        private ResourceLocation parentId;

        @Nullable
        private Advancement parent;

        @Nullable
        private DisplayInfo display;

        private AdvancementRewards rewards = AdvancementRewards.EMPTY;

        private Map<String, Criterion> criteria = Maps.newLinkedHashMap();

        @Nullable
        private String[][] requirements;

        private RequirementsStrategy requirementsStrategy = RequirementsStrategy.AND;

        private final boolean sendsTelemetryEvent;

        Builder(@Nullable ResourceLocation resourceLocation0, @Nullable DisplayInfo displayInfo1, AdvancementRewards advancementRewards2, Map<String, Criterion> mapStringCriterion3, String[][] string4, boolean boolean5) {
            this.parentId = resourceLocation0;
            this.display = displayInfo1;
            this.rewards = advancementRewards2;
            this.criteria = mapStringCriterion3;
            this.requirements = string4;
            this.sendsTelemetryEvent = boolean5;
        }

        private Builder(boolean boolean0) {
            this.sendsTelemetryEvent = boolean0;
        }

        public static Advancement.Builder advancement() {
            return new Advancement.Builder(true);
        }

        public static Advancement.Builder recipeAdvancement() {
            return new Advancement.Builder(false);
        }

        public Advancement.Builder parent(Advancement advancement0) {
            this.parent = advancement0;
            return this;
        }

        public Advancement.Builder parent(ResourceLocation resourceLocation0) {
            this.parentId = resourceLocation0;
            return this;
        }

        public Advancement.Builder display(ItemStack itemStack0, Component component1, Component component2, @Nullable ResourceLocation resourceLocation3, FrameType frameType4, boolean boolean5, boolean boolean6, boolean boolean7) {
            return this.display(new DisplayInfo(itemStack0, component1, component2, resourceLocation3, frameType4, boolean5, boolean6, boolean7));
        }

        public Advancement.Builder display(ItemLike itemLike0, Component component1, Component component2, @Nullable ResourceLocation resourceLocation3, FrameType frameType4, boolean boolean5, boolean boolean6, boolean boolean7) {
            return this.display(new DisplayInfo(new ItemStack(itemLike0.asItem()), component1, component2, resourceLocation3, frameType4, boolean5, boolean6, boolean7));
        }

        public Advancement.Builder display(DisplayInfo displayInfo0) {
            this.display = displayInfo0;
            return this;
        }

        public Advancement.Builder rewards(AdvancementRewards.Builder advancementRewardsBuilder0) {
            return this.rewards(advancementRewardsBuilder0.build());
        }

        public Advancement.Builder rewards(AdvancementRewards advancementRewards0) {
            this.rewards = advancementRewards0;
            return this;
        }

        public Advancement.Builder addCriterion(String string0, CriterionTriggerInstance criterionTriggerInstance1) {
            return this.addCriterion(string0, new Criterion(criterionTriggerInstance1));
        }

        public Advancement.Builder addCriterion(String string0, Criterion criterion1) {
            if (this.criteria.containsKey(string0)) {
                throw new IllegalArgumentException("Duplicate criterion " + string0);
            } else {
                this.criteria.put(string0, criterion1);
                return this;
            }
        }

        public Advancement.Builder requirements(RequirementsStrategy requirementsStrategy0) {
            this.requirementsStrategy = requirementsStrategy0;
            return this;
        }

        public Advancement.Builder requirements(String[][] string0) {
            this.requirements = string0;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, Advancement> functionResourceLocationAdvancement0) {
            if (this.parentId == null) {
                return true;
            } else {
                if (this.parent == null) {
                    this.parent = (Advancement) functionResourceLocationAdvancement0.apply(this.parentId);
                }
                return this.parent != null;
            }
        }

        public Advancement build(ResourceLocation resourceLocation0) {
            if (!this.canBuild(p_138407_ -> null)) {
                throw new IllegalStateException("Tried to build incomplete advancement!");
            } else {
                if (this.requirements == null) {
                    this.requirements = this.requirementsStrategy.createRequirements(this.criteria.keySet());
                }
                return new Advancement(resourceLocation0, this.parent, this.display, this.rewards, this.criteria, this.requirements, this.sendsTelemetryEvent);
            }
        }

        public Advancement save(Consumer<Advancement> consumerAdvancement0, String string1) {
            Advancement $$2 = this.build(new ResourceLocation(string1));
            consumerAdvancement0.accept($$2);
            return $$2;
        }

        public JsonObject serializeToJson() {
            if (this.requirements == null) {
                this.requirements = this.requirementsStrategy.createRequirements(this.criteria.keySet());
            }
            JsonObject $$0 = new JsonObject();
            if (this.parent != null) {
                $$0.addProperty("parent", this.parent.getId().toString());
            } else if (this.parentId != null) {
                $$0.addProperty("parent", this.parentId.toString());
            }
            if (this.display != null) {
                $$0.add("display", this.display.serializeToJson());
            }
            $$0.add("rewards", this.rewards.serializeToJson());
            JsonObject $$1 = new JsonObject();
            for (Entry<String, Criterion> $$2 : this.criteria.entrySet()) {
                $$1.add((String) $$2.getKey(), ((Criterion) $$2.getValue()).serializeToJson());
            }
            $$0.add("criteria", $$1);
            JsonArray $$3 = new JsonArray();
            for (String[] $$4 : this.requirements) {
                JsonArray $$5 = new JsonArray();
                for (String $$6 : $$4) {
                    $$5.add($$6);
                }
                $$3.add($$5);
            }
            $$0.add("requirements", $$3);
            $$0.addProperty("sends_telemetry_event", this.sendsTelemetryEvent);
            return $$0;
        }

        public void serializeToNetwork(FriendlyByteBuf friendlyByteBuf0) {
            if (this.requirements == null) {
                this.requirements = this.requirementsStrategy.createRequirements(this.criteria.keySet());
            }
            friendlyByteBuf0.writeNullable(this.parentId, FriendlyByteBuf::m_130085_);
            friendlyByteBuf0.writeNullable(this.display, (p_214831_, p_214832_) -> p_214832_.serializeToNetwork(p_214831_));
            Criterion.serializeToNetwork(this.criteria, friendlyByteBuf0);
            friendlyByteBuf0.writeVarInt(this.requirements.length);
            for (String[] $$1 : this.requirements) {
                friendlyByteBuf0.writeVarInt($$1.length);
                for (String $$2 : $$1) {
                    friendlyByteBuf0.writeUtf($$2);
                }
            }
            friendlyByteBuf0.writeBoolean(this.sendsTelemetryEvent);
        }

        public String toString() {
            return "Task Advancement{parentId=" + this.parentId + ", display=" + this.display + ", rewards=" + this.rewards + ", criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + ", sends_telemetry_event=" + this.sendsTelemetryEvent + "}";
        }

        public static Advancement.Builder fromJson(JsonObject jsonObject0, DeserializationContext deserializationContext1) {
            ResourceLocation $$2 = jsonObject0.has("parent") ? new ResourceLocation(GsonHelper.getAsString(jsonObject0, "parent")) : null;
            DisplayInfo $$3 = jsonObject0.has("display") ? DisplayInfo.fromJson(GsonHelper.getAsJsonObject(jsonObject0, "display")) : null;
            AdvancementRewards $$4 = jsonObject0.has("rewards") ? AdvancementRewards.deserialize(GsonHelper.getAsJsonObject(jsonObject0, "rewards")) : AdvancementRewards.EMPTY;
            Map<String, Criterion> $$5 = Criterion.criteriaFromJson(GsonHelper.getAsJsonObject(jsonObject0, "criteria"), deserializationContext1);
            if ($$5.isEmpty()) {
                throw new JsonSyntaxException("Advancement criteria cannot be empty");
            } else {
                JsonArray $$6 = GsonHelper.getAsJsonArray(jsonObject0, "requirements", new JsonArray());
                String[][] $$7 = new String[$$6.size()][];
                for (int $$8 = 0; $$8 < $$6.size(); $$8++) {
                    JsonArray $$9 = GsonHelper.convertToJsonArray($$6.get($$8), "requirements[" + $$8 + "]");
                    $$7[$$8] = new String[$$9.size()];
                    for (int $$10 = 0; $$10 < $$9.size(); $$10++) {
                        $$7[$$8][$$10] = GsonHelper.convertToString($$9.get($$10), "requirements[" + $$8 + "][" + $$10 + "]");
                    }
                }
                if ($$7.length == 0) {
                    $$7 = new String[$$5.size()][];
                    int $$11 = 0;
                    for (String $$12 : $$5.keySet()) {
                        $$7[$$11++] = new String[] { $$12 };
                    }
                }
                for (String[] $$13 : $$7) {
                    if ($$13.length == 0 && $$5.isEmpty()) {
                        throw new JsonSyntaxException("Requirement entry cannot be empty");
                    }
                    for (String $$14 : $$13) {
                        if (!$$5.containsKey($$14)) {
                            throw new JsonSyntaxException("Unknown required criterion '" + $$14 + "'");
                        }
                    }
                }
                for (String $$15 : $$5.keySet()) {
                    boolean $$16 = false;
                    for (String[] $$17 : $$7) {
                        if (ArrayUtils.contains($$17, $$15)) {
                            $$16 = true;
                            break;
                        }
                    }
                    if (!$$16) {
                        throw new JsonSyntaxException("Criterion '" + $$15 + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required.");
                    }
                }
                boolean $$18 = GsonHelper.getAsBoolean(jsonObject0, "sends_telemetry_event", false);
                return new Advancement.Builder($$2, $$3, $$4, $$5, $$7, $$18);
            }
        }

        public static Advancement.Builder fromNetwork(FriendlyByteBuf friendlyByteBuf0) {
            ResourceLocation $$1 = friendlyByteBuf0.readNullable(FriendlyByteBuf::m_130281_);
            DisplayInfo $$2 = friendlyByteBuf0.readNullable(DisplayInfo::m_14988_);
            Map<String, Criterion> $$3 = Criterion.criteriaFromNetwork(friendlyByteBuf0);
            String[][] $$4 = new String[friendlyByteBuf0.readVarInt()][];
            for (int $$5 = 0; $$5 < $$4.length; $$5++) {
                $$4[$$5] = new String[friendlyByteBuf0.readVarInt()];
                for (int $$6 = 0; $$6 < $$4[$$5].length; $$6++) {
                    $$4[$$5][$$6] = friendlyByteBuf0.readUtf();
                }
            }
            boolean $$7 = friendlyByteBuf0.readBoolean();
            return new Advancement.Builder($$1, $$2, AdvancementRewards.EMPTY, $$3, $$4, $$7);
        }

        public Map<String, Criterion> getCriteria() {
            return this.criteria;
        }
    }
}