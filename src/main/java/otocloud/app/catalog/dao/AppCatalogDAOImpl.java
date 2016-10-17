package otocloud.app.catalog.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import otocloud.app.catalog.model.AppActivity;
import otocloud.app.catalog.model.AppFeature;
import otocloud.app.catalog.model.AppModule;
import otocloud.app.catalog.model.AppModuleInfoExtra;
import otocloud.app.catalog.model.AppModuleInfoItem;
import otocloud.app.catalog.model.AppPrice;
import otocloud.app.common.CommonUtils;
import otocloud.persistence.dao.JdbcDataSource;
import otocloud.persistence.dao.OperatorDAO;

/**
 * AppCatalog数据访问接口。
 * 
 * @author liuxba
 *
 */
public class AppCatalogDAOImpl extends OperatorDAO implements AppCatalogDAO {
	private static final Logger logger = LoggerFactory.getLogger(AppCatalogDAOImpl.class);
	private Gson gson = CommonUtils.getGson();
	
	public static final String APP_MODULE_FLD_LIST = "id, code, name, short_desc, long_desc, app_type_code, status, entry_id, entry_datetime, update_id, update_datetime, delete_id, delete_datetime";
	public static final String APP_FEATURE_FLD_LIST = "id, app_module_id, feature_code, feature_name, feature_desc, entry_id, entry_datetime, update_id, update_datetime, delete_id, delete_datetime";
	public static final String APP_PRICE_FLD_LIST = "id, app_module_id, app_feature_id, price, since_date, expire_date, entry_id, entry_datetime, update_id, update_datetime, delete_id, delete_datetime";
	public static final String APP_MODULE_INFO_EXTRA_FLD_LIST = "id, app_module_id, app_module_info_item_id, seq_no, item_value, entry_id, entry_datetime, update_id, update_datetime, delete_id, delete_datetime";
	public static final String APP_MODULE_INFO_ITEM_FLD_LIST = "id, segment_name, item_name, seq_no, data_type, status, entry_id, entry_datetime, entry_id, entry_datetime, update_id, update_datetime, delete_id, delete_datetime";
	public static final String APP_ACTIVITY_FLD_LIST = "id, app_module_id, activity_code, activity_name, activity_desc, entry_id, entry_datetime, update_id, update_datetime, delete_id, delete_datetime";
	
	public AppCatalogDAOImpl(JdbcDataSource sysDatasource) {
		super(sysDatasource);
	}
	
