package info.journeymap.shaded.org.eclipse.jetty.util.ssl;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.component.AbstractLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.resource.Resource;
import info.journeymap.shaded.org.eclipse.jetty.util.security.CertificateUtils;
import info.journeymap.shaded.org.eclipse.jetty.util.security.CertificateValidator;
import info.journeymap.shaded.org.eclipse.jetty.util.security.Password;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CRL;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509TrustManager;

public class SslContextFactory extends AbstractLifeCycle implements Dumpable {

    public static final TrustManager[] TRUST_ALL_CERTS = new X509TrustManager[] { new X509TrustManager() {

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };

    private static final Logger LOG = Log.getLogger(SslContextFactory.class);

    public static final String DEFAULT_KEYMANAGERFACTORY_ALGORITHM = Security.getProperty("ssl.KeyManagerFactory.algorithm") == null ? KeyManagerFactory.getDefaultAlgorithm() : Security.getProperty("ssl.KeyManagerFactory.algorithm");

    public static final String DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM = Security.getProperty("ssl.TrustManagerFactory.algorithm") == null ? TrustManagerFactory.getDefaultAlgorithm() : Security.getProperty("ssl.TrustManagerFactory.algorithm");

    public static final String KEYPASSWORD_PROPERTY = "info.journeymap.shaded.org.eclipse.jetty.ssl.keypassword";

    public static final String PASSWORD_PROPERTY = "info.journeymap.shaded.org.eclipse.jetty.ssl.password";

    private final Set<String> _excludeProtocols = new LinkedHashSet();

    private final Set<String> _includeProtocols = new LinkedHashSet();

    private final Set<String> _excludeCipherSuites = new LinkedHashSet();

    private final List<String> _includeCipherSuites = new ArrayList();

    private final Map<String, X509> _aliasX509 = new HashMap();

    private final Map<String, X509> _certHosts = new HashMap();

    private final Map<String, X509> _certWilds = new HashMap();

    private String[] _selectedProtocols;

    private boolean _useCipherSuitesOrder = true;

    private Comparator<String> _cipherComparator;

    private String[] _selectedCipherSuites;

    private Resource _keyStoreResource;

    private String _keyStoreProvider;

    private String _keyStoreType = "JKS";

    private String _certAlias;

    private Resource _trustStoreResource;

    private String _trustStoreProvider;

    private String _trustStoreType = "JKS";

    private boolean _needClientAuth = false;

    private boolean _wantClientAuth = false;

    private Password _keyStorePassword;

    private Password _keyManagerPassword;

    private Password _trustStorePassword;

    private String _sslProvider;

    private String _sslProtocol = "TLS";

    private String _secureRandomAlgorithm;

    private String _keyManagerFactoryAlgorithm = DEFAULT_KEYMANAGERFACTORY_ALGORITHM;

    private String _trustManagerFactoryAlgorithm = DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM;

    private boolean _validateCerts;

    private boolean _validatePeerCerts;

    private int _maxCertPathLength = -1;

    private String _crlPath;

    private boolean _enableCRLDP = false;

    private boolean _enableOCSP = false;

    private String _ocspResponderURL;

    private KeyStore _setKeyStore;

    private KeyStore _setTrustStore;

    private boolean _sessionCachingEnabled = true;

    private int _sslSessionCacheSize = -1;

    private int _sslSessionTimeout = -1;

    private SSLContext _setContext;

    private String _endpointIdentificationAlgorithm = null;

    private boolean _trustAll;

    private boolean _renegotiationAllowed = true;

    private int _renegotiationLimit = 5;

    private SslContextFactory.Factory _factory;

    public SslContextFactory() {
        this(false);
    }

    public SslContextFactory(boolean trustAll) {
        this(trustAll, null);
    }

    public SslContextFactory(String keyStorePath) {
        this(false, keyStorePath);
    }

    private SslContextFactory(boolean trustAll, String keyStorePath) {
        this.setTrustAll(trustAll);
        this.addExcludeProtocols("SSL", "SSLv2", "SSLv2Hello", "SSLv3");
        this.setExcludeCipherSuites("^.*_(MD5|SHA|SHA1)$");
        if (keyStorePath != null) {
            this.setKeyStorePath(keyStorePath);
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        synchronized (this) {
            this.load();
        }
    }

    private void load() throws Exception {
        SSLContext context = this._setContext;
        KeyStore keyStore = this._setKeyStore;
        KeyStore trustStore = this._setTrustStore;
        if (context == null) {
            if (keyStore == null && this._keyStoreResource == null && trustStore == null && this._trustStoreResource == null) {
                TrustManager[] trust_managers = null;
                if (this.isTrustAll()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("No keystore or trust store configured.  ACCEPTING UNTRUSTED CERTIFICATES!!!!!");
                    }
                    trust_managers = TRUST_ALL_CERTS;
                }
                String algorithm = this.getSecureRandomAlgorithm();
                SecureRandom secureRandom = algorithm == null ? null : SecureRandom.getInstance(algorithm);
                context = this._sslProvider == null ? SSLContext.getInstance(this._sslProtocol) : SSLContext.getInstance(this._sslProtocol, this._sslProvider);
                context.init(null, trust_managers, secureRandom);
            } else {
                if (keyStore == null) {
                    keyStore = this.loadKeyStore(this._keyStoreResource);
                }
                if (trustStore == null) {
                    trustStore = this.loadTrustStore(this._trustStoreResource);
                }
                Collection<? extends CRL> crls = this.loadCRL(this.getCrlPath());
                if (keyStore != null) {
                    for (String alias : Collections.list(keyStore.aliases())) {
                        Certificate certificate = keyStore.getCertificate(alias);
                        if (certificate != null && "X.509".equals(certificate.getType())) {
                            X509Certificate x509C = (X509Certificate) certificate;
                            if (!X509.isCertSign(x509C)) {
                                X509 x509 = new X509(alias, x509C);
                                this._aliasX509.put(alias, x509);
                                if (this.isValidateCerts()) {
                                    CertificateValidator validator = new CertificateValidator(trustStore, crls);
                                    validator.setMaxCertPathLength(this.getMaxCertPathLength());
                                    validator.setEnableCRLDP(this.isEnableCRLDP());
                                    validator.setEnableOCSP(this.isEnableOCSP());
                                    validator.setOcspResponderURL(this.getOcspResponderURL());
                                    validator.validate(keyStore, x509C);
                                }
                                LOG.info("x509={} for {}", x509, this);
                                for (String h : x509.getHosts()) {
                                    this._certHosts.put(h, x509);
                                }
                                for (String w : x509.getWilds()) {
                                    this._certWilds.put(w, x509);
                                }
                            } else if (LOG.isDebugEnabled()) {
                                LOG.debug("Skipping " + x509C);
                            }
                        }
                    }
                }
                KeyManager[] keyManagers = this.getKeyManagers(keyStore);
                TrustManager[] trustManagers = this.getTrustManagers(trustStore, crls);
                SecureRandom secureRandom = this._secureRandomAlgorithm == null ? null : SecureRandom.getInstance(this._secureRandomAlgorithm);
                context = this._sslProvider == null ? SSLContext.getInstance(this._sslProtocol) : SSLContext.getInstance(this._sslProtocol, this._sslProvider);
                context.init(keyManagers, trustManagers, secureRandom);
            }
        }
        SSLSessionContext serverContext = context.getServerSessionContext();
        if (serverContext != null) {
            if (this.getSslSessionCacheSize() > -1) {
                serverContext.setSessionCacheSize(this.getSslSessionCacheSize());
            }
            if (this.getSslSessionTimeout() > -1) {
                serverContext.setSessionTimeout(this.getSslSessionTimeout());
            }
        }
        SSLParameters enabled = context.getDefaultSSLParameters();
        SSLParameters supported = context.getSupportedSSLParameters();
        this.selectCipherSuites(enabled.getCipherSuites(), supported.getCipherSuites());
        this.selectProtocols(enabled.getProtocols(), supported.getProtocols());
        this._factory = new SslContextFactory.Factory(keyStore, trustStore, context);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Selected Protocols {} of {}", Arrays.asList(this._selectedProtocols), Arrays.asList(supported.getProtocols()));
            LOG.debug("Selected Ciphers   {} of {}", Arrays.asList(this._selectedCipherSuites), Arrays.asList(supported.getCipherSuites()));
        }
    }

    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        out.append(String.valueOf(this)).append(" trustAll=").append(Boolean.toString(this._trustAll)).append(System.lineSeparator());
        try {
            SSLEngine sslEngine = SSLContext.getDefault().createSSLEngine();
            List<Object> selections = new ArrayList();
            selections.add(new SslSelectionDump("Protocol", sslEngine.getSupportedProtocols(), sslEngine.getEnabledProtocols(), this.getExcludeProtocols(), this.getIncludeProtocols()));
            selections.add(new SslSelectionDump("Cipher Suite", sslEngine.getSupportedCipherSuites(), sslEngine.getEnabledCipherSuites(), this.getExcludeCipherSuites(), this.getIncludeCipherSuites()));
            ContainerLifeCycle.dump(out, indent, selections);
        } catch (NoSuchAlgorithmException var5) {
            LOG.ignore(var5);
        }
    }

    @Override
    protected void doStop() throws Exception {
        synchronized (this) {
            this.unload();
        }
        super.doStop();
    }

    private void unload() {
        this._factory = null;
        this._selectedProtocols = null;
        this._selectedCipherSuites = null;
        this._aliasX509.clear();
        this._certHosts.clear();
        this._certWilds.clear();
    }

    public String[] getSelectedProtocols() {
        return (String[]) Arrays.copyOf(this._selectedProtocols, this._selectedProtocols.length);
    }

    public String[] getSelectedCipherSuites() {
        return (String[]) Arrays.copyOf(this._selectedCipherSuites, this._selectedCipherSuites.length);
    }

    public Comparator<String> getCipherComparator() {
        return this._cipherComparator;
    }

    public void setCipherComparator(Comparator<String> cipherComparator) {
        if (cipherComparator != null) {
            this.setUseCipherSuitesOrder(true);
        }
        this._cipherComparator = cipherComparator;
    }

    public Set<String> getAliases() {
        return Collections.unmodifiableSet(this._aliasX509.keySet());
    }

    public X509 getX509(String alias) {
        return (X509) this._aliasX509.get(alias);
    }

    public String[] getExcludeProtocols() {
        return (String[]) this._excludeProtocols.toArray(new String[0]);
    }

    public void setExcludeProtocols(String... protocols) {
        this._excludeProtocols.clear();
        this._excludeProtocols.addAll(Arrays.asList(protocols));
    }

    public void addExcludeProtocols(String... protocol) {
        this._excludeProtocols.addAll(Arrays.asList(protocol));
    }

    public String[] getIncludeProtocols() {
        return (String[]) this._includeProtocols.toArray(new String[0]);
    }

    public void setIncludeProtocols(String... protocols) {
        this._includeProtocols.clear();
        this._includeProtocols.addAll(Arrays.asList(protocols));
    }

    public String[] getExcludeCipherSuites() {
        return (String[]) this._excludeCipherSuites.toArray(new String[0]);
    }

    public void setExcludeCipherSuites(String... cipherSuites) {
        this._excludeCipherSuites.clear();
        this._excludeCipherSuites.addAll(Arrays.asList(cipherSuites));
    }

    public void addExcludeCipherSuites(String... cipher) {
        this._excludeCipherSuites.addAll(Arrays.asList(cipher));
    }

    public String[] getIncludeCipherSuites() {
        return (String[]) this._includeCipherSuites.toArray(new String[0]);
    }

    public void setIncludeCipherSuites(String... cipherSuites) {
        this._includeCipherSuites.clear();
        this._includeCipherSuites.addAll(Arrays.asList(cipherSuites));
    }

    public boolean isUseCipherSuitesOrder() {
        return this._useCipherSuitesOrder;
    }

    public void setUseCipherSuitesOrder(boolean useCipherSuitesOrder) {
        this._useCipherSuitesOrder = useCipherSuitesOrder;
    }

    public String getKeyStorePath() {
        return this._keyStoreResource.toString();
    }

    public void setKeyStorePath(String keyStorePath) {
        try {
            this._keyStoreResource = Resource.newResource(keyStorePath);
        } catch (Exception var3) {
            throw new IllegalArgumentException(var3);
        }
    }

    public String getKeyStoreProvider() {
        return this._keyStoreProvider;
    }

    public void setKeyStoreProvider(String keyStoreProvider) {
        this._keyStoreProvider = keyStoreProvider;
    }

    public String getKeyStoreType() {
        return this._keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this._keyStoreType = keyStoreType;
    }

    public String getCertAlias() {
        return this._certAlias;
    }

    public void setCertAlias(String certAlias) {
        this._certAlias = certAlias;
    }

    public void setTrustStorePath(String trustStorePath) {
        try {
            this._trustStoreResource = Resource.newResource(trustStorePath);
        } catch (Exception var3) {
            throw new IllegalArgumentException(var3);
        }
    }

    public String getTrustStoreProvider() {
        return this._trustStoreProvider;
    }

    public void setTrustStoreProvider(String trustStoreProvider) {
        this._trustStoreProvider = trustStoreProvider;
    }

    public String getTrustStoreType() {
        return this._trustStoreType;
    }

    public void setTrustStoreType(String trustStoreType) {
        this._trustStoreType = trustStoreType;
    }

    public boolean getNeedClientAuth() {
        return this._needClientAuth;
    }

    public void setNeedClientAuth(boolean needClientAuth) {
        this._needClientAuth = needClientAuth;
    }

    public boolean getWantClientAuth() {
        return this._wantClientAuth;
    }

    public void setWantClientAuth(boolean wantClientAuth) {
        this._wantClientAuth = wantClientAuth;
    }

    public boolean isValidateCerts() {
        return this._validateCerts;
    }

    public void setValidateCerts(boolean validateCerts) {
        this._validateCerts = validateCerts;
    }

    public boolean isValidatePeerCerts() {
        return this._validatePeerCerts;
    }

    public void setValidatePeerCerts(boolean validatePeerCerts) {
        this._validatePeerCerts = validatePeerCerts;
    }

    public void setKeyStorePassword(String password) {
        if (password == null) {
            if (this._keyStoreResource != null) {
                this._keyStorePassword = this.getPassword("info.journeymap.shaded.org.eclipse.jetty.ssl.password");
            } else {
                this._keyStorePassword = null;
            }
        } else {
            this._keyStorePassword = this.newPassword(password);
        }
    }

    public void setKeyManagerPassword(String password) {
        if (password == null) {
            if (System.getProperty("info.journeymap.shaded.org.eclipse.jetty.ssl.keypassword") != null) {
                this._keyManagerPassword = this.getPassword("info.journeymap.shaded.org.eclipse.jetty.ssl.keypassword");
            } else {
                this._keyManagerPassword = null;
            }
        } else {
            this._keyManagerPassword = this.newPassword(password);
        }
    }

    public void setTrustStorePassword(String password) {
        if (password == null) {
            if (this._trustStoreResource != null && !this._trustStoreResource.equals(this._keyStoreResource)) {
                this._trustStorePassword = this.getPassword("info.journeymap.shaded.org.eclipse.jetty.ssl.password");
            } else {
                this._trustStorePassword = null;
            }
        } else {
            this._trustStorePassword = this.newPassword(password);
        }
    }

    public String getProvider() {
        return this._sslProvider;
    }

    public void setProvider(String provider) {
        this._sslProvider = provider;
    }

    public String getProtocol() {
        return this._sslProtocol;
    }

    public void setProtocol(String protocol) {
        this._sslProtocol = protocol;
    }

    public String getSecureRandomAlgorithm() {
        return this._secureRandomAlgorithm;
    }

    public void setSecureRandomAlgorithm(String algorithm) {
        this._secureRandomAlgorithm = algorithm;
    }

    public String getKeyManagerFactoryAlgorithm() {
        return this._keyManagerFactoryAlgorithm;
    }

    public void setKeyManagerFactoryAlgorithm(String algorithm) {
        this._keyManagerFactoryAlgorithm = algorithm;
    }

    public String getTrustManagerFactoryAlgorithm() {
        return this._trustManagerFactoryAlgorithm;
    }

    public boolean isTrustAll() {
        return this._trustAll;
    }

    public void setTrustAll(boolean trustAll) {
        this._trustAll = trustAll;
        if (trustAll) {
            this.setEndpointIdentificationAlgorithm(null);
        }
    }

    public void setTrustManagerFactoryAlgorithm(String algorithm) {
        this._trustManagerFactoryAlgorithm = algorithm;
    }

    public boolean isRenegotiationAllowed() {
        return this._renegotiationAllowed;
    }

    public void setRenegotiationAllowed(boolean renegotiationAllowed) {
        this._renegotiationAllowed = renegotiationAllowed;
    }

    public int getRenegotiationLimit() {
        return this._renegotiationLimit;
    }

    public void setRenegotiationLimit(int renegotiationLimit) {
        this._renegotiationLimit = renegotiationLimit;
    }

    public String getCrlPath() {
        return this._crlPath;
    }

    public void setCrlPath(String crlPath) {
        this._crlPath = crlPath;
    }

    public int getMaxCertPathLength() {
        return this._maxCertPathLength;
    }

    public void setMaxCertPathLength(int maxCertPathLength) {
        this._maxCertPathLength = maxCertPathLength;
    }

    public SSLContext getSslContext() {
        if (!this.isStarted()) {
            return this._setContext;
        } else {
            synchronized (this) {
                return this._factory._context;
            }
        }
    }

    public void setSslContext(SSLContext sslContext) {
        this._setContext = sslContext;
    }

    public String getEndpointIdentificationAlgorithm() {
        return this._endpointIdentificationAlgorithm;
    }

    public void setEndpointIdentificationAlgorithm(String endpointIdentificationAlgorithm) {
        this._endpointIdentificationAlgorithm = endpointIdentificationAlgorithm;
    }

    protected KeyStore loadKeyStore(Resource resource) throws Exception {
        String storePassword = this._keyStorePassword == null ? null : this._keyStorePassword.toString();
        return CertificateUtils.getKeyStore(resource, this.getKeyStoreType(), this.getKeyStoreProvider(), storePassword);
    }

    protected KeyStore loadTrustStore(Resource resource) throws Exception {
        String type = this.getTrustStoreType();
        String provider = this.getTrustStoreProvider();
        String passwd = this._trustStorePassword == null ? null : this._trustStorePassword.toString();
        if (resource == null || resource.equals(this._keyStoreResource)) {
            resource = this._keyStoreResource;
            if (type == null) {
                type = this._keyStoreType;
            }
            if (provider == null) {
                provider = this._keyStoreProvider;
            }
            if (passwd == null) {
                passwd = this._keyStorePassword == null ? null : this._keyStorePassword.toString();
            }
        }
        return CertificateUtils.getKeyStore(resource, type, provider, passwd);
    }

    protected Collection<? extends CRL> loadCRL(String crlPath) throws Exception {
        return CertificateUtils.loadCRL(crlPath);
    }

    protected KeyManager[] getKeyManagers(KeyStore keyStore) throws Exception {
        KeyManager[] managers = null;
        if (keyStore != null) {
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(this.getKeyManagerFactoryAlgorithm());
            keyManagerFactory.init(keyStore, this._keyManagerPassword == null ? (this._keyStorePassword == null ? null : this._keyStorePassword.toString().toCharArray()) : this._keyManagerPassword.toString().toCharArray());
            managers = keyManagerFactory.getKeyManagers();
            if (managers != null) {
                String alias = this.getCertAlias();
                if (alias != null) {
                    for (int idx = 0; idx < managers.length; idx++) {
                        if (managers[idx] instanceof X509ExtendedKeyManager) {
                            managers[idx] = new AliasedX509ExtendedKeyManager((X509ExtendedKeyManager) managers[idx], alias);
                        }
                    }
                }
                if (!this._certHosts.isEmpty() || !this._certWilds.isEmpty()) {
                    for (int idxx = 0; idxx < managers.length; idxx++) {
                        if (managers[idxx] instanceof X509ExtendedKeyManager) {
                            managers[idxx] = new SniX509ExtendedKeyManager((X509ExtendedKeyManager) managers[idxx]);
                        }
                    }
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("managers={} for {}", managers, this);
        }
        return managers;
    }

    protected TrustManager[] getTrustManagers(KeyStore trustStore, Collection<? extends CRL> crls) throws Exception {
        TrustManager[] managers = null;
        if (trustStore != null) {
            if (this.isValidatePeerCerts() && "PKIX".equalsIgnoreCase(this.getTrustManagerFactoryAlgorithm())) {
                PKIXBuilderParameters pbParams = new PKIXBuilderParameters(trustStore, new X509CertSelector());
                pbParams.setMaxPathLength(this._maxCertPathLength);
                pbParams.setRevocationEnabled(true);
                if (crls != null && !crls.isEmpty()) {
                    pbParams.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters(crls)));
                }
                if (this._enableCRLDP) {
                    System.setProperty("com.sun.security.enableCRLDP", "true");
                }
                if (this._enableOCSP) {
                    Security.setProperty("ocsp.enable", "true");
                    if (this._ocspResponderURL != null) {
                        Security.setProperty("ocsp.responderURL", this._ocspResponderURL);
                    }
                }
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(this._trustManagerFactoryAlgorithm);
                trustManagerFactory.init(new CertPathTrustManagerParameters(pbParams));
                managers = trustManagerFactory.getTrustManagers();
            } else {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(this._trustManagerFactoryAlgorithm);
                trustManagerFactory.init(trustStore);
                managers = trustManagerFactory.getTrustManagers();
            }
        }
        return managers;
    }

    public void selectProtocols(String[] enabledProtocols, String[] supportedProtocols) {
        Set<String> selected_protocols = new LinkedHashSet();
        if (!this._includeProtocols.isEmpty()) {
            for (String protocol : this._includeProtocols) {
                if (Arrays.asList(supportedProtocols).contains(protocol)) {
                    selected_protocols.add(protocol);
                } else {
                    LOG.info("Protocol {} not supported in {}", protocol, Arrays.asList(supportedProtocols));
                }
            }
        } else {
            selected_protocols.addAll(Arrays.asList(enabledProtocols));
        }
        selected_protocols.removeAll(this._excludeProtocols);
        if (selected_protocols.isEmpty()) {
            LOG.warn("No selected protocols from {}", Arrays.asList(supportedProtocols));
        }
        this._selectedProtocols = (String[]) selected_protocols.toArray(new String[0]);
    }

    protected void selectCipherSuites(String[] enabledCipherSuites, String[] supportedCipherSuites) {
        List<String> selected_ciphers = new ArrayList();
        if (this._includeCipherSuites.isEmpty()) {
            selected_ciphers.addAll(Arrays.asList(enabledCipherSuites));
        } else {
            this.processIncludeCipherSuites(supportedCipherSuites, selected_ciphers);
        }
        this.removeExcludedCipherSuites(selected_ciphers);
        if (selected_ciphers.isEmpty()) {
            LOG.warn("No supported ciphers from {}", Arrays.asList(supportedCipherSuites));
        }
        Comparator<String> comparator = this.getCipherComparator();
        if (comparator != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sorting selected ciphers with {}", comparator);
            }
            Collections.sort(selected_ciphers, comparator);
        }
        this._selectedCipherSuites = (String[]) selected_ciphers.toArray(new String[0]);
    }

    protected void processIncludeCipherSuites(String[] supportedCipherSuites, List<String> selected_ciphers) {
        for (String cipherSuite : this._includeCipherSuites) {
            Pattern p = Pattern.compile(cipherSuite);
            boolean added = false;
            for (String supportedCipherSuite : supportedCipherSuites) {
                Matcher m = p.matcher(supportedCipherSuite);
                if (m.matches()) {
                    added = true;
                    selected_ciphers.add(supportedCipherSuite);
                }
            }
            if (!added) {
                LOG.info("No Cipher matching '{}' is supported", cipherSuite);
            }
        }
    }

    protected void removeExcludedCipherSuites(List<String> selected_ciphers) {
        for (String excludeCipherSuite : this._excludeCipherSuites) {
            Pattern excludeCipherPattern = Pattern.compile(excludeCipherSuite);
            Iterator<String> i = selected_ciphers.iterator();
            while (i.hasNext()) {
                String selectedCipherSuite = (String) i.next();
                Matcher m = excludeCipherPattern.matcher(selectedCipherSuite);
                if (m.matches()) {
                    i.remove();
                }
            }
        }
    }

    private void checkIsStarted() {
        if (!this.isStarted()) {
            throw new IllegalStateException("!STARTED: " + this);
        }
    }

    public boolean isEnableCRLDP() {
        return this._enableCRLDP;
    }

    public void setEnableCRLDP(boolean enableCRLDP) {
        this._enableCRLDP = enableCRLDP;
    }

    public boolean isEnableOCSP() {
        return this._enableOCSP;
    }

    public void setEnableOCSP(boolean enableOCSP) {
        this._enableOCSP = enableOCSP;
    }

    public String getOcspResponderURL() {
        return this._ocspResponderURL;
    }

    public void setOcspResponderURL(String ocspResponderURL) {
        this._ocspResponderURL = ocspResponderURL;
    }

    public void setKeyStore(KeyStore keyStore) {
        this._setKeyStore = keyStore;
    }

    public KeyStore getKeyStore() {
        if (!this.isStarted()) {
            return this._setKeyStore;
        } else {
            synchronized (this) {
                return this._factory._keyStore;
            }
        }
    }

    public void setTrustStore(KeyStore trustStore) {
        this._setTrustStore = trustStore;
    }

    public KeyStore getTrustStore() {
        if (!this.isStarted()) {
            return this._setTrustStore;
        } else {
            synchronized (this) {
                return this._factory._trustStore;
            }
        }
    }

    public void setKeyStoreResource(Resource resource) {
        this._keyStoreResource = resource;
    }

    public Resource getKeyStoreResource() {
        return this._keyStoreResource;
    }

    public void setTrustStoreResource(Resource resource) {
        this._trustStoreResource = resource;
    }

    public Resource getTrustStoreResource() {
        return this._trustStoreResource;
    }

    public boolean isSessionCachingEnabled() {
        return this._sessionCachingEnabled;
    }

    public void setSessionCachingEnabled(boolean enableSessionCaching) {
        this._sessionCachingEnabled = enableSessionCaching;
    }

    public int getSslSessionCacheSize() {
        return this._sslSessionCacheSize;
    }

    public void setSslSessionCacheSize(int sslSessionCacheSize) {
        this._sslSessionCacheSize = sslSessionCacheSize;
    }

    public int getSslSessionTimeout() {
        return this._sslSessionTimeout;
    }

    public void setSslSessionTimeout(int sslSessionTimeout) {
        this._sslSessionTimeout = sslSessionTimeout;
    }

    protected Password getPassword(String realm) {
        return Password.getPassword(realm, null, null);
    }

    public Password newPassword(String password) {
        return new Password(password);
    }

    public SSLServerSocket newSslServerSocket(String host, int port, int backlog) throws IOException {
        this.checkIsStarted();
        SSLContext context = this.getSslContext();
        SSLServerSocketFactory factory = context.getServerSocketFactory();
        SSLServerSocket socket = (SSLServerSocket) (host == null ? factory.createServerSocket(port, backlog) : factory.createServerSocket(port, backlog, InetAddress.getByName(host)));
        socket.setSSLParameters(this.customize(socket.getSSLParameters()));
        return socket;
    }

    public SSLSocket newSslSocket() throws IOException {
        this.checkIsStarted();
        SSLContext context = this.getSslContext();
        SSLSocketFactory factory = context.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket();
        socket.setSSLParameters(this.customize(socket.getSSLParameters()));
        return socket;
    }

    public SSLEngine newSSLEngine() {
        this.checkIsStarted();
        SSLContext context = this.getSslContext();
        SSLEngine sslEngine = context.createSSLEngine();
        this.customize(sslEngine);
        return sslEngine;
    }

    public SSLEngine newSSLEngine(String host, int port) {
        this.checkIsStarted();
        SSLContext context = this.getSslContext();
        SSLEngine sslEngine = this.isSessionCachingEnabled() ? context.createSSLEngine(host, port) : context.createSSLEngine();
        this.customize(sslEngine);
        return sslEngine;
    }

    public SSLEngine newSSLEngine(InetSocketAddress address) {
        if (address == null) {
            return this.newSSLEngine();
        } else {
            boolean useHostName = this.getNeedClientAuth();
            String hostName = useHostName ? address.getHostName() : address.getAddress().getHostAddress();
            return this.newSSLEngine(hostName, address.getPort());
        }
    }

    public void customize(SSLEngine sslEngine) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Customize {}", sslEngine);
        }
        sslEngine.setSSLParameters(this.customize(sslEngine.getSSLParameters()));
    }

    public SSLParameters customize(SSLParameters sslParams) {
        sslParams.setEndpointIdentificationAlgorithm(this.getEndpointIdentificationAlgorithm());
        sslParams.setUseCipherSuitesOrder(this.isUseCipherSuitesOrder());
        if (!this._certHosts.isEmpty() || !this._certWilds.isEmpty()) {
            sslParams.setSNIMatchers(Collections.singletonList(new SslContextFactory.AliasSNIMatcher()));
        }
        if (this._selectedCipherSuites != null) {
            sslParams.setCipherSuites(this._selectedCipherSuites);
        }
        if (this._selectedProtocols != null) {
            sslParams.setProtocols(this._selectedProtocols);
        }
        if (this.getWantClientAuth()) {
            sslParams.setWantClientAuth(true);
        }
        if (this.getNeedClientAuth()) {
            sslParams.setNeedClientAuth(true);
        }
        return sslParams;
    }

    public void reload(Consumer<SslContextFactory> consumer) throws Exception {
        synchronized (this) {
            consumer.accept(this);
            this.unload();
            this.load();
        }
    }

    public static X509Certificate[] getCertChain(SSLSession sslSession) {
        try {
            Certificate[] javaxCerts = sslSession.getPeerCertificates();
            if (javaxCerts != null && javaxCerts.length != 0) {
                int length = javaxCerts.length;
                X509Certificate[] javaCerts = new X509Certificate[length];
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                for (int i = 0; i < length; i++) {
                    byte[] bytes = javaxCerts[i].getEncoded();
                    ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                    javaCerts[i] = (X509Certificate) cf.generateCertificate(stream);
                }
                return javaCerts;
            } else {
                return null;
            }
        } catch (SSLPeerUnverifiedException var8) {
            return null;
        } catch (Exception var9) {
            LOG.warn("EXCEPTION ", var9);
            return null;
        }
    }

    public static int deduceKeyLength(String cipherSuite) {
        if (cipherSuite == null) {
            return 0;
        } else if (cipherSuite.contains("WITH_AES_256_")) {
            return 256;
        } else if (cipherSuite.contains("WITH_RC4_128_")) {
            return 128;
        } else if (cipherSuite.contains("WITH_AES_128_")) {
            return 128;
        } else if (cipherSuite.contains("WITH_RC4_40_")) {
            return 40;
        } else if (cipherSuite.contains("WITH_3DES_EDE_CBC_")) {
            return 168;
        } else if (cipherSuite.contains("WITH_IDEA_CBC_")) {
            return 128;
        } else if (cipherSuite.contains("WITH_RC2_CBC_40_")) {
            return 40;
        } else if (cipherSuite.contains("WITH_DES40_CBC_")) {
            return 40;
        } else {
            return cipherSuite.contains("WITH_DES_CBC_") ? 56 : 0;
        }
    }

    public String toString() {
        return String.format("%s@%x(%s,%s)", this.getClass().getSimpleName(), this.hashCode(), this._keyStoreResource, this._trustStoreResource);
    }

    class AliasSNIMatcher extends SNIMatcher {

        private String _host;

        private X509 _x509;

        AliasSNIMatcher() {
            super(0);
        }

        public boolean matches(SNIServerName serverName) {
            if (SslContextFactory.LOG.isDebugEnabled()) {
                SslContextFactory.LOG.debug("SNI matching for {}", serverName);
            }
            if (serverName instanceof SNIHostName) {
                String host = this._host = ((SNIHostName) serverName).getAsciiName();
                host = StringUtil.asciiToLowerCase(host);
                this._x509 = (X509) SslContextFactory.this._certHosts.get(host);
                if (this._x509 == null) {
                    this._x509 = (X509) SslContextFactory.this._certWilds.get(host);
                    if (this._x509 == null) {
                        int dot = host.indexOf(46);
                        if (dot >= 0) {
                            String domain = host.substring(dot + 1);
                            this._x509 = (X509) SslContextFactory.this._certWilds.get(domain);
                        }
                    }
                }
                if (SslContextFactory.LOG.isDebugEnabled()) {
                    SslContextFactory.LOG.debug("SNI matched {}->{}", host, this._x509);
                }
            } else if (SslContextFactory.LOG.isDebugEnabled()) {
                SslContextFactory.LOG.debug("SNI no match for {}", serverName);
            }
            return true;
        }

        public String getHost() {
            return this._host;
        }

        public X509 getX509() {
            return this._x509;
        }
    }

    class Factory {

        private final KeyStore _keyStore;

        private final KeyStore _trustStore;

        private final SSLContext _context;

        Factory(KeyStore keyStore, KeyStore trustStore, SSLContext context) {
            this._keyStore = keyStore;
            this._trustStore = trustStore;
            this._context = context;
        }
    }
}