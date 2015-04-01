package se.mah.ad0025.inluppg2;

/**
 * Klass som hanterar användares namn, grupp och position.
 * @author Jonas Dahlström on 2014-10-22.
 */
public class Locations {
    private String member, group, longitude, latitude;

    public Locations(String member, String group, String longitude, String latitude) {
        this.member = member;
        this.group = group;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMember() {
        return member;
    }

    public String getGroup() {
        return group;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
