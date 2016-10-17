package otocloud.app.catalog.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import otocloud.app.AppCatalogOrderService;
import otocloud.app.common.MessageSchema;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.OtoCloudServiceForVerticleImpl;
import otocloud.test.base.OtoCloudTestBase;

/**
 * This is our JUnit test for our verticle. The test uses vertx-unit, so we
 * declare a custom runner.
 */
@RunWith(VertxUnitRunner.class)
public class AppOpsDeployInfoServiceTest extends OtoCloudTestBase<AppCatalogOrderService> {
	static final Logger logger = LoggerFactory.getLogger(AppOpsDeployInfoServiceTest.class);
	private JsonObject message = new JsonObject();
	private JsonObject messageWithNoResult = new JsonObject();
	
	static final String APP_VERSION_IDS_VALID = "1,2,3";
	static final String APP_VERSION_IDS_INVALID = "-1,-2,-3";
	
	@Before
	public void prepare(TestContext context) {
		JsonObject session = new JsonObject().put(MessageBodyConvention.SESSION_ACCT_ID, new Integer(1))
				.put(MessageBodyConvention.SESSION_USER_ID, new Integer(1));
		JsonObject queryParams = new JsonObject().put("ids", APP_VERSION_IDS_VALID);
		message.put(MessageBodyConvention.SESSION, session);
		message.put(MessageBodyConvention.HTTP_QUERY, queryParams);
		message.put(MessageBodyConvention.HTTP_BODY, new JsonObject());
		
		JsonObject sessionWithNoResult = new JsonObject().put(MessageBodyConvention.SESSION_ACCT_ID, new Integer(-1))
				.put(MessageBodyConvention.SESSION_USER_ID, new Integer(-1));
		JsonObject queryParamsWithNoResult = new JsonObject().put("ids", APP_VERSION_IDS_INVALID);
		messageWithNoResult.put(MessageBodyConvention.SESSION, sessionWithNoResult);
		messageWithNoResult.put(MessageBodyConvention.HTTP_QUERY, queryParamsWithNoResult);
		messageWithNoResult.put(MessageBodyConvention.HTTP_BODY, new JsonObject());
	}
	
	@Test
	public void it_should_return_multiple_ops_deploy_info_with_valid_version_ids(TestContext context) {
		final Async async = context.async();

		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_OPS_DEPLOY_INFO_GETALL, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();
			JsonObject appOpsDeployInfo = (JsonObject)msg.body();
			context.assertNotNull(appOpsDeployInfo, "没有返回appOpsDeployInfo数据。");
			//没有数据时，返回“{}”
			context.assertNotEquals("{}", appOpsDeployInfo.encode(), "没有返回有效的appOpsDeployInfo数据。");

			logger.debug("appOpsDeployInfo-->" + appOpsDeployInfo);
			async.complete();
		});
	}

	@Test
	public void it_should_return_no_ops_deploy_info_with_invalid_version_ids(TestContext context) {
		final Async async = context.async();

		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_OPS_DEPLOY_INFO_GETALL, messageWithNoResult, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();
			JsonObject appOpsDeployInfo = (JsonObject)msg.body();
//			context.assertNotNull(appOpsDeployInfo, "没有返回appOpsDeployInfo数据。");
			context.assertEquals("{}", appOpsDeployInfo.encode(), "不应该有返回appOpsDeployInfo数据。");

			logger.debug("appOpsDeployInfo-->" + appOpsDeployInfo);
			async.complete();
		});
	}
}
