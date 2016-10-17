package otocloud.app.order.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import otocloud.app.AppCatalogOrderService;
import otocloud.app.order.model.AppOrder;
import otocloud.app.order.model.AppOrderDetail;
import otocloud.framework.core.OtoCloudServiceForVerticleImpl;
import otocloud.persistence.dao.TransactionConnection;
import otocloud.test.base.OtoCloudTestBase;

@RunWith(VertxUnitRunner.class)
public class AppOrderDAOImplTest extends OtoCloudTestBase<AppCatalogOrderService> {
	private static final Logger logger = LoggerFactory.getLogger(AppOrderDAOImplTest.class);
	private AppOrderDAO appOrderDAO = null;
	private SQLConnection conn;
	private TransactionConnection transConn;
	
	//保存appOrder主键，供其他测试使用。
	private List<Integer> appOrderIds = new ArrayList<Integer>();
	//无返回结果的order ID列表
	private List<Integer> appOrderIdsWithNoResult = new ArrayList<Integer>();
	
	//设置账户编号
	private Integer acctId;
	//无返回结果的账户编号
	private Integer acctIdWithNoResult;
	
	//Setup user Id
	private Integer userIdEntry;
	private Integer userIdUpdate;
	private Integer userIdDelete;
	
	//Setup testing orderId
	private Integer orderId;
	private Integer orderDetailId1;
	private Integer orderDetailId2;
	
	@Before
	public void Prepare(TestContext context) throws IOException {
		final Async async = context.async();
		//1. Initialize DAO object
		appOrderDAO = new AppOrderDAOImpl(getService().getSysDatasource());
		
		//2. Get DB connection
		JDBCClient jdbcClient = getService().getSysDatasource().getSqlClient();
		jdbcClient.getConnection(connRes -> {
			if (connRes.succeeded()) {
				conn = connRes.result();
				TransactionConnection.createTransactionConnection(conn, transConnRet -> {
					if (transConnRet.succeeded()) {
						transConn = transConnRet.result();
					} else {
						logger.error(transConnRet.cause());
					}
					async.complete();
				});
			} else {
				logger.error("Failed to connect DB", connRes.cause());
			}
			
		});
		
		//2. 初始化appModuleIds
		appOrderIds.add(new Integer(1));
		appOrderIds.add(new Integer(2));
		appOrderIdsWithNoResult.add(new Integer(-1));
		appOrderIdsWithNoResult.add(new Integer(-2));
		
		acctId = new Integer(1);
		acctIdWithNoResult = new Integer(-1);
		
		//3. Initialize user Id
		userIdEntry = new Integer(77);
		userIdUpdate = new Integer(88);;
		userIdDelete = new Integer(99);;
		
		//4. Initialize testing order ID
		orderId = new Integer(3);
		orderDetailId1 = new Integer(4);
		orderDetailId2 = new Integer(5);
	}
	
	@Test
	public void it_should_return_multiple_orders_with_valid_acct_id(TestContext context) {
		final Async async = context.async();

		appOrderDAO.getAppOrders(acctId, rsRes -> {
			if (rsRes.succeeded()) {
				List<AppOrder> appOrders = rsRes.result();
				//1. Execute tested function and assert returned result.
				int size = appOrders.size();
				context.assertTrue(size > 0, "Not found any rows in app_order_header table.");
				String jsonStr = Json.encodePrettily(appOrders);
				logger.debug("appOrders-->" + jsonStr);
				JsonArray jsonArray = new JsonArray(jsonStr);
				context.assertTrue(jsonArray.size() == size, "Failed to tranfer to JsonArray.");				
				async.complete();
			}
		});
	}
	
	@Test
	public void it_should_return_no_order_with_invalid_acct_id(TestContext context) {
		final Async async = context.async();

		appOrderDAO.getAppOrders(acctIdWithNoResult, rsRes -> {
			if (rsRes.succeeded()) {
				List<AppOrder> appOrders = rsRes.result();
				
				context.assertTrue(appOrders == null || appOrders.size() == 0, "应该无记录返回。");
				async.complete();
			}
		});
	}
	
	@Test
	public void it_should_return_multiple_orders_with_valid_order_ids(TestContext context) {
		final Async async = context.async();

		appOrderDAO.getAppOrders(acctId, appOrderIds, rsRes -> {
			if (rsRes.succeeded()) {
				List<AppOrder> appOrders = rsRes.result();
				//1. Execute tested function and assert returned result.
				int size = appOrders.size();
				context.assertTrue(size > 0, "Not found any rows in app_order_header table.");
				context.assertTrue(appOrderIds.size() == size, "有ID没有查询出来。");
				String jsonStr = Json.encodePrettily(appOrders);
				logger.debug("appOrders-->" + jsonStr);
				JsonArray jsonArray = new JsonArray(jsonStr);
				context.assertTrue(jsonArray.size() == size, "Failed to tranfer to JsonArray.");				
				async.complete();
			}
		});
	}
	
