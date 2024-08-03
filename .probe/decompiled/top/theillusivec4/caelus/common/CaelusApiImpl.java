package top.theillusivec4.caelus.common;

import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.caelus.api.CaelusApi;

public class CaelusApiImpl extends CaelusApi {

    public static final CaelusApi INSTANCE = new CaelusApiImpl();

    public static final String MOD_ID = "caelus";

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, "caelus");

    private static final RegistryObject<Attribute> FALL_FLYING = ATTRIBUTES.register("fall_flying", () -> new RangedAttribute("caelus.fallFlying", 0.1, 0.0, 1.0).m_22084_(true));

    private static final AttributeModifier ELYTRA_MODIFIER = new AttributeModifier(UUID.fromString("5b6c3728-9c24-42ae-83ac-70d61d8b8199"), "Elytra modifier", 1.0, AttributeModifier.Operation.ADDITION);

    @Override
    public String getModId() {
        return "caelus";
    }

    @Override
    public Attribute getFlightAttribute() {
        return FALL_FLYING.get();
    }

    @Override
    public AttributeModifier getElytraModifier() {
        return ELYTRA_MODIFIER;
    }

    @Override
    public CaelusApi.TriState canFallFly(LivingEntity livingEntity) {
        Attribute fallFlying = FALL_FLYING.get();
        AttributeInstance attribute = livingEntity.getAttribute(fallFlying);
        if (attribute != null) {
            double val = attribute.getValue();
            double baseValue = attribute.getBaseValue();
            double actualBaseValue = fallFlying.getDefaultValue();
            if (baseValue != actualBaseValue) {
                attribute.setBaseValue(actualBaseValue);
            }
            if (val >= 1.0) {
                return CaelusApi.TriState.ALLOW;
            } else {
                return val > 0.0 ? CaelusApi.TriState.DEFAULT : CaelusApi.TriState.DENY;
            }
        } else {
            ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
            return stack.canElytraFly(livingEntity) ? CaelusApi.TriState.ALLOW : CaelusApi.TriState.DEFAULT;
        }
    }

    @Override
    public boolean canFly(LivingEntity livingEntity) {
        return this.canFallFly(livingEntity) == CaelusApi.TriState.ALLOW;
    }
}