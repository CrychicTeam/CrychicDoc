package com.mna.spells.crafting;

import com.google.common.collect.UnmodifiableIterator;
import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.events.SpellReagentsEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.particles.MAParticleType;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellCastingResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemStaff;
import com.mna.recipes.ItemAndPatternRecipeHelper;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.spells.ComponentRecipe;
import com.mna.recipes.spells.ModifierRecipe;
import com.mna.recipes.spells.ShapeRecipe;
import com.mna.spells.NameProcessors;
import com.mna.spells.SpellCaster;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalInt;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;

public class SpellRecipe implements ISpellDefinition, IFactionSpecific {

    public static final int MAX_COMPONENTS = 5;

    public static final int MAX_MODIFIERS = 3;

    public static final String SPELL_COMPOUND_TAG = "spell";

    public static final String SPELL_COLOR_TAG = "spellColor";

    private ModifiedSpellPart<Shape> shape;

    private ArrayList<ModifiedSpellPart<SpellEffect>> components;

    private Modifier[] modifiers;

    private Affinity overrideAffinity = Affinity.UNKNOWN;

    private float complexity;

    private int particleColorOverride = -1;

    private float calculatedManaCost;

    private float overrideManaCost = -1.0F;

    private boolean isMysterious = true;

    public SpellRecipe() {
        this.modifiers = new Modifier[3];
        this.components = new ArrayList(5);
    }

    public SpellRecipe(Shape shape, SpellEffect component) {
        this.modifiers = new Modifier[3];
        this.components = new ArrayList(5);
        this.setShape(shape);
        this.addComponent(component);
    }

    public SpellRecipe setShape(Shape shape) {
        if (shape != null) {
            this.shape = new ModifiedSpellPart<>(shape);
        } else {
            this.shape = null;
        }
        this.recalculateSpellNumbers();
        return this;
    }

    public SpellRecipe addComponent(SpellEffect component) {
        if (this.components.size() < 5 && !this.components.stream().anyMatch(c -> c.getPart() == component)) {
            this.components.add(new ModifiedSpellPart<>(component));
            this.recalculateSpellNumbers();
        }
        return this;
    }

    public SpellRecipe setShapeModifier(Attribute modifier, float value) {
        ModifiedSpellPart<Shape> shape = this.getShape();
        if (shape != null) {
            shape.setValue(modifier, value);
        }
        return this;
    }

    public SpellRecipe setComponentModifier(int componentIndex, Attribute modifier, float value) {
        ModifiedSpellPart<SpellEffect> component = this.getComponent(componentIndex);
        if (component != null) {
            component.setValue(modifier, value);
        }
        return this;
    }

    public void removeComponent(int index) {
        if (index >= 0 && index < this.components.size()) {
            this.components.remove(index);
            this.recalculateSpellNumbers();
        }
    }

    public void setModifier(Modifier modifier, int index) {
        if (index >= 0 && index < 3) {
            if (modifier == null && this.modifiers[index] != null) {
                UnmodifiableIterator var3 = this.modifiers[index].getModifiedAttributes().iterator();
                while (var3.hasNext()) {
                    Attribute attribute = (Attribute) var3.next();
                    if (this.shape != null) {
                        this.shape.resetValueToDefault(attribute);
                    }
                    for (ModifiedSpellPart<SpellEffect> component : this.components) {
                        if (component != null) {
                            component.resetValueToDefault(attribute);
                        }
                    }
                }
            }
            this.modifiers[index] = modifier;
            this.recalculateSpellNumbers();
        }
    }

    public boolean addModifier(Modifier modifier) {
        for (int i = 0; i < 3; i++) {
            if (this.modifiers[i] == null) {
                this.modifiers[i] = modifier;
                return true;
            }
        }
        return false;
    }

    private void recalculateSpellNumbers() {
        this.calculateComplexity();
        this.calculateManaCost();
    }

    public ModifiedSpellPart<Shape> getShape() {
        return this.shape;
    }