	@Test
	public void it_should_return_no_order_with_invalid_order_ids(TestContext context) {
		final Async async = context.async();

		appOrderDAO.getAppOrders(acctId, appOrderIdsWithNoResult, rsRes -> {
			if (rsRes.succeeded()) {
				List<AppOrder> appOrders = rsRes.result();
				//1. Execute tested function and assert returned result.
				context.assertTrue(appOrders == null || appOrders.size() == 0, "应该无返回记录。");				
				async.complete();
			}
		});
	}
	
	@Test
	public void it_should_return_multiple_order_details_with_valid_order_ids(TestContext context) {
		final Async async = context.async();

		appOrderDAO.getAppOrderDetails(appOrderIds, rsRes -> {
			if (rsRes.succeeded()) {
				Map<Integer, List<AppOrderDetail>> appOrdersMap = rsRes.result();
				//1. Execute tested function and assert returned result.
				int size = appOrdersMap.size();
				context.assertTrue(size > 0, "Not found any rows in app_order_header_detail table.");
				context.assertTrue(appOrderIds.size() == size, "有ID没有查询出来。");
				appOrdersMap.forEach((appOrderId, orderDetails) -> {
					logger.debug("appOrderId-->" + appOrderId);
					String jsonStr = Json.encodePrettily(orderDetails);
					JsonArray jsonArray = new JsonArray(jsonStr);
					context.assertTrue(jsonArray.size() == orderDetails.size(), "Failed to tranfer to JsonArray.");
				}); 

				async.complete();
			}
		});
	}

	@Test
	public void it_should_return_multiple_orders_with_valid_order_id(TestContext context) {
		final Async async = context.async();

		appOrderDAO.getAppOrder(acctId, appOrderIds.get(0), rsRes -> {
			if (rsRes.succeeded()) {
				AppOrder appOrder = rsRes.result();
				//1. Execute tested function and assert returned result.
				context.assertNotNull(appOrder, "Not found app order.");
				String jsonStr = Json.encodePrettily(appOrder);
				logger.debug("appOrder-->" + jsonStr);
				JsonObject jsonObject = new JsonObject(jsonStr);
				async.complete();
			}
		});
	}
	
	@Test
	public void it_should_create_one_order_with_no_order_detail(TestContext context) {
		final Async async = context.async();
		AppOrder appOrder = new AppOrder();
		appOrder.setOrgAcctId(acctId);
		appOrder.setTotalPrice(new Double(5000));
		appOrder.setStatus("A");
		
		appOrderDAO.saveAppOrder(transConn, appOrder, userIdEntry.intValue(), rsRes -> {
			if (rsRes.succeeded()) {
				AppOrder retAppOrder = rsRes.result();
				//1. Execute tested function and assert returned result.
				context.assertNotNull(retAppOrder, "Not found app order.");
				context.assertNotNull(retAppOrder.getId(), "Failed to save appOrder");
				logger.debug("New added appOrder.id: " + retAppOrder.getId());
				String jsonStr = Json.encodePrettily(retAppOrder);
				logger.debug("appOrder-->" + jsonStr);
				JsonObject jsonObject = new JsonObject(jsonStr);
				transConn.commit(commitRet -> {
					logger.debug("提交成功　--> order_header.id:　" + retAppOrder.getId());
					async.complete();
				});
			}
		});
	}

