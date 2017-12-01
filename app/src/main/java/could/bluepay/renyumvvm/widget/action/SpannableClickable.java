package could.bluepay.renyumvvm.widget.action;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import could.bluepay.renyumvvm.R;


public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.color_8290af;
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = DEFAULT_COLOR_ID;
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