    @Nullable
    public ModifiedSpellPart<SpellEffect> getComponent(int index) {
        return index >= 0 && index < this.components.size() ? (ModifiedSpellPart) this.components.get(index) : null;
    }

    @Override
    public int countComponents() {
        return this.components.size();
    }

    @Override
    public void clearComponents() {
        this.components.clear();
    }

    @Override
    public int findComponent(SpellEffect component) {
        OptionalInt index = IntStream.range(0, this.components.size()).filter(idx -> ((ModifiedSpellPart) this.components.get(idx)).getPart() == component).findFirst();
        return index.isPresent() ? index.getAsInt() : -1;
    }

    @Override
    public List<IModifiedSpellPart<SpellEffect>> getComponents() {
        return (List<IModifiedSpellPart<SpellEffect>>) this.components.stream().map(c -> c).collect(Collectors.toList());
    }

    @Override
    public void iterateComponents(Consumer<IModifiedSpellPart<SpellEffect>> consumer) {
        for (ModifiedSpellPart<SpellEffect> component : this.components) {
            consumer.accept(component);
        }
    }

    @Override
    public Modifier getModifier(int index) {
        return index >= 0 && index < 3 ? this.modifiers[index] : null;
    }

    @Override
    public List<Modifier> getModifiers() {
        return (List<Modifier>) Arrays.asList(this.modifiers).stream().filter(m -> m != null).collect(Collectors.toList());
    }

    public boolean isAttributeModifiable(Attribute attr) {
        for (Modifier m : this.modifiers) {
            if (m != null && m.modifiesType(attr)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int countModifiers() {
        int c = 0;
        for (Modifier m : this.modifiers) {
            if (m != null) {
                c++;
            }
        }
        return c;
    }

    @Override
    public int getCooldown(@Nullable LivingEntity caster) {
        if (!this.isValid()) {
            return 0;
        } else {
            IPlayerRoteSpells roteCap = caster != null && caster instanceof Player ? (IPlayerRoteSpells) caster.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null) : null;
            float complexityCooldown = this.getComplexity() / 4.0F;
            MutableFloat baseline = new MutableFloat((float) this.shape.getPart().baselineCooldown());
            MutableFloat componentMastery = new MutableFloat(0.0F);
            this.iterateComponents(c -> {
                float componentCD = (float) ((SpellEffect) c.getPart()).baselineCooldown();
                baseline.add(componentCD);
                if (roteCap != null) {
                    float mastery = roteCap.getMastery(c.getPart());
                    componentMastery.add(mastery);
                }
            });
            float totalBaseline = baseline.getValue() + complexityCooldown;
            float componentMasteryFactor = componentMastery.getValue() * 0.625F / (float) this.getComponents().size();
            float shapeMasteryFactor = (roteCap != null ? roteCap.getMastery(this.getShape().getPart()) : 0.0F) * 0.375F;
            float contribution = MathUtils.lerpf(1.0F, 0.5F, componentMasteryFactor + shapeMasteryFactor);
            return (int) Math.ceil((double) (totalBaseline * contribution));
        }
    }

    @Override
    public float getComplexity() {
        return this.complexity;
    }

    @Override
    public float getManaCost() {
        return this.overrideManaCost == -1.0F ? this.calculatedManaCost : this.overrideManaCost;
    }

    @Override
    public void setManaCost(float manaCost) {
        this.overrideManaCost = manaCost;
    }

    @Override
    public int getParticleColorOverride() {
        return this.particleColorOverride;
    }

    @Override
    public void setParticleColorOverride(int color) {
        this.particleColorOverride = color;
    }

    @Override
    public MAParticleType colorParticle(MAParticleType particle, Entity living) {
        float minAlpha = 0.1F;
        if (living != null && living instanceof Player) {
            ((Player) living).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                if (m.getParticleColorOverride() != -1) {
                    float pctx = MathUtils.clamp((float) FastColor.ARGB32.alpha(m.getParticleColorOverride()) / 255.0F, minAlpha, 1.0F);
                    particle.setColor((float) FastColor.ARGB32.red(m.getParticleColorOverride()) * pctx, (float) FastColor.ARGB32.green(m.getParticleColorOverride()) * pctx, (float) FastColor.ARGB32.blue(m.getParticleColorOverride()) * pctx);
                }
            });
        }
        if (this.particleColorOverride != -1) {
            float pct = MathUtils.clamp((float) FastColor.ARGB32.alpha(this.particleColorOverride) / 255.0F, minAlpha, 1.0F);
            particle.setColor((float) FastColor.ARGB32.red(this.particleColorOverride) * pct, (float) FastColor.ARGB32.green(this.particleColorOverride) * pct, (float) FastColor.ARGB32.blue(this.particleColorOverride) * pct);
        }
        return particle;
    }

