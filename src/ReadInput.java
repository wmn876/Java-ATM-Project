package com.calculator.reader;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadInput {

    private final String regex = "([-+]?[0-9]*\\.?[0-9]+[\\/\\+\\-\\*])+([-+]?[0-9]*\\.?[0-9]+)";

    public ReadInput() {}

    public boolean validateInput(String input) {
        Pattern pattern = Pattern.compile(this.regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public static String read() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Input expression (for eg: 4*3/2)");

        String inputLine = scanner.nextLine();

        ReadInput readInput = new ReadInput();

        if(!readInput.validateInput(inputLine)) {
            System.out.println("Mathematical expression validation failed");
            System.exit(0);
        }

        return inputLine;
    }
}


/**************End of Second Package **************/


/**************Start of Third Package **************/


package com.calculator.operations;

public interface Operate {
    Double getResult(Double... numbers);
}
