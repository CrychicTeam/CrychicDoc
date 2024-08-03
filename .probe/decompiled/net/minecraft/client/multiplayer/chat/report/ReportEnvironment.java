package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.yggdrasil.request.AbuseReportRequest.ClientInfo;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest.RealmInfo;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest.ThirdPartyServerInfo;
import com.mojang.realmsclient.dto.RealmsServer;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

public record ReportEnvironment(String f_238774_, @Nullable ReportEnvironment.Server f_238655_) {

    private final String clientVersion;

    @Nullable
    private final ReportEnvironment.Server server;

    public ReportEnvironment(String f_238774_, @Nullable ReportEnvironment.Server f_238655_) {
        this.clientVersion = f_238774_;
        this.server = f_238655_;
    }

    public static ReportEnvironment local() {
        return create(null);
    }

    public static ReportEnvironment thirdParty(String p_238999_) {
        return create(new ReportEnvironment.Server.ThirdParty(p_238999_));
    }

    public static ReportEnvironment realm(RealmsServer p_239765_) {
        return create(new ReportEnvironment.Server.Realm(p_239765_));
    }

    public static ReportEnvironment create(@Nullable ReportEnvironment.Server p_239956_) {
        return new ReportEnvironment(getClientVersion(), p_239956_);
    }

    public ClientInfo clientInfo() {
        return new ClientInfo(this.clientVersion, Locale.getDefault().toLanguageTag());
    }

    @Nullable
    public ThirdPartyServerInfo thirdPartyServerInfo() {
        return this.server instanceof ReportEnvironment.Server.ThirdParty $$0 ? new ThirdPartyServerInfo($$0.ip) : null;
    }

    @Nullable
    public RealmInfo realmInfo() {
        return this.server instanceof ReportEnvironment.Server.Realm $$0 ? new RealmInfo(String.valueOf($$0.realmId()), $$0.slotId()) : null;
    }

    private static String getClientVersion() {
        StringBuilder $$0 = new StringBuilder();
        $$0.append("1.20.1");
        if (Minecraft.checkModStatus().shouldReportAsModified()) {
            $$0.append(" (modded)");
        }
        return $$0.toString();
    }

    public interface Server {

        public static record Realm(long f_238769_, int f_238670_) implements ReportEnvironment.Server {

            private final long realmId;

            private final int slotId;

            public Realm(RealmsServer p_239068_) {
                this(p_239068_.id, p_239068_.activeSlot);
            }

            public Realm(long f_238769_, int f_238670_) {
                this.realmId = f_238769_;
                this.slotId = f_238670_;
            }
        }

        public static record ThirdParty(String f_238648_) implements ReportEnvironment.Server {

            private final String ip;

            public ThirdParty(String f_238648_) {
                this.ip = f_238648_;
            }
        }
    }
}