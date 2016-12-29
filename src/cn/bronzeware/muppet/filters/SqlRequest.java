package cn.bronzeware.muppet.filters;

import java.util.Map;

import cn.bronzeware.muppet.transaction.Transaction;

public class SqlRequest {

	private Transaction transaction;
	
	private Map<String,Object> parameter;

	

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Map<String, Object> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, Object> parameter) {
		this.parameter = parameter;
	}


	
	
}
