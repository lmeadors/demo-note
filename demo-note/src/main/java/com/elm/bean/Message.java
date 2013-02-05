package com.elm.bean;

import java.io.Serializable;

public class Message<PayloadType> implements Serializable {

	private final PayloadType payload;

	public Message(PayloadType payload) {
		this.payload = payload;
	}

	public PayloadType getPayload() {
		return payload;
	}

}
