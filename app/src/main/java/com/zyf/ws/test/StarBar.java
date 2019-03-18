package com.zyf.ws.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zyf.ws.R;

/**
 * Created by zyf on 2019/2/13.
 */
public class StarBar extends View {

    private int starDistance = 0; //星星间距
    private int starCount = 5;  //星星个数
    private int starSize;     //星星高度大小，星星一般正方形，宽度等于高度
    private float starMark = 0.0F;   //评分星星
    private Bitmap starFullBitmap; //亮星星
    private Drawable starEmptyDrawable; //暗星星
    private OnStarChangeListener onStarChangeListener;//监听星星变化接口
    private Paint paint;         //绘制星星画笔
    private boolean integerMark = true;//整数评分
    private boolean onlyShow = false;//true:只能展示，不能评分

    public StarBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StarBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化UI组件
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs){
        setClickable(true);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.StarBar);
        this.starDistance = (int) mTypedArray.getDimension(R.styleable.StarBar_starDistance, 0);
        this.starSize = (int) mTypedArray.getDimension(R.styleable.StarBar_starSize, 32);
        this.starCount = mTypedArray.getInteger(R.styleable.StarBar_starCount, 5);
        this.starMark = mTypedArray.getFloat(R.styleable.StarBar_starStep, 0);
        this.starEmptyDrawable = mTypedArray.getDrawable(R.styleable.StarBar_starEmpty);
        this.starFullBitmap =  drawableToBitmap(mTypedArray.getDrawable(R.styleable.StarBar_starFull));
        this.onlyShow = mTypedArray.getBoolean(R.styleable.StarBar_onlyShow, false);
        mTypedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(starFullBitmap, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP));
    }

    /**
     * 设置是否需要整数评分
     * @param integerMark true：整数评分
     */
    public void setIntegerMark(boolean integerMark){
        this.integerMark = integerMark;
    }

    public void setOnlyShow(boolean onlyShow) {
        this.onlyShow = onlyShow;
    }

    /**
     * 设置显示的星星的分数
     *
     * @param mark 得分
     */
    public void setStarMark(float mark){
        if (integerMark) {
            starMark = (int)Math.ceil(mark);
        }else {
            starMark = Math.round(mark * 10) * 1.0f / 10;
        }
        if (this.onStarChangeListener != null) {
            this.onStarChangeListener.onStarChange(starMark);  //调用监听接口
        }
        invalidate();
    }

    /**
     * 获取显示星星的数目
     *
     * @return starMark
     */
    public float getStarMark(){
        return starMark;
    }


    /**
     * 定义星星点击的监听接口
     */
    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    /**
     * 设置监听
     */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener){
        this.onStarChangeListener = onStarChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starSize * starCount + starDistance * (starCount - 1), starSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (starFullBitmap == null || starEmptyDrawable == null) {
            return;
        }
        for (int i = 0;i < starCount;i++) {
            starEmptyDrawable.setBounds((starDistance + starSize) * i, 0, (starDistance + starSize) * i + starSize, starSize);
            starEmptyDrawable.draw(canvas);
        }
        if (starMark > 1) {
            canvas.drawRect(0, 0, starSize, starSize, paint);
            if(starMark-(int)(starMark) == 0) {
                for (int i = 1; i < starMark; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
            }else {
                for (int i = 1; i < starMark - 1; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
                canvas.translate(starDistance + starSize, 0);
                canvas.drawRect(0, 0, starSize * (Math.round(
                        (starMark - (int) (starMark)) * 10) * 1.0f / 10), starSize, paint);
            }
        }else {
            canvas.drawRect(0, 0, starSize * starMark, starSize, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!onlyShow) {
            int x = (int) event.getX();
            if (x < 0){
                x = 0;
            }
            if (x > getMeasuredWidth()) {
                x = getMeasuredWidth();
            }
            float mask = (x * 0.1f / (getMeasuredWidth() * 0.1f)) * starCount;
//            double dis = Math.ceil(mask) - mask;
//            if (dis > 0f && dis < 0.5f) {
//                mask = (float) (Math.ceil(mask) - 0.5f);
//            } else {
//                mask = (float) Math.ceil(mask);
//            }
            Log.i("StarBar", "mask: " + mask);
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN: {
                    setStarMark(mask);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    setStarMark(mask);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    break;
                }
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    /**
     * drawable转bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(starSize, starSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, starSize, starSize);
        drawable.draw(canvas);
        return bitmap;
    }
}
