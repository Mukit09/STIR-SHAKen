import lombok.extern.slf4j.Slf4j;
import utility.CommonUtils;

import java.io.FileOutputStream;

@Slf4j
public class AppRunner {
    public static void main(String[] args) {
        log.debug("App Started Successfully...");

        RequestBuilder builder = new RequestBuilder();
        String encodedHeader = builder.buildEncodedHeader();
        String encodedPayload = builder.buildEncodedPayload();
        String enCodedSignature = builder.getEncodedSignature(encodedHeader, encodedPayload);

        String identityHeader = encodedHeader + "." + encodedPayload + "." + enCodedSignature;
    //    log.debug(identityHeader);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(CommonUtils.IDENTITY_HEADER_FILE_NAME);
            outputStream.write(identityHeader.getBytes());
        } catch (Exception e) {
            log.error("Exception: ", e);
        } finally {
            CommonUtils.getInstance().closeFileOutputStream(outputStream);
        }

        Verifier verifier = new Verifier();
        verifier.loadPublicKeyFromCertificate();
        verifier.loadDataAndSignatureInByteForm();
        verifier.getSignature();
        boolean isVerified = verifier.verifyData();
        log.debug("Data Verification: " + isVerified);
        if(isVerified) {
            String identityHeader1 = CommonUtils.getInstance().getIdentityHeader();
            String tempArray[] = identityHeader1.split("\\.");
            String encodedHeader1 = tempArray[0];
            String encodedPayload1 = tempArray[1];
            Content content = new Content();
            content.writeContent(encodedHeader1, encodedPayload1);
        }
    }
}
