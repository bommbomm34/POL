import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class PolInterpreter {
    private static final Map<String, String> variables = new HashMap<>();
    private static int pleaseExpected = 0;
    private static int pleaseInRow = 0;
    private static int errors = 0;
    private static Random random = new Random();

    public static void main(String[] args) {
        if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print(">>> ");
                String output = interpret(scanner.nextLine(), true);
                System.out.print(output + (output.isEmpty() ? "" : "\n"));
            }
        } else if (args.length == 1) {
            String output = runFile(args[0]);
            System.out.println(output);
            exit(output.contains("ERROR: ") ? 1 : 0);
        } else {
            System.out.println(throwError("Not more than one or zero arguments expected."));
            exit(1);
        }
    }

    private static void exit(int status) {
        String message = "Exited with status " + status;
        System.out.println(status > 0 ? throwRedMessage(message) : message);
        System.exit(status);
    }

    private static String interpret(String line, boolean politenessRequired) {
        if (line.equals("please exit")) {
            exit(0);
        } else {
            try {
                return runCommand(line, politenessRequired);
            } catch (NumberFormatException ignored) {
                return throwError("Numbers are required as parameters");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return throwError("Internal error (probably nothing is wrong with your code)");
    }

    private static String checkAndFormatParameters(int count, String[] parameters, InputListener output) {
        return checkAndFormatParameters(count, parameters, output, true);
    }
    private static String checkAndFormatParameters(int count, String[] parameters, InputListener output,
                                                   boolean variableDefinable) {
        parameters = formatParameters(parameters);
        if (parameters.length >= 2 && parameters[parameters.length - 2].equals(">")) {
            variables.put(parameters[parameters.length - 1], output.getOutput(parameters));
            return "";
        } else if (count != parameters.length) {
            return throwError("Expected " + count + " parameter(s), but found " + parameters.length);
        } else {
            return output.getOutput(parameters);
        }
    }

    private static String[] formatParameters(String[] parameters) {
        String[] newParameters = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            newParameters[i] = formatParameter(parameters[i]);
        }
        return newParameters;
    }

    private static String formatParameter(String parameter) {
        String newParameter = parameter;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            newParameter = newParameter.replace("{" + entry.getKey() + "}",
                    entry.getValue());
        }
        return newParameter.replace("\\", "");
    }

    private static String calc(double x, double y, String operator) {
        return switch (operator) {
            case "+" -> formatNumber(x + y);
            case "-" -> formatNumber(x - y);
            case "*" -> formatNumber(x * y);
            case "/" -> formatNumber(x / y);
            case "%" -> formatNumber(x % y);
            case "==" -> String.valueOf(x == y);
            case "!=" -> String.valueOf(x != y);
            case "<=" -> String.valueOf(x <= y);
            case ">=" -> String.valueOf(x >= y);
            case "<" -> String.valueOf(x < y);
            case ">" -> String.valueOf(x > y);
            default -> throwError("Invalid operator");
        };
    }

    private static String formatNumber(double number) {
        String stringNumber = String.valueOf(number);
        return stringNumber.endsWith(".0") ? stringNumber.substring(0, stringNumber.length() - 2) : stringNumber;
    }

    private static String verifyPoliteness(String line) {
        if (line.startsWith("please")) {
            if (pleaseInRow == 2) {
                return throwError("Developer is too polite!");
            } else {
                pleaseExpected = 2;
                pleaseInRow++;
            }
        } else if (pleaseExpected == 0) {
            return throwError("Developer is too impolite!");
        } else {
            pleaseExpected--;
            pleaseInRow -= 2;
        }
        return "PERFECT";
    }

    private static String throwError(String error) {
        errors++;
        String errorOutput = "\u001B[31mERROR: " + error + "\u001B[0m";
        if (errors == 3) {
            System.out.println(errorOutput);
            System.out.println("\u001B[31mFATAL ERROR: I'm annoyed of your errors!" +
                    " \uD83D\uDE21 I'm an interpreter, not a teacher!\u001B[0m");
            exit(20);
            return "";
        } else {
            return errorOutput;
        }
    }
    private static String throwRedMessage(String message) {
        return "\u001B[31m" + message + "\u001B[0m";
    }
    private static String runCommand(String line, boolean politenessRequired) {
        String command = line.split(" ")[line.startsWith("please") ? 1 : 0];
        String[] splittedLineByCommand = line.split(command + " ");
        String parameter = splittedLineByCommand.length < 2 ? "" : splittedLineByCommand[1];
        String[] parameters = parameter.split(" ");
        String politeness = politenessRequired ? verifyPoliteness(line) : "PERFECT";
        if (politeness.equals("PERFECT")) {
            switch (command) {
                case "print":
                    String[] splittted = parameter.split(" > ");
                    if (splittted.length == 2) {
                        variables.put(splittted[1], splittted[0]);
                        return "";
                    } else {
                        return String.join(" ", formatParameters(splittted[0].split(" ")));
                    }
                case "calc":
                    return checkAndFormatParameters(3, parameters, new InputListener() {
                        @Override
                        public String getOutput(String[] parameters) {
                            return calc(Double.parseDouble(parameters[0]),
                                    Double.parseDouble(parameters[2]), parameters[1]);
                        }
                    });
                case "run":
                    return checkAndFormatParameters(1, parameters,
                            new InputListener() {
                                @Override
                                public String getOutput(String[] parameters) {
                                    return runFile(parameters[0]);
                                }
                            });
                case "prompt":
                    String promptMessage = parameter.split(parameters[0] + " ")[1];
                    Scanner scanner = new Scanner(System.in);
                    System.out.print(promptMessage);
                    variables.put(parameters[0], scanner.nextLine());
                    return "";
                case "rand":
                    return checkAndFormatParameters(1, parameters, new InputListener() {
                        @Override
                        public String getOutput(String[] parameters) {
                            return formatNumber(random.nextDouble(Double.parseDouble(parameters[0])));
                        }
                    });
                case "randi":
                    return checkAndFormatParameters(1, parameters, new InputListener() {
                        @Override
                        public String getOutput(String[] parameters) {
                            return String.valueOf(random.nextInt(Integer.parseInt(parameters[0])));
                        }
                    });
                default:
                    return throwError("Invalid command.");

            }
        } else {
            return politeness;
        }
    }

    private static String runFile(String path) {
        try {
            List<String> lines = Files.readAllLines(new File(path).toPath());
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < lines.size(); i++) {
                String outputLine = interpret(lines.get(i), true);

                if (outputLine.contains("ERROR: ")) {
                    return outputLine + " at line " + (i + 1);
                } else {
                    output.append(outputLine).append(
                            i + 1 == lines.size() || outputLine.isEmpty() ? "" : "\n");
                }
            }
            return output.toString();
        } catch (IOException e) {
            return throwError("File doesn't exist.");
        }
    }
}

interface InputListener {
    String getOutput(String[] parameters);
}
