package edu.dtlevyiastate.archie;

/** RowItem class to hold all relevent data that will need to be displayed in the list
 * @author David Levy <dtlevy@iastate.edu>
 * @version 0.1
 */
public class CommentItem {
    public String getComment() {
        return Comment;
    }

    public String getPosted() {
        return Posted;
    }

    public String getPoster() {
        return Poster;
    }

    private String Comment="C";
    private String Posted="Pd";
    private String Poster="Ps";

    public CommentItem(String comment, String posted, String poster) {
        this.Comment = comment;
        this.Posted = posted;
        this.Poster = poster;
    }

}