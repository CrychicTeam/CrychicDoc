package info.journeymap.shaded.org.eclipse.jetty.util.security;

import info.journeymap.shaded.org.eclipse.jetty.util.TypeUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ServiceLoader;

public abstract class Credential implements Serializable {

    private static final ServiceLoader<CredentialProvider> CREDENTIAL_PROVIDER_LOADER = ServiceLoader.load(CredentialProvider.class);

    private static final Logger LOG = Log.getLogger(Credential.class);

    private static final long serialVersionUID = -7760551052768181572L;

    public abstract boolean check(Object var1);

    public static Credential getCredential(String credential) {
        if (credential.startsWith("CRYPT:")) {
            return new Credential.Crypt(credential);
        } else if (credential.startsWith("MD5:")) {
            return new Credential.MD5(credential);
        } else {
            for (CredentialProvider cp : CREDENTIAL_PROVIDER_LOADER) {
                if (credential.startsWith(cp.getPrefix())) {
                    Credential credentialObj = cp.getCredential(credential);
                    if (credentialObj != null) {
                        return credentialObj;
                    }
                }
            }
            return new Password(credential);
        }
    }

    public static class Crypt extends Credential {

        private static final long serialVersionUID = -2027792997664744210L;

        public static final String __TYPE = "CRYPT:";

        private final String _cooked;

        Crypt(String cooked) {
            this._cooked = cooked.startsWith("CRYPT:") ? cooked.substring("CRYPT:".length()) : cooked;
        }

        @Override
        public boolean check(Object credentials) {
            if (credentials instanceof char[]) {
                credentials = new String((char[]) credentials);
            }
            if (!(credentials instanceof String) && !(credentials instanceof Password)) {
                Credential.LOG.warn("Can't check " + credentials.getClass() + " against CRYPT");
            }
            String passwd = credentials.toString();
            return this._cooked.equals(UnixCrypt.crypt(passwd, this._cooked));
        }

        public boolean equals(Object credential) {
            if (!(credential instanceof Credential.Crypt)) {
                return false;
            } else {
                Credential.Crypt c = (Credential.Crypt) credential;
                return this._cooked.equals(c._cooked);
            }
        }

        public static String crypt(String user, String pw) {
            return "CRYPT:" + UnixCrypt.crypt(pw, user);
        }
    }

    public static class MD5 extends Credential {

        private static final long serialVersionUID = 5533846540822684240L;

        public static final String __TYPE = "MD5:";

        public static final Object __md5Lock = new Object();

        private static MessageDigest __md;

        private final byte[] _digest;

        MD5(String digest) {
            digest = digest.startsWith("MD5:") ? digest.substring("MD5:".length()) : digest;
            this._digest = TypeUtil.parseBytes(digest, 16);
        }

        public byte[] getDigest() {
            return this._digest;
        }

        @Override
        public boolean check(Object credentials) {
            try {
                byte[] digest = null;
                if (credentials instanceof char[]) {
                    credentials = new String((char[]) credentials);
                }
                if (credentials instanceof Password || credentials instanceof String) {
                    synchronized (__md5Lock) {
                        if (__md == null) {
                            __md = MessageDigest.getInstance("MD5");
                        }
                        __md.reset();
                        __md.update(credentials.toString().getBytes(StandardCharsets.ISO_8859_1));
                        digest = __md.digest();
                    }
                    if (digest != null && digest.length == this._digest.length) {
                        boolean digestMismatch = false;
                        for (int i = 0; i < digest.length; i++) {
                            digestMismatch |= digest[i] != this._digest[i];
                        }
                        return !digestMismatch;
                    } else {
                        return false;
                    }
                } else if (credentials instanceof Credential.MD5) {
                    return this.equals((Credential.MD5) credentials);
                } else if (credentials instanceof Credential) {
                    return ((Credential) credentials).check(this);
                } else {
                    Credential.LOG.warn("Can't check " + credentials.getClass() + " against MD5");
                    return false;
                }
            } catch (Exception var6) {
                Credential.LOG.warn(var6);
                return false;
            }
        }

        public boolean equals(Object obj) {
            if (obj instanceof Credential.MD5) {
                Credential.MD5 md5 = (Credential.MD5) obj;
                if (this._digest.length != md5._digest.length) {
                    return false;
                } else {
                    boolean digestMismatch = false;
                    for (int i = 0; i < this._digest.length; i++) {
                        digestMismatch |= this._digest[i] != md5._digest[i];
                    }
                    return !digestMismatch;
                }
            } else {
                return false;
            }
        }

        public static String digest(String password) {
            try {
                byte[] digest;
                synchronized (__md5Lock) {
                    if (__md == null) {
                        try {
                            __md = MessageDigest.getInstance("MD5");
                        } catch (Exception var5) {
                            Credential.LOG.warn(var5);
                            return null;
                        }
                    }
                    __md.reset();
                    __md.update(password.getBytes(StandardCharsets.ISO_8859_1));
                    digest = __md.digest();
                }
                return "MD5:" + TypeUtil.toString(digest, 16);
            } catch (Exception var7) {
                Credential.LOG.warn(var7);
                return null;
            }
        }
    }
}