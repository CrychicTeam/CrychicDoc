package info.journeymap.shaded.org.eclipse.jetty.util.security;

public interface CredentialProvider {

    Credential getCredential(String var1);

    String getPrefix();
}