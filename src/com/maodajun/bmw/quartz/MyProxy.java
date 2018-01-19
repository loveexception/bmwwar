package com.maodajun.bmw.quartz;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.omg.CosNaming.NamingContextExtPackage.URLStringHelper;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.net.Urls;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Queues;
import com.maodajun.bmw.bean.Login;
import com.sun.jndi.toolkit.url.UrlUtil;

import sun.net.util.URLUtil;

public class MyProxy {
	String key = "20170306100439929";


	   /**
     * 代理加载
     * @param logins
     * @param proxys
     */
    public void proxyLogin(List<Login> logins ,List proxys){
    	WebDriver driver;
    	Queue q =Queues.newArrayDeque(proxys);
    	for (Login login : logins) {
			Map map = (Map) q.remove();
			String ip= ""+map.get("Ip");
			String port= ""+map.get("Port");
			login.setIp(ip);
			login.setPort(port);


		}
    	
    }
	public Map<Login, WebDriver> drivers(List<Login> logins) {
		Map<Login, WebDriver> result = new HashMap<Login, WebDriver>();
		for (Login login : logins) {
			if (Strings.isEmpty(login.getIp())||Strings.isEmpty(login.getPort())) {
				WebDriver driver = new ChromeDriver();
				result.put(login, driver);
				continue;
			}
			Proxy proxy = new Proxy();
			proxy.setHttpProxy(login.getIp() + ":" + login.getPort());
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
			cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
			cap.setCapability(CapabilityType.PROXY, proxy);
			WebDriver driver = new ChromeDriver(cap);
			result.put(login, driver);

		}
		return result;
	}

}
