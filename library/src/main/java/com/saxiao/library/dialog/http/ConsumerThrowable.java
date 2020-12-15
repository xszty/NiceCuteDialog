package com.saxiao.library.dialog.http;

import io.reactivex.functions.Consumer;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public abstract class ConsumerThrowable<T extends Throwable> implements Consumer<T> {
	@Override public void accept(T throwable) throws Exception {
		String errorMessage = "";
		if (throwable instanceof SocketTimeoutException) {
			errorMessage = BaseHttpWarning.REQUEST_ERROR_TIME_OUT;
		}else if(throwable instanceof IOException || throwable instanceof SocketException){
			errorMessage = BaseHttpWarning.REQUEST_ERROR_CONNNECTEXCEPTION;
		}else{
			errorMessage = throwable.getMessage();
		}
		onError(errorMessage);
	}

	public abstract void onError(String message);
}
