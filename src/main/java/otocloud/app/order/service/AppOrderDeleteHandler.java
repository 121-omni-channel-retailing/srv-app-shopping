package otocloud.app.order.service;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import otocloud.app.common.MessageSchema;
import otocloud.app.order.dao.AppOrderDAO;
import otocloud.app.order.dao.AppOrderDAOImpl;
import otocloud.common.ActionURI;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;
import otocloud.persistence.dao.TransactionConnection;

public class AppOrderDeleteHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	private static final Logger logger = LoggerFactory.getLogger(AppOrderDeleteHandler.class);

	private AppOrderDAO appOrderDAO;

	public AppOrderDeleteHandler(OtoCloudComponentImpl componentImpl) {
		super(componentImpl);
		appOrderDAO = new AppOrderDAOImpl(componentImpl.getSysDatasource());
	}

	@Override
	public void handle(OtoCloudBusMessage<JsonObject> msg) {
		logger.debug("handle-->msg: " + msg);
		JsonObject body = msg.body();
	
		//获得消息体中的 content 部分.
//		JsonObject content = body.getJsonObject(MessageBodyConvention.HTTP_BODY);

		//获得消息体中的 queryParams 部分.
		JsonObject params = body.getJsonObject(MessageBodyConvention.HTTP_QUERY);
		
		//获得消息体中的 session 部分.
		JsonObject session = body.getJsonObject(MessageBodyConvention.SESSION);
		Integer acctId = session.getInteger(MessageBodyConvention.SESSION_ACCT_ID);
		Integer userId = session.getInteger(MessageBodyConvention.SESSION_USER_ID);
		
		String idStr = params.getString("id");
		if (idStr == null || idStr.isEmpty()) {
			logger.error("Not found order ID for deleting.");
			msg.fail(400, "Not found order ID for deleting.");
			return;
		}
		Integer id = new Integer(idStr);

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
				
				appOrderDAO.deleteAppOrder(transConn, id, userId, daoRet -> {
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
					
					transConn.commitAndClose(commitRet -> {
						if (commitRet.failed()) {
							logger.error("Failed to commit transaction for deleting app_order_header/app_order_detail", commitRet.cause());
							msg.fail(400, commitRet.cause().getMessage());
							return;
						}
						
						JsonObject updateResult = daoRet.result();
						logger.debug("updateResultStr-->" + updateResult.toString());
						msg.reply(updateResult);						
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

		ActionURI uri = new ActionURI(":id", HttpMethod.DELETE);
		handlerDescriptor.setRestApiURI(uri);

		return handlerDescriptor;
	}

	@Override
	public String getEventAddress() {
		return MessageSchema.HANDLER_NAME_DELETE;
	}

}
