package mp.team.contentprovider;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dragos on 12.05.2017.
 */

public class Quote implements Serializable {
    public int id;
    public String text;
    public Date date;

    public Quote(int id, String text, Date date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public Quote(String text, Date date) {
        this.text = text;
        this.date = date;
    }
}
