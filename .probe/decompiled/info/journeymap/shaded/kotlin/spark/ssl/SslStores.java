package info.journeymap.shaded.kotlin.spark.ssl;

public class SslStores {

    protected String keystoreFile;

    protected String keystorePassword;

    protected String truststoreFile;

    protected String truststorePassword;

    protected boolean needsClientCert;

    public static SslStores create(String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword) {
        return new SslStores(keystoreFile, keystorePassword, truststoreFile, truststorePassword);
    }

    public static SslStores create(String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword, boolean needsClientCert) {
        return new SslStores(keystoreFile, keystorePassword, truststoreFile, truststorePassword, needsClientCert);
    }

    private SslStores(String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword) {
        this.keystoreFile = keystoreFile;
        this.keystorePassword = keystorePassword;
        this.truststoreFile = truststoreFile;
        this.truststorePassword = truststorePassword;
    }

    private SslStores(String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword, boolean needsClientCert) {
        this.keystoreFile = keystoreFile;
        this.keystorePassword = keystorePassword;
        this.truststoreFile = truststoreFile;
        this.truststorePassword = truststorePassword;
        this.needsClientCert = needsClientCert;
    }

    public String keystoreFile() {
        return this.keystoreFile;
    }

    public String keystorePassword() {
        return this.keystorePassword;
    }

    public String trustStoreFile() {
        return this.truststoreFile;
    }

    public String trustStorePassword() {
        return this.truststorePassword;
    }

    public boolean needsClientCert() {
        return this.needsClientCert;
    }
}