package appTechne;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import appTechne.gui.Render;

public class AppInicio {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader lerArq;
		FileReader read;
		Actions action = new Actions();
		String myDocuments = action.getMyDocuments();
		String texto;
		String urlTransmissao = "";
		String urlConsulta = "";
		String uriEsocial = "";
		
		try {
			read = new FileReader(myDocuments+"/eSocial.config");		
			lerArq = new BufferedReader(read);	
			while((texto  = lerArq.readLine()) != null){
				String linha = texto;
				if(linha.contains("transmissao")){
					urlTransmissao = linha;
				}else if(linha.contains("consulta")){
					urlConsulta = linha;
				}else if(linha.contains("eSocial")){
					uriEsocial = linha;
				}
			}
		} catch (FileNotFoundException e) {
			action.criarConfig();			
			try{
				read = new FileReader(myDocuments+"/eSocial.config");
				lerArq = new BufferedReader(read);	
				while((texto  = lerArq.readLine()) != null){
					String linha = texto;
					if(linha.contains("transmissao")){
						urlTransmissao = linha;
					}else if(linha.contains("consulta")){
						urlConsulta = linha;
					}else if(linha.contains("eSocial")){
						uriEsocial = linha;
					}
				}
			}catch(FileNotFoundException ex){
				System.out.println(ex.getMessage());
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		Render render = new Render();
		render.renderizar(urlTransmissao, urlConsulta, uriEsocial);
	}

}
