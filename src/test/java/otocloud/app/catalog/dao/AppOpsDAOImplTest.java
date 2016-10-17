package otocloud.app.catalog.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import otocloud.app.AppCatalogOrderService;
import otocloud.test.base.OtoCloudTestBase;

@RunWith(VertxUnitRunner.class)
public class AppOpsDAOImplTest extends OtoCloudTestBase<AppCatalogOrderService> {
	private static final Logger logger = LoggerFactory.getLogger(AppOpsDAOImplTest.class);
	private AppOpsDAO appOpsDAO = null;
	private SQLConnection conn;
	
	//测试用appVersionIds，有返回结果。
	private List<Integer> appVersionIds = new ArrayList<Integer>();
	//测试用appVersionIds，无返回结果。
	private List<Integer> appVersionIdsWithNoResult = new ArrayList<Integer>();
	
	@Before
	public void Prepare(TestContext context) throws IOException {
		final Async async = context.async();
		appOpsDAO = new AppOpsDAOImpl(getService().getSysDatasource());
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
		
		//2. 初始化appVersionIds
		appVersionIds.add(new Integer(1));
		appVersionIds.add(new Integer(2));
		appVersionIds.add(new Integer(3));
		
		//3. 初始化appVersionIdsWithNoResult
		appVersionIdsWithNoResult.add(new Integer(-1));
		appVersionIdsWithNoResult.add(new Integer(-2));
	}
	
	@Test
	public void it_should_return_multiple_deploy_info_with_valid_version_ids(TestContext context) {
		final Async async = context.async();

		appOpsDAO.getAppDeployInfo(appVersionIds, rsRes -> {
			context.assertTrue(rsRes.succeeded(), "查询appDeployInfo失败。");
			
			JsonObject appOpsDeplyInfo = rsRes.result();
			//1. Execute tested function and assert returned result.
			context.assertNotNull(appOpsDeplyInfo, "Not found any deploy info.");
			
			logger.debug("appOpsDeplyInfo-->" + appOpsDeplyInfo);
			async.complete();
		});
	}
	
	@Test
	public void it_should_return_no_deploy_info_with_invalid_version_ids(TestContext context) {
		final Async async = context.async();

		appOpsDAO.getAppDeployInfo(appVersionIdsWithNoResult, rsRes -> {
			context.assertTrue(rsRes.succeeded(), "查询appDeployInfo失败。");
			JsonObject appOpsDeplyInfo = rsRes.result();
			//1. Execute tested function and assert returned result.
			context.assertTrue(appOpsDeplyInfo == null || appOpsDeplyInfo.isEmpty(), "返回结果有值。");
			
			logger.debug("appOpsDeplyInfo-->" + appOpsDeplyInfo);
			async.complete();
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
