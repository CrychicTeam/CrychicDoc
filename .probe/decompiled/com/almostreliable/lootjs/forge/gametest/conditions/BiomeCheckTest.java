package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.AnyBiomeCheck;
import com.almostreliable.lootjs.loot.condition.BiomeCheck;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("lootjs")
@PrefixGameTestTemplate(false)
public class BiomeCheckTest {

    private static final BlockPos TEST_POS = new BlockPos(1, 0, 1);

    @GameTest(m_177046_ = "empty_test_structure")
    public void AnyBiomeCheck_match(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().m_204166_(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.m_20182_());
        ResourceLocation biome = helper.getLevel().m_9598_().registryOrThrow(Registries.BIOME).getKey(biomeHolder.value());
        ResourceKey<Biome> bKey = this.biome(biome);
        AnyBiomeCheck check = new AnyBiomeCheck(Collections.singletonList(bKey), new ArrayList());
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, check.test(ctx), "Biome " + bKey.location() + " check should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void BiomeCheck_match(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().m_204166_(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.m_20182_());
        ResourceLocation biome = helper.getLevel().m_9598_().registryOrThrow(Registries.BIOME).getKey(biomeHolder.value());
        ResourceKey<Biome> bKey = this.biome(biome);
        BiomeCheck check = new BiomeCheck(Collections.singletonList(bKey), new ArrayList());
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, check.test(ctx), "Biome " + bKey.location() + " check should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void AnyBiomeCheck_fail(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.m_20182_());
        ResourceKey<Biome> bKey = this.biome(new ResourceLocation("minecraft:deep_ocean"));
        AnyBiomeCheck check = new AnyBiomeCheck(Collections.singletonList(bKey), new ArrayList());
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, check.test(ctx), "Biome " + bKey.location() + " check should not pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void BiomeCheck_fail(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.m_20182_());
        ResourceKey<Biome> bKey = this.biome(new ResourceLocation("minecraft:deep_ocean"));
        BiomeCheck check = new BiomeCheck(Collections.singletonList(bKey), new ArrayList());
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, check.test(ctx), "Biome " + bKey.location() + " check should not pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void AnyBiomeCheck_matchTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().m_204166_(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.m_20182_());
        List<TagKey<Biome>> types = biomeHolder.tags().toList();
        AnyBiomeCheck check = new AnyBiomeCheck(new ArrayList(), new ArrayList(types));
        Registry<Biome> biomeReg = helper.getLevel().m_9598_().registryOrThrow(Registries.BIOME);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, check.test(ctx), "Biome " + biomeReg.getKey(biomeHolder.value()) + " tag check should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void BiomeCheck_matchAllTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().m_204166_(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.m_20182_());
        List<TagKey<Biome>> types = biomeHolder.tags().toList();
        BiomeCheck check = new BiomeCheck(new ArrayList(), new ArrayList(types));
        Registry<Biome> biomeReg = helper.getLevel().m_9598_().registryOrThrow(Registries.BIOME);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, check.test(ctx), "Biome " + biomeReg.getKey(biomeHolder.value()) + " tag check should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void AnyBiomeCheck_failAllTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().m_204166_(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.m_20182_());
        AnyBiomeCheck check = new AnyBiomeCheck(new ArrayList(), Collections.singletonList(BiomeTags.IS_NETHER));
        Registry<Biome> biomeReg = helper.getLevel().m_9598_().registryOrThrow(Registries.BIOME);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, check.test(ctx), "Biome " + biomeReg.getKey(biomeHolder.value()) + " tag check should not pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void BiomeCheck_failAllTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().m_204166_(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.m_20182_());
        BiomeCheck check = new BiomeCheck(new ArrayList(), Collections.singletonList(BiomeTags.IS_NETHER));
        Registry<Biome> biomeReg = helper.getLevel().m_9598_().registryOrThrow(Registries.BIOME);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, check.test(ctx), "Biome " + biomeReg.getKey(biomeHolder.value()) + " tag check should not pass"));
    }

    public ResourceKey<Biome> biome(ResourceLocation biome) {
        return ResourceKey.create(Registries.BIOME, (ResourceLocation) Objects.requireNonNull(biome));
    }
}