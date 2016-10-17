package otocloud.app.catalog.service;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.app.catalog.dao.AppOpsDAO;
import otocloud.app.catalog.dao.AppOpsDAOImpl;
import otocloud.app.common.MessageSchema;
import otocloud.common.ActionURI;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

public class AppOpsDeployInfoGetAllHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	private static final Logger logger = LoggerFactory.getLogger(AppOpsDeployInfoGetAllHandler.class);

	private AppOpsDAO appOpsDAO;

	public AppOpsDeployInfoGetAllHandler(OtoCloudComponentImpl componentImpl) {
		super(componentImpl);
		appOpsDAO = new AppOpsDAOImpl(componentImpl.getSysDatasource());
	}

	@Override
	public void handle(OtoCloudBusMessage<JsonObject> msg) {
		logger.debug("handle-->msg: " + msg);
		JsonObject body = msg.body();
	
		//获得消息体中的 content 部分.
		//JsonObject content = body.getJsonObject(MessageBodyConvention.HTTP_BODY);

		//获得消息体中的 queryParams 部分.
		JsonObject params = body.getJsonObject(MessageBodyConvention.HTTP_QUERY);

		//获得消息体中的 session 部分.
		JsonObject session = body.getJsonObject(MessageBodyConvention.SESSION);
		
		List<Integer> appVersionIds = new ArrayList<Integer>();
		String ids = params.getString("ids");
		if (ids != null && !ids.isEmpty()) {
		String[] idsArray = ids.split(",");
			for (String id: idsArray) {
				appVersionIds.add(Integer.valueOf(id));
			}
		}

		//1. 查询appModules
		appOpsDAO.getAppDeployInfo(appVersionIds, daoRet -> {
			if (daoRet.failed()) {
				Throwable err = daoRet.cause();
				String errMsg = err.getMessage();
				logger.error(errMsg, err);
				msg.fail(400, errMsg);
			} else {
				JsonObject appOpsDeployInfo = daoRet.result();
				logger.debug("appOpsDeployInfo-->" + appOpsDeployInfo.toString());
				
				msg.reply(appOpsDeployInfo);
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
