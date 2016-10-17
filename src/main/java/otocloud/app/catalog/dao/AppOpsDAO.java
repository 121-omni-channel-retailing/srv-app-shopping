package otocloud.app.catalog.dao;

import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface AppOpsDAO {
	/**
	 * 为运维系统提供部署信息
	 * @param appVersionIds
	 * @param done
	 */
	public void getAppDeployInfo(List<Integer> appVersionIds, Handler<AsyncResult<JsonObject>> done);
}
