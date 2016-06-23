package com.github.hintofbasil.crabbler;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by will on 23/06/16.
 */
public class RichTextExpander {

    public SpannableString toRichText(String text) {
        SpannableStringBuilder richText = new SpannableStringBuilder(text);
        // Bold
        Pattern pattern = Pattern.compile("<b>([^<>]+)<\\/b>");
        Matcher match = pattern.matcher(richText);
        while(match.find()) {
            String replacement = match.group(1);
            richText.replace(match.start(), match.end(), replacement);
            richText.setSpan(new StyleSpan(Typeface.BOLD), match.start(), match.start() + replacement.length(), 0);
            match = pattern.matcher(richText);
        }
        return new SpannableString(richText);
    }

}
