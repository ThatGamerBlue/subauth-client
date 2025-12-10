# SubAuth Client
Prerequisites
---
To build this project, you will need:
- A copy of Java 7 where Gradle can find it, usually %home%/.jdks
- A copy of Java 11 where Gradle can find it.
- A copy of Java 21 to run Gradle with.

\- OR -
- A copy of Java 21 to run Gradle with.
- You will need to pass -PmodernBuild to Gradle, to use Java 21 to build all subprojects 

Backports
---
Some versions on the supported list don't have released versions, this is because going back and creating builds
for every version I intend to support would be a long process. If you need a build for a supported version, create a GitHub
issue, and I will backport the mod and create a build for you.

Version Policy
---
I will support the last patch version of every major feature/game drop version  
For example:

| Version Name    | Version Number | Supported? |
|-----------------|----------------|------------|
| The Copper Age  | 1.21.10        | ✅          |
| The Copper Age  | 1.21.9         | ❌          |
| Chase the Skies | 1.21.8         | ✅          |
| Chase the Skies | 1.21.7         | ❌          |
| Chase the Skies | 1.21.6         | ❌          |
| Spring to Life  | 1.21.5         | ✅          |

Note this table is not complete, just an example of the types of releases I will support.