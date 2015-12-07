package com.iwork.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.utils.ImageUtil;


/**
 * A-z侧边栏
 * 
 * @author TianXiao
 * @since 2014-6-25
 */
public class SideBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private static final String[] b = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z" };
    private int choose = -1;//
    private Paint paint = new Paint();

    private TextView mTextDialog;

    // 设置首字母浮层
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();// 获取对应高度
        int width = getWidth();// 获取对应宽度
        int length = b.length + 1;
        int singleHeight = height / length;// 获取每一个字母的高度

        // 绘制热门星号
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float bgOnWidthRatio = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 34, dm);//WindowUtil.computeScaledDimenByBalancedRatio(R.dimen.city_pick_slide_star_width);
        float bgOnHeightRatio = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 34, dm);//WindowUtil.computeScaledDimenByBalancedRatio(R.dimen.city_pick_slide_star_width);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.booking_icn_selectcity_star);
        Bitmap bm = ImageUtil.scale(bitmap, bgOnWidthRatio, bgOnHeightRatio, ScaleType.FIT_XY, true);
        float left = (width - bgOnWidthRatio)/ 2.0f;
        float top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 8, dm);
        canvas.drawBitmap(bm, left, top, paint);

        for (int i = 0; i < b.length; i++) {
            paint.setColor(getResources().getColor(android.R.color.darker_gray));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(getResources().getDimension(R.dimen.font_size_xxx_small_sp));
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }

            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(b[i]) / 2.0f;
            float yPos = singleHeight * i + singleHeight + bitmap.getHeight();
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        int length = b.length + 1;
        final int c = (int) (y / getHeight() * length);

        switch (action) {
        case MotionEvent.ACTION_UP:
            choose = -1;//
            invalidate();
            if (mTextDialog != null) {
                mTextDialog.setVisibility(View.INVISIBLE);
            }
            break;

        default:
            if (oldChoose != c) {
                if (c >= 0 && c < length) {
                    if (listener != null) {
                        if (c == 0) {// 选择了热门
                            listener.onTouchingLetterChanged("star");
                        } else {// 拼音检索
                            listener.onTouchingLetterChanged(b[c - 1]);
                        }
                    }
                    if (mTextDialog != null) {
                        mTextDialog.setText(b[c]);
                        mTextDialog.setVisibility(View.VISIBLE);
                    }

                    choose = c;
                    invalidate();
                }
            }

            break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}