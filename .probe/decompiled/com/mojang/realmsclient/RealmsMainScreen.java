package com.mojang.realmsclient;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import com.mojang.realmsclient.client.Ping;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.dto.RealmsNotification;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServerPlayerList;
import com.mojang.realmsclient.dto.RegionPingResult;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.RealmsNewsManager;
import com.mojang.realmsclient.gui.RealmsServerList;
import com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsParentalConsentScreen;
import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
import com.mojang.realmsclient.gui.task.DataFetcher;
import com.mojang.realmsclient.util.RealmsPersistence;
import com.mojang.realmsclient.util.RealmsUtil;
import com.mojang.realmsclient.util.task.GetServerDetailsTask;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsObjectSelectionList;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonLinks;
import net.minecraft.util.Mth;
import org.slf4j.Logger;

public class RealmsMainScreen extends RealmsScreen {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation ON_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/on_icon.png");

    private static final ResourceLocation OFF_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/off_icon.png");

    private static final ResourceLocation EXPIRED_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/expired_icon.png");

    private static final ResourceLocation EXPIRES_SOON_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/expires_soon_icon.png");

    static final ResourceLocation INVITATION_ICONS_LOCATION = new ResourceLocation("realms", "textures/gui/realms/invitation_icons.png");

    static final ResourceLocation INVITE_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/invite_icon.png");

    static final ResourceLocation WORLDICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/world_icon.png");

    private static final ResourceLocation LOGO_LOCATION = new ResourceLocation("realms", "textures/gui/title/realms.png");

    private static final ResourceLocation NEWS_LOCATION = new ResourceLocation("realms", "textures/gui/realms/news_icon.png");

    private static final ResourceLocation POPUP_LOCATION = new ResourceLocation("realms", "textures/gui/realms/popup.png");

    private static final ResourceLocation DARKEN_LOCATION = new ResourceLocation("realms", "textures/gui/realms/darken.png");

    static final ResourceLocation CROSS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/cross_icon.png");

    private static final ResourceLocation TRIAL_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/trial_icon.png");

    static final ResourceLocation INFO_ICON_LOCATION = new ResourceLocation("minecraft", "textures/gui/info_icon.png");

    static final List<Component> TRIAL_MESSAGE_LINES = ImmutableList.of(Component.translatable("mco.trial.message.line1"), Component.translatable("mco.trial.message.line2"));

    static final Component SERVER_UNITIALIZED_TEXT = Component.translatable("mco.selectServer.uninitialized");

    static final Component SUBSCRIPTION_EXPIRED_TEXT = Component.translatable("mco.selectServer.expiredList");

    private static final Component SUBSCRIPTION_RENEW_TEXT = Component.translatable("mco.selectServer.expiredRenew");

    static final Component TRIAL_EXPIRED_TEXT = Component.translatable("mco.selectServer.expiredTrial");

    static final Component SELECT_MINIGAME_PREFIX = Component.translatable("mco.selectServer.minigame").append(CommonComponents.SPACE);

    private static final Component POPUP_TEXT = Component.translatable("mco.selectServer.popup");

    private static final Component PLAY_TEXT = Component.translatable("mco.selectServer.play");

    private static final Component LEAVE_SERVER_TEXT = Component.translatable("mco.selectServer.leave");

    private static final Component CONFIGURE_SERVER_TEXT = Component.translatable("mco.selectServer.configure");

    private static final Component SERVER_EXPIRED_TOOLTIP = Component.translatable("mco.selectServer.expired");

    private static final Component SERVER_EXPIRES_SOON_TOOLTIP = Component.translatable("mco.selectServer.expires.soon");

    private static final Component SERVER_EXPIRES_IN_DAY_TOOLTIP = Component.translatable("mco.selectServer.expires.day");

    private static final Component SERVER_OPEN_TOOLTIP = Component.translatable("mco.selectServer.open");

    private static final Component SERVER_CLOSED_TOOLTIP = Component.translatable("mco.selectServer.closed");

    private static final Component NEWS_TOOLTIP = Component.translatable("mco.news");

    static final Component UNITIALIZED_WORLD_NARRATION = Component.translatable("gui.narrate.button", SERVER_UNITIALIZED_TEXT);

    static final Component TRIAL_TEXT = CommonComponents.joinLines(TRIAL_MESSAGE_LINES);

    private static final int BUTTON_WIDTH = 100;

    private static final int BUTTON_TOP_ROW_WIDTH = 308;

    private static final int BUTTON_BOTTOM_ROW_WIDTH = 204;

    private static final int FOOTER_HEIGHT = 64;

    private static final int LOGO_WIDTH = 128;

    private static final int LOGO_HEIGHT = 34;

    private static final int LOGO_TEXTURE_WIDTH = 128;

    private static final int LOGO_TEXTURE_HEIGHT = 64;

    private static final int LOGO_PADDING = 5;

    private static final int HEADER_HEIGHT = 44;

    private static List<ResourceLocation> teaserImages = ImmutableList.of();

    @Nullable
    private DataFetcher.Subscription dataSubscription;

    private RealmsServerList serverList;

    private final Set<UUID> handledSeenNotifications = new HashSet();

    private static boolean overrideConfigure;

    private static int lastScrollYPosition = -1;

    static volatile boolean hasParentalConsent;

    static volatile boolean checkedParentalConsent;

    static volatile boolean checkedClientCompatability;

    @Nullable
    static Screen realmsGenericErrorScreen;

    private static boolean regionsPinged;

    private final RateLimiter inviteNarrationLimiter;

    private boolean dontSetConnectedToRealms;

    final Screen lastScreen;

    RealmsMainScreen.RealmSelectionList realmSelectionList;

    private boolean realmsSelectionListAdded;

    private Button playButton;

    private Button backButton;

    private Button renewButton;

    private Button configureButton;

    private Button leaveButton;

    private List<RealmsServer> realmsServers = ImmutableList.of();

    volatile int numberOfPendingInvites;

    int animTick;

    private boolean hasFetchedServers;

    boolean popupOpenedByUser;

    private boolean justClosedPopup;

    private volatile boolean trialsAvailable;

    private volatile boolean createdTrial;

    private volatile boolean showingPopup;

    volatile boolean hasUnreadNews;

    @Nullable
    volatile String newsLink;

    private int carouselIndex;

    private int carouselTick;

    private boolean hasSwitchedCarouselImage;

    private List<KeyCombo> keyCombos;

    long lastClickTime;

    private ReentrantLock connectLock = new ReentrantLock();

    private MultiLineLabel formattedPopup = MultiLineLabel.EMPTY;

    private final List<RealmsNotification> notifications = new ArrayList();

    private Button showPopupButton;

    private RealmsMainScreen.PendingInvitesButton pendingInvitesButton;

    private Button newsButton;

    private Button createTrialButton;

    private Button buyARealmButton;

    private Button closeButton;

    public RealmsMainScreen(Screen screen0) {
        super(GameNarrator.NO_TITLE);
        this.lastScreen = screen0;
        this.inviteNarrationLimiter = RateLimiter.create(0.016666668F);
    }

