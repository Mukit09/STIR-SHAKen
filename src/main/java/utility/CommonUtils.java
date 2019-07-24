package utility;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

@Slf4j
public class CommonUtils {
    public static final String IDENTITY_HEADER_FILE_NAME = "identityHeaderFile.txt";
    public final static String CERTIFICATE_FILE_NAME = "certificate.crt";
   // public final static String CERTIFICATE_FILE_NAME = "https://certificates.clearip.com/b15d7cc9-0f26-46c2-83ea-a3e63a82ec3a/7cc4db695d13edada4d1f9861b9b80fe.crt";
    public final static String SIGNATURE_ALGORITHM = "SHA256withECDSA";
    public final static String SIGNATURE_PROVIDER = "SunEC";
    public final static String KEY_STORE_PASSWORD = "123456";
    public final static String PRIVATE_KEY_PASSWORD = "654321";
    public final static String KEY_STORE_FILE_NAME = "keystore.jks";

    private CommonUtils() {}

    private static volatile CommonUtils commonUtils;

    public static CommonUtils getInstance() {
        if(commonUtils == null) {
            synchronized (CommonUtils.class) {
                if(commonUtils == null) {
                    commonUtils = new CommonUtils();
                }
            }
        }
        return commonUtils;
    }

    public void closeFileInputStream(FileInputStream inputStream) {
        if(inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                log.error("Exception: ", e);
            }
        }
    }

    public void closeFileOutputStream(FileOutputStream outputStream) {
        if(outputStream != null) {
            try {
                outputStream.close();
            } catch (Exception e) {
                log.error("Exception: ", e);
            }
        }
    }

    public String getEncodedString(String string) {
        String encodedString = null;
        try {
            encodedString = Base64.getUrlEncoder().withoutPadding().encodeToString(string.getBytes("ISO-8859-1"));
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        return encodedString;
    }

    public String getDecodedString(String string) {
        String decodedString = null;
        try {
            decodedString = new String(Base64.getUrlDecoder().decode(string), "ISO-8859-1");
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        return decodedString;
    }

    public String getIdentityHeader() {
        FileInputStream inputStream = null;
        String identityHeader = null;
        StringBuilder builder = new StringBuilder();
        try {
            inputStream = new FileInputStream(CommonUtils.IDENTITY_HEADER_FILE_NAME);
            int i;
            while((i=inputStream.read())!= -1) {
                builder.append((char)i);
            }
            identityHeader = builder.toString();
        } catch (Exception e) {
            log.error("Exception: ", e);
        } finally {
            CommonUtils.getInstance().closeFileInputStream(inputStream);
        }
        return identityHeader;
    }
}
