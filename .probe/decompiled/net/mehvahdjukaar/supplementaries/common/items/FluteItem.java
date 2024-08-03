package net.mehvahdjukaar.supplementaries.common.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.item.IFirstPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonSpecialItemRenderer;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FluteItem extends InstrumentItem implements IThirdPersonAnimationProvider, IThirdPersonSpecialItemRenderer, IFirstPersonAnimationProvider {

    public FluteItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.BAMBOO);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        CompoundTag tag = pStack.getTag();
        return tag == null ? false : tag.contains("Pet") || super.m_5812_(pStack);
    }

    public static boolean interactWithPet(ItemStack stack, Player player, Entity target, InteractionHand hand) {
        if (!(target instanceof LivingEntity)) {
            return false;
        } else {
            CompoundTag c = stack.getTagElement("Pet");
            if (c != null) {
                return false;
            } else if ((!(target instanceof TamableAnimal animal) || !animal.isTame() || !animal.getOwnerUUID().equals(player.m_20148_())) && !target.getType().is(ModTags.FLUTE_PET)) {
                return false;
            } else {
                if (target instanceof AbstractHorse horse && !horse.isTamed()) {
                    return false;
                }
                if (target instanceof Fox fox && !fox.trusts(player.m_20148_())) {
                    return false;
                }
                CompoundTag com = new CompoundTag();
                com.putString("Name", target.getName().getString());
                com.putUUID("UUID", target.getUUID());
                stack.addTagElement("Pet", com);
                player.m_21008_(hand, stack);
                player.getCooldowns().addCooldown(stack.getItem(), 20);
                return true;
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand handIn) {
        super.use(level, player, handIn);
        ItemStack stack = player.m_21120_(handIn);
        if (level instanceof ServerLevel serverLevel) {
            double x = player.m_20185_();
            double y = player.m_20186_();
            double z = player.m_20189_();
            int r = (Integer) CommonConfigs.Tools.FLUTE_RADIUS.get();
            CompoundTag com = stack.getTagElement("Pet");
            if (com != null) {
                Entity entity = serverLevel.getEntity(com.getUUID("UUID"));
                int maxDist = (Integer) CommonConfigs.Tools.FLUTE_DISTANCE.get() * (Integer) CommonConfigs.Tools.FLUTE_DISTANCE.get();
                if (entity instanceof LivingEntity pet && pet.m_9236_() == player.m_9236_() && pet.m_20280_(player) < (double) maxDist && pet.randomTeleport(x, y, z, false)) {
                    pet.stopSleeping();
                }
            } else {
                AABB bb = new AABB(x - (double) r, y - (double) r, z - (double) r, x + (double) r, y + (double) r, z + (double) r);
                for (Entity e : level.getEntities(player, bb, TamableAnimal.class::isInstance)) {
                    TamableAnimal pet = (TamableAnimal) e;
                    if (pet.isTame() && !pet.isOrderedToSit() && pet.getOwnerUUID().equals(player.m_20148_())) {
                        pet.m_20984_(x, y, z, false);
                    }
                }
            }
            player.getCooldowns().addCooldown(this, 20);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity entity, int pTimeCharged) {
        super.releaseUsing(pStack, pLevel, entity, pTimeCharged);
        pStack.hurtAndBreak(1, entity, en -> en.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.m_7373_(stack, worldIn, tooltip, flagIn);
        CompoundTag tag = stack.getTagElement("Pet");
        if (tag != null) {
            tooltip.add(Component.literal(tag.getString("Name")).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void spawnNoteParticle(Level level, LivingEntity entity, int note) {
        if ((Boolean) ClientConfigs.Items.FLUTE_PARTICLES.get()) {
            Vec3 bx = new Vec3(1.0, 0.0, 0.0);
            Vec3 by = new Vec3(0.0, 1.0, 0.0);
            Vec3 bz = new Vec3(0.0, 0.0, 1.0);
            float xRot = -entity.m_146909_() * (float) (Math.PI / 180.0);
            float yRot = -Mth.wrapDegrees(entity.yHeadRot) * (float) (Math.PI / 180.0);
            bx = bx.xRot(xRot).yRot(yRot);
            by = by.xRot(xRot).yRot(yRot);
            bz = bz.xRot(xRot).yRot(yRot);
            Vec3 armVec = new Vec3(0.0, 0.0, 0.28 + (double) level.random.nextFloat() * 0.5);
            int mirror = entity.getMainArm() == HumanoidArm.RIGHT ^ entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? -1 : 1;
            armVec = armVec.yRot((float) ((-Math.PI / 2) * (double) mirror)).add(0.0, 0.15, 0.1);
            Vec3 newV = bx.scale(armVec.x).add(by.scale(armVec.y)).add(bz.scale(armVec.z));
            double x = entity.m_20185_() + newV.x;
            double y = entity.m_20188_() + newV.y;
            double z = entity.m_20189_() + newV.z;
            SimpleParticleType particle = entity.m_5842_() ? ParticleTypes.BUBBLE : ParticleTypes.NOTE;
            level.addParticle(particle, x, y, z, (double) level.random.nextInt(24) / 24.0, 0.0, 0.0);
        }
    }

    @Override
    public void animateItemFirstPerson(LivingEntity entity, ItemStack stack, InteractionHand hand, PoseStack matrixStack, float partialTicks, float pitch, float attackAnim, float handHeight) {
        if (entity.isUsingItem() && entity.getUseItemRemainingTicks() > 0 && entity.getUsedItemHand() == hand) {
            int mirror = entity.getMainArm() == HumanoidArm.RIGHT ^ hand == InteractionHand.MAIN_HAND ? -1 : 1;
            matrixStack.translate(-0.4 * (double) mirror, 0.2, 0.0);
            float timeLeft = (float) stack.getUseDuration() - ((float) entity.getUseItemRemainingTicks() - partialTicks + 1.0F);
            float sin = Mth.sin((timeLeft - 0.1F) * 1.3F);
            matrixStack.translate(0.0F, sin * 0.0038F, 0.0F);
            matrixStack.mulPose(Axis.ZN.rotationDegrees(90.0F));
            matrixStack.scale(1.0F * (float) mirror, -1.0F * (float) mirror, -1.0F);
        }
    }

    @Override
    public <T extends LivingEntity> boolean poseRightArm(ItemStack stack, HumanoidModel<T> model, T entity, HumanoidArm mainHand) {
        if (entity.getUseItemRemainingTicks() > 0 && entity.getUseItem().getItem() == this) {
            this.animateHands(model, entity, false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isTwoHanded() {
        return true;
    }

    @Override
    public <T extends LivingEntity> boolean poseLeftArm(ItemStack stack, HumanoidModel<T> model, T entity, HumanoidArm mainHand) {
        if (entity.getUseItemRemainingTicks() > 0 && entity.getUseItem().getItem() == this) {
            this.animateHands(model, entity, true);
            return true;
        } else {
            return false;
        }
    }

    private <T extends LivingEntity> void animateHands(HumanoidModel<T> model, T entity, boolean leftHand) {
        ModelPart mainHand = leftHand ? model.leftArm : model.rightArm;
        ModelPart offHand = leftHand ? model.rightArm : model.leftArm;
        Vec3 bx = new Vec3(1.0, 0.0, 0.0);
        Vec3 by = new Vec3(0.0, 1.0, 0.0);
        Vec3 bz = new Vec3(0.0, 0.0, 1.0);
        float headXRot = MthUtils.wrapRad(model.head.xRot);
        float headYRot = MthUtils.wrapRad(model.head.yRot);
        float downFacingRot = Mth.clamp(headXRot, 0.0F, 0.8F);
        float xRot = getMaxHeadXRot(headXRot) - (entity.m_6047_() ? 1.0F : 0.0F) - 0.3F + downFacingRot * 0.5F;
        bx = bx.xRot(xRot);
        by = by.xRot(xRot);
        bz = bz.xRot(xRot);
        Vec3 armVec = new Vec3(0.0, 0.0, 1.0);
        float mirror = leftHand ? -1.0F : 1.0F;
        armVec = armVec.yRot(-0.99F * mirror);
        Vec3 newV = bx.scale(armVec.x).add(by.scale(armVec.y)).add(bz.scale(armVec.z));
        float yaw = (float) Math.atan2(-newV.x, newV.z);
        float len = (float) newV.length();
        float pitch = (float) Math.asin(newV.y / (double) len);
        mainHand.yRot = yaw + headYRot * 1.4F - 0.1F * mirror - 0.5F * downFacingRot * mirror;
        mainHand.xRot = (float) ((double) pitch - (Math.PI / 2));
        offHand.yRot = (float) Mth.clamp((double) (MthUtils.wrapRad(mainHand.yRot) - 1.0F * mirror) * 0.2, -0.15, 0.15) + 1.1F * mirror;
        offHand.xRot = MthUtils.wrapRad(mainHand.xRot - 0.06F);
        float offset = leftHand ? -Mth.clamp(headYRot, -1.0F, 0.0F) : Mth.clamp(headYRot, 0.0F, 1.0F);
        mainHand.z = -offset * 0.95F;
        AnimationUtils.bobModelPart(model.leftArm, (float) entity.f_19797_, 1.0F);
        AnimationUtils.bobModelPart(model.rightArm, (float) entity.f_19797_, -1.0F);
    }

    public static float getMaxHeadXRot(float xRot) {
        return Mth.clamp(xRot, (float) (-Math.PI * 2.0 / 5.0), (float) (Math.PI / 2));
    }

    @Override
    public <T extends Player, M extends EntityModel<T> & ArmedModel & HeadedModel> void renderThirdPersonItem(M parentModel, LivingEntity entity, ItemStack stack, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            boolean leftHand = humanoidArm == HumanoidArm.LEFT;
            ItemDisplayContext transform;
            if (entity.getUseItem() == stack) {
                ModelPart head = parentModel.getHead();
                float oldRot = head.xRot;
                head.xRot = getMaxHeadXRot(MthUtils.wrapRad(oldRot));
                head.translateAndRotate(poseStack);
                head.xRot = oldRot;
                CustomHeadLayer.translateToHead(poseStack, false);
                poseStack.translate(0.0, -0.265625, -0.53125);
                if (leftHand) {
                    poseStack.mulPose(RotHlpr.XN90);
                }
                transform = ItemDisplayContext.HEAD;
            } else {
                parentModel.translateToHand(humanoidArm, poseStack);
                poseStack.mulPose(RotHlpr.XN90);
                poseStack.mulPose(RotHlpr.Y180);
                poseStack.translate((double) ((float) (leftHand ? -1 : 1) / 16.0F), 0.125, -0.625);
                transform = leftHand ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }
            Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(entity, stack, transform, leftHand, poseStack, bufferSource, light);
            poseStack.popPose();
        }
    }
}