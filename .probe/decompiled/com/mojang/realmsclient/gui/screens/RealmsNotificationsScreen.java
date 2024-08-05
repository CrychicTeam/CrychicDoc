package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsNotification;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.task.DataFetcher;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.ResourceLocation;

public class RealmsNotificationsScreen extends RealmsScreen {

    private static final ResourceLocation INVITE_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/invite_icon.png");

    private static final ResourceLocation TRIAL_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/trial_icon.png");

    private static final ResourceLocation NEWS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/news_notification_mainscreen.png");

    private static final ResourceLocation UNSEEN_NOTIFICATION_ICON_LOCATION = new ResourceLocation("minecraft", "textures/gui/unseen_notification.png");

    @Nullable
    private DataFetcher.Subscription realmsDataSubscription;

    @Nullable
    private RealmsNotificationsScreen.DataFetcherConfiguration currentConfiguration;

    private volatile int numberOfPendingInvites;

    static boolean checkedMcoAvailability;

    private static boolean trialAvailable;

    static boolean validClient;

    private static boolean hasUnreadNews;

    private static boolean hasUnseenNotifications;

    private final RealmsNotificationsScreen.DataFetcherConfiguration showAll = new RealmsNotificationsScreen.DataFetcherConfiguration() {

        @Override
        public DataFetcher.Subscription initDataFetcher(RealmsDataFetcher p_275318_) {
            DataFetcher.Subscription $$1 = p_275318_.dataFetcher.createSubscription();
            RealmsNotificationsScreen.this.addNewsAndInvitesSubscriptions(p_275318_, $$1);
            RealmsNotificationsScreen.this.addNotificationsSubscriptions(p_275318_, $$1);
            return $$1;
        }

        @Override
        public boolean showOldNotifications() {
            return true;
        }
    };

    private final RealmsNotificationsScreen.DataFetcherConfiguration onlyNotifications = new RealmsNotificationsScreen.DataFetcherConfiguration() {

        @Override
        public DataFetcher.Subscription initDataFetcher(RealmsDataFetcher p_275731_) {
            DataFetcher.Subscription $$1 = p_275731_.dataFetcher.createSubscription();
            RealmsNotificationsScreen.this.addNotificationsSubscriptions(p_275731_, $$1);
            return $$1;
        }

        @Override
        public boolean showOldNotifications() {
            return false;
        }
    };

    public RealmsNotificationsScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    public void init() {
        this.checkIfMcoEnabled();
        if (this.realmsDataSubscription != null) {
            this.realmsDataSubscription.forceUpdate();
        }
    }

    @Override
    public void added() {
        super.m_274333_();
        this.f_96541_.realmsDataFetcher().notificationsTask.reset();
    }

    @Nullable
    private RealmsNotificationsScreen.DataFetcherConfiguration getConfiguration() {
        boolean $$0 = this.inTitleScreen() && validClient;
        if (!$$0) {
            return null;
        } else {
            return this.getRealmsNotificationsEnabled() ? this.showAll : this.onlyNotifications;
        }
    }

    @Override
    public void tick() {
        RealmsNotificationsScreen.DataFetcherConfiguration $$0 = this.getConfiguration();
        if (!Objects.equals(this.currentConfiguration, $$0)) {
            this.currentConfiguration = $$0;
            if (this.currentConfiguration != null) {
                this.realmsDataSubscription = this.currentConfiguration.initDataFetcher(this.f_96541_.realmsDataFetcher());
            } else {
                this.realmsDataSubscription = null;
            }
        }
        if (this.realmsDataSubscription != null) {
            this.realmsDataSubscription.tick();
        }
    }

    private boolean getRealmsNotificationsEnabled() {
        return this.f_96541_.options.realmsNotifications().get();
    }

    private boolean inTitleScreen() {
        return this.f_96541_.screen instanceof TitleScreen;
    }

