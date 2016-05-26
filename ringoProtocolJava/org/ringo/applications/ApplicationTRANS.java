package org.ringo.applications;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.Main;
import org.ringo.Utils;

public class ApplicationTRANS extends Application {
	
	
	FileOutputStream fos;
	String id_trans = null;
	String filename = null;
	int nummess = 0;
	int nummesslu = 0;
	
	int nofile = 0;

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
				File f = new File(filename);
				if(!f.exists()) {
					f.createNewFile();
				} 
				fos = new FileOutputStream(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nummess = Integer.parseInt(recu[7]);	
		}else{
			Main.entity.sender.send(s);
		}
	}
	
	public void transSEN(String s){
		
		String[] recu = s.split(" ");
		
		if(id_trans!=null && recu[4].equals(id_trans) && nummess>(nummesslu+1)){
			nummesslu++;
			try {
				fos.write(recu[7].getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.print("<" + appName + ">");
			System.out.println(renderDL(nummesslu+1,nummess));
			if(nummess==(nummesslu+1)){
				Main.app=null;
				System.out.print("<" + appName + ">");
				System.out.println("FIN DU TRANSFERT DE FICHIER");
			}
		}else{
			Main.entity.sender.send(s);
		}
		
	}
	
	public static String renderDL(int lu, int total){
		int n = (lu/total) * 10;
		String s = "[";
		for(int i = 0;i<n;i++){
			s+="=";
		}
		for(int i = 0;i<10-n;i++){
			s+=" ";
		}
		s+="] ("+(((double)lu)/total)*100.0+"%)";
		return s;
	}
	
	public static void transAnalyze(String req){
		
		String[] recu = req.split(" ");
		if(recu[3].equals("REQ")){
			if(Main.entity.messagesIds.contains(recu[1])){
				if(Main.app!=null && Main.app.getAppName().equals("TRANS")){
					((ApplicationTRANS) Main.app).nofile();
				}
			}else{
				transREQ(req);
			}
		}else if(recu[3].equals("ROK")){
			if(Main.app!=null && Main.app.getAppName().equals("TRANS")){
				((ApplicationTRANS) Main.app).transROK(req);
			}else{
				Main.entity.sender.send(req);
			}
			
		}else if(recu[3].equals("SEN")){
			if(Main.app!=null && Main.app.getAppName().equals("TRANS")){
				((ApplicationTRANS) Main.app).transSEN(req);
			}else{
				Main.entity.sender.send(req);
			}
		}
		
	}
	
	public void nofile(){
		nofile++;
		if(nofile>=Main.entity.nextAddresses.size()){
			Main.app=null;
			System.out.print("<TRANS>");
			System.out.println("FICHIER INTROUVABLE");
		}
	}
	
	public static void transREQ(String s){
		
		String[] recu = s.split(" ");
		try {
			File f = new File(recu[5]);
			if(f.exists() && !f.isDirectory()) { 
				
				byte[] filedata = Files.readAllBytes(Paths.get(recu[5]));
				int bytes = (int) f.length();
				int num_mess = (int)Math.ceil(bytes/462.0);
				
				String uid = Utils.uniqueId1();
				String id_trans = Utils.uniqueId1();
				String rok = "APPL " + uid + " TRANS### ROK "+id_trans+" "+String.format("%02d", recu[5].length())+" "+recu[5]+" "+String.format("%08d", num_mess );;
				Main.entity.sender.send(rok);
			
				for(int i = 0;i<num_mess;i++){
					uid = Utils.uniqueId1();
					int n = (((bytes - i*462)>462)?462:(bytes - i*462));
					byte[] data = Arrays.copyOfRange(filedata, i*462, i*462+n);
					String sen = "APPL " + uid + " TRANS### SEN "+id_trans+" "+String.format("%08d", i)+" "+String.format("%08d", n)+" "+new String(data, "UTF-8");;
					Main.entity.sender.send(sen);
				}
				
			}else{
				Main.entity.sender.send(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
