package org.wikipedia.travel.landmarkpicker;

/**
 * Created by mnhn3 on 2018-03-04.
 */

//class for location cards shown on recycler viewa, description is optional
public class LandmarkCard {

    private String title;
    private String desc;
    private String thumbUrl;

    public LandmarkCard(String title, String desc, String thumbUrl) {
        this.title = title;
        this.desc = desc;
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }
}
