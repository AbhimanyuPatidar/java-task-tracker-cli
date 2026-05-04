package com.tasktracker;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        TaskManager taskManager = new TaskManager();

        String command = args[0].toLowerCase();

        switch (command) {
            case "add":
                if (args.length < 2) {
                    System.out.println("Invalid number of arguments for 'add'. Expected at least 2, got " + args.length + ".");
                    printUsage();
                    return;
                }

                Status addStatus = null;
                int addDescriptionEnd = args.length;
                String addLastArg = args[args.length - 1];
                for (int i = 1; i < args.length - 1; i++) {
                    if (args[i].startsWith("--status=")) {
                        System.out.println("Invalid placement of --status flag. Use it as the last argument.");
                        printUsage();
                        return;
                    }
                }
                if (addLastArg.startsWith("--status=")) {
                    String addStatusValue = addLastArg.substring("--status=".length());
                    addStatus = Status.fromCliValue(addStatusValue);
                    if (addStatus == null) {
                        System.out.println("Invalid status value: " + addStatusValue + ". Use 1, 2, 3, todo, in-progress, or done.");
                        return;
                    }
                    addDescriptionEnd = args.length - 1;
                } else if (addLastArg.startsWith("--")) {
                    System.out.println("Unknown flag: " + addLastArg);
                    printUsage();
                    return;
                }

                StringBuilder addDescriptionBuilder = new StringBuilder();
                for (int i = 1; i < addDescriptionEnd; i++) {
                    if (addDescriptionBuilder.length() > 0) {
                        addDescriptionBuilder.append(" ");
                    }
                    addDescriptionBuilder.append(args[i]);
                }

                String addDescription = addDescriptionBuilder.toString().trim();

                addDescription = addDescription.replace("{", "\0").replace("}", "\7");

                if (addDescription.isEmpty()) {
                    System.out.println("Description cannot be empty for 'add'.");
                    printUsage();
                    return;
                }

                taskManager.add(addDescription, addStatus);
                break;

            case "update":
                if (args.length < 3) {
                    System.out.println("Invalid number of arguments for 'update'. Expected at least 3, got " + args.length + ".");
                    printUsage();
                    return;
                }

                int updateId;

                try {
                    updateId = Integer.parseInt(args[1]);
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid id for 'update': " + args[1] + ". Id must be a number.");
                    return;
                }

                Status updateStatus = null;
                int updateDescriptionEnd = args.length;
                String updateLastArg = args[args.length - 1];
                for (int i = 2; i < args.length - 1; i++) {
                    if (args[i].startsWith("--status=")) {
                        System.out.println("Invalid placement of --status flag. Use it as the last argument.");
                        printUsage();
                        return;
                    }
                }
                if (updateLastArg.startsWith("--status=")) {
                    String updateStatusValue = updateLastArg.substring("--status=".length());
                    updateStatus = Status.fromCliValue(updateStatusValue);
                    if (updateStatus == null) {
                        System.out.println("Invalid status value: " + updateStatusValue + ". Use 1, 2, 3, todo, in-progress, or done.");
                        return;
                    }
                    updateDescriptionEnd = args.length - 1;
                } else if (updateLastArg.startsWith("--")) {
                    System.out.println("Unknown flag: " + updateLastArg);
                    printUsage();
                    return;
                }

                StringBuilder updateDescriptionBuilder = new StringBuilder();
                for (int i = 2; i < updateDescriptionEnd; i++) {
                    if (updateDescriptionBuilder.length() > 0) {
                        updateDescriptionBuilder.append(" ");
                    }
                    updateDescriptionBuilder.append(args[i]);
                }

                String updateDescription = updateDescriptionBuilder.toString().trim();

                updateDescription = updateDescription.replace("{", "\0").replace("}", "\7");

                if (updateDescription.isEmpty()) {
                    System.out.println("Description cannot be empty for 'update'.");
                    printUsage();
                    return;
                }

                taskManager.update(updateId, updateDescription, updateStatus);
                break;

            case "delete":
                if (args.length != 2) {
                    System.out.println("Invalid number of arguments for 'delete'. Expected 2, got " + args.length + ".");
                    printUsage();
                    return;
                }

                try {
                    taskManager.delete(Integer.parseInt(args[1]));
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid id for 'delete': " + args[1] + ". Id must be a number.");
                    return;
                }
                break;

            case "mark-in-progress":
                if (args.length != 2) {
                    System.out.println("Invalid number of arguments for 'mark-in-progress'. Expected 2, got " + args.length + ".");
                    printUsage();
                    return;
                }

                try {
                    taskManager.markInProgress(Integer.parseInt(args[1]));
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid id for 'mark-in-progress': " + args[1] + ". Id must be a number.");
                    return;
                }
                break;

            case "mark-done":
                if (args.length != 2) {
                    System.out.println("Invalid number of arguments for 'mark-done'. Expected 2, got " + args.length + ".");
                    printUsage();
                    return;
                }

                try {
                    taskManager.markDone(Integer.parseInt(args[1]));
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid id for 'mark-done': " + args[1] + ". Id must be a number.");
                    return;
                }
                break;

            case "list":
                if (args.length != 1) {
                    System.out.println("Invalid number of arguments for 'list'. Expected 1, got " + args.length + ".");
                    printUsage();
                    return;
                }

                taskManager.listAll();
                break;

            case "list-done":
                if (args.length != 1) {
                    System.out.println("Invalid number of arguments for 'list-done'. Expected 1, got " + args.length + ".");
                    printUsage();
                    return;
                }

                taskManager.listDone();
                break;

            case "list-not-done":
                if (args.length != 1) {
                    System.out.println("Invalid number of arguments for 'list-not-done'. Expected 1, got " + args.length + ".");
                    printUsage();
                    return;
                }

                taskManager.listNotDone();
                break;

            case "list-in-progress":
                if (args.length != 1) {
                    System.out.println("Invalid number of arguments for 'list-in-progress'. Expected 1, got " + args.length + ".");
                    printUsage();
                    return;
                }

                taskManager.listInProgress();
                break;

            default:
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: task-cli <command>");
        System.out.println("Commands:");
        System.out.println("  add <description> [--status=<value>]");
        System.out.println("  update <id> <description> [--status=<value>]");
        System.out.println("  delete <id>");
        System.out.println("  mark-in-progress <id>");
        System.out.println("  mark-done <id>");
        System.out.println("  list");
        System.out.println("  list-done");
        System.out.println("  list-not-done");
        System.out.println("  list-in-progress");
        System.out.println("Status values: 1 | 2 | 3 | todo | in-progress | done");
        
    }
}