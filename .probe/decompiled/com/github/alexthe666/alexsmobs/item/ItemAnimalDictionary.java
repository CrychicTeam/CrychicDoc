package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityAnaconda;
import com.github.alexthe666.alexsmobs.entity.EntityAnacondaPart;
import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent;
import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpentPart;
import com.github.alexthe666.alexsmobs.entity.EntityCentipedeBody;
import com.github.alexthe666.alexsmobs.entity.EntityCentipedeHead;
import com.github.alexthe666.alexsmobs.entity.EntityCentipedeTail;
import com.github.alexthe666.alexsmobs.entity.EntityMurmur;
import com.github.alexthe666.alexsmobs.entity.EntityMurmurHead;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWorm;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWormPart;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemAnimalDictionary extends Item {

    private boolean usedOnEntity = false;

    public ItemAnimalDictionary(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.m_21120_(hand);
        if (playerIn instanceof ServerPlayer serverplayerentity) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, itemStackIn);
            serverplayerentity.m_36246_(Stats.ITEM_USED.get(this));
        }
        if (playerIn.m_9236_().isClientSide && target.m_20078_() != null && target.m_20078_().contains("alexsmobs:")) {
            this.usedOnEntity = true;
            String id = target.m_20078_().replace("alexsmobs:", "");
            if (target instanceof EntityBoneSerpent || target instanceof EntityBoneSerpentPart) {
                id = "bone_serpent";
            }
            if (target instanceof EntityCentipedeHead || target instanceof EntityCentipedeBody || target instanceof EntityCentipedeTail) {
                id = "cave_centipede";
            }
            if (target instanceof EntityVoidWorm || target instanceof EntityVoidWormPart) {
                id = "void_worm";
            }
            if (target instanceof EntityAnaconda || target instanceof EntityAnacondaPart) {
                id = "anaconda";
            }
            if (target instanceof EntityMurmur || target instanceof EntityMurmurHead) {
                id = "murmur";
            }
            AlexsMobs.PROXY.openBookGUI(itemStackIn, id);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.m_21120_(handIn);
        if (!this.usedOnEntity) {
            if (playerIn instanceof ServerPlayer serverplayerentity) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, itemStackIn);
                serverplayerentity.m_36246_(Stats.ITEM_USED.get(this));
            }
            if (worldIn.isClientSide) {
                AlexsMobs.PROXY.openBookGUI(itemStackIn);
            }
        }
        this.usedOnEntity = false;
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.alexsmobs.animal_dictionary.desc").withStyle(ChatFormatting.GRAY));
    }
}