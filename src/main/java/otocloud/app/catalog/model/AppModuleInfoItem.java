package otocloud.app.catalog.model;

import java.util.Date;


/**
 * The persistent class for the app_module_info_item database table.
 * 
 */
public class AppModuleInfoItem  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String dataType;
	private Date deleteDatetime;
	private Integer deleteId;
	private Date entryDatetime;
	private Integer entryId;
	private String itemName;
	private String segmentName;
	private Integer seqNo;
	private String status;
	private Date updateDatetime;
	private Integer updateId;

	public AppModuleInfoItem() {
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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


	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getSegmentName() {
		return this.segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}


	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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


	public Integer getSeqNo() {
		return seqNo;
	}


	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

}