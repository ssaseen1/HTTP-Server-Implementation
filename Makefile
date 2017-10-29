r1:r2 r3
	jar -cfe server.jar Server Server.class ConnectionHandler.class
r2:Server.java
	javac Server.java
r3:ConnectionHandler.java
	javac ConnectionHandler.java
clean :
	rm -f *.class *.jar