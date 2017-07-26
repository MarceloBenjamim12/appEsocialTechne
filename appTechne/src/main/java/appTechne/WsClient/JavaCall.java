package appTechne.WsClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class JavaCall {
	
	public JavaCall(){
		
	}
	
	
	
	public String javaCall(KeyStore ks, String xml, String nomeArquivo, String urlTransmissao){
		try{
			SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
	        HttpsURLConnection httpsConnection = null;
	        
	        InputStream cfv5 = getClass().getClassLoader().getResourceAsStream("certificados/acserproacfv5.crt");
	        InputStream icpsv5 = getClass().getClassLoader().getResourceAsStream("certificados/icpbrasilv5.crt");
	        InputStream prov4 =getClass().getClassLoader().getResourceAsStream("certificados/acserprov4.crt");
	        
	        CertificateFactory cf = CertificateFactory.getInstance("X.509");
	        
	        X509Certificate acserproacfv5 = (X509Certificate)cf.generateCertificate(cfv5);
	        X509Certificate icpbrasilv5 = (X509Certificate)cf.generateCertificate(icpsv5);
	        X509Certificate acserprov4 = (X509Certificate)cf.generateCertificate(prov4);
	        
	        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	        
	        KeyManagerFactory keyManagerFactory;
	        keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
	        
	        keyManagerFactory.init(ks ,("@Techne").toCharArray()); 
	        
	        
	        ks.setCertificateEntry("acserproacfv5", acserproacfv5);
	        ks.setCertificateEntry("icpbrasilv5", icpbrasilv5);
	        ks.setCertificateEntry("acserprov4", acserprov4);
	        
	        ks.load(null ,null);
	        
	        tmf.init(ks);
	        SSLContext sslContext = SSLContext.getInstance("TLS");
	        
	        sslContext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
	        
	        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	        
	        URL endpoint = new URL(urlTransmissao);
	        
	        httpsConnection = (HttpsURLConnection) endpoint.openConnection();
	        
	        httpsConnection.connect();
	        MimeHeaders headers = new MimeHeaders();
	        headers.addHeader("Content-Type", "text/xml");
	        

	        MessageFactory messageFactory = MessageFactory.newInstance();
	        SOAPMessage soapMessage = messageFactory.createMessage(headers , (new ByteArrayInputStream(xml.getBytes())));
	        SOAPPart soapPart = soapMessage.getSOAPPart();
	        
	        final SOAPElement stringToSOAPElement = stringToSOAPElement(xml);
	        
	        SOAPEnvelope envelope = soapPart.getEnvelope();
	        envelope.addNamespaceDeclaration("ns", "http://www.esocial.gov.br/schema/lote/eventos/envio/v1_1_0");
	        SOAPBody soapBody = envelope.getBody();
			headers.addHeader("SOAPAction", "http://www.esocial.gov.br/servicos/empregador/lote/eventos/envio/v1_1_0/ServicoEnviarLoteEventos/EnviarLoteEventos");

			soapMessage.saveChanges();
	        
	        SOAPMessage soapResponse = soapConnection.call(soapMessage, endpoint);
	        
	        Document xmlRespostaARequisicao= soapResponse.getSOAPBody().getOwnerDocument();
	        
	        StringWriter sw = new StringWriter();
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        Source sourceContent = soapResponse.getSOAPPart().getContent();
	        StreamResult result = new StreamResult(sw);
	        transformer.transform(sourceContent, result);
	        System.out.println(sw.toString());
	        System.out.println(soapResponse.getMimeHeaders());
	        System.out.println(sw.toString());
	        soapConnection.close();
	        
	        httpsConnection.disconnect();
	        
	        return sw.toString();
		}catch (Exception e) {
			System.out.println(e);
		}
		return null; 
	}
		
	private SOAPElement stringToSOAPElement(String xmlRequestBody) throws SOAPException, SAXException, IOException, ParserConfigurationException {

		// Load the XML text into a DOM Document
		final DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		builderFactory.setNamespaceAware(true);
		final InputStream stream = new ByteArrayInputStream(
				xmlRequestBody.getBytes());
		final Document doc = builderFactory.newDocumentBuilder().parse(stream);

		// Use SAAJ to convert Document to SOAPElement
		// Create SoapMessage
		final MessageFactory msgFactory = MessageFactory.newInstance();
		final SOAPMessage message = msgFactory.createMessage();
		final SOAPBody soapBody = message.getSOAPBody();

		// This returns the SOAPBodyElement that contains ONLY the Payload
		return soapBody.addDocument(doc);
	}
     
}
