package yesman.epicfight.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class GreatswordItem extends WeaponItem {

    protected static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("16295ED8-B092-4A75-9A94-BCD8D56668BB");

    private final float attackDamage;

    private final float attackSpeed;

    public GreatswordItem(Item.Properties build, Tier tier) {
        super(tier, 0, 0.0F, build);
        this.attackDamage = 11.0F + tier.getAttackDamageBonus();
        this.attackSpeed = -2.85F - 0.05F * (float) tier.getLevel();
    }

    @Override
    public int getEnchantmentValue() {
        return 5;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", (double) this.attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_MODIFIER, "Weapon modifier", -0.02, AttributeModifier.Operation.ADDITION));
            return builder.build();
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }
}