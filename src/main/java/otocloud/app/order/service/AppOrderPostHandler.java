package otocloud.app.order.service;

import com.google.gson.Gson;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
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
import otocloud.persistence.dao.TransactionConnection;

public class AppOrderPostHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	private static final Logger logger = LoggerFactory.getLogger(AppOrderPostHandler.class);

	private AppOrderDAO appOrderDAO;

	public AppOrderPostHandler(OtoCloudComponentImpl componentImpl) {
		super(componentImpl);
		appOrderDAO = new AppOrderDAOImpl(componentImpl.getSysDatasource());
	}

	@Override
	public void handle(OtoCloudBusMessage<JsonObject> msg) {
		logger.debug("handle-->msg: " + msg);
		JsonObject body = msg.body();
	
		//获得消息体中的 content 部分.
		JsonObject content = body.getJsonObject(MessageBodyConvention.HTTP_BODY);

		//获得消息体中的 queryParams 部分.
//		JsonObject params = body.getJsonObject(MessageBodyConvention.HTTP_QUERY);
		
		//获得消息体中的 session 部分.
		JsonObject session = body.getJsonObject(MessageBodyConvention.SESSION);
		Integer acctId = session.getInteger(MessageBodyConvention.SESSION_ACCT_ID);
		Integer userId = session.getInteger(MessageBodyConvention.SESSION_USER_ID);
		
		Gson gson = CommonUtils.getGson();
		AppOrder appOrder = gson.fromJson(content.encode(), AppOrder.class);
		appOrder.setOrgAcctId(acctId);
		
		JDBCClient jdbcClient = componentImpl.getSysDatasource().getSqlClient();
		jdbcClient.getConnection(conRes -> {
			if (conRes.failed()) {
				Throwable err = conRes.cause();
				String errMsg = err.getMessage();
				logger.error(errMsg, err);
				msg.fail(400, errMsg);
			}
			SQLConnection conn = conRes.result();
			TransactionConnection.createTransactionConnection(conn, transConnRet -> {
				if (transConnRet.failed()) {
					logger.error("Failed to create transaction connection.", transConnRet.cause());
					msg.fail(400, transConnRet.cause().getMessage());
					return;
				}
				TransactionConnection transConn = transConnRet.result();
				
				appOrderDAO.saveAppOrder(transConn, appOrder, userId.intValue(), daoRet -> {
					if (daoRet.failed()) {
						Throwable err = daoRet.cause();
						String errMsg = err.getMessage();
						logger.error(errMsg, err);
						
						transConn.rollbackAndClose(commitRet -> {
							if (commitRet.failed()) {
								logger.error("Failed to rollback transaction.", commitRet.cause());				
							}
						});							
						msg.fail(400, errMsg);
						return;
					}
					
					AppOrder retAppOrder = daoRet.result();
					transConn.commitAndClose(commitRet -> {
						if (commitRet.failed()) {
							logger.error("Failed to commit transaction for app_order_header/app_order_detail", commitRet.cause());
							msg.fail(400, commitRet.cause().getMessage());
							return;
						}
						String appOrderStr = gson.toJson(retAppOrder);
						logger.debug("appOrderStr-->" + retAppOrder);
						JsonObject appOrderObject = new JsonObject(appOrderStr);
						msg.reply(appOrderObject);
					});
				});
				
			});
				
		});

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HandlerDescriptor getHanlderDesc() {
		HandlerDescriptor handlerDescriptor = super.getHanlderDesc();

		ActionURI uri = new ActionURI("", HttpMethod.POST);
		handlerDescriptor.setRestApiURI(uri);

		return handlerDescriptor;
	}

	@Override
	public String getEventAddress() {
		return MessageSchema.HANDLER_NAME_POST;
	}

}
