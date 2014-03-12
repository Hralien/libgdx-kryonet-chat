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

	@Override
	public void testWifi() {
		final String DEBUG_TAG = "UDPMessenger";
		final Integer BUFFER_SIZE = 4096;

		final String TAG = "multicastLock";
		final int MULTICAST_PORT = 12345;


		Thread receiverThread = null;
		
		Runnable receiver = new Runnable() {
			boolean receiveMessages = true;

			@Override
			public void run() {
				WifiManager wim = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
				if(wim != null) {
					MulticastLock mcLock = wim.createMulticastLock(TAG);
					mcLock.acquire();
				}

				byte[] buffer = new byte[BUFFER_SIZE];
				DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length);
				MulticastSocket rSocket;

				try {
					rSocket = new MulticastSocket(MULTICAST_PORT);
				} catch (IOException e) {
					Log.d(DEBUG_TAG, "Impossible to create a new MulticastSocket on port " + MULTICAST_PORT);
					showAlertBox("erreur", "Impossible to create a new MulticastSocket on port " + MULTICAST_PORT, "ok", null);
					e.printStackTrace();
					return;
				}

				while(receiveMessages) {
					try {
						rSocket.receive(rPacket);
					} catch (IOException e1) {
						Log.d(DEBUG_TAG, "There was a problem receiving the incoming message.");
						showAlertBox("erreur", "There was a problem receiving the incoming message.", "ok", null);

						e1.printStackTrace();
						continue;
					}

					if(!receiveMessages)
						break;

					byte data[] = rPacket.getData();
					int i;
					for(i = 0; i < data.length; i++)
					{
						if(data[i] == '\0')
							break;
					}

					String messageText;

					try {
						messageText = new String(data, 0, i, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						Log.d(DEBUG_TAG, "UTF-8 encoding is not supported. Can't receive the incoming message.");
						showAlertBox("erreur", "UTF-8 encoding is not supported. Can't receive the incoming message.", "ok", null);

						e.printStackTrace();
						continue;
					}

					try {
					} catch (IllegalArgumentException ex) {
						Log.d(DEBUG_TAG, "There was a problem processing the message: " + messageText);
						showAlertBox("erreur", "There was a problem processing the message: " + messageText, "ok", null);

						ex.printStackTrace();
						continue;
					}

				}
			}

		};

		if(receiverThread == null)
			receiverThread = new Thread(receiver);

		if(!receiverThread.isAlive())
			receiverThread.start();
	}
}
