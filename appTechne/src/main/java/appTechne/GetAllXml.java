package appTechne;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetAllXml {
	
	private File[] listOfFile;
	private String urlFile;
	//C:\\eSocial\\XML
	
	public GetAllXml(String url){
		this.urlFile = url;
	}
	
	public List<String> getXML(){
		System.out.println(urlFile);
		List<String> xmlNames = new ArrayList<String>();
		File folder = new File(urlFile);
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i < listOfFiles.length; i++){
			String filename = listOfFiles[i].getName();
			if(filename.endsWith(".xml")||filename.endsWith(".XML")){
				xmlNames.add(filename);
			}
		}
		return xmlNames;
	}
	
	public void setPath(String url){
		this.urlFile = url;
	}
	
	public File[] getXmlFiles(){
		return this.listOfFile;
	}
}
