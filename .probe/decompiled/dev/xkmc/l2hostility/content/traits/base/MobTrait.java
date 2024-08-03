package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHDamageTypes;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.base.NamedEntry;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntSupplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MobTrait extends NamedEntry<MobTrait> implements ItemLike {

    private final IntSupplier color;

    public MobTrait(ChatFormatting format) {
        this(format::m_126665_);
    }

    public MobTrait(IntSupplier color) {
        super(LHTraits.TRAITS);
        this.color = color;
    }

    public TraitConfig getConfig() {
        TraitConfig ans = L2Hostility.TRAIT.getEntry(this.getRegistryName());
        return ans == null ? TraitConfig.DEFAULT : ans;
    }

    public int getCost(double factor) {
        return Math.max(1, (int) Math.round((double) this.getConfig().cost * factor));
    }

    public int getMaxLevel() {
        return this.getConfig().max_rank;
    }

    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        if (this.isBanned()) {
            return false;
        } else {
            TraitConfig config = this.getConfig();
            if (difficulty < config.min_level) {
                return false;
            } else {
                return !EntityConfig.allow(le.m_6095_(), this) ? false : config.allows(le.m_6095_());
            }
        }
    }

    public final boolean allow(LivingEntity le) {
        return this.allow(le, Integer.MAX_VALUE, TraitManager.getMaxLevel() + 1);
    }

    public void initialize(LivingEntity mob, int level) {
    }

    public void postInit(LivingEntity mob, int lv) {
    }

    public void tick(LivingEntity mob, int level) {
    }

    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        LivingHurtEvent e = cache.getLivingHurtEvent();
        assert e != null;
        if (e.getAmount() > 0.0F && !e.getSource().is(LHDamageTypes.KILLER_AURA)) {
            this.postHurtPlayer(level, attacker, traitCache);
        }
    }

    public void postHurtPlayer(int level, LivingEntity attacker, TraitEffectCache traitCache) {
        if (traitCache.reflectTrait(this)) {
            for (Mob e : traitCache.getTargets()) {
                this.postHurtImpl(level, attacker, e);
            }
        } else {
            this.postHurtImpl(level, attacker, traitCache.target);
        }
    }

    public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
    }

    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
    }

    public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
    }

    public void onCreateSource(int level, LivingEntity attacker, CreateSourceEvent event) {
    }

    public void onDamaged(int level, LivingEntity mob, AttackCache cache) {
    }

    public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
    }

    public MutableComponent getFullDesc(@Nullable Integer value) {
        MutableComponent ans = this.getDesc();
        if (value != null) {
            ans = ans.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + value));
        }
        return ans.withStyle(Style.EMPTY.withColor(this.color.getAsInt()));
    }

    public int getColor() {
        return this.color.getAsInt();
    }

    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    protected MutableComponent mapLevel(Function<Integer, MutableComponent> func) {
        MutableComponent comp = null;
        for (int i = 1; i <= this.getMaxLevel(); i++) {
            if (comp == null) {
                comp = (MutableComponent) func.apply(i);
            } else {
                comp = comp.append(Component.literal("/").withStyle(ChatFormatting.GRAY)).append((Component) func.apply(i));
            }
        }
        assert comp != null;
        return comp;
    }

    @Override
    public Item asItem() {
        Item item = ForgeRegistries.ITEMS.getValue(this.getRegistryName());
        if (item == null) {
            item = Items.AIR;
        }
        return item;
    }

    public boolean isBanned() {
        if (LHTraits.TRAITS.get().getKey(this) == null) {
            L2Hostility.LOGGER.error("------------");
            L2Hostility.LOGGER.error("Trait " + this.getClass().getSimpleName() + " is not registered. Why?");
            Set<ResourceLocation> set = LHTraits.TRAITS.get().getKeys();
            L2Hostility.LOGGER.error("List of all ids registered: ");
            for (ResourceLocation e : set) {
                L2Hostility.LOGGER.error(e.toString());
            }
            L2Hostility.LOGGER.error("------------");
        }
        return LHConfig.COMMON.map.containsKey(this.getRegistryName().getPath()) ? !((ForgeConfigSpec.BooleanValue) LHConfig.COMMON.map.get(this.getRegistryName().getPath())).get() : false;
    }

    public String toString() {
        return this.getID();
    }

    public int inherited(MobTraitCap mobTraitCap, int rank, InheritContext ctx) {
        return rank;
    }

    public boolean is(TagKey<MobTrait> tag) {
        return LHTraits.TRAITS.get().tags().getTag(tag).contains(this);
    }
}