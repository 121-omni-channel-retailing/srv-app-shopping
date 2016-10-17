package otocloud.app.catalog.model;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the app_module database table.
 * 
 */
public class AppModule  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String appTypeCode;
	private String code;
	private Date deleteDatetime;
	private Integer deleteId;
	private Date entryDatetime;
	private Integer entryId;
	private String longDesc;
	private String name;
	private String shortDesc;
	private String status;
	private Date updateDatetime;
	private Integer updateId;
	private List<AppActivity> appActivities;
	private List<AppFeature> appFeatures;
	private List<AppModuleInfoExtra> appModuleInfoExtras;
	private List<AppPrice> appPrices;

	public AppModule() {
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getAppTypeCode() {
		return this.appTypeCode;
	}

	public void setAppTypeCode(String appTypeCode) {
		this.appTypeCode = appTypeCode;
	}


	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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


	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getShortDesc() {
		return this.shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
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


	public List<AppActivity> getAppActivities() {
		return this.appActivities;
	}

	public void setAppActivities(List<AppActivity> appActivities) {
		this.appActivities = appActivities;
	}

	public AppActivity addAppActivity(AppActivity appActivity) {
		getAppActivities().add(appActivity);
		appActivity.setAppModuleId(this.getId());
		return appActivity;
	}

	public AppActivity removeAppActivity(AppActivity appActivity) {
		getAppActivities().remove(appActivity);
		appActivity.setAppModuleId(null);
		return appActivity;
	}


	public List<AppFeature> getAppFeatures() {
		return this.appFeatures;
	}

	public void setAppFeatures(List<AppFeature> appFeatures) {
		this.appFeatures = appFeatures;
	}

	public AppFeature addAppFeature(AppFeature appFeature) {
		getAppFeatures().add(appFeature);
		appFeature.setAppModuleId(this.getId());
		return appFeature;
	}

	public AppFeature removeAppFeature(AppFeature appFeature) {
		getAppFeatures().remove(appFeature);
		appFeature.setAppModuleId(null);
		return appFeature;
	}


	public List<AppModuleInfoExtra> getAppModuleInfoExtras() {
		return this.appModuleInfoExtras;
	}

	public void setAppModuleInfoExtras(List<AppModuleInfoExtra> appModuleInfoExtras) {
		this.appModuleInfoExtras = appModuleInfoExtras;
	}

	public AppModuleInfoExtra addAppModuleInfoExtra(AppModuleInfoExtra appModuleInfoExtra) {
		getAppModuleInfoExtras().add(appModuleInfoExtra);
		appModuleInfoExtra.setAppModuleId(this.getId());
		return appModuleInfoExtra;
	}

	public AppModuleInfoExtra removeAppModuleInfoExtra(AppModuleInfoExtra appModuleInfoExtra) {
		getAppModuleInfoExtras().remove(appModuleInfoExtra);
		appModuleInfoExtra.setAppModuleId(null);

		return appModuleInfoExtra;
	}


	public List<AppPrice> getAppPrices() {
		return this.appPrices;
	}

	public void setAppPrices(List<AppPrice> appPrices) {
		this.appPrices = appPrices;
	}

	public AppPrice addAppPrice(AppPrice appPrice) {
		getAppPrices().add(appPrice);
		appPrice.setAppModuleId(this.getId());

		return appPrice;
	}

	public AppPrice removeAppPrice(AppPrice appPrice) {
		getAppPrices().remove(appPrice);
		appPrice.setAppModuleId(this.getId());

		return appPrice;
	}

}