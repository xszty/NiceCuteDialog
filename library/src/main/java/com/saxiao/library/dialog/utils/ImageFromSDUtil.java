package com.saxiao.library.dialog.utils;

import android.annotation.SuppressLint;
import java.io.File;
import java.util.ArrayList;

/**
 * 从sd卡获取本地某个路径下的图片文件
 */
public class ImageFromSDUtil {
	/**
	 * 从sd卡获取图片资源
	 * @return
	 */
	public static java.util.List<String> getImagePathFromSD(String filePath) {
		// 图片列表
		java.util.List<String> imagePathList = new ArrayList<String>();
		// 得到该路径文件夹下所有的文件
		File fileAll = new File(filePath);
		File[] files = fileAll.listFiles();
		// 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (checkIsImageFile(file.getPath())) {
				imagePathList.add(file.getPath());
			}
		}
		// 返回得到的图片列表
		return imagePathList;
	}

	/**
	 * 检查扩展名，得到图片格式的文件
	 * @param fName  文件名
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static boolean checkIsImageFile(String fName) {
		boolean isImageFile = false;
		// 获取扩展名
		String FileEnd = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif") || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
			isImageFile = true;
		} else {
			isImageFile = false;
		}
		return isImageFile;
	}
}
