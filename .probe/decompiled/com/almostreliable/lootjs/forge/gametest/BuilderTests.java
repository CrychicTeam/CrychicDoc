package com.almostreliable.lootjs.forge.gametest;

import com.almostreliable.lootjs.kube.builder.DamageSourcePredicateBuilderJS;
import com.almostreliable.lootjs.loot.condition.WrappedDamageSourceCondition;
import com.almostreliable.lootjs.predicate.ExtendedEntityFlagsPredicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.BiConsumer;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("lootjs")
@PrefixGameTestTemplate(false)
public class BuilderTests {

    @GameTest(m_177046_ = "empty_test_structure")
    public void damageSourcePredicateBuilderJS_Empty(GameTestHelper helper) {
        helper.succeedIf(() -> {
            WrappedDamageSourceCondition defaultP = new DamageSourcePredicateBuilderJS().build();
            JsonObject dsp = defaultP.serializeToJson().getAsJsonObject("DamageSourcePredicate");
            GameTestUtils.assertNull(helper, dsp.get("is_projectile"));
            GameTestUtils.assertNull(helper, dsp.get("is_explosion"));
            GameTestUtils.assertNull(helper, dsp.get("bypasses_armor"));
            GameTestUtils.assertNull(helper, dsp.get("bypasses_invulnerability"));
            GameTestUtils.assertNull(helper, dsp.get("bypasses_magic"));
            GameTestUtils.assertNull(helper, dsp.get("is_fire"));
            GameTestUtils.assertNull(helper, dsp.get("is_magic"));
            GameTestUtils.assertNull(helper, dsp.get("is_lightning"));
        });
    }

    public void damageSourcePredicateFieldTest(GameTestHelper helper, BiConsumer<DamageSourcePredicateBuilderJS, Boolean> setter, String key, boolean value) {
        helper.succeedIf(() -> {
            DamageSourcePredicateBuilderJS builder = new DamageSourcePredicateBuilderJS();
            setter.accept(builder, value);
            JsonObject dsp = builder.build().serializeToJson().getAsJsonObject("DamageSourcePredicate");
            GameTestUtils.assertEquals(helper, dsp.get(key).getAsBoolean(), value);
        });
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate defFlags = new ExtendedEntityFlagsPredicate.Builder().build();
            JsonElement jsonElement = defFlags.serializeToJson();
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_on_fire"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_sneaking"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_sprinting"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_swimming"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_baby"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isInWater"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isUnderWater"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isMonster"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isCreature"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isOnGround"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isUndeadMob"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isArthropodMob"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isIllegarMob"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isWaterMob"));
        });
    }

    public void extendedEntityFlagsPredicateFieldTest(GameTestHelper helper, BiConsumer<ExtendedEntityFlagsPredicate.Builder, Boolean> setter, String key, boolean value) {
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate.Builder builder = new ExtendedEntityFlagsPredicate.Builder();
            setter.accept(builder, value);
            JsonObject eef = builder.build().serializeToJson().getAsJsonObject();
            GameTestUtils.assertEquals(helper, eef.get(key).getAsBoolean(), value);
        });
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isOnFire(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isOnFire, "is_on_fire", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isCrouching(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isCrouching, "is_sneaking", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isSprinting(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isSprinting, "is_sprinting", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isSwimming(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isSwimming, "is_swimming", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isBaby(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isBaby, "is_baby", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isInWater(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isInWater, "isInWater", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isUnderWater(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isUnderWater, "isUnderWater", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isMonster(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isMonster, "isMonster", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isCreature(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isCreature, "isCreature", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isOnGround(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isOnGround, "isOnGround", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isUndeadMob(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isUndeadMob, "isUndeadMob", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isArthropodMob(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isArthropodMob, "isArthropodMob", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isIllegarMob(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isIllegarMob, "isIllegarMob", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isWaterMob(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isWaterMob, "isWaterMob", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isOnFire_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isOnFire, "is_on_fire", true);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isCrouching_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isCrouching, "is_sneaking", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isSprinting_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isSprinting, "is_sprinting", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isSwimming_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isSwimming, "is_swimming", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isBaby_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isBaby, "is_baby", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isInWater_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isInWater, "isInWater", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isUnderWater_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isUnderWater, "isUnderWater", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isMonster_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isMonster, "isMonster", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isCreature_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isCreature, "isCreature", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isOnGround_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isOnGround, "isOnGround", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isUndeadMob_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isUndeadMob, "isUndeadMob", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isArthropodMob_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isArthropodMob, "isArthropodMob", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isIllegarMob_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isIllegarMob, "isIllegarMob", false);
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void extendedEntityFlagsPredicate_isWaterMob_false(GameTestHelper helper) {
        this.extendedEntityFlagsPredicateFieldTest(helper, ExtendedEntityFlagsPredicate.Builder::isWaterMob, "isWaterMob", false);
    }
}