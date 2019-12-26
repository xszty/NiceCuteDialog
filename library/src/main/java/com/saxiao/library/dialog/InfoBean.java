package com.saxiao.library.dialog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yanghui on 2019/05/27.
 * 动态生成dialog所需的信息类
 *
 * @author yanghui
 */
public class InfoBean implements Parcelable {

	private int itemType;
	private String title;
	private String text;
	private String key;
	private String value;


	/**
	 * @param itemType 类型
	 * @param text 文本
	 * * @param key key值
	 * @param value value值
	 */
	public InfoBean(int itemType, String title,String text, String key, String value) {
		this.itemType = itemType;
		this.title = title;
		this.text = text;
		this.value = value;
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.itemType);
		dest.writeString(this.title);
		dest.writeString(this.text);
		dest.writeString(this.value);
		dest.writeString(this.key);
	}

	protected InfoBean(Parcel in) {
		this.itemType = in.readInt();
		this.title = in.readString();
		this.text = in.readString();
		this.value = in.readString();
		this.key = in.readString();
	}

	public static final Creator<InfoBean> CREATOR = new Creator<InfoBean>() {
		@Override public InfoBean createFromParcel(Parcel source) {
			return new InfoBean(source);
		}

		@Override public InfoBean[] newArray(int size) {
			return new InfoBean[size];
		}
	};
}
