
package com.wl.tools.util.parse.controller.util;

import org.springframework.web.multipart.MultipartFile;

public class Archive {
	private String name;
	private String path;
	private MultipartFile[] files;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public MultipartFile[] getFiles() {
		return this.files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}
}
