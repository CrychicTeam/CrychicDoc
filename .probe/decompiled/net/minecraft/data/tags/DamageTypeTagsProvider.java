package net.minecraft.data.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public class DamageTypeTagsProvider extends TagsProvider<DamageType> {

    public DamageTypeTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        super(packOutput0, Registries.DAMAGE_TYPE, completableFutureHolderLookupProvider1);
    }

    @Override
    protected void addTags(HolderLookup.Provider holderLookupProvider0) {
        this.m_206424_(DamageTypeTags.DAMAGES_HELMET).add(DamageTypes.FALLING_ANVIL, DamageTypes.FALLING_BLOCK, DamageTypes.FALLING_STALACTITE);
        this.m_206424_(DamageTypeTags.BYPASSES_ARMOR).add(DamageTypes.ON_FIRE, DamageTypes.IN_WALL, DamageTypes.CRAMMING, DamageTypes.DROWN, DamageTypes.FLY_INTO_WALL, DamageTypes.GENERIC, DamageTypes.WITHER, DamageTypes.DRAGON_BREATH, DamageTypes.STARVE, DamageTypes.FALL, DamageTypes.FREEZE, DamageTypes.STALAGMITE, DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL, DamageTypes.SONIC_BOOM, DamageTypes.OUTSIDE_BORDER);
        this.m_206424_(DamageTypeTags.BYPASSES_SHIELD).addTag(DamageTypeTags.BYPASSES_ARMOR).add(DamageTypes.FALLING_ANVIL, DamageTypes.FALLING_STALACTITE);
        this.m_206424_(DamageTypeTags.BYPASSES_INVULNERABILITY).add(DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL);
        this.m_206424_(DamageTypeTags.BYPASSES_EFFECTS).add(DamageTypes.STARVE);
        this.m_206424_(DamageTypeTags.BYPASSES_RESISTANCE).add(DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL);
        this.m_206424_(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(DamageTypes.SONIC_BOOM);
        this.m_206424_(DamageTypeTags.IS_FIRE).add(DamageTypes.IN_FIRE, DamageTypes.ON_FIRE, DamageTypes.LAVA, DamageTypes.HOT_FLOOR, DamageTypes.UNATTRIBUTED_FIREBALL, DamageTypes.FIREBALL);
        this.m_206424_(DamageTypeTags.IS_PROJECTILE).add(DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.MOB_PROJECTILE, DamageTypes.UNATTRIBUTED_FIREBALL, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.THROWN);
        this.m_206424_(DamageTypeTags.WITCH_RESISTANT_TO).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, DamageTypes.THORNS);
        this.m_206424_(DamageTypeTags.IS_EXPLOSION).add(DamageTypes.FIREWORKS, DamageTypes.EXPLOSION, DamageTypes.PLAYER_EXPLOSION, DamageTypes.BAD_RESPAWN_POINT);
        this.m_206424_(DamageTypeTags.IS_FALL).add(DamageTypes.FALL, DamageTypes.STALAGMITE);
        this.m_206424_(DamageTypeTags.IS_DROWNING).add(DamageTypes.DROWN);
        this.m_206424_(DamageTypeTags.IS_FREEZING).add(DamageTypes.FREEZE);
        this.m_206424_(DamageTypeTags.IS_LIGHTNING).add(DamageTypes.LIGHTNING_BOLT);
        this.m_206424_(DamageTypeTags.NO_ANGER).add(DamageTypes.MOB_ATTACK_NO_AGGRO);
        this.m_206424_(DamageTypeTags.NO_IMPACT).add(DamageTypes.DROWN);
        this.m_206424_(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL).add(DamageTypes.FELL_OUT_OF_WORLD);
        this.m_206424_(DamageTypeTags.WITHER_IMMUNE_TO).add(DamageTypes.DROWN);
        this.m_206424_(DamageTypeTags.IGNITES_ARMOR_STANDS).add(DamageTypes.IN_FIRE);
        this.m_206424_(DamageTypeTags.BURNS_ARMOR_STANDS).add(DamageTypes.ON_FIRE);
        this.m_206424_(DamageTypeTags.AVOIDS_GUARDIAN_THORNS).add(DamageTypes.MAGIC, DamageTypes.THORNS).addTag(DamageTypeTags.IS_EXPLOSION);
        this.m_206424_(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH).add(DamageTypes.MAGIC);
        this.m_206424_(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS).addTag(DamageTypeTags.IS_EXPLOSION);
    }
}