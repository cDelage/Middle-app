package com.epsi.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "datas")
public class DataListDTO {

	@XmlElementWrapper(name ="data-content")
	@XmlElement(name="data")
	private List<KeyValueDTO> datas;
	
	public DataListDTO() {
	}

	public List<KeyValueDTO> getDatas() {
		return datas;
	}

	public void setDatas(List<KeyValueDTO> datas) {
		this.datas = datas;
	}
	
	
}
