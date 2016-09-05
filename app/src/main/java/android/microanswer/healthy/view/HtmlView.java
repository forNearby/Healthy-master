package android.microanswer.healthy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.microanswer.healthy.R;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.util.LinkedList;

public class HtmlView extends LinearLayout {

    private final String TAG = "HtmlView";

    private Parser parser;

    public HtmlView(Context context) {
        super(context);
        init();
    }

    public HtmlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.LEFT);
        parser = new Parser();
        try {
            parser.setProperty(Parser.schemaProperty, new HTMLSchema());
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void setHtml(final String mhtml) {
        removeAllViews();
        String html = mhtml;
        if (!html.startsWith("<html>")) {
            html = "<html>" + html;
        }

        if (!html.endsWith("</html>")) {
            html = html + "</html>";
        }

        if (!html.contains("<p>　　")) {
            html = html.replace("<p>", "<p>　　");
        }

        InputSource inputSource = new InputSource(new StringReader(html));
        try {
            parser.setContentHandler(new HtmlHandler());
            parser.parse(inputSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HtmlHandler extends DefaultHandler {

        private LinkedList<String> tags;

        public HtmlHandler() {
            tags = new LinkedList<String>();
        }

        @Override
        public void startDocument() throws SAXException {
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName, final Attributes attributes)
                throws SAXException {
            if (!qName.equalsIgnoreCase("html") || !qName.equalsIgnoreCase("body")) {
                tags.addLast(qName);
                if (qName.equalsIgnoreCase("img")) {
                    final ImageView imageView = new ImageView(getContext());
//                    imageView.setLayoutParams(new LayoutParams(getWidth(), LayoutParams.WRAP_CONTENT));
//                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageResource(R.mipmap.loading);
                    addView(imageView);
                    ImageLoader.getInstance().displayImage(attributes.getValue("src"), imageView,
                            new DisplayImageOptions.Builder()
                                    .displayer(new BitmapDisplayer() {
                                        @Override
                                        public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                                            int width = getWidth()-getPaddingLeft() - getPaddingRight();
                                            int height = Math.round((width / (float)bitmap.getWidth()) * bitmap.getHeight());


                                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                                            imageAware.getWrappedView().setLayoutParams(layoutParams);
                                            imageAware.setImageBitmap(bitmap);
                                        }
                                    })
                                    .build());
                } else if (qName.equalsIgnoreCase("p")) {
                    TextView tv = new TextView(getContext());
                    tv.setTextColor(Color.GRAY);
                    addView(tv);
                    tv.setText("\n");
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (!qName.equals("html") || !qName.equals("body")) {
                tags.removeLast();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            final String content = new String(ch, start, length);
            if (tags.size() < 1) {
                return;
            }
            String last = tags.getLast();
//            if (last.equalsIgnoreCase("p") || last.equalsIgnoreCase("a")) {
            if (getChildCount() < 1) {
                return;
            }

            View v = getChildAt(getChildCount() - 1);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;

                if (last.equalsIgnoreCase("strong")) {
                    tv.append(Html.fromHtml("<strong>" + content + "</strong>"));
                } else {
                    tv.append(content);
                }
            }
//            }
        }

    }

}
