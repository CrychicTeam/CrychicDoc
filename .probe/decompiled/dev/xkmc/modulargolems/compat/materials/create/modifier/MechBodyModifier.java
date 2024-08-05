package dev.xkmc.modulargolems.compat.materials.create.modifier;

import dev.xkmc.modulargolems.compat.materials.create.CreateCompatRegistry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

public class MechBodyModifier extends GolemModifier {

    public MechBodyModifier() {
        super(StatFilterType.HEAD, 1);
    }

    @Override
    public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
        if (!golem.m_9236_().isClientSide()) {
            int threshold = 200;
            if (golem.f_19797_ % 20 == 0) {
                int mobile = (Integer) golem.getModifiers().getOrDefault(CreateCompatRegistry.MOBILE.get(), 0);
                int force = (Integer) golem.getModifiers().getOrDefault(CreateCompatRegistry.FORCE.get(), 0);
                if (mobile != 0 || force != 0) {
                    MobEffectInstance mobileIns = golem.m_21124_((MobEffect) CreateCompatRegistry.EFF_MOBILE.get());
                    MobEffectInstance forceIns = golem.m_21124_((MobEffect) CreateCompatRegistry.EFF_FORCE.get());
                    int mobileTime = 0;
                    int forceTime = 0;
                    if (mobileIns != null) {
                        mobileTime = mobileIns.getDuration();
                    }
                    if (forceIns != null) {
                        forceTime = forceIns.getDuration();
                    }
                    if (mobile > 0 && mobileTime < threshold || force > 0 && forceTime < threshold) {
                        ItemStack fuel = golem.m_6298_(CreateCompatRegistry.DUMMY.asStack());
                        LivingEntity self = golem;
                        if (fuel.isEmpty()) {
                            LivingEntity captain = golem.getCaptain();
                            if (captain != null) {
                                self = captain;
                                fuel = captain.getProjectile(CreateCompatRegistry.DUMMY.asStack());
                            }
                            if (fuel.isEmpty()) {
                                return;
                            }
                        }
                        int time = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
                        if (time <= 0) {
                            return;
                        }
                        ItemStack remain = fuel.getCraftingRemainingItem();
                        if (!remain.isEmpty()) {
                            self.m_19983_(remain);
                        }
                        fuel.shrink(1);
                        if (mobile > 0) {
                            golem.m_7292_(new MobEffectInstance((MobEffect) CreateCompatRegistry.EFF_MOBILE.get(), mobileTime + time, mobile - 1));
                        }
                        if (force > 0) {
                            golem.m_7292_(new MobEffectInstance((MobEffect) CreateCompatRegistry.EFF_FORCE.get(), forceTime + time, force - 1));
                        }
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, AbstractGolemEntity<?, ?> golem, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        int time = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        if (time <= 0) {
            return InteractionResult.PASS;
        } else {
            int mobile = (Integer) golem.getModifiers().getOrDefault(CreateCompatRegistry.MOBILE.get(), 0);
            int force = (Integer) golem.getModifiers().getOrDefault(CreateCompatRegistry.FORCE.get(), 0);
            if (mobile == 0 && force == 0) {
                return InteractionResult.FAIL;
            } else if (player.m_9236_().isClientSide()) {
                return InteractionResult.SUCCESS;
            } else {
                MobEffectInstance mobileIns = golem.m_21124_((MobEffect) CreateCompatRegistry.EFF_MOBILE.get());
                MobEffectInstance forceIns = golem.m_21124_((MobEffect) CreateCompatRegistry.EFF_FORCE.get());
                int mobileTime = 0;
                int forceTime = 0;
                if (mobileIns != null) {
                    mobileTime = mobileIns.getDuration();
                }
                if (forceIns != null) {
                    forceTime = forceIns.getDuration();
                }
                int maxFactor = MGConfig.COMMON.mechMaxFuel.get();
                if (mobile > 0 && mobileTime >= time * maxFactor) {
                    return InteractionResult.FAIL;
                } else if (force > 0 && forceTime >= time * maxFactor) {
                    return InteractionResult.FAIL;
                } else {
                    if (mobile > 0) {
                        golem.m_7292_(new MobEffectInstance((MobEffect) CreateCompatRegistry.EFF_MOBILE.get(), mobileTime + time, mobile - 1));
                    }
                    if (force > 0) {
                        golem.m_7292_(new MobEffectInstance((MobEffect) CreateCompatRegistry.EFF_FORCE.get(), forceTime + time, force - 1));
                    }
                    if (!player.isCreative()) {
                        ItemStack remain = stack.getCraftingRemainingItem();
                        stack.shrink(1);
                        if (!remain.isEmpty()) {
                            player.getInventory().placeItemBackInInventory(remain);
                        }
                    }
                    float f1 = 1.0F + (golem.m_217043_().nextFloat() - golem.m_217043_().nextFloat()) * 0.2F;
                    golem.m_5496_(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }
}