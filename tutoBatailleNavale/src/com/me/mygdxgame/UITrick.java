package com.me.mygdxgame;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface UITrick {
    public void showToast(CharSequence toastMessage, int toastDuration);
    public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText);
    public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText, Stage stage);
    public void openUri(String uri);
    public void showMyList();
	public int getConnectedWifi();
}
