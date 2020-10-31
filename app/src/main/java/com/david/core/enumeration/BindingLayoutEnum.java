package com.david.core.enumeration;

public enum BindingLayoutEnum {

    Standard(true, 352, 64,
            240, 48, 80,
            176, 132);


    public boolean isTitle() {
        return title;
    }

    public int getComponentWidth() {
        return componentWidth;
    }

    public int getComponentHeight() {
        return componentHeight;
    }

    public int getButtonWidth() {
        return buttonWidth;
    }

    public int getButtonHeight() {
        return buttonHeight;
    }

    public int getPopupWidth() {
        return popupWidth;
    }

    public int getPopupHeight() {
        return popupHeight;
    }

    public int getButtonStart() {
        return buttonStart;
    }

    private final boolean title;
    private final int componentWidth;
    private final int componentHeight;
    private final int buttonWidth;
    private final int buttonHeight;
    private final int buttonStart;
    private final int popupWidth;
    private final int popupHeight;

    BindingLayoutEnum(boolean title, int componentWidth, int componentHeight,
                      int buttonWidth, int buttonHeight, int buttonStart,
                      int popupWidth, int popupHeight) {
        this.title = title;
        this.componentWidth = componentWidth;
        this.componentHeight = componentHeight;
        this.buttonHeight = buttonHeight;
        this.buttonWidth = buttonWidth;
        this.buttonStart = buttonStart;
        this.popupWidth = popupWidth;
        this.popupHeight = popupHeight;
    }
}