	@Test
	public void it_should_create_one_order_with_valid_order(TestContext context) {
		final Async async = context.async();
		AppOrder appOrder = new AppOrder();
		appOrder.setOrgAcctId(acctId);
		appOrder.setTotalPrice(new Double(8000));
		appOrder.setStatus("A");
		appOrder.setEntryId(userIdEntry);
		List<AppOrderDetail> appOrderDetails = new ArrayList<AppOrderDetail>();
		//app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price, entry_id
		AppOrderDetail appOrderDetail1 = new AppOrderDetail();
		appOrderDetail1.setAppModuleId(new Integer(1));
		appOrderDetail1.setAppFeatureId(new Integer(1));
		appOrderDetail1.setPrice(new Double(150));
		appOrderDetail1.setQuantity(new Integer(1));
		appOrderDetail1.setExtendedPrice(new Double(150*1));
		appOrderDetail1.setEntryId(userIdEntry);
		appOrderDetails.add(appOrderDetail1);
		
		AppOrderDetail appOrderDetail2 = new AppOrderDetail();
		appOrderDetail2.setAppModuleId(new Integer(1));
		appOrderDetail2.setAppFeatureId(new Integer(2));
		appOrderDetail2.setPrice(new Double(250));
		appOrderDetail2.setQuantity(new Integer(1));
		appOrderDetail2.setExtendedPrice(new Double(250*1));
		appOrderDetail2.setEntryId(userIdEntry);
		appOrderDetails.add(appOrderDetail2);
		
		AppOrderDetail appOrderDetail3 = new AppOrderDetail();
		appOrderDetail3.setAppModuleId(new Integer(2));
		appOrderDetail3.setAppFeatureId(new Integer(5));
		appOrderDetail3.setPrice(new Double(350));
		appOrderDetail3.setQuantity(new Integer(1));
		appOrderDetail3.setExtendedPrice(new Double(250*1));
		appOrderDetail3.setEntryId(userIdEntry);
		appOrderDetails.add(appOrderDetail3);
		
		appOrder.setAppOrderDetails(appOrderDetails);
		
		appOrderDAO.saveAppOrder(transConn, appOrder, userIdEntry.intValue(), rsRes -> {
			if (rsRes.succeeded()) {
				AppOrder retAppOrder = rsRes.result();
				//1. Execute tested function and assert returned result.
				context.assertNotNull(retAppOrder, "Not found app order.");
				context.assertNotNull(retAppOrder.getId(), "Failed to save appOrder");
				
				//2. Check if appOrderDetail was saved properly.
				List<AppOrderDetail> retAppOrderDetails = appOrder.getAppOrderDetails();
				retAppOrderDetails.forEach(appOrderDetail -> {
					context.assertNotNull(appOrderDetail.getId(), "AppOrderDetail wasn't saved properly.");
				});
				
				logger.debug("New added appOrder.id: " + retAppOrder.getId());
				String jsonStr = Json.encodePrettily(retAppOrder);
				logger.debug("appOrder-->" + jsonStr);
				JsonObject jsonObject = new JsonObject(jsonStr);
				
				transConn.commit(commitRet -> {
					logger.debug("提交成功　--> order_header.id:　" + retAppOrder.getId());
					async.complete();
				});
			}
		});
	}
	
	@Test
	public void it_should_create_multiple_order_details_with_valid_order_details(TestContext context) {
		final Async async = context.async();
		
		List<AppOrderDetail> appOrderDetails = new ArrayList<AppOrderDetail>();
		//app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price, entry_id
		AppOrderDetail appOrderDetail1 = new AppOrderDetail();
//		appOrderDetail1.setAppOrderHeaderId(new Integer(10));
		appOrderDetail1.setAppModuleId(new Integer(1));
		appOrderDetail1.setAppFeatureId(new Integer(1));
		appOrderDetail1.setPrice(new Double(150));
		appOrderDetail1.setQuantity(new Integer(1));
		appOrderDetail1.setExtendedPrice(new Double(150*1));
//		appOrderDetail1.setEntryId(userId);
		appOrderDetails.add(appOrderDetail1);
		
		AppOrderDetail appOrderDetail2 = new AppOrderDetail();
		appOrderDetail2.setAppModuleId(new Integer(2));
		appOrderDetail2.setAppFeatureId(new Integer(2));
		appOrderDetail2.setPrice(new Double(250));
		appOrderDetail2.setQuantity(new Integer(1));
		appOrderDetail2.setExtendedPrice(new Double(250*1));
//		appOrderDetail2.setEntryId(userId);
		appOrderDetails.add(appOrderDetail2);
//		
		AppOrderDetail appOrderDetail3 = new AppOrderDetail();
		appOrderDetail3.setAppModuleId(new Integer(3));
		appOrderDetail3.setAppFeatureId(new Integer(5));
		appOrderDetail3.setPrice(new Double(350));
		appOrderDetail3.setQuantity(new Integer(1));
		appOrderDetail3.setExtendedPrice(new Double(250*1));
//		appOrderDetail3.setEntryId(userId);
		appOrderDetails.add(appOrderDetail3);
		
		appOrderDAO.saveAppOrderDetails(transConn, appOrderDetails, new Integer(10), userIdEntry, rsRes -> {
			if (rsRes.succeeded()) {
				List<AppOrderDetail> retAppOrderDetails = rsRes.result();
				//1. Execute tested function and assert returned result.
				context.assertEquals(3, retAppOrderDetails.size(), "Failed to save app_order_detail.");
				
				//2. Check if appOrderDetail was saved properly.
				retAppOrderDetails.forEach(appOrderDetail -> {
					context.assertNotNull(appOrderDetail.getId(), "AppOrderDetail wasn't saved properly.");
				});
				
//				String jsonStr = Json.encodePrettily(retAppOrderDetails);
//				logger.debug("retAppOrderDetails-->" + jsonStr);
//				JsonObject jsonObject = new JsonObject(jsonStr);
				transConn.commit(commitRet -> {
					logger.debug("提交成功.");
					async.complete();
				});
			}
		});
	}
	
