package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.report.AbuseReport;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
import com.mojang.datafixers.util.Unit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ThrowingComponent;

public interface AbuseReportSender {

    static AbuseReportSender create(ReportEnvironment reportEnvironment0, UserApiService userApiService1) {
        return new AbuseReportSender.Services(reportEnvironment0, userApiService1);
    }

    CompletableFuture<Unit> send(UUID var1, AbuseReport var2);

    boolean isEnabled();

    default AbuseReportLimits reportLimits() {
        return AbuseReportLimits.DEFAULTS;
    }

    public static class SendException extends ThrowingComponent {

        public SendException(Component component0, Throwable throwable1) {
            super(component0, throwable1);
        }
    }

    public static record Services(ReportEnvironment f_238713_, UserApiService f_238677_) implements AbuseReportSender {

        private final ReportEnvironment environment;

        private final UserApiService userApiService;

        private static final Component SERVICE_UNAVAILABLE_TEXT = Component.translatable("gui.abuseReport.send.service_unavailable");

        private static final Component HTTP_ERROR_TEXT = Component.translatable("gui.abuseReport.send.http_error");

        private static final Component JSON_ERROR_TEXT = Component.translatable("gui.abuseReport.send.json_error");

        public Services(ReportEnvironment f_238713_, UserApiService f_238677_) {
            this.environment = f_238713_;
            this.userApiService = f_238677_;
        }

        @Override
        public CompletableFuture<Unit> send(UUID p_239470_, AbuseReport p_239471_) {
            return CompletableFuture.supplyAsync(() -> {
                AbuseReportRequest $$2 = new AbuseReportRequest(1, p_239470_, p_239471_, this.environment.clientInfo(), this.environment.thirdPartyServerInfo(), this.environment.realmInfo());
                try {
                    this.userApiService.reportAbuse($$2);
                    return Unit.INSTANCE;
                } catch (MinecraftClientHttpException var6) {
                    Component $$4 = this.getHttpErrorDescription(var6);
                    throw new CompletionException(new AbuseReportSender.SendException($$4, var6));
                } catch (MinecraftClientException var7) {
                    Component $$6 = this.getErrorDescription(var7);
                    throw new CompletionException(new AbuseReportSender.SendException($$6, var7));
                }
            }, Util.ioPool());
        }

        @Override
        public boolean isEnabled() {
            return this.userApiService.canSendReports();
        }

        private Component getHttpErrorDescription(MinecraftClientHttpException p_239705_) {
            return Component.translatable("gui.abuseReport.send.error_message", p_239705_.getMessage());
        }

        private Component getErrorDescription(MinecraftClientException p_240068_) {
            return switch(p_240068_.getType()) {
                case SERVICE_UNAVAILABLE ->
                    SERVICE_UNAVAILABLE_TEXT;
                case HTTP_ERROR ->
                    HTTP_ERROR_TEXT;
                case JSON_ERROR ->
                    JSON_ERROR_TEXT;
                default ->
                    throw new IncompatibleClassChangeError();
            };
        }

        @Override
        public AbuseReportLimits reportLimits() {
            return this.userApiService.getAbuseReportLimits();
        }
    }
}