package info.journeymap.shaded.org.eclipse.jetty.util.security;

import info.journeymap.shaded.org.eclipse.jetty.util.resource.Resource;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CRL;
import java.security.cert.CertificateFactory;
import java.util.Collection;

public class CertificateUtils {

    public static KeyStore getKeyStore(Resource store, String storeType, String storeProvider, String storePassword) throws Exception {
        KeyStore keystore = null;
        if (store != null) {
            if (storeProvider != null) {
                keystore = KeyStore.getInstance(storeType, storeProvider);
            } else {
                keystore = KeyStore.getInstance(storeType);
            }
            if (!store.exists()) {
                throw new IllegalStateException("no valid keystore");
            }
            InputStream inStream = store.getInputStream();
            Throwable var6 = null;
            try {
                keystore.load(inStream, storePassword == null ? null : storePassword.toCharArray());
            } catch (Throwable var15) {
                var6 = var15;
                throw var15;
            } finally {
                if (inStream != null) {
                    if (var6 != null) {
                        try {
                            inStream.close();
                        } catch (Throwable var14) {
                            var6.addSuppressed(var14);
                        }
                    } else {
                        inStream.close();
                    }
                }
            }
        }
        return keystore;
    }

    public static Collection<? extends CRL> loadCRL(String crlPath) throws Exception {
        Collection<? extends CRL> crlList = null;
        if (crlPath != null) {
            InputStream in = null;
            try {
                in = Resource.newResource(crlPath).getInputStream();
                crlList = CertificateFactory.getInstance("X.509").generateCRLs(in);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
        return crlList;
    }
}