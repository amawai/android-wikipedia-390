package org.wikipedia.travel;

/**
 * Created by mnhn3 on 2018-03-04.
 */

//class for location cards shown on recycler viewa, description is optional
public class PlacesCard {
    public String title;
    public String desc;

    public PlacesCard(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public void setTitle(String title){
        this.title=title;
    }
}
