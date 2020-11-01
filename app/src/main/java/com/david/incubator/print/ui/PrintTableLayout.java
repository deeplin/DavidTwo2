package com.david.incubator.print.ui;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.ViewPrintTableBinding;

public class PrintTableLayout extends BindingBasicLayout<ViewPrintTableBinding> {

    public PrintTableLayout(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_print_table;
    }

    public TableLayout getTable() {
        return binding.tlTable;
    }

    public void setWidth(int rowLength) {
        int width = 400;
        switch (rowLength) {
            case (3):
                width = 300;
                break;
            case (6):
                width = 500;
                break;
            case (7):
                width = 600;
                break;
        }
        ViewGroup.LayoutParams layoutParams = getChildAt(0).getLayoutParams();
        layoutParams.width = width;
        this.getChildAt(0).setLayoutParams(layoutParams);
    }
}