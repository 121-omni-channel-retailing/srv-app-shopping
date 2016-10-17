package otocloud.app.catalog.model;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the app_activity database table.
 * 
 */
public class AppActivity  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer appModuleId;
	private String activityCode;
	private String activityDesc;
	private String activityName;
	private Date deleteDatetime;
	private Integer deleteId;
	private Date entryDatetime;
	private Integer entryId;
	private Date updateDatetime;
	private Integer updateId;
	private List<AppFeatureActivity> appFeatureActivities;
	private List<AppOperation> appOperations;

	public AppActivity() {
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getActivityCode() {
		return this.activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}


	public String getActivityDesc() {
		return this.activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}


	public String getActivityName() {
		return this.activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
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

	public List<AppFeatureActivity> getAppFeatureActivities() {
		return this.appFeatureActivities;
	}

	public void setAppFeatureActivities(List<AppFeatureActivity> appFeatureActivities) {
		this.appFeatureActivities = appFeatureActivities;
	}

	public AppFeatureActivity addAppFeatureActivity(AppFeatureActivity appFeatureActivity) {
		getAppFeatureActivities().add(appFeatureActivity);
		appFeatureActivity.setAppActivity(this);

		return appFeatureActivity;
	}

	public AppFeatureActivity removeAppFeatureActivity(AppFeatureActivity appFeatureActivity) {
		getAppFeatureActivities().remove(appFeatureActivity);
		appFeatureActivity.setAppActivity(null);

		return appFeatureActivity;
	}


	public List<AppOperation> getAppOperations() {
		return this.appOperations;
	}

	public void setAppOperations(List<AppOperation> appOperations) {
		this.appOperations = appOperations;
	}

	public AppOperation addAppOperation(AppOperation appOperation) {
		getAppOperations().add(appOperation);
		appOperation.setAppActivity(this);

		return appOperation;
	}

	public AppOperation removeAppOperation(AppOperation appOperation) {
		getAppOperations().remove(appOperation);
		appOperation.setAppActivity(null);

		return appOperation;
	}


	public Integer getAppModuleId() {
		return appModuleId;
	}


	public void setAppModuleId(Integer appModuleId) {
		this.appModuleId = appModuleId;
	}

}