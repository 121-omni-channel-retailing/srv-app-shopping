package otocloud.app.order.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import otocloud.app.common.CommonUtils;
import otocloud.app.order.model.AppOrder;
import otocloud.app.order.model.AppOrderDetail;
import otocloud.persistence.dao.JdbcDataSource;
import otocloud.persistence.dao.OperatorDAO;
import otocloud.persistence.dao.TransactionConnection;

public class AppOrderDAOImpl extends OperatorDAO implements AppOrderDAO {
	private static final Logger logger = LoggerFactory.getLogger(AppOrderDAOImpl.class);
	private Gson gson = CommonUtils.getGson();
	
	public static final String APP_ORDER_HEADER_FLD_LIST = "id, org_acct_id, total_price, status, entry_id, entry_datetime, update_id, update_datetime, delete_id, delete_datetime";
	public static final String APP_ORDER_DETAIL_FLD_LIST = "id, app_order_header_id, app_module_id, app_feature_id, price, quantity, extended_price, entry_id, entry_datetime, update_id, update_datetime, delete_id, delete_datetime";
	
	public AppOrderDAOImpl(JdbcDataSource sysDatasource) {
		super(sysDatasource);
	}

	@Override
	public void getAppOrders(Integer acctId, List<Integer> appOrderIds, Handler<AsyncResult<List<AppOrder>>> done) {
		logger.debug("getAppOrders-->begin...");
		Future<List<AppOrder>> retFuture = Future.future();
		retFuture.setHandler(done);

		String[] columns = APP_ORDER_HEADER_FLD_LIST.split(",");
		JsonObject where = new JsonObject();
		where.put("org_acct_id", acctId).put("delete_datetime", (Object)null);
		String otherWhere = "";
		if (appOrderIds != null && appOrderIds.size() > 0) {
			otherWhere = String.format(" and id in (%s)", CommonUtils.toSqlIntInList(appOrderIds));
		}
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app_order_header table.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			List<AppOrder> appOrders = new ArrayList<AppOrder>();
			// id, org_acct_id, total_price, status
			for (JsonObject line : rs.getRows()) {
				logger.debug("line-->" + line);
				AppOrder appOrder = gson.fromJson(line.encode(), AppOrder.class);
				appOrders.add(appOrder);
			}
			
			if (appOrders == null || appOrders.size() == 0) {
				retFuture.complete(null);
				return;
			}
			
			// 1.1 Get ID list based on the result from step 1.
			List<Integer> appOrderHeaderIds = new ArrayList<Integer>();
			appOrders.forEach(appOrder -> {
				appOrderHeaderIds.add(appOrder.getId());
			});

			// 2. Get app_order_detail
			getAppOrderDetails(appOrderHeaderIds, retAppOrderDetail -> {
				if (retAppOrderDetail.failed()) {
					logger.error(retAppOrderDetail.cause());
					retFuture.fail(retAppOrderDetail.cause());
					return;
				}
				
				Map<Integer, List<AppOrderDetail>> appOrderDetailMap = retAppOrderDetail.result();
				appOrders.forEach(appOrder -> {
					appOrder.setAppOrderDetails(appOrderDetailMap.get(appOrder.getId()));
				});
				logger.debug("appOrders-->" + appOrders);
				retFuture.complete(appOrders);
			});
			
		});
		
		super.queryBy("app_order_header", columns, where, otherWhere, queryFuture);
		
	}

	@Override
	public void getAppOrders(Integer acctId, Handler<AsyncResult<List<AppOrder>>> done) {
		logger.debug("getAppOrders-->begin --> acctId --> " + acctId.toString());
		getAppOrders(acctId, null, done);
	}

	@Override
	public void getAppOrder(Integer acctId, Integer orderId, Handler<AsyncResult<AppOrder>> done) {
		logger.debug("getAppOrder-->begin --> acctId --> " + acctId.toString() + ", orderId-->" + orderId);
		Future<AppOrder> retFuture = Future.future();
		retFuture.setHandler(done);

		List<Integer> orderIds = new ArrayList<Integer>();
		orderIds.add(orderId);

		getAppOrders(acctId, orderIds, ret -> {
			if (ret.succeeded()) {
				List<AppOrder> appOrders = ret.result();
				if (appOrders == null || appOrders.size() == 0) {
					retFuture.complete(null);
					return;
				}
				retFuture.complete(appOrders.get(0));
			} else {
				logger.error("Failed to load app order from app_order_header/app_order_detail", ret.cause());
				retFuture.fail(ret.cause());
			}

		});
	}

	@Override
	public void getAppOrderDetails(List<Integer> appOrderIds, Handler<AsyncResult<Map<Integer, List<AppOrderDetail>>>> done) {
		logger.debug("getAppOrderDetails-->start...");
		Future<Map<Integer, List<AppOrderDetail>>> retFuture = Future.future();
		retFuture.setHandler(done);
		if (appOrderIds == null || appOrderIds.size() == 0) {
			retFuture.complete(null);
			return;
		}
		
		String[] columns = APP_ORDER_DETAIL_FLD_LIST.split(",");
		JsonObject where = new JsonObject();
		where.put("delete_datetime", (Object)null);
		
		String otherWhere = "";
		if (appOrderIds != null && appOrderIds.size() > 0) {
			otherWhere = String.format("and app_order_header_id in (%s)", CommonUtils.toSqlIntInList(appOrderIds));
		}
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app_order_detail table.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			Map<Integer, List<AppOrderDetail>> appOrderDetailsMap = new HashMap<Integer, List<AppOrderDetail>>();
			// id, org_acct_id, total_price, status
			for (JsonObject line : rs.getRows()) {
				logger.debug("line-->" + line);
				AppOrderDetail appOrderDetail = gson.fromJson(line.encode(), AppOrderDetail.class);
				if (!appOrderDetailsMap.containsKey(appOrderDetail.getAppOrderHeaderId())) {
					appOrderDetailsMap.put(appOrderDetail.getAppOrderHeaderId(), new ArrayList<AppOrderDetail>());
				}
				appOrderDetailsMap.get(appOrderDetail.getAppOrderHeaderId()).add(appOrderDetail);
			}
			retFuture.complete(appOrderDetailsMap);
		});
		
		super.queryBy("app_order_detail", columns, where, otherWhere, queryFuture);
	}

	@Override
	public void saveAppOrder(TransactionConnection transConn, AppOrder appOrder, Integer operatorId,
			Handler<AsyncResult<AppOrder>> done) {
		logger.debug("saveAppOrder-->start...");
		Future<AppOrder> retFuture = Future.future();
		retFuture.setHandler(done);
		
		Future<UpdateResult> appOrderFuture = Future.future();
		appOrderFuture.setHandler(appOrderRet -> {
			if (appOrderRet.succeeded()) {
				Integer id = appOrderRet.result().getKeys().getInteger(0);
				appOrder.setId(id);
				
				if (appOrder.getAppOrderDetails() == null || appOrder.getAppOrderDetails().size() == 0) {
					retFuture.complete(appOrder);
					return;
				}
				
				saveAppOrderDetails(transConn, appOrder.getAppOrderDetails(), id, operatorId, appOrderDetailsRet -> {
					if (appOrderDetailsRet.succeeded()) {
						appOrder.setAppOrderDetails(appOrderDetailsRet.result());
						retFuture.complete(appOrder);
					} else {
						logger.error(appOrderDetailsRet.cause());
						retFuture.fail(appOrderDetailsRet.cause());
						return;
					}
				});
			} else {
				logger.error("Failed to insert app_order_header.", appOrderRet.cause());
				retFuture.fail(appOrderRet.cause());
				return;
			}
		});

		// org_acct_id, total_price, status
		String insertValuesStr = gson.toJson(appOrder);
		JsonObject insertValues = new JsonObject(insertValuesStr);
		//删除订单明细Json对象，只保留订单头。
		insertValues.remove("app_order_details");

		super.insertBy(transConn, "app_order_header", insertValues, operatorId, appOrderFuture);
	}
	
	@Override
	public void saveAppOrderDetails(TransactionConnection transConn, List<AppOrderDetail> appOrderDetails, Integer appOrderHeaderId,
			Integer operatorId, Handler<AsyncResult<List<AppOrderDetail>>> done) {
		logger.debug("saveAppOrderDetail-->start...");
		 Future<List<AppOrderDetail>> retFuture = Future.future();
		 retFuture.setHandler(done);
		
		AtomicInteger counter = new AtomicInteger(appOrderDetails.size());

		for (AppOrderDetail appOrderDetail : appOrderDetails) {
			appOrderDetail.setAppOrderHeaderId(appOrderHeaderId);
			Future<UpdateResult> appOrderDetailFuture = Future.future();
			
			appOrderDetailFuture.setHandler(appOrderDetailRet -> {
				if (appOrderDetailRet.succeeded()) {
					UpdateResult updateResult = appOrderDetailRet.result();
					appOrderDetail.setId(updateResult.getKeys().getInteger(0));
					int curCounter = counter.decrementAndGet();
					logger.debug("Insert app_order_detail, curCounter: " + curCounter + ", app_order_detail.id: " + updateResult.getKeys().getInteger(0));
					if (curCounter <= 0) {
						retFuture.complete(appOrderDetails);
						return;
					}
				} else {
					logger.error("Failed to insert app_order_detail.", appOrderDetailRet.cause());
					retFuture.fail(appOrderDetailRet.cause());
					return;
				}
			});
			
			// app_order_header_id, app_module_id, app_feature_id, price, quantity,
			// extended_price, entry_id
			String insertValuesStr = gson.toJson(appOrderDetail);
			JsonObject insertValues = new JsonObject(insertValuesStr);			
			super.insertBy(transConn, "app_order_detail", insertValues, operatorId, appOrderDetailFuture);
		}
		
	}

	@Override
	public void updateAppOrder(TransactionConnection transConn, AppOrder appOrder, Integer operatorId,
			Handler<AsyncResult<AppOrder>> done) {
		logger.debug("updateAppOrder-->start...");
		Future<AppOrder> retFuture = Future.future();
		retFuture.setHandler(done);
		
		Integer id = appOrder.getId();
		Future<UpdateResult> appOrderFuture = Future.future();
		appOrderFuture.setHandler(appOrderRet -> {
			if (appOrderRet.failed()) {
				logger.error("Failed to update app_order_header.", appOrderRet.cause());
				retFuture.fail(appOrderRet.cause());
				return;
			}
			
			if (appOrderRet.result().getUpdated() <= 0) {
				logger.error("Not update any rows in app_order for id#" + id);
				retFuture.fail(new Exception("Not update any rows in app_order for id#" + id));
				return;
			}
			
			if (appOrder.getAppOrderDetails() == null || appOrder.getAppOrderDetails().size() == 0) {
				retFuture.complete(appOrder);
				return;
			}
			
			updateAppOrderDetails(transConn, appOrder.getAppOrderDetails(), id, operatorId, appOrderDetailsRet -> {
				if (appOrderDetailsRet.failed()) {
					logger.error(appOrderDetailsRet.cause());
					retFuture.fail(appOrderDetailsRet.cause());
					return;
				}
				
				appOrder.setAppOrderDetails(appOrderDetailsRet.result());
				retFuture.complete(appOrder);
			});
		});

		// org_acct_id, total_price, status
		String jsonStr = gson.toJson(appOrder);
		JsonObject setValues = new JsonObject(jsonStr);
		setValues.remove("id");
		//删除订单明细Json对象，只保留订单头。
		setValues.remove("app_order_details");
		JsonObject where = new JsonObject().put("id", appOrder.getId());
		super.updateBy(transConn, "app_order_header", setValues, where, operatorId, appOrderFuture);
	}
	
	@Override
	public void updateAppOrderDetails(TransactionConnection transConn, List<AppOrderDetail> appOrderDetails, Integer appOrderHeaderId,
			Integer operatorId, Handler<AsyncResult<List<AppOrderDetail>>> done) {
		logger.debug("updateAppOrderDetails-->start...");
		 Future<List<AppOrderDetail>> retFuture = Future.future();
		 retFuture.setHandler(done);

		AtomicInteger counter = new AtomicInteger(appOrderDetails.size());

		for (AppOrderDetail appOrderDetail : appOrderDetails) {
			appOrderDetail.setAppOrderHeaderId(appOrderHeaderId);
			Future<UpdateResult> appOrderDetailFuture = Future.future();
			
			appOrderDetailFuture.setHandler(appOrderDetailRet -> {
				if (appOrderDetailRet.failed()) {
					logger.error("Failed to update app_order_detail.", appOrderDetailRet.cause());
					retFuture.fail(appOrderDetailRet.cause());
					return;
				}
				
				UpdateResult updateResult = appOrderDetailRet.result();
				if (updateResult.getUpdated() <= 0) {
					logger.error("Not update any rows for id#" + appOrderDetail.getId());
					retFuture.fail(new Exception("Not update any rows in app_order_detail for id#" + appOrderDetail.getId()));
					return;
				}
				
				int curCounter = counter.decrementAndGet();
				logger.debug("Update app_order_detail, curCounter: " + curCounter);
				if (curCounter <= 0) {
					retFuture.complete(appOrderDetails);
					return;
				}
			});
			
			// app_order_header_id, app_module_id, app_feature_id, price, quantity,
			// extended_price, entry_id
			String setValuesStr = gson.toJson(appOrderDetail);
			JsonObject setValues = new JsonObject(setValuesStr);
			setValues.remove("id");
			JsonObject where = new JsonObject().put("id", appOrderDetail.getId());
			super.updateBy(transConn, "app_order_detail", setValues, where, operatorId, appOrderDetailFuture);
		}
		
	}

	@Override
	public void deleteAppOrder(TransactionConnection transConn, Integer id, Integer operatorId,
			Handler<AsyncResult<JsonObject>> done) {
		logger.debug("deleteAppOrder-->start...");
		Future<JsonObject> retFuture = Future.future();
		retFuture.setHandler(done);
		JsonObject updateResult = new JsonObject();
		
		Future<UpdateResult> appOrderFuture = Future.future();
		appOrderFuture.setHandler(appOrderRet -> {
			if (appOrderRet.failed()) {
				logger.error("Failed to delete app_order_header.", appOrderRet.cause());
				retFuture.fail(appOrderRet.cause());
				return;
			}
			UpdateResult headerUpdateResult = appOrderRet.result();
			if (headerUpdateResult.getUpdated() <= 0) {
				logger.error("Not delete any rows in app_order for id#" + id);
				retFuture.fail(new Exception("Not delete any rows in app_order for id#" + id));
				return;
			}
			updateResult.put("app_order_header", new JsonObject(gson.toJson(headerUpdateResult)));
			deleteAppOrderDetails(transConn, id, operatorId, appOrderDetailsRet -> {
				if (appOrderDetailsRet.failed()) {
					logger.error(appOrderDetailsRet.cause());
					retFuture.fail(appOrderDetailsRet.cause());
					return;
				}
				
				updateResult.put("app_order_detail", new JsonObject(gson.toJson(appOrderDetailsRet.result())));
				retFuture.complete(updateResult);
			});
		});

		// org_acct_id, total_price, status
		JsonObject where = new JsonObject().put("id", id);		
		super.deleteBy(transConn, "app_order_header", where, operatorId, appOrderFuture);
	}
	
	@Override
	public void deleteAppOrderDetails(TransactionConnection transConn, Integer appOrderHeaderId,
			Integer operatorId, Handler<AsyncResult<UpdateResult>> done) {
		logger.debug("deleteAppOrderDetails-->start...");
		Future<UpdateResult> retFuture = Future.future();
		retFuture.setHandler(done);
			
		// app_order_header_id, app_module_id, app_feature_id, price, quantity,
		// extended_price, entry_id
		JsonObject where = new JsonObject().put("app_order_header_id", appOrderHeaderId);
		super.deleteBy(transConn, "app_order_detail", where, operatorId, retFuture);
		
	}

}
