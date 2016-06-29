package com.example;

import com.ipacc.enterprise.elm.EventMetadata;

public class Policy {
	private String policyNumber;
	
	private String policyHolder;

	@EventMetadata
	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	@EventMetadata
	public String getPolicyHolder() {
		return policyHolder;
	}

	public void setPolicyHolder(String policyHolder) {
		this.policyHolder = policyHolder;
	}
	
	
}
