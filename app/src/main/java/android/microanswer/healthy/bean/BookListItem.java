package android.microanswer.healthy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 由 Micro 创建于 2016/6/24.
 */
@Deprecated
public class BookListItem implements Serializable {
    private String name;//名称
    private String img;//图片
//    private String from;//    来源
    private String author;//      作者
    private String summary;//     简介
    private int bookclass;//      分类
    private int count;//      阅读次数
    private int fcount;//收藏数
    private int rcount;//评论读数
    private int id;
    private long time;
    private List<BookPage> list;

    public List<BookPage> getList() {
        return list;
    }

    public void setList(List<BookPage> list) {
        this.list = list;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

//    public String getFrom() {
//        return from;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getBookclass() {
        return bookclass;
    }

    public void setBookclass(int bookclass) {
        this.bookclass = bookclass;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    @Override
    public String toString() {
        return "BookListItem{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", author='" + author + '\'' +
                ", summary='" + summary + '\'' +
                ", bookclass=" + bookclass +
                ", count=" + count +
                ", fcount=" + fcount +
                ", rcount=" + rcount +
                ", id=" + id +
                ", time=" + time +
                ", list=" + list +
                '}';
    }

    /**
     * 章节
     */
    public static class BookPage implements Serializable, Comparable<BookPage> {
        private String message;
        private int id;
        private int seq;
        private String title;
        private int book;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getBook() {
            return book;
        }

        public void setBook(int book) {
            this.book = book;
        }

        @Override
        public String toString() {
            return "BookPage{" +
                    "message='" + message + '\'' +
                    ", id=" + id +
                    ", seq=" + seq +
                    ", title='" + title + '\'' +
                    ", book=" + book +
                    '}';
        }

        @Override
        public int compareTo(BookPage bookPage) {
            return -(bookPage.getId() - getId());
        }
    }
}
