package otocloud.app.order.service;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.Gson;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import otocloud.app.AppCatalogOrderService;
import otocloud.app.common.CommonUtils;
import otocloud.app.common.MessageSchema;
import otocloud.app.order.model.AppOrder;
import otocloud.app.order.model.AppOrderDetail;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.test.base.OtoCloudTestBase;

/**
 * This is our JUnit test for our verticle. The test uses vertx-unit, so we
 * declare a custom runner.
 */
@RunWith(VertxUnitRunner.class)
public class AppOrderServiceTest extends OtoCloudTestBase<AppCatalogOrderService> {
	static final Logger logger = LoggerFactory.getLogger(AppOrderServiceTest.class);
	
	static final Integer ACCT_ID_VALID = new Integer(1);
	static final Integer ACCT_ID_INVALID = new Integer(-1);
	static final Integer USER_ID_VALID = new Integer(99);
	static final Integer USER_ID_INVALID = new Integer(-1);
	static final String ORDER_ID_VALID = "1";
	static final String ORDER_ID_INVALID = "1";
	static final Integer ORDER_ID_TESTING = new Integer(4); //测试订单号，初始化数据时设置好
	static final Integer ORDER_DETAIL_ID1_TESTING = new Integer(6); //测试订单明细ID号，初始化数据时设置好
	static final Integer ORDER_DETAIL_ID2_TESTING = new Integer(7); //测试订单明细ID号，初始化数据时设置好
	
	private JsonObject message = new JsonObject();
	private JsonObject messageWithNoResult = new JsonObject();

	@Before
	public void prepare(TestContext context) {
		JsonObject session = new JsonObject().put(MessageBodyConvention.SESSION_ACCT_ID, ACCT_ID_VALID)
				.put(MessageBodyConvention.SESSION_USER_ID, USER_ID_VALID);
		message.put(MessageBodyConvention.SESSION, session);
		message.put(MessageBodyConvention.HTTP_BODY, new JsonObject());
		
		JsonObject sessionWithNoResult = new JsonObject().put(MessageBodyConvention.SESSION_ACCT_ID, ACCT_ID_INVALID)
				.put(MessageBodyConvention.SESSION_USER_ID, USER_ID_INVALID);
		messageWithNoResult.put(MessageBodyConvention.SESSION, sessionWithNoResult);
		messageWithNoResult.put(MessageBodyConvention.HTTP_BODY, new JsonObject());
	}

