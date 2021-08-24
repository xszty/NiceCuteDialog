package com.saxiao.library.dialog.webHtml;

import com.google.gson.JsonObject;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import java.io.IOException;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

public class PostRequestIntercept implements RequestHandler {
	protected JsonObject jsonObject;
	@RequestMapping(method = { RequestMethod.POST})
	@Override public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
		String content = HttpRequestParser.getContentFromBody(request);
		jsonObject = new JsonObject();
		try {
			jsonObject.addProperty("code",0);
			jsonObject.addProperty("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
		}

		intercept(request,response,context,content);
	}

	protected void intercept(HttpRequest request, HttpResponse response, HttpContext context,String content){

	}
}
