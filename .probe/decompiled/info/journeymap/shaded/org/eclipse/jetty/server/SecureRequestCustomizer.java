package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.BadMessageException;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpScheme;
import info.journeymap.shaded.org.eclipse.jetty.http.PreEncodedHttpField;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.ssl.SslConnection;
import info.journeymap.shaded.org.eclipse.jetty.util.TypeUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.Name;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.ssl.SslContextFactory;
import info.journeymap.shaded.org.eclipse.jetty.util.ssl.X509;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

public class SecureRequestCustomizer implements HttpConfiguration.Customizer {

    private static final Logger LOG = Log.getLogger(SecureRequestCustomizer.class);

    public static final String CACHED_INFO_ATTR = SecureRequestCustomizer.CachedInfo.class.getName();

    private String sslSessionAttribute = "info.journeymap.shaded.org.eclipse.jetty.servlet.request.ssl_session";

    private boolean _sniHostCheck;

    private long _stsMaxAge = -1L;

    private boolean _stsIncludeSubDomains;

    private HttpField _stsField;

    public SecureRequestCustomizer() {
        this(true);
    }

    public SecureRequestCustomizer(@Name("sniHostCheck") boolean sniHostCheck) {
        this(sniHostCheck, -1L, false);
    }

    public SecureRequestCustomizer(@Name("sniHostCheck") boolean sniHostCheck, @Name("stsMaxAgeSeconds") long stsMaxAgeSeconds, @Name("stsIncludeSubdomains") boolean stsIncludeSubdomains) {
        this._sniHostCheck = sniHostCheck;
        this._stsMaxAge = stsMaxAgeSeconds;
        this._stsIncludeSubDomains = stsIncludeSubdomains;
        this.formatSTS();
    }

    public boolean isSniHostCheck() {
        return this._sniHostCheck;
    }

    public void setSniHostCheck(boolean sniHostCheck) {
        this._sniHostCheck = sniHostCheck;
    }

    public long getStsMaxAge() {
        return this._stsMaxAge;
    }

    public void setStsMaxAge(long stsMaxAgeSeconds) {
        this._stsMaxAge = stsMaxAgeSeconds;
        this.formatSTS();
    }

    public void setStsMaxAge(long period, TimeUnit units) {
        this._stsMaxAge = units.toSeconds(period);
        this.formatSTS();
    }

    public boolean isStsIncludeSubDomains() {
        return this._stsIncludeSubDomains;
    }

    public void setStsIncludeSubDomains(boolean stsIncludeSubDomains) {
        this._stsIncludeSubDomains = stsIncludeSubDomains;
        this.formatSTS();
    }

    private void formatSTS() {
        if (this._stsMaxAge < 0L) {
            this._stsField = null;
        } else {
            this._stsField = new PreEncodedHttpField(HttpHeader.STRICT_TRANSPORT_SECURITY, String.format("max-age=%d%s", this._stsMaxAge, this._stsIncludeSubDomains ? "; includeSubDomains" : ""));
        }
    }

    @Override
    public void customize(Connector connector, HttpConfiguration channelConfig, Request request) {
        EndPoint endp = request.getHttpChannel().getEndPoint();
        if (endp instanceof SslConnection.DecryptedEndPoint) {
            SslConnection.DecryptedEndPoint ssl_endp = (SslConnection.DecryptedEndPoint) endp;
            SslConnection sslConnection = ssl_endp.getSslConnection();
            SSLEngine sslEngine = sslConnection.getSSLEngine();
            this.customize(sslEngine, request);
            if (request.getHttpURI().getScheme() == null) {
                request.setScheme(HttpScheme.HTTPS.asString());
            }
        } else if (endp instanceof ProxyConnectionFactory.ProxyEndPoint) {
            ProxyConnectionFactory.ProxyEndPoint proxy = (ProxyConnectionFactory.ProxyEndPoint) endp;
            if (request.getHttpURI().getScheme() == null && proxy.getAttribute("TLS_VERSION") != null) {
                request.setScheme(HttpScheme.HTTPS.asString());
            }
        }
        if (HttpScheme.HTTPS.is(request.getScheme())) {
            this.customizeSecure(request);
        }
    }

    protected void customizeSecure(Request request) {
        request.setSecure(true);
        if (this._stsField != null) {
            request.getResponse().getHttpFields().add(this._stsField);
        }
    }

    protected void customize(SSLEngine sslEngine, Request request) {
        SSLSession sslSession = sslEngine.getSession();
        if (this._sniHostCheck) {
            String name = request.getServerName();
            X509 x509 = (X509) sslSession.getValue("info.journeymap.shaded.org.eclipse.jetty.util.ssl.snix509");
            if (x509 != null && !x509.matches(name)) {
                LOG.warn("Host {} does not match SNI {}", name, x509);
                throw new BadMessageException(400, "Host does not match SNI");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Host {} matched SNI {}", name, x509);
            }
        }
        try {
            String cipherSuite = sslSession.getCipherSuite();
            SecureRequestCustomizer.CachedInfo cachedInfo = (SecureRequestCustomizer.CachedInfo) sslSession.getValue(CACHED_INFO_ATTR);
            X509Certificate[] certs;
            String idStr;
            Integer keySize;
            if (cachedInfo != null) {
                keySize = cachedInfo.getKeySize();
                certs = cachedInfo.getCerts();
                idStr = cachedInfo.getIdStr();
            } else {
                keySize = SslContextFactory.deduceKeyLength(cipherSuite);
                certs = SslContextFactory.getCertChain(sslSession);
                byte[] bytes = sslSession.getId();
                idStr = TypeUtil.toHexString(bytes);
                cachedInfo = new SecureRequestCustomizer.CachedInfo(keySize, certs, idStr);
                sslSession.putValue(CACHED_INFO_ATTR, cachedInfo);
            }
            if (certs != null) {
                request.setAttribute("info.journeymap.shaded.org.javax.servlet.request.X509Certificate", certs);
            }
            request.setAttribute("info.journeymap.shaded.org.javax.servlet.request.cipher_suite", cipherSuite);
            request.setAttribute("info.journeymap.shaded.org.javax.servlet.request.key_size", keySize);
            request.setAttribute("info.journeymap.shaded.org.javax.servlet.request.ssl_session_id", idStr);
            String sessionAttribute = this.getSslSessionAttribute();
            if (sessionAttribute != null && !sessionAttribute.isEmpty()) {
                request.setAttribute(sessionAttribute, sslSession);
            }
        } catch (Exception var10) {
            LOG.warn("EXCEPTION ", var10);
        }
    }

    public void setSslSessionAttribute(String attribute) {
        this.sslSessionAttribute = attribute;
    }

    public String getSslSessionAttribute() {
        return this.sslSessionAttribute;
    }

    public String toString() {
        return String.format("%s@%x", this.getClass().getSimpleName(), this.hashCode());
    }

    private static class CachedInfo {

        private final X509Certificate[] _certs;

        private final Integer _keySize;

        private final String _idStr;

        CachedInfo(Integer keySize, X509Certificate[] certs, String idStr) {
            this._keySize = keySize;
            this._certs = certs;
            this._idStr = idStr;
        }

        X509Certificate[] getCerts() {
            return this._certs;
        }

        Integer getKeySize() {
            return this._keySize;
        }

        String getIdStr() {
            return this._idStr;
        }
    }
}