    @Override
    public boolean isValid() {
        return this.shape == null || this.shape.getPart() == null || this.components.stream().allMatch(c -> c == null) ? false : !this.shape.getPart().isChanneled() || !this.components.stream().anyMatch(c -> !((SpellEffect) c.getPart()).canBeChanneled());
    }

    @Override
    public boolean isHarmful() {
        return this.components.stream().anyMatch(c -> c != null && c.getPart() != null && ((SpellEffect) c.getPart()).getUseTag() == SpellPartTags.HARMFUL);
    }

    public boolean isRote(Player player) {
        MutableBoolean valid = new MutableBoolean(true);
        player.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
            ArrayList<Attribute> modified_attrs = new ArrayList();
            if (!r.isRote(this.shape.getPart())) {
                valid.setFalse();
            } else {
                modified_attrs.addAll((Collection) this.shape.getContainedAttributes().stream().filter(a -> this.shape.getValueWithoutMultipliers(a) != this.shape.getDefaultValue(a)).collect(Collectors.toList()));
                this.components.forEach(c -> {
                    if (!r.isRote(c.getPart())) {
                        valid.setFalse();
                    } else {
                        modified_attrs.addAll((Collection) c.getContainedAttributes().stream().filter(a -> this.shape.getValueWithoutMultipliers(a) != this.shape.getDefaultValue(a)).collect(Collectors.toList()));
                    }
                });
                modified_attrs.forEach(a -> {
                    boolean modifier_attr_rote = false;
                    for (int i = 0; i < this.modifiers.length; i++) {
                        Modifier m = this.modifiers[i];
                        if (m == null) {
                            return;
                        }
                        if (m.modifiesType(a)) {
                            modifier_attr_rote = true;
                            break;
                        }
                    }
                    if (!modifier_attr_rote) {
                        valid.setFalse();
                    }
                });
            }
        });
        return valid.getValue();
    }

    public int getMaxChannelTime() {
        if (!this.isValid()) {
            return 0;
        } else {
            Shape s = this.getShape().getPart();
            return s.isChanneled() ? s.maxChannelTime(this.getShape()) : 0;
        }
    }

    @Override
    public boolean isChanneled() {
        return !this.isValid() ? false : this.getShape().getPart().isChanneled();
    }

    @Override
    public boolean isMysterious() {
        return this.isMysterious;
    }

    public static boolean isMysterious(ItemStack stack) {
        if (!stack.hasTag()) {
            return false;
        } else {
            CompoundTag nbt = stack.getTag();
            if (nbt.contains("spell")) {
                CompoundTag spellTag = nbt.getCompound("spell");
                if (spellTag.contains("mysterious")) {
                    return spellTag.getBoolean("mysterious");
                }
            }
            return false;
        }
    }

    public void setMysterious(boolean mysterious) {
        this.isMysterious = mysterious;
    }

    @Override
    public void setOverrideAffinity(Affinity affinity) {
        this.overrideAffinity = affinity;
    }

    public boolean hasOverrideAffinity() {
        return this.overrideAffinity != Affinity.UNKNOWN;
    }

    public boolean hasOverrideManaCost() {
        return this.overrideManaCost >= 0.0F;
    }

    @Override
    public boolean canFactionCraft(IPlayerProgression progression) {
        if (this.getShape().getPart().getFactionRequirement() != null && this.getShape().getPart().getFactionRequirement() != progression.getAlliedFaction()) {
            return false;
        } else {
            for (int i = 0; i < 5; i++) {
                ModifiedSpellPart<SpellEffect> c = this.getComponent(i);
                if (c != null && c.getPart().getFactionRequirement() != null && c.getPart().getFactionRequirement() != progression.getAlliedFaction()) {
                    return false;
                }
            }
            for (int ix = 0; ix < 3; ix++) {
                Modifier m = this.getModifier(ix);
                if (m != null && m.getFactionRequirement() != null && m.getFactionRequirement() != progression.getAlliedFaction()) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean changeShapeAttributeValue(Attribute attribute, float newValue) {
        if (this.shape == null) {
            return false;
        } else {
            this.shape.setValue(attribute, newValue);
            this.recalculateSpellNumbers();
            return true;
        }
    }

    public boolean changeComponentAttributeValue(int index, Attribute attribute, float newValue) {
        if (index >= 0 && index < this.components.size() && this.components.get(index) != null) {
            ((ModifiedSpellPart) this.components.get(index)).setValue(attribute, newValue);
            this.recalculateSpellNumbers();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        CompoundTag spell = new CompoundTag();
        if (this.getShape() != null) {
            spell.put("shape", this.getShape().toNBT());
        }
        ListTag componentData = new ListTag();
        for (ModifiedSpellPart<SpellEffect> component : this.components) {
            if (component != null) {
                componentData.add(component.toNBT());
            }
        }
        spell.put("components", componentData);
        CompoundTag modifierNBT = new CompoundTag();
        for (int i = 0; i < 3; i++) {
            if (this.getModifier(i) == null) {
                modifierNBT.putString("modifier_" + i, ManaAndArtifice.EMPTY.toString());
            } else {
                modifierNBT.putString("modifier_" + i, this.getModifier(i).getRegistryName().toString());
            }
        }
        spell.put("modifiers", modifierNBT);
        spell.putBoolean("mysterious", this.isMysterious());
        spell.putString("overrideAffinity", this.overrideAffinity.toString());
        if (this.hasOverrideManaCost()) {
            spell.putFloat("overrideManaCost", this.overrideManaCost);
        }
        nbt.put("spell", spell);
        nbt.putInt("spellColor", this.particleColorOverride);
    }

    public static void removeSpellFromTag(CompoundTag nbt) {
        nbt.remove("spell");
    }

    public static SpellRecipe fromNBT(CompoundTag nbt) {
        SpellRecipe recipe = new SpellRecipe();
        if (nbt == null) {
            return recipe;
        } else {
            CompoundTag spellNBT = new CompoundTag();
            if (nbt.contains("spell")) {
                spellNBT = nbt.getCompound("spell");
            }
            if (spellNBT.contains("shape")) {
                recipe.shape = ModifiedSpellPart.fromNBT(spellNBT.getCompound("shape"), (IForgeRegistry<Shape>) Registries.Shape.get());
            }
            if (spellNBT.contains("component")) {
                recipe.components.clear();
                recipe.components.add(ModifiedSpellPart.fromNBT(spellNBT.getCompound("component"), (IForgeRegistry) Registries.SpellEffect.get()));
            } else if (spellNBT.contains("components")) {
                for (Tag cINBT : spellNBT.getList("components", 10)) {
                    if (cINBT instanceof CompoundTag) {
                        ModifiedSpellPart<SpellEffect> comp = ModifiedSpellPart.fromNBT((CompoundTag) cINBT, (IForgeRegistry<SpellEffect>) Registries.SpellEffect.get());
                        if (comp != null) {
                            recipe.components.add(comp);
                        }
                    }
                }
            }
            if (spellNBT.contains("modifiers")) {
                CompoundTag modifierNBT = spellNBT.getCompound("modifiers");
                for (int i = 0; i < 3; i++) {
                    ResourceLocation rLoc = new ResourceLocation(modifierNBT.getString("modifier_" + i));
                    recipe.modifiers[i] = (Modifier) ((IForgeRegistry) Registries.Modifier.get()).getValue(rLoc);
                }
            }
            if (spellNBT.contains("mysterious")) {
                recipe.isMysterious = spellNBT.getBoolean("mysterious");
            }
            if (spellNBT.contains("overrideAffinity")) {
                recipe.overrideAffinity = Affinity.valueOf(spellNBT.getString("overrideAffinity"));
            }
            if (spellNBT.contains("overrideManaCost")) {
                recipe.overrideManaCost = spellNBT.getFloat("overrideManaCost");
            }
            recipe.particleColorOverride = getParticleColorOverride(nbt);
            recipe.recalculateSpellNumbers();
            return recipe;
        }
    }

    public static boolean stackContainsSpell(ItemStack stack) {
        if (!stack.hasTag()) {
            return false;
        } else {
            CompoundTag tag = stack.getTag();
            if (!tag.contains("spell")) {
                return false;
            } else {
                CompoundTag subTag = tag.getCompound("spell");
                return subTag.contains("shape") && (subTag.contains("component") || subTag.contains("components"));
            }
        }
    }

    public void calculateComplexity() {
        this.complexity = 0.0F;
        if (this.shape != null) {
            this.complexity = this.complexity + this.shape.getPart().initialComplexity();
            UnmodifiableIterator var1 = this.shape.getContainedAttributes().iterator();
            while (var1.hasNext()) {
                Attribute attr = (Attribute) var1.next();
                float modifiedValue = this.shape.getValueWithoutMultipliers(attr);
                float modificationTotal = Math.abs(modifiedValue - this.shape.getDefaultValue(attr));
                if (modificationTotal != 0.0F) {
                    this.complexity = this.complexity + (float) ((int) Math.ceil((double) (modificationTotal / this.shape.getStep(attr)))) * this.shape.getStepComplexity(attr);
                }
            }
        }
        for (ModifiedSpellPart<SpellEffect> component : this.components) {
            if (component != null) {
                this.complexity = this.complexity + component.getPart().initialComplexity();
                UnmodifiableIterator var9 = component.getContainedAttributes().iterator();
                while (var9.hasNext()) {
                    Attribute attr = (Attribute) var9.next();
                    float modifiedValue = component.getValueWithoutMultipliers(attr);
                    float modificationTotal = Math.abs(modifiedValue - component.getDefaultValue(attr));
                    if (modificationTotal != 0.0F) {
                        this.complexity = this.complexity + (float) ((int) Math.ceil((double) (modificationTotal / component.getStep(attr)))) * component.getStepComplexity(attr);
                    }
                }
            }
        }
    }

    public void calculateManaCost() {
        this.overrideManaCost = -1.0F;
        this.calculatedManaCost = this.complexity * 1.5F;
    }

    @Override
    public int getTier(@Nonnull Level world) {
        int tier = 1;
        if (this.shape != null) {
            tier = Math.max(tier, this.shape.getPart().getTier(world));
        }
        for (ModifiedSpellPart<SpellEffect> component : this.components) {
            if (component != null) {
                tier = Math.max(tier, component.getPart().getTier(world));
            }
        }
        for (Modifier modifier : this.modifiers) {
            if (modifier != null) {
                tier = Math.max(tier, modifier.getTier(world));
            }
        }
        return tier;
    }

    @Override
    public HashMap<Affinity, Float> getAffinity() {
        HashMap<Affinity, Float> affinityMap = new HashMap();
        if (this.hasOverrideAffinity()) {
            affinityMap.put(this.overrideAffinity, 1.0F);
            return affinityMap;
        } else {
            float total = 0.0F;
            HashMap<Affinity, Integer> affinityCount = new HashMap();
            for (ModifiedSpellPart<SpellEffect> component : this.components) {
                if (component != null) {
                    total++;
                    Affinity aff = component.getPart().getAffinity();
                    if (!affinityCount.containsKey(aff)) {
                        affinityCount.put(aff, 1);
                    } else {
                        affinityCount.put(aff, (Integer) affinityCount.get(aff) + 1);
                    }
                }
            }
            for (Affinity aff : affinityCount.keySet()) {
                affinityMap.put(aff, (float) ((Integer) affinityCount.get(aff)).intValue() / total);
            }
            if (affinityMap.size() == 0) {
                affinityMap.put(Affinity.UNKNOWN, 1.0F);
            }
            return affinityMap;
        }
    }

    @Override
    public Affinity getHighestAffinity() {
        return (Affinity) ((Entry) Collections.max(this.getAffinity().entrySet(), Comparator.comparingDouble(Entry::getValue))).getKey();
    }

    public void maximize() {
        if (this.shape != null) {
            this.shape.getContainedAttributes().forEach(a -> this.shape.setValue(a, this.shape.getMaximumValue(a)));
        }
        this.components.forEach(c -> c.getContainedAttributes().forEach(a -> c.setValue(a, this.shape.getMaximumValue(a))));
        this.recalculateSpellNumbers();
    }

    @Override
    public void addItemTooltip(ItemStack stack, Level worldIn, List<Component> tooltip, Player player) {
        String displayName = stack.getHoverName().getString();
        if (this.isValid() && !this.isMysterious()) {
            SpellCaster.applyAdjusters(stack, player, this, SpellCastStage.SPELL_TOOLTIP);
            NameProcessors.checkAndAddDisplay(this, displayName, tooltip);
            if (!Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("item.mna.spell.shift_prompt").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else {
                ModifiedSpellPart<Shape> shape = this.getShape();
                if (shape != null) {
                    tooltip.add(Component.translatable("item.mna.spell.shape_attributes").withStyle(ChatFormatting.GREEN));
                    UnmodifiableIterator reagent = shape.getPart().getModifiableAttributes().iterator();
                    while (reagent.hasNext()) {
                        AttributeValuePair attr = (AttributeValuePair) reagent.next();
                        tooltip.add(Component.translatable("item.mna.spell.attribute_display", attr.getAttribute().name(), shape.getValue(attr.getAttribute())).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    }
                }
                tooltip.add(Component.literal(" "));
                this.iterateComponents(component -> {
                    String componentName = Component.translatable(((SpellEffect) component.getPart()).getRegistryName().toString()).getString();
                    tooltip.add(Component.translatable("item.mna.spell.component_attributes", componentName).withStyle(ChatFormatting.GREEN));
                    UnmodifiableIterator var3x = ((SpellEffect) component.getPart()).getModifiableAttributes().iterator();
                    while (var3x.hasNext()) {
                        AttributeValuePair attrx = (AttributeValuePair) var3x.next();
                        tooltip.add(Component.translatable("item.mna.spell.attribute_display", attrx.getAttribute().name(), component.getValue(attrx.getAttribute())).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    }
                });
                tooltip.add(Component.literal(" "));
                if (this.isChanneled()) {
                    tooltip.add(Component.translatable("item.mna.spell.channeled_mana_cost_display", String.format("%.2f", this.getManaCost() * 20.0F)).withStyle(ChatFormatting.GOLD));
                } else {
                    tooltip.add(Component.translatable("item.mna.spell.mana_cost_display", String.format("%.2f", this.getManaCost())).withStyle(ChatFormatting.GOLD));
                }
                tooltip.add(Component.translatable("item.mna.spell.complexity_display", this.getComplexity()).withStyle(ChatFormatting.GOLD));
                tooltip.add(Component.literal(" "));
            }
            if (this.getReagents(ManaAndArtifice.instance.proxy.getClientPlayer(), null, null).size() > 0) {
                if (Screen.hasAltDown()) {
                    tooltip.add(Component.translatable("item.mna.spell.required_reagents").withStyle(ChatFormatting.GOLD));
                    for (SpellReagent reagent : this.getReagents(ManaAndArtifice.instance.proxy.getClientPlayer(), null, null)) {
                        reagent.getAddedBy().addReagentTooltip(ManaAndArtifice.instance.proxy.getClientPlayer(), null, tooltip, reagent);
                    }
                    tooltip.add(Component.literal(" "));
                } else {
                    tooltip.add(Component.translatable("item.mna.spell.alt_prompt").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                }
            }
        }
    }

    @Override
    public List<SpellReagent> getReagents(@Nullable Player caster, @Nullable InteractionHand hand, @Nullable SpellCastingResult spellResult) {
        ArrayList<SpellReagent> reagents = new ArrayList();
        List<SpellReagent> shape_reagents = this.getShape().getPart().getRequiredReagents(caster);
        if (shape_reagents != null) {
            reagents.addAll(shape_reagents);
        }
        for (int i = 0; i < this.components.size(); i++) {
            SpellEffect c = this.getComponent(i).getPart();
            if (spellResult == null || spellResult.getResultFor(c) == ComponentApplicationResult.SUCCESS || spellResult.getResultFor(c) == ComponentApplicationResult.DELAYED || spellResult.getResultFor(c) == ComponentApplicationResult.TARGET_ENTITY_SPAWNED) {
                List<SpellReagent> component_reagents = c.getRequiredReagents(caster, hand);
                if (component_reagents != null) {
                    reagents.addAll(component_reagents);
                }
            }
        }
        SpellReagentsEvent event = new SpellReagentsEvent(this, caster, reagents);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getRequiredReagents();
    }

    @Override
    public boolean containsPart(ResourceLocation partID) {
        if (this.getShape() != null && this.getShape().getPart().getRegistryName().equals(partID)) {
            return true;
        } else {
            for (IModifiedSpellPart<SpellEffect> effect : this.getComponents()) {
                if (effect.getPart().getRegistryName().equals(partID)) {
                    return true;
                }
            }
            for (int i = 0; i < 3; i++) {
                Modifier m = this.getModifier(i);
                if (m != null && m.getRegistryName().equals(partID)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean isFactionSpell(IFaction faction) {
        if (this.getShape() != null) {
            IFaction partReq = this.getShape().getPart().getFactionRequirement();
            if (partReq != null && partReq == faction) {
                return true;
            }
        }
        for (IModifiedSpellPart<SpellEffect> effect : this.getComponents()) {
            IFaction partReq = effect.getPart().getFactionRequirement();
            if (partReq != null && partReq == faction) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            Modifier m = this.getModifier(i);
            if (m != null) {
                IFaction partReq = m.getFactionRequirement();
                if (partReq != null && partReq == faction) {
                    return true;
                }
            }
        }
        return false;
    }

    public void writeRecipeForRitual(Level world, CompoundTag nbt) {
        if (this.components.size() == 1) {
            CompoundTag ritualReagentData = new CompoundTag();
            NonNullList<ResourceLocation> patterns = NonNullList.create();
            if (this.shape != null) {
                ShapeRecipe shapeRecipe = ItemAndPatternRecipeHelper.GetRecipe(world, this.shape.getPart().getRegistryName(), RecipeInit.SHAPE_TYPE.get());
                if (shapeRecipe != null) {
                    this.WriteRLocList(ritualReagentData, Arrays.asList(shapeRecipe.getRequiredItems()), "shape_items");
                    for (ResourceLocation rLoc : shapeRecipe.getRequiredPatterns()) {
                        patterns.add(rLoc);
                    }
                }
            }
            ModifiedSpellPart<SpellEffect> component = (ModifiedSpellPart<SpellEffect>) this.components.get(0);
            if (component != null) {
                ComponentRecipe componentRecipe = ItemAndPatternRecipeHelper.GetRecipe(world, component.getPart().getRegistryName(), RecipeInit.COMPONENT_TYPE.get());
                if (componentRecipe != null) {
                    this.WriteRLocList(ritualReagentData, Arrays.asList(componentRecipe.getRequiredItems()), "component_items");
                    for (ResourceLocation rLoc : componentRecipe.getRequiredPatterns()) {
                        patterns.add(rLoc);
                    }
                }
            }
            for (int i = 0; i < this.modifiers.length; i++) {
                if (this.modifiers[i] != null) {
                    ModifierRecipe modifierRecipe = ItemAndPatternRecipeHelper.GetRecipe(world, this.modifiers[i].getRegistryName(), RecipeInit.MODIFIER_TYPE.get());
                    if (modifierRecipe != null) {
                        this.WriteRLocList(ritualReagentData, Arrays.asList(modifierRecipe.getRequiredItems()), "modifier_" + i + "_items");
                        for (ResourceLocation rLoc : modifierRecipe.getRequiredPatterns()) {
                            patterns.add(rLoc);
                        }
                    }
                }
            }
            this.WriteRLocList(ritualReagentData, patterns, "pattern");
            nbt.put("ritual_reagent_data", ritualReagentData);
        }
    }

    private void WriteRLocList(CompoundTag nbt, Collection<ResourceLocation> list, String prefix) {
        if (list.size() != 0) {
            int count = 0;
            nbt.putInt(prefix + "_count", list.size());
            for (ResourceLocation rLoc : list) {
                nbt.putString(prefix + "_" + count++, rLoc.toString());
            }
        }
    }

    private static NonNullList<ResourceLocation> ReadRLocList(CompoundTag nbt, String prefix) {
        NonNullList<ResourceLocation> rLocs = NonNullList.create();
        if (!nbt.contains(prefix + "_count")) {
            return rLocs;
        } else {
            int count = nbt.getInt(prefix + "_count");
            for (int i = 0; i < count; i++) {
                String key = prefix + "_" + i;
                if (nbt.contains(key)) {
                    rLocs.add(new ResourceLocation(nbt.getString(key)));
                } else {
                    ManaAndArtifice.LOGGER.error("Failed to read key '" + key + "' from dynamic ritual spell recipe NBT (not found).");
                }
            }
            return rLocs;
        }
    }

    public static boolean isReagentContainer(ItemStack recipeStack) {
        CompoundTag nbt = recipeStack.getTag();
        return nbt != null && nbt.contains("ritual_reagent_data");
    }

    public static NonNullList<ResourceLocation> getShapeReagents(ItemStack recipeStack) {
        CompoundTag ritualReagentData = recipeStack.getOrCreateTagElement("ritual_reagent_data");
        return ReadRLocList(ritualReagentData, "shape_items");
    }

    public static NonNullList<ResourceLocation> getComponentReagents(ItemStack recipeStack) {
        CompoundTag ritualReagentData = recipeStack.getOrCreateTagElement("ritual_reagent_data");
        return ReadRLocList(ritualReagentData, "component_items");
    }

    public static NonNullList<ResourceLocation> getModifierReagents(ItemStack recipeStack, int modifierIndex) {
        CompoundTag ritualReagentData = recipeStack.getOrCreateTagElement("ritual_reagent_data");
        return ReadRLocList(ritualReagentData, "modifier_" + modifierIndex + "_items");
    }

    public static NonNullList<ResourceLocation> getPatterns(ItemStack recipeStack) {
        CompoundTag ritualReagentData = recipeStack.getOrCreateTagElement("ritual_reagent_data");
        return ReadRLocList(ritualReagentData, "pattern");
    }

    public static int getParticleColorOverride(CompoundTag nbt) {
        return nbt.contains("spellColor") ? nbt.getInt("spellColor") : -1;
    }

    public ItemStack createAsSpell() {
        ItemStack newSpell = new ItemStack(ItemInit.SPELL.get());
        this.writeToNBT(newSpell.getOrCreateTag());
        return newSpell;
    }

    @Override
    public boolean isChargedSpell(ItemStack stack) {
        return stack.getItem() instanceof ItemStaff ? ((ItemStaff) stack.getItem()).isChargeSpell(stack) : false;
    }
}