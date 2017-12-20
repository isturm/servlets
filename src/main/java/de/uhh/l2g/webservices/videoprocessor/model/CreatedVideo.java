package de.uhh.l2g.webservices.videoprocessor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The CreatedVideo model describes videos which where created in the file system via the videoConversion
 * It inherits from the CreatedFile model and adds video-specific properties
 */
@Entity
public class CreatedVideo extends CreatedFile {

	private int bitrateVideo = 0;

	private int bitrateAudio = 0;

	private int width;

	private int height;

	private int bitrate;
	
	// the remote path is temporarily used and will not be persisted
	@Transient
	@JsonIgnore
	private String remotePath;

	/**
	 * @return the bitrate
	 */
	public int getBitrate() {
		return bitrate;
	}

	/**
	 * @param bitrate the bitrate to set
	 */
	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	/**
	 * @return the bitrateVideo
	 */
	public int getBitrateVideo() {
		return bitrateVideo;
	}

	/**
	 * @param bitrateVideo the bitrateVideo to set
	 */
	public void setBitrateVideo(int bitrateVideo) {
		this.bitrateVideo = bitrateVideo;
	}

	/**
	 * @return the bitrateAudio
	 */
	public int getBitrateAudio() {
		return bitrateAudio;
	}

	/**
	 * @param bitrateAudio the bitrateAudio to set
	 */
	public void setBitrateAudio(int bitrateAudio) {
		this.bitrateAudio = bitrateAudio;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the remotePath
	 */
	public String getRemotePath() {
		return remotePath;
	}

	/**
	 * @param remotePath the remotePath to set
	 */
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	
}