    private void checkIfMcoEnabled() {
        if (!checkedMcoAvailability) {
            checkedMcoAvailability = true;
            (new Thread("Realms Notification Availability checker #1") {

                public void run() {
                    RealmsClient $$0 = RealmsClient.create();
                    try {
                        RealmsClient.CompatibleVersionResponse $$1 = $$0.clientCompatible();
                        if ($$1 != RealmsClient.CompatibleVersionResponse.COMPATIBLE) {
                            return;
                        }
                    } catch (RealmsServiceException var3) {
                        if (var3.httpResultCode != 401) {
                            RealmsNotificationsScreen.checkedMcoAvailability = false;
                        }
                        return;
                    }
                    RealmsNotificationsScreen.validClient = true;
                }
            }).start();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (validClient) {
            this.drawIcons(guiGraphics0);
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    private void drawIcons(GuiGraphics guiGraphics0) {
        int $$1 = this.numberOfPendingInvites;
        int $$2 = 24;
        int $$3 = this.f_96544_ / 4 + 48;
        int $$4 = this.f_96543_ / 2 + 80;
        int $$5 = $$3 + 48 + 2;
        int $$6 = 0;
        if (hasUnseenNotifications) {
            guiGraphics0.blit(UNSEEN_NOTIFICATION_ICON_LOCATION, $$4 - $$6 + 5, $$5 + 3, 0.0F, 0.0F, 10, 10, 10, 10);
            $$6 += 14;
        }
        if (this.currentConfiguration != null && this.currentConfiguration.showOldNotifications()) {
            if (hasUnreadNews) {
                guiGraphics0.pose().pushPose();
                guiGraphics0.pose().scale(0.4F, 0.4F, 0.4F);
                guiGraphics0.blit(NEWS_ICON_LOCATION, (int) ((double) ($$4 + 2 - $$6) * 2.5), (int) ((double) $$5 * 2.5), 0.0F, 0.0F, 40, 40, 40, 40);
                guiGraphics0.pose().popPose();
                $$6 += 14;
            }
            if ($$1 != 0) {
                guiGraphics0.blit(INVITE_ICON_LOCATION, $$4 - $$6, $$5, 0.0F, 0.0F, 18, 15, 18, 30);
                $$6 += 16;
            }
            if (trialAvailable) {
                int $$7 = 0;
                if ((Util.getMillis() / 800L & 1L) == 1L) {
                    $$7 = 8;
                }
                guiGraphics0.blit(TRIAL_ICON_LOCATION, $$4 + 4 - $$6, $$5 + 4, 0.0F, (float) $$7, 8, 8, 8, 16);
            }
        }
    }

    void addNewsAndInvitesSubscriptions(RealmsDataFetcher realmsDataFetcher0, DataFetcher.Subscription dataFetcherSubscription1) {
        dataFetcherSubscription1.subscribe(realmsDataFetcher0.pendingInvitesTask, p_239521_ -> this.numberOfPendingInvites = p_239521_);
        dataFetcherSubscription1.subscribe(realmsDataFetcher0.trialAvailabilityTask, p_239494_ -> trialAvailable = p_239494_);
        dataFetcherSubscription1.subscribe(realmsDataFetcher0.newsTask, p_238946_ -> {
            realmsDataFetcher0.newsManager.updateUnreadNews(p_238946_);
            hasUnreadNews = realmsDataFetcher0.newsManager.hasUnreadNews();
        });
    }

    void addNotificationsSubscriptions(RealmsDataFetcher realmsDataFetcher0, DataFetcher.Subscription dataFetcherSubscription1) {
        dataFetcherSubscription1.subscribe(realmsDataFetcher0.notificationsTask, p_274637_ -> {
            hasUnseenNotifications = false;
            for (RealmsNotification $$1 : p_274637_) {
                if (!$$1.seen()) {
                    hasUnseenNotifications = true;
                    break;
                }
            }
        });
    }

    interface DataFetcherConfiguration {

        DataFetcher.Subscription initDataFetcher(RealmsDataFetcher var1);

        boolean showOldNotifications();
    }
}