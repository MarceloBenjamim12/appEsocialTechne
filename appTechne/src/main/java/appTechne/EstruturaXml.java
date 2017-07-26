package appTechne;

import java.io.StringWriter;
import java.security.KeyStore;
import java.security.cert.Certificate;

import javax.swing.JOptionPane;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import appTechne.Enums.EvtEnums;
import appTechne.Objetos.IdeEmpregador;
import appTechne.WsClient.JavaCall;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

public class EstruturaXml {

	public EstruturaXml(){
		
	}
	
	public String passarXMLParaString(Document xml, int espacosIdentacao){
		try {
	        //set up a transformer
	        TransformerFactory transfac = TransformerFactory.newInstance();
	        transfac.setAttribute("indent-number", new Integer(espacosIdentacao));
	        Transformer trans = transfac.newTransformer();
	        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);
	        DOMSource source = new DOMSource(xml);
	        trans.transform(source, result);
	        String xmlString = sw.toString();
	        return xmlString;
	    }catch (TransformerException e) {
	        e.printStackTrace();
	        System.exit(0);
	    }
		return null;
	}
	
	public String prepararString(Document xml, String nomeArquivo, String cert){
		String requestSoap = "";
		String evtNamespace = getEvtNamespace(xml, nomeArquivo);
		IdeEmpregador empregador = getIdeEmpregador(xml, nomeArquivo);
		
		String tpInsc = "";
		String nrInsc = "";
		if(cert.contains("CNPJ")){
			tpInsc = "1";
			nrInsc = cert.substring(6);
		}else if(cert.contains("CPF")){
			tpInsc = "2";
			nrInsc = cert.substring(4);
		}
				
		
		if(evtNamespace != null){
			
			Integer grupo = getGrupo(evtNamespace);
			String idEvento = getIdEvento(xml, evtNamespace, nomeArquivo);
			if(empregador.getNrInsc() != null && empregador.getTpInsc() != null && grupo != null && idEvento != null){
				
				String xmlString = passarXMLParaString(xml, 4);
				xmlString = prepararXML(xmlString, evtNamespace);
				
				requestSoap += "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n";
				requestSoap += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" targetNamespace=\""+evtNamespace+"\" xmlns=\"http://www.esocial.gov.br/schema/lote/eventos/envio/v1_1_0\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:esocial=\"http://www.esocial.gov.br/schema/lote/eventos/envio/v1_1_0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"> \n";
				requestSoap += "<soapenv:Header ></soapenv:Header> \n";
		        requestSoap += "<soapenv:Body > \n";
		        requestSoap += "<envioLoteEventos> \n";
		        requestSoap += "<!--Optional:--> \n";
		        requestSoap += "<loteEventos> \n";
		        requestSoap += "<eSocial xmlns=\"http://www.esocial.gov.br/schema/lote/eventos/envio/v1_1_0\" xmlns:p=\""+evtNamespace+"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\""+evtNamespace+"\"> \n";	        
		        requestSoap += "<envioLoteEventos grupo=\""+grupo+"\"> \n";
		        requestSoap += "<ideEmpregador> \n";
		        requestSoap += "<tpInsc>"+empregador.getTpInsc()+"</tpInsc> \n";
		        requestSoap += "<nrInsc>"+empregador.getNrInsc()+"</nrInsc> \n";
		        requestSoap += "</ideEmpregador> \n";
		        requestSoap += "<ideTransmissor> \n";
		        requestSoap += "<tpInsc>"+tpInsc+"</tpInsc> \n";
		        requestSoap += "<nrInsc>"+nrInsc+"</nrInsc> \n";
		        requestSoap += "</ideTransmissor> \n";
		        requestSoap += "<eventos> \n";
		        requestSoap += "<evento Id=\""+idEvento+"\"> \n";
		        requestSoap += xmlString;
		        requestSoap += "</loteEventos> \n";
		        requestSoap += "</envioLoteEventos> \n";
		        requestSoap += "</soapenv:Body> \n";
		        requestSoap += "</soapenv:Envelope> \n";
		        
		        System.out.println(requestSoap);
		        
				return requestSoap;
			}else{
				JOptionPane.showMessageDialog(null, "Informações insuficiente do xml para envio: (namespace incorreto ou sem informação de empregador)");
				return null;
			}
		}else{
			JOptionPane.showMessageDialog(null, "A tag eSocial está sem o namespace do Evento, Verifique");
			return null;
		}
        
	}
	
	public String getEvtNamespace(Document xml, String nomeArquivo){
		try{
			String evtNameSpace = null;
			NodeList dadosNodeRetorno = xml.getElementsByTagName("eSocial");
			Node currentItem = dadosNodeRetorno.item(0);
			NamedNodeMap attributes = currentItem.getAttributes();
			if (attributes != null)
		    {
		        for (int i = 0; i < attributes.getLength(); i++)
		        {
		            Node node = attributes.item(i);
		            if (node.getNodeType() == Node.ATTRIBUTE_NODE)
		            {
		                String name = node.getNodeName();
		                if(name == "xmlns"){
		                	evtNameSpace = node.getNodeValue();
		                }
		            }
		        }
		    }
			return evtNameSpace;
		}catch(NullPointerException e){
			JOptionPane.showMessageDialog(null, "Não Existe a Tag eSocial no xml "+ nomeArquivo + ", Verifique");
		}
		
		return null;
	}
	
	public Integer getGrupo(String namespace){		
		String evt = getNameEvt(namespace);
		
		Integer grupo = EvtEnums.getSchemaByEvt(evt);
		
		return grupo;
	}
	
	public String getNameEvt(String namespace){
		int posEvt = namespace.indexOf("/evt/");
		String evt = namespace.substring(posEvt + 5);
		posEvt = evt.indexOf("/");
		evt = evt.substring(0, posEvt);
		
		return evt;
	}
	
	
	public IdeEmpregador getIdeEmpregador(Document xml, String nomeArquivo){
		IdeEmpregador empregador = new IdeEmpregador();
		try{
			NodeList tagEmpregador = xml.getElementsByTagName("ideEmpregador");
			NodeList dadosEmpregador = tagEmpregador.item(0).getChildNodes();
			for(int i = 0; i < dadosEmpregador.getLength(); i++){
				Node tagInfo = dadosEmpregador.item(i);
				String tagValor = tagInfo.getTextContent();
				
				if(tagInfo.getNodeName().equals("tpInsc")){
					empregador.setTpInsc(tagValor);
				}else if(tagInfo.getNodeName().equals("nrInsc")){
					empregador.setNrInsc(tagValor);
				}
			}
			return empregador;
		}catch(NullPointerException e){
			JOptionPane.showMessageDialog(null, "Não Existe a Tag ideEmpregador no xml "+ nomeArquivo + ", Verifique");
			return null;
		}
	}
	
	public String prepararXML(String xml, String evt){
		String evento = getNameEvt(evt);
		int posInit = xml.indexOf("<"+evento+">");
		int posFinalEvento = xml.indexOf("</"+evento+">");
		int posFinal =  xml.indexOf("</eSocial>");
		int cont = ("</"+evento+">").length();
		String xmlRetorno =  xml.substring(posInit, posFinalEvento+cont);		
		xmlRetorno += "</evento> \n";
		xmlRetorno += "</eventos> \n";
		xmlRetorno += "</envioLoteEventos> \n";
		xmlRetorno += "</eSocial> \n";
		xmlRetorno += xml.substring(posFinalEvento+cont+1, posFinal);
		return xmlRetorno;
	}
	
	public String getIdEvento(Document xml, String namespace, String nomeArquivo){
		try{
			NodeList evtTag = xml.getElementsByTagName("Id");
			Node node = evtTag.item(0);
			
			return node.getTextContent();
		}catch(NullPointerException e){
			JOptionPane.showMessageDialog(null, "Não Existe a Id do evento no xml "+ nomeArquivo + ", Verifique");
			return null;
		}
	}
}
