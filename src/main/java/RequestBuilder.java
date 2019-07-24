import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utility.CommonUtils;

@Slf4j
@Getter
public class RequestBuilder {
    private String header;
    private String payload;

    public String buildEncodedHeader() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("alg", "ES256");
        jsonObject.put("ppt", "shaken");
        jsonObject.put("typ", "passport");
        jsonObject.put("x5u", CommonUtils.CERTIFICATE_FILE_NAME);
        header = jsonObject.toJSONString().replace("\\", "");
    //    log.debug("header string: " + header);
        String encodedHeader = CommonUtils.getInstance().getEncodedString(header);
        return encodedHeader;
    }

    public String buildEncodedPayload() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("attest", "A");

        JSONObject tn1 = new JSONObject();
        JSONArray jArray1 = new JSONArray();
        jArray1.add("14045266060");
        tn1.put("tn", jArray1);

        JSONObject tn2 = new JSONObject();
        tn2.put("tn", "18001234567");

        jsonObject.put("dest", tn1);
        jsonObject.put("iat", 1548859982);
        jsonObject.put("orig", tn2);
        jsonObject.put("origid", "123456");
        payload = jsonObject.toJSONString();
    //    log.debug("payload found: " + payload);
        String encodedPayload = CommonUtils.getInstance().getEncodedString(payload);
        return encodedPayload;
    }

    public String getEncodedSignature(String encodedHeader, String encodedPayload) {
        String encodedSignature = null;
        try {
            String temp = encodedHeader + "." + encodedPayload;
            Signer signer = new Signer(temp);
            String signature = new String(signer.getByteSignatureArray(), "ISO-8859-1");
            encodedSignature = CommonUtils.getInstance().getEncodedString(signature);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        return encodedSignature;
    }
}
