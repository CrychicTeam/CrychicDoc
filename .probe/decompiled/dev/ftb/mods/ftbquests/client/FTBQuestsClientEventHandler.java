package dev.ftb.mods.ftbquests.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonCreatedEvent;
import dev.ftb.mods.ftblibrary.ui.CustomClickEvent;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.util.PanelPositioning;
import dev.ftb.mods.ftbquests.block.entity.FTBQuestsBlockEntities;
import dev.ftb.mods.ftbquests.events.ClearFileCacheEvent;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.item.LootCrateItem;
import dev.ftb.mods.ftbquests.net.SubmitTaskMessage;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.loot.LootCrate;
import dev.ftb.mods.ftbquests.quest.task.ObservationTask;
import dev.ftb.mods.ftbquests.quest.task.StructureTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class FTBQuestsClientEventHandler {

    private static final ResourceLocation QUESTS_BUTTON = new ResourceLocation("ftbquests", "quests");

    static boolean creativeTabRebuildPending = false;

    private List<ObservationTask> observationTasks = null;

    private ObservationTask currentlyObserving = null;

    private long currentlyObservingTicks = 0L;

    private final List<FormattedCharSequence> pinnedQuestText = new ArrayList();

    public static TextureAtlasSprite inputOnlySprite;

    public static TextureAtlasSprite tankSprite;

    public static TextureAtlasSprite feEnergyEmptySprite;

    public static TextureAtlasSprite feEnergyFullSprite;

    public static TextureAtlasSprite trEnergyEmptySprite;

    public static TextureAtlasSprite trEnergyFullSprite;

    public void init() {
        ClientLifecycleEvent.CLIENT_SETUP.register(this::registerItemColors);
        ClientLifecycleEvent.CLIENT_SETUP.register(this::registerBERs);
        SidebarButtonCreatedEvent.EVENT.register(this::onSidebarButtonCreated);
        ClearFileCacheEvent.EVENT.register(this::onFileCacheClear);
        ClientTickEvent.CLIENT_PRE.register(this::onKeyEvent);
        CustomClickEvent.EVENT.register(this::onCustomClick);
        ClientTickEvent.CLIENT_PRE.register(this::onClientTick);
        ClientGuiEvent.RENDER_HUD.register(this::onScreenRender);
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(this::onPlayerLogin);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(this::onPlayerLogout);
    }

    public static void onTextureStitchPost(TextureAtlas textureAtlas) {
        if (textureAtlas.location().equals(InventoryMenu.BLOCK_ATLAS)) {
            inputOnlySprite = textureAtlas.getSprite(TaskScreenRenderer.INPUT_ONLY_TEXTURE);
            tankSprite = textureAtlas.getSprite(TaskScreenRenderer.TANK_TEXTURE);
            feEnergyEmptySprite = textureAtlas.getSprite(TaskScreenRenderer.FE_ENERGY_EMPTY_TEXTURE);
            feEnergyFullSprite = textureAtlas.getSprite(TaskScreenRenderer.FE_ENERGY_FULL_TEXTURE);
            trEnergyEmptySprite = textureAtlas.getSprite(TaskScreenRenderer.TR_ENERGY_EMPTY_TEXTURE);
            trEnergyFullSprite = textureAtlas.getSprite(TaskScreenRenderer.TR_ENERGY_FULL_TEXTURE);
        }
    }

    private void registerBERs(Minecraft minecraft) {
        BlockEntityRendererRegistry.register((BlockEntityType) FTBQuestsBlockEntities.CORE_TASK_SCREEN.get(), TaskScreenRenderer::new);
    }

    private void registerItemColors(Minecraft minecraft) {
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> {
            LootCrate crate = LootCrateItem.getCrate(stack);
            return crate == null ? -1 : 0xFF000000 | crate.getColor().rgb();
        }, (ItemLike) FTBQuestsItems.LOOTCRATE.get());
    }

    private void onSidebarButtonCreated(SidebarButtonCreatedEvent event) {
        if (event.getButton().getId().equals(QUESTS_BUTTON)) {
            event.getButton().setCustomTextHandler(() -> {
                if (ClientQuestFile.exists()) {
                    if (ClientQuestFile.INSTANCE.isDisableGui() && !ClientQuestFile.INSTANCE.canEdit()) {
                        return "[X]";
                    }
                    if (ClientQuestFile.INSTANCE.selfTeamData.isLocked()) {
                        return "[X]";
                    }
                    if (ClientQuestFile.INSTANCE.selfTeamData.hasUnclaimedRewards(Minecraft.getInstance().player.m_20148_(), ClientQuestFile.INSTANCE)) {
                        return "[!]";
                    }
                }
                return "";
            });
        }
    }

    private void onFileCacheClear(BaseQuestFile file) {
        if (!file.isServerSide()) {
            this.observationTasks = null;
        }
    }

    private void onKeyEvent(Minecraft mc) {
        if (ClientQuestFile.exists() && (!ClientQuestFile.INSTANCE.isDisableGui() || ClientQuestFile.INSTANCE.canEdit()) && FTBQuestsClient.KEY_QUESTS.consumeClick()) {
            ClientQuestFile.openGui();
        }
    }

    private EventResult onCustomClick(CustomClickEvent event) {
        if (event.id().getNamespace().equals("ftbquests") && "open_gui".equals(event.id().getPath())) {
            double mx = Minecraft.getInstance().mouseHandler.xpos();
            double my = Minecraft.getInstance().mouseHandler.ypos();
            Minecraft.getInstance().setScreen(null);
            if (ClientQuestFile.openGui() != null) {
                InputConstants.grabOrReleaseMouse(Minecraft.getInstance().getWindow().getWindow(), 212993, mx, my);
            }
            return EventResult.interruptFalse();
        } else {
            return EventResult.pass();
        }
    }

    private void onClientTick(Minecraft mc) {
        if (mc.level != null && ClientQuestFile.exists() && mc.player != null) {
            this.collectPinnedQuests(ClientQuestFile.INSTANCE);
            if (this.observationTasks == null) {
                this.observationTasks = ClientQuestFile.INSTANCE.collect(ObservationTask.class);
            }
            if (this.observationTasks.isEmpty()) {
                return;
            }
            this.currentlyObserving = null;
            TeamData selfTeamData = ClientQuestFile.INSTANCE.selfTeamData;
            if (mc.hitResult != null && mc.hitResult.getType() != HitResult.Type.MISS) {
                for (ObservationTask task : this.observationTasks) {
                    if (!selfTeamData.isCompleted(task) && task.observe(mc.player, mc.hitResult) && selfTeamData.canStartTasks(task.getQuest())) {
                        this.currentlyObserving = task;
                        break;
                    }
                }
            }
            if (this.currentlyObserving != null) {
                if (!mc.isPaused()) {
                    this.currentlyObservingTicks++;
                }
                if (this.currentlyObservingTicks >= this.currentlyObserving.getTimer()) {
                    new SubmitTaskMessage(this.currentlyObserving.id).sendToServer();
                    selfTeamData.addProgress(this.currentlyObserving, 1L);
                    this.currentlyObserving = null;
                    this.currentlyObservingTicks = 0L;
                }
            } else {
                this.currentlyObservingTicks = 0L;
            }
        }
    }

    private void onPlayerLogin(LocalPlayer localPlayer) {
        if (creativeTabRebuildPending) {
            FTBQuestsClient.rebuildCreativeTabs();
            creativeTabRebuildPending = false;
        }
    }

    private void onPlayerLogout(@Nullable LocalPlayer localPlayer) {
        StructureTask.syncKnownStructureList(List.of());
    }

    private void collectPinnedQuests(ClientQuestFile file) {
        TeamData data = file.selfTeamData;
        List<Quest> pinnedQuests = new ArrayList();
        LongSet pinnedIds = data.getPinnedQuestIds(FTBQuestsClient.getClientPlayer());
        if (!pinnedIds.isEmpty()) {
            if (pinnedIds.contains(1L)) {
                file.forAllQuests(questx -> {
                    if (!data.isCompleted(questx) && data.canStartTasks(questx)) {
                        pinnedQuests.add(questx);
                    }
                });
            } else {
                pinnedIds.longStream().mapToObj(file::getQuest).filter(Objects::nonNull).forEach(pinnedQuests::add);
            }
        }
        Minecraft mc = Minecraft.getInstance();
        this.pinnedQuestText.clear();
        for (int i = 0; i < pinnedQuests.size(); i++) {
            Quest quest = (Quest) pinnedQuests.get(i);
            if (i > 0) {
                this.pinnedQuestText.add(FormattedCharSequence.EMPTY);
            }
            this.pinnedQuestText.addAll(mc.font.split(FormattedText.composite(mc.font.getSplitter().headByWidth(quest.getTitle(), 160, Style.EMPTY.withBold(true)), Component.literal(" ").withStyle(ChatFormatting.DARK_AQUA).append(data.getRelativeProgress(quest) + "%")), 500));
            for (Task task : quest.getTasks()) {
                if (!data.isCompleted(task)) {
                    this.pinnedQuestText.addAll(mc.font.split(FormattedText.composite(mc.font.getSplitter().headByWidth(task.getMutableTitle().withStyle(ChatFormatting.GRAY), 160, Style.EMPTY.applyFormat(ChatFormatting.GRAY)), Component.literal(" ").withStyle(ChatFormatting.GREEN).append(task.formatProgress(data, data.getProgress(task))).append("/").append(task.formatMaxProgress())), 500));
                }
            }
        }
    }

    private void onScreenRender(GuiGraphics graphics, float tickDelta) {
        if (ClientQuestFile.exists()) {
            ClientQuestFile file = ClientQuestFile.INSTANCE;
            Minecraft mc = Minecraft.getInstance();
            int cy = mc.getWindow().getGuiScaledHeight() / 2;
            if (this.currentlyObserving != null) {
                int cx = mc.getWindow().getGuiScaledWidth() / 2;
                MutableComponent cot = this.currentlyObserving.getMutableTitle().withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE);
                int sw = mc.font.width(cot);
                int bw = Math.max(sw, 100);
                Color4I.DARK_GRAY.withAlpha(130).draw(graphics, cx - bw / 2 - 3, cy - 63, bw + 6, 29);
                GuiHelper.drawHollowRect(graphics, cx - bw / 2 - 3, cy - 63, bw + 6, 29, Color4I.DARK_GRAY, false);
                graphics.drawString(mc.font, cot, cx - sw / 2, cy - 60, 16777215);
                double completed = (double) ((float) this.currentlyObservingTicks + tickDelta) / (double) this.currentlyObserving.getTimer();
                GuiHelper.drawHollowRect(graphics, cx - bw / 2, cy - 49, bw, 12, Color4I.DARK_GRAY, false);
                Color4I.LIGHT_BLUE.withAlpha(130).draw(graphics, cx - bw / 2 + 1, cy - 48, (int) (((double) bw - 2.0) * completed), 10);
                String cop = this.currentlyObservingTicks * 100L / this.currentlyObserving.getTimer() + "%";
                graphics.drawString(mc.font, cop, cx - mc.font.width(cop) / 2, cy - 47, 16777215);
            }
            if (!this.pinnedQuestText.isEmpty()) {
                int width = 0;
                for (FormattedCharSequence s : this.pinnedQuestText) {
                    width = Math.max(width, (int) mc.font.getSplitter().stringWidth(s));
                }
                width += 8;
                int height = 9 * this.pinnedQuestText.size() + 8;
                float scale = ThemeProperties.PINNED_QUEST_SIZE.get(file).floatValue();
                int insetX = FTBQuestsClientConfig.PINNED_QUESTS_INSET_X.get();
                int insetY = FTBQuestsClientConfig.PINNED_QUESTS_INSET_Y.get();
                PanelPositioning.PanelPos pos = FTBQuestsClientConfig.PINNED_QUESTS_POS.get().getPanelPos(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), (int) ((float) width * scale), (int) ((float) height * scale), insetX, insetY);
                graphics.pose().pushPose();
                graphics.pose().translate((float) pos.x(), (float) pos.y(), 100.0F);
                graphics.pose().scale(scale, scale, 1.0F);
                GuiHelper.drawHollowRect(graphics, 0, 0, width, height, Color4I.BLACK.withAlpha(100), false);
                Color4I.BLACK.withAlpha(100).draw(graphics, 0, 0, width, height);
                graphics.pose().translate(4.0F, 4.0F, 0.0F);
                for (int i = 0; i < this.pinnedQuestText.size(); i++) {
                    graphics.drawString(mc.font, (FormattedCharSequence) this.pinnedQuestText.get(i), 0, i * 9, -1);
                }
                graphics.pose().popPose();
            }
        }
    }
}