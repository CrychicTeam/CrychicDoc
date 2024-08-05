package dev.ftb.mods.ftbquests.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.api.FTBQuestsTags;
import dev.ftb.mods.ftbquests.command.ChangeProgressArgument;
import dev.ftb.mods.ftbquests.command.QuestObjectArgument;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import dev.ftb.mods.ftbquests.quest.task.forge.ForgeEnergyTask;
import java.util.Iterator;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod("ftbquests")
public class FTBQuestsForge {

    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, "ftbteams");

    private static final RegistryObject<SingletonArgumentInfo<ChangeProgressArgument>> CHANGE_PROGRESS = COMMAND_ARGUMENT_TYPES.register("change_progress", () -> (SingletonArgumentInfo) ArgumentTypeInfos.registerByClass(ChangeProgressArgument.class, SingletonArgumentInfo.contextFree(ChangeProgressArgument::changeProgress)));

    private static final RegistryObject<SingletonArgumentInfo<QuestObjectArgument>> QUEST_OBJECT = COMMAND_ARGUMENT_TYPES.register("quest_object", () -> (SingletonArgumentInfo) ArgumentTypeInfos.registerByClass(QuestObjectArgument.class, SingletonArgumentInfo.contextFree(QuestObjectArgument::new)));

    public FTBQuestsForge() {
        EventBuses.registerModEventBus("ftbquests", FMLJavaModLoadingContext.get().getModEventBus());
        COMMAND_ARGUMENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FTBQuests quests = new FTBQuests();
        ForgeEnergyTask.TYPE = TaskTypes.register(new ResourceLocation("ftbquests", "forge_energy"), ForgeEnergyTask::new, () -> Icon.getIcon(ForgeEnergyTask.EMPTY_TEXTURE.toString()).combineWith(Icon.getIcon(ForgeEnergyTask.FULL_TEXTURE.toString())));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(event -> quests.setup());
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::init);
        MinecraftForge.EVENT_BUS.addListener(FTBQuestsForge::livingDrops);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, FTBQuestsForge::dropsEvent);
    }

    private static void livingDrops(LivingDropsEvent event) {
        LivingEntity living = event.getEntity();
        if (!living.m_9236_().isClientSide && !(living instanceof Player) && !living.m_6095_().is(FTBQuestsTags.EntityTypes.NO_LOOT_CRATES)) {
            if (ServerQuestFile.INSTANCE != null && ServerQuestFile.INSTANCE.isDropLootCrates()) {
                ServerQuestFile.INSTANCE.makeRandomLootCrate(living, living.m_9236_().random).ifPresent(crate -> {
                    ItemEntity itemEntity = new ItemEntity(living.m_9236_(), living.m_20185_(), living.m_20186_(), living.m_20189_(), crate.createStack());
                    itemEntity.setPickUpDelay(10);
                    event.getDrops().add(itemEntity);
                });
            }
        }
    }

    private static void dropsEvent(LivingDropsEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!(player instanceof FakePlayer) && !player.m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                Iterator<ItemEntity> iterator = event.getDrops().iterator();
                while (iterator.hasNext()) {
                    ItemEntity drop = (ItemEntity) iterator.next();
                    ItemStack stack = drop.getItem();
                    if (stack.getItem() == FTBQuestsItems.BOOK.get() && player.m_36356_(stack)) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}