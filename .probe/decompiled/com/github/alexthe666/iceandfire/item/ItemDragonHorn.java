package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ItemDragonHorn extends Item {

    public ItemDragonHorn() {
        super(new Item.Properties().stacksTo(1));
    }

    public static int getDragonType(ItemStack stack) {
        if (stack.getTag() != null) {
            String id = stack.getTag().getString("DragonHornEntityID");
            if (EntityType.byString(id).isPresent()) {
                EntityType entityType = (EntityType) EntityType.byString(id).get();
                if (entityType == IafEntityRegistry.FIRE_DRAGON.get()) {
                    return 1;
                }
                if (entityType == IafEntityRegistry.ICE_DRAGON.get()) {
                    return 2;
                }
                if (entityType == IafEntityRegistry.LIGHTNING_DRAGON.get()) {
                    return 3;
                }
            }
        }
        return 0;
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }

    @NotNull
    @Override
    public InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player playerIn, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        ItemStack trueStack = playerIn.m_21120_(hand);
        if (playerIn.m_9236_().isClientSide || hand != InteractionHand.MAIN_HAND || !(target instanceof EntityDragonBase) || !((EntityDragonBase) target).m_21830_(playerIn) || trueStack.getTag() != null && (trueStack.getTag() == null || !trueStack.getTag().getCompound("EntityTag").isEmpty())) {
            return InteractionResult.FAIL;
        } else {
            CompoundTag newTag = new CompoundTag();
            CompoundTag entityTag = new CompoundTag();
            target.m_20223_(entityTag);
            newTag.put("EntityTag", entityTag);
            newTag.putString("DragonHornEntityID", ForgeRegistries.ENTITY_TYPES.getKey(target.m_6095_()).toString());
            trueStack.setTag(newTag);
            playerIn.m_6674_(hand);
            playerIn.m_9236_().playSound(playerIn, playerIn.m_20183_(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 3.0F, 0.75F);
            target.remove(Entity.RemovalReason.DISCARDED);
            return InteractionResult.SUCCESS;
        }
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.FAIL;
        } else {
            ItemStack stack = context.getItemInHand();
            if (stack.getTag() != null && !stack.getTag().getString("DragonHornEntityID").isEmpty()) {
                Level world = context.getLevel();
                String id = stack.getTag().getString("DragonHornEntityID");
                EntityType type = (EntityType) EntityType.byString(id).orElse(null);
                if (type != null) {
                    Entity entity = type.create(world);
                    if (entity instanceof EntityDragonBase dragon) {
                        dragon.m_20258_(stack.getTag().getCompound("EntityTag"));
                    }
                    if (stack.getTag().contains("EntityUUID")) {
                        entity.setUUID(stack.getTag().getUUID("EntityUUID"));
                    }
                    entity.absMoveTo((double) context.getClickedPos().m_123341_() + 0.5, (double) (context.getClickedPos().m_123342_() + 1), (double) context.getClickedPos().m_123343_() + 0.5, 180.0F + context.getHorizontalDirection().toYRot(), 0.0F);
                    if (world.m_7967_(entity)) {
                        CompoundTag tag = stack.getTag();
                        tag.remove("DragonHornEntityID");
                        tag.remove("EntityTag");
                        tag.remove("EntityUUID");
                        stack.setTag(tag);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (stack.getTag() != null) {
            CompoundTag entityTag = stack.getTag().getCompound("EntityTag");
            if (!entityTag.isEmpty()) {
                String id = stack.getTag().getString("DragonHornEntityID");
                if (EntityType.byString(id).isPresent()) {
                    EntityType type = (EntityType) EntityType.byString(id).get();
                    tooltip.add(Component.translatable(type.getDescriptionId()).withStyle(this.getTextColorForEntityType(type)));
                    String name = Component.translatable("dragon.unnamed").getString();
                    if (!entityTag.getString("CustomName").isEmpty()) {
                        MutableComponent component = Component.Serializer.fromJson(entityTag.getString("CustomName"));
                        if (component != null) {
                            name = component.getString();
                        }
                    }
                    tooltip.add(Component.literal(name).withStyle(ChatFormatting.GRAY));
                    String gender = Component.translatable("dragon.gender").getString() + " " + Component.translatable(entityTag.getBoolean("Gender") ? "dragon.gender.male" : "dragon.gender.female").getString();
                    tooltip.add(Component.literal(gender).withStyle(ChatFormatting.GRAY));
                    int stagenumber = entityTag.getInt("AgeTicks") / 24000;
                    int stage1 = 0;
                    byte var14;
                    if (stagenumber >= 100) {
                        var14 = 5;
                    } else if (stagenumber >= 75) {
                        var14 = 4;
                    } else if (stagenumber >= 50) {
                        var14 = 3;
                    } else if (stagenumber >= 25) {
                        var14 = 2;
                    } else {
                        var14 = 1;
                    }
                    String stage = Component.translatable("dragon.stage").getString() + " " + var14 + " " + Component.translatable("dragon.days.front").getString() + stagenumber + " " + Component.translatable("dragon.days.back").getString();
                    tooltip.add(Component.literal(stage).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    private ChatFormatting getTextColorForEntityType(EntityType type) {
        if (type == IafEntityRegistry.FIRE_DRAGON.get()) {
            return ChatFormatting.DARK_RED;
        } else if (type == IafEntityRegistry.ICE_DRAGON.get()) {
            return ChatFormatting.BLUE;
        } else {
            return type == IafEntityRegistry.LIGHTNING_DRAGON.get() ? ChatFormatting.DARK_PURPLE : ChatFormatting.GRAY;
        }
    }
}