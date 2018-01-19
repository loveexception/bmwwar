package com.maodajun;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.lang.Stopwatch;
import org.nutz.lang.util.PType;

public class IPSpeed {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(1, 1);
		String url = "";
		Response response = Http.get("http://dec.ip3366.net/api/?key=20170306100439929&getnum=30&filter=1&area=2&sarea=1&formats=2");
		String str = response.getContent();
		Map[] maps = Json.fromJsonAsArray( Map.class , str);
		List<Map> oks = new ArrayList<Map>();
		List<Map> errors = new ArrayList<Map>();
		for (Map m : maps) {
			Stopwatch sw = Stopwatch.begin();
			String keys =m.get("Ip")+":"+m.get("Port");
			Http.setHttpProxy(""+m.get("Ip"), Integer.parseInt(""+m.get("Port")));
			try{
				//response = Http.get("https://www.mybmwclub.cn", 5 * 1000);
				oks.add(m);
			}catch (Exception e) {
				errors.add(m);
			}finally{
				sw.stop();
				m.put("speed", sw.getDuration());
			}
			

		}
		System.out.println(oks.size());
		System.out.println(errors.size());
		for (Map map : oks) {
			System.out.println(map.get("Ip")+":"+map.get("Port")+"\t\t"+map.get("speed")+"|"+map.get("Country"));

		}
//		for (Entry<String, Object> e : map.entrySet()) {
//			System.out.println(e.getKey());
//			
//		}


	}

}
