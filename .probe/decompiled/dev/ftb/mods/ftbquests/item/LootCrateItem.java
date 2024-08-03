package dev.ftb.mods.ftbquests.item;

import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.RewardNotificationsScreen;
import dev.ftb.mods.ftbquests.quest.loot.LootCrate;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class LootCrateItem extends Item {

    public LootCrateItem() {
        super(FTBQuestsItems.defaultProps());
    }

    @Nullable
    public static LootCrate getCrate(ItemStack stack) {
        return stack.hasTag() && stack.getItem() instanceof LootCrateItem ? (LootCrate) LootCrate.LOOT_CRATES.get(stack.getTag().getString("type")) : null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        LootCrate crate = getCrate(stack);
        if (crate == null) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        } else {
            int nItems = player.m_6047_() ? stack.getCount() : 1;
            if (!world.isClientSide) {
                for (WeightedReward wr : crate.getTable().generateWeightedRandomRewards(player.m_217043_(), nItems, true)) {
                    wr.getReward().claim((ServerPlayer) player, true);
                }
                world.playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
            } else {
                new RewardNotificationsScreen().openGui();
                for (int i = 0; i < 5; i++) {
                    Vec3 vec3d = new Vec3(((double) world.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                    vec3d = vec3d.xRot(-player.m_146909_() * (float) (Math.PI / 180.0));
                    vec3d = vec3d.yRot(-player.m_146908_() * (float) (Math.PI / 180.0));
                    double d0 = (double) (-world.random.nextFloat()) * 0.6 - 0.3;
                    Vec3 vec3d1 = new Vec3(((double) world.random.nextFloat() - 0.5) * 0.3, d0, 0.6);
                    vec3d1 = vec3d1.xRot(-player.m_146909_() * (float) (Math.PI / 180.0));
                    vec3d1 = vec3d1.yRot(-player.m_146908_() * (float) (Math.PI / 180.0));
                    vec3d1 = vec3d1.add(player.m_20185_(), player.m_20186_() + (double) player.m_20192_(), player.m_20189_());
                    world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
                }
            }
            stack.shrink(nItems);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        LootCrate crate = getCrate(stack);
        return crate != null && crate.isGlow();
    }

    @Override
    public Component getName(ItemStack stack) {
        LootCrate crate = getCrate(stack);
        return (Component) (crate != null && !crate.getItemName().isEmpty() ? Component.translatable(crate.getItemName()) : super.getName(stack));
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        if (world != null && ClientQuestFile.exists()) {
            LootCrate crate = getCrate(stack);
            if (crate != null) {
                if (crate.getItemName().isEmpty()) {
                    tooltip.add(crate.getTable().getMutableTitle().withStyle(ChatFormatting.YELLOW));
                    tooltip.add(Component.empty());
                }
                tooltip.add(Component.translatable("item.ftbquests.lootcrate.tooltip_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("item.ftbquests.lootcrate.tooltip_2").withStyle(ChatFormatting.GRAY));
            } else {
                String name = stack.hasTag() ? stack.getTag().getString("type") : "";
                tooltip.add(Component.translatable("item.ftbquests.lootcrate.missing", name).withStyle(ChatFormatting.RED));
            }
        }
    }
}