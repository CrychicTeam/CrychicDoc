package yesman.epicfight.world.entity;

import java.util.Collections;
import java.util.List;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class DodgeLeft extends LivingEntity {

    private static final List<ItemStack> EMPTY_LIST = Collections.emptyList();

    private LivingEntityPatch<?> entitypatch;

    public DodgeLeft(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    public DodgeLeft(LivingEntityPatch<?> entitypatch) {
        this(EpicFightEntities.DODGE_LEFT.get(), entitypatch.getOriginal().m_9236_());
        this.entitypatch = entitypatch;
        Vec3 pos = entitypatch.getOriginal().m_20182_();
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;
        this.m_6034_(x, y, z);
        this.m_20011_(entitypatch.getOriginal().m_20191_().expandTowards(1.0, 0.0, 1.0));
        if (this.m_9236_().isClientSide()) {
            this.m_146870_();
        }
    }

    @Override
    public void tick() {
        if (this.f_19797_ > 5) {
            this.m_146870_();
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (this.m_9236_().isClientSide()) {
            return false;
        } else {
            if (!((AttackResult.ResultType) DodgeAnimation.DODGEABLE_SOURCE_VALIDATOR.apply(damageSource)).dealtDamage()) {
                this.entitypatch.onDodgeSuccess(damageSource);
            }
            this.m_146870_();
            return false;
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return EMPTY_LIST;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot0) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot0, ItemStack itemStack1) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
}