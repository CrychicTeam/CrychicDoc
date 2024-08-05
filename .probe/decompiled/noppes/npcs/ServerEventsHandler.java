package noppes.npcs;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.brigadier.context.CommandContext;
import java.util.HashMap;
import java.util.concurrent.Executors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.api.gui.MainMenuGui;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.api.wrapper.WrapperEntityData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.VisibilityController;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemSoulstoneEmpty;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiCloneOpen;
import noppes.npcs.packets.client.PacketGuiOpen;
import noppes.npcs.packets.client.PacketMarkData;
import noppes.npcs.quests.QuestKill;

public class ServerEventsHandler {

    public static Villager Merchant;

    @SubscribeEvent
    public void invoke(PlayerInteractEvent.EntityInteract event) {
        ItemStack item = event.getEntity().m_21205_();
        if (!item.isEmpty() && event.getHand() == InteractionHand.MAIN_HAND) {
            boolean isClientSide = event.getEntity().m_9236_().isClientSide;
            boolean npcInteracted = event.getTarget() instanceof EntityNPCInterface;
            if (isClientSide || !CustomNpcs.OpsOnly || event.getEntity().m_20194_().getPlayerList().isOp(event.getEntity().getGameProfile())) {
                if (!isClientSide && item.getItem() == CustomItems.soulstoneEmpty && event.getTarget() instanceof LivingEntity) {
                    ((ItemSoulstoneEmpty) item.getItem()).store((LivingEntity) event.getTarget(), item, event.getEntity());
                }
                if (item.getItem() == CustomItems.wand && npcInteracted && !isClientSide) {
                    if (!CustomNpcsPermissions.hasPermission((ServerPlayer) event.getEntity(), CustomNpcsPermissions.NPC_GUI)) {
                        return;
                    }
                    event.setCanceled(true);
                    if (event.getEntity().m_6047_()) {
                        MainMenuGui.open(event.getEntity(), (EntityCustomNpc) event.getTarget());
                    } else {
                        NoppesUtilServer.sendOpenGui(event.getEntity(), EnumGuiType.MainMenuDisplay, (EntityNPCInterface) event.getTarget());
                    }
                } else if (item.getItem() == CustomItems.cloner && !isClientSide && !(event.getTarget() instanceof Player)) {
                    CompoundTag compound = new CompoundTag();
                    if (!event.getTarget().saveAsPassenger(compound)) {
                        return;
                    }
                    PlayerData data = PlayerData.get(event.getEntity());
                    ServerCloneController.Instance.cleanTags(compound);
                    Packets.send((ServerPlayer) event.getEntity(), new PacketGuiCloneOpen(compound));
                    data.cloned = compound;
                    event.setCanceled(true);
                } else if (item.getItem() == CustomItems.scripter && !isClientSide && npcInteracted) {
                    if (!CustomNpcsPermissions.hasPermission((ServerPlayer) event.getEntity(), CustomNpcsPermissions.NPC_GUI)) {
                        return;
                    }
                    NoppesUtilServer.setEditingNpc(event.getEntity(), (EntityNPCInterface) event.getTarget());
                    event.setCanceled(true);
                    Packets.send((ServerPlayer) event.getEntity(), new PacketGuiOpen(EnumGuiType.Script, BlockPos.ZERO));
                } else if (item.getItem() == CustomItems.mount && !isClientSide) {
                    if (!CustomNpcsPermissions.hasPermission((ServerPlayer) event.getEntity(), CustomNpcsPermissions.TOOL_MOUNTER)) {
                        return;
                    }
                    PlayerData data = PlayerData.get(event.getEntity());
                    event.setCanceled(true);
                    data.mounted = event.getTarget();
                    Packets.send((ServerPlayer) event.getEntity(), new PacketGuiOpen(EnumGuiType.MobSpawnerMounter, BlockPos.ZERO));
                } else if (item.getItem() == CustomItems.wand && event.getTarget() instanceof Villager) {
                    if (!CustomNpcsPermissions.hasPermission((ServerPlayer) event.getEntity(), CustomNpcsPermissions.EDIT_VILLAGER)) {
                        return;
                    }
                    event.setCanceled(true);
                    Merchant = (Villager) event.getTarget();
                    if (!isClientSide) {
                        ServerPlayer data = (ServerPlayer) event.getEntity();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void invoke(LivingDeathEvent event) {
        if (!event.getEntity().m_9236_().isClientSide) {
            Entity source = NoppesUtilServer.GetDamageSourcee(event.getSource());
            if (source != null) {
                if (source instanceof EntityNPCInterface && event.getEntity() != null) {
                    EntityNPCInterface npc = (EntityNPCInterface) source;
                    Line line = npc.advanced.getKillLine();
                    if (line != null) {
                        npc.saySurrounding(Line.formatTarget(line, event.getEntity()));
                    }
                    EventHooks.onNPCKills(npc, event.getEntity());
                }
                Player player = null;
                if (source instanceof Player) {
                    player = (Player) source;
                } else if (source instanceof EntityNPCInterface && ((EntityNPCInterface) source).getOwner() instanceof Player) {
                    player = (Player) ((EntityNPCInterface) source).getOwner();
                }
                if (player != null) {
                    this.doQuest(player, event.getEntity(), true);
                    if (event.getEntity() instanceof EntityNPCInterface) {
                        this.doFactionPoints(player, (EntityNPCInterface) event.getEntity());
                    }
                }
            }
            if (event.getEntity() instanceof Player) {
                PlayerData data = PlayerData.get((Player) event.getEntity());
                data.save(false);
            }
        }
    }

    private void doFactionPoints(Player player, EntityNPCInterface npc) {
        npc.advanced.factions.addPoints(player);
    }

    private void doQuest(Player player, LivingEntity entity, boolean all) {
        PlayerData pdata = PlayerData.get(player);
        PlayerQuestData playerdata = pdata.questData;
        String entityName = ForgeRegistries.ENTITY_TYPES.getKey(entity.m_6095_()).toString();
        if (entity instanceof Player) {
            entityName = "Player";
        }
        for (QuestData data : playerdata.activeQuests.values()) {
            if (data.quest.type == 2 || data.quest.type == 4) {
                if (data.quest.type == 4 && all) {
                    for (Player pl : player.m_9236_().m_45976_(Player.class, entity.m_20191_().inflate(10.0, 10.0, 10.0))) {
                        if (pl != player) {
                            this.doQuest(pl, entity, false);
                        }
                    }
                }
                String name = entityName;
                QuestKill quest = (QuestKill) data.quest.questInterface;
                if (quest.targets.containsKey(entity.m_7755_().getString())) {
                    name = entity.m_7755_().getString();
                } else if (!quest.targets.containsKey(entityName)) {
                    continue;
                }
                HashMap<String, Integer> killed = quest.getKilled(data);
                if (!killed.containsKey(name) || (Integer) killed.get(name) < (Integer) quest.targets.get(name)) {
                    int amount = 0;
                    if (killed.containsKey(name)) {
                        amount = (Integer) killed.get(name);
                    }
                    killed.put(name, amount + 1);
                    quest.setKilled(data, killed);
                    pdata.updateClient = true;
                }
            }
        }
        playerdata.checkQuestCompletion(player, 2);
        playerdata.checkQuestCompletion(player, 4);
    }

    @SubscribeEvent
    public void world(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof Player) {
            PlayerData data = PlayerData.get((Player) event.getEntity());
            data.updateCompanion(event.getLevel());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void attachEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            PlayerData.register(event);
        }
        if (event.getObject() instanceof LivingEntity) {
            MarkData.register(event);
        }
        if (event.getObject().level() != null && !event.getObject().level().isClientSide && event.getObject().level() instanceof ServerLevel) {
            WrapperEntityData.register(event);
        }
    }

    @SubscribeEvent
    public void attachItem(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStackWrapper.register(event);
    }

    @SubscribeEvent
    public void savePlayer(PlayerEvent.SaveToFile event) {
        PlayerData.get(event.getEntity()).save(false);
    }

    @SubscribeEvent
    public void playerTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof EntityNPCInterface) {
            EntityNPCInterface npc = (EntityNPCInterface) event.getTarget();
            npc.tracking.add(event.getEntity().m_19879_());
            VisibilityController.checkIsVisible(npc, (ServerPlayer) event.getEntity());
        }
        if (event.getTarget() instanceof LivingEntity && !event.getTarget().level().isClientSide) {
            MarkData data = MarkData.get((LivingEntity) event.getTarget());
            if (!data.marks.isEmpty()) {
                Packets.send((ServerPlayer) event.getEntity(), new PacketMarkData(event.getTarget().getId(), data.getNBT()));
            }
        }
    }

    @SubscribeEvent
    public void playerStopTracking(PlayerEvent.StopTracking event) {
        if (event.getTarget() instanceof EntityNPCInterface) {
            EntityNPCInterface npc = (EntityNPCInterface) event.getTarget();
            npc.tracking.remove(event.getEntity().m_19879_());
        }
    }

    @SubscribeEvent
    public void commandGive(CommandEvent event) {
        String command = event.getParseResults().getReader().getString();
        if (command.startsWith("/give ")) {
            try {
                CommandContext<CommandSourceStack> context = event.getParseResults().getContext().build(event.getParseResults().getReader().getString());
                for (ServerPlayer player : EntityArgument.getPlayers(context, "targets")) {
                    player.m_20194_().execute(ListenableFutureTask.create(Executors.callable(() -> {
                        PlayerQuestData playerdata = PlayerData.get(player).questData;
                        playerdata.checkQuestCompletion(player, 0);
                    })));
                }
            } catch (Throwable var7) {
            }
        }
    }

    @SubscribeEvent
    public void commandTime(CommandEvent event) {
        String command = event.getParseResults().getReader().getString();
        if (command.startsWith("/time ")) {
            try {
                CustomNpcs.Server.m_18707_(() -> {
                    for (ServerPlayer playerMP : CustomNpcs.Server.getPlayerList().getPlayers()) {
                        VisibilityController.instance.onUpdate(playerMP);
                    }
                });
            } catch (Throwable var4) {
            }
        }
    }
}