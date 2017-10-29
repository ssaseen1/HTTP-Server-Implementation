Programming language: JAVA Socket Programming

The Server will receive the HTTP Request from the client with a file request.
The HTTP response is sent only in the case if the file is located in the www directory. The directory is placed in the HTTP server executable.
If the file is not found in the directory, it gives an error message as 404 not found.
If the client request does not include any file request, e.g. hostname54040, it gives an error message. 

The implementation also deals with multiple directories in a parent directory. It handles directory namefile name with whitespaces, but the white space needs to be replaced with a %20 in the wget command itself.

Multithreading is implemented to handle multiple client requests.

File access time is recorded, which would give the number of times a file is accessed since the server is up.

Example
Server
Server host name remote07.cs.binghamton.edu  Port 45754
skype-ubuntu-precise_4.3.0.37-1_i386.deb128.226.180.1621
skype-ubuntu-precise_4.3.0.37-1_i386.deb128.226.180.1642
cylinder.wrl128.226.180.1641
cylinder.wrl128.226.180.1642
cylinder.wrl128.226.180.1623

Client
wget httpremote07.cs.binghamton.edu45754cylinder.wrl






