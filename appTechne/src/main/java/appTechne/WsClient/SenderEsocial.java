package appTechne.WsClient;

import org.w3c.dom.Document;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.client.utils.URIBuilder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import appTechne.Actions;
import appTechne.ReadyXML;
import appTechne.Objetos.Usuario;
import appTechne.gui.LoginController;

public class SenderEsocial {
	
	public SenderEsocial(){	
		
	}
	
	public void enviarProtocTransmissao(String path, String uri, Document xml, String nrINsc){
		ClientConfig config = new DefaultClientConfig();
		
		Actions action = new Actions();
		
		Client client = Client.create(config);
		
		WebResource webResource = client.resource(UriBuilder.fromUri(uri+"/api/rest/uml/hephaestus/TransmissaoEvento/carregaRetorno").build());
		
		MultivaluedMap formData = new MultivaluedMapImpl();
		
		ReadyXML rx = new ReadyXML();
		
		Document doc = rx.testReadFile();
		
		LoginController loginCtrl = new LoginController();
		
		Usuario usuario = loginCtrl.getLogin();
		
		formData.add("file", doc);
		formData.add("empregadorNrInsc", nrINsc);
		formData.add("username", usuario.getUsername());
		formData.add("passwd", usuario.getPassWd());
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);
		
		action.criaPasta(path, "retEsocial");
		
		saveFile(path, usuario, response.getEntity(String.class));
		
	}
	
	
	public void saveFile(String path, Usuario usuario, String resposta){
		try {
			Date dt = Calendar.getInstance().getTime();
			SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
			Timestamp tms= new Timestamp(dt.getTime());
			String dataHora = formato.format(tms);
			
			File file = new File(path+"/retEsocial/"+"EsoRet"+dataHora+usuario.getGuid()+".xml");	
			
			file.createNewFile();
			
			FileWriter writer = new FileWriter(path+"/retEsocial/"+"EsoRet"+dataHora+usuario.getGuid()+".xml");
			PrintWriter gravarArq = new PrintWriter(writer);
			
			gravarArq.printf(resposta);
			
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
