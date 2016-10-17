package otocloud.app.catalog.dao;

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
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import otocloud.app.AppCatalogOrderService;
import otocloud.app.catalog.model.AppActivity;
import otocloud.app.catalog.model.AppFeature;
import otocloud.app.catalog.model.AppModule;
import otocloud.app.catalog.model.AppModuleInfoExtra;
import otocloud.app.catalog.model.AppPrice;
import otocloud.framework.core.OtoCloudServiceForVerticleImpl;
import otocloud.test.base.OtoCloudTestBase;

@RunWith(VertxUnitRunner.class)
public class AppCatalogDAOImplTest extends OtoCloudTestBase<AppCatalogOrderService> {
	private static final Logger logger = LoggerFactory.getLogger(AppCatalogDAOImplTest.class);
	private AppCatalogDAO appCatalogDAO = null;
	private SQLConnection conn;
	
	//保存app_module主键，供其他测试使用。
	private List<Integer> appModuleIds = new ArrayList<Integer>();
	
	@Before
	public void Prepare(TestContext context) throws IOException {
		final Async async = context.async();
		//Service采用单例模式，必须使用getService()函数得到service，确保在service返回之前已经初始化。
		appCatalogDAO = new AppCatalogDAOImpl(getService().getSysDatasource());
		//1. Get DB connection
		JDBCClient jdbcClient = getService().getSysDatasource().getSqlClient();
		jdbcClient.getConnection(connRes -> {
			if (connRes.succeeded()) {
				conn = connRes.result();
			} else {
				logger.error("Failed to connect DB", connRes.cause());
			}
			async.complete();
		});
		
		//2. 初始化appModuleIds
		appModuleIds.add(new Integer(1));
		appModuleIds.add(new Integer(2));
		appModuleIds.add(new Integer(3));		
	}
	
	@Test
	public void it_should_return_muliple_modules_with_valid_module_ids(TestContext context) {
		final Async async = context.async();

		appCatalogDAO.getAppModules(null, rsRes -> {
			if (rsRes.succeeded()) {
				List<AppModule> appModules = rsRes.result();
				//1. Execute tested function and assert returned result.
				int size = appModules.size();
				context.assertTrue(size > 0, "Not found any rows in app_module table.");
				String jsonStr = Json.encodePrettily(appModules);
				logger.debug("appModules-->" + jsonStr);
				JsonArray jsonArray = new JsonArray(jsonStr);
				context.assertTrue(jsonArray.size() == size, "Failed to tranfer to JsonArray.");
				
				async.complete();
			}
		});
	}
	
	@Test
	public void it_should_return_muliple_features_with_valid_module_ids(TestContext context) {
		final Async async = context.async();
		
		appCatalogDAO.getAppFeatures(appModuleIds, rsRes -> {
			if (rsRes.succeeded()) {
				Map<Integer, List<AppFeature>> appFeaturesMap = rsRes.result();
				//1. Execute tested function and assert returned result.
				int size = appFeaturesMap.size();
				context.assertTrue(size > 0, "Not found any rows in app_feature table.");
				appFeaturesMap.forEach((appModuleId, appFeatures) -> {
					int size2 = appFeatures.size();
					String jsonStr = Json.encodePrettily(appFeatures);
					logger.debug("appFeatures-->appModuleId" + appModuleId + ": " + jsonStr);
					JsonArray jsonArray = new JsonArray(jsonStr);
					context.assertTrue(jsonArray.size() == size2, "Failed to tranfer to JsonArray.");					
				});
				
				async.complete();
			}
		});
	}
	
	@Test
	public void it_should_return_muliple_activities_with_valid_module_ids(TestContext context) {
		logger.debug("testGetActivities-->start...");
		final Async async = context.async();

		appCatalogDAO.getAppActivities(appModuleIds, rsRes -> {
			if (rsRes.succeeded()) {
				Map<Integer, List<AppActivity>> appActivitiesMap = rsRes.result();
				//1. Execute tested function and assert returned result.
				int size = appActivitiesMap.size();
				context.assertTrue(size > 0, "Not found any rows in app_Activity table.");
				appActivitiesMap.forEach((appModuleId, appActivities) -> {
					int size2 = appActivities.size();
					String jsonStr = Json.encodePrettily(appActivities);
					logger.debug("appActivities-->appModuleId" + appModuleId + ": " + jsonStr);
					JsonArray jsonArray = new JsonArray(jsonStr);
					context.assertTrue(jsonArray.size() == size2, "Failed to tranfer to JsonArray.");					
				});
				
				async.complete();
			}
		});
	}
	
	@Test
	public void it_should_return_muliple_module_info_extra_with_valid_module_ids(TestContext context) {
		logger.debug("testGetAppModuleInfoExtras-->start...");
		final Async async = context.async();

		appCatalogDAO.getAppModuleInfoExtras(appModuleIds, rsRes -> {
			if (rsRes.succeeded()) {
				Map<Integer, List<AppModuleInfoExtra>> AppModuleInfoExtrasMap = rsRes.result();
				//1. Execute tested function and assert returned result.
				int size = AppModuleInfoExtrasMap.size();
				context.assertTrue(size > 0, "Not found any rows in app_module_info_extra table.");
				AppModuleInfoExtrasMap.forEach((appModuleId, AppModuleInfoExtras) -> {
					int size2 = AppModuleInfoExtras.size();
					String jsonStr = Json.encodePrettily(AppModuleInfoExtras);
					logger.debug("AppModuleInfoExtras-->appModuleId" + appModuleId + ": " + jsonStr);
					JsonArray jsonArray = new JsonArray(jsonStr);
					context.assertTrue(jsonArray.size() == size2, "Failed to tranfer to JsonArray.");					
				});
				
				async.complete();
			}
		});
	
	}
	
	@Test
	public void it_should_return_muliple_prices_with_valid_module_ids(TestContext context) {
		final Async async = context.async();

		appCatalogDAO.getAppPrices(appModuleIds, rsRes -> {
			if (rsRes.succeeded()) {
				Map<String, List<AppPrice>> appPricesMap = rsRes.result();
				//1. Execute tested function and assert returned result.
				int size = appPricesMap.size();
				context.assertTrue(size > 0, "Not found any rows in app_price table.");
				appPricesMap.forEach((key, appPrices) -> {
					logger.debug("key-->" + key);
					String jsonStr = Json.encodePrettily(appPrices);
					logger.debug("appPrices-->" + jsonStr);
					JsonArray jsonArray = new JsonArray(jsonStr);
					context.assertTrue(jsonArray.size() > 0, "Failed to tranfer to JsonArray.");
					appPrices.forEach(appPrice -> {
						String jsonStrAppPrice = Json.encodePrettily(appPrice); 
						AppPrice appPrice2 = Json.decodeValue(jsonStrAppPrice, AppPrice.class);
						logger.debug("appPrice-->" + appPrice2);
					});
					
				});
				async.complete();
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
	
}
