import lombok.extern.slf4j.Slf4j;
import utility.CommonUtils;

import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Slf4j
public class Verifier {
    private PublicKey publicKey;
    private Signature signature;

    private byte[] byteDataArray;
    private byte[] byteSignatureArray;

    public void loadPublicKeyFromCertificate() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(CommonUtils.CERTIFICATE_FILE_NAME);
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            Certificate certificate = factory.generateCertificate(inputStream);
            this.publicKey = certificate.getPublicKey();
        } catch (Exception e) {
            log.error("Exception: ", e);
        } finally {
            CommonUtils.getInstance().closeFileInputStream(inputStream);
        }
    }

    public void loadDataAndSignatureInByteForm() {
        try {
            String identityHeader = CommonUtils.getInstance().getIdentityHeader();
            String tempArray[] = identityHeader.split("\\.");
            String data = tempArray[0] + "." + tempArray[1];
            this.byteDataArray = data.getBytes();
            String encodedSignature = tempArray[2];
            String signature = CommonUtils.getInstance().getDecodedString(encodedSignature);
            this.byteSignatureArray = signature.getBytes("ISO-8859-1");
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    public void getSignature() {
        try {
            this.signature = Signature.getInstance(CommonUtils.SIGNATURE_ALGORITHM, CommonUtils.SIGNATURE_PROVIDER);
            this.signature.initVerify(this.publicKey);
            this.signature.update(this.byteDataArray);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    public boolean verifyData() {
        try {
            boolean isVerified = this.signature.verify(this.byteSignatureArray);
            return isVerified;
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        return false;
    }
}
