package otocloud.app.common;

import java.text.DateFormat;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.json.JsonObject;
import otocloud.app.order.model.AppOrder;

public class Test {
	public static void main(String[] args) {
		System.out.println("start ...");
		Gson gson = new GsonBuilder()
//			    .serializeNulls()
//			    .setDateFormat(DateFormat.FULL)
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//			    .setPrettyPrinting()
//			    .setVersion(1.0)
			    .create();
		
		String jsonStr = "{\"entry_id\":99,\"org_acct_id\":10,\"status\":\"A\",\"total_price\":5000.0,\"test_date\":\"2015-11-18T07:19:08Z\"}";
		JsonObject jsonObj2 = new JsonObject(jsonStr);
		Object obj = jsonObj2.getValue("test_date");
		
		AppOrder target = new AppOrder();
		target.setOrgAcctId(new Integer(10));
		target.setTotalPrice(new Double(5000));
		target.setStatus("A");
//		target.setEntryDatetime(Instant.now());
		target.setEntryId(new Integer(99));
		
		String json = gson.toJson(target); // serializes target to Json
		JsonObject jsonObj = new JsonObject(json);
		jsonObj.put("test_date", "2015-11-18T07:19:08Z");
		System.out.println("json-->" + jsonObj.encode());
		AppOrder target2 = gson.fromJson(jsonObj.encode(), AppOrder.class); // deserializes json into target2

		System.out.println("ok");
	}

}
