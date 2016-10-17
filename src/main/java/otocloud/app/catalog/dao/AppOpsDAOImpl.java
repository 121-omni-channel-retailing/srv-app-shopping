package otocloud.app.catalog.dao;

import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import otocloud.app.common.CommonUtils;
import otocloud.persistence.dao.JdbcDataSource;
import otocloud.persistence.dao.OperatorDAO;

/**
 * AppOpsDAOImpl数据访问接口实现。
 * 
 * @author liuxba
 *
 */
public class AppOpsDAOImpl extends OperatorDAO implements AppOpsDAO {
	private static final Logger logger = LoggerFactory.getLogger(AppOpsDAOImpl.class);
	
	private static String SQL_GET_APP_DEPLOY_INFO = 
			"select a.id, a.version, b.code, c.group_id, c.artifact_id" + 
				" from app_version a inner join app_module b on a.app_module_id = b.id" + 
				" inner join app_module_pkg c on b.id = c.id" + 
				" where 1=1";
	
	public AppOpsDAOImpl(JdbcDataSource sysDatasource) {
		super(sysDatasource);
	}
	
	public void getAppDeployInfo(List<Integer> appVersionIds, Handler<AsyncResult<JsonObject>> done) {

		logger.debug("getAppDeployInfo-->begin...");
		Future<JsonObject> retFuture = Future.future();
		retFuture.setHandler(done);
		
		String sql = SQL_GET_APP_DEPLOY_INFO;
		if (appVersionIds != null && appVersionIds.size() > 0) {
			sql += " and a.id in (%s)";
			sql = String.format(sql, CommonUtils.toSqlIntInList(appVersionIds));
		}
		
		JsonArray params = new JsonArray();
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app deployment info.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			JsonObject appDeployInfos = new JsonObject();
			// id, org_acct_id, total_price, status
			for (JsonObject line: rs.getRows()) {
				logger.debug("line-->" + line);
				JsonObject jsonObj = new JsonObject();
				jsonObj.put("group_id", line.getString("group_id"));
				jsonObj.put("artifact_id", line.getString("artifact_id"));
				jsonObj.put("version", line.getString("version"));
				jsonObj.put("service_name", line.getString("code"));
				
				appDeployInfos.put(line.getInteger("id").toString(), jsonObj);
				
			}
			logger.debug("appDeployInfos-->" + appDeployInfos);
			retFuture.complete(appDeployInfos);
		});
				
		super.queryWithParams(sql, params, queryFuture);
	}
}
