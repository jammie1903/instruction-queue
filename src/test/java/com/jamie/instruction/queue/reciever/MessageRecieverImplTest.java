package com.jamie.instruction.queue.reciever;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

import com.jamie.instruction.queue.InstructionQueue;
import com.jamie.instruction.queue.bean.InstructionMessage;
import com.jamie.instruction.queue.bean.InstructionType;
import com.jamie.instruction.reciever.MessageRecieverImpl;

public class MessageRecieverImplTest {
	private DateFormat format = new SimpleDateFormat("yyyy­MM­dd'T'HH:mm:ss.SSS'Z'");

	private InstructionQueue mockInstructionQueue = mock(InstructionQueue.class);
	private MessageRecieverImpl testInstance = new MessageRecieverImpl(mockInstructionQueue);

	private void runExceptionTest(String instructionMessage, String expectedException) {
		try {
			testInstance.recieve(instructionMessage);
			fail("Exception not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals(expectedException, e.getMessage());
		}
	}

	@Test
	public void recieveNonMessageTest() {
		runExceptionTest("NotAnInstructionMessage A AB12 0 0 2016-12-03T09:16:00.012Z",
				"Message was not an instuction");
	}

	@Test
	public void recieveInvalidSegmentCountTooFewTest() {
		runExceptionTest("InstructionMessage A AB12 0 0", "Message did not contain the correct amount of segments");
	}

	@Test
	public void recieveInvalidSegmentCountTooManyTest() {
		runExceptionTest("InstructionMessage A AB12 0 0 0 2016-12-03T09:16:00.012Z",
				"Message did not contain the correct amount of segments");
	}

	@Test
	public void recieveInvalidInstructionTypeTest() {
		runExceptionTest("InstructionMessage E AB12 0 0 2016-12-03T09:16:00.012Z", "InstructionType was not valid");
	}

	@Test
	public void recieveInvalidProductCodeInvalidOrderTest() {
		runExceptionTest("InstructionMessage A 12AB 0 0 2016-12-03T09:16:00.012Z", "ProductCode was not valid");
	}

	@Test
	public void recieveInvalidProductCodeLowercaseTest() {
		runExceptionTest("InstructionMessage B ab13 0 0 2016-12-03T09:16:00.012Z", "ProductCode was not valid");
	}

	@Test
	public void recieveInvalidProductCodeWrongCharacterCountTest() {
		runExceptionTest("InstructionMessage C ABC13 0 0 2016-12-03T09:16:00.012Z", "ProductCode was not valid");
	}

	@Test
	public void recieveInvalidProductCodeWrongDigitCountTest() {
		runExceptionTest("InstructionMessage D AB123 0 0 2016-12-03T09:16:00.012Z", "ProductCode was not valid");
	}

	@Test
	public void recieveInvalidQuantityTest() {
		runExceptionTest("InstructionMessage A AB12 -1 0 2016-12-03T09:16:00.012Z", "Quantity was not valid");
	}

	@Test
	public void recieveNonNumericQuantityTest() {
		runExceptionTest("InstructionMessage A AB12 C 0 2016-12-03T09:16:00.012Z", "Quantity was not valid");
	}

	@Test
	public void recieveInvalidUOMTooLowTest() {
		runExceptionTest("InstructionMessage A AB12 1 -1 2016-12-03T09:16:00.012Z", "UOM was not valid");
	}

	@Test
	public void recieveInvalidUOMTooHighTest() {
		runExceptionTest("InstructionMessage A AB12 1 256 2016-12-03T09:16:00.012Z", "UOM was not valid");
	}

	@Test
	public void recieveNonNumericUOMTest() {
		runExceptionTest("InstructionMessage A AB12 1 D 2016-12-03T09:16:00.012Z", "UOM was not valid");
	}

	@Test
	public void recieveInvalidTimestampTest() {
		runExceptionTest("InstructionMessage A AB12 1 2 19/03/1992T12:13:45.323Z", "Timestamp was not valid");
	}

	@Test
	public void recieveInvalidTimestampPreEpochTest() {
		runExceptionTest("InstructionMessage A AB12 1 2 19/03/1992T12:13:45.323Z", "Timestamp was not valid");
	}

	@Test
	public void recieveInvalidTimestampFutureDateTest() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		String timestamp = format.format(calendar.getTime());

		runExceptionTest("InstructionMessage A AB12 1 2 " + timestamp, "Timestamp was not valid");
	}

	@Test
	public void recieveValidMessageTest() throws ParseException {
		ArgumentCaptor<InstructionMessage> messageCaptor = ArgumentCaptor.forClass(InstructionMessage.class);
		testInstance.recieve("InstructionMessage A AB12 1 2 2016­12­03T10:33:53.666Z");
		verify(mockInstructionQueue).enqueue(messageCaptor.capture());
		InstructionMessage message = messageCaptor.getValue();
		assertEquals(InstructionType.A, message.getInstructionType());
		assertEquals("AB12", message.getProductCode());
		assertEquals(Integer.valueOf(1), message.getQuantity());
		assertEquals(Integer.valueOf(2), message.getUOM());

		Date expectedTimestamp = format.parse("2016­12­03T10:33:53.666Z");

		assertEquals(expectedTimestamp, message.getTimestamp());
	}
}
