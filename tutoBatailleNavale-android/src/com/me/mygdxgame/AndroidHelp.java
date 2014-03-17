package com.me.mygdxgame;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import m4ges.controllers.UITrick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.badlogic.gdx.scenes.scene2d.Stage;


public class AndroidHelp implements UITrick {
	Handler uiThread;
	Context appContext;


	public AndroidHelp(Context appContext) {
		uiThread = new Handler();
		this.appContext = appContext;
	}

	@Override
	public void showToast(final CharSequence toastMessage, int toastDuration,Stage stage) {
		uiThread.post(new Runnable() {
			public void run() {
				Toast.makeText(appContext, toastMessage, Toast.LENGTH_SHORT)
				.show();
			}
		});
	}



	@Override
	public void showAlertBox(final String alertBoxTitle, final String alertBoxMessage,
			final String alertBoxButtonText, Stage stage) {
		uiThread.post(new Runnable() {
			public void run() {
				new AlertDialog.Builder(appContext)
				.setTitle(alertBoxTitle)
				.setMessage(alertBoxMessage)
				.setNeutralButton(alertBoxButtonText,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
					}
				}).create().show();
			}
		});		
	}
	@Override
	public void openUri(String uri) {
		Uri myUri = Uri.parse(uri);
		Intent intent = new Intent(Intent.ACTION_VIEW, myUri);
		appContext.startActivity(intent);
	}


	@Override
	public void showMyList() {
	}

	@Override
	public String getMacAddress() {
		WifiManager wifiMan = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		String macAddr = wifiInf.getMacAddress();
		return macAddr;
	}


}
