package com.epsi.services;

import java.text.ParseException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.epsi.entity.KeyValueDTO;
import com.epsi.entity.DataListDTO;
import com.epsi.entity.FileKeyValueDTO;

public interface IFileReaderService {

	public void readCSVFile(String path, String emmeteur);
	
	public DataListDTO unmarshall(String path) throws InstantiationException, IllegalAccessException, JAXBException, SAXException;
	
	public void constructFile(String path, List<KeyValueDTO> datas, String type, String emmeteur) throws ParseException;
	
	public void readXMLFile(String path, String emmeteur);
	
	public void constructErrorFile(String emmeteur, String type, String error) throws ParseException;
	
	public void sendFileToGouv(FileKeyValueDTO file) throws Exception;
}
