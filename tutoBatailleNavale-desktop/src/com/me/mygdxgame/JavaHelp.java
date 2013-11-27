package com.me.mygdxgame;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class JavaHelp implements UITrick{

	@Override
	public void showToast(CharSequence toastMessage, int toastDuration) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(new JFrame("Message"), toastMessage);

	}

	@Override
	public void showAlertBox(String alertBoxTitle, String alertBoxMessage,
			String alertBoxButtonText) {
		JOptionPane.showMessageDialog(new JFrame(alertBoxTitle), alertBoxMessage);

	}

	@Override
	public void openUri(String uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showMyList() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getConnectedWifi() {
		try {
			InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}

}
