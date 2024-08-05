package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.world.DragonPosWorldData;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemSummoningCrystal extends Item {

    public ItemSummoningCrystal() {
        super(new Item.Properties().stacksTo(1));
    }

    public static boolean hasDragon(ItemStack stack) {
        if (stack.getItem() instanceof ItemSummoningCrystal && stack.getTag() != null) {
            for (String tagInfo : stack.getTag().getAllKeys()) {
                if (tagInfo.contains("Dragon")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }

    public ItemStack onItemUseFinish(Level worldIn, LivingEntity LivingEntity) {
        return new ItemStack(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        boolean flag = false;
        String desc = "entity.iceandfire.fire_dragon";
        if (stack.getItem() == IafItemRegistry.SUMMONING_CRYSTAL_ICE.get()) {
            desc = "entity.iceandfire.ice_dragon";
        }
        if (stack.getItem() == IafItemRegistry.SUMMONING_CRYSTAL_LIGHTNING.get()) {
            desc = "entity.iceandfire.lightning_dragon";
        }
        if (stack.getTag() != null) {
            for (String tagInfo : stack.getTag().getAllKeys()) {
                if (tagInfo.contains("Dragon")) {
                    CompoundTag dragonTag = stack.getTag().getCompound(tagInfo);
                    String dragonName = I18n.get(desc);
                    if (!dragonTag.getString("CustomName").isEmpty()) {
                        dragonName = dragonTag.getString("CustomName");
                    }
                    tooltip.add(Component.translatable("item.iceandfire.summoning_crystal.bound", dragonName).withStyle(ChatFormatting.GRAY));
                    flag = true;
                }
            }
        }
        if (!flag) {
            tooltip.add(Component.translatable("item.iceandfire.summoning_crystal.desc_0").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("item.iceandfire.summoning_crystal.desc_1").withStyle(ChatFormatting.GRAY));
        }
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getPlayer().m_21120_(context.getHand());
        boolean flag = false;
        BlockPos offsetPos = context.getClickedPos().relative(context.getClickedFace());
        float yaw = context.getPlayer().m_146908_();
        boolean displayError = false;
        if (stack.getItem() == this && hasDragon(stack)) {
            int dragonCount = 0;
            if (stack.getTag() != null) {
                for (String tagInfo : stack.getTag().getAllKeys()) {
                    if (tagInfo.contains("Dragon")) {
                        dragonCount++;
                        CompoundTag dragonTag = stack.getTag().getCompound(tagInfo);
                        UUID id = dragonTag.getUUID("DragonUUID");
                        if (id != null && !context.getLevel().isClientSide) {
                            try {
                                Entity entity = context.getLevel().getServer().getLevel(context.getPlayer().m_9236_().dimension()).getEntity(id);
                                if (entity != null) {
                                    flag = true;
                                    this.summonEntity(entity, context.getLevel(), offsetPos, yaw);
                                }
                            } catch (Exception var19) {
                                var19.printStackTrace();
                                displayError = true;
                            }
                            DragonPosWorldData data = DragonPosWorldData.get(context.getLevel());
                            BlockPos dragonChunkPos = null;
                            if (data != null) {
                                dragonChunkPos = data.getDragonPos(id);
                            }
                            if (IafConfig.chunkLoadSummonCrystal) {
                                try {
                                    boolean flag2 = false;
                                    if (!flag && data != null && context.getLevel().isClientSide) {
                                        ServerLevel serverWorld = (ServerLevel) context.getLevel();
                                        ChunkPos pos = new ChunkPos(dragonChunkPos);
                                        serverWorld.setChunkForced(pos.x, pos.z, true);
                                    }
                                    if (flag2) {
                                        try {
                                            Entity entity = context.getLevel().getServer().getLevel(context.getPlayer().m_9236_().dimension()).getEntity(id);
                                            if (entity != null) {
                                                flag = true;
                                                this.summonEntity(entity, context.getLevel(), offsetPos, yaw);
                                            }
                                        } catch (Exception var17) {
                                            var17.printStackTrace();
                                        }
                                    }
                                } catch (Exception var18) {
                                    IceAndFire.LOGGER.warn("Could not load chunk when summoning dragon");
                                    var18.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                context.getPlayer().playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                context.getPlayer().playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
                context.getPlayer().m_6674_(context.getHand());
                context.getPlayer().displayClientMessage(Component.translatable("message.iceandfire.dragonTeleport"), true);
                stack.setTag(new CompoundTag());
            } else if (displayError) {
                context.getPlayer().displayClientMessage(Component.translatable("message.iceandfire.noDragonTeleport"), true);
            }
        }
        return InteractionResult.PASS;
    }

    public void summonEntity(Entity entity, Level worldIn, BlockPos offsetPos, float yaw) {
        entity.moveTo((double) offsetPos.m_123341_() + 0.5, (double) offsetPos.m_123342_() + 0.5, (double) offsetPos.m_123343_() + 0.5, yaw, 0.0F);
        if (entity instanceof EntityDragonBase) {
            ((EntityDragonBase) entity).setCrystalBound(false);
        }
        if (IafConfig.chunkLoadSummonCrystal) {
            DragonPosWorldData data = DragonPosWorldData.get(worldIn);
            if (data != null) {
                data.removeDragon(entity.getUUID());
            }
        }
    }
}