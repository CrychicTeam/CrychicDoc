package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class AdvancementProgress implements Comparable<AdvancementProgress> {

    final Map<String, CriterionProgress> criteria;

    private String[][] requirements = new String[0][];

    private AdvancementProgress(Map<String, CriterionProgress> mapStringCriterionProgress0) {
        this.criteria = mapStringCriterionProgress0;
    }

    public AdvancementProgress() {
        this.criteria = Maps.newHashMap();
    }

    public void update(Map<String, Criterion> mapStringCriterion0, String[][] string1) {
        Set<String> $$2 = mapStringCriterion0.keySet();
        this.criteria.entrySet().removeIf(p_8203_ -> !$$2.contains(p_8203_.getKey()));
        for (String $$3 : $$2) {
            if (!this.criteria.containsKey($$3)) {
                this.criteria.put($$3, new CriterionProgress());
            }
        }
        this.requirements = string1;
    }

    public boolean isDone() {
        if (this.requirements.length == 0) {
            return false;
        } else {
            for (String[] $$0 : this.requirements) {
                boolean $$1 = false;
                for (String $$2 : $$0) {
                    CriterionProgress $$3 = this.getCriterion($$2);
                    if ($$3 != null && $$3.isDone()) {
                        $$1 = true;
                        break;
                    }
                }
                if (!$$1) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean hasProgress() {
        for (CriterionProgress $$0 : this.criteria.values()) {
            if ($$0.isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean grantProgress(String string0) {
        CriterionProgress $$1 = (CriterionProgress) this.criteria.get(string0);
        if ($$1 != null && !$$1.isDone()) {
            $$1.grant();
            return true;
        } else {
            return false;
        }
    }

    public boolean revokeProgress(String string0) {
        CriterionProgress $$1 = (CriterionProgress) this.criteria.get(string0);
        if ($$1 != null && $$1.isDone()) {
            $$1.revoke();
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return "AdvancementProgress{criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + "}";
    }

    public void serializeToNetwork(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeMap(this.criteria, FriendlyByteBuf::m_130070_, (p_144360_, p_144361_) -> p_144361_.serializeToNetwork(p_144360_));
    }

    public static AdvancementProgress fromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        Map<String, CriterionProgress> $$1 = friendlyByteBuf0.readMap(FriendlyByteBuf::m_130277_, CriterionProgress::m_12917_);
        return new AdvancementProgress($$1);
    }

    @Nullable
    public CriterionProgress getCriterion(String string0) {
        return (CriterionProgress) this.criteria.get(string0);
    }

    public float getPercent() {
        if (this.criteria.isEmpty()) {
            return 0.0F;
        } else {
            float $$0 = (float) this.requirements.length;
            float $$1 = (float) this.countCompletedRequirements();
            return $$1 / $$0;
        }
    }

    @Nullable
    public String getProgressText() {
        if (this.criteria.isEmpty()) {
            return null;
        } else {
            int $$0 = this.requirements.length;
            if ($$0 <= 1) {
                return null;
            } else {
                int $$1 = this.countCompletedRequirements();
                return $$1 + "/" + $$0;
            }
        }
    }

    private int countCompletedRequirements() {
        int $$0 = 0;
        for (String[] $$1 : this.requirements) {
            boolean $$2 = false;
            for (String $$3 : $$1) {
                CriterionProgress $$4 = this.getCriterion($$3);
                if ($$4 != null && $$4.isDone()) {
                    $$2 = true;
                    break;
                }
            }
            if ($$2) {
                $$0++;
            }
        }
        return $$0;
    }

    public Iterable<String> getRemainingCriteria() {
        List<String> $$0 = Lists.newArrayList();
        for (Entry<String, CriterionProgress> $$1 : this.criteria.entrySet()) {
            if (!((CriterionProgress) $$1.getValue()).isDone()) {
                $$0.add((String) $$1.getKey());
            }
        }
        return $$0;
    }

    public Iterable<String> getCompletedCriteria() {
        List<String> $$0 = Lists.newArrayList();
        for (Entry<String, CriterionProgress> $$1 : this.criteria.entrySet()) {
            if (((CriterionProgress) $$1.getValue()).isDone()) {
                $$0.add((String) $$1.getKey());
            }
        }
        return $$0;
    }

    @Nullable
    public Date getFirstProgressDate() {
        Date $$0 = null;
        for (CriterionProgress $$1 : this.criteria.values()) {
            if ($$1.isDone() && ($$0 == null || $$1.getObtained().before($$0))) {
                $$0 = $$1.getObtained();
            }
        }
        return $$0;
    }

    public int compareTo(AdvancementProgress advancementProgress0) {
        Date $$1 = this.getFirstProgressDate();
        Date $$2 = advancementProgress0.getFirstProgressDate();
        if ($$1 == null && $$2 != null) {
            return 1;
        } else if ($$1 != null && $$2 == null) {
            return -1;
        } else {
            return $$1 == null && $$2 == null ? 0 : $$1.compareTo($$2);
        }
    }

    public static class Serializer implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress> {

        public JsonElement serialize(AdvancementProgress advancementProgress0, Type type1, JsonSerializationContext jsonSerializationContext2) {
            JsonObject $$3 = new JsonObject();
            JsonObject $$4 = new JsonObject();
            for (Entry<String, CriterionProgress> $$5 : advancementProgress0.criteria.entrySet()) {
                CriterionProgress $$6 = (CriterionProgress) $$5.getValue();
                if ($$6.isDone()) {
                    $$4.add((String) $$5.getKey(), $$6.serializeToJson());
                }
            }
            if (!$$4.entrySet().isEmpty()) {
                $$3.add("criteria", $$4);
            }
            $$3.addProperty("done", advancementProgress0.isDone());
            return $$3;
        }

        public AdvancementProgress deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = GsonHelper.convertToJsonObject(jsonElement0, "advancement");
            JsonObject $$4 = GsonHelper.getAsJsonObject($$3, "criteria", new JsonObject());
            AdvancementProgress $$5 = new AdvancementProgress();
            for (Entry<String, JsonElement> $$6 : $$4.entrySet()) {
                String $$7 = (String) $$6.getKey();
                $$5.criteria.put($$7, CriterionProgress.fromJson(GsonHelper.convertToString((JsonElement) $$6.getValue(), $$7)));
            }
            return $$5;
        }
    }
}