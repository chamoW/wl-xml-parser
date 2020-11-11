package com.wl.tools.util.parse.controller.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	private final Logger logger = LoggerFactory.getLogger(Util.class);
	private static final String UPLOADED_FOLDER = "C://temp//";
	private List<String> prop;

	public Util() {
		this.prop = new ArrayList<>();
		// this.prop.add("BankId");
		// this.prop.add("AppId");
		// this.prop.add("ComponentId");
		// this.prop.add("ServerId");
		// this.prop.add("UserIP");
		this.prop.add("UserId");
		this.prop.add("DocId");
		this.prop.add("DocType");
		this.prop.add("PersonId");
		this.prop.add("EventCode");
		this.prop.add("UserBehavior");
		this.prop.add("SessionId");
		this.prop.add("ErrorCode");
		// this.prop.add("Elapstime");
		this.prop.add("TimeStamp");
		this.prop.add("RequestEventBody");
		this.prop.add("ResponseEventBody");
		// this.prop.add("Amount");
		// this.prop.add("Data01");
		// this.prop.add("Data02");
		// this.prop.add("Data03");
		// this.prop.add("Data04");
		// this.prop.add("Data05");
		// this.prop.add("Data08");
		// this.prop.add("Data09");
		// this.prop.add("Data10");
		this.prop.add("DescError");
		this.prop.add("CauseError");
	}

	public List<String> analizeXml(byte[] dataFile) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new ByteArrayInputStream(dataFile));

		return parseData(doc);
	}

	/**
	 * @author wlopez
	 * @since Oct 1, 2020
	 * @param data
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	private List<List<String>> analizeXml(List<String> data)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		List<List<String>> listMap = new ArrayList<>();

		for (String line : data) {
			Document doc = dBuilder.parse(new InputSource(new StringReader(line)));

			listMap.add(parseData(doc));
		}

		return listMap;

	}

	/**
	 * @author wlopez
	 * @since Oct 1, 2020
	 * @param cabecera
	 * @param valores
	 * @param extras
	 * @param evencode
	 * @param doc
	 * @return
	 */
	private List<String> parseData(Document doc) {

		List<String> cabeceraValor = new ArrayList<>();

		// List<String> cabecera = new ArrayList<>();
		// List<String> valores = new ArrayList<>();
		// List<String> extras = new ArrayList<>();

		String evencode = "";

		doc.getDocumentElement().normalize();

		logger.info("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("GLTDataImpl");

		logger.info("----------------------------");

		Node nNode = nList.item(0);

		logger.info("\nCurrent Element :" + nNode.getNodeName());
		if (nNode.getNodeType() == 1) {
			Element eElement = (Element) nNode;

			for (String nodeConfig : prop) {
				String value = "";
				Node node = eElement.getElementsByTagName(nodeConfig).item(0);
				if (node != null) {
					value = node.getTextContent();
					if ("EventCode".equalsIgnoreCase(nodeConfig)) {
						evencode = value;
					} else if ("RequestEventBody".equalsIgnoreCase(nodeConfig)
							|| "ResponseEventBody".equalsIgnoreCase(nodeConfig)) {
						value = value.replace("|br|", "\n");

						if (value.contains("cookie:")) {
							String value1 = value.substring(0, value.indexOf("cookie:"));
							String value2 = value.substring(value.indexOf("cookie:"));
							value2 = value2.substring(value2.indexOf("]"));

							value = value1 + value2;

						}

						if (value.contains("<?xml")) {
							try {
								org.jdom2.Document docXML = new SAXBuilder().build(new StringReader(value));
								String prettyXML = new XMLOutputter(Format.getPrettyFormat()).outputString(docXML);
								value = StringEscapeUtils.escapeXml(prettyXML);
							} catch (JDOMException | IOException e) {
								e.printStackTrace();
							}
						} else if (value.startsWith("[ResponseBody:")) {

							value = extractedJson(value, "ResponseBody");

						} else if (value.contains("[RequestBody:")) {

							String value1 = value.substring(0, value.indexOf("[RequestBody:"));
							String value2 = value.substring(value.indexOf("[RequestBody:"));

							value2 = extractedJson(value2, "RequestBody");

							value = value1 + value2;

						}

					}

				} else {
					value = "----------------------------";
				}

				cabeceraValor.add(value);

//				logger.info(nodeConfig + ": " + value);
			}

			cabeceraValor.add("aditional-" + evencode);
		}
		return cabeceraValor;
	}

	/**
	 * @author wlopez
	 * @since Oct 12, 2020
	 * @param value
	 * @return
	 */
	private String extractedJson(String value, String tag) {
		value = value.substring(0, value.length() - 2);
		value = value.replace("[" + tag, "{\"" + tag + "\"");
		value = value + "}";
		value = value.replace("\"{\\n", "{");
		value = value.replace("\\n", "");
		value = value.replace("\\", "");
		value = value.replace("\"}\"", "\"}");

		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(value, Object.class);
			value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			logger.info("value JSON: " + value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return value;
	}

	public List<List<String>> saveUploadedFiles(List<MultipartFile> files)
			throws ParserConfigurationException, SAXException, IOException {
		MultipartFile file = (MultipartFile) files.get(0);

		logger.info("file: " + file);

		List<String> data = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {

				logger.info("line1: " + line);

				if (line.contains("<GLTDataImpl>")) {
					line = line.substring(line.indexOf("<GLTDataImpl>"));

					logger.info("line2: " + line);

					data.add(line);
				}
			}

			return analizeXml(data);
		}

		// return analizeXml(bytes);
	}

	public List<String> formatTable(List<List<String>> listWithData) {
		StringBuilder extras = new StringBuilder();
		StringBuilder table = new StringBuilder();
		table.append("<div class='table-responsive-sm'>");
		table.append("<table class='table-sm  table-bordered table-hover table-format' >");
		table.append("<thead class='thead-light' >");
		table.append("<tr>");

		for (String header : prop) {
			table.append("<th scope='col'>").append(header).append("</th>");
		}
		table.append("</tr>");
		table.append("</thead>");

		table.append("<tbody>");
		listWithData.stream().forEach(listItem -> {
			table.append("<tr>");
			listItem.stream().forEach(value -> {

				if (value.contains("aditional-")) {
					String extraValue = value.split("-")[1];
					extras.append("\t<b>").append(extraValue).append("</b>");
				} else {
					// logger.info("value in table: " + value);

					if (value.contains("xml")) {
						table.append("<td class='xml-format'>");
						table.append(value);
						table.append("</td>");

					} else if (value.contains("ResponseBody") || value.contains("RequestBody")) {
						table.append("<td class='json-format'>");
						table.append(value);
						table.append("</td>");

					} else {
						table.append("<td>");
						table.append(value);
						table.append("</td>");
					}

				}
			});

			table.append("</tr>");
		});

		table.append("</tbody>");
		table.append("</table>");
		table.append("</div>");

		List<String> dataReturn = new ArrayList<>();
		dataReturn.add(table.toString());
		dataReturn.add(extras.toString());

		return dataReturn;
	}

	public List<String> formatAccordion(List<List<String>> listWithData) {
		StringBuilder extras = new StringBuilder();
		StringBuilder accordion = new StringBuilder();
//		table.append("<table border='1' class='table-format' >");
//		table.append("<tr>");
//
//		for (String header : prop) {
//			table.append("<th>").append(header).append("</th>");
//		}
//		table.append("</tr>");

		accordion.append("<div class='accordion' id='accordionExample'>");
		listWithData.stream().forEach(listItem -> {
			accordion.append("<div class='card'>");
			accordion.append("<div class='card-header' id='headingOne'>");
			accordion.append("<h2 class='mb-0'>");
			accordion.append(
					"<button class='btn btn-link btn-block text-left' type='button' data-toggle='collapse' data-target='#collapseOne' aria-expanded='true' aria-controls='collapseOne'>");
			accordion.append("Collapsible Group Item #1");
			accordion.append("</button>");
			accordion.append("</h2>");
			accordion.append("</div>");
			accordion.append(
					"<div id='collapseOne' class='collapse' aria-labelledby='headingOne' data-parent='#accordionExample'>");
			accordion.append("<div class='card-body'>");

			listItem.stream().forEach(value -> {

//				if (value.contains("aditional-")) {
//					String extraValue = value.split("-")[1];
//					extras.append("\t<b>").append(extraValue).append("</b>");
//				} else {
				// logger.info("value in table: " + value);

				if (value.contains("xml")) {
					accordion.append("<td class='xml-format'>");
					accordion.append(value);
					accordion.append("</td>");

				} else if (value.contains("ResponseBody") || value.contains("RequestBody")) {
					accordion.append("<td class='json-format'>");
					accordion.append(value);
					accordion.append("</td>");

				} else {
					accordion.append("<td>");
					accordion.append(value);
					accordion.append("</td>");
				}

				// }
			});

			accordion.append(" </div>");
			accordion.append("</div>");
			accordion.append("</div>");
			accordion.append("</div>");
		});

		accordion.append("</div>");

		List<String> dataReturn = new ArrayList<>();
		dataReturn.add(accordion.toString());
		dataReturn.add(extras.toString());

		return dataReturn;
	}
}
