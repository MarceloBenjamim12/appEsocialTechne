package appTechne;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

public class CertificadosManager {
	
	public static final DERObjectIdentifier RESPONSAVEL = new DERObjectIdentifier("2.16.76.1.3.2");  
	public static final DERObjectIdentifier CNPJ = new DERObjectIdentifier("2.16.76.1.3.3");   
	public static final DERObjectIdentifier CPF = new DERObjectIdentifier("2.16.76.1.3.1");
	
	
	public CertificadosManager(){
		
	}

	
	public KeyStore carregarCertificados(){
		try{
			KeyStore ks = KeyStore.getInstance("Windows-MY");
			ks.load(null, "@Techne".toCharArray());
			return ks;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getCertificateByKeyStore(KeyStore ks){
		try {
			List<String> listaCertificados = new ArrayList<String>();
			Enumeration<String> aliasEnum;
			try {
				aliasEnum = ks.aliases();		
				while (aliasEnum.hasMoreElements()) {
					String aliasKey = (String) aliasEnum.nextElement();
					Certificate c = ks.getCertificate(aliasKey) ;
					if (ks.isKeyEntry(aliasKey)) {
						listaCertificados.add(aliasKey);
					}
				}
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String[] certs = new String[listaCertificados.size()];
			listaCertificados.toArray(certs);
			String response = (String) JOptionPane.showInputDialog(null, "Escolha o certificado para envio", "Opção certificados",
			        JOptionPane.PLAIN_MESSAGE, null, certs, certs[0]);
			
		
			Certificate cert = ks.getCertificate(response);
			if (cert instanceof X509Certificate) {
		      X509Certificate x509=(X509Certificate)cert;
		      String certCnpj = getCnpjCertificado(x509);
		      return certCnpj;
		    }
			
			return null;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public String getCnpjCertificado(X509Certificate certificate){
		Collection<?> alternativeNames;
		try {
			alternativeNames = X509ExtensionUtil.getSubjectAlternativeNames(certificate);
		 
	        for (Object alternativeName : alternativeNames) {  
	            if (alternativeName instanceof ArrayList) {  
	                ArrayList<?> listOfValues = (ArrayList<?>) alternativeName;  
	                Object value = listOfValues.get(1);  
	                if (value instanceof DERSequence) {  
	                    DERSequence derSequence = (DERSequence) value;  
	                    DERObjectIdentifier derObjectIdentifier = (DERObjectIdentifier) derSequence.getObjectAt(0);  
	                    DERTaggedObject derTaggedObject = (DERTaggedObject) derSequence.getObjectAt(1);  
	                    DERObject derObject = derTaggedObject.getObject();  
	  
	                    String valueOfTag = "";  
	                    if (derObject instanceof DEROctetString) {  
	                        DEROctetString octet = (DEROctetString) derObject;  
	                        valueOfTag = new String(octet.getOctets());  
	                    }   
	                    else if (derObject instanceof DERPrintableString) {  
	                        DERPrintableString octet = (DERPrintableString) derObject;  
	                        valueOfTag = new String(octet.getOctets());  
	                    }   
	                    else if (derObject instanceof DERUTF8String) {  
	                        DERUTF8String str = (DERUTF8String) derObject;  
	                        valueOfTag = str.getString();  
	                    }  
	                      
	                    if ((valueOfTag != null) && (!"".equals(valueOfTag))) {  
	                        if (derObjectIdentifier.equals(CNPJ)) {  
	                        	return "CNPJ: "+valueOfTag; 
	                        }  
	                        if (derObjectIdentifier.equals(CPF)) {  
	                        	return "CPF: "+valueOfTag; 
	                        }  
	                    }  
	                }  
	            } 
	        }
		} catch (CertificateParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return null;
	}
}
