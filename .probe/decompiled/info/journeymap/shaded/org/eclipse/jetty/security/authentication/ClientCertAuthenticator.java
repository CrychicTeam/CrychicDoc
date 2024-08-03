package info.journeymap.shaded.org.eclipse.jetty.security.authentication;

import info.journeymap.shaded.org.eclipse.jetty.security.ServerAuthException;
import info.journeymap.shaded.org.eclipse.jetty.security.UserAuthentication;
import info.journeymap.shaded.org.eclipse.jetty.server.Authentication;
import info.journeymap.shaded.org.eclipse.jetty.server.UserIdentity;
import info.journeymap.shaded.org.eclipse.jetty.util.B64Code;
import info.journeymap.shaded.org.eclipse.jetty.util.resource.Resource;
import info.journeymap.shaded.org.eclipse.jetty.util.security.CertificateUtils;
import info.journeymap.shaded.org.eclipse.jetty.util.security.CertificateValidator;
import info.journeymap.shaded.org.eclipse.jetty.util.security.Password;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;

public class ClientCertAuthenticator extends LoginAuthenticator {

    private static final String PASSWORD_PROPERTY = "info.journeymap.shaded.org.eclipse.jetty.ssl.password";

    private String _trustStorePath;

    private String _trustStoreProvider;

    private String _trustStoreType = "JKS";

    private transient Password _trustStorePassword;

    private boolean _validateCerts;

    private String _crlPath;

    private int _maxCertPathLength = -1;

    private boolean _enableCRLDP = false;

    private boolean _enableOCSP = false;

    private String _ocspResponderURL;

    @Override
    public String getAuthMethod() {
        return "CLIENT_CERT";
    }

    @Override
    public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory) throws ServerAuthException {
        if (!mandatory) {
            return new DeferredAuthentication(this);
        } else {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            X509Certificate[] certs = (X509Certificate[]) request.getAttribute("info.journeymap.shaded.org.javax.servlet.request.X509Certificate");
            try {
                if (certs != null && certs.length > 0) {
                    if (this._validateCerts) {
                        KeyStore trustStore = this.getKeyStore(this._trustStorePath, this._trustStoreType, this._trustStoreProvider, this._trustStorePassword == null ? null : this._trustStorePassword.toString());
                        Collection<? extends CRL> crls = this.loadCRL(this._crlPath);
                        CertificateValidator validator = new CertificateValidator(trustStore, crls);
                        validator.validate(certs);
                    }
                    for (X509Certificate cert : certs) {
                        if (cert != null) {
                            Principal principal = cert.getSubjectDN();
                            if (principal == null) {
                                principal = cert.getIssuerDN();
                            }
                            String username = principal == null ? "clientcert" : principal.getName();
                            char[] credential = B64Code.encode(cert.getSignature());
                            UserIdentity user = this.login(username, credential, req);
                            if (user != null) {
                                return new UserAuthentication(this.getAuthMethod(), user);
                            }
                        }
                    }
                }
                if (!DeferredAuthentication.isDeferred(response)) {
                    response.sendError(403);
                    return Authentication.SEND_FAILURE;
                } else {
                    return Authentication.UNAUTHENTICATED;
                }
            } catch (Exception var15) {
                throw new ServerAuthException(var15.getMessage());
            }
        }
    }

    @Deprecated
    protected KeyStore getKeyStore(InputStream storeStream, String storePath, String storeType, String storeProvider, String storePassword) throws Exception {
        return this.getKeyStore(storePath, storeType, storeProvider, storePassword);
    }

    protected KeyStore getKeyStore(String storePath, String storeType, String storeProvider, String storePassword) throws Exception {
        return CertificateUtils.getKeyStore(Resource.newResource(storePath), storeType, storeProvider, storePassword);
    }

    protected Collection<? extends CRL> loadCRL(String crlPath) throws Exception {
        return CertificateUtils.loadCRL(crlPath);
    }

    @Override
    public boolean secureResponse(ServletRequest req, ServletResponse res, boolean mandatory, Authentication.User validatedUser) throws ServerAuthException {
        return true;
    }

    public boolean isValidateCerts() {
        return this._validateCerts;
    }

    public void setValidateCerts(boolean validateCerts) {
        this._validateCerts = validateCerts;
    }

    public String getTrustStore() {
        return this._trustStorePath;
    }

    public void setTrustStore(String trustStorePath) {
        this._trustStorePath = trustStorePath;
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

    public void setTrustStorePassword(String password) {
        this._trustStorePassword = Password.getPassword("info.journeymap.shaded.org.eclipse.jetty.ssl.password", password, null);
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
}