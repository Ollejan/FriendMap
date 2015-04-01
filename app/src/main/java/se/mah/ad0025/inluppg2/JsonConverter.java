package se.mah.ad0025.inluppg2;

import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Klass som gör om strängar till JSON-format.
 * @author Jonas Dahlström on 2014-10-12.
 */
public class JsonConverter {

    public String getGroups() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );
        try {
            writer.beginObject().name("type").value("groups").endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public String registerGroup(String group, String member) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );
        try {
            writer.beginObject().name("type").value("register").name("group").value(group).name("member").value(member).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TEST", stringWriter.toString());
        return stringWriter.toString();
    }

    public String unregisterGroup(String memberID) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );
        try {
            writer.beginObject().name("type").value("unregister").name("id").value(memberID).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public String sendLocation(String id, String longitude, String latitude) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );
        try {
            writer.beginObject().name("type").value("location").name("id").value(id).name("longitude").value(longitude).name("latitude").value(latitude).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public String getMembers(String group) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );
        try {
            writer.beginObject().name("type").value("members").name("group").value(group).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public String makeGroupIdJson(String group, String ID) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );
        try {
            writer.beginObject().name("group").value(group).name("id").value(ID).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public String sendText(String ID, String textMessage) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );
        try {
            writer.beginObject().name("type").value("textchat").name("id").value(ID).name("text").value(textMessage).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public String sendImage(String ID, String textMessage, String longitude, String latitude) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );
        try {
            writer.beginObject().name("type").value("imagechat").name("id").value(ID).name("text").value(textMessage).name("longitude").value(longitude).name("latitude").value(latitude).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}
