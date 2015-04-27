package edu.dtlevyiastate.archie;

/** RowItem class to hold all relevent data that will need to be displayed in the list
 * @author David Levy <dtlevy@iastate.edu>
 * @version 0.1
 */
public class RowItem {
    private int imageId;
    private String title;
    private String desc;

    public RowItem(int imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
    }
    public int getImageId() {
        return imageId;
    }
    public String getDesc() {
        return desc;
    }
    public String getTitle() {
        return title;
    }
    public String toString() {
        return title + "\n" + desc;
    }
}