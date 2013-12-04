package com.me.mygdxgame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.Toast;

import chat.Network;

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
		//		appContext.startActivity(new Intent(this.appContext, MyListActivity.class));
	}

	@Override
	public int getConnectedWifi(){
//
//		int ssid = 0;
//		ConnectivityManager connManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		if (networkInfo.isConnected()) {
//			final WifiManager wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
//			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
//			if (connectionInfo != null) {
//				ssid = connectionInfo.getIpAddress();
//			}
//		}
		return -1;
	}
//	InetAddress getBroadcastAddress() throws IOException {
//		WifiManager wifi = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
//		DhcpInfo dhcp = wifi.getDhcpInfo();
//		// handle null somehow
//
//		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
//		byte[] quads = new byte[4];
//		for (int k = 0; k < 4; k++)
//			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
//		return InetAddress.getByAddress(quads);
//	}
//	public void test() throws IOException{
//		DatagramSocket socket = new DatagramSocket(Network.portUDP);
//		socket.setBroadcast(true);
//		
//		byte[] data=new byte[1024];
//		DatagramPacket packet = new DatagramPacket(data, data.length,
//				getBroadcastAddress(), Network.portUDP);
//		socket.send(packet);
//
//		byte[] buf = new byte[1024];
//		DatagramPacket packet1 = new DatagramPacket(buf, buf.length);
//		socket.receive(packet1);
//	}

}
