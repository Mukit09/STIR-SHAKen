import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import utility.CommonUtils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

@Slf4j
@Getter
@Setter
public class Signer {
    private PrivateKey privateKey;
    private KeyStore keyStore;
    private byte[] byteDataArray;
    private byte[] byteSignatureArray;

    public Signer(String data) {
        loadKeyStore();
        getPrivateKey();
        loadData(data);
        signData();
    }

    private void loadKeyStore() {
        char passwordCharArray[] = CommonUtils.KEY_STORE_PASSWORD.toCharArray();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(CommonUtils.KEY_STORE_FILE_NAME);
            this.keyStore = KeyStore.getInstance("JKS");
            this.keyStore.load(inputStream, passwordCharArray);
        } catch (Exception e) {
            log.error("Exception: ", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("Exception: ", e);
                }
            }
        }
    }

    private void getPrivateKey() {
        char[] passwordCharArray = CommonUtils.PRIVATE_KEY_PASSWORD.toCharArray();
        try {
            this.privateKey = (PrivateKey) keyStore.getKey("aliasmukit", passwordCharArray);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    public void signData() {
        try {
            Signature signature = Signature.getInstance(CommonUtils.SIGNATURE_ALGORITHM, CommonUtils.SIGNATURE_PROVIDER);
            signature.initSign(this.privateKey);
            signature.update(this.byteDataArray);
            this.byteSignatureArray = signature.sign();
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    public void loadData(String data) {
        this.byteDataArray = data.getBytes();
    }
}
