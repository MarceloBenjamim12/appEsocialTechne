package appTechne;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import org.w3c.dom.Document;

import appTechne.WsClient.JavaCall;
import appTechne.WsClient.SenderEsocial;
import appTechne.gui.RenderUpload;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Actions{
	
	String myDocuments;
	RenderUpload renderUpload;
	
	public Actions(){
		renderUpload = new RenderUpload();
	}
	
	public String getMyDocuments(){
		try {
			Process p =  Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
			p.waitFor();
			
			InputStream in = p.getInputStream();
		    byte[] b = new byte[in.available()];
		    in.read(b);
		    in.close();

		    myDocuments = new String(b);
		    myDocuments = myDocuments.split("\\s\\s+")[4];
		    
		    return myDocuments;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void criarConfig(){
		try {
			String urlTrans = JOptionPane.showInputDialog("Digite a URL de transmissão");
			String urlCons = JOptionPane.showInputDialog("Digite a URL de consulta");
			String uriEsocial = JOptionPane.showInputDialog("Digite a URL do seu eSocial");
			
			File f = new File(myDocuments+"/eSocial.config");
			f.createNewFile();
			
			FileWriter writer = new FileWriter(myDocuments+"/eSocial.config");
			PrintWriter gravarArq = new PrintWriter(writer);
			
			gravarArq.printf("transmissao: "+ urlTrans +  "%n");
			gravarArq.printf("consulta: "+ urlCons +"%n");
			gravarArq.printf("eSocial: "+ uriEsocial +"%n");
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	public void transmitir(String[] listaXml, String path, String transmissao, String consulta, String eSocial){
		renderUpload.iniciaUpload();
		try {
			List<String> listaXMlRetorno = new ArrayList<String>();
			EstruturaXml estrutura = new EstruturaXml();
			File f;		
			CertificadosManager cm = new CertificadosManager();			
			KeyStore ks = cm.carregarCertificados();				
			String cert = cm.getCertificateByKeyStore(ks);
			this.criaPasta(path, "xmlProtoc");
			
			for(String xml : listaXml){
				try{
					f = new File(path+"/"+xml);
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					
					Document doc = dBuilder.parse(f);
					
					//XmlDoc pode ser nulo, isto quer dizer que o Xml está com o schema eSocial Errado
					String xmlDoc = estrutura.prepararString(doc, xml, cert);
					
					if(xmlDoc != null){
						/*JavaCall call = new JavaCall();
						listaXMlRetorno.add( call.javaCall(ks, xmlDoc, xml, transmissao) );*/
					}
					this.moverArquivo(path, f, xml, "xmlProtoc");
				}catch(Exception e){
					listaXMlRetorno.add("Erro não encontrado no arquivo "+xml+ e);
				}
			} 
			this.criaPasta(path, "protoc");
			
			Date dt = Calendar.getInstance().getTime();
			SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
			Timestamp tms= new Timestamp(dt.getTime());
			String dataHora = formato.format(tms);
			int id= 0;
			for(String retorno : listaXMlRetorno){
				
				File protocFolder = new File(path+"/protoc/"+"EsoProtoc"+dataHora+id+".xml");
				protocFolder.createNewFile();
				
				FileWriter writer = new FileWriter(path+"/protoc/"+"EsoProtoc"+dataHora+id+".xml");
				PrintWriter gravarArq = new PrintWriter(writer);
				
				gravarArq.printf(retorno);
				writer.close();
				id++;
			}
		/*}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		*/}catch (Exception e) {
			e.printStackTrace();
	    }
		
		verificaRetorno(path, eSocial);
	}
	
	public void moverArquivo(String path, File arquivo, String xml, String folderName){
		try {
			File fromFile = new File(path+"/"+folderName+"/"+xml);
			fromFile.createNewFile();
			InputStream in = new FileInputStream(arquivo);
		    OutputStream out = new FileOutputStream(fromFile); 
		    
		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		    	out.write(buf, 0, len);
		    }
		    in.close();
		    out.close();
		    arquivo.deleteOnExit(); 
		}catch(Exception e){
			e.printStackTrace();
		}		 
	}
	
	public void criaPasta(String path, String nomeFolder){
		File folder = new File(path+"/"+nomeFolder);
		if(!folder.exists() && !folder.isDirectory()){
			new File(path+"/"+nomeFolder).mkdir();
			folder = new File(path+"/"+nomeFolder);
		}		
	}
	
	public void verificaRetorno(String path, String eSocial){
		SenderEsocial send = new SenderEsocial();
		
		File directory = new File(path+"/protoc/");
		File[] contents = directory.listFiles();
		for ( File f : contents) {
		  send
		}
	}
	
	
}
