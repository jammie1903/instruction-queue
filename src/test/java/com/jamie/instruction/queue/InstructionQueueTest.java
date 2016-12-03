package com.jamie.instruction.queue;

import com.jamie.instruction.queue.bean.InstructionMessage;
import com.jamie.instruction.queue.bean.InstructionType;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class InstructionQueueTest {
	private InstructionQueue testInstance = new InstructionQueue();

	private InstructionMessage createInstructionMessage(InstructionType type) {
		InstructionMessage returnValue = new InstructionMessage();
		returnValue.setInstructionType(type);
		return returnValue;
	}

	@Test
	public void peekDoesNotRemoveMessageTest() {
		InstructionMessage message = createInstructionMessage(InstructionType.B);
		testInstance.enqueue(message);
		assertEquals(message, testInstance.peek());
		assertEquals(message, testInstance.peek());
	}

	@Test
	public void dequeueRemovesMessageTest() {
		InstructionMessage message = createInstructionMessage(InstructionType.B);
		testInstance.enqueue(message);
		assertEquals(message, testInstance.dequeue());
		assertNull(testInstance.dequeue());
	}

	@Test
	public void dequeuePriorityOrderTest() {
		List<InstructionMessage> messages = Arrays.asList(createInstructionMessage(InstructionType.A),
				createInstructionMessage(InstructionType.B), createInstructionMessage(InstructionType.C),
				createInstructionMessage(InstructionType.D), createInstructionMessage(InstructionType.A),
				createInstructionMessage(InstructionType.B), createInstructionMessage(InstructionType.C),
				createInstructionMessage(InstructionType.D));

		List<InstructionMessage> expectedorder = Arrays.asList(messages.get(0), messages.get(4), messages.get(1),
				messages.get(5), messages.get(2), messages.get(3), messages.get(6), messages.get(7));

		for (InstructionMessage instructionMessage : messages) {
			testInstance.enqueue(instructionMessage);
		}

		for (InstructionMessage instructionMessage : expectedorder) {
			assertEquals(instructionMessage, testInstance.dequeue());
		}

	}

	@Test
	public void countTest() {
		assertEquals(0, testInstance.count());
		testInstance.enqueue(createInstructionMessage(InstructionType.B));
		assertEquals(1, testInstance.count());
		testInstance.enqueue(createInstructionMessage(InstructionType.B));
		assertEquals(2, testInstance.count());
		testInstance.dequeue();
		assertEquals(1, testInstance.count());
		testInstance.dequeue();
		assertEquals(0, testInstance.count());
		testInstance.dequeue();
		assertEquals(0, testInstance.count());
	}

	@Test
	public void isEmptytest() {
		assertTrue(testInstance.isEmpty());
		testInstance.enqueue(createInstructionMessage(InstructionType.B));
		assertFalse(testInstance.isEmpty());
		testInstance.dequeue();
		assertTrue(testInstance.isEmpty());
	}
}
