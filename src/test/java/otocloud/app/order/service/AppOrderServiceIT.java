package otocloud.app.order.service;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.restassured.response.ValidatableResponse;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.app.common.CommonUtils;
import otocloud.app.order.model.AppOrder;
import otocloud.app.order.model.AppOrderDetail;
import otocloud.test.base.OtoCloudTestBaseIT;

@SuppressWarnings("unused")
public class AppOrderServiceIT extends OtoCloudTestBaseIT {    
    static Logger logger = LoggerFactory.getLogger(AppOrderServiceIT.class.getName());
    
    /**
     * 设置测试订单ID号，这些订单ID号需要在测试之前通过初始化数据库数据得到。
     */
    public static final int ORDER_ID_GET_VALID = 1;
    public static final int ORDER_ID_GET_INVALID = -1;
    
    public static final int ORDER_ID_PUT_VALID = 3;
    public static final int ORDER_DETAIL_ID1_PUT_VALID = 4;
    public static final int ORDER_DETAIL_ID2_PUT_VALID = 5;
    public static final int ORDER_ID_PUT_INVALID = -1;
    
    public static final int ORDER_ID_DELETE_VALID = 3;
    public static final int ORDER_ID_DELETE_INVALID = -1;
    
	@Test
	public void it_should_return_multiple_orders() {
			given()
			.when()
				.get("/api/app-shopping/app-orders?token={accessToken}", accessToken)
			.then().assertThat()
				.statusCode(200)
				.body("size()", greaterThan(0))
				.log().all(true);
	}
	
	@Test
	public void it_should_return_one_order_with_valid_order_id() {
		given()
		.when()
			.get("/api/app-shopping/app-orders/{id}?token={accessToken}", ORDER_ID_GET_VALID, accessToken)
		.then().assertThat()
			.statusCode(200)
			.body("id", notNullValue())
			.body("appOrderDetails.size()", greaterThan(0))
			.log().all(true);
	}
	
	@Test
	public void it_should_return_no_order_with_invalid_order_id() {
		given()
		.when()
			.get("/api/app-shopping/app-orders/{id}?token={accessToken}", ORDER_ID_GET_INVALID, accessToken)
		.then().assertThat()
			.statusCode(200)
			.body(isEmptyOrNullString())
			.log().all(true);
	}
	
	@Test
	public void it_should_create_one_new_order() {
		//// org_acct_id, total_price, status
		AppOrder appOrder = new AppOrder();
		appOrder.setTotalPrice(new Double(6500));
		appOrder.setStatus("A");
		// app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price
		List<AppOrderDetail> appOrderDetails = new LinkedList<AppOrderDetail>();
		AppOrderDetail appOrderDetail1 = new AppOrderDetail();
		appOrderDetail1.setAppModuleId(new Integer(1));
		appOrderDetail1.setAppFeatureId(null);
		appOrderDetail1.setPrice(new Double(5500));
		appOrderDetail1.setQuantity(new Integer(1));
		appOrderDetail1.setPrice(new Double(5500));
		appOrderDetail1.setExtendedPrice(new Double(5500));
		appOrderDetails.add(appOrderDetail1);
		
		AppOrderDetail appOrderDetail2 = new AppOrderDetail();
		appOrderDetail2.setAppModuleId(new Integer(2));
		appOrderDetail2.setAppFeatureId(new Integer(3));
		appOrderDetail2.setPrice(new Double(1000));
		appOrderDetail2.setQuantity(new Integer(1));
		appOrderDetail2.setPrice(new Double(1000));
		appOrderDetail2.setExtendedPrice(new Double(1000));
		appOrderDetails.add(appOrderDetail2);
		
		appOrder.setAppOrderDetails(appOrderDetails);
		
		Gson gson = CommonUtils.getGson();
		String jsonStr = gson.toJson(appOrder);
		
		given()
			.contentType("application/json")
			.body(jsonStr)
		.when()
			.post("/api/app-shopping/app-orders?token={accessToken}", accessToken)
		.then().assertThat()
			.statusCode(200)
			.body("id",greaterThan(0))
			.body("app_order_details.size()", equalTo(2))
			.log().all(true);
	}
	
