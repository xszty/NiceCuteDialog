package com.saxiao.library.dialog.utils;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.util.HashMap;

public class HeaderFlowable {

	public static Flowable<java.util.Map<String,String>> getHeader(String _t){
		return Flowable.create(e -> {
			HashMap<String,String> header = new HashMap<>();
			String _n = HeaderUtils.getCharAndNumr(12);
			header.put("token",GlobalToken.getToken());
			header.put("app_key","appId");
			header.put("aa",HeaderUtils.encode().replaceAll("\r|\n",""));
			header.put("tt",_t);
			header.put("nn",_n);
			header.put("ss",HeaderUtils.sortAndSha1(GlobalToken.getkey(),_n,_t));
			header.put("ff",GlobalToken.getkey());
			e.onNext(header);
			e.onComplete();
		}, BackpressureStrategy.ERROR);
	}
}
