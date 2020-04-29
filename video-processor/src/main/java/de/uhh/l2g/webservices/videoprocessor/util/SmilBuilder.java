package de.uhh.l2g.webservices.videoprocessor.util;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uhh.l2g.webservices.videoprocessor.model.CreatedVideo;

/**
 * Builds a SMIL (XML) file for adaptive Streaming
 */
public class SmilBuilder {
	
	/**
	 * Builds a SMIL (xml) file for adaptive Streaming
	 * @param filePath the file path where the SMIL file will be created
	 * @param videos the videos which will be listed in the file for adaptive streaming
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void buildSmil(String filePath, List<CreatedVideo> videos) throws ParserConfigurationException, TransformerException {
		buildSmil(filePath, videos, 0, 0);
	}

	/**
	 * Builds a SMIL (xml) file for adaptive Streaming
	 * @param filePath the file path where the SMIL file will be created
	 * @param videos the videos which will be listed in the file for adaptive streaming
	 * @param maxHeight the max height of the videos used for building the SMIL
	 * @param maxBitrate the max bitrate of the videos used for building the SMIL
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void buildSmil(String filePath, List<CreatedVideo> videos, long maxHeight, long maxBitrate) throws ParserConfigurationException, TransformerException {
		// sort the video list with width descending for a better organization of the smil file (with an anonymous class)
		Collections.sort(videos, new Comparator<CreatedVideo>() {
		    @Override
		    public int compare(CreatedVideo v1, CreatedVideo v2) {
		        return Integer.valueOf(v2.getWidth()).compareTo(Integer.valueOf(v1.getWidth()));
		    }
		});
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();
		// create root element
		Element rootElement = doc.createElement("smil");
		doc.appendChild(rootElement);
		
		// head node
		Element headsElement = doc.createElement("head");
		// the head node is empty
		rootElement.appendChild(headsElement);
		
		// body node
		Element bodyElement = doc.createElement("body");
		rootElement.appendChild(bodyElement);

		// switch node
		Element switchElement = doc.createElement("switch");
		bodyElement.appendChild(switchElement);
		
		// video elements
		for (CreatedVideo video: videos) {
			// ignore qualities that exceed the set values
			if (((video.getHeight() > maxHeight) && (maxHeight!=0)) || ((video.getBitrate() > maxBitrate) && (maxBitrate!=0))) {
				continue;
			}
			Element videoElement = doc.createElement("video");
			videoElement.setAttribute("src", video.getFilename());
			videoElement.setAttribute("height", String.valueOf(video.getHeight()));
			videoElement.setAttribute("width", String.valueOf(video.getWidth()));
			// if there a seperate video or audio-bitrate is given add those
			if ((video.getBitrateAudio() > 0) || (video.getBitrateVideo()) > 0) {
				Element videoParamElement = SmilBuilder.createParam("videoBitrate",video.getBitrateVideo(),doc);
				videoElement.appendChild(videoParamElement);
				Element audioParamElement = SmilBuilder.createParam("audioBitrate",video.getBitrateAudio(),doc);
				videoElement.appendChild(audioParamElement);
			} 
			// add the overall bitrate
			videoElement.setAttribute("system-bitrate", String.valueOf(video.getBitrate()));
			
			// add the video element to the switch node
			switchElement.appendChild(videoElement);
		}

		// prepare writing of xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filePath));
		// indent
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		// write the file
		transformer.transform(source, result);
	}

	/**
	 * Creates a param element of the xml file
	 * @param name the name property
	 * @param bitrate the bitrate property
	 * @param doc the document in which the param will be created
	 * @return the xml param element
	 */
	private static Element createParam(String name, int bitrate, Document doc) {
		Element paramElement = doc.createElement("param");
		paramElement.setAttribute("name", name);
		paramElement.setAttribute("value", String.valueOf(bitrate));
		paramElement.setAttribute("valuetype", "data");
		return paramElement;
	}
}