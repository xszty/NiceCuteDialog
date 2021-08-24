package com.saxiao.library.dialog.webHtml;

import com.google.gson.JsonObject;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import java.io.IOException;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

public class GetRequestIntercept implements RequestHandler {
	protected JsonObject jsonObject;
	@RequestMapping(method = { RequestMethod.POST})
	@Override public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
		jsonObject = new JsonObject();
		try {
			jsonObject.addProperty("code",0);
			jsonObject.addProperty("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
		}

		intercept(request,response,context);
	}

	protected void intercept(HttpRequest request, HttpResponse response, HttpContext context){

	}
}
