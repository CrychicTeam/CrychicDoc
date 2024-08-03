package com.corosus.mobtimizations.loader.forge;

import com.corosus.coroutil.util.CU;
import com.corosus.mobtimizations.CommandMisc;
import com.corosus.mobtimizations.Mobtimizations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("mobtimizations")
public class MobtimizationsForge extends Mobtimizations {

    public MobtimizationsForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.register(Mobtimizations.class);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandMisc.register(event.getDispatcher());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
    }

    @SubscribeEvent
    public void worldTick(TickEvent.LevelTickEvent event) {
        if (Mobtimizations.testSpawningActive) {
            Level level = event.level;
            int huskOrZombieCount = 0;
            if (event.phase == TickEvent.Phase.END && level.dimension() == Level.OVERWORLD && level.getGameTime() % 100L == 0L && level instanceof ServerLevel serverLevel) {
                for (Entity entity : serverLevel.getAllEntities()) {
                    if (entity instanceof Zombie) {
                        huskOrZombieCount++;
                    }
                }
                int spawnMax = 1000;
                int spawnRange = 100;
                int spawnCount = 0;
                if (huskOrZombieCount < spawnMax) {
                    for (int i = 0; i < spawnMax - huskOrZombieCount; i++) {
                        int playerCount = level.m_6907_().size();
                        Player player = (Player) level.m_6907_().get(CU.rand().nextInt(playerCount));
                        int x = Mth.floor(player.m_20182_().x + (double) (CU.rand().nextFloat() * (float) spawnRange - CU.rand().nextFloat() * (float) spawnRange));
                        int z = Mth.floor(player.m_20182_().z + (double) (CU.rand().nextFloat() * (float) spawnRange - CU.rand().nextFloat() * (float) spawnRange));
                        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                        Husk mob = new Husk(EntityType.HUSK, level);
                        mob.m_6034_((double) x, (double) y, (double) z);
                        mob.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(mob.m_20183_()), MobSpawnType.SPAWNER, (SpawnGroupData) null, (CompoundTag) null);
                        level.m_7967_(mob);
                        spawnCount++;
                    }
                }
                System.out.println("spawned " + spawnCount + " husks");
            }
        }
    }
}