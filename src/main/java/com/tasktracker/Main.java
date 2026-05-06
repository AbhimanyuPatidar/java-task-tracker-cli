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
                addStatus = Status.fromCliValue(addLastArg);
                if (addStatus != null) {
                    addDescriptionEnd = args.length - 1;
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
                updateStatus = Status.fromCliValue(updateLastArg);
                if (updateStatus != null) {
                    updateDescriptionEnd = args.length - 1;
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
                if (args.length == 1) {
                    taskManager.listAll();
                } else if (args.length == 2) {
                    String filter = args[1].toLowerCase();
                    switch (filter) {
                        case "done":
                            taskManager.listDone();
                            break;
                        case "todo":
                            taskManager.listTodo();
                            break;
                        case "in-progress":
                            taskManager.listInProgress();
                            break;
                        default:
                            System.out.println("Unknown list filter: " + args[1]);
                            printUsage();
                    }
                } else {
                    System.out.println("Invalid number of arguments for 'list'. Expected 1 or 2, got " + args.length + ".");
                    printUsage();
                }
                break;

            default:
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: task-cli <command>");
        System.out.println("Commands:");
        System.out.println("  add <description> [status]");
        System.out.println("  update <id> <description> [status]");
        System.out.println("  delete <id>");
        System.out.println("  mark-in-progress <id>");
        System.out.println("  mark-done <id>");
        System.out.println("  list [done|todo|in-progress]");
        System.out.println("Status values: 1 | 2 | 3 | todo | in-progress | done");
        System.out.println("Note: Use double quotes for multi-word descriptions.");
        
    }
}