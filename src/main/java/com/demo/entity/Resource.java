package com.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="resource")
public class Resource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8216008103262902829L;

	private Integer id;

	private String resCode;

	private String resName;

	private String resUrl;

	private String resType;

	private String resIcon;

	private Integer state;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "res_code")
	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode == null ? null : resCode.trim();
	}

	@Column(name = "res_name")
	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName == null ? null : resName.trim();
	}

	@Column(name = "res_url")
	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl == null ? null : resUrl.trim();
	}

	@Column(name = "res_type")
	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	@Column(name = "res_icon")
	public String getResIcon() {
		return resIcon;
	}

	public void setResIcon(String resIcon) {
		this.resIcon = resIcon == null ? null : resIcon.trim();
	}

	@Column(name = "state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}