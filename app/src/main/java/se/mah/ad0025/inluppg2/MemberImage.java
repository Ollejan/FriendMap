package se.mah.ad0025.inluppg2;

import android.graphics.Bitmap;

/**
 * Klass som hanterar användares tagna bilder och deras position.
 * @author Jonas Dahlström on 2014-10-25.
 */
public class MemberImage {
    private String member, group, imageText, longitude, latitude;
    private Bitmap image, smallImage;

    public MemberImage(String member, String group, String imageText, String longitude, String latitude, Bitmap image, Bitmap smallImage) {
        this.member = member;
        this.group = group;
        this.imageText = imageText;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
        this.smallImage = smallImage;
    }

    public String getMember() {
        return member;
    }

    public String getGroup() {
        return group;
    }

    public String getImageText() {
        return imageText;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getSmallImage() {
        return smallImage;
    }
}
