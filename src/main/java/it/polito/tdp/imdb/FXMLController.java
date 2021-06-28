/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {

    	Actor a = this.boxAttore.getValue();
    	if(a==null) {
    		this.txtResult.setText("Scegliere un attore");
    		return ;
    	}
    	if(this.model.getGrafo()==null) {
    		this.txtResult.setText("Creare il  grafo prima!");
    		return ;
    	}
    	List<Actor> attori= this.model.raggiungibili(a);
    	for(Actor aa: attori) {
    		this.txtResult.appendText(aa.toString()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	String gen = this.boxGenere.getValue();
    	if(gen==null) {
    		this.txtResult.setText("Scegliere un genere");
    		return ;
    	}
    	
    	this.model.creaGrafo(gen);
    	this.txtResult.setText("Grafo creato!\n# vertici: "+this.model.getNVertici()+"\n# archi: "+this.model.getNArchi());
        this.boxAttore.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    	int ng=0 ;
    	try {
    		ng = Integer.parseInt(this.txtGiorni.getText());
    	}catch(NumberFormatException ne) {
    		ne.printStackTrace();
    		return ;
    	}
    	if(this.model.getGrafo()==null) {
    		this.txtResult.setText("Creare il  grafo prima!");
    		return ;
    	}
    	int p=this.model.simula(ng);
    	this.txtResult.appendText("Pause: "+p+"\n");
    	for(Actor a : this.model.getIntervist()) {
    	    this.txtResult.appendText(a.getFirstName()+" "+a.getLastName()+" "+a.getGender()+"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxGenere.getItems().setAll(this.model.getGeneri());
    }
}
