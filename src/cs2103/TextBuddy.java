package cs2103;

/**
 * This class is a Command Line Interface program that store, retrieve and
 * delete text information in a file. The text will be changed every time
 * the user modifies it.
 * 
 * Below is a sample interaction with the program:
 * 
 Welcome to TextBuddy. 
 mytextfile.txt is ready for use 
 command: add little brown fox 
 added to mytextfile.txt: “little brown fox” 
 command: display 
 1.little brown fox command: add jumped over the moon 
 added to mytextfile.txt: “jumped over the moon” 
 command: display 
 1. little brown fox 
 2. jumped over the moon 
 command: delete 2 
 deleted from mytextfile.txt: “jumped over the moon” 
 command: display 
 1. little brown fox 
 command: clear all content deleted from mytextfile.txt 
 command: display 
 mytextfile.txt is empty 
 command: exit
 * 
 * @author Yucca
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextBuddy {
	
	// These are different message formats
	private static final String MESSAGE_ADDED = "added to %1$s: '%2$s'";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_NO_SUCH_LINE = "there is no such line :%1$s";
	private static final String MESSAGE_DELETED = "deleted from %1$s: '%2$s'";
	private static final String MESSAGE_EMPTY_FILE = "%1$s is empty";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";

	// These are the correct number of parameters for each command
	private static final int PARAM_SIZE_FOR_DELETE_LINE = 1;

	// This arrayList will be used to store the text
	private static ArrayList<String> contentList = new ArrayList<String>();

	// These variables are declared for the whole class for input/output
	private static Scanner scanner = new Scanner(System.in);
	private static PrintStream printer = System.out;
	public static String m_fileName = null; 
	private static File m_file = null;
	private static BufferedWriter writer;
	private static BufferedReader reader;

	// These are the possible command types
	enum COMMAND_TYPE {
		ADD_LINE, DELETE_LINE, DISPLAY, CLEAR, SORT, SEARCH, INVALID, EXIT
	};


	/**
	 * high level logic of the program
	 * @param args
	 */
	public static void main(String[] args) {
		isValidInputs(args);
		showToUser(String.format(MESSAGE_WELCOME, m_fileName));
		createFile();
		run();
	}
	
	/**
	 * This is the run loop of the program.
	 */
	private static void run() {
		while (true) {
			System.out.print("command:");
			String userCommand = scanner.nextLine();
			String feedback = executeCommand(userCommand);
			showToUser(feedback);
		}
	}

	/**
	 * This is to create the output file. If the file already exists, it will write 
	 * on the existing file.
	 */
	public static void createFile() {
		try {
			m_file = new File(m_fileName);
			if (!m_file.exists()) {
				m_file.createNewFile();
			}
			reader = new BufferedReader(new FileReader(m_file));
			readTextOut();
			writer = new BufferedWriter(new FileWriter(m_file));
		} catch (IOException e) {
			showToUser(e.getMessage());
		}
	}
	
	public static void createNewFile() {
		try {
			clear();
			m_file = new File(m_fileName);
			m_file.createNewFile();
			reader = new BufferedReader(new FileReader(m_file));
			readTextOut();
			writer = new BufferedWriter(new FileWriter(m_file));
		} catch (IOException e) {
			showToUser(e.getMessage());
		}
	}

	/**
	 * This is to check whether the input that user enters is valid.
	 * @param inputs
	 */
	private static void isValidInputs(String[] inputs) {
		if (inputs.length == 0) {
			throwError("you should specify an output file");
		} 
		else if (inputs.length > 1) {
			throwError("you should specify only one output file");
		} 
		else if (!isValidFileName(inputs[0])) {
			throwError("this is not a valid txt file name");
		} 
		else {
			m_fileName = inputs[0];
		}
	}

	/**
	 * this is to print the text to the user.
	 * @param text
	 */
	private static void showToUser(String text) {
		System.out.println(text);
	}

	/**
	 * This is to execute one command.
	 * @param userCommand
	 * @return
	 */
	public static String executeCommand(String userCommand) {
		if (isEmptyCommand(userCommand)) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		String commandTypeString = getFirstWord(userCommand);
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD_LINE:
			return addLine(userCommand);
		case DELETE_LINE:
			return deleteLine(userCommand);
		case DISPLAY:
			return display();
		case CLEAR:
			return clear();
		case INVALID:
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		case EXIT:
			exit();
		default:
			// throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
	}

	/**
	 * This method read out the text in txt file and save it in the content list
	 */
	private static void readTextOut() {
		boolean isEnded = false;
		
		try{
			if (reader != null) {
				while (!isEnded) {
					String nextLine = reader.readLine();
					if (nextLine == null) {
						isEnded = true;
					}
					else {
						contentList.add(nextLine);
					}
				}
			}
		} catch (IOException e) {
			showToUser(e.getMessage());
		}
	}
	
	/**
	 * This is to check whether the file name entered by the user is valid.
	 * @param fileName
	 * @return
	 */
	private static boolean isValidFileName(String fileName) {
		if (fileName.length() < 4) {
			return false;
		} else {
			// get the last four letters, which is the extension name of the 
			// file name
			String extension = fileName.substring(fileName.length() - 4);
			String capitalizedExtension = extension.toUpperCase();
			if (capitalizedExtension.equals(".TXT")) {
				return true;
			} 
			else {
				return false;
			}
		}
	}

	/**
	 * This is to print out the error and terminate the program.
	 * @param errorMessage
	 */
	private static void throwError(String errorMessage) {
		printer.println(errorMessage);
		System.exit(1);
	}

	/**
	 * This is to check whether a command is empty.
	 * @param userCommand
	 * @return
	 */
	private static boolean isEmptyCommand(String userCommand) {
		return userCommand.trim().isEmpty();
	}

	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * 
	 * @param commandTypeString
	 *            is the first word of the user command
	 */
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD_LINE;
		} 
		else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE_LINE;
		} 
		else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} 
		else if (commandTypeString.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} 
		else if (commandTypeString.equalsIgnoreCase("exit")) {
			return COMMAND_TYPE.EXIT;
		} 
		else {
			return COMMAND_TYPE.INVALID;
		}
	}

	/**
	 * This method adds a new text line to the file
	 * @param userCommand
	 * @return
	 */
	private static String addLine(String userCommand) {
		String content = removeFirstWord(userCommand);
		contentList.add(content);
		save();

		return String.format(MESSAGE_ADDED, m_fileName, content);
	}

	/**
	 * This method deletes a specific text line from the file
	 * @param userCommand
	 * @return
	 */
	private static String deleteLine(String userCommand) {
		String[] parameters = splitParameters(removeFirstWord(userCommand));

		if (parameters.length != PARAM_SIZE_FOR_DELETE_LINE) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		// parse string to number
		String lineString = parameters[0];
		if (!isParsable(lineString)) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		int line = Integer.parseInt(lineString);
		// the index of a line in the array should be line number - 1
		int index = line - 1;
		if (index < 0 || index >= contentList.size()) {
			return String.format(MESSAGE_NO_SUCH_LINE, lineString);
		}

		String toBeDeleted = contentList.get(index);
		contentList.remove(index);
		save();
		
		return String.format(MESSAGE_DELETED, m_fileName, toBeDeleted);
	}

	/**
	 * This method checks whether a string is able to be parsed to an integer
	 * @param input
	 * @return
	 */
	public static boolean isParsable(String input) {
		boolean parsable = true;
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException e) {
			parsable = false;
		}
		return parsable;
	}

	/**
	 * This method display all the text lines in the file
	 * @return
	 */
	private static String display() {
		int size = contentList.size();

		if (size < 1) {
			return String.format(MESSAGE_EMPTY_FILE, m_fileName);
		}

		// append all the contents stored in the array list
		String displayContent = "";
		for (int i = 0; i < size - 1; i++) {
			int index = i + 1;
			displayContent += index + "." + contentList.get(i) + "\n";
		}
		// the last line does not have "\n"
		displayContent += size + "." + contentList.get(size - 1);

		return displayContent;
	}

	/**
	 * This method clear all the text lines in the file.
	 * @return
	 */
	private static String clear() {
		contentList = new ArrayList<String>();
		save();
		return String.format(MESSAGE_CLEAR, m_fileName);
	}

	
	/**
	 * This method exits the program.
	 */
	private static void exit() {
		save();
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			showToUser(e.getMessage());
		}
		System.exit(0);
	}

	/**
	 * This method saves the current operation and updates the file
	 */
	private static void save() {
		try {
			writer = new BufferedWriter(new FileWriter(m_file));
			for (int i = 0; i < contentList.size(); i++) {
				writer.write(contentList.get(i) + "\n");
			}
		} catch (IOException e) {
			showToUser(e.getMessage());
		}
	}

	
	
	/**
	 * This method removes the first word from a string
	 * @param userCommand
	 * @return
	 */
	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	/**
	 * This method gets the first word from a string
	 * @param userCommand
	 * @return
	 */
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	/**
	 * This method split the parameters into an array of strings
	 * @param commandParametersString
	 * @return
	 */
	private static String[] splitParameters(String commandParametersString) {
		String[] parameters = commandParametersString.trim().split("\\s+");
		return parameters;
	}

}
