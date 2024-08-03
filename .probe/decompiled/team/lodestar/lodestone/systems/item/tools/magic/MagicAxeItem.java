package team.lodestar.lodestone.systems.item.tools.magic;

import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import team.lodestar.lodestone.systems.item.tools.LodestoneAxeItem;

public class MagicAxeItem extends LodestoneAxeItem {

    public final float magicDamage;

    public MagicAxeItem(Tier material, float damage, float speed, float magicDamage, Item.Properties properties) {
        super(material, damage, speed, properties.durability(material.getUses()));
        this.magicDamage = magicDamage;
    }

    @Override
    public Builder<Attribute, AttributeModifier> createExtraAttributes() {
        Builder<Attribute, AttributeModifier> builder = new Builder();
        builder.put(LodestoneAttributeRegistry.MAGIC_DAMAGE.get(), new AttributeModifier((UUID) LodestoneAttributeRegistry.UUIDS.get(LodestoneAttributeRegistry.MAGIC_DAMAGE), "Weapon magic damage", (double) this.magicDamage, AttributeModifier.Operation.ADDITION));
        return builder;
    }
}