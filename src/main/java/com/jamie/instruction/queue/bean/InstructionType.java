package com.jamie.instruction.queue.bean;

public enum InstructionType {

	A(1), B(2), C(3), D(3);

	private int priorityVal;

	private InstructionType(int priorityVal) {
		this.priorityVal = priorityVal;
	}
	
	public int getPriorityVal() {
		return priorityVal;
	}
}
