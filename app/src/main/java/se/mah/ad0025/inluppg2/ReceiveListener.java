package se.mah.ad0025.inluppg2;

import org.json.JSONObject;

/**
 * @author Jonas Dahlström on 2014-10-12.
 */
public interface ReceiveListener {
    public void newMessage(String answer);
    public void newJsonMessage(JSONObject jsonObject);
}
