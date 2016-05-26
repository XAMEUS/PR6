package org.ringo.applications;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.Main;
import org.ringo.Utils;

public class ApplicationTRANS extends Application {
	
	
	FileOutputStream fos;
	String id_trans = null;
	String filename = null;
	int nummess = 0;
	int nummesslu = 0;

	public ApplicationTRANS() {
		this.appName = "TRANS";
	}

	@Override
	public void prompt() {
		String s = "";
		System.out.print("<" + appName + ">");
		System.out.println("Requete de fichier : Quelle est le nom du fichier que vous voulez recevoir ?");
		System.out.print("<" + appName + ">");
		s = Main.sc.nextLine().replaceAll("\n", "");
		action(s);
		
	}
	
	@Override
	public void action(String s) {
		filename=s;
		String uid = Utils.uniqueId1();
		String send = "APPL " + uid + " TRANS### REQ "+String.format("%02d", s.length())+" "+s;
		Main.entity.sender.send(send);
	}

	public void transROK(String s){
	
		String[] recu = s.split(" ");
		
		if(recu[6].equals(filename)){
				id_trans = recu[4];
				try {
					fos = new FileOutputStream(filename);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nummess = Integer.parseInt(recu[7]);		
			}
	}
	
	public void transSEN(String s){
		
		String[] recu = s.split(" ");
		
		if(id_trans!=null){
			if(recu[4].equals(id_trans) && nummess>nummesslu){
				nummesslu++;
				try {
					fos.write(recu[7].getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.print("<" + appName + ">");
				System.out.println(renderDL(nummesslu,nummess));
				if(nummess==nummesslu){
					Main.app=null;
					System.out.print("<" + appName + ">");
					System.out.println("FIN DU TRANSFERT DE FICHIER");
				}
			}
		}
		
	}
	
public void transREQ(String s){
		
		String[] recu = s.split(" ");
		if(Main.entity.messagesIds.contains(recu[1])){
			Main.app=null;
			System.out.print("<" + appName + ">");
			System.out.println("FICHIER INTROUVABLE");
		}
	}
	
	public static String renderDL(int lu, int total){
		int n = (lu/total) * 10;
		String s = "[";
		s+=new String(new char[n]).replace("\0", "▓");
		s+=new String(new char[total-n]).replace("\0", " ");
		s+="] ("+(((double)lu)/total)*100.0+"%)";
		return s;
	}
	
	public static void transAnalyze(String req){
		
		String[] recu = req.split(" ");
		if(recu[3].equals("REQ")){
			if(Main.app!=null && Main.app.getAppName().equals("TRANS")){
				((ApplicationTRANS) Main.app).transREQ(req);
			}
		}else if(recu[3].equals("ROK")){
			if(Main.app!=null && Main.app.getAppName().equals("TRANS")){
				((ApplicationTRANS) Main.app).transROK(req);
			}
			
		}else if(recu[3].equals("SEN")){
			if(Main.app!=null && Main.app.getAppName().equals("TRANS")){
				((ApplicationTRANS) Main.app).transSEN(req);
			}
		}
		
	}

}
