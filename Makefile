JFLAGS = -g
JC = javac
J = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/Exercise1.java \
	src/Exercise2.java \
	src/Exercise3.java \
	src/TestExercise1.java \
	src/TestExercise2.java \
	src/TestExercise3.java
	
default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) src/*.class

test: clean classes
	$(J) -ea src/TestExercise1 && $(J) -ea src/TestExercise2 && $(J) -ea src/TestExercise3


