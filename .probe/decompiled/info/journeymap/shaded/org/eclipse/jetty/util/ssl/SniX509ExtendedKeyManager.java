package info.journeymap.shaded.org.eclipse.jetty.util.ssl;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedKeyManager;

public class SniX509ExtendedKeyManager extends X509ExtendedKeyManager {

    public static final String SNI_X509 = "info.journeymap.shaded.org.eclipse.jetty.util.ssl.snix509";

    private static final String NO_MATCHERS = "no_matchers";

    private static final Logger LOG = Log.getLogger(SniX509ExtendedKeyManager.class);

    private final X509ExtendedKeyManager _delegate;

    public SniX509ExtendedKeyManager(X509ExtendedKeyManager keyManager) {
        this._delegate = keyManager;
    }

    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return this._delegate.chooseClientAlias(keyType, issuers, socket);
    }

    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine) {
        return this._delegate.chooseEngineClientAlias(keyType, issuers, engine);
    }

    protected String chooseServerAlias(String keyType, Principal[] issuers, Collection<SNIMatcher> matchers, SSLSession session) {
        String[] aliases = this._delegate.getServerAliases(keyType, issuers);
        if (aliases != null && aliases.length != 0) {
            String host = null;
            X509 x509 = null;
            if (matchers != null) {
                for (SNIMatcher m : matchers) {
                    if (m instanceof SslContextFactory.AliasSNIMatcher) {
                        SslContextFactory.AliasSNIMatcher matcher = (SslContextFactory.AliasSNIMatcher) m;
                        host = matcher.getHost();
                        x509 = matcher.getX509();
                        break;
                    }
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Matched {} with {} from {}", host, x509, Arrays.asList(aliases));
            }
            if (x509 != null) {
                for (String a : aliases) {
                    if (a.equals(x509.getAlias())) {
                        session.putValue("info.journeymap.shaded.org.eclipse.jetty.util.ssl.snix509", x509);
                        return a;
                    }
                }
                return null;
            } else {
                return "no_matchers";
            }
        } else {
            return null;
        }
    }

    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        SSLSocket sslSocket = (SSLSocket) socket;
        String alias = this.chooseServerAlias(keyType, issuers, sslSocket.getSSLParameters().getSNIMatchers(), sslSocket.getHandshakeSession());
        if (alias == "no_matchers") {
            alias = this._delegate.chooseServerAlias(keyType, issuers, socket);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Chose alias {}/{} on {}", alias, keyType, socket);
        }
        return alias;
    }

    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
        String alias = this.chooseServerAlias(keyType, issuers, engine.getSSLParameters().getSNIMatchers(), engine.getHandshakeSession());
        if (alias == "no_matchers") {
            alias = this._delegate.chooseEngineServerAlias(keyType, issuers, engine);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Chose alias {}/{} on {}", alias, keyType, engine);
        }
        return alias;
    }

    public X509Certificate[] getCertificateChain(String alias) {
        return this._delegate.getCertificateChain(alias);
    }

    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return this._delegate.getClientAliases(keyType, issuers);
    }

    public PrivateKey getPrivateKey(String alias) {
        return this._delegate.getPrivateKey(alias);
    }

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return this._delegate.getServerAliases(keyType, issuers);
    }
}