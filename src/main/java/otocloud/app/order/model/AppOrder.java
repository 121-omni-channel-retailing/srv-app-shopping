package otocloud.app.order.model;

import java.util.Date;
import java.util.List;


/**
 * The model class for the app_order_header database table.
 * 
 */
public class AppOrder  {
	private static final long serialVersionUID = 1L;
	private Integer id;

	private Date deleteDatetime;

	private Integer deleteId;

	private Date entryDatetime;

	private Integer entryId;

	private Integer orgAcctId;

	private String status;

	private Date updateDatetime;

	private Integer updateId;
	
	private Double totalPrice;
	
	private List<AppOrderDetail> appOrderDetails;
	
	public AppOrder() {
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

	public Integer getOrgAcctId() {
		return this.orgAcctId;
	}

	public void setOrgAcctId(Integer orgAcctId) {
		this.orgAcctId = orgAcctId;
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

	public List<AppOrderDetail> getAppOrderDetails() {
		return appOrderDetails;
	}

	public void setAppOrderDetails(List<AppOrderDetail> appOrderDetails) {
		if (appOrderDetails != null) {
			for (AppOrderDetail appOrderDetail : appOrderDetails) {
				appOrderDetail.setAppOrderHeaderId(this.id);
			}
		}
		this.appOrderDetails = appOrderDetails;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

}