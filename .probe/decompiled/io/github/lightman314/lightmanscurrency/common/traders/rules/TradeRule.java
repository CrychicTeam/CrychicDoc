package io.github.lightman314.lightmanscurrency.common.traders.rules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import io.github.lightman314.lightmanscurrency.api.traders.rules.TradeRuleType;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class TradeRule {

    private static final List<IRuleLoadListener> LISTENERS = new ArrayList();

    private static final List<String> IGNORE_MISSING = new ArrayList();

    public final TradeRuleType<?> type;

    private ITradeRuleHost host = null;

    private boolean isActive = false;

    public static void addLoadListener(@Nonnull IRuleLoadListener listener) {
        if (!LISTENERS.contains(listener)) {
            LISTENERS.add(listener);
        }
    }

    public static void addIgnoreMissing(@Nonnull String oldType) {
        if (!IGNORE_MISSING.contains(oldType)) {
            IGNORE_MISSING.add(oldType);
        }
    }

    public static String translationKeyOfType(@Nonnull ResourceLocation ruleType) {
        return "traderule." + ruleType.getNamespace() + "." + ruleType.getPath();
    }

    public static MutableComponent nameOfType(@Nonnull ResourceLocation ruleType) {
        return Component.translatable(translationKeyOfType(ruleType));
    }

    public final MutableComponent getName() {
        return nameOfType(this.type.type);
    }

    protected void setHost(@Nullable ITradeRuleHost host) {
        this.host = host;
    }

    public boolean isActive() {
        return this.canActivate(this.host) && this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    protected boolean allowHost(@Nonnull ITradeRuleHost host) {
        return this.onlyAllowOnTraders() && !host.isTrader() ? false : !this.onlyAllowOnTrades() || host.isTrade();
    }

    public boolean canActivate() {
        return this.canActivate(this.host);
    }

    protected boolean canActivate(@Nullable ITradeRuleHost host) {
        return this.allowHost(host);
    }

    protected boolean onlyAllowOnTraders() {
        return false;
    }

    protected boolean onlyAllowOnTrades() {
        return false;
    }

    public void beforeTrade(TradeEvent.PreTradeEvent event) {
    }

    public void tradeCost(TradeEvent.TradeCostEvent event) {
    }

    public void afterTrade(TradeEvent.PostTradeEvent event) {
    }

    protected TradeRule(@Nonnull TradeRuleType<?> type) {
        this.type = type;
    }

    @Nonnull
    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putString("Type", this.type.toString());
        compound.putBoolean("Active", this.isActive);
        this.saveAdditional(compound);
        return compound;
    }

    protected abstract void saveAdditional(@Nonnull CompoundTag var1);

    public final void load(@Nonnull CompoundTag compound) {
        this.isActive = compound.getBoolean("Active");
        this.loadAdditional(compound);
    }

    protected abstract void loadAdditional(@Nonnull CompoundTag var1);

    public abstract JsonObject saveToJson(@Nonnull JsonObject var1);

    public abstract void loadFromJson(@Nonnull JsonObject var1) throws JsonSyntaxException, ResourceLocationException;

    public abstract CompoundTag savePersistentData();

    public abstract void loadPersistentData(CompoundTag var1);

    public final void receiveUpdateMessage(@Nonnull LazyPacketData data) {
        if (data.contains("SetActive")) {
            this.isActive = data.getBoolean("SetActive");
        }
        this.handleUpdateMessage(data);
    }

    protected abstract void handleUpdateMessage(@Nonnull LazyPacketData var1);

    public static void saveRules(@Nonnull CompoundTag compound, @Nonnull List<TradeRule> rules, @Nonnull String tag) {
        ListTag ruleData = new ListTag();
        for (TradeRule rule : rules) {
            ruleData.add(rule.save());
        }
        compound.put(tag, ruleData);
    }

    public static boolean savePersistentData(@Nonnull CompoundTag compound, @Nonnull List<TradeRule> rules, @Nonnull String tag) {
        ListTag ruleData = new ListTag();
        for (TradeRule rule : rules) {
            CompoundTag thisRuleData = rule.savePersistentData();
            if (thisRuleData != null) {
                thisRuleData.putString("Type", rule.type.toString());
                ruleData.add(thisRuleData);
            }
        }
        if (ruleData.isEmpty()) {
            return false;
        } else {
            compound.put(tag, ruleData);
            return true;
        }
    }

    public static JsonArray saveRulesToJson(@Nonnull List<TradeRule> rules) {
        JsonArray ruleData = new JsonArray();
        for (TradeRule rule : rules) {
            if (rule.isActive) {
                JsonObject thisRuleData = rule.saveToJson(new JsonObject());
                if (thisRuleData != null) {
                    thisRuleData.addProperty("Type", rule.type.toString());
                    ruleData.add(thisRuleData);
                }
            }
        }
        return ruleData;
    }

    public static List<TradeRule> loadRules(@Nonnull CompoundTag compound, @Nonnull String tag, @Nullable ITradeRuleHost host) {
        List<TradeRule> rules = new ArrayList();
        if (compound.contains(tag, 9)) {
            ListTag ruleData = compound.getList(tag, 10);
            List<CompoundTag> allData = new ArrayList();
            for (int i = 0; i < ruleData.size(); i++) {
                allData.add(ruleData.getCompound(i));
            }
            LISTENERS.forEach(l -> l.beforeLoading(host, allData, rules));
            for (CompoundTag data : allData) {
                TradeRule thisRule = Deserialize(data);
                if (thisRule != null) {
                    rules.add(thisRule);
                    thisRule.host = host;
                }
            }
            LISTENERS.forEach(l -> l.afterLoading(host, allData, rules));
        }
        return rules;
    }

    public static void loadPersistentData(@Nonnull CompoundTag compound, @Nonnull List<TradeRule> tradeRules, @Nonnull String tag) {
        if (compound.contains(tag, 9)) {
            ListTag ruleData = compound.getList(tag, 10);
            for (int i = 0; i < ruleData.size(); i++) {
                CompoundTag thisRuleData = ruleData.getCompound(i);
                boolean query = true;
                for (int r = 0; query && r < tradeRules.size(); r++) {
                    if (((TradeRule) tradeRules.get(r)).type.toString().contentEquals(thisRuleData.getString("Type"))) {
                        ((TradeRule) tradeRules.get(r)).loadPersistentData(thisRuleData);
                        query = false;
                    }
                }
            }
        }
    }

    public static List<TradeRule> Parse(@Nonnull JsonArray tradeRuleData, @Nullable ITradeRuleHost host) {
        List<TradeRule> rules = new ArrayList();
        for (int i = 0; i < tradeRuleData.size(); i++) {
            try {
                JsonObject thisRuleData = tradeRuleData.get(i).getAsJsonObject();
                TradeRule thisRule = Deserialize(thisRuleData);
                thisRule.host = host;
                rules.add(thisRule);
            } catch (Throwable var6) {
                LightmansCurrency.LogError("Error loading Trade Rule at index " + i + ".", var6);
            }
        }
        return rules;
    }

    public static boolean ValidateTradeRuleList(@Nonnull List<TradeRule> rules, @Nonnull ITradeRuleHost host) {
        boolean changed = false;
        for (TradeRuleType<?> ruleType : TraderAPI.getTradeRuleTypes()) {
            TradeRule rule = ruleType.createNew();
            if (rule != null && host.allowTradeRule(rule) && rule.allowHost(host) && !HasTradeRule(rules, rule.type.type)) {
                rules.add(rule);
                rule.host = host;
                changed = true;
            }
        }
        for (int i = 0; i < rules.size(); i++) {
            TradeRule r1 = (TradeRule) rules.get(i);
            for (int j = i + 1; j < rules.size(); j++) {
                if (((TradeRule) rules.get(j)).type == r1.type) {
                    rules.remove(j--);
                    changed = true;
                }
            }
        }
        return changed;
    }

    public static boolean ValidateTradeRuleActiveStates(@Nonnull List<TradeRule> rules) {
        boolean changed = false;
        for (TradeRule rule : rules) {
            if (rule.isActive && !rule.canActivate()) {
                rule.isActive = false;
                changed = true;
            }
        }
        return changed;
    }

    public static boolean HasTradeRule(@Nonnull List<TradeRule> rules, @Nonnull ResourceLocation type) {
        return GetTradeRule(rules, type) != null;
    }

    @Nullable
    public static TradeRule GetTradeRule(@Nonnull List<TradeRule> rules, @Nonnull ResourceLocation type) {
        for (TradeRule rule : rules) {
            if (rule.type.type.equals(type)) {
                return rule;
            }
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public abstract TradeRulesClientSubTab createTab(TradeRulesClientTab<?> var1);

    @Nonnull
    public static TradeRule CreateRule(@Nonnull ResourceLocation type) {
        TradeRuleType<?> ruleType = TraderAPI.getTradeRuleType(type);
        if (ruleType != null) {
            return ruleType.createNew();
        } else {
            LightmansCurrency.LogError("Could not find a TradeRuleType of type '" + type + "'. Unable to create the Trade Rule.");
            return null;
        }
    }

    @Nonnull
    public static TradeRule Deserialize(@Nonnull CompoundTag compound) {
        String thisType = compound.contains("Type") ? compound.getString("Type") : compound.getString("type");
        TradeRuleType<?> ruleType = TraderAPI.getTradeRuleType(new ResourceLocation(thisType));
        if (ruleType != null) {
            return ruleType.load(compound);
        } else if (IGNORE_MISSING.contains(thisType)) {
            return null;
        } else {
            LightmansCurrency.LogError("Could not find a TradeRuleType of type '" + thisType + "'. Unable to load the Trade Rule.");
            return null;
        }
    }

    @Nonnull
    public static TradeRule Deserialize(@Nonnull JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        String thisType = GsonHelper.getAsString(json, "Type");
        TradeRuleType<?> ruleType = TraderAPI.getTradeRuleType(new ResourceLocation(thisType));
        if (ruleType != null) {
            TradeRule rule = ruleType.loadFromJson(json);
            rule.loadFromJson(json);
            rule.setActive(true);
            return rule;
        } else {
            throw new JsonSyntaxException("Could not find a deserializer of type '" + thisType + "'.");
        }
    }

    @Nullable
    public static TradeRule getRule(@Nonnull ResourceLocation type, @Nonnull List<TradeRule> rules) {
        for (TradeRule rule : rules) {
            if (rule.type.type.equals(type)) {
                return rule;
            }
        }
        return null;
    }

    public static CompoundTag CreateRuleMessage() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Create", true);
        return tag;
    }

    public static CompoundTag RemoveRuleMessage() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Remove", true);
        return tag;
    }

    public static boolean isCreateMessage(CompoundTag tag) {
        return tag.contains("Create") && tag.getBoolean("Create");
    }

    public static boolean isRemoveMessage(CompoundTag tag) {
        return tag.contains("Remove") && tag.getBoolean("Remove");
    }
}