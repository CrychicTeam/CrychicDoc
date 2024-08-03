package com.mna.enchantments.auras;

import com.mna.api.events.AuraEvent;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.enchantments.base.MAEnchantmentBase;
import com.mna.items.ItemInit;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;

public class Aura extends MAEnchantmentBase {

    private int _minLevel = 1;

    private int _maxLevel = 1;

    private int _duration = 100;

    private int _magnitude;

    private int _radius = 25;

    private float _manaCost;

    private MobEffect _effect;

    private RegistryObject<MobEffect> _deferredEffect;

    private Predicate<Player> _applicationPredicate;

    public Aura(Enchantment.Rarity rarityIn) {
        super(rarityIn, EnchantmentCategory.ARMOR, new EquipmentSlot[] { EquipmentSlot.CHEST });
        this._magnitude = 0;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        boolean result = false;
        if (stack.getItem() instanceof ArmorItem armor) {
            EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(stack);
            result = slot == EquipmentSlot.CHEST;
        }
        return result;
    }

    public boolean canEnchant(ItemStack stack, Player enchanter) {
        boolean canEnchant = this.canEnchant(stack);
        AuraEvent.Compatibility event = new AuraEvent.Compatibility(this, null, stack, enchanter);
        event.setResult(canEnchant ? Result.ALLOW : Result.DENY);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() == Result.ALLOW;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return !(ench instanceof Aura);
    }

    public boolean isCompatibleWith(Enchantment other, Player enchanter) {
        boolean isCompatible = this.m_44695_(other);
        AuraEvent.Compatibility event = new AuraEvent.Compatibility(this, other, ItemStack.EMPTY, enchanter);
        event.setResult(isCompatible ? Result.ALLOW : Result.DENY);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() == Result.ALLOW;
    }

    @Override
    public int getMinLevel() {
        return this._minLevel;
    }

    @Override
    public int getMaxLevel() {
        return this._maxLevel;
    }

    public Aura withDuration(int duration) {
        this._duration = duration;
        return this;
    }

    public Aura withMagnitude(int magnitude) {
        this._magnitude = magnitude;
        return this;
    }

    public Aura withRadius(int radius) {
        this._radius = radius;
        return this;
    }

    public Aura withManaCost(float manaCost) {
        this._manaCost = manaCost;
        return this;
    }

    public Aura withEffect(MobEffect effect) {
        this._effect = effect;
        return this;
    }

    public Aura withEffect(RegistryObject<MobEffect> effect) {
        this._deferredEffect = effect;
        return this;
    }

    public Aura withPredicate(Predicate<Player> predicate) {
        this._applicationPredicate = predicate;
        return this;
    }

    public Aura withMinLevel(int minLevel) {
        this._minLevel = minLevel;
        return this;
    }

    public Aura withMaxLevel(int maxLevel) {
        this._maxLevel = maxLevel;
        return this;
    }

    private MobEffect getEffect() {
        if (this._effect == null && this._deferredEffect != null && this._deferredEffect.isPresent()) {
            this._effect = this._deferredEffect.get();
        }
        return this._effect;
    }

    public void apply(Player source, int enchantmentLevel, boolean manaOnly) {
        if (this._applicationPredicate == null || this._applicationPredicate.test(source)) {
            if (!MinecraftForge.EVENT_BUS.post(new AuraEvent.Tick(this, source))) {
                source.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                    boolean hasBelt = CuriosApi.getCuriosHelper().findFirstCurio(source, ItemInit.BELT_SELFISHNESS.get()).isPresent();
                    List<Player> players = null;
                    double radius = (double) this._radius;
                    if (hasBelt) {
                        players = Arrays.asList(source);
                    } else {
                        AuraEvent.Radius radiusEvent = new AuraEvent.Radius(this, source, (double) this._radius);
                        MinecraftForge.EVENT_BUS.post(radiusEvent);
                        radius = radiusEvent.getRadius();
                        players = source.m_9236_().m_45976_(Player.class, source.m_20191_().inflate(radius));
                    }
                    int magnitude = enchantmentLevel - 1 + this._magnitude;
                    for (Player player : players) {
                        float adjustedManaCost = this._manaCost * (float) (hasBelt ? 2 : 5);
                        AuraEvent.Numerics numericsEvent = new AuraEvent.Numerics(this, source, player, radius, magnitude, adjustedManaCost, hasBelt);
                        MinecraftForge.EVENT_BUS.post(numericsEvent);
                        if (!m.getCastingResource().hasEnoughAbsolute(player, numericsEvent.getManaCost())) {
                            break;
                        }
                        if (source.m_5647_() == null || player.m_7307_(source)) {
                            if (!manaOnly) {
                                MobEffectInstance newInst = new MobEffectInstance(this.getEffect(), this._duration, numericsEvent.getMagnitude(), true, false);
                                player.m_7292_(newInst);
                            }
                            m.getCastingResource().consume(source, numericsEvent.getManaCost());
                        }
                    }
                });
            }
        }
    }
}