	@Test
	public void it_should_update_one_order_with_valid_id() {
		//// org_acct_id, total_price, status
		AppOrder appOrder = new AppOrder();
		appOrder.setId(new Integer(ORDER_ID_PUT_VALID));
		appOrder.setTotalPrice(new Double(7500));
		appOrder.setStatus("A");
		// app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price
		List<AppOrderDetail> appOrderDetails = new LinkedList<AppOrderDetail>();
		AppOrderDetail appOrderDetail1 = new AppOrderDetail();
		appOrderDetail1.setId(new Integer(ORDER_DETAIL_ID1_PUT_VALID));
		appOrderDetail1.setAppModuleId(new Integer(1));
		appOrderDetail1.setAppFeatureId(null);
		appOrderDetail1.setPrice(new Double(4500));
		appOrderDetail1.setQuantity(new Integer(1));
		appOrderDetail1.setPrice(new Double(4500));
		appOrderDetail1.setExtendedPrice(new Double(4500));
		appOrderDetails.add(appOrderDetail1);
		
		AppOrderDetail appOrderDetail2 = new AppOrderDetail();
		appOrderDetail2.setId(new Integer(ORDER_DETAIL_ID2_PUT_VALID));
		appOrderDetail2.setAppModuleId(new Integer(2));
		appOrderDetail2.setAppFeatureId(new Integer(3));
		appOrderDetail2.setPrice(new Double(3000));
		appOrderDetail2.setQuantity(new Integer(1));
		appOrderDetail2.setPrice(new Double(3000));
		appOrderDetail2.setExtendedPrice(new Double(3000));
		appOrderDetails.add(appOrderDetail2);
		
		appOrder.setAppOrderDetails(appOrderDetails);
		
		Gson gson = CommonUtils.getGson();
		String jsonStr = gson.toJson(appOrder);
		
		given()
			.contentType("application/json")
			.body(jsonStr)
		.when()
			.put("/api/app-shopping/app-orders/{id}?token={accessToken}", ORDER_ID_PUT_VALID, accessToken)
		.then().assertThat()
			.statusCode(200)
			.body("id",is(ORDER_ID_PUT_VALID))
			.body("app_order_details.size()", greaterThan(0))
			.log().all(true);
	}
	
	@Test
	public void it_should_update_no_order_with_invalid_order_id() {
		//// org_acct_id, total_price, status
		AppOrder appOrder = new AppOrder();
		appOrder.setId(new Integer(ORDER_ID_PUT_INVALID));
		appOrder.setTotalPrice(new Double(7500));
		appOrder.setStatus("A");
		// app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price
		List<AppOrderDetail> appOrderDetails = new LinkedList<AppOrderDetail>();
		AppOrderDetail appOrderDetail1 = new AppOrderDetail();
		appOrderDetail1.setId(new Integer(ORDER_DETAIL_ID1_PUT_VALID));
		appOrderDetail1.setAppModuleId(new Integer(1));
		appOrderDetail1.setAppFeatureId(null);
		appOrderDetail1.setPrice(new Double(4500));
		appOrderDetail1.setQuantity(new Integer(1));
		appOrderDetail1.setPrice(new Double(4500));
		appOrderDetail1.setExtendedPrice(new Double(4500));
		appOrderDetails.add(appOrderDetail1);
		
		AppOrderDetail appOrderDetail2 = new AppOrderDetail();
		appOrderDetail2.setId(new Integer(ORDER_DETAIL_ID2_PUT_VALID));
		appOrderDetail2.setAppModuleId(new Integer(2));
		appOrderDetail2.setAppFeatureId(new Integer(3));
		appOrderDetail2.setPrice(new Double(3000));
		appOrderDetail2.setQuantity(new Integer(1));
		appOrderDetail2.setPrice(new Double(3000));
		appOrderDetail2.setExtendedPrice(new Double(3000));
		appOrderDetails.add(appOrderDetail2);
		
		appOrder.setAppOrderDetails(appOrderDetails);
		
		Gson gson = CommonUtils.getGson();
		String jsonStr = gson.toJson(appOrder);
		
		given()
			.contentType("application/json")
			.body(jsonStr)
		.when()
			.put("/api/app-shopping/app-orders/{id}?token={accessToken}", ORDER_ID_PUT_INVALID, accessToken)
		.then().assertThat()
			.statusCode(200)
			.body("errCode",is(400))
			.body("errMsg", containsString("Not update any rows"))
			.log().all(true);
	}
	
	@Test
	public void it_should_delete_one_order_with_valid_order_id() {
		given()
		.when()
			.delete("/api/app-shopping/app-orders/{id}?token={accessToken}", ORDER_ID_DELETE_VALID, accessToken)
		.then().assertThat()
			.statusCode(200)
			.body("app_order_header.updated",is(1))
			.body("app_order_detail.updated", greaterThan(0))
			.log().all(true);
	}
	
	@Test
	public void it_should_delete_no_order_with_invalid_order_id() {
		given()
		.when()
			.delete("/api/app-shopping/app-orders/{id}?token={accessToken}", ORDER_ID_DELETE_INVALID, accessToken)
		.then().assertThat()
			.statusCode(200)
			.body("errCode",is(400))
			.body("errMsg", containsString("Not delete any rows"))
			.log().all(true);
	}

}