	@After
	public void clear(TestContext context) {
		if (conn == null) return;
		final Async async = context.async();
		conn.close(ret -> {			
			if (ret.succeeded()) {
				logger.debug("Close DB connection successfully.");
			} else {
				logger.error("Failed to close DB connection", ret.cause());
			}
			async.complete();
		});
	}

	@Test
	public void it_should_update_one_order_with_valid_order(TestContext context) {
		final Async async = context.async();
		AppOrder appOrder = new AppOrder();
		appOrder.setId(orderId);
		appOrder.setOrgAcctId(acctId);
		appOrder.setTotalPrice(new Double(9000));
		appOrder.setStatus("A");
		List<AppOrderDetail> appOrderDetails = new ArrayList<AppOrderDetail>();
		//app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price, entry_id
		AppOrderDetail appOrderDetail1 = new AppOrderDetail();
		appOrderDetail1.setId(orderDetailId1);
		appOrderDetail1.setAppOrderHeaderId(orderId);
		appOrderDetail1.setAppModuleId(new Integer(1));
		appOrderDetail1.setAppFeatureId(new Integer(1));
		appOrderDetail1.setPrice(new Double(150));
		appOrderDetail1.setQuantity(new Integer(1));
		appOrderDetail1.setExtendedPrice(new Double(150*1));
		appOrderDetails.add(appOrderDetail1);
		
		AppOrderDetail appOrderDetail2 = new AppOrderDetail();
		appOrderDetail2.setId(new Integer(orderDetailId2));
		appOrderDetail2.setAppOrderHeaderId(orderId);
		appOrderDetail2.setAppModuleId(new Integer(1));
		appOrderDetail2.setAppFeatureId(new Integer(2));
		appOrderDetail2.setPrice(new Double(250));
		appOrderDetail2.setQuantity(new Integer(1));
		appOrderDetail2.setExtendedPrice(new Double(250*1));
		appOrderDetails.add(appOrderDetail2);
		
		appOrder.setAppOrderDetails(appOrderDetails);
		
		appOrderDAO.updateAppOrder(transConn, appOrder, userIdUpdate, rsRes -> {
			if (rsRes.succeeded()) {
				AppOrder retAppOrder = rsRes.result();
				//1. Execute tested function and assert returned result.
				context.assertNotNull(retAppOrder, "Not found app order.");
				context.assertNotNull(retAppOrder.getId(), "Failed to save appOrder");
				
				//2. Check if appOrderDetail was saved properly.
				List<AppOrderDetail> retAppOrderDetails = appOrder.getAppOrderDetails();
				retAppOrderDetails.forEach(appOrderDetail -> {
					context.assertNotNull(appOrderDetail.getId(), "AppOrderDetail wasn't saved properly.");
				});
				
				logger.debug("New added appOrder.id: " + retAppOrder.getId());
				String jsonStr = Json.encodePrettily(retAppOrder);
				logger.debug("appOrder-->" + jsonStr);
				JsonObject jsonObject = new JsonObject(jsonStr);
				
				transConn.commitAndClose(commitRet -> {
					logger.debug("提交成功　--> order_header.id:　" + retAppOrder.getId());
					async.complete();
				});
			}
		});
	}
	
	@Test
	public void it_should_delete_one_order_with_valid_order_id(TestContext context) {
		final Async async = context.async();
		
		appOrderDAO.deleteAppOrder(transConn, orderId, userIdDelete, rsRes -> {
			if (rsRes.succeeded()) {
				JsonObject updateResult = rsRes.result();
				
				context.assertTrue(updateResult.getJsonObject("app_order_header").getInteger("updated")>0, "Failed to delete app_order_header/app_order_detail softly.");

				transConn.commitAndClose(commitRet -> {
					logger.debug("删除成功　--> updateResult.getUpdated():　" + updateResult.toString());
					async.complete();
				});
			}
		});
	}
}
