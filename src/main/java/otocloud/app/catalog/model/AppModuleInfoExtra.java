package otocloud.app.catalog.model;

import java.util.Date;


/**
 * The persistent class for the app_module_info_extra database table.
 * 
 */
public class AppModuleInfoExtra  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date deleteDatetime;
	private Integer deleteId;
	private Date entryDatetime;
	private Integer entryId;
	private String itemValue;
	private Integer seqNo;
	private Date updateDatetime;
	private Integer updateId;
	private Integer appModuleId;
	private AppModuleInfoItem appModuleInfoItem;

	public AppModuleInfoExtra() {
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


	public String getItemValue() {
		return this.itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}


	public Integer getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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

	public AppModuleInfoItem getAppModuleInfoItem() {
		return this.appModuleInfoItem;
	}

	public void setAppModuleInfoItem(AppModuleInfoItem appModuleInfoItem) {
		this.appModuleInfoItem = appModuleInfoItem;
	}


	public Integer getAppModuleId() {
		return appModuleId;
	}


	public void setAppModuleId(Integer appModuleId) {
		this.appModuleId = appModuleId;
	}

}