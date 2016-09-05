package android.microanswer.healthy.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Micro on 2016/6/23.
 */
@Deprecated
public class LoreNews implements Serializable {
    private List<LoreListItem> list;
    private int page;
    private int size;
    private int total;
    private int totalpage;

    public List<LoreListItem> getList() {
        return list;
    }

    public void setList(List<LoreListItem> list) {
        this.list = list;
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
