package com.squoshi.irons_spells_js.spell;

import com.squoshi.irons_spells_js.IronsSpellsJSPlugin;
import com.squoshi.irons_spells_js.util.ISSKJSUtils;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class CustomSpell extends AbstractSpell {

    private final ResourceLocation spellResource;

    private final DefaultConfig defaultConfig;

    private final CastType castType;

    private final ISSKJSUtils.SoundEventHolder startSound;

    private final ISSKJSUtils.SoundEventHolder finishSound;

    private final Consumer<CustomSpell.CastContext> onCast;

    private final Consumer<CustomSpell.CastClientContext> onClientCast;

    private final Consumer<CustomSpell.PreCastContext> onPreCast;

    private final Consumer<CustomSpell.PreCastClientContext> onPreClientCast;

    private final boolean allowLooting;

    private final boolean needsLearning;

    private final Predicate<Player> canBeCrafted;

    private final BiFunction<Integer, LivingEntity, List<MutableComponent>> uniqueInfo;

    private final AnimationHolder castStartAnimation;

    private final AnimationHolder castFinishAnimation;

    private final Predicate<CustomSpell.PreCastTargetingContext> preCastConditions;

    public CustomSpell(CustomSpell.Builder b) {
        this.spellResource = b.spellResource;
        this.defaultConfig = new DefaultConfig().setMinRarity(b.minRarity).setSchoolResource(b.school).setMaxLevel(b.maxLevel).setCooldownSeconds((double) b.cooldownSeconds).build();
        this.castType = b.castType;
        this.startSound = b.startSound;
        this.finishSound = b.finishSound;
        this.onCast = b.onCast;
        this.onClientCast = b.onClientCast;
        this.onPreCast = b.onPreCast;
        this.onPreClientCast = b.onPreClientCast;
        this.manaCostPerLevel = b.manaCostPerLevel;
        this.baseSpellPower = b.baseSpellPower;
        this.spellPowerPerLevel = b.spellPowerPerLevel;
        this.castTime = b.castTime;
        this.baseManaCost = b.baseManaCost;
        this.allowLooting = b.allowLooting;
        this.needsLearning = b.needsLearning;
        this.canBeCrafted = b.canBeCrafted;
        this.uniqueInfo = b.uniqueInfo;
        this.castStartAnimation = b.castStartAnimation;
        this.castFinishAnimation = b.castFinishAnimation;
        this.preCastConditions = b.preCastConditions;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellResource;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return this.castType;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return this.startSound != null ? Optional.ofNullable(ForgeRegistries.SOUND_EVENTS.getValue(this.startSound.getLocation())) : super.getCastStartSound();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return this.finishSound != null ? Optional.ofNullable(ForgeRegistries.SOUND_EVENTS.getValue(this.finishSound.getLocation())) : super.getCastFinishSound();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (this.onCast != null) {
            CustomSpell.CastContext context = new CustomSpell.CastContext(level, spellLevel, entity, castSource, playerMagicData);
            ISSKJSUtils.safeCallback(this.onCast, context, "Error while calling onCast");
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        if (this.onClientCast != null) {
            CustomSpell.CastClientContext context = new CustomSpell.CastClientContext(level, spellLevel, entity, castData);
            ISSKJSUtils.safeCallback(this.onClientCast, context, "Error while calling onClientCast");
        }
        super.onClientCast(level, spellLevel, entity, castData);
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (this.onPreCast != null) {
            CustomSpell.PreCastContext context = new CustomSpell.PreCastContext(level, spellLevel, entity, playerMagicData);
            ISSKJSUtils.safeCallback(this.onPreCast, context, "Error while calling onPreCast");
        }
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, MagicData playerMagicData) {
        if (this.onPreClientCast != null) {
            CustomSpell.PreCastClientContext context = new CustomSpell.PreCastClientContext(level, spellLevel, entity, hand, playerMagicData);
            ISSKJSUtils.safeCallback(this.onPreClientCast, context, "Error while calling onPreClientCast");
        }
        super.onClientPreCast(level, spellLevel, entity, hand, playerMagicData);
    }

    @Override
    public boolean allowLooting() {
        return this.allowLooting;
    }

    @Override
    public boolean needsLearning() {
        return this.needsLearning;
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        return this.canBeCrafted != null ? this.canBeCrafted.test(player) : true;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return this.uniqueInfo != null ? (List) this.uniqueInfo.apply(spellLevel, caster) : super.getUniqueInfo(spellLevel, caster);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return this.castStartAnimation != null ? this.castStartAnimation : super.getCastStartAnimation();
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return this.castFinishAnimation != null ? this.castFinishAnimation : super.getCastFinishAnimation();
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return this.preCastConditions != null ? this.preCastConditions.test(new CustomSpell.PreCastTargetingContext(level, spellLevel, entity, playerMagicData, this)) : super.checkPreCastConditions(level, spellLevel, entity, playerMagicData);
    }

    public static class Builder extends BuilderBase<CustomSpell> {

        private SpellRarity minRarity = SpellRarity.COMMON;

        private ResourceLocation school = SchoolRegistry.BLOOD_RESOURCE;

        private int maxLevel = 10;

        private int cooldownSeconds = 20;

        private CastType castType = CastType.INSTANT;

        private ISSKJSUtils.SoundEventHolder startSound = null;

        private ISSKJSUtils.SoundEventHolder finishSound = null;

        private final ResourceLocation spellResource;

        private Consumer<CustomSpell.CastContext> onCast = null;

        private Consumer<CustomSpell.CastClientContext> onClientCast = null;

        private Consumer<CustomSpell.PreCastContext> onPreCast = null;

        private Consumer<CustomSpell.PreCastClientContext> onPreClientCast = null;

        private int manaCostPerLevel = 20;

        private int baseSpellPower = 0;

        private int spellPowerPerLevel = 1;

        private int castTime = 0;

        private int baseManaCost = 40;

        private boolean allowLooting = false;

        private boolean needsLearning = false;

        private Predicate<Player> canBeCrafted = null;

        private BiFunction<Integer, LivingEntity, List<MutableComponent>> uniqueInfo;

        private AnimationHolder castStartAnimation = null;

        private AnimationHolder castFinishAnimation = null;

        private Predicate<CustomSpell.PreCastTargetingContext> preCastConditions = null;

        public Builder(ResourceLocation i) {
            super(i);
            this.spellResource = i;
        }

        @Info("    Sets the cast type. Can be `CONTINUOUS`, `INSTANT`, `LONG`, or `NONE`.\n")
        public CustomSpell.Builder setCastType(CastType type) {
            this.castType = type;
            return this;
        }

        @Info("    Sets the sound that the spell will play when it starts casting.\n")
        public CustomSpell.Builder setStartSound(ISSKJSUtils.SoundEventHolder soundEvent) {
            this.startSound = soundEvent;
            return this;
        }

        @Info("    Sets the sound that the spell will play after it is done casting.\n")
        public CustomSpell.Builder setFinishSound(ISSKJSUtils.SoundEventHolder soundEvent) {
            this.finishSound = soundEvent;
            return this;
        }

        @Info("    Sets the rarity of the spell. Can be `COMMON`, `UNCOMMON`, `RARE`, `EPIC`, or `LEGENDARY`.\n")
        public CustomSpell.Builder setMinRarity(SpellRarity rarity) {
            this.minRarity = rarity;
            return this;
        }

        @Info("    Sets the school of the spell. The different schools each are a resource location.\n\n    Example: `.setSchool(SchoolRegistry.BLOOD_RESOURCE`\n    Another example: `setSchool('irons_spellbooks:blood')`\n")
        public CustomSpell.Builder setSchool(ISSKJSUtils.SchoolHolder schoolHolder) {
            this.school = schoolHolder.getLocation();
            return this;
        }

        @Info("    Sets the max level of the spell. Goes up to `10` from `1`.\n")
        public CustomSpell.Builder setMaxLevel(int level) {
            this.maxLevel = level;
            return this;
        }

        @Info("    Sets the cooldown of the spell in seconds. Cannot be a decimal value for some reason.\n")
        public CustomSpell.Builder setCooldownSeconds(int seconds) {
            this.cooldownSeconds = seconds;
            return this;
        }

        @Info("    Sets the mana cost per the spell's level. For example, you could input `10` into this method, and each level of the spell will multiply that value by the level.\n")
        public CustomSpell.Builder setManaCostPerLevel(int cost) {
            this.manaCostPerLevel = cost;
            return this;
        }

        @Info("    Sets the base spell power. Can be from `1` to `10`. The spell power per level adds on to this.\n")
        public CustomSpell.Builder setBaseSpellPower(int power) {
            this.baseSpellPower = power;
            return this;
        }

        @Info("    Sets the spell power per level.\n")
        public CustomSpell.Builder setSpellPowerPerLevel(int power) {
            this.spellPowerPerLevel = power;
            return this;
        }

        @Info("    Sets the cast time. This is used for `LONG` or `CONTINUOUS` spell types.\n")
        public CustomSpell.Builder setCastTime(int time) {
            this.castTime = time;
            return this;
        }

        @Info("    Sets the base mana cost. The mana cost per level adds on to this.\n")
        public CustomSpell.Builder setBaseManaCost(int cost) {
            this.baseManaCost = cost;
            return this;
        }

        @Info("    Sets the callback for when the spell is cast. This is what the spell does when it is casted.\n")
        public CustomSpell.Builder onCast(Consumer<CustomSpell.CastContext> consumer) {
            this.onCast = consumer;
            return this;
        }

        @Info("    Sets the callback for when the spell is cast on the client side. This is what the spell does when it is casted.\n")
        public CustomSpell.Builder onClientCast(Consumer<CustomSpell.CastClientContext> consumer) {
            this.onClientCast = consumer;
            return this;
        }

        @Info("    Sets the callback for when the spell is about to be cast. This is what the spell does before it is casted.\n")
        public CustomSpell.Builder onPreCast(Consumer<CustomSpell.PreCastContext> consumer) {
            this.onPreCast = consumer;
            return this;
        }

        @Info("    Sets the callback for when the spell is about to be cast on the client side. This is what the spell does before it is casted.\n")
        public CustomSpell.Builder onPreClientCast(Consumer<CustomSpell.PreCastClientContext> consumer) {
            this.onPreClientCast = consumer;
            return this;
        }

        @Info("    Sets whether or not the spell can be looted from a loot table.\n")
        public CustomSpell.Builder setAllowLooting(boolean allow) {
            this.allowLooting = allow;
            return this;
        }

        @Info("    Sets whether or not the spell needs to be learned before it can be casted.\n")
        public CustomSpell.Builder needsLearning(boolean needs) {
            this.needsLearning = needs;
            return this;
        }

        @Info("    Sets the predicate for whether or not the spell can be crafted by a player.\n")
        public CustomSpell.Builder canBeCraftedBy(Predicate<Player> predicate) {
            this.canBeCrafted = predicate;
            return this;
        }

        @Info("    Sets the unique info for the spell. It is what is displayed on the spell in-game, e.g how some spells have damage values listed.\n")
        public CustomSpell.Builder setUniqueInfo(BiFunction<Integer, LivingEntity, List<MutableComponent>> info) {
            this.uniqueInfo = info;
            return this;
        }

        @Info("    Sets the cast start animation for the spell.\n")
        public CustomSpell.Builder setCastStartAnimation(String path, boolean playOnce, boolean animatesLegs) {
            this.castStartAnimation = new AnimationHolder(path, playOnce, animatesLegs);
            return this;
        }

        @Info("    Sets the cast finish animation for the spell.\n")
        public CustomSpell.Builder setCastFinishAnimation(String path, boolean playOnce, boolean animatesLegs) {
            this.castFinishAnimation = new AnimationHolder(path, playOnce, animatesLegs);
            return this;
        }

        @Info("    Sets the pre-cast conditions for the spell. It is a Predicate, which means it requires a boolean return value. This can be used for targeting spells and for cancelling the spell before it is casted.\n\n    Example: ```js\n    .checkPreCastConditions(ctx => {\n        return ISSUtils.preCastTargetHelper(ctx.level, ctx.entity, ctx.playerMagicData, ctx.spell, 48, 0.35)\n    })\n    ```\n")
        public CustomSpell.Builder checkPreCastConditions(Predicate<CustomSpell.PreCastTargetingContext> predicate) {
            this.preCastConditions = predicate;
            return this;
        }

        @Override
        public RegistryInfo<AbstractSpell> getRegistryType() {
            return IronsSpellsJSPlugin.SPELL_REGISTRY;
        }

        public CustomSpell createObject() {
            return new CustomSpell(this);
        }
    }

    static record CastClientContext(Level getLevel, int getSpellLevel, LivingEntity getEntity, ICastData getCastData) {
    }

    static record CastContext(Level getLevel, int getSpellLevel, LivingEntity getEntity, CastSource getCastSource, MagicData getPlayerMagicData) {
    }

    static record PreCastClientContext(Level getLevel, int getSpellLevel, LivingEntity getEntity, InteractionHand getHand, MagicData getPlayerMagicData) {
    }

    static record PreCastContext(Level getLevel, int getSpellLevel, LivingEntity getEntity, MagicData getPlayerMagicData) {
    }

    static record PreCastTargetingContext(Level getLevel, int getSpellLevel, LivingEntity getEntity, MagicData getPlayerMagicData, AbstractSpell getSpell) {
    }
}