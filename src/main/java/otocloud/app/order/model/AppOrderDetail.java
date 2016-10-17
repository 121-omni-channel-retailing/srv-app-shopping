package otocloud.app.order.model;

import java.util.Date;

/**
 * The model class for the app_order_detail database table.
 * 
 */
public class AppOrderDetail  {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer appFeatureId;

	private Integer appModuleId;

	private Integer appOrderHeaderId;

	private Date deleteDatetime;

	private Integer deleteId;

	private Date entryDatetime;

	private Integer entryId;

	private Double extendedPrice;

	private Double price;

	private Integer quantity;

	private Date updateDatetime;

	private Integer updateId;

	public AppOrderDetail() {
	}

	public Integer getAppFeatureId() {
		return this.appFeatureId;
	}

	public void setAppFeatureId(Integer appFeatureId) {
		this.appFeatureId = appFeatureId;
	}

	public Integer getAppModuleId() {
		return this.appModuleId;
	}

	public void setAppModuleId(Integer appModuleId) {
		this.appModuleId = appModuleId;
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

	public Double getExtendedPrice() {
		return this.extendedPrice;
	}

	public void setExtendedPrice(Double extendedPrice) {
		this.extendedPrice = extendedPrice;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAppOrderHeaderId() {
		return appOrderHeaderId;
	}

	public void setAppOrderHeaderId(Integer appOrderHeaderId) {
		this.appOrderHeaderId = appOrderHeaderId;
	}

}