package adapter;

import android.graphics.Paint;

import com.iwork.ui.view.WheelView;


/**
 * 滚轮适配器
 * 
 */
abstract public class WheelAdapter {
    private WheelView mWheelView;

    abstract public int getCount();

    abstract public String getItem(int index);

    public String getItemStringFormatter() {
        return null;
    }

    public void setItemStringFormatter(String formatter) {
    }

    public float getMaxWidth(Paint paint) {
        String first = getItem(0);
        String last = getItem(getCount() - 1);
        float firstWidth = paint.measureText(first);
        float lastWidth = paint.measureText(last);
        return Math.max(firstWidth, lastWidth);
    }

    public void setWheelView(WheelView wheelView) {
        mWheelView = wheelView;
    }

    public void notifyChanged() {
        mWheelView.computeTextBaseY();
        mWheelView.refresh();
    }

    abstract public int getValue(int index);

    /**
     * 获取当前起始值
     * 
     * @return
     */
    abstract public int getStartValue();

    abstract public void setStartValue(int value);

    /**
     * 获取当前结束值
     *
     * @return
     */
    abstract public int getEndValue();

    ;

    public void setEndValue(int value) {

    }

    /**
     * 获取间隔值
     *
     * @return
     */
    abstract public int getInterval();

    abstract public int getValueIndex(int value);
}
