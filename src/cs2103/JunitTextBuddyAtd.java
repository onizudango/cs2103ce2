package cs2103;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;

public class JunitTextBuddyAtd {
	private static final String TEST_FILENAME = "test.txt";	
	private PrintStream console;
	private ByteArrayOutputStream bytes;
	
	private static final String MESSAGE_ADDED = "added to %1$s: '%2$s'";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_NO_SUCH_LINE = "there is no such line :%1$s";
	private static final String MESSAGE_DELETED = "deleted from %1$s: '%2$s'";
	private static final String MESSAGE_EMPTY_FILE = "%1$s is empty";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	
	/*
	public void testRun() {
		setUp();
		tearDown();
		testAddNormal();
		testDisplayNormal();
		testBadDelete();
		testGoodDelete();
		testClear();
	}
	*/
	
	@Test
	public void testAdd() {
		setUp();
		tearDown();
		testAddNormal();
	}
	
	@Test
	public void testDisplay() {
		testDisplayNormal();
	}
	
	public void setUp() {
		bytes = new ByteArrayOutputStream();
		console = System.out;
		System.setOut(new PrintStream(bytes));
	}
	
	public void tearDown() {
		System.setOut(console);
	}
	
	/*
	public void welcome() {
		String[] arguments = {TEST_FILENAME};	
		TextBuddy.main(arguments);
		assertEquals(String.format(MESSAGE_WELCOME, TEST_FILENAME), bytes.toString());
	}
	/*
	
	/*
	public void testIncorrectFileName() {
		String[] arguments = {"hehe"};		
		TextBuddy.main(arguments);
	}
	*/
	
	public void testAddNormal() {
		TextBuddy.m_fileName = TEST_FILENAME;
		TextBuddy.createFile();
		String command = "add little brown fox";
		String content = "little brown fox";
		String expected = TextBuddy.executeCommand(command);
		assertEquals(String.format(MESSAGE_ADDED, TEST_FILENAME, content), expected);
	}
	
	public void testDisplayNormal() {
		TextBuddy.m_fileName = TEST_FILENAME;
		System.out.println(TextBuddy.m_fileName);
		String command = "display";
		String content = "1.little brown fox";
		String expected = TextBuddy.executeCommand(command);
		
		System.out.println(expected);
		assertEquals(content, expected);
	}
	
	public void testBadDelete() {
		String command = "delete 2";
		String content = String.format(MESSAGE_NO_SUCH_LINE, "2");
		String expected = TextBuddy.executeCommand(command);
		assertEquals(content, expected);
	}
	
	public void testGoodDelete() {
		String command = "delete 1";
		String content = String.format(MESSAGE_DELETED, TEST_FILENAME, "little brown fox");
		String expected = TextBuddy.executeCommand(command);
		assertEquals(content, expected);
	}
	
	public void testClear() {
		String command = "clear";
		String content = String.format(MESSAGE_CLEAR, TEST_FILENAME);
		String expected = TextBuddy.executeCommand(command);
		
		System.out.println(expected);
		System.out.println(content);
		assertEquals(content, expected);
	}
}
