package com.mcoding.broadcast.controller;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mcoding.base.ui.bean.common.JsonResult;
import com.mcoding.comp.broadcast.BroadcastManager;
import com.mcoding.comp.broadcast.ParkingMsg;
import com.mcoding.comp.license.LicenseManager;
import com.mcoding.comp.license.utils.Constant;

@EnableAutoConfiguration
@Controller
@RequestMapping("broadcast")
public class BroadCastController {
	
	public static boolean isVerify = true;
	public static String verifyMsg = "";
	
	private Logger logger = LoggerFactory.getLogger(BroadCastController.class);
	
	@Value("${license.verify}")
	private String licenseVerify;
	
	@RequestMapping(value="/", produces="text/html")
	@ResponseBody
	public String index(){
//		return "index";
		if (isVerify) {
			return "启动成功";
		}else{
			return "授权失败:" + verifyMsg;
		}
	}
	
	@RequestMapping("front/speak")
	@ResponseBody
	public JsonResult<String> speak(String cardNum, String parkNum, String parkTime, Integer rate, Integer volume, Integer speakTimes) {
		JsonResult<String> result = new JsonResult<String>();
		
		try {
			if(!isVerify){
				throw new RuntimeException("服务未获得授权");
			}
			
			if(StringUtils.isBlank(cardNum)){
				throw new NullPointerException("车牌号 cardNum 不能为空");
			}
			if (StringUtils.isBlank(parkNum)) {
				throw new NullPointerException("停泊号 parkNum 不能为空");
			}
			if(StringUtils.isBlank(parkTime)){
				parkTime = "10";
			}
			if (rate == null || rate < -10 || rate >10) {
				rate = -2;
			}
			if (volume == null || volume <0 || volume >100) {
				volume = 100;
			}
			if (speakTimes == null || speakTimes.equals(0)) {
				speakTimes =3; 
			}
			
			ParkingMsg msg1 = new ParkingMsg(cardNum, parkNum, parkTime, "东码头");
			msg1.setRate(rate);
			msg1.setVolume(volume);
			
			for(int i=0; i<speakTimes; i++){
				BroadcastManager.getInstance().broadcast(msg1);
			}
			
			result.setStatus("00");
			result.setMsg("ok");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setStatus("error");
			result.setMsg(e.getMessage());
		}
		
		return result;
		
	}
	
	@PostConstruct
	private void init() throws Exception {
		try {
			if (!"false".equals(licenseVerify)) {
				this.verify();
			}
			
			BroadcastManager.getInstance().start();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}
	
	private void verify() {
		
		String filePath  = new File("").getAbsolutePath() + File.separator + Constant.DEFAULT_FILE_NAME; 
		File license = new File(filePath);
		try {
			isVerify = LicenseManager.getInstance().verify(license);
			
			if (!isVerify) {
				throw new RuntimeException("该电脑没有获得授权");
			}
		} catch (Exception e) {
			isVerify = false;
			verifyMsg = e.getMessage();
			logger.error(e.getMessage());
		} 
		
	}

	@PreDestroy
	private void stop() {
		BroadcastManager.getInstance().stop();
		
	}

}
