package com.mcoding.broadcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer{

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	public static void main(String[] args) {
//		SpringApplication.run(BroadCastController.class, args);
		SpringApplication.run(Application.class);
		openBrowser();
	}

	private static void openBrowser() {
		
		try {
			
			ProcessBuilder pb = new ProcessBuilder();
			pb.command("cmd.exe","/c start http://localhost/broadcast/").start();
			
			/*String url = "http://localhost/index.jsp";
			java.net.URI uri = java.net.URI.create(url);
			// 获取当前系统桌面扩展
			java.awt.Desktop dp = java.awt.Desktop.getDesktop();
			// 判断系统桌面是否支持要执行的功能
			if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
				dp.browse(uri);// 获取系统默认浏览器打开链接
			}*/
		} catch (java.lang.NullPointerException e) {
			// 此为uri为空时抛出异常
			e.printStackTrace();
		} catch (java.io.IOException e) {
			// 此为无法获取系统默认浏览器
			e.printStackTrace();
		}
	}

}
