package otocloud.app.order.dao;

import java.util.List;
import java.util.Map;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import otocloud.app.order.model.AppOrder;
import otocloud.app.order.model.AppOrderDetail;
import otocloud.persistence.dao.TransactionConnection;

/**
 * 订单访问DAO
 * @author liuxba
 *
 */
public interface AppOrderDAO {
	public void getAppOrders(Integer acctId, Handler<AsyncResult<List<AppOrder>>> done);

	public void getAppOrders(Integer acctId, List<Integer> appOrderIds, Handler<AsyncResult<List<AppOrder>>> done);

	public void getAppOrderDetails(List<Integer> appOrderIds, Handler<AsyncResult<Map<Integer, List<AppOrderDetail>>>> done);

	public void getAppOrder(Integer acctId, Integer orderId, Handler<AsyncResult<AppOrder>> done);

	public void saveAppOrder(TransactionConnection conn, AppOrder appOrder, Integer operatorId,
			Handler<AsyncResult<AppOrder>> done);

	public void saveAppOrderDetails(TransactionConnection conn, List<AppOrderDetail> appOrderDetail, Integer appOrderHeaderId,
			Integer operatorId, Handler<AsyncResult<List<AppOrderDetail>>> done);
	
	public void updateAppOrder(TransactionConnection conn, AppOrder appOrder, Integer operatorId,
			Handler<AsyncResult<AppOrder>> done);

	public void updateAppOrderDetails(TransactionConnection conn, List<AppOrderDetail> appOrderDetail, Integer appOrderHeaderId,
			Integer operatorId, Handler<AsyncResult<List<AppOrderDetail>>> done);
	
	public void deleteAppOrder(TransactionConnection conn, Integer appOrderHeaderId, Integer operatorId,
			Handler<AsyncResult<JsonObject>> done);

	public void deleteAppOrderDetails(TransactionConnection conn, Integer appOrderHeaderId,
			Integer operatorId, Handler<AsyncResult<UpdateResult>> done);
	
}
