package com.david.incubator.print;

import com.david.core.util.ILifeCycleOwner;

public interface IPrintView extends ILifeCycleOwner {
    void clearInfo();

    float drawInitialPrintPoint(Integer start, Integer length, int[] dataArray);

    float drawPrintPoint(Integer start, Integer length, int[] dataSeries, float previousY);

    float getPacketLength();
}
