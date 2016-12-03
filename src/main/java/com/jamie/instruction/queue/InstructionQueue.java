package com.jamie.instruction.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jamie.instruction.queue.bean.InstructionMessage;

public class InstructionQueue {

	List<InstructionMessage> queue = new ArrayList<>();

	public void enqueue(InstructionMessage message) {
		queue.add(message);
		Collections.sort(queue);
	}

	public InstructionMessage dequeue() {
		if(isEmpty()) {
			return null;
		}
		return queue.remove(0);
	}

	public InstructionMessage peek() {
		if (isEmpty()) {
			return null;
		}
		return queue.get(0);
	}

	public int count() {
		return queue.size();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
