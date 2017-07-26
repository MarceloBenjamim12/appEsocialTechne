package appTechne;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ReadyXML {
	
	public ReadyXML(){
		
	}
	
	public Document testReadFile() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("/resource/cargo.xml");
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			try {
				Document doc = docBuilder.parse(in);
				return doc;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}

}
