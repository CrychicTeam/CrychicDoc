package com.mna.items.constructs.parts.arms;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.FluidParameterRegistry;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.events.construct.ConstructSprayEffectEvent;
import com.mna.api.tools.MATags;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class ConstructPartFluidNozzleRight extends ItemConstructPart {

    public ConstructPartFluidNozzleRight(ConstructMaterial material) {
        super(material, ConstructSlot.RIGHT_ARM, 64);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.FLUID_DISPENSE };
    }

    @Override
    public float getAttackDamage() {
        return 0.0F;
    }

    @Override
    public int getAttackSpeedModifier() {
        return 10;
    }

    @Override
    public float getRangedAttackDamage() {
        return 0.0F;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        FluidStack backpackFluid = this.getBackpackFluid(player);
        if (!backpackFluid.isEmpty()) {
            player.m_6672_(hand);
            world.playSound(player, player.m_20185_(), player.m_20186_(), player.m_20189_(), FluidParameterRegistry.INSTANCE.forFluid(backpackFluid.getFluid()).spraySound(), SoundSource.NEUTRAL, 0.25F, (float) (0.9 + Math.random() * 0.1));
            return InteractionResultHolder.consume(stack);
        } else {
            if (!player.m_9236_().isClientSide()) {
                player.m_213846_(Component.translatable("item.mna.constructs.construct_fluid_nozzle.no_backpack"));
                player.getCooldowns().addCooldown(this, 60);
            }
            return InteractionResultHolder.pass(stack);
        }
    }

    @Override
    public void onUseTick(Level world, LivingEntity living, ItemStack stack, int ticksRemaining) {
        if (ticksRemaining < 2 && living instanceof Player) {
            ((Player) living).getCooldowns().addCooldown(this, 60);
            ((Player) living).m_5810_();
        }
        FluidStack backpackFluid = this.getBackpackFluid(living);
        if (!backpackFluid.isEmpty() && this.consumeBackpackFluid(living)) {
            FluidParameterRegistry.FluidParameter flParam = FluidParameterRegistry.INSTANCE.forFluid(backpackFluid.getFluid());
            if (world.isClientSide) {
                for (int i = 0; i < flParam.getParticleQuantity(); i++) {
                    Vec3 fwd = living.m_20156_();
                    Vec3 particlePos = living.m_20182_().add(0.0, (double) (living.m_20192_() / 5.0F * 4.0F), 0.0).add(fwd);
                    Vec3 vel = flParam.adjustVelocity(fwd);
                    world.addParticle(flParam.particle(), particlePos.x, particlePos.y, particlePos.z, vel.x, vel.y, vel.z);
                }
            } else {
                AABB bb = living.m_20191_().inflate(0.0, 1.0, 0.0);
                Vec3 look = living.m_20156_();
                bb = bb.expandTowards(look.scale(8.0));
                world.getEntities(living, bb, e -> !(e instanceof LivingEntity) || e == living ? false : living.hasLineOfSight(e)).stream().map(e -> (LivingEntity) e).forEach(e -> {
                    ConstructSprayEffectEvent effectEvt = new ConstructSprayEffectEvent(living, e, backpackFluid.getFluid(), false, false);
                    MinecraftForge.EVENT_BUS.post(effectEvt);
                });
            }
        } else {
            ((Player) living).getCooldowns().addCooldown(this, 60);
            ((Player) living).m_5810_();
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity living, int ticksRemaining) {
        if (living instanceof Player) {
            ((Player) living).getCooldowns().addCooldown(this, 60);
        }
    }

    private FluidStack getBackpackFluid(LivingEntity living) {
        Optional<SlotResult> equipped = CuriosApi.getCuriosHelper().findFirstCurio(living, is -> MATags.isItemIn(is.getItem(), MATags.Items.FLUID_ARM_PLAYER_TANKS));
        if (equipped.isEmpty()) {
            return FluidStack.EMPTY;
        } else {
            Optional<FluidStack> fluid = FluidUtil.getFluidContained(((SlotResult) equipped.get()).stack());
            return fluid.isEmpty() ? FluidStack.EMPTY : (FluidStack) fluid.get();
        }
    }

    private boolean consumeBackpackFluid(LivingEntity living) {
        Optional<SlotResult> equipped = CuriosApi.getCuriosHelper().findFirstCurio(living, is -> MATags.isItemIn(is.getItem(), MATags.Items.FLUID_ARM_PLAYER_TANKS));
        if (equipped.isEmpty()) {
            return false;
        } else {
            LazyOptional<IFluidHandlerItem> fluid = FluidUtil.getFluidHandler(((SlotResult) equipped.get()).stack());
            if (!fluid.isPresent()) {
                return false;
            } else if (((IFluidHandlerItem) fluid.resolve().get()).drain(10, IFluidHandler.FluidAction.SIMULATE).getAmount() == 10) {
                ((IFluidHandlerItem) fluid.resolve().get()).drain(10, IFluidHandler.FluidAction.EXECUTE);
                return true;
            } else {
                return false;
            }
        }
    }
}