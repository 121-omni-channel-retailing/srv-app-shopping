package otocloud.app.catalog.model;

import java.util.Date;


/**
 * The persistent class for the app_price database table.
 * 
 */
public class AppPrice  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date deleteDatetime;
	private Integer deleteId;
	private Date entryDatetime;
	private Integer entryId;
	private Date expireDate;
	private Double price;
	private Date sinceDate;
	private Date updateDatetime;
	private Integer updateId;
	private Integer appFeatureId;
	private Integer appModuleId;

	public AppPrice() {
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Date getDeleteDatetime() {
		return this.deleteDatetime;
	}

	public void setDeleteDatetime(Date deleteDatetime) {
		this.deleteDatetime = deleteDatetime;
	}


	public Integer getDeleteId() {
		return this.deleteId;
	}

	public void setDeleteId(Integer deleteId) {
		this.deleteId = deleteId;
	}


	public Date getEntryDatetime() {
		return this.entryDatetime;
	}

	public void setEntryDatetime(Date entryDatetime) {
		this.entryDatetime = entryDatetime;
	}


	public Integer getEntryId() {
		return this.entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	public Date getUpdateDatetime() {
		return this.updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}


	public Integer getUpdateId() {
		return this.updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}

	public Integer getAppFeatureId() {
		return appFeatureId;
	}


	public void setAppFeatureId(Integer appFeatureId) {
		this.appFeatureId = appFeatureId;
	}


	public Integer getAppModuleId() {
		return appModuleId;
	}


	public void setAppModuleId(Integer appModuleId) {
		this.appModuleId = appModuleId;
	}


	public Date getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}


	public Date getSinceDate() {
		return sinceDate;
	}


	public void setSinceDate(Date sinceDate) {
		this.sinceDate = sinceDate;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}
	
}