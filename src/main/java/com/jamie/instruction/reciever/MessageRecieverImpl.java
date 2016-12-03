package com.jamie.instruction.reciever;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jamie.instruction.queue.InstructionQueue;
import com.jamie.instruction.queue.bean.InstructionMessage;
import com.jamie.instruction.queue.bean.InstructionType;

public class MessageRecieverImpl implements MessageReciever {

	private static final int EXPECTED_SEGMENT_COUNT = 6;
	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy­MM­dd'T'HH:mm:ss.SSS'Z'");
	private InstructionQueue instructionQueue;

	public MessageRecieverImpl(InstructionQueue instructionQueue) {
		this.instructionQueue = instructionQueue;
	}

	public void recieve(String message) {
		if (!message.startsWith("InstructionMessage")) {
			throw new IllegalArgumentException("Message was not an instuction");
		}
		String[] segments = getMessageSegments(message);
		InstructionMessage instructionMessage = new InstructionMessage();
		instructionMessage.setInstructionType(readInstructionType(segments[1]));
		instructionMessage.setProductCode(readProductCode(segments[2]));
		instructionMessage.setQuantity(readQuantity(segments[3]));
		instructionMessage.setUOM(readUOM(segments[4]));
		instructionMessage.setTimestamp(readTimestamp(segments[5]));
		instructionQueue.enqueue(instructionMessage);
	}

	private InstructionType readInstructionType(String rawValue) {
		try {
			return InstructionType.valueOf(rawValue);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("InstructionType was not valid", e);
		}
	}

	private String readProductCode(String rawValue) {
		if (rawValue.matches("^[A-Z]{2}\\d{2}$")) {
			return rawValue;
		} else {
			throw new IllegalArgumentException("ProductCode was not valid");
		}
	}

	private Integer readQuantity(String rawValue) {
		try {
			Integer value = Integer.valueOf(rawValue);
			if(value < 0) {
				throw new IllegalArgumentException("Quantity was not valid");
			}
			return value;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Quantity was not valid", e);
		}
	}

	private Integer readUOM(String rawValue) {
		try {
			Integer value = Integer.valueOf(rawValue);
			if(value < 0 || value >= 256) {
				throw new IllegalArgumentException("UOM was not valid");
			}
			return value;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("UOM was not valid", e);
		}
	}

	private Date readTimestamp(String rawValue) {
		try {
			Date date = dateFormat.parse(rawValue);
			if(date.getTime() > System.currentTimeMillis()) {
				throw new IllegalArgumentException("Timestamp was not valid");
			}
			return date;
		} catch (ParseException e) {
			throw new IllegalArgumentException("Timestamp was not valid", e);
		}
	}

	private String[] getMessageSegments(String message) {
		String[] segments = message.split("\\s+");
		if (segments.length != EXPECTED_SEGMENT_COUNT) {
			throw new IllegalArgumentException("Message did not contain the correct amount of segments");
		}
		return segments;
	}
}
