package com.netease.nim.demo.nim.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.netease.nim.demo.nim.tools.ToolTeamMemberAit;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolMoon {

    /**
     * 表情的放大倍数
     */
    private static final float EMOJIICON_SCALE = 1.2f;

    public static void identifyFaceExpression(Context context, TextView textView, String value) {
        identifyFaceExpression(
                context,
                textView,
                value,
                (int) (textView.getTextSize() * EMOJIICON_SCALE)
        );
    }

    public static void identifyFaceExpressionAndATags(Context context, TextView textView, String value) {
        SpannableString mSpannableString = makeSpannableStringTags(
                context,
                value,
                (int) (textView.getTextSize() * EMOJIICON_SCALE)
        );
        viewSetText(textView, mSpannableString);
    }

    /**
     * 具体类型的view设置内容
     *
     * @param textView
     * @param mSpannableString
     */
    private static void viewSetText(View textView, SpannableString mSpannableString) {
        TextView tv = (TextView) textView;
        tv.setText(mSpannableString);
    }

    public static void identifyFaceExpression(Context context, View textView, String value, int size) {
        SpannableString mSpannableString = replaceEmoticons(context, value, size);
        viewSetText(textView, mSpannableString);
    }

    public static void identifyRecentVHFaceExpressionAndTags(Context context, View textView,
                                                             String value, int size) {
        SpannableString mSpannableString = makeSpannableStringTags(context, value, size, false);
        ToolTeamMemberAit.replaceAitForeground(value, mSpannableString);
        viewSetText(textView, mSpannableString);
    }

    /**
     * lstmsgviewholder类使用,只需显示a标签对应的文本
     */
    public static void identifyFaceExpressionAndTags(Context context,
                                                     View textView, String value, int size) {
        SpannableString mSpannableString = makeSpannableStringTags(context, value, size, false);
        viewSetText(textView, mSpannableString);
    }

    private static SpannableString replaceEmoticons(Context context, String value, int size) {
        if (TextUtils.isEmpty(value)) {
            value = "";
        }

        SpannableString mSpannableString = new SpannableString(value);
        Matcher matcher = EmojiManager.getPattern().matcher(value);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String emot = value.substring(start, end);
            EmojiSpan span = new EmojiSpan(context, emot, size, size);
            if (span.getDrawable() != null) {
                mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return mSpannableString;
    }

    private static Pattern mATagPattern = Pattern.compile("<a.*?>.*?</a>");

    public static SpannableString makeSpannableStringTags(Context context, String value, int size) {
        return makeSpannableStringTags(context, value, size, true);
    }

    public static SpannableString makeSpannableStringTags(Context context, String value, int size, boolean bTagClickable) {
        ArrayList<ATagSpan> tagSpans = new ArrayList<ATagSpan>();
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        //a标签需要替换原始文本,放在moonutil类中
        Matcher aTagMatcher = mATagPattern.matcher(value);

        int start;
        int end;
        while (aTagMatcher.find()) {
            start = aTagMatcher.start();
            end = aTagMatcher.end();
            String atagString = value.substring(start, end);
            ATagSpan tagSpan = getTagSpan(atagString);
            value = value.substring(0, start) + tagSpan.getTag() + value.substring(end);
            tagSpan.setRange(start, start + tagSpan.getTag().length());
            tagSpans.add(tagSpan);
            aTagMatcher = mATagPattern.matcher(value);
        }


        SpannableString mSpannableString = new SpannableString(value);
        Matcher matcher = EmojiManager.getPattern().matcher(value);
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            String emot = value.substring(start, end);
            EmojiSpan span = new EmojiSpan(context, emot, size, size);
            if (span.getDrawable() != null) {
                mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if (bTagClickable) {
            for (ATagSpan tagSpan : tagSpans) {
                mSpannableString.setSpan(tagSpan, tagSpan.start, tagSpan.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return mSpannableString;
    }

    public static void replaceEmoticons(Context context, EditText editText, int start, int count) {
        Editable editable = editText.getText();
        if (count <= 0 || editable.length() < start + count)
            return;
        CharSequence s = editable.subSequence(start, start + count);
        Matcher matcher = EmojiManager.getPattern().matcher(s);
        int emojiSize = (int) (editText.getTextSize() * EMOJIICON_SCALE);
        while (matcher.find()) {
            int from = start + matcher.start();
            int to = start + matcher.end();
            String emot = editable.subSequence(from, to).toString();
            EmojiSpan span = new EmojiSpan(context, emot, emojiSize, emojiSize);
            if(span.getDrawable()!=null){
                editable.setSpan(span, from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static Drawable getEmotDrawable(Context context, String text, float scale) {
        Drawable drawable = EmojiManager.getDrawable(context, text);

        // scale
        if (drawable != null) {
            int width = (int) (drawable.getIntrinsicWidth() * scale);
            int height = (int) (drawable.getIntrinsicHeight() * scale);
            drawable.setBounds(0, 0, width, height);
        }

        return drawable;
    }

    private static ATagSpan getTagSpan(String text) {
        String href = null;
        String tag = null;
        if (text.toLowerCase().contains("href")) {
            int start = text.indexOf("\"");
            int end = text.indexOf("\"", start + 1);
            if (end > start)
                href = text.substring(start + 1, end);
        }
        int start = text.indexOf(">");
        int end = text.indexOf("<", start);
        if (end > start)
            tag = text.substring(start + 1, end);
        return new ATagSpan(tag, href);

    }

    private static class ATagSpan extends ClickableSpan {
        private int start;
        private int end;
        private String mUrl;
        private String tag;

        ATagSpan(String tag, String url) {
            this.tag = tag;
            this.mUrl = url;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }

        public String getTag() {
            return tag;
        }

        public void setRange(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void onClick(View widget) {
            try {
                if (TextUtils.isEmpty(mUrl))
                    return;
                Uri uri = Uri.parse(mUrl);
                String scheme = uri.getScheme();
                if (TextUtils.isEmpty(scheme)) {
                    mUrl = "http://" + mUrl;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
