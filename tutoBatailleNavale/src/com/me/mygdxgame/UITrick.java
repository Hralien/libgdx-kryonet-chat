package com.me.mygdxgame;

public interface UITrick {
    public void showToast(CharSequence toastMessage, int toastDuration);
    public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText);
    public void openUri(String uri);
    public void showMyList();
	public int getConnectedWifi();
}
