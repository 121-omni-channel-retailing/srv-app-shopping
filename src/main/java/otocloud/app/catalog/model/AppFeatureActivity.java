package otocloud.app.catalog.model;

import java.util.Date;


/**
 * The persistent class for the app_feature_activity database table.
 * 
 */
public class AppFeatureActivity  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date deleteDatetime;
	private Integer deleteId;
	private Date entryDatetime;
	private Integer entryId;
	private Date updateDatetime;
	private Integer updateId;
	private AppActivity appActivity;
	private AppFeature appFeature;

	public AppFeatureActivity() {
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


	public AppActivity getAppActivity() {
		return this.appActivity;
	}

	public void setAppActivity(AppActivity appActivity) {
		this.appActivity = appActivity;
	}


	public AppFeature getAppFeature() {
		return this.appFeature;
	}

	public void setAppFeature(AppFeature appFeature) {
		this.appFeature = appFeature;
	}

}