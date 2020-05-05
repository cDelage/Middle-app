package com.epsi.entity;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "fileKeyValue")
public class FileKeyValueDTO {

	private String name;
	private String source;
	private String emmeteur;
	
	@XmlElementWrapper(name = "datas")
	@XmlElement(name="data")
	private List<KeyValueDTO> datas;
	private String statusLecture;
	private String statusEnvoi;
	private Date dateReception;
	private Date dateEnvoi;
	private String error;
	
	public FileKeyValueDTO() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public List<KeyValueDTO> getDatas() {
		return datas;
	}
	public void setDatas(List<KeyValueDTO> datas) {
		this.datas = datas;
	}

	public String getStatusLecture() {
		return statusLecture;
	}

	public void setStatusLecture(String statusLecture) {
		this.statusLecture = statusLecture;
	}

	public String getStatusEnvoi() {
		return statusEnvoi;
	}

	public void setStatusEnvoi(String statusEnvoi) {
		this.statusEnvoi = statusEnvoi;
	}

	public Date getDateReception() {
		return dateReception;
	}

	public void setDateReception(Date dateReception) {
		this.dateReception = dateReception;
	}

	public Date getDateEnvoi() {
		return dateEnvoi;
	}

	public void setDateEnvoi(Date dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}

	public String getEmmeteur() {
		return emmeteur;
	}

	public void setEmmeteur(String emmeteur) {
		this.emmeteur = emmeteur;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