	@Test
	public void it_should_return_multiple_orders_with_valid_acct(TestContext context) {
		final Async async = context.async();

		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_ORDER_GETALL, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();
			JsonArray appOrders = (JsonArray) msg.body();
			context.assertTrue(appOrders.size() > 0, "没有返回App Order数据。");
			appOrders.forEach(appOrderJsonObj -> {
				logger.debug("appOrderJsonObj-->" + appOrderJsonObj);
				String str = appOrderJsonObj.toString();
				AppOrder appOrder = Json.decodeValue(str, AppOrder.class);
				logger.debug("appOrder.id:" + appOrder.getId());
			});

			logger.debug("msg-->" + msg);
			async.complete();
		});

	}

	@Test
	public void it_should_return_one_order_with_valid_order_id(TestContext context) {
		final Async async = context.async();
		JsonObject queryParams = new JsonObject().put("id", ORDER_ID_VALID);
		message.put(MessageBodyConvention.HTTP_QUERY, queryParams);

		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_ORDER_GET, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();

			JsonObject appOrderJsonObj = (JsonObject) msg.body();
			context.assertNotNull(appOrderJsonObj, "没有返回app_order结果");
			String appOrderJsonStr = appOrderJsonObj.encodePrettily();
			logger.debug("appOrderJsonObj-->" + appOrderJsonStr);

			AppOrder appOrder = Json.decodeValue(appOrderJsonStr, AppOrder.class);
			Integer orderId = Integer.valueOf(message.getJsonObject(MessageBodyConvention.HTTP_QUERY).getString("id"));
			
			context.assertTrue(orderId.equals(appOrder.getId()), "没有返回App Order数据。");

			logger.debug("msg-->" + msg);
			async.complete();
		});

	}

	@Test
	public void it_should_return_no_order_with_invalid_order_id(TestContext context) {
		final Async async = context.async();
		JsonObject queryParamsWithNoResult = new JsonObject().put("id", ORDER_ID_INVALID);
		messageWithNoResult.put(MessageBodyConvention.HTTP_QUERY, queryParamsWithNoResult);
		
		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_ORDER_GET, messageWithNoResult, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();

			JsonObject appOrderJsonObj = (JsonObject) msg.body();

			context.assertNull(appOrderJsonObj, "应该无返回结果。");

			async.complete();
		});
	}
	
	@Test
	public void it_should_create_one_order_with_valid_order_data(TestContext context) {
		final Async async = context.async();
		
		//// org_acct_id, total_price, status
		AppOrder appOrder = new AppOrder();
		appOrder.setTotalPrice(new Double(2500));
		appOrder.setStatus("A");
		// app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price
		List<AppOrderDetail> appOrderDetails = new LinkedList<AppOrderDetail>();
		AppOrderDetail appOrderDetail1 = new AppOrderDetail();
		appOrderDetail1.setAppModuleId(new Integer(1));
		appOrderDetail1.setAppFeatureId(null);
		appOrderDetail1.setPrice(new Double(500));
		appOrderDetail1.setQuantity(new Integer(1));
		appOrderDetail1.setPrice(new Double(500));
		appOrderDetail1.setExtendedPrice(new Double(500));
		appOrderDetails.add(appOrderDetail1);
		
		AppOrderDetail appOrderDetail2 = new AppOrderDetail();
		appOrderDetail2.setAppModuleId(new Integer(2));
		appOrderDetail2.setAppFeatureId(new Integer(3));
		appOrderDetail2.setPrice(new Double(2000));
		appOrderDetail2.setQuantity(new Integer(1));
		appOrderDetail2.setPrice(new Double(2000));
		appOrderDetail2.setExtendedPrice(new Double(2000));
		appOrderDetails.add(appOrderDetail2);
		
		appOrder.setAppOrderDetails(appOrderDetails);
		
		Gson gson = CommonUtils.getGson();
		
		String jsonStr = gson.toJson(appOrder);
		
		message.put(MessageBodyConvention.HTTP_BODY, new JsonObject(jsonStr));
		
		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_ORDER_POST, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();

			JsonObject appOrderJsonObj = (JsonObject) msg.body();

			context.assertNotNull(appOrderJsonObj, "无返回结果。");

			async.complete();
		});
	}
	
	@Test
	public void it_should_update_one_order_with_valid_order_data(TestContext context) {
		final Async async = context.async();
		
		//// org_acct_id, total_price, status
		AppOrder appOrder = new AppOrder();
		appOrder.setId(ORDER_ID_TESTING);
		appOrder.setTotalPrice(new Double(2100));
		appOrder.setStatus("A");
		// app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price
		List<AppOrderDetail> appOrderDetails = new LinkedList<AppOrderDetail>();
		AppOrderDetail appOrderDetail1 = new AppOrderDetail();
		appOrderDetail1.setId(ORDER_DETAIL_ID1_TESTING);
		appOrderDetail1.setAppModuleId(new Integer(1));
		appOrderDetail1.setAppFeatureId(null);
		appOrderDetail1.setPrice(new Double(600));
		appOrderDetail1.setQuantity(new Integer(1));
		appOrderDetail1.setPrice(new Double(600));
		appOrderDetail1.setExtendedPrice(new Double(600));
		appOrderDetails.add(appOrderDetail1);
		
		AppOrderDetail appOrderDetail2 = new AppOrderDetail();
		appOrderDetail2.setId(ORDER_DETAIL_ID2_TESTING);
		appOrderDetail2.setAppModuleId(new Integer(2));
		appOrderDetail2.setAppFeatureId(new Integer(3));
		appOrderDetail2.setPrice(new Double(1500));
		appOrderDetail2.setQuantity(new Integer(1));
		appOrderDetail2.setPrice(new Double(1500));
		appOrderDetail2.setExtendedPrice(new Double(1500));
		appOrderDetails.add(appOrderDetail2);
		
		appOrder.setAppOrderDetails(appOrderDetails);
		
		Gson gson = CommonUtils.getGson();
		String jsonStr = gson.toJson(appOrder);
		
		message.put(MessageBodyConvention.HTTP_BODY, new JsonObject(jsonStr));
		JsonObject queryParams = new JsonObject().put("id", ORDER_ID_TESTING.toString());
		message.put(MessageBodyConvention.HTTP_QUERY, queryParams);

		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_ORDER_PUT, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();

			JsonObject appOrderJsonObj = (JsonObject) msg.body();
			context.assertNotNull(appOrderJsonObj, "无返回结果。");
			logger.debug("appOrderJsonObj-->" + appOrderJsonObj.encodePrettily());

			async.complete();
		});
	}
	
	@Test
	public void it_should_delete_one_order_with_valid_order_id(TestContext context) {
		final Async async = context.async();

		message.put(MessageBodyConvention.HTTP_BODY, new JsonObject());
		JsonObject queryParams = new JsonObject().put("id", ORDER_ID_TESTING.toString());
		message.put(MessageBodyConvention.HTTP_QUERY, queryParams);

		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_ORDER_DELETE, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();

			JsonObject updateResult = (JsonObject) msg.body();
			JsonObject updateResultHeader = updateResult.getJsonObject("app_order_header");
			context.assertTrue(updateResultHeader.getInteger("updated")>0, "删除订单失败。");

			async.complete();
		});
	}
}
