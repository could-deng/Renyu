package could.bluepay.renyumvvm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import could.bluepay.renyumvvm.http.bean.BestResultBean;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.utils.ViewUtils;

/**
 * 查询best平台的chartview
 */

public class BestChartView extends View {

    private Paint mPaint;

    int width ;
    int height;
    private List<BestResultBean.DateBean> beanDataList;
//    "ccy":"THB","sum_numbers":4291,"sum_price":150703,"create_time":"2017-53","max_time":"2017-12-31"

    private int data_priceMax = 0;
    private int data_priceMin = 0;
    private int data_numMax = 0;
    private int data_numMin = 0;

    public static int BufferTop = 10;
    public static int BufferBottom = 10;
    public static int BufferLeft = 10;
    public static int BufferRight = 10;


    private float xUnit;//X轴每个柱子的宽度
    private float yPriceUnit;//price数据超过bottom底线上1单位占的px
    private float yNumUnit;//num数据超过bottom底线上1单位占的px

    private int yAxisPriceTextColor = Color.rgb(139,58,58);//0xFF8c8c8c;//price类型y坐标的字颜色

    private int yAxisNumTextColor = Color.rgb(205,200,177);


    public BestChartView(Context context) {
        this(context,null);

    }

    public BestChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BestChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }



    private List<BestResultBean.DateBean> getBeanDataList(){
        if(beanDataList == null){
            beanDataList = new ArrayList<>();
        }
        return beanDataList;
    }

    public void setBeanDataList(List<BestResultBean.DateBean> list){
        getBeanDataList().clear();
        getBeanDataList().addAll(list);
        this.invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();

        drawTwoHLine(canvas);
        drawData(canvas);
    }

    private void drawData(Canvas canvas){
        if(getBeanDataList().size() == 0 || getBeanDataList().size()>100){
            Logger.e(Logger.DEBUG_TAG,"size == 0 || size>100");
            return;
        }
        for(BestResultBean.DateBean bean:getBeanDataList()){
            if(data_numMax == 0 || bean.getSum_numbers()>data_numMax){
                data_numMax = bean.getSum_numbers();
            }
            if(data_numMin == 0 || bean.getSum_numbers()<data_numMin){
                data_numMin = bean.getSum_numbers();
            }
            if(data_priceMax == 0 || bean.getSum_price()>data_priceMax){
                data_priceMax = bean.getSum_price();
            }
            if(data_priceMin == 0 || bean.getSum_price()<data_priceMin){
                data_priceMin = bean.getSum_price();
            }
        }
        //画y轴的坐标
        getPaint().reset();
        getPaint().setAntiAlias(true);
        getPaint().setColor(yAxisPriceTextColor);
        getPaint().setTextSize(ViewUtils.sp2px(getContext(),10));

        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();

        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;

        canvas.drawText(String.valueOf(data_priceMax), 4, BufferTop + offY, getPaint());
        canvas.drawText(String.valueOf(data_priceMin),4,height-BufferBottom+offY,getPaint());


        float pxTextLeft = getPaint().measureText(String.valueOf(data_priceMax));


        xUnit = (width-BufferLeft - BufferRight-pxTextLeft)*1.0f /getBeanDataList().size();
        yPriceUnit = (height-BufferTop - BufferBottom)*1.0f/(data_priceMax-data_priceMin);
        yNumUnit = (height-BufferTop-BufferBottom)*1.0f/(data_numMax-data_numMin);
        for(int i=0;i<getBeanDataList().size();i++){
            BestResultBean.DateBean data = getBeanDataList().get(i);
            drawSpecRect(canvas,true,i,data.getSum_price(),pxTextLeft);
//            drawSpecRect(canvas,false,i,data.getSum_numbers(),pxTextLeft);
        }
    }


    /**
     * 画矩形
     * @param canvas
     * @param priceData
     * @param index 从0开始
     */
    private void drawSpecRect(Canvas canvas,boolean priceData,int index,int value,float bufferLeft){
        getPaint().reset();
        getPaint().setStrokeWidth(2);
        getPaint().setStyle(Paint.Style.FILL);
        float yUnit ;
        if(priceData){
            getPaint().setColor(yAxisPriceTextColor);
            value -= data_priceMin;
            yUnit = yPriceUnit;
        }else {
            getPaint().setColor(yAxisNumTextColor);
            value -= data_numMin;
            yUnit = yNumUnit;
        }
        canvas.drawRect(BufferLeft+bufferLeft+index*xUnit,
                height-(BufferBottom+(value)*yUnit),
                BufferLeft+bufferLeft+(index+1)*xUnit,
                height,
                getPaint());
    }
    /**
     * 画两条XY轴坐标线
      * @param canvas
     */
    private void drawTwoHLine(Canvas canvas){
        drawHLine(canvas,0,width,height,height);
        drawHLine(canvas,0,0,0,height);
    }


    /**
     * 横纵坐标
     * @param canvas
     */
    private void drawHLine(Canvas canvas,int xStart,int xEnd,int yStart,int yEnd){
        getPaint().reset();
        getPaint().setStrokeWidth(2);
        getPaint().setStyle(Paint.Style.STROKE);
        getPaint().setColor(Color.BLACK);
        getPaint().setAntiAlias(true);

        canvas.drawLine(xStart,yStart,xEnd,yEnd,getPaint());
    }





    private Paint getPaint(){
        if(mPaint == null){
            mPaint = new Paint();

        }
        return mPaint;
    }
}
