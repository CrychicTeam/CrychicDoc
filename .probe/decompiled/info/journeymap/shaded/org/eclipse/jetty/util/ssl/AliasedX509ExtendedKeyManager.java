package info.journeymap.shaded.org.eclipse.jetty.util.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;

public class AliasedX509ExtendedKeyManager extends X509ExtendedKeyManager {

    private final String _alias;

    private final X509ExtendedKeyManager _delegate;

    public AliasedX509ExtendedKeyManager(X509ExtendedKeyManager keyManager, String keyAlias) {
        this._alias = keyAlias;
        this._delegate = keyManager;
    }

    public X509ExtendedKeyManager getDelegate() {
        return this._delegate;
    }

    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        if (this._alias == null) {
            return this._delegate.chooseClientAlias(keyType, issuers, socket);
        } else {
            for (String kt : keyType) {
                String[] aliases = this._delegate.getClientAliases(kt, issuers);
                if (aliases != null) {
                    for (String a : aliases) {
                        if (this._alias.equals(a)) {
                            return this._alias;
                        }
                    }
                }
            }
            return null;
        }
    }

    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        if (this._alias == null) {
            return this._delegate.chooseServerAlias(keyType, issuers, socket);
        } else {
            String[] aliases = this._delegate.getServerAliases(keyType, issuers);
            if (aliases != null) {
                for (String a : aliases) {
                    if (this._alias.equals(a)) {
                        return this._alias;
                    }
                }
            }
            return null;
        }
    }

    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return this._delegate.getClientAliases(keyType, issuers);
    }

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return this._delegate.getServerAliases(keyType, issuers);
    }

    public X509Certificate[] getCertificateChain(String alias) {
        return this._delegate.getCertificateChain(alias);
    }

    public PrivateKey getPrivateKey(String alias) {
        return this._delegate.getPrivateKey(alias);
    }

    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
        if (this._alias == null) {
            return this._delegate.chooseEngineServerAlias(keyType, issuers, engine);
        } else {
            String[] aliases = this._delegate.getServerAliases(keyType, issuers);
            if (aliases != null) {
                for (String a : aliases) {
                    if (this._alias.equals(a)) {
                        return this._alias;
                    }
                }
            }
            return null;
        }
    }

    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine) {
        if (this._alias == null) {
            return this._delegate.chooseEngineClientAlias(keyType, issuers, engine);
        } else {
            for (String kt : keyType) {
                String[] aliases = this._delegate.getClientAliases(kt, issuers);
                if (aliases != null) {
                    for (String a : aliases) {
                        if (this._alias.equals(a)) {
                            return this._alias;
                        }
                    }
                }
            }
            return null;
        }
    }
}