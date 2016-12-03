package com.jamie.instruction.queue.bean;

import java.util.Date;

public class InstructionMessage {
	private InstructionType instructionType;
	private String productCode;
	private Integer quantity;
	private Integer UOM;
	private Date timestamp;

	public InstructionType getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(InstructionType instructionType) {
		this.instructionType = instructionType;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getUOM() {
		return UOM;
	}

	public void setUOM(Integer UOM) {
		this.UOM = UOM;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
