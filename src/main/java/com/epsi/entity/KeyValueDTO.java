package com.epsi.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "data")
public class KeyValueDTO {

	@XmlElement(name="Identifiant")
	private long id_origin;
	
	@XmlElement(name ="Nom-Information")
	private String nom_information;
	
	@XmlElement(name ="Valeur-Information")
	private String valeurInformation;
	
	
	
	public KeyValueDTO(){
	}



	public long getId() {
		return id_origin;
	}



	public void setid_origin(long id_origin) {
		this.id_origin = id_origin;
	}



	public String getNom_information() {
		return nom_information;
	}



	public void setNom_information(String nom_information) {
		this.nom_information = nom_information;
	}



	public String getValue_information() {
		return valeurInformation;
	}



	public void setValeur_Information(String valeurInformation) {
		this.valeurInformation = valeurInformation;
	}

	

}
