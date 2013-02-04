package com.elm.bean;

import java.io.Serializable;

public class Message<T> implements Serializable {

	private final T payload;

	public Message(T payload) {
		this.payload = payload;
	}

	public T getPayload() {
		return payload;
	}

}
