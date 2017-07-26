package appTechne.gui;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RenderUpload {

	JFrame janela;
	JLabel panelImg;
	
	public RenderUpload(){
		janela = new  JFrame("Fazendo Upload");
		panelImg = new JLabel();
	}
	
	public void iniciaUpload(){
		try{
			janela.setBounds(500, 400, 500, 400);
			janela.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			janela.getContentPane().setLayout(null);
			janela.setResizable(false);	
			
			File file = new File("src/main/java/img/upload.png");			
			Image img = ImageIO.read(file);				
			Image resizedImage = img.getScaledInstance(100, 100, 0);			
			panelImg.setIcon(new ImageIcon(resizedImage));	
			panelImg.setBounds(50, 10, 100, 100);
			
			janela.setSize(200,200);
			janela.getContentPane().add(panelImg);						
			
			janela.setVisible(true);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
