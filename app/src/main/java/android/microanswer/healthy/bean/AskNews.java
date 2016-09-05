package android.microanswer.healthy.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Micro on 2016/6/23.
 */
@Deprecated
public class AskNews implements Serializable {
    private ArrayList<AskListItem> askListItems;
    private int page;
    private int size;
    private int total;
    private int totalpage;

    public ArrayList<AskListItem> getAskListItems() {
        return askListItems;
    }

    public void setAskListItems(ArrayList<AskListItem> askListItems) {
        this.askListItems = askListItems;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }
}
