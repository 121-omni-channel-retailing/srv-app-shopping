package otocloud.app.common;

/**
 * 定义所有服务以及Handler地址
 * 
 * @author liuxba
 *
 */
public class MessageSchema {
	public static final String HANDLER_NAME_GETALL = "getAll";
	public static final String HANDLER_NAME_GET = "get";
	public static final String HANDLER_NAME_POST = "post";
	public static final String HANDLER_NAME_PUT = "put";
	public static final String HANDLER_NAME_DELETE = "delete";
	
	/*
	 * APP Shopping消息地址定义。
	 */
	public static final String SRV_NAME_APP_SHOPPING = "app-shopping";
	
	//应用产品目录
	public static final String COMP_NAME_APP_CATALOG = "app-catalogs";
	public static final String EVENT_ADDR_APP_CATALOG_GETALL = SRV_NAME_APP_SHOPPING + "." + COMP_NAME_APP_CATALOG + "." + HANDLER_NAME_GETALL;
	public static final String EVENT_ADDR_APP_CATALOG_GET = SRV_NAME_APP_SHOPPING + "." + COMP_NAME_APP_CATALOG + "." + HANDLER_NAME_GET;
	
	//部署信息
	public static final String COMP_NAME_APP_OPS_DEPLOY_INFO = "app-ops-deploy-infos";
	public static final String EVENT_ADDR_APP_OPS_DEPLOY_INFO_GETALL = SRV_NAME_APP_SHOPPING + "." + COMP_NAME_APP_OPS_DEPLOY_INFO + "." + HANDLER_NAME_GETALL;
	
	//订单
	public static final String COMP_NAME_APP_ORDER = "app-orders";
	public static final String EVENT_ADDR_APP_ORDER_GETALL = SRV_NAME_APP_SHOPPING + "." + COMP_NAME_APP_ORDER + "." + HANDLER_NAME_GETALL;
	public static final String EVENT_ADDR_APP_ORDER_GET = SRV_NAME_APP_SHOPPING + "." + COMP_NAME_APP_ORDER + "." + HANDLER_NAME_GET;
	public static final String EVENT_ADDR_APP_ORDER_POST = SRV_NAME_APP_SHOPPING + "." + COMP_NAME_APP_ORDER + "." + HANDLER_NAME_POST;
	public static final String EVENT_ADDR_APP_ORDER_PUT = SRV_NAME_APP_SHOPPING + "." + COMP_NAME_APP_ORDER + "." + HANDLER_NAME_PUT;
	public static final String EVENT_ADDR_APP_ORDER_DELETE = SRV_NAME_APP_SHOPPING + "." + COMP_NAME_APP_ORDER + "." + HANDLER_NAME_DELETE;

}
