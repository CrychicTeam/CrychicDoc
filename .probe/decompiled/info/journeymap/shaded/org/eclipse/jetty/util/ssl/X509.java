package info.journeymap.shaded.org.eclipse.jetty.util.ssl;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

public class X509 {

    private static final Logger LOG = Log.getLogger(X509.class);

    private static final int KEY_USAGE__KEY_CERT_SIGN = 5;

    private static final int SUBJECT_ALTERNATIVE_NAMES__DNS_NAME = 2;

    private final X509Certificate _x509;

    private final String _alias;

    private final List<String> _hosts = new ArrayList();

    private final List<String> _wilds = new ArrayList();

    public static boolean isCertSign(X509Certificate x509) {
        boolean[] key_usage = x509.getKeyUsage();
        return key_usage != null && key_usage[5];
    }

    public X509(String alias, X509Certificate x509) throws CertificateParsingException, InvalidNameException {
        this._alias = alias;
        this._x509 = x509;
        boolean named = false;
        Collection<List<?>> altNames = x509.getSubjectAlternativeNames();
        if (altNames != null) {
            for (List<?> list : altNames) {
                if (((Number) list.get(0)).intValue() == 2) {
                    String cn = list.get(1).toString();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Certificate SAN alias={} CN={} in {}", alias, cn, this);
                    }
                    if (cn != null) {
                        named = true;
                        this.addName(cn);
                    }
                }
            }
        }
        if (!named) {
            LdapName name = new LdapName(x509.getSubjectX500Principal().getName("RFC2253"));
            for (Rdn rdn : name.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("CN")) {
                    String cnx = rdn.getValue().toString();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Certificate CN alias={} CN={} in {}", alias, cnx, this);
                    }
                    if (cnx != null && cnx.contains(".") && !cnx.contains(" ")) {
                        this.addName(cnx);
                    }
                }
            }
        }
    }

    protected void addName(String cn) {
        cn = StringUtil.asciiToLowerCase(cn);
        if (cn.startsWith("*.")) {
            this._wilds.add(cn.substring(2));
        } else {
            this._hosts.add(cn);
        }
    }

    public String getAlias() {
        return this._alias;
    }

    public X509Certificate getCertificate() {
        return this._x509;
    }

    public Set<String> getHosts() {
        return new HashSet(this._hosts);
    }

    public Set<String> getWilds() {
        return new HashSet(this._wilds);
    }

    public boolean matches(String host) {
        host = StringUtil.asciiToLowerCase(host);
        if (!this._hosts.contains(host) && !this._wilds.contains(host)) {
            int dot = host.indexOf(46);
            if (dot >= 0) {
                String domain = host.substring(dot + 1);
                if (this._wilds.contains(domain)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return String.format("%s@%x(%s,h=%s,w=%s)", this.getClass().getSimpleName(), this.hashCode(), this._alias, this._hosts, this._wilds);
    }
}