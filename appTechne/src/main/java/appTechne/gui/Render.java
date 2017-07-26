package appTechne.gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import appTechne.Actions;
import appTechne.GetAllXml;

public class Render extends JPanel{
	
	static String path = "C:\\";
	public String urlTransmissao;
	public String urlConsulta;
	public String uriEsocial;
	
	JFileChooser chooser;
	GetAllXml xml;
	JList<String> myList;
	String selecionados;
	
	JFrame janela;
	JLabel panelImg;
	JButton buttonTransmitir;
	JButton buttonTransmitirTodos;
	JButton btnFolder;
	JScrollPane jScrollPane1;
	JTextArea txtFolder;
	JTextArea txtTrans;
	JTextArea txtCons;
	JLabel lblFolder;
	JLabel lblTrans;
	JLabel lblCons;
	String[] xmlFilesNames;
	
	
	public Render(){
		xml = new GetAllXml(path);
		janela = new JFrame("App eSocial Techne");
		panelImg = new JLabel();
		buttonTransmitir = new JButton();
		buttonTransmitirTodos = new JButton();
		btnFolder = new JButton();
		jScrollPane1  = new JScrollPane();
		myList = new JList<String>();
		txtFolder = new JTextArea();
		txtTrans = new JTextArea();
		txtCons = new JTextArea();
		lblFolder = new JLabel();
		lblTrans = new JLabel();
		lblCons = new JLabel();		
	}
	
	
	public void renderizar(String transmissao, String consulta, String uri) {
		urlTransmissao = transmissao;
		urlConsulta = consulta;
		uriEsocial = uri;
		
		try {			
			janela.setBounds(100, 100, 500, 400);
			janela.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			janela.getContentPane().setLayout(null);
			janela.setResizable(false);		
			
			File file = new File("src/main/java/img/Logo_Hephaestus_novo.png");			
			Image img = ImageIO.read(file);				
			Image resizedImage = img.getScaledInstance(270, 100, 0);			
			panelImg.setIcon(new ImageIcon(resizedImage));	
			panelImg.setBounds(40, 40, 330, 100);
			
			List<String> listXmlName = xml.getXML();			
			xmlFilesNames = new String[listXmlName.size()];
			listXmlName.toArray(xmlFilesNames);			
			myList = new JList<String>(xmlFilesNames);
			myList.setDragEnabled(true);
			myList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			myList.addListSelectionListener(TesteLista());
			
			jScrollPane1.setViewportView(myList);
			jScrollPane1.setBounds(320, 40, 320, 200);
			
			buttonTransmitir.setText("Transmitir");
			buttonTransmitir.setBounds(320, 260, 150, 25);
			buttonTransmitir.addActionListener(IniciaTransmissão());
			janela.add(buttonTransmitir);
			
			btnFolder.setText("Abrir");
			btnFolder.setBounds(570, 330, 70, 25);
			btnFolder.addActionListener(updateList());
			
			janela.add(btnFolder);
			
			buttonTransmitirTodos.setText("Transmitir Todos");
			buttonTransmitirTodos.setBounds(490, 260, 150, 25);
			buttonTransmitirTodos.addActionListener(TransmitirTudo());
			janela.add(buttonTransmitirTodos);
			
			lblFolder.setText("Diretorio XML");
			lblFolder.setBounds(40, 330, 100, 25);
			janela.add(lblFolder);
			
			txtFolder.setBounds(150, 330, 400, 25);
			txtFolder.setEditable(false);
			txtFolder.setText(path);
			janela.add(txtFolder);
			
			lblTrans.setText("Url Transmissão");
			lblTrans.setBounds(40, 365, 100, 25);
			janela.add(lblTrans);
			
			txtTrans.setBounds(150, 365, 480, 25);
			txtTrans.setEditable(false);
			txtTrans.setText(transmissao.substring(13));
			janela.add(txtTrans);
			
			lblCons.setText("Url de Consulta");
			lblCons.setBounds(40, 400, 100,25);	
			janela.add(lblCons);
			
			txtCons.setBounds(150, 400, 480, 25);
			txtCons.setEditable(false);
			txtCons.setText(consulta.substring(10));
			janela.add(txtCons);
			
			janela.setSize(700,550);
			janela.getContentPane().add(panelImg);
			janela.add(jScrollPane1);						
			
			janela.setVisible(true);	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ActionListener updateList() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e){
				chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("C:\\"));
			    chooser.setDialogTitle("Selecione o Diretorio");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    chooser.setAcceptAllFileFilterUsed(false);
			    //    
			    if (chooser.showOpenDialog(janela) == JFileChooser.APPROVE_OPTION) { 
			    	//System.out.println("getCurrentDirectory(): " +  chooser.getCurrentDirectory());
				    //System.out.println("getSelectedFile() : "  +  chooser.getSelectedFile());
				    path = chooser.getSelectedFile().getPath();
				    txtFolder.setText(path);
			    	xml.setPath(path);
			    	
			    	List<String> listXmlName = xml.getXML();
					xmlFilesNames = new String[listXmlName.size()];
					listXmlName.toArray(xmlFilesNames);			
					myList = new JList<String>(xmlFilesNames);	
					myList.setDragEnabled(true);
					myList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					myList.addListSelectionListener(TesteLista());
					jScrollPane1.setViewportView(myList);
					jScrollPane1.repaint();
			    }else {
			      System.out.println("No Selection ");
			    }
		     }
        };
	}
	
	
		
	private ListSelectionListener TesteLista() {
		return new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				selecionados = "";
				// TODO Auto-generated method stub			    
			    Object obj[ ] = myList.getSelectedValues();
			    for(int i = 0; i < obj.length; i++)
			    {
			    	selecionados += (String) obj[i]+",";			    	
			    }
			}
		};
	}
	
	private ActionListener IniciaTransmissão() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e){
				if(selecionados != null && selecionados.length() > 0){
					String[] xmlNomes = selecionados.split(",");
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int confirm = JOptionPane.showConfirmDialog(null, "Deseja enviar apenas os arquivos selecionado?, clique em \"Sim\", senão clique no botão \"Enviar Todos\"", "Escolha" , dialogButton);
					if(confirm == JOptionPane.YES_OPTION){
						Actions action = new Actions();
						if(urlTransmissao != null && urlConsulta != null || uriEsocial != null){
							action.transmitir(xmlNomes, path, urlTransmissao.substring(13) , urlConsulta.substring(10) , uriEsocial.substring(9));
						}else{
							JOptionPane.showMessageDialog(null, "O arquivo de configuração não possui url de transmissão ou de consulta");
						}
					}
				}else{
					JOptionPane.showMessageDialog(null, "Selecione ao menos um xml para envio");
				}
			}
		};
	}
	
	private ActionListener TransmitirTudo() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e){
				System.out.println("entrou");
				if(xmlFilesNames != null && xmlFilesNames.length > 0){
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int confirm = JOptionPane.showConfirmDialog(null, "Deseja enviar todos os arquivos?", "Escolha" , dialogButton);
					if(confirm == JOptionPane.YES_OPTION){
						Actions action = new Actions();
						if(urlTransmissao != null && urlConsulta != null || uriEsocial != null){
							action.transmitir(xmlFilesNames, path, urlTransmissao.substring(13) , urlConsulta.substring(10), uriEsocial.substring(9));
						}else{
							JOptionPane.showMessageDialog(null, "O arquivo de configuração não possui url de transmissão ou de consulta");
						}
					}
				}else{
					JOptionPane.showMessageDialog(null, "essa pasta não contem arquivos XMl");
				}
			}
		};
	}
	
}
