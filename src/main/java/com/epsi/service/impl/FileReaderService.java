package com.epsi.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.epsi.entity.KeyValueDTO;
import com.epsi.entity.DataListDTO;
import com.epsi.entity.FileKeyValueDTO;
import com.epsi.entity.MemoryFileUpload;
import com.epsi.services.IFileReaderService;
import com.opencsv.CSVReader;

@Component
public class FileReaderService implements IFileReaderService {

	private static final String SENDING_PROCESS_START = "Sending process start";
	private static final String LOG_FAILED_SEND_TO_GOUV = "Failed to send %FILE% to gouv : ";
	private static final String FAILED_TO_READ_XML = "Failed to read xml file cause : {}";
	private static final String FAILED_TO_CONSTRUCT_ERROR_FILE = "Failed to construct error file : {}";
	private static final String NA = "NA";
	private static final String ERROR = "ERROR";
	private static final String CATCH_EXCEPTION_DURING_READ_CSV = "Catch exception during read CSV : ";
	private static final String XML = "XML";
	private static final String CSV = "CSV";
	private static final String WAIT = "WAIT";
	private static final String DONE = "DONE";
	private static final String LOG_START_CSV = "CSV Parser reader process start for : {}";
	private static final Logger LOG = LogManager.getLogger(FileReaderService.class);

	private SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");

	@Autowired
	private MemoryFileUpload memoryFileUpload;

	@Override
	public void readCSVFile(String path, String emmeteur) {
		LOG.info(LOG_START_CSV, path);

		try {
			Reader reader = Files.newBufferedReader(Paths.get(path));
			CSVReader csvReader = new CSVReader(reader);
			List<KeyValueDTO> datas = new ArrayList<KeyValueDTO>();

			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				KeyValueDTO data = new KeyValueDTO();
				data.setid_origin(Long.valueOf(nextRecord[0]));
				data.setNom_information(nextRecord[1]);
				data.setValeur_Information(nextRecord[2]);
				datas.add(data);
			}
			csvReader.close();
			constructFile(path, datas, CSV, emmeteur);

		} catch (Exception exception) {
			LOG.error(CATCH_EXCEPTION_DURING_READ_CSV, exception);
			try {
				constructErrorFile(emmeteur, CSV, exception.getLocalizedMessage());
			} catch (ParseException e) {
				LOG.error(FAILED_TO_CONSTRUCT_ERROR_FILE, e);
			}
		}

	}

	@Override
	public void readXMLFile(String path, String emmeteur) {
		try {
			List<KeyValueDTO> datas = unmarshall(path).getDatas();
			constructFile(path, datas, XML, emmeteur);
		} catch (Exception e) {
			LOG.error(FAILED_TO_READ_XML, e);
			try {
				constructErrorFile(emmeteur, XML, e.getLocalizedMessage());
			} catch (ParseException exception) {
				LOG.error(FAILED_TO_CONSTRUCT_ERROR_FILE, exception);
			}
		}
	}

	@Override
	public void constructErrorFile(String emmeteur, String type, String error) throws ParseException {
		FileKeyValueDTO fileDataDTO = new FileKeyValueDTO();
		String nowString = formatter.format(new java.util.Date());
		Date now = formatter.parse(nowString);
		fileDataDTO.setEmmeteur(emmeteur);
		fileDataDTO.setStatusLecture(ERROR);
		fileDataDTO.setStatusEnvoi(NA);
		fileDataDTO.setDateReception(now);
		fileDataDTO.setError(error);
		memoryFileUpload.addFile(fileDataDTO);
	}

	@Override
	public void constructFile(String path, List<KeyValueDTO> datas, String type, String emmeteur)
			throws ParseException {
		FileKeyValueDTO fileDataDTO = new FileKeyValueDTO();
		String nowString = formatter.format(new java.util.Date());
		Date now = formatter.parse(nowString);
		fileDataDTO.setDatas(datas);
		fileDataDTO.setName(new File(path).getName());
		fileDataDTO.setEmmeteur(emmeteur);
		fileDataDTO.setSource(path);
		fileDataDTO.setStatusLecture(DONE);
		fileDataDTO.setStatusEnvoi(WAIT);
		fileDataDTO.setDateReception(now);
		memoryFileUpload.addFile(fileDataDTO);
		try {
			sendFileToGouv(fileDataDTO);
		}catch(Exception e) {
			LOG.error(LOG_FAILED_SEND_TO_GOUV, fileDataDTO.getName());
			fileDataDTO.setStatusEnvoi(ERROR);
		}
		
	}

	@Override
	public DataListDTO unmarshall(String path)
			throws InstantiationException, IllegalAccessException, JAXBException, SAXException {
		JAXBContext jaxbContext = JAXBContext.newInstance(DataListDTO.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		File file = new File(path);
		DataListDTO datas = (DataListDTO) jaxbUnmarshaller.unmarshal(file);
		return datas;
	}

	/**
	 * Send file to gouvernement
	 * 
	 * @param file
	 * @throws Exception
	 */
	@Override
	public void sendFileToGouv(FileKeyValueDTO file) throws Exception {
		LOG.info(SENDING_PROCESS_START);
		
		URL url = new URL("http://localhost:2222/api/gouv/all");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		JSONObject jsonReq = new JSONObject();
		StringBuilder req = new StringBuilder("{all:[");
		for(KeyValueDTO data : file.getDatas()) {
			req.append(new JSONObject(data).toString());
			req.append(",");
		}
		req.append("]}");
		LOG.info("Request : {}", req);
		OutputStream os = conn.getOutputStream();
		os.write(req.toString().getBytes());
		os.flush();
		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();

	}
}
