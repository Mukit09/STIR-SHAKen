import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.CommonUtils;

@Slf4j
public class Content {

    public void writeContent(String encodedHeader, String encodedPayload) {
        writeHeaderInfo(encodedHeader);
        writePayloadInfo(encodedPayload);
    }

    public void writeHeaderInfo(String encodeHeader) {
        String header = CommonUtils.getInstance().getDecodedString(encodeHeader);
        log.debug("Header: " + header);
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(header);
            log.debug("Certificate location: " + object.get("x5u"));
        } catch (ParseException e) {
            log.error("Exception: ", e);
        }
    }

    public void writePayloadInfo(String encodedPayload) {
        String payload = CommonUtils.getInstance().getDecodedString(encodedPayload);
        log.debug("Payload: " + payload);
    }
}
