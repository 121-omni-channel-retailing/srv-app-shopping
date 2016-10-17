package otocloud.app.catalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.app.catalog.dao.AppCatalogDAO;
import otocloud.app.catalog.dao.AppCatalogDAOImpl;
import otocloud.app.catalog.model.AppFeature;
import otocloud.app.catalog.model.AppModule;
import otocloud.app.catalog.model.AppModuleInfoExtra;
import otocloud.app.catalog.model.AppPrice;
import otocloud.app.common.CommonUtils;
import otocloud.app.common.MessageSchema;
import otocloud.common.ActionURI;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

public class AppCatalogListHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	private static final Logger logger = LoggerFactory.getLogger(AppCatalogListHandler.class);

	private AppCatalogDAO appCatalogDAO;

	public AppCatalogListHandler(OtoCloudComponentImpl componentImpl) {
		super(componentImpl);
		appCatalogDAO = new AppCatalogDAOImpl(componentImpl.getSysDatasource());
	}

	@Override
	public void handle(OtoCloudBusMessage<JsonObject> msg) {
		logger.debug("handle-->msg: " + msg);
		JsonObject body = msg.body();

		// 获得消息体中的 content 部分.
		// JsonObject content =
		// body.getJsonObject(MessageBodyConvention.HTTP_BODY);

		// 获得消息体中的 queryParams 部分.
		// JsonObject params =
		// body.getJsonObject(MessageBodyConvention.HTTP_QUERY);

		// 获得消息体中的 session 部分.
		JsonObject session = body.getJsonObject(MessageBodyConvention.SESSION);
		
		Gson gson = CommonUtils.getGson();
		
		// 1. 查询appModules
		appCatalogDAO.getAppModules(null, daoRet -> {
			if (daoRet.failed()) {
				Throwable err = daoRet.cause();
				String errMsg = err.getMessage();
				logger.error(errMsg, err);
				msg.fail(400, err.getMessage());
				return;
			} else {
				List<AppModule> appModules = daoRet.result();
				logger.debug("appModules-->" + appModules.toString());

				// 1.1 没有查询到结果，空值返回
				if (appModules == null || appModules.size() == 0) {
					msg.reply(null);
					return;
				}

				// 1.2 得到appModules主键列表
				List<Integer> appModulesIds = new ArrayList<Integer>();
				appModules.forEach(appModule -> {
					appModulesIds.add(new Integer(appModule.getId()));
				});

				// 2. 查询appFeatures
				appCatalogDAO.getAppFeatures(appModulesIds, daoRet2 -> {
					if (daoRet2.succeeded()) {
						Map<Integer, List<AppFeature>> appFeaturesMap = daoRet2.result();
						appModules.forEach(appModule -> {
							appModule.setAppFeatures(appFeaturesMap.get(new Integer(appModule.getId())));
						});

						// 3. 查询价格
						appCatalogDAO.getAppPrices(appModulesIds, daoRet3 -> {
							if (daoRet3.succeeded()) {
								Map<String, List<AppPrice>> appPriceMap = daoRet3.result();
								setupAppPrices(appModules, appPriceMap);

								// 4. 查询产品附加信息
								appCatalogDAO.getAppModuleInfoExtras(appModulesIds, daoRet4 -> {
									if (daoRet4.succeeded()) {
										Map<Integer, List<AppModuleInfoExtra>> appModuleInfoExtraMap = daoRet4.result();
										setAppModuleInfoExtras(appModules, appModuleInfoExtraMap);
									} else {
										Throwable err = daoRet4.cause();
										logger.error("访问app_module_info_extra/app_module_info_item表错误", err);
										msg.fail(400, err.getMessage());
									}
									String appModulesStr = gson.toJson(appModules);
									logger.debug("appModulesStr-->" + appModulesStr);
									JsonArray appModulesArray = new JsonArray(appModulesStr);
									msg.reply(appModulesArray);
								});

							} else {
								Throwable err = daoRet3.cause();
								logger.error("访问app_price表错误", err);
								msg.fail(400, err.getMessage());
							}
						});
					} else {
						Throwable err = daoRet2.cause();
						logger.error("访问app_feature表错误", err);
						msg.fail(400, err.getMessage());
					}
				});

			}

		});

	}

	private void setAppModuleInfoExtras(List<AppModule> appModules,
			Map<Integer, List<AppModuleInfoExtra>> appModuleInfoExtraMap) {
		// 设置appModule中AppModuleInfoExtra
		appModules.forEach(appModule -> {
			appModule.setAppModuleInfoExtras(appModuleInfoExtraMap.get(appModule.getId()));
		});
	}

	private void setupAppPrices(List<AppModule> appModules, Map<String, List<AppPrice>> appPriceMap) {
		// 设置appModule和appModule中appFeature的price
		appModules.forEach(appModule -> {
			appModule.setAppPrices(appPriceMap.get(appModule.getId().toString()));
			if (appModule.getAppFeatures() != null) {
				appModule.getAppFeatures().forEach(appFeature -> {
					appFeature.setAppPrices(
							appPriceMap.get(appFeature.getAppModuleId().toString() + "_" + appFeature.getId()));
				});
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HandlerDescriptor getHanlderDesc() {
		HandlerDescriptor handlerDescriptor = super.getHanlderDesc();

		ActionURI uri = new ActionURI("", HttpMethod.GET);
		handlerDescriptor.setRestApiURI(uri);

		return handlerDescriptor;
	}

	@Override
	public String getEventAddress() {
		return MessageSchema.HANDLER_NAME_GETALL;
	}

}
