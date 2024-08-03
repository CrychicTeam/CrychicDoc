package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.TotemExplosion;
import com.github.alexmodguy.alexscaves.server.message.UpdateItemTagMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class TotemOfPossessionItem extends Item implements Vanishable, UpdatesStackTags {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public TotemOfPossessionItem() {
        super(new Item.Properties().durability(1000).rarity(Rarity.UNCOMMON));
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", 2.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", -2.4F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.m_21120_(interactionHand);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            updateEntityIdFromServer(serverLevel, player, itemstack);
        }
        Entity controlledEntity = this.getControlledEntity(level, itemstack);
        if (isBound(itemstack) && (controlledEntity == null || !controlledEntity.isAlive()) && !level.isClientSide) {
            resetBound(itemstack);
        }
        if (!isBound(itemstack) || controlledEntity == null || !isEntityLookingAt(player, controlledEntity, 5.0) && itemstack.getEnchantmentLevel(ACEnchantmentRegistry.SIGHTLESS.get()) <= 0) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            player.m_216990_(ACSoundRegistry.TOTEM_OF_POSSESSION_USE.get());
            player.m_6672_(interactionHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int i1) {
        Entity controlledEntity = this.getControlledEntity(level, stack);
        if (controlledEntity != null) {
            controlledEntity.setGlowingTag(false);
        }
        if (level.isClientSide) {
            AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(user.m_19879_(), stack));
        }
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            stack.shrink(1);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int timeUsing) {
        Entity controlledEntity = this.getControlledEntity(level, stack);
        if ((!isBound(stack) || controlledEntity != null && controlledEntity.isAlive()) && stack.getDamageValue() < stack.getMaxDamage()) {
            if (isBound(stack) && controlledEntity != null && (isEntityLookingAt(user, controlledEntity, 5.0) || stack.getEnchantmentLevel(ACEnchantmentRegistry.SIGHTLESS.get()) != 0) && (!(controlledEntity instanceof Player) || AlexsCaves.COMMON_CONFIG.totemOfPossessionPlayers.get())) {
                if (timeUsing % 2 == 0 && level.isClientSide && (!(user instanceof Player player) || !player.isCreative())) {
                    stack.setDamageValue(stack.getDamageValue() + 1);
                }
                int i = this.getUseDuration(stack) - timeUsing;
                int realStart = 15;
                float time = i < realStart ? (float) i / (float) realStart : 1.0F;
                float maxDist = 32.0F * time;
                float speed = 1.25F + 0.35F * (float) stack.getEnchantmentLevel(ACEnchantmentRegistry.RAPID_POSSESSION.get());
                HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(user, entityx -> entityx.canBeHitByProjectile() && !entityx.equals(controlledEntity), (double) maxDist);
                Vec3 vec3 = hitResult.getLocation();
                if (controlledEntity instanceof Mob mob) {
                    PathNavigation pathNavigation = mob.getNavigation();
                    pathNavigation.moveTo(vec3.x, vec3.y, vec3.z, (double) (time * speed));
                    if (stack.getEnchantmentLevel(ACEnchantmentRegistry.SIGHTLESS.get()) > 0) {
                        controlledEntity.setGlowingTag(true);
                    }
                } else {
                    boolean flying = controlledEntity instanceof FlyingAnimal || controlledEntity instanceof FlyingMob;
                    Vec3 vec31 = vec3.subtract(controlledEntity.position());
                    boolean jumpFlag = false;
                    if (!flying && controlledEntity.horizontalCollision && controlledEntity.onGround() && vec31.y > 0.0) {
                        jumpFlag = true;
                    } else if (!flying && vec31.y > 0.0) {
                        vec31 = new Vec3(vec31.x, 0.0, vec31.z);
                    }
                    float yaw = -((float) Mth.atan2(vec31.x, vec31.z)) * (180.0F / (float) Math.PI);
                    if (vec31.length() > 1.0) {
                        vec31 = vec31.normalize();
                        if (!level.isClientSide) {
                            controlledEntity.setYRot(yaw);
                            controlledEntity.setYBodyRot(controlledEntity.getYRot());
                        }
                    }
                    Vec3 jumpAdd = vec31.scale((double) (0.15F * speed));
                    if (jumpFlag) {
                        jumpAdd = jumpAdd.add(0.0, 0.6, 0.0);
                    }
                    controlledEntity.setDeltaMovement(controlledEntity.getDeltaMovement().scale(0.8F).add(jumpAdd));
                }
                if (level.isClientSide) {
                    for (int particles = 0; (float) particles < 1.0F + controlledEntity.getBbWidth() * 2.0F; particles++) {
                        level.addParticle(DustParticleOptions.REDSTONE, controlledEntity.getRandomX(0.75), controlledEntity.getRandomY(), controlledEntity.getRandomZ(0.75), 0.0, 0.0, 0.0);
                    }
                } else {
                    AABB hitBox = controlledEntity.getBoundingBox().inflate(3.0);
                    if (controlledEntity instanceof Player || controlledEntity instanceof Mob) {
                        for (Entity entity : level.getEntities(controlledEntity, hitBox, Entity::m_271807_)) {
                            if (!controlledEntity.is(entity) && !controlledEntity.isAlliedTo(entity) && !entity.is(user) && !entity.isAlliedTo(controlledEntity) && !entity.isPassengerOfSameVehicle(controlledEntity) && entity instanceof LivingEntity) {
                                LivingEntity target = (LivingEntity) entity;
                                if (controlledEntity instanceof Mob) {
                                    Mob mobx = (Mob) controlledEntity;
                                    mobx.setTarget(target);
                                    mobx.m_6703_(target);
                                    if (i % 4 == 0 && target.getHealth() > mobx.m_21223_() && !target.m_6095_().is(ACTagRegistry.RESISTS_TOTEM_OF_POSSESSION) && stack.getEnchantmentLevel(ACEnchantmentRegistry.ASTRAL_TRANSFERRING.get()) > 0) {
                                        CompoundTag tag = stack.getOrCreateTag();
                                        tag.putUUID("BoundEntityUUID", target.m_20148_());
                                        CompoundTag entityTag = target.serializeNBT();
                                        entityTag.putString("id", ForgeRegistries.ENTITY_TYPES.getKey(target.m_6095_()).toString());
                                        tag.put("BoundEntityTag", entityTag);
                                        user.m_216990_(ACSoundRegistry.TOTEM_OF_POSSESSION_USE.get());
                                        if (level instanceof ServerLevel) {
                                            ServerLevel serverLevel = (ServerLevel) level;
                                            if (user instanceof Player player) {
                                                updateEntityIdFromServer(serverLevel, player, stack);
                                            }
                                        }
                                    }
                                } else if (controlledEntity instanceof Player player) {
                                    player.attack(target);
                                    player.resetAttackStrengthTicker();
                                }
                            }
                        }
                    }
                }
            } else {
                user.stopUsingItem();
                if (level.isClientSide) {
                    AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(user.m_19879_(), stack));
                }
            }
        } else {
            if (controlledEntity != null && stack.getEnchantmentLevel(ACEnchantmentRegistry.DETONATING_DEATH.get()) > 0) {
                TotemExplosion explosion = new TotemExplosion(level, user, controlledEntity.getX(), controlledEntity.getY(), controlledEntity.getZ(), 2.0F + (float) Math.floor((double) controlledEntity.getBbWidth()), Explosion.BlockInteraction.KEEP);
                explosion.explode();
                explosion.finalizeExplosion(true);
            }
            resetBound(stack);
            user.stopUsingItem();
            if (level.isClientSide) {
                AlexsCaves.sendMSGToServer(new UpdateItemTagMessage(user.m_19879_(), stack));
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    private static void resetBound(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.remove("BoundEntityTag");
        tag.remove("BoundEntityUUID");
        tag.remove("ControllingEntityID");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getTag() != null) {
            Tag entity = stack.getTag().get("BoundEntityTag");
            if (entity instanceof CompoundTag) {
                Optional<EntityType<?>> optional = EntityType.by((CompoundTag) entity);
                if (optional.isPresent()) {
                    Component untranslated = ((EntityType) optional.get()).getDescription().copy().withStyle(ChatFormatting.GRAY);
                    tooltip.add(untranslated);
                }
            }
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public static UUID getBoundEntityUUID(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            return tag.contains("BoundEntityUUID") ? tag.getUUID("BoundEntityUUID") : null;
        } else {
            return null;
        }
    }

    private static void updateEntityIdFromServer(ServerLevel level, Player player, ItemStack itemStack) {
        UUID uuid = getBoundEntityUUID(itemStack);
        CompoundTag tag = itemStack.getOrCreateTag();
        int prev = !tag.contains("ControllingEntityID") ? -1 : tag.getInt("ControllingEntityID");
        int set = -1;
        if (uuid != null) {
            Entity entity = level.getEntity(uuid);
            set = entity == null ? -1 : entity.getId();
        }
        tag.putInt("ControllingEntityID", set);
        if (prev != set) {
            AlexsCaves.sendMSGToAll(new UpdateItemTagMessage(player.m_19879_(), itemStack));
        }
    }

    private Entity getControlledEntity(Level level, ItemStack itemStack) {
        if (level.isClientSide) {
            CompoundTag tag = itemStack.getOrCreateTag();
            int id = tag.contains("ControllingEntityID") ? tag.getInt("ControllingEntityID") : -1;
            return id == -1 ? null : level.getEntity(id);
        } else if (level instanceof ServerLevel serverLevel) {
            UUID uuid = getBoundEntityUUID(itemStack);
            return uuid == null ? null : serverLevel.getEntity(uuid);
        } else {
            return null;
        }
    }

    private static boolean isEntityLookingAt(LivingEntity looker, Entity seen, double degree) {
        degree *= 1.0 + (double) looker.m_20270_(seen) * 0.1;
        Vec3 vec3 = looker.m_20252_(1.0F).normalize();
        Vec3 vec31 = new Vec3(seen.getX() - looker.m_20185_(), seen.getBoundingBox().minY + (double) seen.getEyeHeight() - (looker.m_20186_() + (double) looker.m_20192_()), seen.getZ() - looker.m_20189_());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0 - degree / d0 && looker.hasLineOfSight(seen);
    }

    public static boolean isBound(ItemStack stack) {
        return getBoundEntityUUID(stack) != null;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity hurtMob, LivingEntity livingEntity1) {
        if (hurtMob.m_6095_().is(ACTagRegistry.RESISTS_TOTEM_OF_POSSESSION) || hurtMob instanceof Player && !AlexsCaves.COMMON_CONFIG.totemOfPossessionPlayers.get()) {
            if (livingEntity1 instanceof Player player) {
                player.displayClientMessage(Component.translatable("item.alexscaves.totem_of_possession.invalid"), true);
            }
        } else {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putUUID("BoundEntityUUID", hurtMob.m_20148_());
            CompoundTag entityTag = hurtMob.serializeNBT();
            entityTag.putString("id", ForgeRegistries.ENTITY_TYPES.getKey(hurtMob.m_6095_()).toString());
            tag.put("BoundEntityTag", entityTag);
            livingEntity1.m_216990_(ACSoundRegistry.TOTEM_OF_POSSESSION_USE.get());
        }
        return true;
    }
}