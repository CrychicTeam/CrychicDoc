package net.minecraft.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

public class ArmorItem extends Item implements Equipable {

    private static final EnumMap<ArmorItem.Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap(ArmorItem.Type.class), p_266744_ -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {

        @Override
        protected ItemStack execute(BlockSource p_40408_, ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };

    protected final ArmorItem.Type type;

    private final int defense;

    private final float toughness;

    protected final float knockbackResistance;

    protected final ArmorMaterial material;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public static boolean dispenseArmor(BlockSource blockSource0, ItemStack itemStack1) {
        BlockPos $$2 = blockSource0.getPos().relative((Direction) blockSource0.getBlockState().m_61143_(DispenserBlock.FACING));
        List<LivingEntity> $$3 = blockSource0.getLevel().m_6443_(LivingEntity.class, new AABB($$2), EntitySelector.NO_SPECTATORS.and(new EntitySelector.MobCanWearArmorEntitySelector(itemStack1)));
        if ($$3.isEmpty()) {
            return false;
        } else {
            LivingEntity $$4 = (LivingEntity) $$3.get(0);
            EquipmentSlot $$5 = Mob.m_147233_(itemStack1);
            ItemStack $$6 = itemStack1.split(1);
            $$4.setItemSlot($$5, $$6);
            if ($$4 instanceof Mob) {
                ((Mob) $$4).setDropChance($$5, 2.0F);
                ((Mob) $$4).setPersistenceRequired();
            }
            return true;
        }
    }

    public ArmorItem(ArmorMaterial armorMaterial0, ArmorItem.Type armorItemType1, Item.Properties itemProperties2) {
        super(itemProperties2.defaultDurability(armorMaterial0.getDurabilityForType(armorItemType1)));
        this.material = armorMaterial0;
        this.type = armorItemType1;
        this.defense = armorMaterial0.getDefenseForType(armorItemType1);
        this.toughness = armorMaterial0.getToughness();
        this.knockbackResistance = armorMaterial0.getKnockbackResistance();
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
        Builder<Attribute, AttributeModifier> $$3 = ImmutableMultimap.builder();
        UUID $$4 = (UUID) ARMOR_MODIFIER_UUID_PER_TYPE.get(armorItemType1);
        $$3.put(Attributes.ARMOR, new AttributeModifier($$4, "Armor modifier", (double) this.defense, AttributeModifier.Operation.ADDITION));
        $$3.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier($$4, "Armor toughness", (double) this.toughness, AttributeModifier.Operation.ADDITION));
        if (armorMaterial0 == ArmorMaterials.NETHERITE) {
            $$3.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier($$4, "Armor knockback resistance", (double) this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        this.defaultModifiers = $$3.build();
    }

    public ArmorItem.Type getType() {
        return this.type;
    }

    @Override
    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    public ArmorMaterial getMaterial() {
        return this.material;
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack0, ItemStack itemStack1) {
        return this.material.getRepairIngredient().test(itemStack1) || super.isValidRepairItem(itemStack0, itemStack1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        return this.m_269277_(this, level0, player1, interactionHand2);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot0) {
        return equipmentSlot0 == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot0);
    }

    public int getDefense() {
        return this.defense;
    }

    public float getToughness() {
        return this.toughness;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.getMaterial().getEquipSound();
    }

    public static enum Type {

        HELMET(EquipmentSlot.HEAD, "helmet"), CHESTPLATE(EquipmentSlot.CHEST, "chestplate"), LEGGINGS(EquipmentSlot.LEGS, "leggings"), BOOTS(EquipmentSlot.FEET, "boots");

        private final EquipmentSlot slot;

        private final String name;

        private Type(EquipmentSlot p_266754_, String p_266886_) {
            this.slot = p_266754_;
            this.name = p_266886_;
        }

        public EquipmentSlot getSlot() {
            return this.slot;
        }

        public String getName() {
            return this.name;
        }
    }
}