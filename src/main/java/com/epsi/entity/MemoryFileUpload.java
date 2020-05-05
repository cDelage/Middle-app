package com.epsi.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MemoryFileUpload {

	private List<FileKeyValueDTO> filesStored;

	public MemoryFileUpload() {
		this.filesStored = new ArrayList<FileKeyValueDTO>();
	}

	public List<FileKeyValueDTO> getFilesStored() {
		return filesStored;
	}

	public void setFilesStored(List<FileKeyValueDTO> filesStored) {
		this.filesStored = filesStored;
	}
	
	public void addFile(FileKeyValueDTO file) {
		this.filesStored.add(file);
	}
	
}
