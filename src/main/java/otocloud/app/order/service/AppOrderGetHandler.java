package otocloud.app.order.service;

import com.google.gson.Gson;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.app.common.CommonUtils;
import otocloud.app.common.MessageSchema;
import otocloud.app.order.dao.AppOrderDAO;
import otocloud.app.order.dao.AppOrderDAOImpl;
import otocloud.app.order.model.AppOrder;
import otocloud.common.ActionURI;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

public class AppOrderGetHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	private static final Logger logger = LoggerFactory.getLogger(AppOrderGetHandler.class);

	private AppOrderDAO appOrderDAO;

	public AppOrderGetHandler(OtoCloudComponentImpl componentImpl) {
		super(componentImpl);
		appOrderDAO = new AppOrderDAOImpl(componentImpl.getSysDatasource());
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
		Integer acctId = session.getInteger(MessageBodyConvention.SESSION_ACCT_ID);
		Integer userId = session.getInteger(MessageBodyConvention.SESSION_USER_ID);
		
		String id = params.getString("id");
		Integer orderId = Integer.valueOf(id);
		
		Gson gson = CommonUtils.getGson();
		
		//1. 查询appModules
		appOrderDAO.getAppOrder(acctId, orderId, daoRet -> {
			if (daoRet.failed()) {
				Throwable err = daoRet.cause();
				String errMsg = err.getMessage();
				logger.error(errMsg, err);
				msg.fail(400, errMsg);
			} else {
				AppOrder appOrder = daoRet.result();
			
				//1.1 没有查询到结果，空值返回
				if (appOrder == null) {
					msg.reply(null);
					return;
				}
						
				String appOrderStr = gson.toJson(appOrder);
				logger.debug("appOrderStr-->" + appOrderStr);
				
				JsonObject appOrderObject = new JsonObject(appOrderStr);
				msg.reply(appOrderObject);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HandlerDescriptor getHanlderDesc() {
		HandlerDescriptor handlerDescriptor = super.getHanlderDesc();

		ActionURI uri = new ActionURI(":id", HttpMethod.GET);
		handlerDescriptor.setRestApiURI(uri);

		return handlerDescriptor;
	}

	@Override
	public String getEventAddress() {
		return MessageSchema.HANDLER_NAME_GET;
	}

}