    private boolean shouldShowMessageInList() {
        if (hasParentalConsent() && this.hasFetchedServers) {
            if (this.trialsAvailable && !this.createdTrial) {
                return true;
            } else {
                for (RealmsServer $$0 : this.realmsServers) {
                    if ($$0.ownerUUID.equals(this.f_96541_.getUser().getUuid())) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean shouldShowPopup() {
        if (!hasParentalConsent() || !this.hasFetchedServers) {
            return false;
        } else {
            return this.popupOpenedByUser ? true : this.realmsServers.isEmpty();
        }
    }

    @Override
    public void init() {
        this.keyCombos = Lists.newArrayList(new KeyCombo[] { new KeyCombo(new char[] { '3', '2', '1', '4', '5', '6' }, () -> overrideConfigure = !overrideConfigure), new KeyCombo(new char[] { '9', '8', '7', '1', '2', '3' }, () -> {
            if (RealmsClient.currentEnvironment == RealmsClient.Environment.STAGE) {
                this.switchToProd();
            } else {
                this.switchToStage();
            }
        }), new KeyCombo(new char[] { '9', '8', '7', '4', '5', '6' }, () -> {
            if (RealmsClient.currentEnvironment == RealmsClient.Environment.LOCAL) {
                this.switchToProd();
            } else {
                this.switchToLocal();
            }
        }) });
        if (realmsGenericErrorScreen != null) {
            this.f_96541_.setScreen(realmsGenericErrorScreen);
        } else {
            this.connectLock = new ReentrantLock();
            if (checkedClientCompatability && !hasParentalConsent()) {
                this.checkParentalConsent();
            }
            this.checkClientCompatability();
            if (!this.dontSetConnectedToRealms) {
                this.f_96541_.setConnectedToRealms(false);
            }
            this.showingPopup = false;
            this.realmSelectionList = new RealmsMainScreen.RealmSelectionList();
            if (lastScrollYPosition != -1) {
                this.realmSelectionList.m_93410_((double) lastScrollYPosition);
            }
            this.m_7787_(this.realmSelectionList);
            this.realmsSelectionListAdded = true;
            this.m_264313_(this.realmSelectionList);
            this.addMiddleButtons();
            this.addFooterButtons();
            this.addTopButtons();
            this.updateButtonStates(null);
            this.formattedPopup = MultiLineLabel.create(this.f_96547_, POPUP_TEXT, 100);
            RealmsNewsManager $$0 = this.f_96541_.realmsDataFetcher().newsManager;
            this.hasUnreadNews = $$0.hasUnreadNews();
            this.newsLink = $$0.newsLink();
            if (this.serverList == null) {
                this.serverList = new RealmsServerList(this.f_96541_);
            }
            if (this.dataSubscription != null) {
                this.dataSubscription.forceUpdate();
            }
        }
    }

    private static boolean hasParentalConsent() {
        return checkedParentalConsent && hasParentalConsent;
    }

    public void addTopButtons() {
        this.pendingInvitesButton = (RealmsMainScreen.PendingInvitesButton) this.m_142416_(new RealmsMainScreen.PendingInvitesButton());
        this.newsButton = (Button) this.m_142416_(new RealmsMainScreen.NewsButton());
        this.showPopupButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.selectServer.purchase"), p_86597_ -> this.popupOpenedByUser = !this.popupOpenedByUser).bounds(this.f_96543_ - 90, 12, 80, 20).build());
    }

    public void addMiddleButtons() {
        this.createTrialButton = (Button) this.m_7787_(Button.builder(Component.translatable("mco.selectServer.trial"), p_280681_ -> {
            if (this.trialsAvailable && !this.createdTrial) {
                Util.getPlatform().openUri("https://aka.ms/startjavarealmstrial");
                this.f_96541_.setScreen(this.lastScreen);
            }
        }).bounds(this.f_96543_ / 2 + 52, this.popupY0() + 137 - 20, 98, 20).build());
        this.buyARealmButton = (Button) this.m_7787_(Button.builder(Component.translatable("mco.selectServer.buy"), p_231255_ -> Util.getPlatform().openUri("https://aka.ms/BuyJavaRealms")).bounds(this.f_96543_ / 2 + 52, this.popupY0() + 160 - 20, 98, 20).build());
        this.closeButton = (Button) this.m_7787_(new RealmsMainScreen.CloseButton());
    }

    public void addFooterButtons() {
        this.playButton = Button.builder(PLAY_TEXT, p_86659_ -> this.play(this.getSelectedServer(), this)).width(100).build();
        this.configureButton = Button.builder(CONFIGURE_SERVER_TEXT, p_86672_ -> this.configureClicked(this.getSelectedServer())).width(100).build();
        this.renewButton = Button.builder(SUBSCRIPTION_RENEW_TEXT, p_86622_ -> this.onRenew(this.getSelectedServer())).width(100).build();
        this.leaveButton = Button.builder(LEAVE_SERVER_TEXT, p_86679_ -> this.leaveClicked(this.getSelectedServer())).width(100).build();
        this.backButton = Button.builder(CommonComponents.GUI_BACK, p_280683_ -> {
            if (!this.justClosedPopup) {
                this.f_96541_.setScreen(this.lastScreen);
            }
        }).width(100).build();
        GridLayout $$0 = new GridLayout();
        GridLayout.RowHelper $$1 = $$0.createRowHelper(1);
        LinearLayout $$2 = $$1.addChild(new LinearLayout(308, 20, LinearLayout.Orientation.HORIZONTAL), $$1.newCellSettings().paddingBottom(4));
        $$2.addChild(this.playButton);
        $$2.addChild(this.configureButton);
        $$2.addChild(this.renewButton);
        LinearLayout $$3 = $$1.addChild(new LinearLayout(204, 20, LinearLayout.Orientation.HORIZONTAL), $$1.newCellSettings().alignHorizontallyCenter());
        $$3.addChild(this.leaveButton);
        $$3.addChild(this.backButton);
        $$0.m_264134_(p_272289_ -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(p_272289_);
        });
        $$0.arrangeElements();
        FrameLayout.centerInRectangle($$0, 0, this.f_96544_ - 64, this.f_96543_, 64);
    }

    void updateButtonStates(@Nullable RealmsServer realmsServer0) {
        this.backButton.f_93623_ = true;
        if (hasParentalConsent() && this.hasFetchedServers) {
            boolean $$1 = this.shouldShowPopup() && this.trialsAvailable && !this.createdTrial;
            this.createTrialButton.f_93624_ = $$1;
            this.createTrialButton.f_93623_ = $$1;
            this.buyARealmButton.f_93624_ = this.shouldShowPopup();
            this.closeButton.f_93624_ = this.shouldShowPopup();
            this.newsButton.f_93623_ = true;
            this.newsButton.f_93624_ = this.newsLink != null;
            this.pendingInvitesButton.f_93623_ = true;
            this.pendingInvitesButton.f_93624_ = true;
            this.showPopupButton.f_93623_ = !this.shouldShowPopup();
            this.playButton.f_93624_ = !this.shouldShowPopup();
            this.renewButton.f_93624_ = !this.shouldShowPopup();
            this.leaveButton.f_93624_ = !this.shouldShowPopup();
            this.configureButton.f_93624_ = !this.shouldShowPopup();
            this.backButton.f_93624_ = !this.shouldShowPopup();
            this.playButton.f_93623_ = this.shouldPlayButtonBeActive(realmsServer0);
            this.renewButton.f_93623_ = this.shouldRenewButtonBeActive(realmsServer0);
            this.leaveButton.f_93623_ = this.shouldLeaveButtonBeActive(realmsServer0);
            this.configureButton.f_93623_ = this.shouldConfigureButtonBeActive(realmsServer0);
        } else {
            m_202376_(new AbstractWidget[] { this.playButton, this.renewButton, this.configureButton, this.createTrialButton, this.buyARealmButton, this.closeButton, this.newsButton, this.pendingInvitesButton, this.showPopupButton, this.leaveButton });
        }
    }

    private boolean shouldShowPopupButton() {
        return (!this.shouldShowPopup() || this.popupOpenedByUser) && hasParentalConsent() && this.hasFetchedServers;
    }

    boolean shouldPlayButtonBeActive(@Nullable RealmsServer realmsServer0) {
        return realmsServer0 != null && !realmsServer0.expired && realmsServer0.state == RealmsServer.State.OPEN;
    }

    private boolean shouldRenewButtonBeActive(@Nullable RealmsServer realmsServer0) {
        return realmsServer0 != null && realmsServer0.expired && this.isSelfOwnedServer(realmsServer0);
    }

    private boolean shouldConfigureButtonBeActive(@Nullable RealmsServer realmsServer0) {
        return realmsServer0 != null && this.isSelfOwnedServer(realmsServer0);
    }

    private boolean shouldLeaveButtonBeActive(@Nullable RealmsServer realmsServer0) {
        return realmsServer0 != null && !this.isSelfOwnedServer(realmsServer0);
    }

    @Override
    public void tick() {
        super.m_86600_();
        if (this.pendingInvitesButton != null) {
            this.pendingInvitesButton.tick();
        }
        this.justClosedPopup = false;
        this.animTick++;
        boolean $$0 = hasParentalConsent();
        if (this.dataSubscription == null && $$0) {
            this.dataSubscription = this.initDataFetcher(this.f_96541_.realmsDataFetcher());
        } else if (this.dataSubscription != null && !$$0) {
            this.dataSubscription = null;
        }
        if (this.dataSubscription != null) {
            this.dataSubscription.tick();
        }
        if (this.shouldShowPopup()) {
            this.carouselTick++;
        }
        if (this.showPopupButton != null) {
            this.showPopupButton.f_93624_ = this.shouldShowPopupButton();
            this.showPopupButton.f_93623_ = this.showPopupButton.f_93624_;
        }
    }

    private DataFetcher.Subscription initDataFetcher(RealmsDataFetcher realmsDataFetcher0) {
        DataFetcher.Subscription $$1 = realmsDataFetcher0.dataFetcher.createSubscription();
        $$1.subscribe(realmsDataFetcher0.serverListUpdateTask, p_275856_ -> {
            List<RealmsServer> $$1x = this.serverList.updateServersList(p_275856_);
            boolean $$2 = false;
            for (RealmsServer $$3 : $$1x) {
                if (this.isSelfOwnedNonExpiredServer($$3)) {
                    $$2 = true;
                }
            }
            this.realmsServers = $$1x;
            this.hasFetchedServers = true;
            this.refreshRealmsSelectionList();
            if (!regionsPinged && $$2) {
                regionsPinged = true;
                this.pingRegions();
            }
        });
        callRealmsClient(RealmsClient::m_274314_, p_274622_ -> {
            this.notifications.clear();
            this.notifications.addAll(p_274622_);
            this.refreshRealmsSelectionList();
        });
        $$1.subscribe(realmsDataFetcher0.pendingInvitesTask, p_280682_ -> {
            this.numberOfPendingInvites = p_280682_;
            if (this.numberOfPendingInvites > 0 && this.inviteNarrationLimiter.tryAcquire(1)) {
                this.f_96541_.getNarrator().sayNow(Component.translatable("mco.configure.world.invite.narration", this.numberOfPendingInvites));
            }
        });
        $$1.subscribe(realmsDataFetcher0.trialAvailabilityTask, p_238839_ -> {
            if (!this.createdTrial) {
                if (p_238839_ != this.trialsAvailable && this.shouldShowPopup()) {
                    this.trialsAvailable = p_238839_;
                    this.showingPopup = false;
                } else {
                    this.trialsAvailable = p_238839_;
                }
            }
        });
        $$1.subscribe(realmsDataFetcher0.liveStatsTask, p_238847_ -> {
            for (RealmsServerPlayerList $$1x : p_238847_.servers) {
                for (RealmsServer $$2 : this.realmsServers) {
                    if ($$2.id == $$1x.serverId) {
                        $$2.updateServerPing($$1x);
                        break;
                    }
                }
            }
        });
        $$1.subscribe(realmsDataFetcher0.newsTask, p_231355_ -> {
            realmsDataFetcher0.newsManager.updateUnreadNews(p_231355_);
            this.hasUnreadNews = realmsDataFetcher0.newsManager.hasUnreadNews();
            this.newsLink = realmsDataFetcher0.newsManager.newsLink();
            this.updateButtonStates(null);
        });
        return $$1;
    }

    private static <T> void callRealmsClient(RealmsMainScreen.RealmsCall<T> realmsMainScreenRealmsCallT0, Consumer<T> consumerT1) {
        Minecraft $$2 = Minecraft.getInstance();
        CompletableFuture.supplyAsync(() -> {
            try {
                return realmsMainScreenRealmsCallT0.request(RealmsClient.create($$2));
            } catch (RealmsServiceException var3) {
                throw new RuntimeException(var3);
            }
        }).thenAcceptAsync(consumerT1, $$2).exceptionally(p_274626_ -> {
            LOGGER.error("Failed to execute call to Realms Service", p_274626_);
            return null;
        });
    }

    private void refreshRealmsSelectionList() {
        boolean $$0 = !this.hasFetchedServers;
        this.realmSelectionList.m_7178_();
        List<UUID> $$1 = new ArrayList();
        for (RealmsNotification $$2 : this.notifications) {
            this.addEntriesForNotification(this.realmSelectionList, $$2);
            if (!$$2.seen() && !this.handledSeenNotifications.contains($$2.uuid())) {
                $$1.add($$2.uuid());
            }
        }
        if (!$$1.isEmpty()) {
            callRealmsClient(p_274625_ -> {
                p_274625_.notificationsSeen($$1);
                return null;
            }, p_274630_ -> this.handledSeenNotifications.addAll($$1));
        }
        if (this.shouldShowMessageInList()) {
            this.realmSelectionList.m_7085_(new RealmsMainScreen.TrialEntry());
        }
        RealmsMainScreen.Entry $$3 = null;
        RealmsServer $$4 = this.getSelectedServer();
        for (RealmsServer $$5 : this.realmsServers) {
            RealmsMainScreen.ServerEntry $$6 = new RealmsMainScreen.ServerEntry($$5);
            this.realmSelectionList.m_7085_($$6);
            if ($$4 != null && $$4.id == $$5.id) {
                $$3 = $$6;
            }
        }
        if ($$0) {
            this.updateButtonStates(null);
        } else {
            this.realmSelectionList.setSelected($$3);
        }
    }

    private void addEntriesForNotification(RealmsMainScreen.RealmSelectionList realmsMainScreenRealmSelectionList0, RealmsNotification realmsNotification1) {
        if (realmsNotification1 instanceof RealmsNotification.VisitUrl $$2) {
            realmsMainScreenRealmSelectionList0.m_7085_(new RealmsMainScreen.NotificationMessageEntry($$2.getMessage(), $$2));
            realmsMainScreenRealmSelectionList0.m_7085_(new RealmsMainScreen.ButtonEntry($$2.buildOpenLinkButton(this)));
        }
    }

    void refreshFetcher() {
        if (this.dataSubscription != null) {
            this.dataSubscription.reset();
        }
    }

    private void pingRegions() {
        new Thread(() -> {
            List<RegionPingResult> $$0 = Ping.pingAllRegions();
            RealmsClient $$1 = RealmsClient.create();
            PingResult $$2 = new PingResult();
            $$2.pingResults = $$0;
            $$2.worldIds = this.getOwnedNonExpiredWorldIds();
            try {
                $$1.sendPingResults($$2);
            } catch (Throwable var5) {
                LOGGER.warn("Could not send ping result to Realms: ", var5);
            }
        }).start();
    }

    private List<Long> getOwnedNonExpiredWorldIds() {
        List<Long> $$0 = Lists.newArrayList();
        for (RealmsServer $$1 : this.realmsServers) {
            if (this.isSelfOwnedNonExpiredServer($$1)) {
                $$0.add($$1.id);
            }
        }
        return $$0;
    }

    public void setCreatedTrial(boolean boolean0) {
        this.createdTrial = boolean0;
    }

    private void onRenew(@Nullable RealmsServer realmsServer0) {
        if (realmsServer0 != null) {
            String $$1 = CommonLinks.extendRealms(realmsServer0.remoteSubscriptionId, this.f_96541_.getUser().getUuid(), realmsServer0.expiredTrial);
            this.f_96541_.keyboardHandler.setClipboard($$1);
            Util.getPlatform().openUri($$1);
        }
    }

    private void checkClientCompatability() {
        if (!checkedClientCompatability) {
            checkedClientCompatability = true;
            (new Thread("MCO Compatability Checker #1") {

                public void run() {
                    RealmsClient $$0 = RealmsClient.create();
                    try {
                        RealmsClient.CompatibleVersionResponse $$1 = $$0.clientCompatible();
                        if ($$1 != RealmsClient.CompatibleVersionResponse.COMPATIBLE) {
                            RealmsMainScreen.realmsGenericErrorScreen = new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen);
                            RealmsMainScreen.this.f_96541_.execute(() -> RealmsMainScreen.this.f_96541_.setScreen(RealmsMainScreen.realmsGenericErrorScreen));
                            return;
                        }
                        RealmsMainScreen.this.checkParentalConsent();
                    } catch (RealmsServiceException var3) {
                        RealmsMainScreen.checkedClientCompatability = false;
                        RealmsMainScreen.LOGGER.error("Couldn't connect to realms", var3);
                        if (var3.httpResultCode == 401) {
                            RealmsMainScreen.realmsGenericErrorScreen = new RealmsGenericErrorScreen(Component.translatable("mco.error.invalid.session.title"), Component.translatable("mco.error.invalid.session.message"), RealmsMainScreen.this.lastScreen);
                            RealmsMainScreen.this.f_96541_.execute(() -> RealmsMainScreen.this.f_96541_.setScreen(RealmsMainScreen.realmsGenericErrorScreen));
                        } else {
                            RealmsMainScreen.this.f_96541_.execute(() -> RealmsMainScreen.this.f_96541_.setScreen(new RealmsGenericErrorScreen(var3, RealmsMainScreen.this.lastScreen)));
                        }
                    }
                }
            }).start();
        }
    }

    void checkParentalConsent() {
        (new Thread("MCO Compatability Checker #1") {

            public void run() {
                RealmsClient $$0 = RealmsClient.create();
                try {
                    Boolean $$1 = $$0.mcoEnabled();
                    if ($$1) {
                        RealmsMainScreen.LOGGER.info("Realms is available for this user");
                        RealmsMainScreen.hasParentalConsent = true;
                    } else {
                        RealmsMainScreen.LOGGER.info("Realms is not available for this user");
                        RealmsMainScreen.hasParentalConsent = false;
                        RealmsMainScreen.this.f_96541_.execute(() -> RealmsMainScreen.this.f_96541_.setScreen(new RealmsParentalConsentScreen(RealmsMainScreen.this.lastScreen)));
                    }
                    RealmsMainScreen.checkedParentalConsent = true;
                } catch (RealmsServiceException var3) {
                    RealmsMainScreen.LOGGER.error("Couldn't connect to realms", var3);
                    RealmsMainScreen.this.f_96541_.execute(() -> RealmsMainScreen.this.f_96541_.setScreen(new RealmsGenericErrorScreen(var3, RealmsMainScreen.this.lastScreen)));
                }
            }
        }).start();
    }

    private void switchToStage() {
        if (RealmsClient.currentEnvironment != RealmsClient.Environment.STAGE) {
            (new Thread("MCO Stage Availability Checker #1") {

                public void run() {
                    RealmsClient $$0 = RealmsClient.create();
                    try {
                        Boolean $$1 = $$0.stageAvailable();
                        if ($$1) {
                            RealmsClient.switchToStage();
                            RealmsMainScreen.LOGGER.info("Switched to stage");
                            RealmsMainScreen.this.refreshFetcher();
                        }
                    } catch (RealmsServiceException var3) {
                        RealmsMainScreen.LOGGER.error("Couldn't connect to Realms: {}", var3.toString());
                    }
                }
            }).start();
        }
    }

    private void switchToLocal() {
        if (RealmsClient.currentEnvironment != RealmsClient.Environment.LOCAL) {
            (new Thread("MCO Local Availability Checker #1") {

                public void run() {
                    RealmsClient $$0 = RealmsClient.create();
                    try {
                        Boolean $$1 = $$0.stageAvailable();
                        if ($$1) {
                            RealmsClient.switchToLocal();
                            RealmsMainScreen.LOGGER.info("Switched to local");
                            RealmsMainScreen.this.refreshFetcher();
                        }
                    } catch (RealmsServiceException var3) {
                        RealmsMainScreen.LOGGER.error("Couldn't connect to Realms: {}", var3.toString());
                    }
                }
            }).start();
        }
    }

    private void switchToProd() {
        RealmsClient.switchToProd();
        this.refreshFetcher();
    }

    private void configureClicked(@Nullable RealmsServer realmsServer0) {
        if (realmsServer0 != null && (this.f_96541_.getUser().getUuid().equals(realmsServer0.ownerUUID) || overrideConfigure)) {
            this.saveListScrollPosition();
            this.f_96541_.setScreen(new RealmsConfigureWorldScreen(this, realmsServer0.id));
        }
    }

    private void leaveClicked(@Nullable RealmsServer realmsServer0) {
        if (realmsServer0 != null && !this.f_96541_.getUser().getUuid().equals(realmsServer0.ownerUUID)) {
            this.saveListScrollPosition();
            Component $$1 = Component.translatable("mco.configure.world.leave.question.line1");
            Component $$2 = Component.translatable("mco.configure.world.leave.question.line2");
            this.f_96541_.setScreen(new RealmsLongConfirmationScreen(p_231253_ -> this.leaveServer(p_231253_, realmsServer0), RealmsLongConfirmationScreen.Type.INFO, $$1, $$2, true));
        }
    }

    private void saveListScrollPosition() {
        lastScrollYPosition = (int) this.realmSelectionList.m_93517_();
    }

    @Nullable
    private RealmsServer getSelectedServer() {
        if (this.realmSelectionList == null) {
            return null;
        } else {
            RealmsMainScreen.Entry $$0 = (RealmsMainScreen.Entry) this.realmSelectionList.m_93511_();
            return $$0 != null ? $$0.getServer() : null;
        }
    }

    private void leaveServer(boolean boolean0, final RealmsServer realmsServer1) {
        if (boolean0) {
            (new Thread("Realms-leave-server") {

                public void run() {
                    try {
                        RealmsClient $$0 = RealmsClient.create();
                        $$0.uninviteMyselfFrom(realmsServer1.id);
                        RealmsMainScreen.this.f_96541_.execute(() -> RealmsMainScreen.this.removeServer(realmsServer1));
                    } catch (RealmsServiceException var2) {
                        RealmsMainScreen.LOGGER.error("Couldn't configure world");
                        RealmsMainScreen.this.f_96541_.execute(() -> RealmsMainScreen.this.f_96541_.setScreen(new RealmsGenericErrorScreen(var2, RealmsMainScreen.this)));
                    }
                }
            }).start();
        }
        this.f_96541_.setScreen(this);
    }

    void removeServer(RealmsServer realmsServer0) {
        this.realmsServers = this.serverList.removeItem(realmsServer0);
        this.realmSelectionList.m_6702_().removeIf(p_231250_ -> {
            RealmsServer $$2 = p_231250_.getServer();
            return $$2 != null && $$2.id == realmsServer0.id;
        });
        this.realmSelectionList.setSelected(null);
        this.updateButtonStates(null);
        this.playButton.f_93623_ = false;
    }

    void dismissNotification(UUID uUID0) {
        callRealmsClient(p_274628_ -> {
            p_274628_.notificationsDismiss(List.of(uUID0));
            return null;
        }, p_274632_ -> {
            this.notifications.removeIf(p_274621_ -> p_274621_.dismissable() && uUID0.equals(p_274621_.uuid()));
            this.refreshRealmsSelectionList();
        });
    }

    public void resetScreen() {
        if (this.realmSelectionList != null) {
            this.realmSelectionList.setSelected(null);
        }
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.keyCombos.forEach(KeyCombo::m_86227_);
            this.onClosePopup();
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    void onClosePopup() {
        if (this.shouldShowPopup() && this.popupOpenedByUser) {
            this.popupOpenedByUser = false;
        } else {
            this.f_96541_.setScreen(this.lastScreen);
        }
    }

    @Override
    public boolean charTyped(char char0, int int1) {
        this.keyCombos.forEach(p_231245_ -> p_231245_.keyPressed(char0));
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.realmSelectionList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.blit(LOGO_LOCATION, this.f_96543_ / 2 - 64, 5, 0.0F, 0.0F, 128, 34, 128, 64);
        if (RealmsClient.currentEnvironment == RealmsClient.Environment.STAGE) {
            this.renderStage(guiGraphics0);
        }
        if (RealmsClient.currentEnvironment == RealmsClient.Environment.LOCAL) {
            this.renderLocal(guiGraphics0);
        }
        if (this.shouldShowPopup()) {
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate(0.0F, 0.0F, 100.0F);
            this.drawPopup(guiGraphics0, int1, int2, float3);
            guiGraphics0.pose().popPose();
        } else {
            if (this.showingPopup) {
                this.updateButtonStates(null);
                if (!this.realmsSelectionListAdded) {
                    this.m_7787_(this.realmSelectionList);
                    this.realmsSelectionListAdded = true;
                }
                this.playButton.f_93623_ = this.shouldPlayButtonBeActive(this.getSelectedServer());
            }
            this.showingPopup = false;
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
        if (this.trialsAvailable && !this.createdTrial && this.shouldShowPopup()) {
            int $$4 = 8;
            int $$5 = 8;
            int $$6 = 0;
            if ((Util.getMillis() / 800L & 1L) == 1L) {
                $$6 = 8;
            }
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate(0.0F, 0.0F, 110.0F);
            guiGraphics0.blit(TRIAL_ICON_LOCATION, this.createTrialButton.m_252754_() + this.createTrialButton.m_5711_() - 8 - 4, this.createTrialButton.m_252907_() + this.createTrialButton.m_93694_() / 2 - 4, 0.0F, (float) $$6, 8, 8, 8, 16);
            guiGraphics0.pose().popPose();
        }
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.isOutsidePopup(double0, double1) && this.popupOpenedByUser) {
            this.popupOpenedByUser = false;
            this.justClosedPopup = true;
            return true;
        } else {
            return super.m_6375_(double0, double1, int2);
        }
    }

    private boolean isOutsidePopup(double double0, double double1) {
        int $$2 = this.popupX0();
        int $$3 = this.popupY0();
        return double0 < (double) ($$2 - 5) || double0 > (double) ($$2 + 315) || double1 < (double) ($$3 - 5) || double1 > (double) ($$3 + 171);
    }

    private void drawPopup(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = this.popupX0();
        int $$5 = this.popupY0();
        if (!this.showingPopup) {
            this.carouselIndex = 0;
            this.carouselTick = 0;
            this.hasSwitchedCarouselImage = true;
            this.updateButtonStates(null);
            if (this.realmsSelectionListAdded) {
                this.m_169411_(this.realmSelectionList);
                this.realmsSelectionListAdded = false;
            }
            this.f_96541_.getNarrator().sayNow(POPUP_TEXT);
        }
        if (this.hasFetchedServers) {
            this.showingPopup = true;
        }
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 0.7F);
        RenderSystem.enableBlend();
        guiGraphics0.blit(DARKEN_LOCATION, 0, 44, 0.0F, 0.0F, this.f_96543_, this.f_96544_ - 44, 310, 166);
        RenderSystem.disableBlend();
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics0.blit(POPUP_LOCATION, $$4, $$5, 0.0F, 0.0F, 310, 166, 310, 166);
        if (!teaserImages.isEmpty()) {
            guiGraphics0.blit((ResourceLocation) teaserImages.get(this.carouselIndex), $$4 + 7, $$5 + 7, 0.0F, 0.0F, 195, 152, 195, 152);
            if (this.carouselTick % 95 < 5) {
                if (!this.hasSwitchedCarouselImage) {
                    this.carouselIndex = (this.carouselIndex + 1) % teaserImages.size();
                    this.hasSwitchedCarouselImage = true;
                }
            } else {
                this.hasSwitchedCarouselImage = false;
            }
        }
        this.formattedPopup.renderLeftAlignedNoShadow(guiGraphics0, this.f_96543_ / 2 + 52, $$5 + 7, 10, 16777215);
        this.createTrialButton.m_88315_(guiGraphics0, int1, int2, float3);
        this.buyARealmButton.m_88315_(guiGraphics0, int1, int2, float3);
        this.closeButton.m_88315_(guiGraphics0, int1, int2, float3);
    }

    int popupX0() {
        return (this.f_96543_ - 310) / 2;
    }

    int popupY0() {
        return this.f_96544_ / 2 - 80;
    }

    public void play(@Nullable RealmsServer realmsServer0, Screen screen1) {
        if (realmsServer0 != null) {
            try {
                if (!this.connectLock.tryLock(1L, TimeUnit.SECONDS)) {
                    return;
                }
                if (this.connectLock.getHoldCount() > 1) {
                    return;
                }
            } catch (InterruptedException var4) {
                return;
            }
            this.dontSetConnectedToRealms = true;
            this.f_96541_.setScreen(new RealmsLongRunningMcoTaskScreen(screen1, new GetServerDetailsTask(this, screen1, realmsServer0, this.connectLock)));
        }
    }

    boolean isSelfOwnedServer(RealmsServer realmsServer0) {
        return realmsServer0.ownerUUID != null && realmsServer0.ownerUUID.equals(this.f_96541_.getUser().getUuid());
    }

    private boolean isSelfOwnedNonExpiredServer(RealmsServer realmsServer0) {
        return this.isSelfOwnedServer(realmsServer0) && !realmsServer0.expired;
    }

    void drawExpired(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        guiGraphics0.blit(EXPIRED_ICON_LOCATION, int1, int2, 0.0F, 0.0F, 10, 28, 10, 28);
        if (int3 >= int1 && int3 <= int1 + 9 && int4 >= int2 && int4 <= int2 + 27 && int4 < this.f_96544_ - 40 && int4 > 32 && !this.shouldShowPopup()) {
            this.m_257404_(SERVER_EXPIRED_TOOLTIP);
        }
    }

    void drawExpiring(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5) {
        if (this.animTick % 20 < 10) {
            guiGraphics0.blit(EXPIRES_SOON_ICON_LOCATION, int1, int2, 0.0F, 0.0F, 10, 28, 20, 28);
        } else {
            guiGraphics0.blit(EXPIRES_SOON_ICON_LOCATION, int1, int2, 10.0F, 0.0F, 10, 28, 20, 28);
        }
        if (int3 >= int1 && int3 <= int1 + 9 && int4 >= int2 && int4 <= int2 + 27 && int4 < this.f_96544_ - 40 && int4 > 32 && !this.shouldShowPopup()) {
            if (int5 <= 0) {
                this.m_257404_(SERVER_EXPIRES_SOON_TOOLTIP);
            } else if (int5 == 1) {
                this.m_257404_(SERVER_EXPIRES_IN_DAY_TOOLTIP);
            } else {
                this.m_257404_(Component.translatable("mco.selectServer.expires.days", int5));
            }
        }
    }

    void drawOpen(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        guiGraphics0.blit(ON_ICON_LOCATION, int1, int2, 0.0F, 0.0F, 10, 28, 10, 28);
        if (int3 >= int1 && int3 <= int1 + 9 && int4 >= int2 && int4 <= int2 + 27 && int4 < this.f_96544_ - 40 && int4 > 32 && !this.shouldShowPopup()) {
            this.m_257404_(SERVER_OPEN_TOOLTIP);
        }
    }

    void drawClose(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        guiGraphics0.blit(OFF_ICON_LOCATION, int1, int2, 0.0F, 0.0F, 10, 28, 10, 28);
        if (int3 >= int1 && int3 <= int1 + 9 && int4 >= int2 && int4 <= int2 + 27 && int4 < this.f_96544_ - 40 && int4 > 32 && !this.shouldShowPopup()) {
            this.m_257404_(SERVER_CLOSED_TOOLTIP);
        }
    }

    void renderNews(GuiGraphics guiGraphics0, int int1, int int2, boolean boolean3, int int4, int int5, boolean boolean6, boolean boolean7) {
        boolean $$8 = false;
        if (int1 >= int4 && int1 <= int4 + 20 && int2 >= int5 && int2 <= int5 + 20) {
            $$8 = true;
        }
        if (!boolean7) {
            guiGraphics0.setColor(0.5F, 0.5F, 0.5F, 1.0F);
        }
        boolean $$9 = boolean7 && boolean6;
        float $$10 = $$9 ? 20.0F : 0.0F;
        guiGraphics0.blit(NEWS_LOCATION, int4, int5, $$10, 0.0F, 20, 20, 40, 20);
        if ($$8 && boolean7) {
            this.m_257404_(NEWS_TOOLTIP);
        }
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (boolean3 && boolean7) {
            int $$11 = $$8 ? 0 : (int) (Math.max(0.0F, Math.max(Mth.sin((float) (10 + this.animTick) * 0.57F), Mth.cos((float) this.animTick * 0.35F))) * -6.0F);
            guiGraphics0.blit(INVITATION_ICONS_LOCATION, int4 + 10, int5 + 2 + $$11, 40.0F, 0.0F, 8, 8, 48, 16);
        }
    }

    private void renderLocal(GuiGraphics guiGraphics0) {
        String $$1 = "LOCAL!";
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((float) (this.f_96543_ / 2 - 25), 20.0F, 0.0F);
        guiGraphics0.pose().mulPose(Axis.ZP.rotationDegrees(-20.0F));
        guiGraphics0.pose().scale(1.5F, 1.5F, 1.5F);
        guiGraphics0.drawString(this.f_96547_, "LOCAL!", 0, 0, 8388479, false);
        guiGraphics0.pose().popPose();
    }

    private void renderStage(GuiGraphics guiGraphics0) {
        String $$1 = "STAGE!";
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((float) (this.f_96543_ / 2 - 25), 20.0F, 0.0F);
        guiGraphics0.pose().mulPose(Axis.ZP.rotationDegrees(-20.0F));
        guiGraphics0.pose().scale(1.5F, 1.5F, 1.5F);
        guiGraphics0.drawString(this.f_96547_, "STAGE!", 0, 0, -256, false);
        guiGraphics0.pose().popPose();
    }

    public RealmsMainScreen newScreen() {
        RealmsMainScreen $$0 = new RealmsMainScreen(this.lastScreen);
        $$0.m_6575_(this.f_96541_, this.f_96543_, this.f_96544_);
        return $$0;
    }

    public static void updateTeaserImages(ResourceManager resourceManager0) {
        Collection<ResourceLocation> $$1 = resourceManager0.listResources("textures/gui/images", p_193492_ -> p_193492_.getPath().endsWith(".png")).keySet();
        teaserImages = $$1.stream().filter(p_231247_ -> p_231247_.getNamespace().equals("realms")).toList();
    }

    class ButtonEntry extends RealmsMainScreen.Entry {

        private final Button button;

        private final int xPos = RealmsMainScreen.this.f_96543_ / 2 - 75;

        public ButtonEntry(Button button0) {
            this.button = button0;
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            this.button.m_6375_(double0, double1, int2);
            return true;
        }

        @Override
        public boolean keyPressed(int int0, int int1, int int2) {
            return this.button.m_7933_(int0, int1, int2) ? true : super.m_7933_(int0, int1, int2);
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.button.m_264152_(this.xPos, int2 + 4);
            this.button.m_88315_(guiGraphics0, int6, int7, float9);
        }

        @Override
        public Component getNarration() {
            return this.button.m_6035_();
        }
    }

    class CloseButton extends RealmsMainScreen.CrossButton {

        public CloseButton() {
            super(RealmsMainScreen.this.popupX0() + 4, RealmsMainScreen.this.popupY0() + 4, p_86775_ -> RealmsMainScreen.this.onClosePopup(), Component.translatable("mco.selectServer.close"));
        }
    }

    static class CrossButton extends Button {

        protected CrossButton(Button.OnPress buttonOnPress0, Component component1) {
            this(0, 0, buttonOnPress0, component1);
        }

        protected CrossButton(int int0, int int1, Button.OnPress buttonOnPress2, Component component3) {
            super(int0, int1, 14, 14, component3, buttonOnPress2, f_252438_);
            this.m_257544_(Tooltip.create(component3));
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
            float $$4 = this.m_198029_() ? 14.0F : 0.0F;
            guiGraphics0.blit(RealmsMainScreen.CROSS_ICON_LOCATION, this.m_252754_(), this.m_252907_(), 0.0F, $$4, 14, 14, 14, 28);
        }
    }

    abstract class Entry extends ObjectSelectionList.Entry<RealmsMainScreen.Entry> {

        @Nullable
        public RealmsServer getServer() {
            return null;
        }
    }

    class NewsButton extends Button {

        private static final int SIDE = 20;

        public NewsButton() {
            super(RealmsMainScreen.this.f_96543_ - 115, 12, 20, 20, Component.translatable("mco.news"), p_274636_ -> {
                if (RealmsMainScreen.this.newsLink != null) {
                    ConfirmLinkScreen.confirmLinkNow(RealmsMainScreen.this.newsLink, RealmsMainScreen.this, true);
                    if (RealmsMainScreen.this.hasUnreadNews) {
                        RealmsPersistence.RealmsPersistenceData $$2 = RealmsPersistence.readFile();
                        $$2.hasUnreadNews = false;
                        RealmsMainScreen.this.hasUnreadNews = false;
                        RealmsPersistence.writeFile($$2);
                    }
                }
            }, f_252438_);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
            RealmsMainScreen.this.renderNews(guiGraphics0, int1, int2, RealmsMainScreen.this.hasUnreadNews, this.m_252754_(), this.m_252907_(), this.m_198029_(), this.f_93623_);
        }
    }

    class NotificationMessageEntry extends RealmsMainScreen.Entry {

        private static final int SIDE_MARGINS = 40;

        private static final int ITEM_HEIGHT = 36;

        private static final int OUTLINE_COLOR = -12303292;

        private final Component text;

        private final List<AbstractWidget> children = new ArrayList();

        @Nullable
        private final RealmsMainScreen.CrossButton dismissButton;

        private final MultiLineTextWidget textWidget;

        private final GridLayout gridLayout;

        private final FrameLayout textFrame;

        private int lastEntryWidth = -1;

        public NotificationMessageEntry(Component component0, RealmsNotification realmsNotification1) {
            this.text = component0;
            this.gridLayout = new GridLayout();
            int $$2 = 7;
            this.gridLayout.addChild(new ImageWidget(20, 20, RealmsMainScreen.INFO_ICON_LOCATION), 0, 0, this.gridLayout.newCellSettings().padding(7, 7, 0, 0));
            this.gridLayout.addChild(SpacerElement.width(40), 0, 0);
            this.textFrame = this.gridLayout.addChild(new FrameLayout(0, 9 * 3), 0, 1, this.gridLayout.newCellSettings().paddingTop(7));
            this.textWidget = this.textFrame.addChild(new MultiLineTextWidget(component0, RealmsMainScreen.this.f_96547_).setCentered(true).setMaxRows(3), this.textFrame.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyTop());
            this.gridLayout.addChild(SpacerElement.width(40), 0, 2);
            if (realmsNotification1.dismissable()) {
                this.dismissButton = this.gridLayout.addChild(new RealmsMainScreen.CrossButton(p_275478_ -> RealmsMainScreen.this.dismissNotification(realmsNotification1.uuid()), Component.translatable("mco.notification.dismiss")), 0, 2, this.gridLayout.newCellSettings().alignHorizontallyRight().padding(0, 7, 7, 0));
            } else {
                this.dismissButton = null;
            }
            this.gridLayout.m_264134_(this.children::add);
        }

        @Override
        public boolean keyPressed(int int0, int int1, int int2) {
            return this.dismissButton != null && this.dismissButton.m_7933_(int0, int1, int2) ? true : super.m_7933_(int0, int1, int2);
        }

        private void updateEntryWidth(int int0) {
            if (this.lastEntryWidth != int0) {
                this.refreshLayout(int0);
                this.lastEntryWidth = int0;
            }
        }

        private void refreshLayout(int int0) {
            int $$1 = int0 - 80;
            this.textFrame.setMinWidth($$1);
            this.textWidget.setMaxWidth($$1);
            this.gridLayout.arrangeElements();
        }

        @Override
        public void renderBack(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            super.m_274437_(guiGraphics0, int1, int2, int3, int4, int5, int6, int7, boolean8, float9);
            guiGraphics0.renderOutline(int3 - 2, int2 - 2, int4, 70, -12303292);
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.gridLayout.m_264152_(int3, int2);
            this.updateEntryWidth(int4 - 4);
            this.children.forEach(p_280688_ -> p_280688_.render(guiGraphics0, int6, int7, float9));
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            if (this.dismissButton != null) {
                this.dismissButton.m_6375_(double0, double1, int2);
            }
            return true;
        }

        @Override
        public Component getNarration() {
            return this.text;
        }
    }

    class PendingInvitesButton extends ImageButton {

        private static final Component TITLE = Component.translatable("mco.invites.title");

        private static final Tooltip NO_PENDING_INVITES = Tooltip.create(Component.translatable("mco.invites.nopending"));

        private static final Tooltip PENDING_INVITES = Tooltip.create(Component.translatable("mco.invites.pending"));

        private static final int WIDTH = 18;

        private static final int HEIGHT = 15;

        private static final int X_OFFSET = 10;

        private static final int INVITES_WIDTH = 8;

        private static final int INVITES_HEIGHT = 8;

        private static final int INVITES_OFFSET = 11;

        public PendingInvitesButton() {
            super(RealmsMainScreen.this.f_96543_ / 2 + 64 + 10, 15, 18, 15, 0, 0, 15, RealmsMainScreen.INVITE_ICON_LOCATION, 18, 30, p_279110_ -> RealmsMainScreen.this.f_96541_.setScreen(new RealmsPendingInvitesScreen(RealmsMainScreen.this.lastScreen, TITLE)), TITLE);
            this.m_257544_(NO_PENDING_INVITES);
        }

        public void tick() {
            this.m_257544_(RealmsMainScreen.this.numberOfPendingInvites == 0 ? NO_PENDING_INVITES : PENDING_INVITES);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
            super.renderWidget(guiGraphics0, int1, int2, float3);
            this.drawInvitations(guiGraphics0);
        }

        private void drawInvitations(GuiGraphics guiGraphics0) {
            boolean $$1 = this.f_93623_ && RealmsMainScreen.this.numberOfPendingInvites != 0;
            if ($$1) {
                int $$2 = (Math.min(RealmsMainScreen.this.numberOfPendingInvites, 6) - 1) * 8;
                int $$3 = (int) (Math.max(0.0F, Math.max(Mth.sin((float) (10 + RealmsMainScreen.this.animTick) * 0.57F), Mth.cos((float) RealmsMainScreen.this.animTick * 0.35F))) * -6.0F);
                float $$4 = this.m_198029_() ? 8.0F : 0.0F;
                guiGraphics0.blit(RealmsMainScreen.INVITATION_ICONS_LOCATION, this.m_252754_() + 11, this.m_252907_() + $$3, (float) $$2, $$4, 8, 8, 48, 16);
            }
        }
    }

    class RealmSelectionList extends RealmsObjectSelectionList<RealmsMainScreen.Entry> {

        public RealmSelectionList() {
            super(RealmsMainScreen.this.f_96543_, RealmsMainScreen.this.f_96544_, 44, RealmsMainScreen.this.f_96544_ - 64, 36);
        }

        public void setSelected(@Nullable RealmsMainScreen.Entry realmsMainScreenEntry0) {
            super.m_6987_(realmsMainScreenEntry0);
            if (realmsMainScreenEntry0 != null) {
                RealmsMainScreen.this.updateButtonStates(realmsMainScreenEntry0.getServer());
            } else {
                RealmsMainScreen.this.updateButtonStates(null);
            }
        }

        @Override
        public int getMaxPosition() {
            return this.m_5773_() * 36;
        }

        @Override
        public int getRowWidth() {
            return 300;
        }
    }

    interface RealmsCall<T> {

        T request(RealmsClient var1) throws RealmsServiceException;
    }

    class ServerEntry extends RealmsMainScreen.Entry {

        private static final int SKIN_HEAD_LARGE_WIDTH = 36;

        private final RealmsServer serverData;

        public ServerEntry(RealmsServer realmsServer0) {
            this.serverData = realmsServer0;
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.renderMcoServerItem(this.serverData, guiGraphics0, int3, int2, int6, int7);
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
                RealmsMainScreen.this.f_96541_.setScreen(new RealmsCreateRealmScreen(this.serverData, RealmsMainScreen.this));
            } else if (RealmsMainScreen.this.shouldPlayButtonBeActive(this.serverData)) {
                if (Util.getMillis() - RealmsMainScreen.this.lastClickTime < 250L && this.m_93696_()) {
                    RealmsMainScreen.this.f_96541_.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    RealmsMainScreen.this.play(this.serverData, RealmsMainScreen.this);
                }
                RealmsMainScreen.this.lastClickTime = Util.getMillis();
            }
            return true;
        }

        @Override
        public boolean keyPressed(int int0, int int1, int int2) {
            if (CommonInputs.selected(int0) && RealmsMainScreen.this.shouldPlayButtonBeActive(this.serverData)) {
                RealmsMainScreen.this.f_96541_.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                RealmsMainScreen.this.play(this.serverData, RealmsMainScreen.this);
                return true;
            } else {
                return super.m_7933_(int0, int1, int2);
            }
        }

        private void renderMcoServerItem(RealmsServer realmsServer0, GuiGraphics guiGraphics1, int int2, int int3, int int4, int int5) {
            this.renderLegacy(realmsServer0, guiGraphics1, int2 + 36, int3, int4, int5);
        }

        private void renderLegacy(RealmsServer realmsServer0, GuiGraphics guiGraphics1, int int2, int int3, int int4, int int5) {
            if (realmsServer0.state == RealmsServer.State.UNINITIALIZED) {
                guiGraphics1.blit(RealmsMainScreen.WORLDICON_LOCATION, int2 + 10, int3 + 6, 0.0F, 0.0F, 40, 20, 40, 20);
                float $$6 = 0.5F + (1.0F + Mth.sin((float) RealmsMainScreen.this.animTick * 0.25F)) * 0.25F;
                int $$7 = 0xFF000000 | (int) (127.0F * $$6) << 16 | (int) (255.0F * $$6) << 8 | (int) (127.0F * $$6);
                guiGraphics1.drawCenteredString(RealmsMainScreen.this.f_96547_, RealmsMainScreen.SERVER_UNITIALIZED_TEXT, int2 + 10 + 40 + 75, int3 + 12, $$7);
            } else {
                int $$8 = 225;
                int $$9 = 2;
                this.renderStatusLights(realmsServer0, guiGraphics1, int2, int3, int4, int5, 225, 2);
                if (!"0".equals(realmsServer0.serverPing.nrOfPlayers)) {
                    String $$10 = ChatFormatting.GRAY + realmsServer0.serverPing.nrOfPlayers;
                    guiGraphics1.drawString(RealmsMainScreen.this.f_96547_, $$10, int2 + 207 - RealmsMainScreen.this.f_96547_.width($$10), int3 + 3, 8421504, false);
                    if (int4 >= int2 + 207 - RealmsMainScreen.this.f_96547_.width($$10) && int4 <= int2 + 207 && int5 >= int3 + 1 && int5 <= int3 + 10 && int5 < RealmsMainScreen.this.f_96544_ - 40 && int5 > 32 && !RealmsMainScreen.this.shouldShowPopup()) {
                        RealmsMainScreen.this.m_257404_(Component.literal(realmsServer0.serverPing.playerList));
                    }
                }
                if (RealmsMainScreen.this.isSelfOwnedServer(realmsServer0) && realmsServer0.expired) {
                    Component $$11 = realmsServer0.expiredTrial ? RealmsMainScreen.TRIAL_EXPIRED_TEXT : RealmsMainScreen.SUBSCRIPTION_EXPIRED_TEXT;
                    int $$12 = int3 + 11 + 5;
                    guiGraphics1.drawString(RealmsMainScreen.this.f_96547_, $$11, int2 + 2, $$12 + 1, 15553363, false);
                } else {
                    if (realmsServer0.worldType == RealmsServer.WorldType.MINIGAME) {
                        int $$13 = 13413468;
                        int $$14 = RealmsMainScreen.this.f_96547_.width(RealmsMainScreen.SELECT_MINIGAME_PREFIX);
                        guiGraphics1.drawString(RealmsMainScreen.this.f_96547_, RealmsMainScreen.SELECT_MINIGAME_PREFIX, int2 + 2, int3 + 12, 13413468, false);
                        guiGraphics1.drawString(RealmsMainScreen.this.f_96547_, realmsServer0.getMinigameName(), int2 + 2 + $$14, int3 + 12, 7105644, false);
                    } else {
                        guiGraphics1.drawString(RealmsMainScreen.this.f_96547_, realmsServer0.getDescription(), int2 + 2, int3 + 12, 7105644, false);
                    }
                    if (!RealmsMainScreen.this.isSelfOwnedServer(realmsServer0)) {
                        guiGraphics1.drawString(RealmsMainScreen.this.f_96547_, realmsServer0.owner, int2 + 2, int3 + 12 + 11, 5000268, false);
                    }
                }
                guiGraphics1.drawString(RealmsMainScreen.this.f_96547_, realmsServer0.getName(), int2 + 2, int3 + 1, 16777215, false);
                RealmsUtil.renderPlayerFace(guiGraphics1, int2 - 36, int3, 32, realmsServer0.ownerUUID);
            }
        }

        private void renderStatusLights(RealmsServer realmsServer0, GuiGraphics guiGraphics1, int int2, int int3, int int4, int int5, int int6, int int7) {
            int $$8 = int2 + int6 + 22;
            if (realmsServer0.expired) {
                RealmsMainScreen.this.drawExpired(guiGraphics1, $$8, int3 + int7, int4, int5);
            } else if (realmsServer0.state == RealmsServer.State.CLOSED) {
                RealmsMainScreen.this.drawClose(guiGraphics1, $$8, int3 + int7, int4, int5);
            } else if (RealmsMainScreen.this.isSelfOwnedServer(realmsServer0) && realmsServer0.daysLeft < 7) {
                RealmsMainScreen.this.drawExpiring(guiGraphics1, $$8, int3 + int7, int4, int5, realmsServer0.daysLeft);
            } else if (realmsServer0.state == RealmsServer.State.OPEN) {
                RealmsMainScreen.this.drawOpen(guiGraphics1, $$8, int3 + int7, int4, int5);
            }
        }

        @Override
        public Component getNarration() {
            return (Component) (this.serverData.state == RealmsServer.State.UNINITIALIZED ? RealmsMainScreen.UNITIALIZED_WORLD_NARRATION : Component.translatable("narrator.select", this.serverData.name));
        }

        @Nullable
        @Override
        public RealmsServer getServer() {
            return this.serverData;
        }
    }

    class TrialEntry extends RealmsMainScreen.Entry {

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.renderTrialItem(guiGraphics0, int1, int3, int2, int6, int7);
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            RealmsMainScreen.this.popupOpenedByUser = true;
            return true;
        }

        private void renderTrialItem(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5) {
            int $$6 = int3 + 8;
            int $$7 = 0;
            boolean $$8 = false;
            if (int2 <= int4 && int4 <= (int) RealmsMainScreen.this.realmSelectionList.m_93517_() && int3 <= int5 && int5 <= int3 + 32) {
                $$8 = true;
            }
            int $$9 = 8388479;
            if ($$8 && !RealmsMainScreen.this.shouldShowPopup()) {
                $$9 = 6077788;
            }
            for (Component $$10 : RealmsMainScreen.TRIAL_MESSAGE_LINES) {
                guiGraphics0.drawCenteredString(RealmsMainScreen.this.f_96547_, $$10, RealmsMainScreen.this.f_96543_ / 2, $$6 + $$7, $$9);
                $$7 += 10;
            }
        }

        @Override
        public Component getNarration() {
            return RealmsMainScreen.TRIAL_TEXT;
        }
    }
}