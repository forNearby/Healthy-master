package android.microanswer.healthy.bean;

/**
 * 由 Micro 创建于 2016/7/22.
 */

public class FriendGroup extends Friend {
    private String letter;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "FriendGroup{" +
                "letter='" + letter + '\'' +
                '}';
    }
}
