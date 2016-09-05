package android.microanswer.healthy.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.BookListItem;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * 由 Micro 创建于 2016/8/9.
 */

public class BookPageDialog extends Dialog implements View.OnClickListener {
    private BookListItem item;
    private HtmlView htmlView;
    private Toolbar toolbar;
    private int index;

    private Button up, next;
    private ScrollView view_booklistpagedialog_scrollview;


    public BookPageDialog(Context context, BookListItem item, int index) {
        super(context, R.style.bookdialogstyle);
        this.index = index;
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_bookpagedialog);
        htmlView = (HtmlView) findViewById(R.id.view_bookpagedialog_htmlview);
        toolbar = (Toolbar) findViewById(R.id.view_booklistpagedialog_toolbar);
        view_booklistpagedialog_scrollview = (ScrollView) findViewById(R.id.view_booklistpagedialog_scrollview);
        up = (Button) findViewById(R.id.view_booklistpagedialog_up);
        up.setOnClickListener(this);
        next = (Button) findViewById(R.id.view_booklistpagedialog_next);
        next.setOnClickListener(this);
        toolbar.setTitle(item.getList().get(index).getTitle());
        htmlView.setBackgroundColor(Color.WHITE);

        htmlView.setHtml(item.getList().get(index).getMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.x = 0;
        attributes.y = 0;
        attributes.alpha = 1f;
        attributes.format = PixelFormat.RGBA_8888;
        attributes.width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        attributes.height = getWindow().getWindowManager().getDefaultDisplay().getHeight() * 4 / 5;
        attributes.gravity = Gravity.LEFT | Gravity.BOTTOM;
        getWindow().setAttributes(attributes);
    }

    @Override
    public void onClick(View view) {
        if (view == up) {
            if (index == 0) {
                Toast.makeText(getContext(), "没有了", Toast.LENGTH_SHORT).show();
            } else {
                index--;
                toolbar.setTitle(item.getList().get(index).getTitle());
                htmlView.setHtml(item.getList().get(index).getMessage());
                view_booklistpagedialog_scrollview.smoothScrollTo(0, 0);

            }
        } else if (view == next) {
            if (index == item.getList().size() - 1) {
                Toast.makeText(getContext(), "没有了", Toast.LENGTH_SHORT).show();
            } else {
                index++;
                toolbar.setTitle(item.getList().get(index).getTitle());
                htmlView.setHtml(item.getList().get(index).getMessage());
                view_booklistpagedialog_scrollview.smoothScrollTo(0, 0);
            }

        }
    }
}
