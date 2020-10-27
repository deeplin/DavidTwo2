package com.david.core.enumeration;

import com.david.R;

public enum AlarmPriorityEnum {
    H(R.drawable.alarm_left_high_background, R.drawable.alarm_right_high_background),
    M(R.drawable.alarm_left_middle_background, R.drawable.alarm_right_middle_background),
    L(R.drawable.alarm_left_low_background, R.drawable.alarm_right_low_background),
    I(R.drawable.alarm_left_info_background, R.drawable.alarm_right_info_background),
    USER(0, 0);

    public int getLeftColorId() {
        return leftColorId;
    }

    public int getRightColorId() {
        return rightColorId;
    }

    private final int leftColorId;
    private final int rightColorId;

    AlarmPriorityEnum(int leftColorId, int rightColorId) {
        this.leftColorId = leftColorId;
        this.rightColorId = rightColorId;
    }
}
