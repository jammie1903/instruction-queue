package com.jamie.instruction.reciever;

import com.jamie.instruction.queue.InstructionQueue;

public class MessageRecieverImpl implements MessageReciever {

	private InstructionQueue instructionQueue;

	public MessageRecieverImpl(InstructionQueue instructionQueue) {
		this.instructionQueue = instructionQueue;
	}

	public void recieve(String message) {
		// TODO 
	}
}
