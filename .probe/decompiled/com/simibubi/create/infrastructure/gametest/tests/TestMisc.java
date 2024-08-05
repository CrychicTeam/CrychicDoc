package com.simibubi.create.infrastructure.gametest.tests;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlockEntity;
import com.simibubi.create.content.schematics.SchematicExport;
import com.simibubi.create.content.schematics.SchematicItem;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import com.simibubi.create.infrastructure.gametest.CreateGameTestHelper;
import com.simibubi.create.infrastructure.gametest.GameTestGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

@GameTestGroup(path = "misc")
public class TestMisc {

    @GameTest(template = "schematicannon", timeoutTicks = 300)
    public static void schematicannon(CreateGameTestHelper helper) {
        BlockPos whiteEndBottom = helper.m_177449_(new BlockPos(5, 2, 1));
        BlockPos redEndTop = helper.m_177449_(new BlockPos(5, 4, 7));
        ServerLevel level = helper.m_177100_();
        SchematicExport.saveSchematic(SchematicExport.SCHEMATICS.resolve("uploaded/Deployer"), "schematicannon_gametest", true, level, whiteEndBottom, redEndTop);
        ItemStack schematic = SchematicItem.create(level.m_246945_(Registries.BLOCK), "schematicannon_gametest.nbt", "Deployer");
        BlockPos anchor = helper.m_177449_(new BlockPos(1, 2, 1));
        schematic.getOrCreateTag().putBoolean("Deployed", true);
        schematic.getOrCreateTag().put("Anchor", NbtUtils.writeBlockPos(anchor));
        BlockPos cannonPos = new BlockPos(3, 2, 6);
        SchematicannonBlockEntity cannon = helper.getBlockEntity((BlockEntityType<SchematicannonBlockEntity>) AllBlockEntityTypes.SCHEMATICANNON.get(), cannonPos);
        cannon.inventory.setStackInSlot(0, schematic);
        cannon.state = SchematicannonBlockEntity.State.RUNNING;
        cannon.statusMsg = "running";
        helper.m_177361_(() -> {
            if (cannon.state != SchematicannonBlockEntity.State.STOPPED) {
                helper.fail("Schematicannon not done");
            }
            BlockPos lastBlock = new BlockPos(1, 4, 7);
            helper.m_177208_(Blocks.RED_WOOL, lastBlock);
        });
    }

    @GameTest(template = "shearing")
    public static void shearing(CreateGameTestHelper helper) {
        BlockPos sheepPos = new BlockPos(2, 1, 2);
        Sheep sheep = helper.getFirstEntity(EntityType.SHEEP, sheepPos);
        sheep.shear(SoundSource.NEUTRAL);
        helper.m_177361_(() -> helper.m_177194_(Items.WHITE_WOOL, sheepPos, 2.0));
    }

    @GameTest(template = "smart_observer_blocks")
    public static void smartObserverBlocks(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(2, 2, 1);
        BlockPos leftLamp = new BlockPos(3, 4, 3);
        BlockPos rightLamp = new BlockPos(1, 4, 3);
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            helper.m_177255_(leftLamp, RedstoneLampBlock.LIT, true);
            helper.m_177255_(rightLamp, RedstoneLampBlock.LIT, false);
        });
    }

    @GameTest(template = "threshold_switch_pulley")
    public static void thresholdSwitchPulley(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(3, 7, 1);
        BlockPos switchPos = new BlockPos(1, 6, 1);
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            ThresholdSwitchBlockEntity switchBe = helper.getBlockEntity((BlockEntityType<ThresholdSwitchBlockEntity>) AllBlockEntityTypes.THRESHOLD_SWITCH.get(), switchPos);
            float level = switchBe.getStockLevel();
            if (level < 0.0F || level > 1.0F) {
                helper.fail("Invalid level: " + level);
            }
        });
    }

    @GameTest(template = "netherite_backtank", timeoutTicks = 200)
    public static void netheriteBacktank(CreateGameTestHelper helper) {
        BlockPos lava = new BlockPos(2, 2, 3);
        BlockPos zombieSpawn = lava.above(2);
        BlockPos armorStandPos = new BlockPos(2, 2, 1);
        helper.m_177127_(5L, () -> {
            Zombie zombie = (Zombie) helper.m_177176_(EntityType.ZOMBIE, zombieSpawn);
            ArmorStand armorStand = helper.getFirstEntity(EntityType.ARMOR_STAND, armorStandPos);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                zombie.m_8061_(slot, armorStand.getItemBySlot(slot).copy());
            }
        });
        helper.m_177361_(() -> {
            helper.assertSecondsPassed(9);
            helper.m_177374_(EntityType.ZOMBIE, lava);
        });
    }
}