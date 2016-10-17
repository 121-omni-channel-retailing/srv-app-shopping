package otocloud.app.common;

import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 121Cloud通用工具类。
 * @author liuxba
 *
 */
public class CommonUtils {
	
	/**
	 * 将一个整型数据列表转换为SQL in 中的带逗号的列表。
	 * @param list<Integer> 整型数据列表
	 * @return 用逗号分隔的整型数据列表。
	 */
	public static String toSqlIntInList(List<Integer> list) {
		StringBuilder sqlInList = new StringBuilder();
		list.forEach(item -> {
			sqlInList.append(",").append(item.toString());
		});
		
		return sqlInList.toString().substring(1);
	}
	
	/**
	 * 将一个字符型数据列表转换为SQL in 中的带逗号的列表。
	 * @param list<Integer> 整型数据列表
	 * @return 用逗号分隔的字符型数据列表。
	 */
	public static String toSqlStrInList(List<String> list) {
		StringBuilder sqlInList = new StringBuilder();
		list.forEach(item -> {
			sqlInList.append(", '").append(item.toString()).append("'");
		});
		
		return sqlInList.toString().substring(1);
	}
	
	/**
	 * 返回Gson对象，目前121Cloud统一使用该种配置的Gson对象进行Json序列化。
	 *  1. 数据对象与json对象的命名转换规则为：FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
	 *  2. Vertx SQLClient读取时间格式为 yyyy-MM-dd'T'HH:mm:ss'Z'
	 *  
	 * @return
	 */
	public static Gson getGson() {
		Gson gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//				.setExclusionStrategies(new ClassExclusionStrategy(List.class))
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
				.setPrettyPrinting()
				.create();
		return gson;
	}

}
