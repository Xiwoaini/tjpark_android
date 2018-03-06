package tjpark.tjsinfo.com.tjpark.entity;

import java.io.Serializable;

/**
 * Created by panning on 2018/2/13.
 */

public class Person  implements Serializable {

    private String personItem;


    public int getPersonImg() {
        return personImg;
    }

    public void setPersonImg(int personImg) {
        this.personImg = personImg;
    }

    private int personImg;

    public String getPersonItem() {
        return personItem;
    }

    public void setPersonItem(String personItem) {
        this.personItem = personItem;
    }

}
