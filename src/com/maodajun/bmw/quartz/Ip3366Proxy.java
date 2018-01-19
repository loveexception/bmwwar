package com.maodajun.bmw.quartz;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;


@IocBean
public class Ip3366Proxy implements FreeProxy {
	public static String api = "http://dec.ip3366.net/api/?";
	public static String address = "%u5317%u4EAC%2C%u4E0A%u6D77%2C%u5E7F%u5DDE%2C%u6DF1%u5733%2C%u676D%u5DDE";
	public static String key = "20170306100439929";
	
	private String Ip ;
	private String Port;

	public Ip3366Proxy(String Ip, String Port) {
		this.Ip = Ip;
		this.Port = Port;
	}
	@Override
	public String getIp() {
		return Ip;
	}
	@Override
	public void setIp(String ip) {
		Ip = ip;
	}
	@Override
	public String getPort() {
		return Port;
	}
	@Override
	public void setPort(String port) {
		Port = port;
	}
	/**
	 * 今日所需要的代理
	 * 
	 * @return
	 */
	public static List<FreeProxy> creatProxys(Map<String, Integer> rate) {
		int pagnum = 300;
		// 北上广深
		String url =  api + "key=" + key + "&getnum=" + pagnum + "&filter=1" + "&ipaddress="
				+ address + "&area=1" + "&sarea=1" + "&formats=2";
		System.out.println(url);

		Response req = Http.get(url);
		String json = req.getContent("gb2312");
		List proxys = Json.fromJsonAsList(Object.class, json);
		List<FreeProxy> result = new ArrayList<FreeProxy>();
		for (Object object : proxys) {
			Map map = (Map)object;
			FreeProxy freeProxy = new Ip3366Proxy(""+map.get("Ip"),""+map.get("Port"));
			result.add(freeProxy);		
		}
		return result;
	}

}
