package appTechne.gui;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import appTechne.Objetos.Usuario;

public class LoginController {

	JTextField userField;
	JTextField passField;
	JPanel myPanel;
	
	public LoginController(){		
		userField = new JTextField();
		passField = new JTextField();
		
		myPanel = new JPanel();
		myPanel.add(new JLabel("UserName: "));
		myPanel.add(userField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Password:"));
		myPanel.add(passField);		
	}
	
	public Usuario getLogin(){
		int result = JOptionPane.showConfirmDialog(null, myPanel, "Preenche o login e senha para inicio da transmissão", JOptionPane.OK_CANCEL_OPTION);
		Usuario usuario = new Usuario();
		
		usuario.setUsername(userField.getText());
		usuario.setPassWd( passField.getText());
		
		return usuario;
	}
	
	
}
