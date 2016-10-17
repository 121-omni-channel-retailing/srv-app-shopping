package otocloud.app.catalog.model;

import java.util.Date;


/**
 * The persistent class for the app_operation database table.
 * 
 */
public class AppOperation  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date deleteDatetime;
	private Integer deleteId;
	private Date entryDatetime;
	private Integer entryId;
	private String httpMethod;
	private String inputDesc;
	private String operationDesc;
	private String outputDesc;
	private String restUrl;
	private String status;
	private Date updateDatetime;
	private Integer updateId;
	private AppActivity appActivity;

	public AppOperation() {
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


	public String getHttpMethod() {
		return this.httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}


	public String getInputDesc() {
		return this.inputDesc;
	}

	public void setInputDesc(String inputDesc) {
		this.inputDesc = inputDesc;
	}


	public String getOperationDesc() {
		return this.operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}


	public String getOutputDesc() {
		return this.outputDesc;
	}

	public void setOutputDesc(String outputDesc) {
		this.outputDesc = outputDesc;
	}


	public String getRestUrl() {
		return this.restUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
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


	public AppActivity getAppActivity() {
		return this.appActivity;
	}

	public void setAppActivity(AppActivity appActivity) {
		this.appActivity = appActivity;
	}

}