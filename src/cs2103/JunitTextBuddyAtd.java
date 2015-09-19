package cs2103;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import org.junit.Test;

public class JunitTextBuddyAtd {
	private static final String TEST_FILENAME = "test.txt";	

	@Test
	public void testSort() {
		TextBuddy.m_fileName = TEST_FILENAME;
		TextBuddy.createNewFile();
		
		ArrayList<String> commandList = addCommands();
		Collections.sort(commandList);
		String displayContent = displayList(commandList);
		
		TextBuddy.executeCommand("sort");
		TextBuddy.executeCommand("display");
		
		assertEquals(TextBuddy.executeCommand("display"), displayContent);
	}
	
	@Test
	public void testSearch1() {
		TextBuddy.m_fileName = TEST_FILENAME;
		TextBuddy.createNewFile();
		
		ArrayList<String> commandList = addCommands();
		ArrayList<String> filteredList = filterKeyword(commandList ,"is");
		String displayContent = displayList(filteredList);
		
		assertEquals(TextBuddy.executeCommand("search is"), displayContent);
	}
	
	@Test
	public void testSearch2() {
		TextBuddy.m_fileName = TEST_FILENAME;
		TextBuddy.createNewFile();
		
		ArrayList<String> commandList = addCommands();
		ArrayList<String> filteredList = filterKeyword(commandList ,"-");
		String displayContent = displayList(filteredList);
		
		assertEquals(TextBuddy.executeCommand("search -"), displayContent);
	}
	
	
	public ArrayList<String> addCommands() {
		ArrayList<String> commandList = new ArrayList<String>();
		String command1 = "add this is a string";
		String command2 = "add another string";
		String command3 = "add try another string";
		commandList.add("this is a string");
		commandList.add("another string");
		commandList.add("try another string");
		TextBuddy.executeCommand(command1);
		TextBuddy.executeCommand(command2);
		TextBuddy.executeCommand(command3);
		
		// generate a random add command
		for (int i=0; i<10; i++) {
			String randomString = UUID.randomUUID().toString();
			String command = String.format("add %1$s", randomString);
			commandList.add(randomString);
			TextBuddy.executeCommand(command);
		}
		
		return commandList;
	}
	
	public String displayList(ArrayList<String> list) {
		int size = list.size();
		String displayContent = "";
		for (int i = 0; i < size - 1; i++) {
			int index = i + 1;
			displayContent += index + "." + list.get(i) + "\n";
		}
		// the last line does not have "\n"
		displayContent += size + "." + list.get(size - 1);
		return displayContent;
	}
	
	private ArrayList<String> filterKeyword(ArrayList<String> target, String keyword) {
		ArrayList<String> result = new ArrayList<String>();
		for (String element: target) {
			if (element.contains(keyword)) {
				result.add(element);
			}
		}
		return result;
	}
}
