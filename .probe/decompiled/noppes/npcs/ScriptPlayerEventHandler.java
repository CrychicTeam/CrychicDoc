package noppes.npcs;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import noppes.npcs.api.IDamageSource;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.event.ItemEvent;
import noppes.npcs.api.event.PlayerEvent;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerScriptData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemNbtBook;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketItemUpdate;
import noppes.npcs.shared.common.util.LogWriter;

public class ScriptPlayerEventHandler {

    @SubscribeEvent
    public void onServerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            PlayerData data = PlayerData.get(player);
            if (player.f_19797_ % 10 == 0) {
                EventHooks.onPlayerTick(data.scriptData);
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack item = player.getInventory().getItem(i);
                    if (!item.isEmpty() && item.getItem() == CustomItems.scripted_item) {
                        ItemScriptedWrapper isw = (ItemScriptedWrapper) NpcAPI.Instance().getIItemStack(item);
                        EventHooks.onScriptItemUpdate(isw, player);
                        if (isw.updateClient) {
                            isw.updateClient = false;
                            Packets.send((ServerPlayer) player, new PacketItemUpdate(i, isw.getMCNbt()));
                        }
                    }
                }
            }
            if (data.playerLevel != player.experienceLevel) {
                EventHooks.onPlayerLevelUp(data.scriptData, data.playerLevel - player.experienceLevel);
                data.playerLevel = player.experienceLevel;
            }
            data.timers.update();
        }
    }

    @SubscribeEvent
    public void invoke(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getEntity().m_9236_().isClientSide && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel() instanceof ServerLevel) {
            if (event.getItemStack().getItem() == CustomItems.teleporter) {
                event.setCanceled(true);
            } else {
                PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
                PlayerEvent.AttackEvent ev = new PlayerEvent.AttackEvent(handler.getPlayer(), 2, NpcAPI.Instance().getIBlock(event.getLevel(), event.getPos()));
                event.setCanceled(EventHooks.onPlayerAttack(handler, ev));
                if (event.getItemStack().getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                    ItemScriptedWrapper isw = ItemScripted.GetWrapper(event.getItemStack());
                    ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent(isw, handler.getPlayer(), 2, NpcAPI.Instance().getIBlock(event.getLevel(), event.getPos()));
                    eve.setCanceled(event.isCanceled());
                    event.setCanceled(EventHooks.onScriptItemAttack(isw, eve));
                }
            }
        }
    }

    @SubscribeEvent
    public void invoke(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getEntity().m_9236_().isClientSide && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel() instanceof ServerLevel) {
            if (event.getItemStack().getItem() == CustomItems.nbt_book) {
                ((ItemNbtBook) event.getItemStack().getItem()).blockEvent(event);
                event.setCanceled(true);
            } else if (event.getItemStack().getItem() == CustomItems.teleporter) {
                event.setCanceled(true);
            } else {
                PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
                handler.hadInteract = true;
                PlayerEvent.InteractEvent ev = new PlayerEvent.InteractEvent(handler.getPlayer(), 2, NpcAPI.Instance().getIBlock(event.getLevel(), event.getPos()));
                event.setCanceled(EventHooks.onPlayerInteract(handler, ev));
                if (event.getItemStack().getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                    ItemScriptedWrapper isw = ItemScripted.GetWrapper(event.getItemStack());
                    ItemEvent.InteractEvent eve = new ItemEvent.InteractEvent(isw, handler.getPlayer(), 2, NpcAPI.Instance().getIBlock(event.getLevel(), event.getPos()));
                    event.setCanceled(EventHooks.onScriptItemInteract(isw, eve));
                }
            }
        }
    }

    @SubscribeEvent
    public void invoke(PlayerInteractEvent.EntityInteract event) {
        if (!event.getEntity().m_9236_().isClientSide && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel() instanceof ServerLevel) {
            if (event.getItemStack().getItem() == CustomItems.nbt_book) {
                ((ItemNbtBook) event.getItemStack().getItem()).entityEvent(event);
                event.setCanceled(true);
            } else {
                PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
                PlayerEvent.InteractEvent ev = new PlayerEvent.InteractEvent(handler.getPlayer(), 1, NpcAPI.Instance().getIEntity(event.getTarget()));
                event.setCanceled(EventHooks.onPlayerInteract(handler, ev));
                if (event.getItemStack().getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                    ItemScriptedWrapper isw = ItemScripted.GetWrapper(event.getItemStack());
                    ItemEvent.InteractEvent eve = new ItemEvent.InteractEvent(isw, handler.getPlayer(), 1, NpcAPI.Instance().getIEntity(event.getTarget()));
                    event.setCanceled(EventHooks.onScriptItemInteract(isw, eve));
                }
            }
        }
    }

    @SubscribeEvent
    public void invoke(PlayerInteractEvent.RightClickItem event) {
        if (!event.getEntity().m_9236_().isClientSide && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel() instanceof ServerLevel) {
            if (event.getEntity().isCreative() && event.getEntity().m_6047_() && event.getItemStack().getItem() == CustomItems.scripted_item) {
                NoppesUtilServer.sendOpenGui(event.getEntity(), EnumGuiType.ScriptItem, null);
            } else {
                PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
                if (handler.hadInteract) {
                    handler.hadInteract = false;
                } else {
                    PlayerEvent.InteractEvent ev = new PlayerEvent.InteractEvent(handler.getPlayer(), 0, null);
                    event.setCanceled(EventHooks.onPlayerInteract(handler, ev));
                    if (event.getItemStack().getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                        ItemScriptedWrapper isw = ItemScripted.GetWrapper(event.getItemStack());
                        ItemEvent.InteractEvent eve = new ItemEvent.InteractEvent(isw, handler.getPlayer(), 0, null);
                        event.setCanceled(EventHooks.onScriptItemInteract(isw, eve));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void invoke(ArrowLooseEvent event) {
        if (!event.getEntity().m_9236_().isClientSide && event.getLevel() instanceof ServerLevel) {
            PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
            PlayerEvent.RangedLaunchedEvent ev = new PlayerEvent.RangedLaunchedEvent(handler.getPlayer());
            event.setCanceled(EventHooks.onPlayerRanged(handler, ev));
        }
    }

    @SubscribeEvent
    public void invoke(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().m_9236_().isClientSide && event.getLevel() instanceof ServerLevel) {
            PlayerScriptData handler = PlayerData.get(event.getPlayer()).scriptData;
            PlayerEvent.BreakEvent ev = new PlayerEvent.BreakEvent(handler.getPlayer(), NpcAPI.Instance().getIBlock((ServerLevel) event.getLevel(), event.getPos()), event.getExpToDrop());
            event.setCanceled(EventHooks.onPlayerBreak(handler, ev));
            event.setExpToDrop(ev.exp);
        }
    }

    @SubscribeEvent
    public void invoke(ItemTossEvent event) {
        if (event.getPlayer().m_9236_() instanceof ServerLevel) {
            PlayerScriptData handler = PlayerData.get(event.getPlayer()).scriptData;
            event.setCanceled(EventHooks.onPlayerToss(handler, event.getEntity()));
        }
    }

    @SubscribeEvent
    public void invoke(EntityItemPickupEvent event) {
        if (event.getEntity().m_9236_() instanceof ServerLevel) {
            PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
            event.setCanceled(EventHooks.onPlayerPickUp(handler, event.getItem()));
        }
    }

    @SubscribeEvent
    public void invoke(PlayerContainerEvent.Open event) {
        if (event.getEntity().m_9236_() instanceof ServerLevel) {
            PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
            EventHooks.onPlayerContainerOpen(handler, event.getContainer());
        }
    }

    @SubscribeEvent
    public void invoke(PlayerContainerEvent.Close event) {
        if (event.getEntity().m_9236_() instanceof ServerLevel) {
            PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
            EventHooks.onPlayerContainerClose(handler, event.getContainer());
        }
    }

    @SubscribeEvent
    public void invoke(LivingDeathEvent event) {
        if (event.getEntity().m_9236_() instanceof ServerLevel) {
            Entity source = NoppesUtilServer.GetDamageSourcee(event.getSource());
            if (event.getEntity() instanceof Player) {
                PlayerScriptData handler = PlayerData.get((Player) event.getEntity()).scriptData;
                EventHooks.onPlayerDeath(handler, event.getSource(), source);
            }
            if (source instanceof Player) {
                PlayerScriptData handler = PlayerData.get((Player) source).scriptData;
                EventHooks.onPlayerKills(handler, event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public void invoke(LivingHurtEvent event) {
        if (event.getEntity().m_9236_() instanceof ServerLevel) {
            Entity source = NoppesUtilServer.GetDamageSourcee(event.getSource());
            if (event.getEntity() instanceof Player) {
                PlayerScriptData handler = PlayerData.get((Player) event.getEntity()).scriptData;
                PlayerEvent.DamagedEvent pevent = new PlayerEvent.DamagedEvent(handler.getPlayer(), source, event.getAmount(), event.getSource());
                event.setCanceled(EventHooks.onPlayerDamaged(handler, pevent));
                event.setAmount(pevent.damage);
            }
            if (source instanceof Player) {
                PlayerScriptData handler = PlayerData.get((Player) source).scriptData;
                PlayerEvent.DamagedEntityEvent pevent = new PlayerEvent.DamagedEntityEvent(handler.getPlayer(), event.getEntity(), event.getAmount(), event.getSource());
                event.setCanceled(EventHooks.onPlayerDamagedEntity(handler, pevent));
                event.setAmount(pevent.damage);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void invoke(LivingAttackEvent event) {
        if (event.getEntity().m_9236_() instanceof ServerLevel) {
            Entity source = NoppesUtilServer.GetDamageSourcee(event.getSource());
            if (source instanceof Player) {
                PlayerScriptData handler = PlayerData.get((Player) source).scriptData;
                ItemStack item = ((Player) source).m_21205_();
                IEntity target = NpcAPI.Instance().getIEntity(event.getEntity());
                IDamageSource damageSource = NpcAPI.Instance().getIDamageSource(event.getSource());
                PlayerEvent.AttackEvent ev = new PlayerEvent.AttackEvent(handler.getPlayer(), target, damageSource);
                event.setCanceled(EventHooks.onPlayerAttack(handler, ev));
                if (item.getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                    ItemScriptedWrapper isw = ItemScripted.GetWrapper(item);
                    ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent(isw, handler.getPlayer(), target, damageSource);
                    eve.setCanceled(event.isCanceled());
                    event.setCanceled(EventHooks.onScriptItemAttack(isw, eve));
                }
            }
        }
    }

    @SubscribeEvent
    public void invoke(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().m_9236_() instanceof ServerLevel) {
            PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
            EventHooks.onPlayerLogin(handler);
        }
    }

    @SubscribeEvent
    public void invoke(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().m_9236_() instanceof ServerLevel) {
            PlayerScriptData handler = PlayerData.get(event.getEntity()).scriptData;
            EventHooks.onPlayerLogout(handler);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void invoke(ServerChatEvent event) {
        if (event.getPlayer().m_9236_() instanceof ServerLevel && event.getPlayer() != EntityNPCInterface.ChatEventPlayer) {
            PlayerScriptData handler = PlayerData.get(event.getPlayer()).scriptData;
            String message = event.getMessage().getString();
            PlayerEvent.ChatEvent ev = new PlayerEvent.ChatEvent(handler.getPlayer(), event.getMessage().getString());
            EventHooks.onPlayerChat(handler, ev);
            event.setCanceled(ev.isCanceled());
            if (!message.equals(ev.message)) {
                MutableComponent chat = Component.translatable("");
                chat.append(ForgeHooks.newChatWithLinks(ev.message));
                event.setMessage(chat);
            }
        }
    }

    private Set<Class> getClasses(String packageName) {
        packageName = packageName.replace('.', '/');
        HashSet<String> urls = new HashSet();
        try {
            Module module = EntityEvent.class.getModule();
            Enumeration<URL> resources = module.getClassLoader().getResources(packageName);
            while (resources.hasMoreElements()) {
                URL url = (URL) resources.nextElement();
                String path = url.getPath();
                int i = path.indexOf(".jar");
                if (i > 0) {
                    urls.add(path.substring(0, i + 4));
                }
            }
        } catch (Throwable var14) {
        }
        try {
            Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources(packageName);
            while (resources.hasMoreElements()) {
                URL url = (URL) resources.nextElement();
                String path = url.getPath();
                int i = path.indexOf(".jar");
                if (i > 0) {
                    urls.add(path.substring(0, i + 4));
                }
            }
        } catch (Throwable var13) {
        }
        HashSet<Class> classes = new HashSet();
        for (String path : urls) {
            try {
                JarFile file = new JarFile(new File(path));
                Enumeration<JarEntry> entries = file.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = (JarEntry) entries.nextElement();
                    if (!entry.isDirectory() && entry.getName().startsWith(packageName)) {
                        String name = entry.getName().replace('/', '.');
                        try {
                            Class c = Class.forName(name.substring(0, name.length() - 6));
                            if (Event.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers()) && Modifier.isPublic(c.getModifiers())) {
                                if (c.getDeclaredClasses().length > 0) {
                                    classes.addAll(Arrays.asList(c.getDeclaredClasses()));
                                } else {
                                    classes.add(c);
                                }
                            }
                            classes.add(c);
                        } catch (Throwable var11) {
                        }
                    }
                }
            } catch (Throwable var12) {
            }
        }
        return classes;
    }

    public ScriptPlayerEventHandler registerForgeEvents() {
        ForgeEventHandler.eventNames.clear();
        ForgeEventHandler handler = new ForgeEventHandler();
        try {
            Method m = handler.getClass().getMethod("forgeEntity", Event.class);
            Method register = MinecraftForge.EVENT_BUS.getClass().getDeclaredMethod("register", Class.class, Object.class, Method.class);
            register.setAccessible(true);
            for (Class c : this.getClasses("net.minecraftforge.event.")) {
                try {
                    if (!GenericEvent.class.isAssignableFrom(c) && !EntityEvent.EntityConstructing.class.isAssignableFrom(c) && !LevelEvent.CreateSpawnPosition.class.isAssignableFrom(c) && !TickEvent.RenderTickEvent.class.isAssignableFrom(c) && !TickEvent.ClientTickEvent.class.isAssignableFrom(c) && !NetworkEvent.ClientCustomPayloadEvent.class.isAssignableFrom(c) && !ItemTooltipEvent.class.isAssignableFrom(c)) {
                        String eventName = ForgeEventHandler.getEventName(c);
                        if (!ForgeEventHandler.eventNames.contains(eventName)) {
                            register.invoke(MinecraftForge.EVENT_BUS, c, handler, m);
                            ForgeEventHandler.eventNames.add(eventName);
                        }
                    }
                } catch (Throwable var7) {
                }
            }
            if (PixelmonHelper.Enabled) {
                try {
                    for (Class c : this.getClasses("com.pixelmonmod.pixelmon.api.events.")) {
                        ForgeEventHandler.eventNames.add(ForgeEventHandler.getEventName(c));
                        register.invoke(PixelmonHelper.EVENT_BUS, c, handler, m);
                    }
                } catch (Throwable var8) {
                    LogWriter.except(var8);
                }
            }
        } catch (Throwable var9) {
            LogWriter.except(var9);
        }
        return this;
    }
}