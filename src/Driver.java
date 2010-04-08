import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import remote.obj.ProgramAuthHandler;

import cipher.ProKeyGen;

import UI.*;
import control.*;

public class Driver {
	
	public static int selfAuth(String serverPath) {
		try {
			ProKeyGen pkg = new ProKeyGen("common.jar");
			Registry reg = LocateRegistry.getRegistry(serverPath);
			ProgramAuthHandler pah = (ProgramAuthHandler)reg.lookup("ProgramAuthHandler");
			return pah.authProgram(pkg.getNo(), pkg.getProgramKey().getEncoded());
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(), "The common.jar is not found. Please don't rename the program.");
			e.printStackTrace();
			return 4;
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(new JFrame(), "The server is unreachable. Please check your network connection.");
			e.printStackTrace();
			return 8;
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(new JFrame(), "The server is unreachable or is not started yet. Please check your network connection.");
			e.printStackTrace();
			return 16;
		}
		
	}
	
	public static void main(String[] argv){
		String serverPath = null;
		if(argv.length != 0) 
			serverPath = argv[0];
		else 
			serverPath = "localhost";
		if (selfAuth(serverPath) != 0) {
			JOptionPane.showMessageDialog(new JFrame(), "The program is not the same.");
			System.exit(1);
		}
		
		//SoftCard sd = new SoftCard();
		Timer t = new Timer();
		Date now = new Date();
		Client.getInstance().setT(t);
		MainFrame mf = new MainFrame();
		//mf.loginPanel.enableAll();
		Task task = new Task( Task.PRE_AUTH);
		Client.getInstance().setMf(mf);
		t.schedule(task, now, 2000);
	}
}
