package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.MatchEquipmentSlot;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("lootjs")
@PrefixGameTestTemplate(false)
public class MatchEquipmentSlotTest {

    @GameTest(m_177046_ = "empty_test_structure")
    public void hasDiamondInHand(GameTestHelper helper) {
        LootContext ctx = this.basicSetup(helper);
        MatchEquipmentSlot check = new MatchEquipmentSlot(EquipmentSlot.MAINHAND, itemStack -> itemStack.getItem() == Items.DIAMOND);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, check.test(ctx), "MatchEquipmentSlot check should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void hasStickInOffHand(GameTestHelper helper) {
        LootContext ctx = this.basicSetup(helper);
        MatchEquipmentSlot check = new MatchEquipmentSlot(EquipmentSlot.OFFHAND, itemStack -> itemStack.getItem() == Items.STICK);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, check.test(ctx), "MatchEquipmentSlot check should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void matchBoots(GameTestHelper helper) {
        LootContext ctx = this.basicSetup(helper);
        MatchEquipmentSlot check = new MatchEquipmentSlot(EquipmentSlot.FEET, itemStack -> itemStack.getItem() == Items.DIAMOND_BOOTS);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, check.test(ctx), "MatchEquipmentSlot check should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void failMatchMainHand(GameTestHelper helper) {
        LootContext ctx = this.basicSetup(helper);
        MatchEquipmentSlot check = new MatchEquipmentSlot(EquipmentSlot.MAINHAND, ItemFilter.SWORD);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, check.test(ctx), "MatchEquipmentSlot check should fail"));
    }

    public LootContext basicSetup(GameTestHelper helper) {
        ServerPlayer player = new ServerPlayer(helper.getLevel().getServer(), helper.getLevel(), new GameProfile(UUID.randomUUID(), "test-mock-server-player")) {

            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return true;
            }
        };
        LootParams params = new LootParams.Builder(helper.getLevel()).withParameter(LootContextParams.ORIGIN, player.m_20182_()).withParameter(LootContextParams.THIS_ENTITY, player).create(LootContextParamSets.CHEST);
        LootContext lc = new LootContext.Builder(params).create(null);
        player.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND));
        player.m_8061_(EquipmentSlot.OFFHAND, new ItemStack(Items.STICK));
        player.m_8061_(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        player.m_8061_(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        player.m_8061_(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
        player.m_8061_(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        return lc;
    }
}