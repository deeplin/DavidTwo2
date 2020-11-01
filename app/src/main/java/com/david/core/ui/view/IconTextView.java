package com.david.core.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.LazyLiveData;
import com.david.databinding.ViewIconTextBinding;

public class IconTextView extends BindingBasicLayout<ViewIconTextBinding> {

    public final LazyLiveData<Integer> imageId = new LazyLiveData<>();
    public final LazyLiveData<String> value = new LazyLiveData<>();

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        binding.setViewModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_icon_text;
    }
}