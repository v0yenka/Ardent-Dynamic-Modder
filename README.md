# Ardent: Project Edge Dynamic Modder

A lightweight, high-performance Java Agent developed as part of the **School of Dragons (Project Edge)** community restoration effort. 

This tool utilizes dynamic JVM instrumentation to inject custom modifications (gameplay tweaks, bug fixes, and feature unlocks) directly into the server's bytecode at runtime. It completely eliminates the need to permanently alter or recompile the core server `.jar` files.

> **Note:** This is currently a **Test Release (v1.0-beta)**. The exact class paths targeted by this agent are based on early reverse engineering of the server structure and may require updates as the emulator evolves.

## The Restoration & Reverse Engineering Process
During the restoration of the School of Dragons game files, we needed a way to test server capabilities, bypass missing authorization checks, and fix unstable code loops that caused server crashes—all without breaking the original bytecode.

By using tools like **JD-GUI** and **Bytecode Viewer**, I reverse-engineered the core server logic to identify critical classes and methods (e.g., `ExperienceManager`, `PlayerProfile`). This agent intercepts these classes via the `java.lang.instrument` API just before they are loaded into RAM, injecting Javassist-compiled Java syntax directly into the workflow.

## Features
* **Global XP Multiplier:** Dynamically intercepts experience allocation and multiplies it by a configurable value.
* **Unlock All Dragons:** Forces authorization checks to bypass paywalls/progression locks.
* **Crash Prevention (Bug Fixes):** Overrides faulty loops or corrupted packet handling methods to prevent server downtime.
* **Zero-Downtime Injection:** Modifies behavior entirely in memory.
* **External Configuration:** Toggle features via `config.json`.

## Configuration (`config.json`)
The agent reads operational parameters from a JSON file in the working directory:
```json
{
  "serverVersion": "Project Edge v1.0 (School of Dragons Community)",
  "cheats": {
    "infiniteStamina": true,
    "noFallDamage": false,
    "xpMultiplier": 10,
    "unlockAllDragons": true,
    "patchKnownBugs": true
  }
}
```

## How to Attach to the Server
This program does not run standalone. It must be attached to the Java Virtual Machine (JVM) as an agent during the startup of the target application or server.

### Live Server (Terminal)
This is the standard method for attaching the modder to a running Project Edge server instance.
1. Locate your compiled agent `.jar` (e.g., `Ardent-1.0-SNAPSHOT.jar`).
2. Place the agent `.jar` and the `config.json` file in the same directory as your main server `.jar`.
3. Append the `-javaagent` flag to your standard JVM startup command:

```bash
java -javaagent:Ardent-1.0-SNAPSHOT.jar -jar ProjectEdgeServer.jar
```
## Disclaimer
This project relies on the Javassist library for dynamic bytecode manipulation. It implements the ClassFileTransformer interface from the java.lang.instrument API, acting as a middleman between the JVM's class loader and the server's core logic. Target classes are intercepted and logically rewritten directly in the RAM just milliseconds before they are initialized by the server.

Ardent is built strictly for educational research and the preservation of abandoned online games within privately hosted, open-source emulator environments. It does not interact with any official retail game servers.
