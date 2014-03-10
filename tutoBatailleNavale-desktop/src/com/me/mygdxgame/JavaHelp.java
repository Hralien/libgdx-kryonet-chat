package com.me.mygdxgame;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import m4ges.controllers.UITrick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Input.Keys;




public class JavaHelp implements UITrick{


	@Override
	public void showToast(CharSequence toastMessage, int toastDuration,Stage stage) {
		//		JOptionPane.showMessageDialog(new JFrame("Message"), toastMessage);
		new Dialog("Some Text", new Skin(Gdx.files.internal("data/uiskin.json")), "dialog") {
			protected void result (Object object) {
			}
		}.text(toastMessage.toString()).button("Continuer", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
	}


	@Override
	public void showAlertBox(String alertBoxTitle, String alertBoxMessage,
			String alertBoxButtonText, Stage stage) {
		if(stage==null){
			System.err.println(alertBoxMessage);
		}
		else
			new Dialog(alertBoxTitle, new Skin(Gdx.files.internal("data/uiskin.json")), "dialog") {
			protected void result (Object object) {
			}
		}.text(alertBoxMessage).button(alertBoxButtonText, true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
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


	@Override
	public void testWifi() {
		// TODO Auto-generated method stub

	}



}
