
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#folder-structure">Folder structure</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#big-o-notation">Big O notation</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

Coding challenge. 

It has been solved using java version 17. It has not been tested with any other java version and there is no guarantee
it could work with other java versions. 

There are no external frameworks or libraries dependencies. The reason of this decision is based on the interview from 
8.9.2022, where my understanding was that there are many custom services developed in-house instead of using third party
tools, therefore no external libraries for testing or whatsoever reasons.

<!-- GETTING STARTED -->
## Getting Started
### Prerequisites
To execute the code the java version 17 should be installed in your computer. Most probably will work with other java 
version, but it has not been tested and there is no guarantee.

A Makefile is provided to build the source files. You might need to install "make" in your local computer to use the 
Makefile. The make version used for this project is GNU Make 3.81 and there is no guarantee it works with other make
versions. The make command has been only used in unix based machines, there is no guarantee it will work with windows
based machines.

### Folder structure
- A Readme.md file, the file you are reading.

- The code is stored in the src folder. It contains two files per exercise, the code and the test.

- A Makefile is provided to build the java source files. If you compile the code with "make", the .class files will be 
stored in the src folder as well.

- The folder testFile contains text documents for testing the Exercise3 code.


<!-- USAGE EXAMPLES -->
## Usage
Open a terminal and navigate to the root directory. From now on we assume your terminal has a working directory the root
directory.

- You can "compile" the java code with:
   ```sh
   make
   ```

- You can delete the binary files created with "compile" with:
   ```sh
   clean
   ```

- You can run all the tests with:
   ```sh
   make test
   ```

- Assuming you have compiled first the code, you can run a single tests with the following commands:
   ```sh
   java -ea src.TestExercise1
   ```
   or
   ```sh
   java -ea src.TestExercise2
   ```
   or
   ```sh
   java -ea src.TestExercise3
   ```

- Assuming you have compiled first the code, you can run the code for Exercise1 with the following command:
   ```sh
   java src.Exercise1 "Atar a la rata"
   ```
   or if it is a single word, you can remove the quotations marks:
   ```sh
   java src.Exercise1 mom
   ```

- Assuming you have compiled first the code, you can run the code for Exercise2 with the following format:
  ```sh
    java src.Exercise2 5 2 3 4 1
   ```
  example for finding the 5 complementary of [2,3,4,1]:
   ```sh
   java src.Exercise2 5 2 3 4 1
   ```

- Assuming you have compiled first the code, you can run the code for Exercise3 with the following format:

   ```sh
   java src.Exercise3 directory N P "terms"
   ```
   example for printing the first three positions every 5 seconds:
   ```sh
   java src.Exercise3 testFolder 3 5 terms
   ```
  
<!-- CODE COMPLEXITY -->
## Big O notation
The complexity is explained in the source code with comments.
