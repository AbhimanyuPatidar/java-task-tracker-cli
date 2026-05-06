
# Java Task Tracker CLI

A command-line task management application built in Java.

## Purpose

This project helps you manage personal tasks quickly from the terminal without needing a GUI app.
It is designed to be simple, predictable, and fast for day-to-day task tracking.

The goal is to provide a practical CLI experience while keeping the implementation clean:
- Positional command arguments for quick terminal usage.
- File-based persistence so your tasks survive restarts.
- No external libraries, so core Java concepts are used end-to-end.

## What It Does

The CLI supports the full task workflow:
- Add a task, optionally with initial status.
- Update task description and optionally status.
- Delete a task.
- Mark a task as in-progress or done.
- List all tasks.
- List tasks by status filter (`done`, `todo`, `in-progress`).

## How It Works

### Storage Model

- Tasks are stored in `tasks.json` in the current project directory.
- If `tasks.json` does not exist, it is created automatically on first run.
- Persistence uses Java native filesystem APIs (`java.nio.file`).

### Command Style

- All input is accepted through positional arguments.
- Multi-word descriptions should be wrapped in double quotes.

## How To Execute Commands

The command definitions below (for example, `add <description> [status]`) describe only the arguments passed to the application.

You can run those commands in two ways:

### Beginner Starter Kit (3 quick commands)

If you are running this project for the first time, use these commands from project root:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='add "My first task"'
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='list'
```

### Option A: Run With Maven (recommended)

Use Maven to compile and execute in one step:

```bash
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='add "Buy groceries"'
```

In this case, the app receives only:

```bash
add "Buy groceries"
```

### Option B: Compile And Run Manually (without Maven)

#### 1) Compile source files

From project root:

```bash
javac -d target/classes src/main/java/com/tasktracker/*.java
```

#### 2) Run with classpath

Windows (PowerShell/CMD):

```bash
java -cp target/classes com.tasktracker.Main add "Buy groceries"
java -cp target/classes com.tasktracker.Main list done
```

Linux/macOS/Git Bash:

```bash
java -cp target/classes com.tasktracker.Main add "Buy groceries"
java -cp target/classes com.tasktracker.Main list done
```

Note: For this project (single package, no external dependencies), the classpath points to the compiled output folder only (`target/classes`).

## Setup and Run

### Prerequisites

- Java 17+ (or your configured project Java version)
- Maven

### Compile

```bash
mvn compile
```

### Run

```bash
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args="<command>"
```

## Commands

### Add

```bash
add <description> [status]
```

Examples:

```bash
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='add "Buy groceries"'
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='add "Write report" in-progress'
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='add "Read book" done'
```

### Update

```bash
update <id> <description> [status]
```

Examples:

```bash
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='update 1 "Buy groceries and fruits"'
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='update 2 "Write final report" done'
```

### Delete

```bash
delete <id>
```

Example:

```bash
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='delete 3'
```

### Mark In Progress

```bash
mark-in-progress <id>
```

Example:

```bash
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='mark-in-progress 1'
```

### Mark Done

```bash
mark-done <id>
```

Example:

```bash
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='mark-done 1'
```

### List

```bash
list [done|todo|in-progress]
```

Examples:

```bash
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='list'
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='list done'
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='list todo'
mvn exec:java -Dexec.mainClass=com.tasktracker.Main -Dexec.args='list in-progress'
```

## Status Values

Where status is accepted, you can use:
- `todo` or `1`
- `in-progress` or `2`
- `done` or `3`

## Error Handling Notes

The CLI handles common edge cases gracefully:
- Unknown commands show usage guidance.
- Invalid IDs show clear numeric validation messages.
- Missing required arguments show command-specific usage errors.
- Not-found task operations return informative messages.