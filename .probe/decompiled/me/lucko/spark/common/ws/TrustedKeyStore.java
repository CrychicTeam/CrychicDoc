package me.lucko.spark.common.ws;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.lucko.spark.common.util.Configuration;

public class TrustedKeyStore {

    private static final String TRUSTED_KEYS_OPTION = "trustedKeys";

    private final Configuration configuration;

    private final CompletableFuture<KeyPair> localKeyPair;

    private final Set<PublicKey> remoteTrustedKeys;

    private final Map<String, PublicKey> remotePendingKeys = new HashMap();

    public TrustedKeyStore(Configuration configuration) {
        this.configuration = configuration;
        this.localKeyPair = CompletableFuture.supplyAsync(ViewerSocketConnection.CRYPTO::generateKeyPair);
        this.remoteTrustedKeys = new HashSet();
        this.readTrustedKeys();
    }

    public PublicKey getLocalPublicKey() {
        return ((KeyPair) this.localKeyPair.join()).getPublic();
    }

    public PrivateKey getLocalPrivateKey() {
        return ((KeyPair) this.localKeyPair.join()).getPrivate();
    }

    public boolean isKeyTrusted(PublicKey publicKey) {
        return publicKey != null && this.remoteTrustedKeys.contains(publicKey);
    }

    public void addPendingKey(String clientId, PublicKey publicKey) {
        this.remotePendingKeys.put(clientId, publicKey);
    }

    public boolean trustPendingKey(String clientId) {
        PublicKey key = (PublicKey) this.remotePendingKeys.remove(clientId);
        if (key == null) {
            return false;
        } else {
            this.remoteTrustedKeys.add(key);
            this.writeTrustedKeys();
            return true;
        }
    }

    private void readTrustedKeys() {
        for (String encodedKey : this.configuration.getStringList("trustedKeys")) {
            try {
                PublicKey publicKey = ViewerSocketConnection.CRYPTO.decodePublicKey(Base64.getDecoder().decode(encodedKey));
                this.remoteTrustedKeys.add(publicKey);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
    }

    private void writeTrustedKeys() {
        List<String> encodedKeys = (List<String>) this.remoteTrustedKeys.stream().map(key -> Base64.getEncoder().encodeToString(key.getEncoded())).collect(Collectors.toList());
        this.configuration.setStringList("trustedKeys", encodedKeys);
    }
}