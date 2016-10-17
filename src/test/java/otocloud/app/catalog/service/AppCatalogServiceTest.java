package otocloud.app.catalog.service;

import org.junit.Test;
import org.junit.runner.RunWith;

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
import otocloud.app.catalog.model.AppModule;
import otocloud.app.common.MessageSchema;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.test.base.OtoCloudTestBase;

/**
 * This is our JUnit test for our verticle. The test uses vertx-unit, so we
 * declare a custom runner.
 */
@RunWith(VertxUnitRunner.class)
public class AppCatalogServiceTest extends OtoCloudTestBase<AppCatalogOrderService> {
	static final Logger logger = LoggerFactory.getLogger(AppCatalogServiceTest.class);
	static final int APP_CATALOG_ID_VALID = 1;
	static final int APP_CATALOG_ID_INVALID = 0;
	
	@Test
	public void it_should_return_multiple_catalogs_with_dummy_message(TestContext context) {
		final Async async = context.async();
		
		JsonObject message = new JsonObject();
		message.put(MessageBodyConvention.SESSION, new JsonObject());
		message.put(MessageBodyConvention.HTTP_BODY, new JsonObject());
		
		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_CATALOG_GETALL, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();
			JsonArray appModules = (JsonArray)msg.body();
			context.assertTrue(appModules.size()>0, "没有返回App Module数据。");
			appModules.forEach(appModuleJsonObj -> {
				logger.debug("appModuleJsonObj-->" + appModuleJsonObj);
				String str = appModuleJsonObj.toString();
				AppModule appModule = Json.decodeValue(str, AppModule.class);
				logger.debug("appModule.id:" + appModule.getId());
			});
			
			logger.debug("msg-->" + msg);
			async.complete();
		});
		
	}
	
	@Test
	public void it_should_return_one_catalog_with_valid_catalog_id(TestContext context) {
		final Async async = context.async();
		
		Integer id = new Integer(APP_CATALOG_ID_VALID);
		JsonObject message = new JsonObject();
		message.put("session", new JsonObject());
		JsonObject queryParamsObj = new JsonObject();
		queryParamsObj.put("id", id.toString());
		message.put("queryParams", queryParamsObj);
		
		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_CATALOG_GET, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();
						
			JsonObject appModuleJsonObj = (JsonObject)msg.body();
			context.assertNotNull(appModuleJsonObj, "没有返回app_module结果");
			String appModuleJsonStr = appModuleJsonObj.encodePrettily();
			logger.debug("appModuleJsonObj-->" + appModuleJsonStr);
			
			AppModule appModule = Json.decodeValue(appModuleJsonStr, AppModule.class);
			
			context.assertTrue(id.equals(appModule.getId()), "没有返回App Module数据。");
			
			logger.debug("msg-->" + msg);
			async.complete();
		});
		
	}
	
	@Test
	public void it_should_return_no_catalog_with_invalid_catalog_id(TestContext context) {
		final Async async = context.async();
		
		Integer id = new Integer(APP_CATALOG_ID_INVALID);
		JsonObject message = new JsonObject();
		message.put("session", new JsonObject());
		JsonObject queryParamsObj = new JsonObject();
		queryParamsObj.put("id", id.toString());
		message.put("queryParams", queryParamsObj);
		
		vertx.eventBus().send(MessageSchema.EVENT_ADDR_APP_CATALOG_GET, message, ret -> {
			context.assertTrue(ret.succeeded());
			Message<Object> msg = ret.result();
			
			JsonObject appModuleJsonObj = (JsonObject)msg.body();
			
			context.assertNull(appModuleJsonObj, "应该无返回结果。");
			
			async.complete();
		});
		
	}

}