	@Override
	public void getAppModules(List<Integer> appModulesIds, Handler<AsyncResult<List<AppModule>>> done) {
		logger.debug("getAppModules-->begin...");
		Future<List<AppModule>> retFuture = Future.future();
		retFuture.setHandler(done);
		
		String[] columns = APP_MODULE_FLD_LIST.split(",");
		JsonObject where = new JsonObject();
		where.put("status", "A").put("delete_datetime", (Object)null);
		String otherWhere = "";
		if (appModulesIds != null && appModulesIds.size() > 0) {
			otherWhere = String.format(" and id in (%s)", CommonUtils.toSqlIntInList(appModulesIds));
		}
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app_module table.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			List<AppModule> appModules = new ArrayList<AppModule>();
			// id, org_acct_id, total_price, status
			for (JsonObject line : rs.getRows()) {
				logger.debug("line-->" + line);
				AppModule appModule = gson.fromJson(line.encode(), AppModule.class);
				appModules.add(appModule);
			}
			retFuture.complete(appModules);
		});
				
		super.queryBy("app_module", columns, where, otherWhere, queryFuture);
	}
	
	/**
	 * 查询app_feature表，按照app_module方式返回。
	 * @param done
	 * @param searchCriteria app_module的列表
	 */
	@Override
	public void getAppFeatures(List<Integer> appModulesIds, Handler<AsyncResult<Map<Integer, List<AppFeature>>>> done) {
		logger.debug("getAppFeatures-->start...");
		Future<Map<Integer, List<AppFeature>>> retFuture = Future.future();
		retFuture.setHandler(done);
		
		String[] columns = APP_FEATURE_FLD_LIST.split(",");
		JsonObject where = new JsonObject();
		where.put("delete_datetime", (Object)null);
		String otherWhere = "";
		if (appModulesIds != null && appModulesIds.size() > 0) {
			otherWhere = String.format(" and app_module_id in (%s)", CommonUtils.toSqlIntInList(appModulesIds));
		}
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app_feature table.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			Map<Integer,List<AppFeature>> appFeaturesMap = new HashMap<Integer,List<AppFeature>>();
			// id, org_acct_id, total_price, status
			for (JsonObject line : rs.getRows()) {
				logger.debug("line-->" + line);
				AppFeature appFeature = gson.fromJson(line.encode(), AppFeature.class);

				if (!appFeaturesMap.containsKey(appFeature.getAppModuleId())) {
					appFeaturesMap.put(appFeature.getAppModuleId(), new ArrayList<AppFeature>());
				}
				appFeaturesMap.get(appFeature.getAppModuleId()).add(appFeature);
			}
			retFuture.complete(appFeaturesMap);
		});

		super.queryBy("app_feature", columns, where, otherWhere, queryFuture);
	}
	
	/**
	 * 
	 */
	@Override
	public void getAppPrices(List<Integer> appModulesIds, Handler<AsyncResult<Map<String, List<AppPrice>>>> done) {
		logger.debug("getAppPrices-->start...");
		Future<Map<String, List<AppPrice>>> retFuture = Future.future();
		retFuture.setHandler(done);
		
		String[] columns = APP_PRICE_FLD_LIST.split(",");
		JsonObject where = new JsonObject();
		where.put("delete_datetime", (Object)null);
		String otherWhere = " and since_date <= CURDATE() and (expire_date is null or expire_date>CURDATE())";
		if (appModulesIds != null && appModulesIds.size() > 0) {
			otherWhere += String.format(" and app_module_id in (%s)", CommonUtils.toSqlIntInList(appModulesIds));
		}
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app_price table.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			Map<String,List<AppPrice>> appPricesMap = new HashMap<String,List<AppPrice>>();
			// id, org_acct_id, total_price, status
			for (JsonObject line : rs.getRows()) {
				logger.debug("line-->" + line);
				AppPrice appPrice = gson.fromJson(line.encode(), AppPrice.class);
		
				if (!appPricesMap.containsKey(getKey(appPrice))) {
					appPricesMap.put(getKey(appPrice), new ArrayList<AppPrice>());
				}
				appPricesMap.get(getKey(appPrice)).add(appPrice);
			}
			retFuture.complete(appPricesMap);
		});

		super.queryBy("app_price", columns, where, otherWhere, queryFuture);
	}

	/**
	 * 返回应用的附加信息，即自定义应用信息。
	 */
	@Override
	public void getAppModuleInfoExtras(List<Integer> appModulesIds, Handler<AsyncResult<Map<Integer, List<AppModuleInfoExtra>>>> done) {
		logger.debug("AppModuleInfoExtra-->start...");
		Future<Map<Integer, List<AppModuleInfoExtra>>> retFuture = Future.future();
		retFuture.setHandler(done);

		String[] columns = APP_MODULE_INFO_EXTRA_FLD_LIST.split(",");
		JsonObject where = new JsonObject();
		where.put("delete_datetime", (Object)null);
		String otherWhere = "";
		if (appModulesIds != null && appModulesIds.size() > 0) {
			otherWhere += String.format(" and app_module_id in (%s)", CommonUtils.toSqlIntInList(appModulesIds));
		}
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app_module_info_extra table.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			Map<Integer,List<AppModuleInfoExtra>> appModuleInfoExtrasMap = new HashMap<Integer,List<AppModuleInfoExtra>>();
			// id, org_acct_id, total_price, status
			for (JsonObject line : rs.getRows()) {
				logger.debug("line-->" + line);
				AppModuleInfoExtra appModuleInfoExtra = gson.fromJson(line.encode(), AppModuleInfoExtra.class);

				if (!appModuleInfoExtrasMap.containsKey(appModuleInfoExtra.getAppModuleId())) {
					appModuleInfoExtrasMap.put(appModuleInfoExtra.getAppModuleId(), new ArrayList<AppModuleInfoExtra>());
				}
				appModuleInfoExtrasMap.get(appModuleInfoExtra.getAppModuleId()).add(appModuleInfoExtra);
			}
			logger.debug("getAppModuleInfoExtras-->size:" + appModuleInfoExtrasMap.size());
			retFuture.complete(appModuleInfoExtrasMap);
		});

		super.queryBy("app_module_info_extra", columns, where, otherWhere, queryFuture);
	}
	
	/**
	 * 返回应用自定义信息项。
	 * @param conn
	 * @param done
	 */
	private void getAppModuleInfoItems(SQLConnection conn, Handler<AsyncResult<Map<Integer, AppModuleInfoItem>>> done) {

		logger.debug("getAppModuleInfoItems-->begin...");
		Future<Map<Integer, AppModuleInfoItem>> retFuture = Future.future();
		retFuture.setHandler(done);

		String[] columns = APP_MODULE_INFO_ITEM_FLD_LIST.split(",");
		JsonObject where = new JsonObject();
		where.put("delete_datetime", (Object)null).put("status", "A");
		String otherWhere = "";
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app_module_info_item table.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			Map<Integer,AppModuleInfoItem> appModuleInfoItemsMap = new HashMap<Integer,AppModuleInfoItem>();
			// id, org_acct_id, total_price, status
			for (JsonObject line : rs.getRows()) {
				logger.debug("line-->" + line);
				AppModuleInfoItem appModuleInfoItem = gson.fromJson(line.encode(), AppModuleInfoItem.class);

				appModuleInfoItemsMap.put(appModuleInfoItem.getId(), appModuleInfoItem);
			}
			logger.debug("getAppModuleInfoItems-->size:" + appModuleInfoItemsMap.size());
			retFuture.complete(appModuleInfoItemsMap);
		});

		super.queryBy("app_module_info_item", columns, where, otherWhere, queryFuture);
	}
	
	@Override
	public void getAppActivities(List<Integer> appModulesIds, Handler<AsyncResult<Map<Integer, List<AppActivity>>>> done) {
		logger.debug("getAppActivities-->begin...");
		Future<Map<Integer, List<AppActivity>>> retFuture = Future.future();
		retFuture.setHandler(done);

		String[] columns = APP_ACTIVITY_FLD_LIST.split(",");
		JsonObject where = new JsonObject();
		where.put("delete_datetime", (Object)null);
		String otherWhere = "";
		if (appModulesIds != null && appModulesIds.size() > 0) {
			otherWhere += String.format(" and app_module_id in (%s)", CommonUtils.toSqlIntInList(appModulesIds));
		}
		
		//Setup queryFuture
		Future<ResultSet> queryFuture = Future.future();
		queryFuture.setHandler(rsRet -> {
			if (rsRet.failed()) {
				logger.error("Failed to query app_activity table.", rsRet.cause());
				retFuture.fail(rsRet.cause());
				return;
			}
			
			ResultSet rs = rsRet.result();
			Map<Integer,List<AppActivity>> appActivitiesMap = new HashMap<Integer,List<AppActivity>>();
			// id, org_acct_id, total_price, status
			for (JsonObject line : rs.getRows()) {
				logger.debug("line-->" + line);
				AppActivity appActivity = gson.fromJson(line.encode(), AppActivity.class);

				if (!appActivitiesMap.containsKey(appActivity.getAppModuleId())) {
					appActivitiesMap.put(appActivity.getAppModuleId(), new ArrayList<AppActivity>());
				}
				appActivitiesMap.get(appActivity.getAppModuleId()).add(appActivity);
			}
			logger.debug("getAppActivities-->size:" + appActivitiesMap.size());
			retFuture.complete(appActivitiesMap);
		});

		super.queryBy("app_activity", columns, where, otherWhere, queryFuture);
	}

	/**
	 * 利用利用AppPrice中的module_id和feature_id生成中间加下划线作为key。
	 * @return
	 */
	public static String getKey(AppPrice appPrice) {
		return appPrice.getAppFeatureId() == null ? appPrice.getAppModuleId().toString()
				: appPrice.getAppModuleId().toString() + "_" + appPrice.getAppFeatureId().toString();
	}
}
