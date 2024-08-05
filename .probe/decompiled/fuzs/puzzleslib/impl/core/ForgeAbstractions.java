package fuzs.puzzleslib.impl.core;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public final class ForgeAbstractions implements CommonAbstractions {

    @Override
    public MinecraftServer getMinecraftServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider menuProvider, BiConsumer<ServerPlayer, FriendlyByteBuf> screenOpeningDataWriter) {
        NetworkHooks.openScreen(player, menuProvider, buf -> screenOpeningDataWriter.accept(player, buf));
    }

    @Override
    public boolean isBossMob(EntityType<?> type) {
        return type.is(Tags.EntityTypes.BOSSES);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, Level level, BlockPos pos) {
        return state.getEnchantPowerBonus(level, pos);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, Entity entity) {
        return stack.canEquip(slot, entity);
    }

    @Override
    public int getMobLootingLevel(Entity entity, @Nullable Entity killerEntity, @Nullable DamageSource damageSource) {
        return ForgeHooks.getLootingLevel(entity, killerEntity, damageSource);
    }

    @Override
    public boolean getMobGriefingRule(Level level, @Nullable Entity entity) {
        return ForgeEventFactory.getMobGriefingEvent(level, entity);
    }

    @Override
    public void onPlayerDestroyItem(Player player, ItemStack itemStack, @Nullable InteractionHand interactionHand) {
        ForgeEventFactory.onPlayerDestroyItem(player, itemStack, interactionHand);
    }

    @Nullable
    @Override
    public MobSpawnType getMobSpawnType(Mob mob) {
        return mob.getSpawnType();
    }

    @Override
    public Pack.Info createPackInfo(ResourceLocation id, Component description, int packVersion, FeatureFlagSet features, boolean hidden) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.RuntimeException: Constructor net/minecraft/server/packs/repository/Pack$Info.<init>(Lnet/minecraft/network/chat/Component;IILnet/minecraft/world/flag/FeatureFlagSet;Z)V not found
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ExprUtil.getSyntheticParametersMask(ExprUtil.java:49)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.appendParamList(InvocationExprent.java:957)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.NewExprent.toJava(NewExprent.java:460)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1018)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ExitExprent.toJava(ExitExprent.java:86)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.listToJava(ExprProcessor.java:895)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.BasicBlockStatement.toJava(BasicBlockStatement.java:90)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1283)
        //
        // Bytecode:
        // 0: new net/minecraft/server/packs/repository/Pack$Info
        // 3: dup
        // 4: aload 2
        // 5: iload 3
        // 6: iload 3
        // 7: aload 4
        // 9: iload 5
        // b: invokespecial net/minecraft/server/packs/repository/Pack$Info.<init> (Lnet/minecraft/network/chat/Component;IILnet/minecraft/world/flag/FeatureFlagSet;Z)V
        // e: areturn
    }

    @Override
    public boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack itemStack) {
        return enchantment.canApplyAtEnchantingTable(itemStack);
    }

    @Override
    public boolean isAllowedOnBooks(Enchantment enchantment) {
        return enchantment.isAllowedOnBooks();
    }
}