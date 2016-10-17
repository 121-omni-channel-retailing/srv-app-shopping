package otocloud.app.catalog.model;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the app_feature database table.
 * 
 */
public class AppFeature  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer appModuleId;
	private Date deleteDatetime;
	private Integer deleteId;
	private Date entryDatetime;
	private Integer entryId;
	private String featureCode;
	private String featureDesc;
	private String featureName;
	private Date updateDatetime;
	private Integer updateId;
	private List<AppFeatureActivity> appFeatureActivities;
	private List<AppPrice> appPrices;

	public AppFeature() {
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


	public String getFeatureCode() {
		return this.featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}


	public String getFeatureDesc() {
		return this.featureDesc;
	}

	public void setFeatureDesc(String featureDesc) {
		this.featureDesc = featureDesc;
	}


	public String getFeatureName() {
		return this.featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
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
		appFeatureActivity.setAppFeature(this);

		return appFeatureActivity;
	}

	public AppFeatureActivity removeAppFeatureActivity(AppFeatureActivity appFeatureActivity) {
		getAppFeatureActivities().remove(appFeatureActivity);
		appFeatureActivity.setAppFeature(null);

		return appFeatureActivity;
	}


	public List<AppPrice> getAppPrices() {
		return this.appPrices;
	}

	public void setAppPrices(List<AppPrice> appPrices) {
		this.appPrices = appPrices;
	}

	public AppPrice addAppPrice(AppPrice appPrice) {
		getAppPrices().add(appPrice);
		appPrice.setAppFeatureId(this.getId());

		return appPrice;
	}

	public AppPrice removeAppPrice(AppPrice appPrice) {
		getAppPrices().remove(appPrice);
		appPrice.setAppFeatureId(null);

		return appPrice;
	}


	public Integer getAppModuleId() {
		return appModuleId;
	}


	public void setAppModuleId(Integer appModuleId) {
		this.appModuleId = appModuleId;
	}

}